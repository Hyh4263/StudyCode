package com.example.medicine.controller;

import com.example.medicine.entity.IllnessKind;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.medicine.dto.Result;

import java.util.List;

/**
 * 疾病分类控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("illness_kind")
public class IllnessKindController extends BaseController<IllnessKind> {

    //获取所有疾病种类
    @GetMapping("/findList")
    public Result findList() {
//        if (loginUser == null) {
//            return Result.fail();
//        }
//        if (loginUser.getRoleStatus() != 1) {
//            return Result.fail("权限不足");
//        }
        List<IllnessKind> list = illnessKindService.findList();
        if (list == null) {
            return Result.fail("获取疾病种类失败");
        }
        return Result.ok(list);
    }
}
