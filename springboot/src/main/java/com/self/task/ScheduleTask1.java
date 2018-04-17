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
 * 定时任务实现类（清明节活动专场提醒）
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
        
        logger.info("====（清明节活动专场提醒）ScheduleTask1 ====> 开启!" + simpleDateFormat.format(calendar.getTime()));
        try {

        	List<User> userList = userService.select(null);
			for (User user : userList) {
				logger.info("do something"+user.getUsername()+"notice消息");
			}
            
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
