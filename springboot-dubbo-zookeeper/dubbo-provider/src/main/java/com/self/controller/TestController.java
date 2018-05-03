package com.self.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.self.model.User;
import com.self.service.UserService;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private UserService userService;
	

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
