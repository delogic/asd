package br.com.delogic.asd.content;

import java.io.InputStream;
import java.util.zip.ZipEntry;

/**
 * Classe que possui o nome do arquivo que será dado dentro do zip file e o
 * inputStream para o arquivo.
 *
 * @author Célio
 *
 */
public class ContentZipEntry extends ZipEntry {

    private InputStream inputStream;

    public ContentZipEntry(String fileName, InputStream inputStream) {
        super(fileName);
        this.setInputStream(inputStream);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

}
