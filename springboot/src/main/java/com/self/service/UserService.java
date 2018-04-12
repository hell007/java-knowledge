
package com.self.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.github.pagehelper.PageHelper;
import com.self.model.User;


@Service
public class UserService extends BaseService<User>{

	
	public List<User> selectPage(User user, int pageNum, int pageSize) {
		Example example = new Example(User.class);
		Criteria criteria = example.createCriteria();
		PageHelper.orderBy("id desc");
		// 调用插件进行分页，会将后一句查询进行分页
		PageHelper.startPage(pageNum, pageSize);
		return mapper.selectByExample(example);
	}
}
