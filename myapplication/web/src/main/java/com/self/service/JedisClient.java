package com.self.service;

/**
 * @ClassName: JedisClient.java
 * @Description: jedis客户端封装接口. <br/>
 * @author Dzy yndxc@163.com
 * @date 2016年5月3日 下午11:03:55
 */
public interface JedisClient {

	/**
	 * 根据key获取缓存值
	 * @param key
	 * @return
	 */
	String get(String key);
	/**
	 * 根据key设置缓存值
	 * @param key
	 * @param value
	 * @return
	 */
	String set(String key,String value);
	
	/**
	 * 获取指定类目hkey下的key的值
	 * @param hkey
	 * @param key
	 * @return
	 */
	String hget(String hkey, String key);
	
	/**
	 * 指定类目hkey下的key设置值
	 * @param hkey
	 * @param key
	 * @param value
	 * @return
	 */
	long hset(String hkey,String key,String value);
	
	/**
	 * 指定key的值递增
	 * @param key
	 * @return
	 */
	long incr(String key);
	
	/**
	 * 设置有效时间
	 * @param key
	 * @param second
	 * @return
	 */
	long expire(String key,int second);
	
	/**
	 * 获取失效时间
	 * @param key
	 * @return
	 */
	long ttl(String key);
	
	/**
	 * 从缓存中删除
	 * @param key
	 * @return
	 */
	long del(String key);
	
	
	/**
	 * 从指定类目下删除
	 * @param hkey
	 * @param key
	 * @return
	 */
	long hdel(String hkey,String key);
}
