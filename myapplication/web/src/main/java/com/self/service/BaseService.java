package com.self.service;

import java.io.Serializable;
import java.util.List;

/**
 * BaseService
 * @author wzh
 * @time 2017-10-21
 */
public interface BaseService<T>{
	
	/**
     * 根据主键进行查询,必须保证结果唯一
     * 单个字段做主键时,可以直接写主键的值
     * 联合主键时,key可以是实体类,也可以是Map
     * @param key
     * @return
     */
    public T selectByKey(Serializable key);
    
    /**
     * 根据实体类不为null的字段查询总数,条件全部使用=号and条件
     * @param entity
     * @return
     */
    public int selectCount(T entity);
    
    /**
     * 根据实体类不为null的字段进行查询,条件全部使用=号and条件
     * @param entity
     * @return
     */
    public List<T> select(T entity);

    /**
     * 插入一条数据
     * 支持Oracle序列,UUID,类似Mysql的INDENTITY自动增长(自动回写)
     * 优先使用传入的参数值,参数值空时,才会使用序列、UUID,自动增长
     * @param entity
     * @return
     */
    public int save(T entity);
    
    /**
     * 插入一条数据,只插入不为null的字段,不会影响有默认值的字段
     * 支持Oracle序列,UUID,类似Mysql的INDENTITY自动增长(自动回写)
     * 优先使用传入的参数值,参数值空时,才会使用序列、UUID,自动增长
     * @param entity
     * @return
     */
    public int saveNotNull(T entity);
    
    /**
     * 根据实体类中字段不为null的条件进行删除,条件全部使用=号and条件
     * @param entity
     * @return
     */
    public int delete(T entity);

    /**
     * 通过主键进行删除,这里最多只会删除一条数据
     * 单个字段做主键时,可以直接写主键的值
     * 联合主键时,key可以是实体类,也可以是Map
     * @param key
     * @return
     */
    public int delete(Serializable key);
    
    /**
     * 通过主键进行删除,这里最多只会删除一条数据
     * 单个字段做主键时,可以直接写主键的值
     * 联合主键时,key可以是实体类,也可以是Map
     * @param key
     * @return
     */
    public int delete(Class<?> clazz,String[] ids);

    /**
     * 根据主键进行更新,这里最多只会更新一条数据
     * @param entity 参数为实体类
     * @return
     */
    public int updateAll(T entity);

    /**
     * 根据主键进行更新
     * 只会更新不是null的数据
     * @param entity
     * @return
     */
    public int updateNotNull(T entity);

    public List<T> selectByExample(Object example);

    /**
     * 单表分页查询
     * 
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<T> selectPage(int pageNum,int pageSize);
    
}
