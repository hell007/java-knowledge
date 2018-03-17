package com.jie.utils.security;

import org.springframework.util.DigestUtils;

/**
 * @ClassName: MD5Utils
 * @Description: MD5 加密类. <br/>
 * @author jie
 * @date 2018年2月28日
 */
public class MD5Utils {
	
	/**
	 * MD5摘要
	 * @param str 原始字符串
	 * @return 加密字符串
	 */
	public static String encrypt(String str){
		return DigestUtils.md5DigestAsHex(str.getBytes());
	}
	
	
	
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args){
		System.out.println("md5加密："+MD5Utils.encrypt("admin"));
	}
}
