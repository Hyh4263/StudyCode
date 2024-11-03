package com.example.medicine.service;

import com.example.medicine.dao.ImageDao;
import com.example.medicine.dao.MedicineDao;
import com.example.medicine.entity.Image;
import com.example.medicine.entity.MedicalNews;
import com.example.medicine.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Hyh
 * @description:
 */
@Service
public class ImageService extends BaseService<Image>{

    @Autowired
    protected ImageDao imageDao;
    @Override
    public Image save(Image o) {
        if (Assert.isEmpty(o.getId())) {
            imageDao.insert(o);
        } else {
            imageDao.updateById(o);
        }
        return imageDao.selectById(o.getId());
    }

    @Override
    public Image get(Serializable id) {
        return null;
    }

    @Override
    public int delete(Serializable id) {
        return 0;
    }

    @Override
    public List<Image> query(Image o) {
        return null;
    }

    @Override
    public List<Image> all() {
        return null;
    }
}
