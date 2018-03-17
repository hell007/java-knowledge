package com.jie.utils.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: AESUtils
 * @Description: AES 加密解密类. <br/>
 * @author jie
 * @date 2018年2月28日
 */

public class AESUtils {
	
	//日志对象
	protected static  Logger logger = LoggerFactory.getLogger(AESUtils.class);
	
	private static final String KEY_ALGORITHM = "AES";
	//默认的加密算法
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

  
    
    /**
     * AES 加密操作
     * @param content 待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return Base64.encodeBase64String(result);//通过Base64转码返回
        } catch (Exception e) {
        	logger.error("AESUtils encrypt", e);    	
        }
        return null;
    }
    

    /**
     * AES 解密操作
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));
            return new String(result, "utf-8");
        } catch (Exception e) {
        	logger.error("AESUtils decrypt", e);
        }
        return null;
    }

    
    /**
     * 生成加密秘钥
     * @param password
     * @return 
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
        	SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            //AES 要求密钥长度为 128
            kg.init(128, random);
            //kg.init(128, new SecureRandom(password.getBytes()));window is ok
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException e) {
        	logger.error("AESUtils getSecretKey", e);
        }
        return null;
    }

    
    /**
     * main
     * @param args
     */
    public static void main(String[] args){
    	
    	String salt = MD5Utils.encrypt(new Date().toString());  	
    	String temp = salt;
    	String pwd = AESUtils.encrypt("test123", salt);
    	
    	System.out.println("salt:" + temp);
    	System.out.println("pwd加密:" + pwd);
    	System.out.println("pwd解密:" + AESUtils.decrypt(pwd, temp));
        
    }
    
    
}
