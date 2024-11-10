package com.example.medicine.service;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.medicine.dao.MedicineDao;
import com.example.medicine.entity.*;
import com.example.medicine.utils.*;
import com.example.medicine.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 药品服务类
 *
 * @author XUEW
 */
@Service
public class MedicineService extends BaseService<Medicine> {

    @Autowired
    protected MedicineDao medicineDao;

    @Override
    public List<Medicine> query(Medicine o) {
        QueryWrapper<Medicine> wrapper = new QueryWrapper();
        if (Assert.notEmpty(o)) {
            Map<String, Object> bean2Map = BeanUtil.bean2Map(o);
            for (String key : bean2Map.keySet()) {
                if (Assert.isEmpty(bean2Map.get(key))) {
                    continue;
                }
                wrapper.eq(VariableNameUtils.humpToLine(key), bean2Map.get(key));
            }
        }
        return medicineDao.selectList(wrapper);
    }

    @Override
    public List<Medicine> all() {
        return query(null);
    }

    @Override
    public Medicine save(Medicine o) {
        if (Assert.isEmpty(o.getId())) {
            medicineDao.insert(o);
        } else {
            medicineDao.updateById(o);
        }
        return medicineDao.selectById(o.getId());
    }

    @Override
    public Medicine get(Serializable id) {
        return medicineDao.selectById(id);
    }

    @Override
    public int delete(Serializable id) {
        return medicineDao.deleteById(id);
    }

    public Map<String, Object> getMedicineList(String nameValue, Integer page) {

        List<Medicine> medicineList = null;
        Map<String, Object> map = new HashMap<>(4);
        if (Assert.notEmpty(nameValue)) {
            medicineList = medicineDao.selectList(new QueryWrapper<Medicine>().
                    like("medicine_name", nameValue)
                    .or().like("keyword", nameValue)
                    .or().like("medicine_effect", nameValue)
                    .last("limit " + (page - 1) * 9 + "," + page * 9));
        } else {
            medicineList = medicineDao.selectList(new QueryWrapper<Medicine>()
                    .last("limit " + (page - 1) * 9 + "," + page * 9));
        }

        map.put("medicineList", medicineList);
        map.put("size", medicineList.size() < 9 ? 1 : medicineList.size() / 9 + 1);
        return map;
    }

//    public Map<String, Object> searchMedicine(String keyword) {
//
//        Map<String, Object> map = new HashMap<>();
//        QueryWrapper<Medicine> medinceQueryWrapper = new QueryWrapper<>();
//
//
//        // 构建查询条件
//        if (Assert.notEmpty(keyword)) {
//            keyword = keyword.trim(); // 去除关键词前后空格
//            medinceQueryWrapper
//                    .like("medicine_name", keyword)
//                    .or()
//                    .like("keyword", keyword)
//                    .or()
//                    .like("medicine_effect", keyword)
//                    .or()
//                    .like("medicine_brand", keyword)
//                    .or()
//                    .like("interaction", keyword)
//                    .or()
//                    .like("taboo", keyword)
//                    .or()
//                    .like("us_age", keyword)
//                    .or()
//                    .like("medicine_price", keyword)
//            ;
//
//
//        }
//
//        medinceQueryWrapper.orderByDesc("create_time");
//
//        // 查询结果，不使用分页
//        List<Map<String, Object>> list = medicineDao.selectMaps(medinceQueryWrapper);
//        map.put("medicine", list);
//        return map;
//    }

    public List<Search> searchMedicine(String keyword) {
        List<Search> results = new ArrayList<>();
        QueryWrapper<Medicine> medicineQueryWrapper = new QueryWrapper<>();

        // Construct the query
        if (Assert.notEmpty(keyword)) {
            keyword = keyword.trim();
            medicineQueryWrapper
                    .like("medicine_name", keyword)
                    .or()
                    .like("medicine_effect", keyword)
                    .or()
                    .like("medicine_brand", keyword)
                    .or()
                    .like("interaction", keyword)
                    .or()
                    .like("taboo", keyword)
                    .or()
                    .like("us_age", keyword);
        }
        medicineQueryWrapper.orderByDesc("create_time");

        // Fetch medicine records and populate Search objects
        List<Medicine> medicineList = medicineDao.selectList(medicineQueryWrapper);
        for (Medicine medicine : medicineList) {
            Search searchResult = new Search();
            searchResult.setMedicineId(medicine.getId());
            searchResult.setMedicineName(medicine.getMedicineName());
            searchResult.setMedicineEffect(medicine.getMedicineEffect());
            searchResult.setMedicineBrand(medicine.getMedicineBrand());
            searchResult.setInteraction(medicine.getInteraction());
            searchResult.setTaboo(medicine.getTaboo());
            searchResult.setUsAge(medicine.getUsAge());
            searchResult.setMedicinePrice(medicine.getMedicinePrice());
            searchResult.setImgPath(medicine.getImgPath());
            searchResult.setCreateTime(medicine.getCreateTime());
            searchResult.setUpdateTime(medicine.getUpdateTime());
            results.add(searchResult);
        }
        return results;
    }

    public Map<String, Object> findMedicineList(Integer page, Integer pageSize) {
        // 使用 MyBatis Plus 的 Page<> 对象进行分页查询
        Page<Map<String, Object>> pageResult = new Page<>(page, pageSize);
        QueryWrapper<Medicine> medicineQueryWrapper = new QueryWrapper<>();
        medicineQueryWrapper.orderByDesc("create_time");
        Page<Map<String, Object>> medicinePage = medicineDao.selectMapsPage(pageResult, medicineQueryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("medicineList", medicinePage.getRecords());
        map.put("totalPages", pageResult.getPages()); // 总页数
        map.put("totalElements", pageResult.getTotal()); // 总记录数
        map.put("currentPage", pageResult.getCurrent()); // 当前页码
        return map;
    }

    public Medicine getOne(QueryWrapper<Medicine> queryWrapper) {
        return medicineDao.selectOne(queryWrapper);
    }

    // 根据药品ID列表查询所有药品信息
    public List<Medicine> findByIds(List<Integer> ids) {
        return medicineDao.selectBatchIds(ids);
    }
}