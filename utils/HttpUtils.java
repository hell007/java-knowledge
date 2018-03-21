package com.jie.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jie.model.extend.RespMessage;

/**
 * @ClassName: HttpUtils
 * @Description: htpp请求类. <br/>
 * @author jie
 * @date 2017年11月28日
 */

public class HttpUtils {
	
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	/**
     * 向指定url路径发起get请求
     * @param url 请求路径
     * @param param 请求参数
     * @return
     */
    public static RespMessage doGet(String url,Map<String,String> param){
    	RespMessage respMessage = new RespMessage();
        HttpURLConnection httpURLConnection = null;
        URL targetUrl = null;
        try{
            // 拼装请求参数
            StringBuffer targetUrlStr = new StringBuffer(url).append("?");
            for(Map.Entry<String, String> entry : param.entrySet()){
                targetUrlStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = targetUrlStr.substring(0,targetUrlStr.length()-1);
            
            targetUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) targetUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            
            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            
            StringBuffer sb = new StringBuffer();
            String temp = null;
            while((temp=br.readLine())!=null){
                sb.append(temp);
            }
            
            br.close();
            isr.close();
            in.close();
            
            JSONObject resultJson = JSONObject.parseObject(sb.toString());
            respMessage.setRespCode(resultJson.getString("code"));
            respMessage.setStatus(resultJson.getBoolean("status"));
            respMessage.setRespMsg(resultJson.getString("msg"));
            
            JSONObject resultJsonMap = JSONObject.parseObject(resultJson.getString("args"));
            respMessage.setRespArgs(resultJsonMap);
            return respMessage;
        }catch (Exception e) {
            respMessage.setRespCode("500");
            respMessage.setRespMsg("请求发起失败");
            return respMessage;
        }finally {
            if(httpURLConnection !=null){
                httpURLConnection.disconnect();
            }
        }
    }
    

	/**
     * 向指定URL发送GET方法的请求 
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	logger.error("sendGet e" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
            	logger.error("sendPost ex" + ex);
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        	logger.error("sendPost e" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
            	logger.error("sendPost ex" + ex);
                ex.printStackTrace();
            }
        }
        return result;
    }    
}
