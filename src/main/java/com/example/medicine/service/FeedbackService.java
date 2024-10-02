package com.example.medicine.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.medicine.dao.FeedbackDao;
import com.example.medicine.entity.Feedback;
import com.example.medicine.utils.Assert;
import com.example.medicine.utils.BeanUtil;
import com.example.medicine.utils.VariableNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 反馈服务类
 *
 * @author XUEW
 */
@Service
public class FeedbackService extends BaseService<Feedback> {

    @Autowired
    protected FeedbackDao userDao;

    @Override
    public List<Feedback> query(Feedback o) {
        QueryWrapper<Feedback> wrapper = new QueryWrapper();
        if (Assert.notEmpty(o)) {
            Map<String, Object> bean2Map = BeanUtil.bean2Map(o);
            for (String key : bean2Map.keySet()) {
                if (Assert.isEmpty(bean2Map.get(key))) {
                    continue;
                }
                wrapper.eq(VariableNameUtils.humpToLine(key), bean2Map.get(key));
            }
        }
        return userDao.selectList(wrapper);
    }

    @Override
    public List<Feedback> all() {
        return query(null);
    }

    @Override
    public Feedback save(Feedback o) {
        if (Assert.isEmpty(o.getId())) {
            userDao.insert(o);
        } else {
            userDao.updateById(o);
        }
        return userDao.selectById(o.getId());
    }

    @Override
    public Feedback get(Serializable id) {
        return userDao.selectById(id);
    }

    @Override
    public int delete(Serializable id) {
        return userDao.deleteById(id);
    }

    public Page<Feedback> findFeedBacksList(Integer pageNow, Integer pageSize) {

        Page<Feedback> feedbackPage = new Page<>(pageNow, pageSize);
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        Page<Feedback> page = userDao.selectPage(feedbackPage, wrapper);
        return page;
    }
    public Feedback getOne(QueryWrapper<Feedback> queryWrapper) {
        return userDao.selectOne(queryWrapper);
    }
}