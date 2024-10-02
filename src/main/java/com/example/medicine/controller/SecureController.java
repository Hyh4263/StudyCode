package com.example.medicine.controller;
 
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 需要登录后携带JWT才能访问
 */
@Slf4j
@RestController
@RequestMapping("/secure")
public class SecureController 
{
 
    /**
     * 查询 用户信息，登录后携带JWT才能访问
     */
//    @GetMapping("/getUserInfo")
//    public String login(HttpServletRequest request) {
//        Integer id = (Integer) request.getAttribute("id");
//        String userName = request.getAttribute("userName").toString();
//        String password= request.getAttribute("password").toString();
//        return "当前用户信息id=" + id + ",userName=" + userName+ ",password=" + password;
//    }



        /**
         * 获取用户信息，从请求中提取JWT并解析
         */
        @GetMapping("/getUserInfo")
        public String login(HttpServletRequest request) {
            Integer id = (Integer) request.getAttribute("id");
            // 确保 userName 不为 null
            String userName = request.getAttribute("userName") != null ? request.getAttribute("userName").toString() : "未设置";
            // 确保 password 不为 null
            String password = request.getAttribute("password") != null ? request.getAttribute("password").toString() : "未设置";
            return "获取的用户信息id=" + id + ",userName=" + userName + ",password=" + password;
        }




}