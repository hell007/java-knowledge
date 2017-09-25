package com.myimooc.springbootweb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;



/**
 * Http请求工具类
 * @author wzh
 * @date 2017-09-25
 * @version V1.0
 */
public class HttpUtils {
	
	static boolean proxySet = false;
    static String proxyHost = "127.0.0.1";
    static int proxyPort = 8087;
    
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
        BufferedReader in = null;
        try{
            //拼装请求参数
            StringBuffer targetUrlStr = new StringBuffer(url).append("?");
            for(Map.Entry<String, String> entry : param.entrySet()){
                targetUrlStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = targetUrlStr.substring(0,targetUrlStr.length()-1);
            System.out.println("url=="+url);
            targetUrl = new URL(url);
            //打开和URL之间的连接
            httpURLConnection = (HttpURLConnection) targetUrl.openConnection();
            
            //设置通用的请求属性
            httpURLConnection.setRequestMethod("GET");
            //httpURLConnection.setRequestProperty("accept","*/*");
            //httpURLConnection.setRequestProperty("connection","Keep-Alive");
            //httpURLConnection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //建立实际的连接
            httpURLConnection.connect();
            
            //定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));         
            StringBuffer sb = new StringBuffer();           
            String temp = null;
            while((temp=in.readLine())!=null){
                sb.append(temp);
            }
            
            JSONObject resultJson = JSONObject.parseObject(sb.toString());
            respMessage.setRespCode(resultJson.getString("respCode"));
            respMessage.setRespMsg(resultJson.getString("respMsg"));
            
            JSONObject resultJsonMap = JSONObject.parseObject(resultJson.getString("respArgs"));
            //respMessage.setRespArgs(resultJsonMap);
            
            respMessage.setRespArgs(JSONObject.parseObject(sb.toString()));
            
        }catch (Exception e) {
            respMessage.setRespCode("500");
            respMessage.setRespMsg("请求发起失败");
            return respMessage;
        }
        //使用finally块来关闭输入流
        finally {
            if(httpURLConnection !=null){
                httpURLConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return respMessage;
    }
    
    
    /**
     * 向指定url路径发起post请求
     * @param url 请求路径
     * @param param 请求参数
     * @param isproxy 是否使用代理模式
     * @return
     */
    @SuppressWarnings("deprecation")
	public static RespMessage doPost(String url,Map<String,String> param,boolean isproxy) {  
    	
        RespMessage respMessage = new RespMessage();
        HttpURLConnection httpURLConnection = null;
        URL targetUrl = null;
        BufferedReader in = null;
        PrintWriter out = null;
        
        StringBuffer buffer = new StringBuffer();  
        if (param != null && !param.isEmpty()) {  
            for (Map.Entry<String, String> entry : param.entrySet()) {  
                buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue())).append("&");  
  
            } 
            buffer.deleteCharAt(buffer.length()-1);
        }  
          
        try {  
            targetUrl = new URL(url); 
            
            //打开和URL之间的连接   
            if(isproxy){//使用代理模式
                @SuppressWarnings("static-access")
                Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                httpURLConnection = (HttpURLConnection) targetUrl.openConnection(proxy);
            }else{
            	httpURLConnection = (HttpURLConnection) targetUrl.openConnection();
            } 
              
            //发送POST请求必须设置如下两行    
            httpURLConnection.setDoOutput(true);  
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            
            //设置通用的请求属性    
            //httpURLConnection.setRequestProperty("accept","*/*");  
            //httpURLConnection.setRequestProperty("connection","Keep-Alive");  
            //httpURLConnection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            //获取URLConnection对象对应的输出流    
            out = new PrintWriter(httpURLConnection.getOutputStream());  
            //发送请求参数    
            out.print(buffer);  
            //flush输出流的缓冲    
            out.flush();  
            //定义BufferedReader输入流来读取URL的响应    
            in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));  
    
            StringBuffer sb = new StringBuffer();           
            String temp = null;
            while((temp=in.readLine())!=null){
                sb.append(temp);
            }
            
            JSONObject resultJson = JSONObject.parseObject(sb.toString());
            respMessage.setRespCode(resultJson.getString("respCode"));
            respMessage.setRespMsg(resultJson.getString("respMsg"));
            
            JSONObject resultJsonMap = JSONObject.parseObject(resultJson.getString("respArgs"));
            respMessage.setRespArgs(resultJsonMap);
            
        } catch (Exception e) {  
        	respMessage.setRespCode("500");
            respMessage.setRespMsg("请求发起失败");
            return respMessage;  
        }  
        // 使用finally块来关闭输出流、输入流    
        finally {  
        	if(httpURLConnection !=null){
                httpURLConnection.disconnect();
            }
            try {  
                if (out != null) {  
                    out.close();  
                }  
                if (in != null) {  
                    in.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return respMessage;  
    }
    
    
    
    public static void main(String[] args) {
        //demo:代理访问
        String url = "https://api.douban.com/v2/book/1220562";
        Map<String, String> params = new HashMap<String, String>();
        //params.put("pageNum", "1");
        //params.put("pageSize", "2");
        //RespMessage rm = HttpUtils.doPost(url,params,false);
        RespMessage rm = HttpUtils.doGet(url,params);
        System.out.println(rm.toString());
    }
    
}
