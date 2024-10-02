package com.example.medicine.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.medicine.dao.IllnessDao;
import com.example.medicine.dto.Result;
import com.example.medicine.entity.*;
import com.example.medicine.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 疾病服务类
 *
 * @author XUEW
 */
@Service
public class IllnessService extends BaseService<Illness> {

    @Autowired
    protected IllnessDao illnessDao;

    @Override
    public List<Illness> query(Illness o) {
        QueryWrapper<Illness> wrapper = new QueryWrapper();
        if (Assert.notEmpty(o)) {
            Map<String, Object> bean2Map = BeanUtil.bean2Map(o);
            for (String key : bean2Map.keySet()) {
                if (Assert.isEmpty(bean2Map.get(key))) {
                    continue;
                }
                wrapper.eq(VariableNameUtils.humpToLine(key), bean2Map.get(key));
            }
        }
        return illnessDao.selectList(wrapper);
    }

    @Override
    public List<Illness> all() {
        return query(null);
    }

    @Override
    public Illness save(Illness o) {

        if (Assert.isEmpty(o.getId())) {
            illnessDao.insert(o);
        } else {
            illnessDao.updateById(o);
        }
        return illnessDao.selectById(o.getId());
    }





    @Override
    public Illness get(Serializable id) {
        return illnessDao.selectById(id);
    }

    @Override
    public int delete(Serializable id) {
        return illnessDao.deleteById(id);
    }

//    public Map<String, Object> findIllness(Integer kind, String illnessName, Integer page,Integer pageSize) {
//
//        Map<String, Object> map = new HashMap<>(4);
//        QueryWrapper<Illness> illnessQueryWrapper = new QueryWrapper<>();
//        if (Assert.notEmpty(illnessName)) {
//            illnessQueryWrapper
//                    .like("illness_name", illnessName)
//                    .or()
//                    .like("include_reason", illnessName)
//                    .or()
//                    .like("illness_symptom", illnessName)
//                    .or()
//                    .like("special_symptom", illnessName);
//        }
//        if (kind != null) {
//            if (Assert.notEmpty(illnessName)) {
//                illnessQueryWrapper.last("and (kind_id = " + kind + ") ORDER BY create_time DESC limit " + (page - 1) * 9 + "," + page * 9);
//            } else {
//                illnessQueryWrapper.eq("kind_id", kind);
//                illnessQueryWrapper.orderByDesc("create_time");
//                illnessQueryWrapper.last("limit " + (page - 1) * 9 + "," + page * 9);
//            }
//        } else {
//            illnessQueryWrapper.orderByDesc("create_time");
//            illnessQueryWrapper.last("limit " + (page - 1) * 9 + "," + page * 9);
//
//        }
//        int size = illnessDao.selectMaps(illnessQueryWrapper).size();
//        List<Map<String, Object>> list = illnessDao.selectMaps(illnessQueryWrapper);
//        list.forEach(l -> {
//            Integer id = MapUtil.getInt(l, "id");
//            Pageview pageInfo = pageviewDao.selectOne(new QueryWrapper<Pageview>().eq("illness_id", id));
//            l.put("kindName", "暂无归属类");
//            l.put("create_time", MapUtil.getDate(l, "create_time"));
//            l.put("pageview", pageInfo == null ? 0 : pageInfo.getPageviews());
//            Integer kindId = MapUtil.getInt(l, "kind_id");
//            if (Assert.notEmpty(kindId)) {
//                IllnessKind illnessKind = illnessKindDao.selectById(kindId);
//                if (Assert.notEmpty(illnessKind)) {
//                    l.put("kindName", illnessKind.getName());
//                }
//            }
//        });
//        map.put("illness", list);
//        map.put("size", size < 9 ? 1 : size / 9 + 1);
//        return map;
//    }


