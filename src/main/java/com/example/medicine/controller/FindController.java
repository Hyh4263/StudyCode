package com.example.medicine.controller;


import com.example.medicine.dto.Result;
import com.example.medicine.entity.Illness;
import com.example.medicine.entity.Search;
import com.example.medicine.service.IllnessService;
import com.example.medicine.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Hyh
 * @description:搜索控制器
 *
 */
@RestController
@RequestMapping(value = "search")
public class FindController{
        @Autowired
        private IllnessService illnessService;
        @Autowired
        private MedicineService medicineService;


        /**
         * 根据名字和描述搜索、
         * @param keyword
         * @return
         *
         * APi:http://localhost:8080/search?keyword=感冒
         */
//    @RequestMapping()
//    public Result findByKeyword(@RequestParam String keyword) {
//        keyword = keyword.trim();
//        //创建一个Map用来存放查询到的药品和疾病数据
//        Map<String, Object> map = new HashMap<>();
//
//        //根据关键词查找疾病相关数据
//        Map<String, Object> findIllness = illnessService.searchIllness(keyword);
//        //根据关键词差药品相关数据
//        Map<String, Object> findMedicine = medicineService.searchMedicine(keyword);
//        if (findIllness == null && findMedicine == null) {
//            return Result.fail();
//        }
//        if (findMedicine != null) {
//            map.put("searchMedicine", findMedicine);
//        }
//        if (findIllness != null) {
//            map.put("searchIllness", findIllness);
//        }
//
//        return Result.ok(map);
//    }
        @RequestMapping()
        public Result findByKeyword(@RequestParam String keyword) {
            keyword = keyword.trim();

            // Search in illness and medicine
            List<Search> illnessResults = illnessService.searchIllness(keyword);
            List<Search> medicineResults = medicineService.searchMedicine(keyword);

            // Combine results
            List<Search> allResults = new ArrayList<>();
            allResults.addAll(illnessResults);
            allResults.addAll(medicineResults);

            if (allResults.isEmpty()) {
                return Result.fail("No results found");
            }

            return Result.ok(allResults);
        }

}
