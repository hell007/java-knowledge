package com.self.utils;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 * @ClassName: SpringContextHolder
 * @Description: 静态保存应用上下文. <br/>
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 * @author Dzy yndxc@163.com
 * @date 2015年8月10日
 */
@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware,MessageSourceAware, DisposableBean {

	private static ApplicationContext applicationContext = null;

	private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

	private static MessageSource ms = null;
	
	private static Properties p = null;
	
	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return applicationContext;
	}
	
	public static String getRootRealPath(){
		String rootRealPath ="";
		try {
			rootRealPath=getApplicationContext().getResource("").getFile().getAbsolutePath();
		} catch (IOException e) {
			logger.warn("获取系统根目录失败");
		}
		return rootRealPath;
	}
	
	public static String getResourceRootRealPath(){
		String rootRealPath ="";
		try {
			rootRealPath=new DefaultResourceLoader().getResource("").getFile().getAbsolutePath();
		} catch (IOException e) {
			logger.warn("获取资源根目录失败");
		}
		return rootRealPath;
	}
	

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clearHolder() {
		if (logger.isDebugEnabled()){
			logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		}
		applicationContext = null;
	}

	/**
	 * 实现ApplicationContextAware接口, 注入Context到静态变量中.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		logger.debug("注入ApplicationContext到SpringContextHolder:{}", applicationContext);
		if (SpringContextHolder.applicationContext != null) {
			logger.info("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + SpringContextHolder.applicationContext);
		}
		SpringContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 实现DisposableBean接口, 在Context关闭时清理静态变量.
	 */
	@Override
	public void destroy() throws Exception {
		SpringContextHolder.clearHolder();
	}

	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContextInjected() {
		Validate.validState(applicationContext != null, "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
	}
	
	/**
	 * 获取ServletContext对象
	 * @return ServletContext对象
	 */
	public static ServletContext getServletContext() {
		return ((WebApplicationContext)applicationContext).getServletContext();
	}
	
	/**
	 * 获取web.xml中定义的参数
	 * @param paramName 参数名称
	 * @return 返回配置的参数值
	 */
	public static String getInitParam(String paramName) {
		return getServletContext().getInitParameter(paramName);
	}
	
	/**
	 * 获取默认区域spring国际化资源
	 * @param code message的键
	 * @return　返回对应的消息
	 */
	public static String getMessage(String code) {
		return getMessage(Locale.getDefault(), code, new String[]{});
	}
	
	/**
	 * 获取默认区域的spring国际化资源
	 * @param code message的键
	 * @param args message中的占位参数
	 * @return 消息
	 */
	public static String getMessage(String code, String... args) {
		return getMessage(Locale.getDefault(), code, args);
	}
	
	/**
	 * 获取spring国际化资源
	 * @param locale 区域对象
	 * @param code message的键
	 * @param args message中的占位参数
	 * @return　消息
	 */
	public static String getMessage(Locale locale, String code, String... args) {
		return ms.getMessage(code, args, locale);
	}
	
	/**
	 * 获取spring国际化资源
	 * @param locale 区域对象
	 * @param code message的键
	 * @param defaultMessage 未找到消息时返回的默认消息
	 * @param args message中的占位参数
	 * @return　如果找到code对应的消息则返回该消息,否则返回defaultMessage
	 */
	public static String getMessage(Locale locale, String code, String defaultMessage, String... args) {
		return ms.getMessage(code, args, defaultMessage, locale);
	}
	
	/**
	 * 获取默认区域spring国际化资源
	 * @param code message的键
	 * @param defaultMessage 未找到消息时返回的默认消息
	 * @param args message中的占位参数
	 * @return　如果找到code对应的消息则返回该消息,否则返回defaultMessage
	 */
	public static String getMessage(String code, String defaultMessage, String... args) {
		return getMessage(Locale.getDefault(), code, defaultMessage, args);
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		ms = messageSource;
	}
	
	/**
	 * 获取SpringBeetl配置的共享变量
	 * @return Map
	 */
	public static Map<String,Object> getBeetlShareVar(){
		org.beetl.ext.spring.BeetlGroupUtilConfiguration cfg= getBean("beetlConfig");
		return cfg.getGroupTemplate().getSharedVars();
	}
	
	/**
	 * 获取默认配置,与Spring-config.xml中对应
	 * @param key 配置中的主键
	 * @return　返回对应的信息
	 */
	public static String getConfig(String key) {
		if(p == null){
			p = getBean("cknet");
		}
		return p.get(key).toString();
	}
}
