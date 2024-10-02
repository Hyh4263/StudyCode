package com.example.medicine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.medicine.entity.Pageview;
import org.springframework.stereotype.Repository;

/**
 * 分页数据库访问
 *
 * @author XUEW
 */
@Repository
public interface PageviewDao extends BaseMapper<Pageview> {

}
