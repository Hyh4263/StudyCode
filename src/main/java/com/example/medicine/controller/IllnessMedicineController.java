package com.example.medicine.controller;

import com.example.medicine.entity.IllnessMedicine;
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

}
