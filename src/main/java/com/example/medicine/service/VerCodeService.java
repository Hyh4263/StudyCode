package com.example.medicine.service;

import com.example.medicine.dao.VerCodeMapper;
import com.example.medicine.entity.VerCode;
import com.example.medicine.utils.Assert;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Hyh
 * @description:
 */
@Service
public class VerCodeService implements IService<VerCode> {

    private VerCodeMapper verCodeMapper;


    @Override
    public VerCode save(VerCode verCode) {
        if (verCode != null) {
            verCodeMapper.insert(verCode);
        }
        return verCodeMapper.selectById(verCode.getId());
    }

    @Override
    public VerCode get(Serializable id) {
        return null;
    }

    @Override
    public int delete(Serializable id) {
        return 0;
    }

    @Override
    public List<VerCode> query(VerCode o) {
        return null;
    }

    @Override
    public List<VerCode> all() {
        return null;
    }
}
