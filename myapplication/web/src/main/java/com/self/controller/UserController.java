package com.self.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.GetsResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.self.model.User;
import com.self.service.UserService;
import com.self.service.XmemcachedClient;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	private User user;
	
	@Autowired
	private XmemcachedClient xmemcachedClient;
	
	
	@RequestMapping("/show")
	@ResponseBody
    public Object showInfo(){
        return "hello world";
    }
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
    public Map<String,Object> list(){  
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<User> list = userService.select(null); 
    	map.put("list",list);
    	
/*    	List<String> list2 = new ArrayList<String>();
    	list2.add("jack");
    	list2.add("rose");*/
    	
    	String key = "key02";
    	xmemcachedClient.set(key, JSON.toJSONString(list));
		System.out.println("get(key)======"+xmemcachedClient.get(key));

		GetsResponse<Object> val = xmemcachedClient.gets(key);
		System.out.println("gets(key)======"+val);

		System.out.println("cas======"+xmemcachedClient.cas(key, "val2222", 7200, 99L));
		System.out.println("get======"+xmemcachedClient.get(key));

		System.out.println("cas======"+xmemcachedClient.cas(key, "val2222", 7200, val.getCas()));
		System.out.println("get======"+xmemcachedClient.get(key));
		
    	return map;   
    }
	
}
