package com.example.medicine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.medicine.entity.Illness;
import org.springframework.stereotype.Repository;

/**
 * 疾病数据库访问
 *
 * @author XUEW
 */
@Repository
public interface IllnessDao extends BaseMapper<Illness> {

}
