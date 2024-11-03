package com.example.medicine.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.medicine.dao.VerCodeMapper;
import com.example.medicine.dto.Result;
import com.example.medicine.entity.User;
import com.example.medicine.entity.VerCode;
import com.example.medicine.utils.Assert;
import com.example.medicine.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 登录控制器
 */
@RestController
@RequestMapping(value = "login")
public class LoginController extends BaseController<User> {

    @Autowired
    private VerCodeMapper verCodeMapper;

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        String email = user.getUserEmail();
        if (Assert.isEmpty(email)) {
            return Result.fail("邮箱不能为空");
        }

        if (Assert.isEmpty(user.getCode())) {
            return Result.fail("验证码不能为空");
        }


        QueryWrapper<VerCode> verCodeQueryWrapper = new QueryWrapper<>();
        verCodeQueryWrapper.eq("code_value", user.getCode());
        VerCode storedCode = verCodeMapper.selectOne(verCodeQueryWrapper);
        if (storedCode == null) {
            return Result.fail("验证码错误或已过期");
        }

        List<User> query = userService.query(User.builder().userAccount(user.getUserAccount()).build());

        if (Assert.notEmpty(query)) {
            return Result.fail("账户已被注册");
        }

        user.setRoleStatus(0);
//        http://localhost:8081/images/userImg/img_6.png
        user.setImgPath("http://localhost:8081" + user.getImgPath());

        user = userService.save(user);

        session.setAttribute("loginUser", user);
        return Result.ok(user);
    }

    /**
     * 登录
     */
//    @PostMapping("/login")
//    public Result login(@RequestBody User user) {
//        List<User> users = userService.query(user);
//        if (Assert.notEmpty(users)) {
//            session.setAttribute("loginUser", users.get(0));
//            return Result.ok("登录成功");
//        }
//        if (Assert.isEmpty(userService.query(User.builder().userAccount(user.getUserAccount()).build()))) {
//            return Result.fail("账户尚未注册");
//        }
//        return Result.fail("密码错误");
//    }
    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpSession session) {
//        List<User> users = userService.query(user);
        User loginUser = userService.getUserByAccountAndPassword(user.getUserAccount(), user.getUserPwd());

        if (Assert.notEmpty(loginUser)) {

            session.setAttribute("loginUser", loginUser);
            System.out.println("loginUser = " + loginUser);
            // 生成Token
//            String token = JwtTokenUtil.generateToken(loginUser.getUsername());
            String token = JwtTokenUtil.generateToken(loginUser.getUserAccount());
            System.out.println("token = " + token);
//            return Result.ok("登录成功").addData("token", token);
            return Result.ok(token);
        }
        if (Assert.isEmpty(userService.query(User.builder().userAccount(user.getUserAccount()).build()))) {
            return Result.fail("账户尚未注册");
        }
        return Result.fail("密码错误");
    }

    /**
     * 退出登录
     */

    // 退出登录接口
    @PostMapping("/logout")
    public Result logout(HttpSession session) {
        // 清除会话中的登录用户信息
        session.removeAttribute("loginUser");
        // 可选：使会话失效
        session.invalidate();

        return Result.ok("退出登录成功");
    }


    /**
     * 发送邮箱验证码
     */
    @GetMapping("/sendEmailCode")
    public Result<Object> sendEmailCode(@RequestParam String email) {
        if (StrUtil.isEmpty(email)) {
            return Result.fail("邮箱不可为空");
        }

        // 发送验证码
        String verifyCode = emailClient.sendEmailCode(email);

        if (verifyCode == null) {
            return Result.fail("验证码发送失败");
        }

        // 保存验证码信息
        VerCode code = new VerCode();

        code.setCodeValue(verifyCode);
        code.setCreateTime(new Date());
        verCodeMapper.insert(code);

        return Result.ok();
    }
}
