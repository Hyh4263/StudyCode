package com.example.medicine.controller;

import com.example.medicine.dao.ImageDao;
import com.example.medicine.dto.ResponseDto;
import com.example.medicine.entity.Image;
import com.example.medicine.entity.User;
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
        File newFile = new File( imagePath + File.separator + dateDirs + File.separator + newFileName);
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
        User user = new User();
        user.setId(Integer.parseInt(userId));
        user.setImgPath(saveImage.getUrl());
        userService.save(user);
        //完整的url
        return ResponseDto.Success("上传成功", saveImage.getPath());
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
}
