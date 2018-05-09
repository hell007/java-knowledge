package com.self.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.self.model.User;
import com.self.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	/*@Autowired
	private XmemcachedClient xmemcachedClient;*/
	
	
	@RequestMapping("/show")
	@ResponseBody
    public Object showInfo(){
        return "hello world";
    }
	
	
	/*@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
    public Map<String,Object> list(){  
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<User> list = userService.select(null); 
    	map.put("list",list);
    	
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
    }*/
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
    public Map<String,Object> test(){  
		Map<String,Object> map = new HashMap<String,Object>();
		User user = userService.getUser("admin", "123456");
		map.put("user", user);
		return map;
	}
	
	@RequestMapping(value = "/test1", method = RequestMethod.GET)
	@ResponseBody
    public Map<String,Object> test1(){  
		Map<String,Object> map = new HashMap<String,Object>();
		List<User> list = userService.getAll();
		map.put("list", list);
		return map;
	}
	
	@RequestMapping(value = "/test2", method = RequestMethod.GET)
	@ResponseBody
    public Map<String,Object> test2(){  
		Map<String,Object> map = new HashMap<String,Object>();
		User user = userService.selectByKey("1");
		user.setGender("女");
		user.setPhone("13777777777");
		userService.updateUser(user);;
		map.put("ok", true);
		return map;
	}
	
	
	
}
