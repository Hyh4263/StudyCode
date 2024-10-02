package com.example.medicine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.medicine.entity.History;
import org.springframework.stereotype.Repository;

/**
 * 历史数据库访问
 *
 * @author XUEW
 */
@Repository
public interface HistoryDao extends BaseMapper<History> {

}
