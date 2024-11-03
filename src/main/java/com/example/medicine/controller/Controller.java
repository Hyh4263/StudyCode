package com.example.medicine.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * <p>描述:   图片和视频上传和查看。</p>
 * <p>文件名称: Controller.java</p>
 * <p>创建时间: 2022-07-08 </p>
 * @author 万笑佛
 * @since 2022-07-08
 */

@RestController
@RequestMapping("/editor")
public class Controller   {

    /**
     * 图片上传。
     *
     * @param fileUpload
     * @return JSONObject
     */
    @PostMapping(value = "/image/upload" )
    @ResponseBody
    public void imageUpload(@RequestParam("file") MultipartFile fileUpload) {

        //获取文件名
        String fileName = fileUpload.getOriginalFilename();
        String tmpFilePath =  "D://test//image//"  ;

        //没有路径就创建路径
        File tmp = new File(tmpFilePath);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
        String resourcesPath = tmpFilePath + "//" + fileName;

        File upFile = new File(resourcesPath);
        try {
            fileUpload.transferTo(upFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 图片查看。

     * @return String
     */
    @GetMapping("/image/look")
    public String imageLook (HttpServletResponse response) {

        File file = new File("D:\\test\\image\\1.png");
        byte[] bytes = new byte[1024];
        try (OutputStream os = response.getOutputStream();
             FileInputStream fis = new FileInputStream(file)){
            while ((fis.read(bytes)) != -1) {
                os.write(bytes);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }


    /**
     * 视频上传。
     *
     * @param fileUpload
     * @return JSONObject
     */
    @PostMapping(value = "/video/upload" )
    @ResponseBody
    public void videoUpload(@RequestParam("file") MultipartFile fileUpload) {

        //获取文件名
        String fileName = fileUpload.getOriginalFilename();
        String tmpFilePath =  "D://test//video//"  ;
        //没有路径就创建路径
        File tmp = new File(tmpFilePath);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
        String resourcesPath = tmpFilePath + "//" + fileName;
        File upFile = new File(resourcesPath);
        try {
            fileUpload.transferTo(upFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 视频查看。

     * @return String
     */
    @GetMapping("/video/look")
    public String videoLook (HttpServletResponse response) {
        File file = new File("D:\\test\\video\\1.mp4");
        byte[] bytes = new byte[1024];
        try (OutputStream os = response.getOutputStream();
             FileInputStream fis = new FileInputStream(file)){
            while ((fis.read(bytes)) != -1) {
                os.write(bytes);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

}