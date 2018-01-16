package com.self.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.self.dao.EmployeeDao;
import com.self.pojo.Employee;
import com.self.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	/**
     * 日志记录.
     */
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);


    @Autowired
    private EmployeeDao paramDao;


    //查找所有
    @Override
    public List<Employee> findAll() {
        try {
            return paramDao.findAll();
        } catch (Exception e) {
            logger.error("find systemparameters list failure", e);
            return null;
        }
    }


    //保存或者修改list<pojo>
    @Override
    public void save(List<Employee> params) {
        try {
            paramDao.save(params);
        } catch (Exception e) {
            logger.error("save systemparametersList failure: ", e);
            throw new RuntimeException(e);
        }
    }

    //保存或者修改pojo
    @Override
    public void save(Employee systemParameters) {
        try{
            paramDao.save(systemParameters);
        }catch(Exception e){
            logger.error("save systemparameters failure: ", e);
            throw new RuntimeException(e);
        }
    }

    //通过id删除
    @Override
    public void delete(Integer id) {
        try {
            paramDao.delete(id);
        } catch (Exception e) {
            logger.error("delete systemparameters failure: ", e);
            throw new RuntimeException(e);
        }
    }

    //查找通过key
    @Override
    public List<Employee> findByKey(String key) {
        List<Employee> list ;
        try {
            list = paramDao.findByKey(key);
        } catch (Exception e) {
            logger.error("find systemparameters by key failure: ", e);
            return null;
        }
        return list;
    }

    //修改value通过匹配key
    @Override
    @Transactional
    public void modifyEmployee(String value, Integer id) {
        try {
            paramDao.modifyEmployee(value,id);
        } catch (Exception e) {
            logger.error("update systemParameters by key failure: ", e);
            throw new RuntimeException(e);
        }
    } 
}
