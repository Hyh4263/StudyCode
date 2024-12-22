package com.example.medicine.controller;

import com.example.medicine.dto.RespResult;
import com.example.medicine.dto.ResponseMessage;
import com.example.medicine.dto.Result;
import com.example.medicine.entity.User;
import com.example.medicine.service.OnlineUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 消息控制器
 *
 * @author Hyh
 */
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController<User> {
    @Autowired
    private OnlineUserManager onlineUserManager;

    /**
     * 发送消息
     */
//    @PostMapping("/query")
//    public RespResult query(@RequestBody String content) {
//        String result = apiService.query(content);
//        return RespResult.success(result);
//    }
//    @PostMapping("/query")
//    public Result query(@RequestBody String content) {
//        try {
//            // URL 解码
//            String decodedContent = URLDecoder.decode(content, "UTF-8");
//            System.out.println("解码后的内容: " + decodedContent);
//
//            // 将解码后的内容传递给服务进行处理
//            String result = apiService.query(decodedContent);
//            return Result.ok(result);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return Result.fail("解码失败");
//        }
//    }
    @PostMapping("/query")
    public Result query(@RequestBody String content) {
        try {
            // 如果需要解码，可以解码处理
            String decodedContent = URLDecoder.decode(content, "UTF-8");
            System.out.println("收到并解码后的内容: " + decodedContent);

            // 调用智能模型处理消息
            String result = apiService.query(decodedContent);

            // 检查结果是否为空
            if (result != null && !result.isEmpty()) {
                System.out.println("result = " + result);
                return Result.ok(result);
            } else {
                return Result.fail("未能生成有效回复，请稍后再试。");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Result.fail("解码失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("服务器错误，请稍后重试");
        }
    }

    //查找用户
    @GetMapping("/findOnlineUser")
    public Result findOnlineUser() {
        List<User> userList = userService.all();
        HashMap<String, Object> map = new HashMap<>();


        userList.forEach(user -> {
            if (!user.getUserAccount().equals("superAdmin") && !user.getUserAccount().equals(loginUser) ) {
                //如果用户权限为1
                if (user.getRoleStatus() ==  1){
                    map.put(user.getUserAccount(), user); // 或者根据你的需求放入其他信息

                }
            }
        });

        return Result.ok(map);


    }
}
