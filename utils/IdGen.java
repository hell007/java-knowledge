package com.self.utils;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @ClassName: IdGen
 * @Description: 封装各种生成唯一性ID算法的工具类. <br/>
 * @author jie
 * @date 2016年5月11日
 */
@Service
@Lazy(false)
public class IdGen {

	private static SecureRandom random = new SecureRandom();
	
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 封装JDK自带的UUID, 通过Random生成16位, 中间无-分割.
	 */
	public static String uuid16() {
		String result = UUID.randomUUID().toString().replaceAll("-", "");
		return result.substring(0,4)
				.concat(UUID.randomUUID().toString().replaceAll("-", "").substring(8,12))
				.concat(UUID.randomUUID().toString().replaceAll("-", "").substring(12,16))
				.concat(UUID.randomUUID().toString().replaceAll("-", "").substring(18,22));
	}
	
	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}
	
	
	/**
	 * main
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		System.out.println("uuid："+uuid());	
	}

}
