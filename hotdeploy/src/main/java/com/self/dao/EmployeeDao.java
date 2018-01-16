package com.self.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.self.pojo.Employee;

public interface EmployeeDao extends Repository<Employee,Integer>{

    List<Employee> findAll();

    //JPQl,修改和删除需要用@Modifying注解，且调用的方法需要加事务
    @Modifying 
    @Query("update Employee e set e.name = ?1 where e.id = ?2") 
    void modifyEmployee(String name, Integer id);

    //JPQl查找
    @Query("select e from Employee e where key like %:un%")  
    List<Employee> findByKey(@Param("un") String key);

    <S extends Employee> Iterable<S> save(Iterable<S> entities);

    <S extends Employee> S save(S entity);

    void delete(Integer id);
    
    
    
}
