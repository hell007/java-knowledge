package com.self.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.github.pagehelper.PageHelper;
import com.self.utils.BaseMapper;

@Service
public abstract class BaseService<T>{

    @Autowired
    protected BaseMapper<T> mapper;
    
    /**
     * 查询到与入参对象最匹配的数据库中实体
     * @param entity
     * @return
     */
    public T selectOneBy(T entity){
    	return mapper.selectOne(entity);
    }
    
    /**
     * 根据主键进行查询,必须保证结果唯一
     * 单个字段做主键时,可以直接写主键的值
     * 联合主键时,key可以是实体类,也可以是Map
     * @param key
     * @return
     */
    public T selectByKey(Serializable key) {
        return mapper.selectByPrimaryKey(key);
    }
    
    /**
     * 根据实体类不为null的字段查询总数,条件全部使用=号and条件
     * @param entity
     * @return
     */
    public int selectCount(T entity){
    	return mapper.selectCount(entity);
    }
    
    /**
     * 根据实体类不为null的字段进行查询,条件全部使用=号and条件
     * @param entity
     * @return
     */
    public List<T> select(T entity){
    	return mapper.select(entity);
    }

    /**
     * 插入一条数据
     * 支持Oracle序列,UUID,类似Mysql的INDENTITY自动增长(自动回写)
     * 优先使用传入的参数值,参数值空时,才会使用序列、UUID,自动增长
     * @param entity
     * @return
     */
    public int save(T entity) {
        return mapper.insert(entity);
    }
    
    /**
     * 插入一条数据,只插入不为null的字段,不会影响有默认值的字段
     * 支持Oracle序列,UUID,类似Mysql的INDENTITY自动增长(自动回写)
     * 优先使用传入的参数值,参数值空时,才会使用序列、UUID,自动增长
     * @param entity
     * @return
     */
    public int saveNotNull(T entity) {
    	return mapper.insertSelective(entity);
    }
    
    /**
     * 根据实体类中字段不为null的条件进行删除,条件全部使用=号and条件
     * @param entity
     * @return
     */
    public int delete(T entity) {
        return mapper.delete(entity);
    }

    /**
     * 通过主键进行删除,这里最多只会删除一条数据
     * 单个字段做主键时,可以直接写主键的值
     * 联合主键时,key可以是实体类,也可以是Map
     * @param key
     * @return
     */
    public int delete(Serializable key) {
        return mapper.deleteByPrimaryKey(key);
    }
    
    /**
     * 通过主键进行删除,这里最多只会删除一条数据
     * 单个字段做主键时,可以直接写主键的值
     * 联合主键时,key可以是实体类,也可以是Map
     * @param key
     * @return
     */
    public int delete(Class<?> clazz,String[] ids) {
    	Example example = new Example(clazz);
		Criteria criteria = example.createCriteria();
		if(ArrayUtils.isNotEmpty(ids)){
			criteria.andIn("id", Arrays.asList(ids));
		}
    	return mapper.deleteByExample(example);
    }

    /**
     * 根据主键进行更新,这里最多只会更新一条数据
     * @param entity 参数为实体类
     * @return
     */
    public int updateAll(T entity) {
        return mapper.updateByPrimaryKey(entity);
    }

    /**
     * 根据主键进行更新
     * 只会更新不是null的数据
     * @param entity
     * @return
     */
    public int updateNotNull(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    /**
     * 单表分页查询
     * 
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<T> selectPage(int pageNum,int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        //Spring4支持泛型注入
        return mapper.select(null);
    }
}

