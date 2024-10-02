package com.example.medicine.controller;

import com.example.medicine.dto.RespResult;
import com.example.medicine.dto.Result;
import com.example.medicine.entity.User;
import com.example.medicine.utils.Assert;
import com.example.medicine.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping(value = "user")
public class UserController extends BaseController<User> {

    /**
     * 修改资料
     */
    @PostMapping("/saveProfile")
    public Result saveProfile(@RequestBody User user) {
        if (Assert.isEmpty(user)) {
            return Result.fail("保存对象不能为空");
        }
//       User save = userService.save(user);
//        session.setAttribute("loginUser", user);
//        return RespResult.success("保存成功");
        int save = userService.updateUser(user);
        if (save != -1){
            return Result.ok(save);
        }
        return Result.fail("更新失败");
    }

    /**
     * 修改密码
     */
//    @PostMapping("/savePassword")
//    public Result savePassword( String oldPass, String newPass) {
//        if (!loginUser.getUserPwd().equals(oldPass)) {
//            return Result.fail("旧密码错误");
//        }
//        loginUser.setUserPwd(newPass);
//        loginUser = userService.save(loginUser);
//        return Result.ok();
//
//    }
    @PostMapping("/savePassword")
    public Result savePassword(@RequestBody User user) {
        String oldPass = user.getUserPwd();
        String newPass = user.getUserNewPwd();
//        User loginUser = userService.findUserByUserAccount(user.getUserAccount());
        User loginUser = userService.getUserByAccountAndPassword(user.getUserAccount(), user.getUserPwd());

        if (!loginUser.getUserPwd().equals(oldPass)) {
            return Result.fail("旧密码错误");
        }

        loginUser.setUserPwd(newPass);
        userService.save(loginUser);

        return Result.ok("密码修改成功");
//        if (!loginUser.getUserPwd().equals(oldPass)) {
//            return Result.fail("旧密码错误");
//        }
//        loginUser.setUserPwd(newPass);
//        loginUser = userService.save(loginUser);
//        return Result.ok();

    }


    // 新增的方法用于查询个人资料
//    @GetMapping("/profile")
//    public Result getProfile(HttpSession session) {
//        User loginUser = (User) session.getAttribute("loginUser");
//
//        if (loginUser == null) {
//            return Result.fail("用户未登录");
//        }
//
//        // 调用服务层获取用户详细信息
////        User profile = userService.getProfile(loginUser.getId());
//        User profile = userService.get(loginUser.getId());
//        if (profile != null) {
//            return Result.ok(profile);  // 返回用户信息
//        } else {
//            return Result.fail("无法获取用户信息");
//        }
//    }

    // 新增的方法用于查询个人资料
    @GetMapping("/profile")
    public Result getProfile(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Claims claims = JwtTokenUtil.parseToken(token);
        String username = claims.getSubject();

        // 根据用户名获取用户信息
        User profile = userService.getProfileByAccount(username);
        if (profile != null) {
            return Result.ok(profile);  // 返回用户信息
        } else {
            return Result.fail("无法获取用户信息");
        }
    }
//    @GetMapping("/profile")
//    public Result getProfile(HttpServletRequest request) {
//        // 获取 Authorization 请求头
//        String authHeader = request.getHeader("Authorization");
//
//        // 检查 Authorization 头是否存在
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return Result.fail("缺少 Authorization 头或者格式错误");
//        }
//
//        // 提取 token 并解析
//        String token = authHeader.replace("Bearer ", "");
//        Claims claims;
//        try {
//            claims = JwtTokenUtil.parseToken(token);
//        } catch (Exception e) {
//            return Result.fail("无效的 Token");
//        }
//
//        String username = claims.getSubject();
//
//        // 根据用户名获取用户信息
//        User profile = userService.getProfileByAccount(username);
//        if (profile != null) {
//            return Result.ok(profile);  // 返回用户信息
//        } else {
//            return Result.fail("无法获取用户信息");
//        }
//    }
    /**
     * 管理员获取所有用户列表
     */
    @GetMapping("/allUser")
    public Result all() {
        List<User> all = userService.all();
        if (all == null) {
            return Result.fail("获取用户列表失败");
        }
        return Result.ok(all);
    }


}


