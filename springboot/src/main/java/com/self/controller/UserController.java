
package com.self.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.self.model.User;
import com.self.service.UserService;



@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	private User user;
    
	
	@GetMapping("/index")
	public String index(Model modle,User user,
    		@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "pageSize", defaultValue = "10") int pageSize){
		List<User> list = userService.selectPage(user,pageNum, pageSize);
		//modle.addAttribute("page", new PageInfo<User>(list));
		modle.addAttribute("list", list);
		return "user/index";
	}
	
    //http://localhost:8080/user/list
    @ResponseBody
    @RequestMapping("/list")
    public Map<String,Object> list(
    		@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "pageSize", defaultValue = "10") int pageSize){  
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<User> list = userService.selectPage(user,pageNum, pageSize); 
    	map.put("list",list);
    	return map;   
    }
    
    
}
