package com.example.medicine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.medicine.entity.User;
import org.springframework.stereotype.Repository;

/**
 * 用户数据库访问
 *
 * @author XUEW
 */
@Repository
public interface UserDao extends BaseMapper<User> {

}
