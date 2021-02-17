package br.com.delogic.asd.housekeeping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpaceHousekeepingJobTest {

    private SpaceHousekeepingJob hk;

    private final Logger logger = LoggerFactory.getLogger(SpaceHousekeepingJobTest.class);

    @Before
    public void clean() {
        File tempDir = new File(getTempDir());
        tempDir.delete();
    }

    @Test
    public void deveExcluirArquivos() throws Exception {
        dadoArquivosEmMB(10, 1);
        dadoHouseKeepingEspacoMB(5);
        quandoExecutarHousekeeping();
        entaoArquivosRemanecentes(5);
        entaoArquivosRemanecente("arquivo.txt5");
        entaoArquivosRemanecente("arquivo.txt6");
        entaoArquivosRemanecente("arquivo.txt7");
        entaoArquivosRemanecente("arquivo.txt8");
        entaoArquivosRemanecente("arquivo.txt9");
    }

    @Test
    public void deveExcluirArquivosSomente1Vez() throws Exception {
        dadoArquivosEmMB(10, 1);
        dadoHouseKeepingEspacoMB(5);
        quandoExecutarHousekeeping();
        quandoExecutarHousekeeping();
        quandoExecutarHousekeeping();
        quandoExecutarHousekeeping();
        quandoExecutarHousekeeping();
        entaoArquivosRemanecentes(5);
        entaoArquivosRemanecente("arquivo.txt5");
        entaoArquivosRemanecente("arquivo.txt6");
        entaoArquivosRemanecente("arquivo.txt7");
        entaoArquivosRemanecente("arquivo.txt8");
        entaoArquivosRemanecente("arquivo.txt9");
    }

    @Test
    public void deveExcluirArquivos1Apenas() throws Exception {
        dadoArquivosEmMB(10, 1);
        dadoHouseKeepingEspacoMB(9);
        quandoExecutarHousekeeping();
        entaoArquivosRemanecentes(9);
        entaoArquivosRemanecente("arquivo.txt1");
        entaoArquivosRemanecente("arquivo.txt2");
        entaoArquivosRemanecente("arquivo.txt3");
        entaoArquivosRemanecente("arquivo.txt4");
        entaoArquivosRemanecente("arquivo.txt5");
        entaoArquivosRemanecente("arquivo.txt6");
        entaoArquivosRemanecente("arquivo.txt7");
        entaoArquivosRemanecente("arquivo.txt8");
        entaoArquivosRemanecente("arquivo.txt9");
    }

    @Test
    public void deveExcluirNenhumArquivo() throws Exception {
        dadoArquivosEmMB(10, 1);
        dadoHouseKeepingEspacoMB(10);
        quandoExecutarHousekeeping();
        entaoArquivosRemanecentes(10);
        entaoArquivosRemanecente("arquivo.txt0");
        entaoArquivosRemanecente("arquivo.txt1");
        entaoArquivosRemanecente("arquivo.txt2");
        entaoArquivosRemanecente("arquivo.txt3");
        entaoArquivosRemanecente("arquivo.txt4");
        entaoArquivosRemanecente("arquivo.txt5");
        entaoArquivosRemanecente("arquivo.txt6");
        entaoArquivosRemanecente("arquivo.txt7");
        entaoArquivosRemanecente("arquivo.txt8");
        entaoArquivosRemanecente("arquivo.txt9");
    }

    @Test
    public void deveExcluirTodosArquivos() throws Exception {
        dadoArquivosEmMB(10, 1);
        dadoHouseKeepingEspacoMB(0.9999);
        quandoExecutarHousekeeping();
        entaoArquivosRemanecentes(0);
    }

    private void entaoArquivosRemanecente(String arquivo) {
        File file = new File(getTempDir() + arquivo);
        assertTrue("arquivo não existe:" + file.getAbsolutePath(), file.exists());
    }

    private void dadoArquivosEmMB(int qtde, int tamanho) throws Exception {

        for (int i = 0; i < qtde; i++) {
            File arquivo = new File(getTempDir() + "arquivo.txt" + i);
            logger.debug("gravando arquivos:" + arquivo.getAbsolutePath());
            FileUtils.writeStringToFile(arquivo, getTextoEmMB(tamanho));
            Thread.sleep(10);
        }

    }

    private String getTempDir() {
        String dir = System.getProperty("java.io.tmpdir");
        dir = dir.endsWith("/") ? dir : dir + File.separator;
        dir += "temphousekeeping" + File.separator;
        new File(dir).mkdirs();
        return dir;
    }

    private String getTextoEmMB(int tamanho) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < (1024 * 1024)) {
            sb.append("a");
        }
        return sb.toString();
    }

    private void dadoHouseKeepingEspacoMB(double i) {
        hk = new SpaceHousekeepingJob(getTempDir(), i / 1024);
    }

    private void quandoExecutarHousekeeping() {
        hk.run();
    }

    private void entaoArquivosRemanecentes(int i) {
        File[] arquivos = new File(getTempDir()).listFiles();
        assertEquals(i, arquivos.length);

        for (File file : arquivos) {
            logger.debug("Arquivo remanescente:" + file.getAbsolutePath() + " : existe?" + file.exists());
        }

    }

}