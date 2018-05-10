package com.self.dao;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.common.Mapper;

import com.self.model.User;

/**
 * mybatis动态sql注解
 * @author wzh
 * http://www.cnblogs.com/ibook360/archive/2012/07/16/2594056.html
 */
@CacheNamespace(size = 512)
public interface UserMapper extends Mapper<User> {

	
	@SelectProvider(type = UserSqlProvider.class, method = "getAll")  
    @Options(useCache = true, flushCache = false, timeout = 10000)  
    @Results(value = {  
            @Result(id = true, property = "id", column = "id"),  
            @Result(property = "name", column = "name"),
            @Result(property = "phone", column = "phone")
    })  
    List<User> getAll();
	
	
	/*@ResultMap(value = "queryUserAddress") 
	List<User> queryUserAddress();*/
	
	
	@SelectProvider(type = UserSqlProvider.class, method = "getUser")
	@Options(useCache = true, flushCache = false, timeout = 10000)
	@Results(value = {  
			@Result(id = true, property = "id", column = "id", javaType = String.class, jdbcType = JdbcType.VARCHAR),  
			@Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR) 
    }) 
    User getUser(@Param("name") String name, @Param("password") String password);
	
	@InsertProvider(type = UserSqlProvider.class, method = "insertUser")
	@Options(flushCache = true, timeout = 20000)
    void insertUser(@Param("user") User user);

    @UpdateProvider(type = UserSqlProvider.class, method = "updateUser")
    @Options(flushCache = true, timeout = 20000)
    void updateUser(@Param("user") User user);
    
    @DeleteProvider(type = UserSqlProvider.class, method = "deleteUser")
    @Options(flushCache = true, timeout = 20000)
    void deleteUser(@Param("id") String id);
}
