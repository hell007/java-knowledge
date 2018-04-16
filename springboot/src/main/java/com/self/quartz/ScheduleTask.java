package com.self.quartz;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * quartz单任务
 */

@Configuration
@Component
@EnableScheduling // 该注解必须要加
public class ScheduleTask { 
     public void scheduleTest() {
         System.err.println("scheduleTest开始定时执行");
    }
}
