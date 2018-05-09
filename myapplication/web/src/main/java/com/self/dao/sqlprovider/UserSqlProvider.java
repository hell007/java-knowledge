package com.self.dao.sqlprovider;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import com.self.model.User;

/**
 * MyBatis动态SQL语句构建器，链式SQL构建始于version 3.4.2
 */
@Component
public class UserSqlProvider {

    /**
     * 根据用户名和密码查询
     * @param params
     * @return
     */
    public String getUser(Map<String, String> params){
        String name = params.get("name");
        String password = params.get("password");
        SQL sql = new SQL().SELECT("*").FROM("self_user");
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
        SQL sql = new SQL().UPDATE("self_user").SET("phone = #{phone}, gender = #{gender}")
                .WHERE("id=#{id}");
        return sql.toString();
    }
}
