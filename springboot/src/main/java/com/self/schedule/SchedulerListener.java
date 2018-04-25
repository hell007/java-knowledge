package com.self.schedule;


import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.self.service.ScheduleJobService;


@Configuration
@EnableScheduling
@Component
public class SchedulerListener {
	
    @Autowired  
    public ScheduleJobService scheduleJobService;  
	
    
    /** 
     *  启动的时候执行该方法，或者是使用ApplicationListener，在启动的时候执行该方法 
     *  具体使用见：http://blog.csdn.net/liuchuanhong1/article/details/77568187 
     */  
    // 每天的某个时间点，启动所有的定时任务，然后每个定时任务根据自己的cron="* * * * * *" 定时启动	
    // 此处的配置与 applicationContext-quartz.xml  配置相同
    //@Scheduled(cron="0 08 18 ? * *")
    public void schedule() {   
    	scheduleJobService.scheduleJobs();  
     } 
    
}
