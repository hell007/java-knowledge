package com.self.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * @ClassName: DateUtils
 * @Description: 日期工具类, 继承org.apache.commons.lang.time.DateUtils类. <br/>
 * @author jie
 * @date 2017年11月28日
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = { 
		"yyyy-MM-dd", 
		"yyyy-MM-dd HH:mm:ss", 
		"yyyy-MM-dd HH:mm", 
		"yyyy/MM/dd", 
		"yyyy/MM/dd HH:mm:ss", 
		"yyyy/MM/dd HH:mm" 
		};
	
	//获取日期时间函数
	
	/**
	 * 得到当前日期和时间字符串 
	 * @return String 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}
	
	
	
	/**
	 * 得到当前日期字符串 
	 * @return String 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	
	
	/**
	 * 得到当前日期字符串  
	 * @param pattern 可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 * @return String 格式（yyyy-MM-dd）
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	
	
	/**
	 * 得到当前时间字符串 
	 * @return String 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}
	
	
	
	/**
	 * 得到当前年份字符串 
	 * @return String 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	

	/**
	 * 得到当前月份字符串 
	 * @return String 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	

	/**
	 * 得到当天字符串 
	 * @return String 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	
	
	/**
	 *得到当前星期字符串 
	 * @return String 格式（E）星期几 
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	
	//日期时间格式化
	
	/**
	 * 得到日期字符串 
	 * @param date
	 * @param pattern 可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 * @return String 默认格式（yyyy-MM-dd） 
	 */
	public static String formatDate(Date date, String pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length() > 0) {
			formatDate = DateFormatUtils.format(date, pattern);
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	

	/**
	 * 得到日期字符串 
	 * @param date
	 * @param pattern 可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 * @return String 默认格式（yyyy-MM-dd）
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	

	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 * @param date
	 * @return String 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	
	
	/**
	 * 将字符串日期转换为指定格式的日期对象
	 * @param strDate
	 * @param pattern
	 * @return Date
	 */
	public static Date parseDate(String strDate, String pattern) {
		if(StringUtils.isBlank(strDate)){
			return new Date();
		}
		if (pattern == null || pattern.length() == 0) {
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}
	
	
	
	/**
	 * 日期型字符串转化为日期 格式
	 * @param str 
	 * { "yyyy-MM-dd", 
	 * 	"yyyy-MM-dd HH:mm:ss", 
	 * 	"yyyy-MM-dd HH:mm", 
	 *  "yyyy/MM/dd", 
	 *  "yyyy/MM/dd HH:mm:ss", 
	 *  "yyyy/MM/dd HH:mm" }
	 * @return Date
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}
	

	
	/**
	 * 获取开始时间
	 * @param date
	 * @return Date
	 */
	public static Date getDateStart(Date date) {
		if(date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date= sdf.parse(formatDate(date, "yyyy-MM-dd")+" 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
	
	/**
	 * 获取结束时间
	 * @param date
	 * @return Date
	 */
	public static Date getDateEnd(Date date) {
		if(date==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date= sdf.parse(formatDate(date, "yyyy-MM-dd") +" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
	
	/**
	 * 获取过去的天数
	 * @param date
	 * @return long
	 */
	public static long getPastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}
	
    
	
	
	/**
	 * main
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		System.out.println("===================");
		System.out.println("获取开始时间时间戳："+getDateStart(parseDate("2017/11/28")).getTime());
		System.out.println("获取结束时间时间戳："+getDateEnd(parseDate("2017/12/28")).getTime());
		System.out.println("获取过去的天数："+getPastDays(parseDate("2017/11/28")));
		System.out.println("时间日期格式化："+formatDate(parseDate("2017/11/28")));
		System.out.println("获取当前格式化日期时间星期："+getDate("yyyy年MM月dd日 E"));
		long time = new Date().getTime()-parseDate("2017-11-28").getTime();
		System.out.println("时间差天数："+time/(24*60*60*1000));
	}
	
	
}
