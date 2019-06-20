package me.ztiany.mvc.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.5 22:44
 */
@Controller
public class UploadController {

    //files 要和文件表单name相同
    @RequestMapping(value = "/upload/uploadFile.action")
    public String uploadFile(MultipartFile[] files, HttpServletRequest request) throws IOException {

        //直接使用：ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("WEB-INF/image")也可以
        //要求 idea 以非 exploded 发布才能获取到此路径
        String realPath = request.getServletContext().getRealPath("WEB-INF/image");
        File path = new File(realPath);
        if (!path.exists()) {
            path.mkdirs();
        }

        //H:\dev_tools\tomcat\apache-tomcat-8.5.4\bin\.
        System.out.println("------------------" + new File(".").getAbsolutePath());
        //H:\dev_tools\tomcat\apache-tomcat-8.5.4\webapps\springmvc\WEB-INF\image
        System.out.println("------------------" + realPath);

        for (MultipartFile multipartFile : files) {
            String originalFilename = multipartFile.getOriginalFilename();
            System.out.println("------------------" + originalFilename);
            String extension = FilenameUtils.getExtension(originalFilename);
            File file = new File(realPath, UUID.randomUUID().toString() + "." + extension);
            System.out.println("save file path------- " + file.getAbsolutePath());

            multipartFile.transferTo(file);
        }

        return "redirect:/common/success.action";
    }

    @RequestMapping(value = "/upload/uploadPage.action")
    public String uploadPage() {
        return "upload";
    }
}
