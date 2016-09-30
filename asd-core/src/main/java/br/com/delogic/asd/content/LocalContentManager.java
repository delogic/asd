package br.com.delogic.asd.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import br.com.delogic.asd.exception.UnexpectedApplicationException;
import br.com.delogic.jfunk.Convert;
import br.com.delogic.jfunk.Converter;
import br.com.delogic.jfunk.Has;

public class LocalContentManager implements ContentManager {

    private final Iterator<? extends Object> iterator;
    private final String absolutePath;
    private final String contextPath;
    private String path = "/static-content/";

    private static final Logger logger = LoggerFactory.getLogger("CONTENT");

    public LocalContentManager(File contentDirectory, Iterator<? extends Object> iterator, String contextPath) {
        this.absolutePath = contentDirectory.getAbsolutePath();
        this.iterator = iterator;
        this.contextPath = contextPath;
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
        return newFileName;
    }

    @Override
    public void update(InputStream inputStream, String fileName) {
        saveToServer(inputStream, absolutePath + File.separatorChar, fileName);
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
        String zipFileName = null;

        try {
            zipFileName = iterator.next().toString() + ".zip";
            String absoluteFileName = absolutePath + File.separatorChar + zipFileName;
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
}
