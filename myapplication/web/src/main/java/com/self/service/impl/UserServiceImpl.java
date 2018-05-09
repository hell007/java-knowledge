package com.self.service.impl;

import java.util.List;

import org.apache.ibatis.type.StringTypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.github.pagehelper.PageHelper;
import com.self.dao.UserMapper;
import com.self.model.User;
import com.self.service.UserService;
import com.self.utils.StringUtils;

/**
 * UserServiceImpl
 * @author wzh
 * @time 2017-10-21
 */

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{
	
	@Autowired
	private UserMapper userMapper;

	/**
	 * 分页查询
	 * @param User 参数条件
	 * @param pageNum 当前页
	 * @param pageSize 每页显示数
	 * @return
	 */
	@Override
	@SuppressWarnings("deprecation") 
	public List<User> selectPage(User user,int pageNum, int pageSize) {
		Example example = new Example(User.class);
		PageHelper.orderBy("created desc");
		//调用插件进行分页，会将后一句查询进行分页
		PageHelper.startPage(pageNum, pageSize);
        return mapper.selectByExample(example);
	}
	
	/**
	 * 根据name查询用户
	 * @param name
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@Override
	public User selectUserByName(String name){
		Example example = new Example(User.class);
		Criteria criteria = example.createCriteria();
		if(!StringUtils.isNotBlank(name)) {return null;}
		criteria.andCondition("name like", "%"+name+"%", StringTypeHandler.class);
		List<User> list = mapper.selectByExample(example);
		return list.get(0);
	}
	
	
	@Override
	public List<User> getAll() {
		return userMapper.getAll();
	}

	@Override
	public User getUser(String name, String password) {
		return userMapper.getUser(name, password);
	}

	@Override
	public void updateUser(User user) {
		userMapper.updateUser(user);	
	}

	
	
}
