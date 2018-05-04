package com.self.service.impl;

import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.self.service.XmemcachedClient;

public class XmemcachedClientImpl implements XmemcachedClient {
	
	private static Logger logger = LoggerFactory.getLogger(XmemcachedClientImpl.class);
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	private static final int DEFAULT_EXPIRE_TIME = 7200;	// 2H
	
	/**
	 * 存储 (默认7200秒)
	 * @param key		:存储的key名称
	 * @param value		:实际存储的数据
	 */
	public void set(String key, Object value) {
		try {
			memcachedClient.set(key, DEFAULT_EXPIRE_TIME, value);
		} catch (TimeoutException e) {
			logger.error("set", e);
		} catch (InterruptedException e) {
			logger.error("set", e);
		} catch (MemcachedException e) {
			logger.error("set", e);
		}
	}
	
	/**
	 * 存储
	 * @param key		:存储的key名称
	 * @param value		:实际存储的数据
	 * @param expTime	:expire时间 (单位秒,超过这个时间,memcached将这个数据替换出,0表示永久存储(默认是一个月))
	 */
	public void set(String key, Object value, int expTime) {
		try {
			memcachedClient.set(key, expTime, value);
		} catch (TimeoutException e) {
			logger.error("set", e);
		} catch (InterruptedException e) {
			logger.error("set", e);
		} catch (MemcachedException e) {
			logger.error("set", e);
		}
	}

	/**
	 * Cas	原子性Set
	 * @param key
	 * @param value
	 * @param expTime
	 * @param cas
	 */
	public boolean cas(String key, Object value, int expTime, long cas) {
		try {
			return memcachedClient.cas(key, expTime, value, cas);
		} catch (TimeoutException e) {
			logger.error("cas", e);
		} catch (InterruptedException e) {
			logger.error("cas", e);
		} catch (MemcachedException e) {
			logger.error("cas", e);
		}
		return false;
	}
	
	/**
	 * 查询
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		try {
			Object value = memcachedClient.get(key);
			return value;
		} catch (TimeoutException e) {
			logger.error("get", e);
		} catch (InterruptedException e) {
			logger.error("get", e);
		} catch (MemcachedException e) {
			logger.error("get", e);
		}	
		return null;
	}

	/**
	 * 查询 和Cas匹配使用,可查询缓存Cas版本
	 * @param key
	 * @return
     */
	public GetsResponse<Object> gets(String key){
		try {
			GetsResponse<Object> response = memcachedClient.gets(key);
			return response;
		} catch (TimeoutException e) {
			logger.error("gets", e);
		} catch (InterruptedException e) {
			logger.error("gets", e);
		} catch (MemcachedException e) {
			logger.error("gets", e);
		}
		return null;
	}

	/**
	 * 删除
	 * @param key
	 */
	public boolean delete(String key) {
		try {
			return memcachedClient.delete(key);
		} catch (TimeoutException e) {
			logger.error("delete", e);
		} catch (InterruptedException e) {
			logger.error("delete", e);
		} catch (MemcachedException e) {
			logger.error("delete", e);
		}
		return false;
	}

	/**
	 * 递增
	 * @param key
	 */
	public void incr(String key) {
		try {			
			memcachedClient.incr(key, 1);
		} catch (TimeoutException e) {
			logger.error("incr", e);
		} catch (InterruptedException e) {
			logger.error("incr", e);
		} catch (MemcachedException e) {
			logger.error("incr", e);
		}
	}
	
	
}
