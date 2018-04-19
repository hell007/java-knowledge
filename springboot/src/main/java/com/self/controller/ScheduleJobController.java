package com.self.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.self.model.ScheduleJob;
import com.self.service.ScheduleJobService;

@Controller
@RequestMapping(value = "/job")
public class ScheduleJobController {
	
	@Autowired
	private ScheduleJobService scheduleJobService;
	
	
	
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> list(){  
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<ScheduleJob> list = scheduleJobService.select(null); 
    	map.put("list",list);
    	return map;   
    }
    
    @RequestMapping(value="run")
	@ResponseBody
	public Map<String,Object> run(){
		Map<String,Object> map = new HashMap<String,Object>();
		scheduleJobService.startJob();
    	map.put("ok",true);
    	return map;
	}
    
    @RequestMapping(value="standby")
	@ResponseBody
	public Map<String,Object> standby(){
		Map<String,Object> map = new HashMap<String,Object>();
		scheduleJobService.standbyJob();
    	map.put("ok",true);
    	return map;
	}
    
    @RequestMapping(value="stop")
	@ResponseBody
	public Map<String,Object> stop(){
		Map<String,Object> map = new HashMap<String,Object>();
		scheduleJobService.shutdownJob();
    	map.put("ok",true);
    	return map;
	}
    
    @RequestMapping(value="create/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> create(@PathVariable String id){
		Map<String,Object> map = new HashMap<String,Object>();
		scheduleJobService.createJob(id);
    	map.put("ok",true);
    	return map;
	}
    
    @RequestMapping(value="remove/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> remove(@PathVariable String id){
		Map<String,Object> map = new HashMap<String,Object>();
		scheduleJobService.deleteJob(id);
    	map.put("ok",true);
    	return map;
	}
	
	
	@RequestMapping(value="pause/{id}", method = RequestMethod.GET)
	@ResponseBody 
	public Map<String,Object> pause(@PathVariable String id){
		Map<String,Object> map = new HashMap<String,Object>();
		scheduleJobService.pauseJob(id);
    	map.put("ok",true);
    	return map;
	}
	
	
	@RequestMapping(value="resume/{id}", method = RequestMethod.GET)
	@ResponseBody 
	public Map<String,Object> resume(@PathVariable String id){
		Map<String,Object> map = new HashMap<String,Object>();
		scheduleJobService.resumeJob(id);
    	map.put("ok",true);
    	return map;
	}
	
	
	
	
}
