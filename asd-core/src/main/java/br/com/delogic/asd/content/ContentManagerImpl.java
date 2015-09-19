package br.com.delogic.asd.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import br.com.delogic.jfunk.Has;

public class ContentManagerImpl implements ContentManager {

    private final Resource directory;
    private final Iterator<? extends Object> iterator;
    private final ServletContext context;
    private String path = "/static-content/";
    private String absolutePath;
    private String contextPath;

    private static final Logger logger = LoggerFactory.getLogger("CONTENT");

    public ContentManagerImpl(Resource contentDirectory, Iterator<? extends Object> iterator, ServletContext servletContext) {
        this.directory = contentDirectory;
        this.iterator = iterator;
        this.context = servletContext;
    }

    @PostConstruct
    public void init() throws Exception {
        this.absolutePath = directory.getFile().getAbsolutePath();
        contextPath = context.getContextPath();
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

    public Resource getDirectory() {
        return directory;
    }

    public Iterator<? extends Object> getIterator() {
        return iterator;
    }

    public String getPath() {
        return path;
    }

    public ContentManagerImpl setPath(String path) {
        this.path = path;
        return this;
    }

}
