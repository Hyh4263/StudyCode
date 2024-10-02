package com.example.medicine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.medicine.entity.IllnessMedicine;
import org.springframework.stereotype.Repository;

/**
 * 疾病药品数据库访问
 *
 * @author XUEW
 */
@Repository
public interface IllnessMedicineDao extends BaseMapper<IllnessMedicine> {

}
