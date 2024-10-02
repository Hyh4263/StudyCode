package com.example.medicine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.medicine.constant.MedicalConstants;
import com.example.medicine.dto.Result;
import com.example.medicine.entity.Illness;
import com.example.medicine.entity.Medicine;
import com.example.medicine.utils.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 药品控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("medicine")
public class MedicineController extends BaseController<Medicine> {


    /**
     * 查询相关疾病下的药
     */
//    @GetMapping("findMedicines")
//    public Result findMedicines(Map<String, Object> map, String nameValue, Integer page) {
//        // 处理page
//        page = ObjectUtils.isEmpty(page) ? 1 : page;
//        if (loginUser != null && Assert.notEmpty(nameValue)) {
//            historyService.insetOne(loginUser.getId(), MedicalConstants.TYPE_MEDICINE, nameValue);
//        }
//        map.putAll(medicineService.getMedicineList(nameValue, page));
//        map.put("history", loginUser == null ? null : historyService.findList(loginUser.getId()));
//        map.put("title", nameValue);
//        return Result.ok(map);
//    }

    /**
     * 分页查询所有药品
     */
    @GetMapping("findMedicines")
    public Result findMedicines(@RequestParam(defaultValue = "1") Integer pageNow, @RequestParam(defaultValue = "5") Integer pageSize) {

//        if (loginUser != null && Assert.notEmpty(nameValue)) {
//            historyService.insetOne(loginUser.getId(), MedicalConstants.TYPE_MEDICINE, nameValue);
//        }


        Map<String, Object> medicines = medicineService.findMedicineList(pageNow, pageSize);
        return Result.ok(medicines);


    }


    @GetMapping("findMedicineOne/{id}")
    public Result findMedicineOne(Map<String, Object> map, @PathVariable Integer id) {
        Medicine medicine = medicineService.get(id);
//        historyService.insetOne(loginUser.getId(),MedicalConstants.TYPE_MEDICINE,medicine.getMedicineName());
        map.put("medicine", medicine);
        return Result.ok(map);
    }


    /**
     * 更新/添加疾病信息
     */
    @PostMapping("saveMedicine")
    public Result saveMedicine(@RequestBody Medicine medicine) {
//        if (Assert.isEmpty(loginUser)) {
//            return Result.fail("请登录");
//        }
//        if (loginUser.getRoleStatus() != 1) {
//            return Result.fail("权限不足");
//        }
        if (Assert.isEmpty(medicine.getId())) {
            //添加
            QueryWrapper<Medicine> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("medicine_name", medicine.getMedicineName());
            if (medicineService.getOne(queryWrapper) != null) {
                return Result.fail("添加失败，该 illnesses 已经存在");
            }
        }

        //更新或添加疾病信息
        Medicine save = medicineService.save(medicine);
        if (save == null) {
            return Result.fail("添加或更新失败");
        }
        return Result.ok();

    }

    /**
     * 删除疾病信息
     */
    @DeleteMapping("deleteMedicine/{id}")
    public Result deleteMedicine(@PathVariable Integer id) {
//        if (Assert.isEmpty(loginUser)) {
//            return Result.fail("请登录");
//        }
//        if (loginUser.getRoleStatus() != 1) {
//            return Result.fail("权限不足");
//        }
        if (Assert.isEmpty(id)) {
            return Result.fail("参数错误");
        }
        medicineService.delete(id);
        Medicine medicine = medicineService.get(id);
        if (medicine != null) {
            return Result.fail("删除失败");
        }
        return Result.ok();
    }

}
