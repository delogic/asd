package br.com.delogic.asd.content;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import br.com.delogic.asd.exception.ContentException;

public class LocalContentManagerTest extends Assert {

    private List<ContentZipEntry> iss;

    private ContentManager contentManager;

    private String zipFile;

    private Exception thrown;

    private long startTime;

    @Before
    public void init() {
        contentManager = new LocalContentManager(
            new java.io.File(getTempDir() + "localcontentmanager"), new TimeIterator(),
            "/testcontext");
        iss = new ArrayList<ContentZipEntry>();
        zipFile = null;
    }

    @Test
    public void shouldCreateZippedFile() {
        givenTheResource("file.txt", "test-resources/file.txt");
        whenCreatingZip();
        thenZipIsCreated();
    }

    @Test
    public void shouldCreateZippedFileWithManyFiles() {
        givenTheResource("file.txt", "test-resources/file.txt");
        givenTheResource("aston.jpg", "test-resources/aston.jpg");
        givenTheResource("delogic.png", "test-resources/delogic.png");
        givenTheResource("file.pdf", "test-resources/file.pdf");
        whenCreatingZip();
        thenZipIsCreated();
    }

    private void givenTheResource(String fileName, String fileLocation) {
        try {
            iss.add(new ContentZipEntry(fileName, new ClassPathResource(fileLocation).getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void whenCreatingZip() {
        try {
            zipFile = contentManager.createZip(iss.toArray(new ContentZipEntry[iss.size()]));
        } catch (Exception e) {
            thrown = e;
        }
    }

    private void thenZipIsCreated() {
        assertNotNull(zipFile);
        try {
            InputStream is = contentManager.getInpuStream(zipFile);
            assertNotNull(is);
            assertNotEquals(-1, is.read());
            is.close();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldNotCreateZippedFileWithoutFile() {
        givenNoFile();
        whenCreatingZip();
        thenExceptionIs(ContentManager.NO_CONTENT_TO_ZIP);
    }

    private void thenExceptionIs(ContentException noContentToZip) {
        assertTrue(noContentToZip.equals(thrown));
    }

    private void givenNoFile() {
        iss = Collections.emptyList();
    }

    @Test
    public void shouldNotCreateZippedFileWithFilesRepeatedName() {
        givenTheResource("file.txt", "test-resources/file.txt");
        givenTheResource("file.txt", "test-resources/file.txt");
        whenCreatingZip();
        thenExceptionIs(ContentManager.REPEATED_FILE_NAMES);
        thenExceptionMessageIs("There are repeated file names. Check [file.txt, file.txt]");
    }

    private void thenExceptionMessageIs(String string) {
        assertEquals(string, thrown.getMessage());
    }

    @Test
    public void shouldUseExistingZipFile() throws Exception {
        givenStartTime();
        givenTheResource("file.txt", "test-resources/file.txt");
        givenZipIsCreated();
        givenZipLastModificationIs(1);
        iss = new ArrayList<ContentZipEntry>();
        givenTheResource("file2.txt", "test-resources/file.txt");
        whenCreatingZip();
        thenLastModificationIsAfterStartTime();
    }

    private void thenLastModificationIsAfterStartTime() {
        File zip = new File(getTempDir() + zipFile);
        assertTrue("arquivo não existe:" + zip.getName(), zip.exists());
        assertTrue(zip.lastModified() > startTime);
    }

    private void givenStartTime() {
        startTime = System.currentTimeMillis();
    }

    private void givenZipLastModificationIs(int i) throws Exception {
        File zip = new File(getTempDir() + zipFile);
        assertTrue("arquivo não existe:" + zip.getName(), zip.exists());
        zip.setLastModified(i);
        assertEquals(i, zip.lastModified());
    }

    private String getTempDir() {
        String tempDir = System.getProperty("java.io.tmpdir");
        return tempDir.endsWith(File.separator) ? tempDir : tempDir + File.separator;
    }

    private void givenZipIsCreated() {
        whenCreatingZip();
        thenZipIsCreated();
    }

}
