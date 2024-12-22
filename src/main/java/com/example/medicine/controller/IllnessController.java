package com.example.medicine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.medicine.constant.MedicalConstants;
import com.example.medicine.dto.RespResult;
import com.example.medicine.dto.Result;
import com.example.medicine.entity.Illness;
import com.example.medicine.entity.IllnessMedicine;
import com.example.medicine.entity.Medicine;
import com.example.medicine.utils.Assert;
import org.apache.ibatis.annotations.Delete;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 疾病控制器
 *
 * @author Hyh
 */
@RestController
@RequestMapping("illness")
public class IllnessController extends BaseController<Illness> {


    /**
     * 查询相关疾病
     */
//    @GetMapping("findIllness")
//    public Result findIllness(Map<String, Object> map, Integer kind, String illnessName, Integer page) {
//        // 处理page
//        page = ObjectUtils.isEmpty(page) ? 1 : page;
//
//        Map<String, Object> illness = illnessService.findIllness(kind, illnessName, page);
//        if (Assert.notEmpty(kind)) {
//            map.put("title", illnessKindService.get(kind).getName() + (illnessName == null ? "" : ('"' + illnessName + '"' + "的搜索结果")));
//        } else {
//            map.put("title", illnessName == null ? "全部" : ('"' + illnessName + '"' + "的搜索结果"));
//        }
//        if (loginUser != null && kind != null) {
//            historyService.insetOne(loginUser.getId(), MedicalConstants.TYPE_OPERATE,
//                    illnessKindService.get(kind).getId() + "," + (Assert.isEmpty(illnessName) ? "无" : illnessName));
//        }
//        if (loginUser != null && Assert.notEmpty(illnessName)) {
//            historyService.insetOne(loginUser.getId(), MedicalConstants.TYPE_ILLNESS, illnessName);
//        }
//        map.putAll(illness);
//        map.put("page", page);
//        map.put("kind", kind);
//        map.put("illnessName", illnessName);
//        map.put("kindList", illnessKindService.findList());
//        map.put("history", loginUser == null ? null : historyService.findList(loginUser.getId()));
//        return Result.ok(map);
//    }

    /**
     * 分页查询相关疾病
     */
    @GetMapping("findIllness")
    public Result findIllness(Map<String, Object> map, Integer kind, String illnessName, @RequestParam(defaultValue = "1") Integer pageNow, @RequestParam(defaultValue = "10") Integer pageSize) {

        // 调用service层的分页查询方法
        Map<String, Object> illness = illnessService.findIllness(kind, illnessName, pageNow, pageSize);

        // 设置页面标题
        if (Assert.notEmpty(kind)) {
            map.put("title", illnessKindService.get(kind).getName() + (illnessName == null ? "" : ('"' + illnessName + '"' + "的搜索结果")));
        } else {
            map.put("title", illnessName == null ? "全部" : ('"' + illnessName + '"' + "的搜索结果"));
        }

        // 插入历史记录
        if (loginUser != null && kind != null) {
            historyService.insetOne(loginUser.getId(), MedicalConstants.TYPE_OPERATE,
                    illnessKindService.get(kind).getId() + "," + (Assert.isEmpty(illnessName) ? "无" : illnessName));
        }
        if (loginUser != null && Assert.notEmpty(illnessName)) {
            historyService.insetOne(loginUser.getId(), MedicalConstants.TYPE_ILLNESS, illnessName);
        }

        // 添加查询结果和其他信息到 map 中
        map.putAll(illness);
        map.put("page", pageNow);
        map.put("pageSize", pageSize);
        map.put("kind", kind);
        map.put("illnessName", illnessName);
        map.put("kindList", illnessKindService.findList());
        map.put("history", loginUser == null ? null : historyService.findList(loginUser.getId()));

        return Result.ok(map);
    }

    /**
     * 根据疾病ID查找疾病详情
     */
    @GetMapping("findIllnessOne/{illnessId}")
    public Result findIllnessOne(@PathVariable Integer illnessId) {
        Map<String, Object> map = new HashMap<>();

        // 获取疾病的详细信息
        Map<String, Object> illnessOne = illnessService.findIllnessOne(illnessId);
        if (illnessOne == null) {
            return Result.fail("没有找到该疾病详情数据");
        }
//        // 查找疾病关联的所有 IllnessMedicine 对象
//
//        QueryWrapper<IllnessMedicine> wrapper = new QueryWrapper<>();
//        wrapper.eq("illness_id", illnessId);
//        List<IllnessMedicine> illnessMedicines = illnessMedicineService.findList(wrapper);
//
//        if (illnessMedicines == null || illnessMedicines.isEmpty()) {
//            return Result.fail("没有找到该疾病关联的药品");
//        }
//
//        // 提取所有药品ID
//        List<Integer> medicineIds = illnessMedicines.stream()
//                .map(IllnessMedicine::getMedicineId)
//                .collect(Collectors.toList());
//
//        // 根据药品ID列表查询所有药品信息
//        List<Medicine> medicines = medicineService.findByIds(medicineIds);
//        if (medicines == null || medicines.isEmpty()) {
//            return Result.fail("没有找到与该疾病关联的药品信息");
//        }

        // 将疾病信息和药品信息放入 map 中
        map.put("illnessDetail", illnessOne);
//        map.put("illnessMedicines", medicines);

        return Result.ok(map);
    }


    /**
     * 更新/添加疾病信息
     */
    @PostMapping("saveIllness")
    public Result saveIllness(@RequestBody Illness illness) {
//        if (Assert.isEmpty(loginUser)) {
//            return Result.fail("请登录");
//        }
//        if (loginUser.getRoleStatus() != 1) {
//            return Result.fail("权限不足");
//        }
        if (Assert.isEmpty(illness.getId())) {
            //添加
            QueryWrapper<Illness> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("illness_name", illness.getIllnessName());
            queryWrapper.eq("kind_id", illness.getKindId());
            queryWrapper.eq("img_path", illness.getImgPath());
            if (illnessService.getOne(queryWrapper) != null) {
                return Result.fail("添加失败，该 illnesses 已经存在");
            }
        }

        //更新或添加疾病信息
        Illness save = illnessService.save(illness);
        if (save == null) {
            return Result.fail("添加或更新失败");
        }
        return Result.ok();

    }

    /**
     * 删除疾病信息
     */
    @DeleteMapping("deleteIllness/{id}")
    public Result deleteIllness(@PathVariable Integer id) {
//        if (Assert.isEmpty(loginUser)) {
//            return Result.fail("请登录");
//        }
//        if (loginUser.getRoleStatus() != 1) {
//            return Result.fail("权限不足");
//        }
        if (Assert.isEmpty(id)) {
            return Result.fail("参数错误");
        }
        illnessService.delete(id);
        Illness illness = illnessService.get(id);
        if (illness != null) {
            return Result.fail("删除失败");
        }
        return Result.ok();
    }
}
