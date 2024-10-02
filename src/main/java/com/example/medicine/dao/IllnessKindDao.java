package com.example.medicine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.medicine.entity.IllnessKind;
import org.springframework.stereotype.Repository;

/**
 * 疾病分类数据库访问
 *
 * @author XUEW
 */
@Repository
public interface IllnessKindDao extends BaseMapper<IllnessKind> {

}
