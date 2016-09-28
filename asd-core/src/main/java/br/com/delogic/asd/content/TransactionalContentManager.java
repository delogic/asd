package br.com.delogic.asd.content;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.delogic.jfunk.Has;

public class TransactionalContentManager implements ContentManager {

    private ContentManager contentManager1;
    private ContentManager contentManager2;
    private static final List<String> TEMP_FILES = new ArrayList<String>();
    private static final Logger logger = LoggerFactory.getLogger("CONTENT");

    public TransactionalContentManager(ContentManager contentManager1, ContentManager contentManager2) {
        this.contentManager1 = contentManager1;
        this.contentManager2 = contentManager2;
    }

    @Override
    public String get(String name) {
        if (!Has.content(name)) {
            return "";
        }
        if (TEMP_FILES.contains(name)) {
            return contentManager1.get(name);
        }
        return contentManager2.get(name);
    }

    @Override
    public String create(InputStream inputStream, String fileName) {
        return addTempFile(contentManager1.create(inputStream, fileName));
    }

    @Override
    public void update(InputStream inputStream, String fileName) {
        contentManager1.update(inputStream, fileName);
        addTempFile(fileName);
    }

    @Override
    public String createZip(ContentZipEntry... contentZipEntries) {
        return addTempFile(contentManager1.createZip(contentZipEntries));
    }

    @Override
    public String commit(String name) {
        if (!TEMP_FILES.contains(name)) {
            logger.warn(String.format(
                "Não foi possível realizar o commit do arquivo %s. O arquivo não existem mais no "
                    + "diretório base ou já foi comitado",
                name));
            return name;
        }

        try {
            TEMP_FILES.remove(name);
            return contentManager2.create(contentManager1.getInpuStream(name), name);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ocorreu um erro ao tentar obter o inputstream do arquivo : " + name);
        }
    }

    @Override
    public InputStream getInpuStream(String name) throws Exception {
        if (!Has.content(name)) {
            return null;
        }
        if (TEMP_FILES.contains(name)) {
            return contentManager1.getInpuStream(name);
        }
        return contentManager2.getInpuStream(name);
    }

    public static String addTempFile(String newFileName) {
        TEMP_FILES.add(newFileName);
        return newFileName;
    }

}
