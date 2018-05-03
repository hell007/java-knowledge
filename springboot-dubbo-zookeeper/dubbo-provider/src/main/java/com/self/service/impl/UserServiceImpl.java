package com.self.service.impl;

import java.util.List;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.self.model.User;
import com.self.service.UserService;

/**
 * UserServiceImpl
 * @author wzh
 * @time 2017-10-21
 * 注意接口上什么都不加，但是其实现类上要加上一个注解@Service 
 * 注意该注解是com.alibaba.dubbo.config.annotation.Service 如果引入错误，发布也不会成功的
 * 
 */

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{

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
		Criteria criteria = example.createCriteria();
		PageHelper.orderBy("created desc");
		//调用插件进行分页，会将后一句查询进行分页
		PageHelper.startPage(pageNum, pageSize);
        return mapper.selectByExample(example);
	}
	
	
	
}
