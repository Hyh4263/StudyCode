package com.example.medicine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.medicine.constant.MedicalConstants;
import com.example.medicine.constant.SymptomMatcher;
import com.example.medicine.dto.Result;
import com.example.medicine.entity.Illness;
import com.example.medicine.entity.IllnessMedicine;
import com.example.medicine.entity.Medicine;
import com.example.medicine.utils.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 药品控制器
 *
 * @author Hyh
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
//    @PostMapping("saveMedicine")
//    public Result saveMedicine(@RequestBody Medicine medicine) {
////        if (Assert.isEmpty(loginUser)) {
////            return Result.fail("请登录");
////        }
////        if (loginUser.getRoleStatus() != 1) {
////            return Result.fail("权限不足");
////        }
//        if (Assert.isEmpty(medicine.getId())) {
//            //添加
//            QueryWrapper<Medicine> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("medicine_name", medicine.getMedicineName());
//            if (medicineService.getOne(queryWrapper) != null) {
//                return Result.fail("添加失败，该 illnesses 已经存在");
//            }
//        }
//
//        //更新或添加疾病信息
//        Medicine save = medicineService.save(medicine);
//        if (save == null) {
//            return Result.fail("添加或更新失败");
//        }
//        return Result.ok();
//
//    }
    @PostMapping("saveMedicine")
    public Result saveMedicine(@RequestBody Medicine medicine) {
        // 验证登录用户权限
        // if (Assert.isEmpty(loginUser)) {
        //     return Result.fail("请登录");
        // }
        // if (loginUser.getRoleStatus() != 1) {
        //     return Result.fail("权限不足");
        // }

        boolean isNewMedicine = Assert.isEmpty(medicine.getId());

        // 如果是添加操作，检查是否已存在相同名称的药品
        if (isNewMedicine) {
            QueryWrapper<Medicine> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("medicine_name", medicine.getMedicineName());
            queryWrapper.eq("medicine_type", medicine.getMedicineType());

            if (medicineService.getOne(queryWrapper) != null) {
                return Result.fail("添加失败，该药品已存在");
            }
        }

        // 保存或更新药品信息
        Medicine savedMedicine = medicineService.save(medicine);
        if (savedMedicine == null) {
            return Result.fail("添加或更新药品失败");
        }

        // 处理疾病相关的药品逻辑，基于功效与症状的匹配
        associateMedicineWithIllnesses(savedMedicine);

        return Result.ok();
    }

    /**
     * 根据药品的功效和疾病的症状建立疾病与药品的关联关系
     *
     * @param medicine 已保存的药品信息
     */
    private void associateMedicineWithIllnesses(Medicine medicine) {
        List<Illness> illnesses = illnessService.all();
        String medicineEffect = medicine.getMedicineEffect();

        for (Illness illness : illnesses) {
            String illnessName = illness.getIllnessName();

            // 判断功效和症状是否匹配
            if (SymptomMatcher.isEffectMatchingSymptom(medicineEffect, illnessName)) {
                List<IllnessMedicine> existingIllnessMedicines = illnessMedicineService.query(
                        IllnessMedicine.builder()
                                .medicineId(medicine.getId())
                                .illnessId(illness.getId())
                                .build()
                );

                // 如果不存在关联关系，添加新的关联
                if (Assert.isEmpty(existingIllnessMedicines)) {
                    IllnessMedicine illnessMedicine = IllnessMedicine.builder()
                            .illnessId(illness.getId())
                            .medicineId(medicine.getId())
                            .createTime(new Date())
                            .updateTime(new Date())
                            .build();
                    illnessMedicineService.save(illnessMedicine);
                }
            }
        }
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

    /**
     * 查询所有药品
     */
    @GetMapping("allMedicines")
    public Result allMedicines() {
        return Result.ok(medicineService.all());
    }


}
