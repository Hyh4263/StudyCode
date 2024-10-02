package com.example.medicine.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.medicine.entity.Feedback;
import org.springframework.stereotype.Repository;

/**
 * 反馈数据库访问
 *
 * @author XUEW
 */
@Repository
public interface FeedbackDao extends BaseMapper<Feedback> {

}
