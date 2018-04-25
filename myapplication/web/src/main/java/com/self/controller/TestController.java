package com.self.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.self.model.User;
import com.self.service.UserService;


@Api(value="/v1/api", tags="接口测试模块")
@RestController
@RequestMapping("/v1/api")
public class TestController {
	
	@Autowired
	private UserService userService;
	private User user;
	
	
    @ApiOperation(value="展示首页信息", notes = "展示首页信息")
    @GetMapping("/show")
    public Object showInfo(){
        return "hello world";
    }
    
    
    @ApiOperation(value="用户列表", notes = "获取用户列表")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="pageNum", value="pageNum", required = true, dataType ="Integer", paramType = "path"),
    	@ApiImplicitParam(name="pageSize", value="pageSize", required = true, dataType ="Integer", paramType = "path")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String,Object> list(
    		@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "pageSize", defaultValue = "10") int pageSize){  
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<User> list = userService.selectPage(user,pageNum, pageSize); 
    	map.put("list",list);
    	return map;   
    }
    
    
    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public Map<String,Object> edit (@PathVariable(value = "id") String id){
    	Map<String,Object> map = new HashMap<String,Object>();
    	User user = userService.selectByKey(id);
    	map.put("user", user);
		return map;
	}
    

    @ApiOperation(value="创建用户", notes="根据User对象创建用户")
	@ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
	@PostMapping(value = "/save")
	public Map<String,Object> save (@RequestBody User user){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("ok", true);
		return map;
	}
    
    
    @ApiOperation(value="删除用户", notes="根据url的id来指定删除用户")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", paramType = "path")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public Map<String,Object> delete (@PathVariable(value = "id") String id){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("ok", true);
		return map;
	}
    
    
    
}
