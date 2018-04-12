package com.self.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.self.pojo.Employee;
import com.self.service.EmployeeService;

/**
 * @title 控制器
 * @author jie
 * @date 1.0 2018-01-16
 */

@Controller
@RequestMapping(value = "/emp")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	// 等价于 @RequestMapping(value="/index",method=RequestMethod.GET)
	@GetMapping("/index")
	public String index(Model modle,HttpServletRequest request){
		List<Employee> list = employeeService.findAll();
		modle.addAttribute("list", list);
		return "index";
	}
}
