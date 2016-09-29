package br.com.delogic.asd.content;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.delogic.jfunk.Has;

public class TransactionalContentManager implements ContentManager {

    private ContentManager tempContentManager;
    private ContentManager finalContentManager;
    private final List<String> TEMP_FILES = new ArrayList<String>();
    private static final Logger logger = LoggerFactory.getLogger("CONTENT");

    public TransactionalContentManager(ContentManager tempContentManager, ContentManager finalContentManager) {
        this.tempContentManager = tempContentManager;
        this.finalContentManager = finalContentManager;
    }

    @Override
    public String get(String name) {
        if (!Has.content(name)) {
            return "";
        }
        if (TEMP_FILES.contains(name)) {
            return tempContentManager.get(name);
        }
        return finalContentManager.get(name);
    }

    @Override
    public synchronized String create(InputStream inputStream, String fileName) {
        return addTempFile(tempContentManager.create(inputStream, fileName));
    }

    @Override
    public synchronized void update(InputStream inputStream, String fileName) {
        tempContentManager.update(inputStream, fileName);
        addTempFile(fileName);
    }

    @Override
    public synchronized String createZip(ContentZipEntry... contentZipEntries) {
        return addTempFile(tempContentManager.createZip(contentZipEntries));
    }

    @Override
    public synchronized String commit(String name) {
        if (!TEMP_FILES.contains(name)) {
            logger.warn(String.format(
                "Não foi possível realizar o commit do arquivo %s. O arquivo não existem mais no "
                    + "diretório base ou já foi comitado",
                name));
            return name;
        }

        try {
            String newFileName = finalContentManager.create(tempContentManager.getInpuStream(name), name);
            TEMP_FILES.remove(name);
            return newFileName;
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
            return tempContentManager.getInpuStream(name);
        }
        return finalContentManager.getInpuStream(name);
    }

    public String addTempFile(String newFileName) {
        TEMP_FILES.add(newFileName);
        return newFileName;
    }

}
