package com.example.medicine.controller;

import com.example.medicine.entity.Illness;
import com.example.medicine.service.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("illness")
public class DiseaseController {

    @Autowired
    private DiseaseService diseaseService;

    //疾病分页查询
    @GetMapping("/diseases")
    public List<Illness> getOrderInfoList(@RequestParam(value = "pageNow", defaultValue = "1") int pageNow,
                                          @RequestParam(value = "pageSize", defaultValue = "3") int pageSize) {

        return diseaseService.getOrderInfoList(pageNow, pageSize);
    }


}
