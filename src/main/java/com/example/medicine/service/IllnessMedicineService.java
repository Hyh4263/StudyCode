package com.example.medicine.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.medicine.dao.IllnessMedicineDao;
import com.example.medicine.entity.IllnessMedicine;
import com.example.medicine.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 疾病药品服务类
 *
 * @author XUEW
 */
@Service
public class IllnessMedicineService extends BaseService<IllnessMedicine> {

    @Autowired
    protected IllnessMedicineDao illnessMedicineDao;

    @Override
    public List<IllnessMedicine> query(IllnessMedicine o) {
        QueryWrapper<IllnessMedicine> wrapper = new QueryWrapper();
        if (Assert.notEmpty(o)) {
            Map<String, Object> bean2Map = BeanUtil.bean2Map(o);
            for (String key : bean2Map.keySet()) {
                if (Assert.isEmpty(bean2Map.get(key))) {
                    continue;
                }
                wrapper.eq(VariableNameUtils.humpToLine(key), bean2Map.get(key));
            }
        }
        return illnessMedicineDao.selectList(wrapper);
    }

    @Override
    public List<IllnessMedicine> all() {
        return query(null);
    }

    @Override
    public IllnessMedicine save(IllnessMedicine o) {
        if (Assert.isEmpty(o.getId())) {
            illnessMedicineDao.insert(o);
        } else {
            illnessMedicineDao.updateById(o);
        }
        return illnessMedicineDao.selectById(o.getId());
    }

    @Override
    public IllnessMedicine get(Serializable id) {
        return illnessMedicineDao.selectById(id);
    }


    @Override
    public int delete(Serializable id) {
        return illnessMedicineDao.deleteById(id);
    }

    /**
     * 判断药品功效是否匹配疾病的症状
     *
     * @param medicineEffect 药品的功效描述
     * @param illnessSymptom 疾病的症状描述
     * @return 如果匹配则返回 true，否则返回 false
     */
    public boolean isEffectMatchingSymptom(String medicineEffect, String illnessSymptom) {
        // 简单的关键词匹配，可以考虑更复杂的匹配逻辑，比如基于 NLP 或数据库中存储的标准化症状关键词
        if (medicineEffect == null || illnessSymptom == null) {
            return false;
        }

        // 将症状按逗号、空格等分隔成关键词数组，进行匹配
        String[] symptoms = illnessSymptom.split("[,，、 ]");
        for (String symptom : symptoms) {
            if (medicineEffect.contains(symptom)) {
                return true;
            }
        }
        return false;
    }

//根据疾病ID查询所有列表
    public List<IllnessMedicine> findList(QueryWrapper queryWrapper) {
        return illnessMedicineDao.selectList(queryWrapper);
    }

}