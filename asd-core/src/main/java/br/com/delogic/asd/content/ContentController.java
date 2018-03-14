package br.com.delogic.asd.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping
public class ContentController {

    public static class Files {

        private final List<File> files;

        public Files(List<File> files) {
            this.files = files;
        }

        public List<File> getFiles() {
            return files;
        }

    }

    private final ContentManager contentManager;
    private static final Logger logger = LoggerFactory.getLogger(ContentController.class);

    public ContentController(ContentManager contentManager) {
        this.contentManager = contentManager;
    }

    @RequestMapping(value = "/upload", method = { RequestMethod.POST, RequestMethod.PUT })
    @ResponseBody
    public Files upload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            String fileName = contentManager.create(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
            String fileUrl = contentManager.get(fileName);

            return new Files(Arrays.asList(new File(fileName, multipartFile.getOriginalFilename(),
                multipartFile.getSize(), fileUrl)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/download", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
    public @ResponseBody void download(
        @RequestParam("file") String file,
        @RequestParam(value = "disposition", defaultValue = "attachment") String disposition,
        @RequestParam("name") String name,
        HttpServletRequest request, HttpServletResponse response) throws Exception {

        InputStream is = contentManager.getInpuStream(file);
        ServletContext context = request.getSession().getServletContext();
        response.setContentType(context.getMimeType(file));

        response.addHeader("Content-Disposition", disposition + ";filename=" + getNomeComExtensao(name, file));
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
        tentarFechar(is);
    }
    
    private void tentarFechar(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (Exception e) {
            logger.debug("erro ao tentar fechar input stream, talvez já esteja fechado:" + e.getMessage());
        }
    }

    private String getNomeComExtensao(String nome, String file) {
        if (file.contains(".")) {
            return nome + file.substring(file.lastIndexOf("."), file.length());
        }
        return nome;
    }

    public ContentManager getUploadManager() {
        return contentManager;
    }

}
