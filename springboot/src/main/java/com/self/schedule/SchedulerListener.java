package com.self.schedule;


import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;


/**
* 调度工厂类
*/
@Configuration
@EnableScheduling
@Component
public class SchedulerListener {
	
	@Autowired  
    public SchedulerAllJob myScheduler;  
	
	
	/** 
     *  启动的时候执行该方法，或者是使用ApplicationListener，在启动的时候执行该方法 
     *  具体使用见：http://blog.csdn.net/liuchuanhong1/article/details/77568187 
     * @throws SchedulerException 
     */  
    //此处暂且注释，后续有后台定时任务逻辑 开启
    @Scheduled(fixedRate = 5000) // 每隔5s查库，并根据查询结果决定是否重新设置定时任务
    //@Scheduled(cron="0 08 18 ? * *")
    public void schedule() throws SchedulerException {   
            myScheduler.scheduleJobs();  
     }   
      
    @Bean  
    public SchedulerFactoryBean schedulerFactoryBean(){  
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();   
        return schedulerFactoryBean;   
    }
    
}
