package br.com.delogic.asd.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import br.com.delogic.asd.exception.AsdRuntimeException;
import br.com.delogic.asd.exception.UnexpectedApplicationException;
import br.com.delogic.jfunk.Convert;
import br.com.delogic.jfunk.Converter;
import br.com.delogic.jfunk.Has;

public class LocalContentManager implements ContentManager {

    private static final String TEMP_ZIP_PREFIX = "T3Mp_z1P-";
    private static final String TEMP_ZIP_SUFIX = ".zip";
    private final Iterator<? extends Object> iterator;
    private final String absolutePath;
    private final String contextPath;
    private String path = "/static-content/";

    private static final Logger logger = LoggerFactory.getLogger("CONTENT");

    public LocalContentManager(File contentDirectory, Iterator<? extends Object> iterator, String contextPath) {
        this.absolutePath = contentDirectory.getAbsolutePath();
        this.iterator = iterator;
        this.contextPath = contextPath;
        new File(absolutePath).mkdirs();
    }

    @Override
    public String get(String name) {
        if (!Has.content(name)) {
            return "";
        }
        return contextPath + path + name;
    }

    @Override
    public String create(InputStream inputStream, String fileName) {
        String val = iterator.next().toString();
        // TODO change to avoid getting the extension incorrectly.
        String newFileName = "file" + val + "." + getFileExtension(fileName);
        saveToServer(inputStream, absolutePath + File.separatorChar,
            newFileName);
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
        saveToServer(inputStream, absolutePath + File.separatorChar, fileName);
        tentarFechar(inputStream);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")
            || fileName.endsWith(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static void saveToServer(InputStream inputStream,
        String directoryPath, String fileName) {
        try {
            logger.debug("Saving content " + directoryPath + fileName);
            FileCopyUtils.copy(inputStream, new FileOutputStream(directoryPath
                + fileName));
        } catch (IOException e) {
            logger.error("Cannot save content", e);
            throw new RuntimeException("Cannot save content with name "
                + fileName, e);
        }
    }

    public InputStream getInpuStream(String name) throws Exception {
        if (Has.content(name) && name.startsWith(TEMP_ZIP_PREFIX) && name.endsWith(TEMP_ZIP_SUFIX)) {
        	String tempDir = System.getProperty("java.io.tmpdir");
        	tempDir = tempDir.endsWith(File.separator) ? tempDir: tempDir + File.separator;  
            File tempZipFile = new File(tempDir + name);
            return new FileInputStream(tempZipFile);
        }
        return new FileInputStream(absolutePath + File.separatorChar + name);
    }

    public Iterator<? extends Object> getIterator() {
        return iterator;
    }

    public String getPath() {
        return path;
    }

    public LocalContentManager setPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public String createZip(ContentZipEntry... contentZipEntries) {

        validateFiles(contentZipEntries);

        ZipOutputStream zos = null;
        FileOutputStream fos = null;
        File finalZipFile = null;
        
        try {
            File tempZipFile = File.createTempFile(iterator.next().toString(), null);
            fos = new FileOutputStream(tempZipFile);
            zos = new ZipOutputStream(fos);
            byte[] buffer = new byte[1024];
            int read;
            
            Arrays.sort(contentZipEntries, new Comparator<ContentZipEntry>() {
                @Override
                public int compare(ContentZipEntry o1, ContentZipEntry o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            MessageDigest digest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
                        
            for (ContentZipEntry contentZipEntry : contentZipEntries) {
                zos.putNextEntry(contentZipEntry);
                InputStream in = contentZipEntry.getInputStream();
                while ((read = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, read);
                    digest.update(buffer, 0, read);
                }
                in.close();
                zos.closeEntry();
            }
            zos.close();
            fos.close();
            
            String md5Hex = Hex.encodeHexString(digest.digest());
            System.err.println(md5Hex);
             
            String tempDir = System.getProperty("java.io.tmpdir");
            tempDir = tempDir.endsWith(File.separator) ? tempDir: tempDir + File.separator;  
            finalZipFile = new File(tempDir + TEMP_ZIP_PREFIX + md5Hex + TEMP_ZIP_SUFIX);
            if (finalZipFile.exists()) {
                //change lastmodified so we don't delete if someone's downloading
                finalZipFile.setLastModified(System.currentTimeMillis());
                tempZipFile.delete();
            } else {
                tempZipFile.renameTo(finalZipFile);
            }
             
            return finalZipFile.getName();

        } catch (Exception e) {
            throw new UnexpectedApplicationException("Exception when creating a zip file with entries "
                + Arrays.toString(contentZipEntries), e);
        }
        
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

//	@Override
	public String getMd5Base64(String name) {
		try (InputStream is = getInpuStream(name)) {
			return Base64Utils.encodeToString(DigestUtils.md5(is));
		} catch (Exception e) {
			throw new AsdRuntimeException("Error trying to get md5 from:" + name, e);
		}
	}
}
