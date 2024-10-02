package com.example.medicine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.medicine.entity.User;
import com.example.medicine.entity.VerCode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户数据库访问
 *
 * @author XUEW
 */

@Mapper
public interface VerCodeMapper extends BaseMapper<VerCode> {


}
