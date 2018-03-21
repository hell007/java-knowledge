package com.jie.utils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: HttpClientUtils
 * @Description: HttpClient请求类. <br/>
 * @author jie
 * @date 2017年11月28日
 */
public class HttpClientUtils {
	
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	/**
	 * 无参数get请求
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {							
		return doGet(url, null);
	}
	
	/**
	 * 带参数get请求
	 * @param url
	 * @param param
	 * @return
	 */
	public static String doGet(String url, Map<String, String> param) {	
		// 创建一个默认可关闭的Httpclient 对象
		CloseableHttpClient httpClient = HttpClients.createDefault();	
		// 设置返回值
		String resultMsg = "";	
		// 定义HttpResponse 对象
		CloseableHttpResponse response = null;							
		try {
			// 创建URI,可以设置host，设置参数等
			URIBuilder builder = new URIBuilder(url);					
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();
			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);	
			// 执行请求
			response = httpClient.execute(httpGet);		
			// 判断返回状态为200则给返回值赋值
			if (response.getStatusLine().getStatusCode() == 200) {		
				resultMsg = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			logger.error("doGet e",e);
			e.printStackTrace();
		} finally {	
			// 不要忘记关闭
			try {
				if (response != null) {
					response.close();
				}
				httpClient.close();
			} catch (IOException ex) {
				logger.error("doGet ex",ex);
				ex.printStackTrace();
			}
		}
		return resultMsg;
	}
	
	/**
	 * 无参数post请求
	 * @param url
	 * @return
	 */
	public static String doPost(String url) {							
		return doPost(url, null);
	}
	
	/**
	 * 带参数post请求
	 * @param url
	 * @param param
	 * @return
	 */
	public static String doPost(String url, Map<String, String> param) {
		// 创建一个默认可关闭的Httpclient 对象
		CloseableHttpClient httpClient = HttpClients.createDefault();	
		CloseableHttpResponse response = null;
		String resultMsg = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);	
			// 创建参数列表
			if (param != null) {										
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);					
			if (response.getStatusLine().getStatusCode() == 200) {
				resultMsg = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			logger.error("doPost e",e);
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpClient.close();
			} catch (IOException ex) {
				logger.error("doPost ex",ex);
				ex.printStackTrace();
			}
		}
		return resultMsg;
	}
	
	/**
	 * post json
	 * @param url
	 * @param json
	 * @return
	 */
	public static String doPostJson(String url, String json) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			logger.error("doPostJson e",e);
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpClient.close();
			} catch (IOException ex) {
				logger.error("doPostJson ex",ex);
				ex.printStackTrace();
			}
		}
		return resultString;
	}
	
	
}
