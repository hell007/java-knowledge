package com.self.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.self.model.User;
import com.self.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	//注解为alibaba的 @Reference ,
	//如果想加入版本号，则加上version即可，值要与发布接口的版本号对应
	@Reference
	UserService userService;
	

    @GetMapping("/show")
    public Object showInfo(){
        return "hello world";
    }
    
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String,Object> list(){  
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<User> list = userService.select(null); 
    	map.put("list",list);
    	return map;   
    }

}
