package com.self.service;

import java.util.List;

import com.self.pojo.Employee;

public interface EmployeeService {
	
	//查找所有
    public List<Employee> findAll();

    //保存或者修改list<pojo>
    public void save(List<Employee> params);

    //保存或者修改pojo
    public void save(Employee emp);

    //通过id删除
    public void delete(Integer id);

    //查找通过key
    public List<Employee> findByKey(String key);

    //修改value通过匹配key
    public void modifyEmployee(String value,Integer id);
}
