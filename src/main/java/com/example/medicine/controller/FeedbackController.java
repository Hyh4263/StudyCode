package com.example.medicine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.medicine.dto.Result;
import com.example.medicine.entity.Feedback;
import com.example.medicine.entity.Medicine;
import com.example.medicine.utils.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 反馈控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping(value = "feedback")
public class FeedbackController extends BaseController<Feedback> {


    /**
     * 分页查询反馈
     */
    @GetMapping("findFeedBacks")
    public Result findFeedBacks(@RequestParam(defaultValue = "1") Integer pageNow, @RequestParam(defaultValue = "5") Integer pageSize) {

//        if (loginUser != null && Assert.notEmpty(nameValue)) {
//            historyService.insetOne(loginUser.getId(), MedicalConstants.TYPE_MEDICINE, nameValue);
//        }
        Page<Feedback> feedBacksList = feedbackService.findFeedBacksList(pageNow, pageSize);
        HashMap<String, Object> map = new HashMap<>();
        if (feedBacksList == null) {
            return Result.fail("没有查询到反馈信息");
        }
        map.put("feedBacksList", feedBacksList.getRecords());
        map.put("totalPages", feedBacksList.getPages()); // 总页数
        map.put("totalElements", feedBacksList.getTotal()); // 总记录数
        map.put("currentPage", feedBacksList.getCurrent()); // 当前页码
        return Result.ok(map);

    }

    /**
     * 查询单个反馈
     */
    @GetMapping("findFeedBackOne/{id}")
    public Result findFeedBackOne(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<>();
        Feedback feedbackById = feedbackService.get(id);
        if (feedbackById == null) {
            return Result.fail("反馈信息不存在");
        }
        map.put("FeedBack", feedbackById);
        return Result.ok(map);
    }


    /**
     * 更新/添加疾病信息
     */
    @PostMapping("saveFeedBack")
    public Result saveFeedBack(@RequestBody Feedback feedback) {
//        if (Assert.isEmpty(loginUser)) {
//            return Result.fail("请登录");
//        }
//        if (loginUser.getRoleStatus() != 1) {
//            return Result.fail("权限不足");
//        }
        if (Assert.isEmpty(feedback)) {
            return Result.fail("参数错误");
        }
        if (Assert.isEmpty(feedback.getUserId())) {
            return Result.fail("请先登录");
        }

        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content", feedback.getContent());
        queryWrapper.eq("user_id", feedback.getUserId());
        queryWrapper.eq("contact_method", feedback.getContact());
        Feedback one = feedbackService.getOne(queryWrapper);
        if (one != null) {
            if (one.getName().equals(feedback.getName())) {
                return Result.fail("添加失败，该用户已经存在相同信息反馈");
            }
        }

        Feedback save = feedbackService.save(feedback);
        if (save == null) {
            return Result.fail("添加或更新失败");
        }
        return Result.ok();


    }

    /**
     * 删除反馈信息
     */
    @DeleteMapping("deleteFeedBack/{id}")
    public Result deleteFeedBack(@PathVariable Integer id) {
//        if (Assert.isEmpty(loginUser)) {
//            return Result.fail("请登录");
//        }
//        if (loginUser.getRoleStatus() != 1) {
//            return Result.fail("权限不足");
//        }
        if (Assert.isEmpty(id)) {
            return Result.fail("参数错误");
        }
        Feedback feedback = feedbackService.get(id);
        if (feedback != null) {
            feedbackService.delete(id);
            return Result.ok();
        } else {
            return Result.fail("要删除的反馈不存在,删除失败");
        }

    }

}
