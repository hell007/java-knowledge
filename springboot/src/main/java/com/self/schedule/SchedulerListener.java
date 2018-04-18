package com.self.schedule;


import org.quartz.Scheduler;
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
	
	/**
	 * Spring boot 集成Quartz的使用（解决quartz的job无法注入spring对象的问题）
	 * https://blog.csdn.net/mengruobaobao/article/details/79106343
	 */
	
	@Autowired  
    public SchedulerAllJob myScheduler;  
	
	@Autowired  
    private JobFactory jobFactory;
	  
      
    @Bean  
    public SchedulerFactoryBean schedulerFactoryBean(){  
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
    
}
