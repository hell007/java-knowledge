package com.self.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.self.model.Article;
import com.self.model.User;
import com.self.service.ArticleService;
import com.self.service.UserService;
import com.self.utils.AppPushUtils;
import com.self.utils.Base64Image;
import com.self.utils.FilesUtil;
import com.self.utils.GolbalUtil;
import com.self.utils.PropertiesUtil;
import com.self.utils.StringUtils;
import com.self.utils.WebUtil;

@Controller
@Scope(value="prototype") 
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	
	//private Article article;
	
	/**
	 * 瀑布流案列
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getui")
	public String getui(Model model) {
		String appId = "8xO68VhE097nrb7iLtOEL2";  
        String appKey = "Enn8AUi93c75DHdOrJk1V4";  
        String masterSecret = "LfvK0oXVbrA2h15UOumGb3";  
          
        Map<String,String> msg = new HashMap<String, String>();  
        msg.put("title", "个推测试");  
        msg.put("titleText", "个推中");  
        //msg.put("transText", "{title:'通知标题',content:'通知内容',payload:'通知去干嘛这里可以自定义'}");  
        msg.put("transText", "{payload:'通知去干嘛这里可以自定义'}"); 
          //c23b79ca51cdd30b9383a5ba37f8894e "bbf41a62e592df0cc2b468338abc0018","b69ebb023fab52414b15bf9991814d92","a5e7a3e5c741d28a20b1aa08ac782b38"
        String[] cids = {"590e8e1327276ce39f74e5fe898535b0"};  
          
        AppPushUtils pushUtils = new AppPushUtils(appId, appKey, masterSecret); 
        
        //sigle
//        for(String cid : cids) {  
//            System.out.println("正在发送消息...");  
//            IPushResult ret =  pushUtils.pushMsgToSingle(cid, msg);  
//            System.out.println(ret.getResponse().toString());  
//        } 
         
        //list
        @SuppressWarnings("unused")
		IPushResult ret =  pushUtils.pushMsgToList(cids, msg);
        
        return "test/getui";
	}
	

	/**
	 * 瀑布流案列返回数据回调
	 * @param model
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getList")
	@ResponseBody
	public Map<String,Object> getList(Model model,String title,Article article,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
		Map<String,Object> map = new HashMap<String, Object>();
		article.setTitle(title);
		//查到分页列表
		List<Article> list= articleService.selectPage(article,pageNum, pageSize);
		if(list.size()>0){
			for(Article a : list){
				article.setContent(HtmlUtils.htmlUnescape(a.getContent()));
			}
			SimplePropertyPreFilter mfilter = new SimplePropertyPreFilter(Article.class,"id","title","pic","content");
			JSONArray bjson = JSONArray.parseArray(JSONObject.toJSONString(list,mfilter));
			
			map.put("title",title);
			map.put("list",bjson);
			if(pageNum==8){
				map.put("isSuccess",false);
			}else{
				map.put("isSuccess",true);
			}
		}else{
			map.put("isSuccess",false);
		}
		return map;
	}
	
	/**
	 * 获取文章信息
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getItem")
	@ResponseBody
	public Map<String,Object> getItem(String id) {
		Article article = articleService.selectByKey(id);
		SimplePropertyPreFilter mfilter = new SimplePropertyPreFilter(Article.class,"id","title","pic","content","created","keywords","views");
		String bjson = JSONObject.toJSONString(article,mfilter);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("item",bjson);
		return map;
	}
	
	
	/**
	 * 图片base64上传
	 * @param name
	 * @param passwd
	 * @param imgdata
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bas64ImageUpload")
	@ResponseBody
	public Map<String,Object> bas64ImageUpload(String name, String passwd, String imgdata) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();

		if(StringUtils.isBlank(name) || StringUtils.isBlank(passwd) ){
			map.put("success", false);
			map.put("message", "权限不够！");
			return map;
		}
		//查询用户
		User user = userService.selectUserByName(name);
		
		//用户匹配上传、保存
		if(user.getPasswd().equals(passwd)){	
			
			System.out.println(imgdata);
			if(imgdata!=null){
				String saveFilePath = PropertiesUtil.getFileIO("savePicUrl", "properties/config.properties");
				String filePath = GolbalUtil.USER_IMG_PATH;	
				//保存图片
				Base64Image.generateImage(imgdata, saveFilePath+filePath+"test.png");

				map.put("success", true);
				map.put("message", "上传头像成功！");
				map.put("path", saveFilePath+filePath+"test.png");
			}else{
				map.put("success", false);
				map.put("message", "上传头像失败！");
			}
		}
		return map;
	}
	
	
	/**
	 * 图片上传案列
	 * @param name
	 * @param passwd
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "imageUpload")
	@ResponseBody
	public Map<String,Object> imageUpload(String name, String passwd, MultipartFile file) throws Exception {
		//System.out.println(WebUtil.md5Encryption("admin")); 21232f297a57a5a743894a0e4a801fc3
		Map<String,Object> map = new HashMap<String, Object>();

		if(StringUtils.isBlank(name) || StringUtils.isBlank(passwd) ){
			map.put("success", false);
			map.put("message", "权限不够！");
			return map;
		}
		//查询用户
		User user = userService.selectUserByName(name);
		
		//用户匹配上传、保存
		if(user.getPasswd().equals(passwd)){
			//上传图片 注意：相册或者手机拍照没有类型后缀名
			if(file!=null && !file.isEmpty()){
				//判断是否为图片类型 由于上传图片时候就限制类型，可以不判断
//				if(!FilesUtil.isAllowFile(file.getOriginalFilename(),"image")){
//					map.put("success", false);
//					map.put("message", "上传的文件不是图片类型！");
//					return map;
//				}
				//保存文件的子目录
				String filePath = GolbalUtil.USER_IMG_PATH;			
				//返回文件名称
				Map<String,String> fm = new HashMap<String,String>();
				fm = FilesUtil.saveFile(filePath,file,false);		
				if(fm!=null && fm.size()>0){
					//跟新图像
					user.setHeader(fm.get("thumbB"));
					userService.updateNotNull(user);
					
					map.put("success", true);
					map.put("message", "上传头像成功！");
					map.put("path", fm.get("thumbB"));
				}else{
					map.put("success", false);
					map.put("message", "上传头像失败！");
				}	
			}
		}
		return map;
	}
	
	
	/**
	 * 图片批量上传
	 * @param name
	 * @param passwd
	 * @param file[]
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "batchImageUpload")
	@ResponseBody
	public Map<String,Object> batchImageUpload(String name, String passwd, MultipartFile[] file) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		if(StringUtils.isBlank(name) || StringUtils.isBlank(passwd) ){
			map.put("success", false);
			map.put("message", "权限不够！");
			return map;
		}
		//查询用户
		User user = userService.selectUserByName(name);
		//用户匹配上传、保存
		if(user.getPasswd().equals(passwd)){
			List<String> listImagePath = new ArrayList<String>();  
			for (MultipartFile mf : file) {  
				//上传图片 注意：相册或者手机拍照没有类型后缀名
				if(!mf.isEmpty()){
					//保存文件的子目录
					String filePath = GolbalUtil.USER_IMG_PATH;			
					//返回文件名称
					Map<String,String> fm = new HashMap<String,String>();
					fm = FilesUtil.saveFile(filePath,mf,false);		
					if(fm!=null && fm.size()>0){	
						listImagePath.add(fm.get("thumbB"));
					}	
				}
			}
			map.put("success", true);
			map.put("message", "上传头像成功！");
			map.put("pathList", listImagePath);
		}
		return map;
	}
	
	/**
	 * 用户登录
	 * @param name
	 * @param passwd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "login")
	@ResponseBody
	public Map<String,Object> login(String name, String passwd) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		if(StringUtils.isBlank(name) || StringUtils.isBlank(passwd) ){
			map.put("success", false);
			map.put("message", "用户名或者密码不能为空！");
			return map;
		}
		//查询用户
		User user = userService.selectUserByName(name);
		if(user.getPasswd().equals(WebUtil.md5Encryption(passwd))){
			user.setLastlogin(new Date());
			userService.updateNotNull(user);
			SimplePropertyPreFilter mfilter = new SimplePropertyPreFilter(User.class,"id","name","passwd","phone","gender","header");
			String bjson = JSONObject.toJSONString(user,mfilter);
			map.put("success", true);
			map.put("data", bjson);
		}else{
			map.put("success", false);
			map.put("message", "用户名或者密码错误！");
			return map;
		}
		return map;
	}
	
	
	/**
	 * 跨域请求
	 * @param callback
	 * @param id
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getJsonp",produces="application/json;charset=UTF-8")
	@ResponseBody
	public JSONPObject getJsonp(String callback, Integer id,String title,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "pageSize", defaultValue = "10") int pageSize) throws Exception {	
		
		Article a = new Article();
		a.setTitle(title);
		//查到分页列表
		List<Article> list= articleService.selectPage(a,pageNum, pageSize);
		for(Article article : list){
			article.setContent(HtmlUtils.htmlUnescape(article.getContent()));
		}
		SimplePropertyPreFilter mfilter = new SimplePropertyPreFilter(Article.class,"id","title","pic","content");
		JSONArray bjson = JSONArray.parseArray(JSONObject.toJSONString(list,mfilter));
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("list",bjson);
		if(pageNum==8){
			map.put("isSuccess",false);
		}else{
			map.put("isSuccess",true);
		}
		map.put("info","id=="+ id +";pageNum==" + pageNum +"你跨越请求成功了");
		return new JSONPObject(callback, map == null ? "" : map);
	}
	
	
	
	
	
	
	
	


}