    public Map<String, Object> findIllness(Integer kind, String illnessName, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>(4);
        QueryWrapper<Illness> illnessQueryWrapper = new QueryWrapper<>();

        // 构建查询条件
        if (Assert.notEmpty(illnessName)) {
            illnessQueryWrapper
                    .like("illness_name", illnessName)
                    .or()
                    .like("include_reason", illnessName)
                    .or()
                    .like("illness_symptom", illnessName)
                    .or()
                    .like("special_symptom", illnessName);
        }
        if (kind != null) {
            illnessQueryWrapper.eq("kind_id", kind);
        }

        illnessQueryWrapper.orderByDesc("create_time");

        // 使用 MyBatis Plus 的 Page<> 对象进行分页查询
        Page<Map<String, Object>> pageResult = new Page<>(page, pageSize);
        illnessDao.selectMapsPage(pageResult, illnessQueryWrapper);

        List<Map<String, Object>> list = pageResult.getRecords();

        list.forEach(l -> {
            Integer id = MapUtil.getInt(l, "id");
            Pageview pageInfo = pageviewDao.selectOne(new QueryWrapper<Pageview>().eq("illness_id", id));
            l.put("kindName", "暂无归属类");
            l.put("create_time", MapUtil.getDate(l, "create_time"));
            l.put("pageview", pageInfo == null ? 0 : pageInfo.getPageviews());

            Integer kindId = MapUtil.getInt(l, "kind_id");
            if (Assert.notEmpty(kindId)) {
                IllnessKind illnessKind = illnessKindDao.selectById(kindId);
                if (Assert.notEmpty(illnessKind)) {
                    l.put("kindName", illnessKind.getName());
                }
            }
        });

        map.put("illness", list);
        map.put("totalPages", pageResult.getPages()); // 总页数
        map.put("totalElements", pageResult.getTotal()); // 总记录数
        map.put("currentPage", pageResult.getCurrent()); // 当前页码
        return map;
    }


//    public Map<String, Object> findIllness(Integer kind, String illnessName, Integer page, Integer pageSize) {
//        Map<String, Object> map = new HashMap<>(4);
//
//        // 创建分页对象，page 为当前页，pageSize 为每页显示的条数
//        Page<Illness> pageObj = new Page<>(page, pageSize);
//        QueryWrapper<Illness> illnessQueryWrapper = new QueryWrapper<>();
//
//        // 根据 illnessName 和 kind 进行查询
//        if (Assert.notEmpty(illnessName)) {
//            illnessQueryWrapper
//                    .like("illness_name", illnessName)
//                    .or()
//                    .like("include_reason", illnessName)
//                    .or()
//                    .like("illness_symptom", illnessName)
//                    .or()
//                    .like("special_symptom", illnessName);
//        }
//        if (kind != null) {
//            illnessQueryWrapper.eq("kind_id", kind);
//
//        }
//
//
//        // 添加排序条件
//        illnessQueryWrapper.orderByDesc("create_time");
//
//        // 执行分页查询
//        Page<Illness> illnessPage = illnessDao.selectPage(pageObj, illnessQueryWrapper);
//
//        List<Map<String, Object>> illnessList = new ArrayList<>();
//        for (Illness illness : illnessPage.getRecords()) {
//            Map<String, Object> illnessMap = BeanUtil.bean2Map(illness);
//
//            IllnessKind illnessKind = illnessKindDao.selectById(illness.getKindId());
//            if (illnessKind != null) {
//                illnessMap.put("kindName", illnessKind.getName());
//            } else {
//                illnessMap.put("kindName", "暂无归属类");
//            }
//
//            illnessList.add(illnessMap);
//        }
//
//        // 查询结果和分页信息封装进返回 Map
//        map.put("illnessList", illnessPage.getRecords()); // 当前页的数据
//        map.put("totalPages", illnessPage.getPages()); // 总页数
//        map.put("totalElements", illnessPage.getTotal()); // 总记录数
//        map.put("currentPage", illnessPage.getCurrent()); // 当前页码
//        return map;
//    }

    public Map<String, Object> findIllnessOne(Integer id) {
        Illness illness = illnessDao.selectOne(new QueryWrapper<Illness>().eq("id", id));
        List<IllnessMedicine> illnessMedicines = illnessMedicineDao.selectList(new QueryWrapper<IllnessMedicine>().eq("illness_id", id));
        List<Medicine> list = new ArrayList<>(4);
        Map<String, Object> map = new HashMap<>(4);
        Pageview illness_id = pageviewDao.selectOne(new QueryWrapper<Pageview>().eq("illness_id", id));
        if (Assert.isEmpty(illness_id)) {
            illness_id = new Pageview();
            illness_id.setIllnessId(id);
            illness_id.setPageviews(1);
            pageviewDao.insert(illness_id);
        } else {
            illness_id.setPageviews(illness_id.getPageviews() + 1);
            pageviewDao.updateById(illness_id);
        }
        map.put("illness", illness);

        if (CollUtil.isNotEmpty(illnessMedicines)) {
            illnessMedicines.forEach(illnessMedicine -> {
                Medicine medicine = medicineDao.selectOne(new QueryWrapper<Medicine>().eq("id", illnessMedicine.getMedicineId()));
                if (ObjectUtil.isNotNull(medicine)) {
                    list.add(medicine);
                }
            });
            map.put("medicine", list);

        }

        return map;
    }

    public Illness getOne(QueryWrapper<Illness> queryWrapper) {
        return illnessDao.selectOne(queryWrapper);
    }


}