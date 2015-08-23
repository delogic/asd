package br.com.delogic.asd.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping
public class UploadController {

    @Inject
    private ContentManager contentManager;

    public static class Files {

        private final List<UploadedFile> files;

        public Files(List<UploadedFile> files) {
            this.files = files;
        }

        public List<UploadedFile> getFiles() {
            return files;
        }

    }

    @RequestMapping(value = "/upload", method = { RequestMethod.POST, RequestMethod.PUT })
    @ResponseBody
    public Files upload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            String fileName = contentManager.create(
                multipartFile.getInputStream(),
                multipartFile.getOriginalFilename());

            String filePath = contentManager.get(fileName);

            return new Files(Arrays.asList(new UploadedFile(fileName, multipartFile.getOriginalFilename(),
                multipartFile.getSize(), filePath, null, null, null)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/download", method = { RequestMethod.GET })
    public @ResponseBody void download(
        @RequestParam("file") String file,
        @RequestParam(value = "disposition", defaultValue = "attachment") String disposition,
        @RequestParam("nome") String nome,
        HttpServletRequest request, HttpServletResponse response) throws Exception {

        // TODO verificar outro jeito de obter o mime type
        // ServletContext context = request.getServletContext();
        InputStream is = contentManager.getInpuStream(file);
        // response.setContentType(context.getMimeType(file));

        response.addHeader("Content-Disposition",
            disposition + ";filename=" + getNomeComExtensao(nome, file));
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }

    private String getNomeComExtensao(String nome, String file) {

        if (file.contains(".") &&
            file.lastIndexOf(".") == file.length() - 4) {
            return nome + file.substring(file.lastIndexOf("."), file.length());
        }

        return nome;
    }

    public ContentManager getUploadManager() {
        return contentManager;
    }

    public void setUploadManager(ContentManager uploadManager) {
        this.contentManager = uploadManager;
    }

}
