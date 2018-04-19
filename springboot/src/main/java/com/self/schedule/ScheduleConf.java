package com.self.schedule;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class ScheduleConf {
	
	/**
	 * Spring boot 集成Quartz的使用（解决quartz的job无法注入spring对象的问题）
	 * https://blog.csdn.net/mengruobaobao/article/details/79106343
	 */
	
	@Autowired  
    private JobFactory jobFactory;  
  
	
    @Bean  
    public SchedulerFactoryBean schedulerFactoryBean() {  
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();  
        schedulerFactoryBean.setJobFactory(jobFactory);  
        return schedulerFactoryBean;  
    }  
      
    /**
     * 注入了一个 自定义的JobFactory ，然后 把其设置为SchedulerFactoryBean 的 JobFactory。
     * 其目的是因为我在具体的Job 中 需要Spring 注入一些Service。
     * 所以我们要自定义一个jobfactory， 让其在具体job 类实例化时 使用Spring 的API 来进行依赖注入
     * @return
     */
    @Bean  
    public Scheduler scheduler() {  
        return schedulerFactoryBean().getScheduler();  
    }  
    
    
}
