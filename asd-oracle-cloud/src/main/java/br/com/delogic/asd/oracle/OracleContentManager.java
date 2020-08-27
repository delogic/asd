package br.com.delogic.asd.oracle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import javax.activation.MimetypesFileTypeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;

import br.com.delogic.asd.content.ContentManager;
import br.com.delogic.asd.content.ContentZipEntry;
import br.com.delogic.asd.exception.UnexpectedApplicationException;
import br.com.delogic.jfunk.Convert;
import br.com.delogic.jfunk.Converter;
import br.com.delogic.jfunk.Has;

public class OracleContentManager implements ContentManager {

    private final String cdn;
    private final String bucket;
    private final String endpoint;
    private final String namespace;
    private final ObjectStorage client;
    private final Iterator<? extends Object> iterator;

    private boolean cache = true;
    private final String temp = "/tmp/";
    private final MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
    private static final Logger logger = LoggerFactory.getLogger(ContentManager.class);

    public OracleContentManager(AuthenticationDetailsProvider authenticationDetailsProvider,
        Iterator<? extends Object> iterator, String endpoint, String bucket, String namespace) {
        if (!Has.content(endpoint) || !endpoint.startsWith("http")) {
            throw new IllegalArgumentException("Endpoint must be a full URL containing the protocol starting by http");
        }
        this.bucket = bucket;
        this.endpoint = endpoint;
        this.iterator = iterator;
        this.namespace = namespace;
        this.client = new ObjectStorageClient(authenticationDetailsProvider);

        cdn = String.format("%s/n/%s/b/%s/o/", endpoint, namespace, bucket);
    }

    @Override
    public String create(InputStream inputStream, String fileName) {
        fileName = getRealFileName(fileName);
        String val = iterator.next().toString();
        String newFileName = "file" + val + "." + getFileExtension(fileName);

        putObject(inputStream, newFileName);

        return newFileName;
    }

    @Override
    public void update(InputStream inputStream, String fileName) {
        putObject(inputStream, fileName);
    }

    @Override
    public String get(String name) {
        return !Has.content(name) ? "" : cdn + name;
    }

    @Override
    public InputStream getInpuStream(String name) throws Exception {
        GetObjectResponse response = client.getObject(
            GetObjectRequest.builder()
                .bucketName(bucket)
                .namespaceName(namespace)
                .objectName(name)
                .build());

        return response.getInputStream();
    }

    @Override
    public String createZip(ContentZipEntry... contentZipEntries) {
        validateFiles(contentZipEntries);

        ZipOutputStream zos = null;
        FileOutputStream fos = null;
        String zipFileName = null;
        String absoluteFileName = "";

        try {
            zipFileName = iterator.next().toString() + ".zip";
            absoluteFileName = temp + File.separatorChar + zipFileName;
            fos = new FileOutputStream(absoluteFileName);
            zos = new ZipOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;

            for (ContentZipEntry contentZipEntry : contentZipEntries) {
                zos.putNextEntry(contentZipEntry);
                InputStream in = contentZipEntry.getInputStream();
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
                zos.closeEntry();
            }

        } catch (Exception e) {
            throw new UnexpectedApplicationException("Exception when creating a zip file with entries "
                + Arrays.toString(contentZipEntries), e);
        } finally {
            try {
                zos.close();
                fos.close();
                FileInputStream fis = new FileInputStream(absoluteFileName);

                putObject(fis, zipFileName);

            } catch (IOException e) {
                throw new UnexpectedApplicationException("Exception when closing the zip file with entries "
                    + Arrays.toString(contentZipEntries), e);
            }
        }

        return zipFileName;

    }

    @Override
    public String commit(String name) {
        return name;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public String getCdn() {
        return cdn;
    }

    public String getBucket() {
        return bucket;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getNamespace() {
        return namespace;
    }

    public Iterator<? extends Object> getIterator() {
        return iterator;
    }

    private void putObject(InputStream inputStream, String fileName) {
        PutObjectRequest put = createMetadata(PutObjectRequest.builder(), fileName, inputStream)
            .bucketName(bucket)
            .namespaceName(namespace)
            .objectName(fileName)
            .putObjectBody(inputStream)
            .build();

        client.putObject(put);

        tentarFechar(inputStream);

    }

    private String getFileExtension(String fileName) {
        return Has.content(fileName) && fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
    }

    private void tentarFechar(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (Exception e) {
            logger.debug("erro ao tentar fechar input stream, talvez já esteja fechado:" + e.getMessage());
        }
    }

    protected PutObjectRequest.Builder createMetadata(PutObjectRequest.Builder builder, String fileName, InputStream inputStream) {
        Map<String, String> metadata = new HashMap<>();

        builder.contentType(typeMap.getContentType(fileName));
        if (cache) {
            metadata.put("Last-Modified", formatDate(new Date(), "EEE, dd MMM yyyy HH:mm:ss z"));
            builder.cacheControl("max-age=31536000");
        }
        if (inputStream instanceof FileInputStream) {
            FileInputStream fis = (FileInputStream) inputStream;
            try {
                builder.contentLength(fis.getChannel().size());
            } catch (IOException e) {
                logger.error("erro ao tentar ler content length do arquivo para evitar OOM no envio", e);
            }
        } else {
            // definir como deverá ser feito, talvez será necessário escrever o
            // arquivo para o filesystem para ler a partir de File.
        }

        builder.opcMeta(metadata);

        return builder;
    }

    private String getRealFileName(String fileName) {
        return fileName.replace(cdn, "");
    }

    private void validateFiles(ContentZipEntry[] contentZipEntries) {
        NO_CONTENT_TO_ZIP.thrownIf(!Has.content(contentZipEntries));
        Set<String> fileNames = Convert.from(contentZipEntries).toSetOf(new Converter<ContentZipEntry, String>() {
            @Override
            public String to(ContentZipEntry in) {
                return in.getName();
            }
        });
        REPEATED_FILE_NAMES.thrownIf(fileNames.size() < contentZipEntries.length, "There are repeated file names. Check %s",
            Arrays.asList(contentZipEntries));
    }

    private String formatDate(Date data, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(data);
    }

}
