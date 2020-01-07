package com.self.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/vr")
public class VrController {
	
	@ResponseBody
    @RequestMapping("/test")
    public Map<String,Object> list(
    		@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "pageSize", defaultValue = "10") int pageSize){  
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("pageNum", pageNum);
    	map.put("pageSize",pageSize);
    	return map;   
    }
	
	@GetMapping("/index")
	public String index(Model modle){
		//modle.addAttribute("list", list);
		return "vr/index";
	}
}
