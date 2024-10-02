package com.example.medicine.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.medicine.dao.DiseaseDao;
import com.example.medicine.entity.Illness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseService {

    @Autowired
    private DiseaseDao diseaseDao;


    public List<Illness> getOrderInfoList(int pageNow, int pageSize) {
        Page<Illness> page = new Page<>(pageNow, pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        IPage<Illness> iPage = diseaseDao.selectPage(page, wrapper);
        return iPage.getRecords();
    }


}
