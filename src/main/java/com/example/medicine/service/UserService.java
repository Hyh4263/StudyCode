package com.example.medicine.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.medicine.dao.UserDao;

import com.example.medicine.dao.VerCodeMapper;
import com.example.medicine.entity.Healthy;
import com.example.medicine.entity.User;
import com.example.medicine.entity.VerCode;
import com.example.medicine.utils.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 用户服务类
 *
 * @author XUEW
 */
@Service
public class UserService extends BaseService<User> {

    @Autowired
    protected UserDao userDao;


    @Override
    public List<User> query(User o) {
        QueryWrapper<User> wrapper = new QueryWrapper();
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
    public List<User> all() {
        return query(null);
    }

    public List<User> UserList() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//        userQueryWrapper.eq("role_status", 1);
        userQueryWrapper.orderByDesc("create_time");
        return userDao.selectList(userQueryWrapper);

    }


    @Override
    public User save(User o) {
        if (Assert.isEmpty(o.getId())) {
            userDao.insert(o);
        } else {
            userDao.updateById(o);
        }
        return userDao.selectById(o.getId());
    }

    @Override
    public User get(Serializable id) {
        return userDao.selectById(id);
    }

    @Override
    public int delete(Serializable id) {
        return userDao.deleteById(id);
    }

    public User getProfileByAccount(String userAccount) {
        return userDao.selectOne(new QueryWrapper<User>().eq("user_account", userAccount));
    }

    // 根据username查询所有用户
    // select * from user where username != #{username};"

    public List<User> selectAllUser(String username) {
//        return userDao.selectList(new QueryWrapper<User>().ne("user_name", username));
        return userDao.selectList(new QueryWrapper<User>().ne("user_name", username));

    }

    //根据username查询用户id
    public Integer selectUserId(String username) {
        return userDao.selectOne(new QueryWrapper<User>().eq("user_name", username)).getId();
    }

    //    selectUserDtoByUserId
    public User selectUserByUserId(Integer id) {
        return userDao.selectById(id);
    }

    //根据用户账号和密码获取用户信息
    public User getUserByAccountAndPassword(String userAccount, String password) {
        return userDao.selectOne(new QueryWrapper<User>().eq("user_account", userAccount).eq("user_pwd", password));
    }

    //修改用户信息
    public int updateUser(User user) {
        if (Assert.isEmpty(user.getId())) {
            return -1;
        }
        // 清除账号和密码字段
        user.setUserAccount(null);
        user.setUserPwd(null);
        return userDao.updateById(user);
    }

    public User findUserByUserAccount(String userName) {
        return userDao.selectOne(new QueryWrapper<User>().eq("user_name", userName));
    }


    public Map<String, Object> findList(Integer page, Integer pageSize) {
        return null;
    }

    public Page<User> findUserList(Integer pageNow, Integer pageSize) {

        Page<User> HealthyPage = new Page<>(pageNow, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        Page<User> page = userDao.selectPage(HealthyPage, wrapper);
        return page;
    }

    public void updateUserAvatarById(User user) {
//        UPDATE `user` set avatar=#{avatar} WHERE id=#{id}

        userDao.updateById(user);
    }

    public void updateUserAvatar(Integer userId, String imgPath) {

        // 检查用户是否存在
        User user = userDao.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 使用 MyBatis-Plus 的 updateById 方法更新用户头像路径
        User updateUser = new User();
        updateUser.setImgPath(imgPath);
        updateUser.setId(userId);
        int result = userDao.updateById(updateUser);
        if (result != 1) {
            throw new RuntimeException("Failed to update user avatar");
        }


    }
}