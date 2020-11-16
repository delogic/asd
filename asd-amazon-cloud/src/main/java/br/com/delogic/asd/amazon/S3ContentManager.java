package br.com.delogic.asd.amazon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import javax.activation.MimetypesFileTypeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import br.com.delogic.asd.content.ContentManager;
import br.com.delogic.asd.content.ContentZipEntry;
import br.com.delogic.asd.exception.UnexpectedApplicationException;
import br.com.delogic.jfunk.Convert;
import br.com.delogic.jfunk.Converter;
import br.com.delogic.jfunk.Has;

/**
 * Caching policy from
 * https://developers.google.com/speed/docs/best-practices/caching
 *
 * @author celio@delogic.com.br
 *
 */
public class S3ContentManager implements ContentManager {

    private boolean cache = true;
    private final String bucket;
    private final String endpoint;
    private final String path;
    private final AmazonS3 client;
    private final Iterator<? extends Object> iterator;
    private final MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
    private final String cdn;
    private CannedAccessControlList defaultPermission = CannedAccessControlList.PublicRead;

    private final String temp = "/tmp/";

    private static final Logger logger = LoggerFactory.getLogger(S3ContentManager.class);

    public S3ContentManager(AWSCredentials credentials, String endpoint, String bucket, String cdn, Iterator<? extends Object> iterator) {
        if (!Has.content(endpoint) || !endpoint.startsWith("http")) {
            throw new IllegalArgumentException("Endpoint must be a full URL containing the protocol starting by http");
        }

        this.endpoint = endpoint;
        this.bucket = bucket;
        this.iterator = iterator;
        this.cdn = cdn;

        client = new AmazonS3Client(credentials);
        client.setEndpoint(endpoint);
        path = endpoint + "/" + bucket + "/";

        if (!Has.content(cdn)) {
            cdn = path;
        }

        if (!cdn.endsWith("/")) {
            cdn += "/";
        }

    }

    @Override
    public String get(String name) {
        return !Has.content(name) ? "" : cdn + name;
    }

    @Override
    public String create(InputStream inputStream, String fileName) {
        fileName = getRealFileName(fileName);
        String val = iterator.next().toString();
        String newFileName = "file" + val + "." + getFileExtension(fileName);

        ObjectMetadata metadata = createMetadata(newFileName, inputStream);
        PutObjectRequest req = new PutObjectRequest(bucket, newFileName, inputStream, metadata);
        req.setCannedAcl(getDefaultPermission());

        client.putObject(req);
        tentarFechar(inputStream);

        return newFileName;
    }

    private void tentarFechar(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (Exception e) {
            logger.debug("erro ao tentar fechar input stream, talvez já esteja fechado:" + e.getMessage());
        }
    }

    @Override
    public void update(InputStream inputStream, String fileName) {
        fileName = getRealFileName(fileName);

        ObjectMetadata metadata = createMetadata(fileName, inputStream);

        PutObjectRequest req = new PutObjectRequest(bucket, fileName, inputStream, metadata);
        req.setCannedAcl(getDefaultPermission());
        client.putObject(req);
        tentarFechar(inputStream);
    }

    protected ObjectMetadata createMetadata(String fileName, InputStream inputStream) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(typeMap.getContentType(fileName));
        if (cache) {
            metadata.setLastModified(new Date());
            metadata.setCacheControl("max-age=31536000");
        }
        if (inputStream instanceof FileInputStream) {
            FileInputStream fis = (FileInputStream) inputStream;
            try {
                metadata.setHeader("Content-Length", fis.getChannel().size());
            } catch (IOException e) {
                logger.error("erro ao tentar ler content length do arquivo para evitar OOM no envio", e);
            }
        } else {
            // definir como deverá ser feito, talvez será necessário escrever o
            // arquivo para o filesystem para ler a partir de File.
        }
        return metadata;
    }

    private String getRealFileName(String fileName) {
        return fileName.replace(path, "");
    }

    private String getFileExtension(String fileName) {
        return Has.content(fileName) && fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
    }

    public String getBucket() {
        return bucket;
    }

    public Iterator<? extends Object> getIterator() {
        return iterator;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    @Override
    public InputStream getInpuStream(String name) throws Exception {
        return client.getObject(new GetObjectRequest(bucket, name)).getObjectContent();
    }

    public String getCdn() {
        return cdn;
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
                ObjectMetadata metadata = createMetadata(zipFileName, fis);
                PutObjectRequest req = new PutObjectRequest(bucket, zipFileName, fis, metadata);
                req.setCannedAcl(getDefaultPermission());

                client.putObject(req);

            } catch (IOException e) {
                throw new UnexpectedApplicationException("Exception when closing the zip file with entries "
                    + Arrays.toString(contentZipEntries), e);
            }
        }

        return zipFileName;

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

    @Override
    public String commit(String name) {
        return name;
    }

    public CannedAccessControlList getDefaultPermission() {
        return defaultPermission;
    }

    public void setDefaultPermission(CannedAccessControlList defaultPermission) {
        this.defaultPermission = defaultPermission;
    }

	//@Override
	public String getMd5Base64(String name) {
		return client.getObject(new GetObjectRequest(bucket, name)).getObjectMetadata().getContentMD5();
	}

}
