package com.self.dao;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import com.self.model.User;

/**
 * MyBatis动态SQL语句构建器，链式SQL构建始于version 3.4.2
 * http://www.cnblogs.com/ibook360/archive/2012/07/16/2594056.html
 */
@Component
public class UserSqlProvider {
	
	/**
	 * 查询全部
	 * 注意：返回的结果集中id,name,phone有值，其他字段值null
	 * @return
	 */
	public String getAll() {  
		SQL sql = new SQL()
					.SELECT("id,name,phone")
					.FROM("self_user");  
        return sql.toString();  
    }
	
	/**
	 * 多表查询
	 * @return
	 */
	public static String queryUserAddress(){
        SQL sql = new SQL()
        		.SELECT("u.id, u.name*")
        		.SELECT("a.id, a.addressname")
        		.FROM("self_user u")
        		.LEFT_OUTER_JOIN("myself_user_address a on u.id = a.uid")
        		.WHERE("id=#{id}");
        return sql.toString();
    }
	
    /**
     * 根据用户名和密码查询
     * @param params
     * @return
     */
    public String getUser(Map<String, String> params){
        String name = params.get("name");
        String password = params.get("password");
        SQL sql = new SQL()
        			.SELECT("*")
        			.FROM("self_user");
        if(!StringUtils.isEmpty(name)){
            sql.WHERE("name=#{name}");
        }
        if(!StringUtils.isEmpty(password)){
            sql.WHERE("passwd=#{password}");
        }
        return sql.toString();
    }

    /**
     * 修改用户名和密码
     * @param user
     * @return
     */
    public String updateUser(User user){
        SQL sql = new SQL()
        			.UPDATE("self_user")
        			.SET("phone = #{phone}, gender = #{gender}")
        			.WHERE("id = #{id}");
        return sql.toString();
    }
    
    /**
     * 添加用户
     * @return
     */
    public String insertUser() {  
    	SQL sql = new SQL()
    				.INSERT_INTO("self_user")
    				.VALUES("id", "#{user.id,javaType=string,jdbcType=VARCHAR}")
    				.VALUES("name", "#{user.userName,javaType=string,jdbcType=VARCHAR}");  
        return sql.toString();  
    }
    
    /**
     * 删除用户
     * @return
     */
    public String deleteUser() {  
    	
    	SQL sql = new SQL()
    				.DELETE_FROM("self_user")
    				.WHERE("id = #{id}");  
        return sql.toString();  
    }
    
    public static void main(String[] args) {
		// 测试
		System.out.println("======"+queryUserAddress());
	}
    
}
