package com.self.service;

import net.rubyeye.xmemcached.GetsResponse;

public interface XmemcachedClient {

	
	/**
	 * 存储 (默认7200秒)
	 * @param key		:存储的key名称
	 * @param value		:实际存储的数据
	 */
	void set(String key, Object value);
	
	/**
	 * 存储
	 * @param key		:存储的key名称
	 * @param value		:实际存储的数据
	 * @param expTime	:expire时间 (单位秒,超过这个时间,memcached将这个数据替换出,0表示永久存储(默认是一个月))
	 */
	void set(String key, Object value, int expTime);

	/**
	 * Cas	原子性Set
	 * @param key
	 * @param value
	 * @param expTime
	 * @param cas
	 */
	boolean cas(String key, Object value, int expTime, long cas);
	
	/**
	 * 查询
	 * @param key
	 * @return
	 */
	Object get(String key);

	/**
	 * 查询		和Cas匹配使用,可查询缓存Cas版本
	 * @param key
	 * @return
     */
	 GetsResponse<Object> gets(String key);
	

	/**
	 * 删除
	 * @param key
	 */
	boolean delete(String key);

	/**
	 * 递增
	 * @param key
	 */
	void incr(String key);

}
