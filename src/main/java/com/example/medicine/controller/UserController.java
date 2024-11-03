package com.example.medicine.controller;

import com.example.medicine.dao.ImageDao;
import com.example.medicine.dto.Result;
import com.example.medicine.entity.Image;
import com.example.medicine.entity.User;
import com.example.medicine.utils.Assert;
import com.example.medicine.utils.JwtTokenUtil;
import com.example.medicine.utils.StringUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "user")
public class UserController extends BaseController<User> {



    /**
     * Save user profile information.
     */
    @PostMapping("/saveProfile")
    public Result saveProfile(@RequestBody User user) {
        if (Assert.isEmpty(user)) {
            return Result.fail("保存对象不能为空");
        }
        int result = userService.updateUser(user);
        return result != -1 ? Result.ok(result) : Result.fail("更新失败");
    }

    /**
     * Change user password.
     */
    @PostMapping("/savePassword")
    public Result savePassword(@RequestBody User user) {
        User loginUser = userService.getUserByAccountAndPassword(user.getUserAccount(), user.getUserPwd());
        if (loginUser == null || !loginUser.getUserPwd().equals(user.getUserPwd())) {
            return Result.fail("旧密码错误");
        }
        loginUser.setUserPwd(user.getUserNewPwd());
        userService.save(loginUser);
        return Result.ok("密码修改成功");
    }

    /**
     * Get profile information based on token.
     */
    @GetMapping("/profile")
    public Result getProfile(HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        if (username == null) {
            return Result.fail("Token无效或用户未登录");
        }
        User profile = userService.getProfileByAccount(username);
        return profile != null ? Result.ok(profile) : Result.fail("无法获取用户信息");
    }

    /**
     * Admin: Get list of all users.
     */
    @GetMapping("/allUser")
    public Result getAllUsers() {
        List<User> allUsers = userService.all();
        return allUsers != null ? Result.ok(allUsers) : Result.fail("获取用户列表失败");
    }




    private String extractUsernameFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }
        Claims claims = JwtTokenUtil.parseToken(token.replace("Bearer ", ""));
        return claims != null ? claims.getSubject() : null;
    }


}
