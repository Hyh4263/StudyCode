package com.example.medicine.service;

import com.example.medicine.dao.*;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 基础服务类
 *
 * @author XUEW
 */

public abstract class BaseService<T> implements IService<T> {

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected HistoryDao historyDao;

    @Autowired
    protected IllnessDao illnessDao;

    @Autowired
    protected IllnessKindDao illnessKindDao;

    @Autowired
    protected IllnessMedicineDao illnessMedicineDao;

    @Autowired
    protected MedicineDao medicineDao;

    @Autowired
    protected MedicalNewsDao medicalNewsDao;

    @Autowired
    protected PageviewDao pageviewDao;

    @Autowired
    protected VerCodeMapper verCodeMapper;

}
