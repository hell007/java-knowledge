package com.self.task;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.self.model.User;
import com.self.service.UserService;


/**
 * 定时任务实现类（取消超时生成的订单）
 */
@DisallowConcurrentExecution
public class ScheduleTask1 implements Job {
	
    private static Logger logger = LoggerFactory.getLogger(ScheduleTask1.class);
    
    @Autowired
    private  UserService userService;

	public void execute(JobExecutionContext context) throws JobExecutionException {
    	
    	// 定义日期格式
    	Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("当前时间: " + simpleDateFormat.format(calendar.getTime()));
        
        logger.info("====（取消超时生成的订单）ScheduleTask1 ====> 开启!" + simpleDateFormat.format(calendar.getTime()));
        
        List<User> list = userService.select(null);
		logger.info("已取消"+ list.get(0).getName()+"用户超时的订单");
    }
	
}
