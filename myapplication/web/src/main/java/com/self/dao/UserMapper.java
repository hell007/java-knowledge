package com.self.dao;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import tk.mybatis.mapper.common.Mapper;

import com.self.model.User;

public interface UserMapper extends Mapper<User> {
	
	//mybatis动态sql
	
	@SelectProvider(type = UserSqlProvider.class, method = "getUser")
    User getUser(@Param("name") String name, @Param("password") String password);
	
	@InsertProvider(type = UserSqlProvider.class, method = "insertUser")
    void insertUser(User user);

    @UpdateProvider(type = UserSqlProvider.class, method = "updateUser")
    void updateUser(User user);
    
    @DeleteProvider(type = UserSqlProvider.class, method = "deleteUser")
    void deleteUser(User user);
}
