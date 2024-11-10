package com.example.medicine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.medicine.dao.ImageDao;
import com.example.medicine.dao.VideoDao;
import com.example.medicine.dto.ResponseDto;
import com.example.medicine.entity.Image;
import com.example.medicine.entity.User;
import com.example.medicine.entity.Video;
import com.example.medicine.service.UserService;
import com.example.medicine.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {

    @Value("${kkBlog.image-path}")
    private String imagePath;

    @Value("${kkBlog.video-path}")
    private String videoPath;

    @Value("${kkBlog.ip-address}")
    private String ipAddress;

    @Value("${server.port}")
    private String port;


    @Value("${kkBlog.protocol}")
    private String protocol;
    @Autowired
    private UserService userService;  // Assume userService has methods to update user data

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private VideoDao videoDao;

    @ResponseBody
    @PostMapping("/upload")
    public ResponseDto upload(@RequestParam MultipartFile image, @RequestParam String userId,
                              @RequestParam(required = false) String name, @RequestParam(required = false) String description) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String res = sdf.format(new Date());
        //原始名称
        String originalFilename = image.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            return ResponseDto.Fail("图片格式异常！");
        }
        if (StringUtils.isNull(userId)) {
            return ResponseDto.Fail("用户id为空！");
        }
        //生成文件名
        assert originalFilename != null;
        String newFileName = res + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //创建年月日文件夹
        Calendar date = Calendar.getInstance();
        String folderPath = date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH) + 1) + File.separator + (date.get(Calendar.DATE));
        File dateDirs = new File(folderPath);
        //新文件
        File newFile = new File(imagePath + File.separator + dateDirs + File.separator + newFileName);
        //判断目标文件所在的目录是否存在
        if (!newFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }
        //将内存中的数据写入磁盘
        image.transferTo(newFile);
        Image saveImage = new Image();
        // 保存到数据库
        saveImage.setUserId(Integer.parseInt(userId));
        saveImage.setName(name);
        saveImage.setDescription(description);
        // 格式化创建时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMddHHmmssSS");
        String formattedDate = dateFormat.format(new Date());
        // 解析格式化后的字符串为 Date 对象
        Date createTime = dateFormat.parse(formattedDate);
        saveImage.setCreateTime(createTime);

        saveImage.setPath(("/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        saveImage.setUrl((protocol + "://" + ipAddress + ":" + port + "/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        imageDao.insert(saveImage);

        //完整的url
        return ResponseDto.Success("上传成功", saveImage.getUrl());
    }

    //删除上传图片
    @DeleteMapping("/deleteUploadByUrl")
    public ResponseDto deleteUploadByUrl(@RequestParam String url) {
        //Image uploaded successfully: http://localhost:8081/images/2024/11/6/20241106164222371ab69ef7b-c265-4284-a45e-e052aede5709.png
        //根据传递的url找到图片
        QueryWrapper<Image> wrapper = new QueryWrapper<>();
        wrapper.eq("url", url);
        Image image = imageDao.selectOne(wrapper);
        if (image == null) {
            return ResponseDto.Fail("图片不存在数据库");
        }
        //根据图片路径找到本地存储图片文件
//        D:\data\images\
        File file = new File("D:\\data" + image.getPath());


        if (!file.exists()) {
            return ResponseDto.Fail("图片不存在本地");
        }
        // 删除该图片文件
        file.delete();
        //再删除数据库记录
//        int i = imageDao.deleteById(image.getUrl());
        int i = imageDao.delete(wrapper);
        if (i == 0) {
            return ResponseDto.Fail("删除失败");
        }
        return ResponseDto.Success("删除成功", null);

    }

    // 更换头像
    @ResponseBody
    @PostMapping("/changeAvatar")
    public ResponseDto changeAvatar(@RequestParam MultipartFile image,
                                    @RequestParam(required = false) String name,
                                    @RequestParam String userId) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String res = sdf.format(new Date());
        //原始名称
        String originalFilename = image.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            return ResponseDto.Fail("图片格式异常！");
        }
        if (image.getSize() > 1024 * 1024 * 2) {
            return ResponseDto.Fail("头像仅支持最大图片2M");
        }
        //生成文件名
        assert originalFilename != null;
        String newFileName = res + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //创建年月日文件夹
        Calendar date = Calendar.getInstance();
        String folderPath = date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH) + 1) + File.separator + (date.get(Calendar.DATE));
        File dateDirs = new File(folderPath);
        //新文件
        File newFile = new File(imagePath + File.separator + dateDirs + File.separator + newFileName);
        //判断目标文件所在的目录是否存在
        if (!newFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }
        //将内存中的数据写入磁盘
        image.transferTo(newFile);
        Image saveImage = new Image();
        // 保存到数据库
        saveImage.setUserId(Integer.parseInt(userId));
        saveImage.setCreateTime(new Date());
        saveImage.setName(StringUtils.isEmpty(name) ? originalFilename : name);
        saveImage.setPath(("/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        saveImage.setUrl((protocol + "://" + ipAddress + ":" + port + "/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        imageDao.insert(saveImage);
        // 保存用户的背景图
        User updateUser = new User();
        updateUser.setId(Integer.parseInt(userId));
        updateUser.setImgPath(saveImage.getUrl());
        userService.save(updateUser);

        //完整的url
        return ResponseDto.Success("上传成功", saveImage.getUrl());
    }

    @ResponseBody
    @PostMapping("/uploadVideo")
    public ResponseDto uploadVideo(@RequestParam MultipartFile video, @RequestParam String userId,
                                   @RequestParam(required = false) String videoName, @RequestParam(required = false) String description) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String res = sdf.format(new Date());

        // 原始名称
        String originalFilename = video.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            return ResponseDto.Fail("视频格式异常！");
        }
        if (StringUtils.isNull(userId)) {
            return ResponseDto.Fail("用户id为空！");
        }

        // 生成文件名
        assert originalFilename != null;
        String newFileName = res + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 创建年月日文件夹
        Calendar date = Calendar.getInstance();
        String folderPath = date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH) + 1) + File.separator + (date.get(Calendar.DATE));
        File dateDirs = new File(folderPath);

        // 新文件
        File newFile = new File(videoPath + File.separator + dateDirs + File.separator + newFileName);

        // 判断目标文件所在的目录是否存在
        if (!newFile.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }

        // 将内存中的数据写入磁盘
        video.transferTo(newFile);

        // 保存视频信息到数据库
        Video saveVideo = new Video();
        saveVideo.setUserId(Integer.parseInt(userId));
        saveVideo.setName(videoName);
        saveVideo.setDescription(description);

        // 格式化创建时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMddHHmmssSS");
        String formattedDate = dateFormat.format(new Date());
        Date createTime = dateFormat.parse(formattedDate);
        saveVideo.setCreateTime(createTime);

        // 设置视频的文件路径和URL
        saveVideo.setPath(("/videos/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        saveVideo.setUrl((protocol + "://" + ipAddress + ":" + port + "/videos/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));

        // 插入到数据库
        videoDao.insert(saveVideo);

        // 返回完整的 URL
        return ResponseDto.Success("视频上传成功", saveVideo.getUrl());
    }

    @DeleteMapping("/deleteUploadVideoByUrl")
    public ResponseDto deleteUploadVideoByUrl(@RequestParam String url) {
        //Image uploaded successfully: http://localhost:8081/images/2024/11/6/20241106164222371ab69ef7b-c265-4284-a45e-e052aede5709.png
        //根据传递的url找到图片
//        http://localhost:8081/videos/2024/11/9/20241109202818446e7d1d07c-44de-4c08-af05-f6ee4f6772e3.mp4
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("url", url);
        Video video = videoDao.selectOne(wrapper);
        if (video == null) {
            return ResponseDto.Fail("视频不存在数据库");
        }
        //根据图片路径找到本地存储图片文件
//        D:\data\images\
        File file = new File("D:\\data" + video.getPath());


        if (!file.exists()) {
            return ResponseDto.Fail("视频不存在本地");
        }
        // 删除该图片文件
        boolean delete = file.delete();
        //再删除数据库记录
        int i = videoDao.delete(wrapper);
        if (i == 0 && !delete) {
            return ResponseDto.Fail("删除失败");
        }
        return ResponseDto.Success("删除成功", null);

    }

}
