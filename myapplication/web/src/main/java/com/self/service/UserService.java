package com.self.service;

import java.util.List;

import com.self.model.User;

/**
 * UserService
 * @author wzh
 * @time 2017-10-21
 */
public interface UserService extends BaseService<User> {
	
	/**
	 * 分页查询
	 * @param User 参数条件
	 * @param pageNum 当前页
	 * @param pageSize 每页显示数
	 * @return
	 */
	public List<User> selectPage(User user,int pageNum, int pageSize);
	
	/**
	 * 根据name查询用户
	 * @param name
	 * @return
	 */
	public User selectUserByName(String name);
	
	
	
	public List<User> getAll();
	
	public User getUser(String name, String password);
	
	public void updateUser(User user);
	
}
