package com.self.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.self.model.ScheduleJob;
import com.self.service.ScheduleJobService;


@Component
public class SchedulerAllJob {
	
    private static Logger logger = LoggerFactory.getLogger(SchedulerAllJob.class);
    
    //当前Trigger使用的 
    private Map<String, String> jobUniqueMap = new HashMap<String, String>(); 
    
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    
    @Autowired
    private ScheduleJobService scheduleJobService;
    
    /**
     * 
     * @throws SchedulerException
     */
    public void scheduleJobs() throws SchedulerException {
        try {
            //schedulerFactoryBean由spring创建注入
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            //查询所有定时任务
            List<ScheduleJob> jobList = scheduleJobService.selectAll();

            //获取最新删除(禁用)任务列表，将其从调度器中删除，并且从jobUniqueMap中删除
            List<ScheduleJob> jobDelList = scheduleJobService.selectDel();

            for (ScheduleJob delJob : jobDelList) {
                JobKey jobKey = JobKey.jobKey(delJob.getJobname(), delJob.getJobgroup());
                scheduler.deleteJob(jobKey);
                jobUniqueMap.remove(TriggerKey.triggerKey(delJob.getJobname(), delJob.getJobgroup()));
            }

            for (ScheduleJob job : jobList) {
                TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobname(), job.getJobgroup());

                //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

                //该job数据库中的Trigger表达式；不存在，创建一个
                String dbCron = job.getCronexpression();

                if (null == trigger) {
                    //JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(job.getJobName(), job.getJobGroup()).build();
                    try {
                        @SuppressWarnings("unchecked")
                        Class<?extends Job> clazz = (Class<?extends Job>) Class.forName(job.getQuartzclass());
                        JobDetail jobDetail = JobBuilder.newJob(clazz)
                        								.withIdentity(job.getJobname(),job.getJobgroup())
                        								.build();
                        jobDetail.getJobDataMap().put("scheduleJob", job);

                        // 表达式调度构建器
                        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronexpression());
                        // 按新的cronExpression表达式构建一个新的trigger
                        trigger = TriggerBuilder.newTrigger()
                                                .withIdentity(job.getJobname(),job.getJobgroup())
                                                .withSchedule(scheduleBuilder)
                                                .build();

                        jobUniqueMap.put(triggerKey.toString(), trigger.getCronExpression());
                        //currentCron = trigger.getCronExpression();
                        scheduler.scheduleJob(jobDetail, trigger);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                } else if (!jobUniqueMap.get(triggerKey.toString()).equals(dbCron)) {
                    // Trigger已存在，那么更新相应的定时设置
                    // 表达式调度构建器
                    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(dbCron);
                    // 按新的cronExpression表达式重新构建trigger
                    trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

                    trigger = trigger.getTriggerBuilder()
                                     .withIdentity(triggerKey)
                                     .withSchedule(scheduleBuilder)
                                     .build();
                    // 按新的trigger重新设置job执行
                    scheduler.rescheduleJob(triggerKey, trigger);

                    jobUniqueMap.put(triggerKey.toString(), dbCron);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
