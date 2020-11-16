package br.com.delogic.asd.content;

import java.io.InputStream;

import br.com.delogic.asd.exception.ContentException;

/**
 * Manages the dynamic content in the application. Files uploaded, images, etc.
 * This service is mainly used by {@code UploadController} for storing files
 * where this interface implementation demands. This interface has different
 * implementations, saving the files locally, to the temp folder, to a NAS or in
 * the cloud. Developers should rely on this interface to know where a resource
 * is and present the correct path to it.
 *
 * @author celio@delogic.com.br
 *
 */
public interface ContentManager {

    public static final ContentException NO_CONTENT_TO_ZIP = new ContentException("No files were passed to be zipped");

    public static final ContentException REPEATED_FILE_NAMES = new ContentException();

    /**
     * Stores the content with a new name. This is called by
     * {@code UploadController} for storing the files accordingly but can be
     * called by any controller directly.
     *
     * @param inputStream
     *            - content to be stored
     * @param fileName
     *            - fileName which will be used to extract the extension and use
     *            the metadata. The content saved will have another file name.
     * @return a new file name named file-something.extension
     */
    String create(InputStream inputStream, String fileName);

    /**
     * Replaces the content. Will save the content with the named sent as
     * parameter.
     *
     * @param inputStream
     *            - content to be stored
     * @param fileName
     *            - fileName which will be used to extract the extension and use
     *            the metadata as well as use the same name for the new file
     */
    void update(InputStream inputStream, String fileName);

    /**
     * Returns the path to the content. It may return the relative path to the
     * application, like /context/path/to or the full URL including the protocol
     * like http://mydomain.com/path/to. It depends on the implementation used.
     *
     * @param name
     *            of the content
     * @return content access path on the web
     */
    String get(String name);

    /**
     * Returns the input stream for the content. If the content is saved to the
     * current server than will return a local InputStream. if content is saved
     * remotely than will return a remote input stream.
     *
     * @param name
     *            of the content
     * @return InputStream
     * @throws Exception
     *             thrown by IO
     */
    InputStream getInpuStream(String name) throws Exception;

    /**
     * Creates a zip and returns its file name. This name can be used to get a
     * new InputStream to the zipped package or to get the URI.
     * 
     * It must be a temporary file therefore not stored. If a user wants to
     * store this file he should create it passing the InputStream of the Zip
     * temporary file to the create method himself.
     *
     * @param contentZipEntries
     * @return The zip file name to get and download the new zipped content.
     */
    String createZip(ContentZipEntry... contentZipEntries);

    /**
     * Obtain the content of param from contentManager1 and save content on
     * contentManager2 returning fileName created on contentManager2
     *
     * @param name
     *            - content saved
     * @return a new file name named file-something.extension
     */
    String commit(String name);

    /**
     * Returns the MD5 for the inputStream content in Base64 format. Prefer
     * Base64Utils from Spring to convert.
     * 
     * @param name
     * @return Md5 in Base64 encoded
     */
    // String getMd5Base64(String name);

}
