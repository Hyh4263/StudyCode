package com.example.medicine.controller;

import com.example.medicine.dto.Result;
import com.example.medicine.entity.IllnessMedicine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 疾病药品控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("illness_medicine")
public class IllnessMedicineController extends BaseController<IllnessMedicine> {

    /**
     * 根据疾病ID查询疾病关联药品
     */
    @GetMapping ("findMedicineByIllnessId/{illnessId}")
    public Object findMedicineByIllnessId(Integer illnessId) {
        return illnessMedicineService.query(new IllnessMedicine().builder().illnessId(illnessId).build());
    }




}
