package com.self.schedule;

import java.util.HashMap;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.self.model.ScheduleJob;

@Component
public class ScheduleJobManager {
	
	private static Logger logger = LoggerFactory.getLogger(ScheduleJobManager.class);
	
	
	@Autowired  
    private  Scheduler scheduler;
    //当前Trigger使用的 
    private static Map<String, String> jobUniqueMap = new HashMap<String, String>();
    

	/**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(String jobname, String jobgroup) {
        return TriggerKey.triggerKey(jobname,jobgroup);
    }
    
    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(String jobname, String jobgroup) {
        return JobKey.jobKey(jobname + jobgroup);
    }
    
    /**
     * 获取表达式触发器
     */
    public CronTrigger getCronTrigger(TriggerKey triggerKey) {   	
        try {
            return (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (Exception e) {
        	logger.error("getCronTrigger 获取定时任务CronTrigger出现异常!");
            throw new RuntimeException(e);
        }
    }
	
    
    /**
     * 创建任务
     * @param job
     */
	public void createScheduleJob(ScheduleJob job) {
		
		TriggerKey triggerKey = getTriggerKey(job.getJobname(), job.getJobgroup());
        //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
        CronTrigger trigger = getCronTrigger(triggerKey);
        //该job数据库中的Trigger表达式；不存在，创建一个
        String dbCron = job.getCronexpression(); 
        
        if (null == trigger) {
	        try {       
	        	//构建job信息 
	        	@SuppressWarnings("unchecked")
	            Class<?extends Job> clazz = (Class<?extends Job>) Class.forName(job.getQuartzclass());
	        	JobDetail jobDetail = JobBuilder.newJob(clazz)
						.withIdentity(job.getJobname(),job.getJobgroup())
						.build();
	        	
	        	jobDetail.getJobDataMap().put("scheduleJob", job);
	
	            //表达式调度构建器
	            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronexpression());
	            
	            //按新的cronExpression表达式构建一个新的trigger
	            trigger = TriggerBuilder.newTrigger()
	                                    .withIdentity(job.getJobname(),job.getJobgroup())
	                                    .withSchedule(scheduleBuilder)
	                                    .build();
	
	            jobUniqueMap.put(triggerKey.toString(), trigger.getCronExpression());
	
	            scheduler.scheduleJob(jobDetail, trigger); 
	            
	            logger.info("createScheduleJob 创建了"+job.getJobname()+"任务!");
	            
	        } catch (Exception e) {  
	        	logger.error("createScheduleJob 创建任务失败!");
	            throw new RuntimeException(e);  
	        }  
        } else if (!jobUniqueMap.get(triggerKey.toString()).equals(dbCron)) {
        	
        	updateScheduleJob(job);
        }
      
    } 
	
	
	/**
	 * 更新任务
	 * @param job
	 */
	public void updateScheduleJob(ScheduleJob job) {
		
		TriggerKey triggerKey = getTriggerKey(job.getJobname(), job.getJobgroup());
        //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
        CronTrigger trigger = getCronTrigger(triggerKey);
        //该job数据库中的Trigger表达式；不存在，创建一个
        String dbCron = job.getCronexpression();  
        
        try {  
        	//Trigger已存在，那么更新相应的定时设置
            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(dbCron);
            
            //按新的cronExpression表达式重新构建trigger
            trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder()
                             .withIdentity(triggerKey)
                             .withSchedule(scheduleBuilder)
                             .build();
            
            //按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);

            jobUniqueMap.put(triggerKey.toString(), dbCron);
            
            logger.info("updateScheduleJob 更新了"+job.getJobname()+"任务!");
             
        } catch (Exception e) { 
        	logger.error("updateScheduleJob 更新任务失败!");
            throw new RuntimeException(e);  
        }  
    } 
	
	
	/**
	 * 暂停任务
	 * @param job
	 */
    public void pauseScheduleJob(ScheduleJob job) {
        try {      
            //停止触发器  
            scheduler.pauseTrigger(getTriggerKey(job.getJobname(), job.getJobgroup())); 
            //停止任务
            scheduler.pauseJob(getJobKey(job.getJobname(), job.getJobgroup()));
        } catch (Exception e) {
        	logger.error("pauseScheduleJob 暂停任务失败!");
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * 恢复任务
     * @param job
     */
    public void resumeScheduleJob(ScheduleJob job) {
        try {
            //恢复触发器 
            scheduler.resumeTrigger(getTriggerKey(job.getJobname(), job.getJobgroup()));
            //恢复任务
            scheduler.resumeJob(getJobKey(job.getJobname(), job.getJobgroup()));
        } catch (Exception e) {
        	logger.error("resumeScheduleJob 恢复任务失败!");
            throw new RuntimeException(e);
        }
    }
	
	
	/**
	 * 删除任务
	 * @param job
	 */
	public void removeScheduleJob(ScheduleJob job) {  
        try {     	
            //停止触发器  
            scheduler.pauseTrigger(getTriggerKey(job.getJobname(), job.getJobgroup()));  
            //移除触发器  
            scheduler.unscheduleJob(getTriggerKey(job.getJobname(), job.getJobgroup()));  
            jobUniqueMap.remove(getTriggerKey(job.getJobname(), job.getJobgroup()));
            //删除任务  
            scheduler.deleteJob(getJobKey(job.getJobname(), job.getJobgroup()));                
        } catch (Exception e) {  
        	logger.error("removeScheduleJob 删除任务失败!");
            throw new RuntimeException(e);  
        }  
    } 
	
	
	/**
	 * 启动scheduler
	 */
	public void startScheduleJob() {  
        try {  
            scheduler.start();  
        } catch (Exception e) {  
        	logger.error("startScheduleJob 启动scheduler失败!");
            throw new RuntimeException(e);  
        }  
    }
	
	
	/**
	 * 备用scheduler
	 */
	public void standbyScheduleJob() {  
        try {  
            scheduler.standby();  
        } catch (Exception e) {  
        	logger.error("standbyScheduleJob 备用scheduler失败!");
            throw new RuntimeException(e);  
        }  
    }
	
	
	/**
	 * 关闭scheduler,停止后不能重新开始,否则会报错
	 */
	public void shutdownScheduleJob() {  
        try {  
            if (!scheduler.isShutdown()) {  
                scheduler.shutdown();  
            }  
        } catch (Exception e) {  
        	logger.error("shutdownScheduleJob 关闭scheduler失败!");
            throw new RuntimeException(e);  
        }  
    } 
	
}
