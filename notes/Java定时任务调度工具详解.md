
## Java定时任务调度工具详解

### 介绍

> 任务调度: 基于给定的时间点，给定的时间间隔或者给定的执行次数自动执行的任务

在Java中的定时调度工具: Timer：能实现日常60%的定时任务; Quartz：能搞定一切


Timer与Quartz区别

```

出身不同：
    Timer由JDK提供，调用方式简单粗暴，不需要别的jar包支持
    Quartz源于开源项目，非JDK自带，需要引入别的jar包支持
    
能力区别：主要体现在对时间的控制上
    Timer实现如某个具体时间执行什么任务
    Quartz实现如每个星期天8点执行什么任务
    Quartz的时间控制功能远比Timer强大和完善
    
底层机制：
    Timer走后台线程执行定时任务
    Quartz能够使用多个执行线程去执行定时任务
    Quartz确实比Timer强大很多，但是，Timer能实现的功能，尽量不动用Quartz
        毕竟大哥的出场费要比小弟高
        
```


### Java定时任务调度工具详解之Timer


1、Timer的定义：有且仅有**一个后台线程**对多个业务线程进行**定时定频率**的调度


> 案列

需定时调度的业务逻辑类

```
package com.self.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

// 继承 TimerTask 类
public class MyTimerTask extends TimerTask{
    private String name;

    // 计数器，没执行一次加一
    private Integer count = 0;
    
    public MyTimerTask(String name){
        this.name = name;
    }

    // 重写 run 方法
    @Override
    public void run() {
        if (count < 3) {
            // 以yyyy-MM-dd HH:mm:ss的格式打印当前执行时间
            // 如2016-11-11 00:00:00
            Calendar calendar = Calendar.getInstance();
            // 定义日期格式
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("Current exec time is : " + simpleDateFormat.format(calendar.getTime()));

            // 打印当前 name 的内容
            System.out.println("Current exec name is : " + name);

            count++;
        }else{
            // 取消任务执行
            cancel();
            System.out.println("Task cancel!");
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```

定时调度类

```
/*
timer.schedule(任务名称,延迟时间,间隔调度时间);
timer.schedule(myTimerTask,2000L);
*/

package com.self.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

public class MyTimer {

    public static void main(String[] args){

        // 创建一个 Timer 实例
        Timer timer = new Timer();

        // 创建一个 MyTimerTask 实例
        MyTimerTask myTimerTask = new MyTimerTask("No.1");

        // 通过 Timer 定时定频率调用 MyTimerTask 的业务逻辑
        // 即第一次执行是在当前时间的两秒之后，之后每隔一秒钟执行一次
        timer.schedule(myTimerTask,2000L);

        System.out.println("schedule time is " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(myTimerTask.scheduledExecutionTime()));

        /**
         * 获取当前时间，并设置成距离当前时间三秒之后的时间
         * 如当前时间是2016-11-10 23:59:57
         * 则设置后的时间则为2016-11-11 00:00:00
         */
        Calendar calendar = Calendar.getInstance();
        // 定义日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current exec time is : " + simpleDateFormat.format(calendar.getTime()));

        calendar.add(Calendar.SECOND,3);

        // schedule的四种用法
        /**
         * 1.在时间等于或超过time的时候执行仅且执行一次task
         *   如在2016-11-11 00:00:00执行一次task，打印任务名字
         */
        //myTimerTask.setName("schedule1");
        //timer.schedule(myTimerTask,calendar.getTime());

        /**
         * 2.时间等于或超过time首次执行task
         *   之后每隔period毫秒重复执行一次task
         *   如在2016-11-11 00:00:00第一次执行task，打印任务名字
         *   之后每隔两秒执行一次task
         */
        //myTimerTask.setName("schedule2");
        //timer.schedule(myTimerTask,calendar.getTime(),2000L);

        /**
         * 3.等待delay毫秒后仅执行且执行一个task
         *   如现在是2016-11-11 00:00:00
         *   则在2016-11-11 00:00:01执行一次task，打印任务名字
         */
        //myTimerTask.setName("schedule3");
        //timer.schedule(myTimerTask,1000L);

        /**
         * 4.等到delay毫秒后首次执行task
         *   之后每隔period毫秒重复执行一次task
         *   如现在是2016-11-11 00:00:00
         *   则在2016-11-11 00:00:01第一次执行task，打印任务名字
         *   之后每隔两秒执行一次task
         */
        //myTimerTask.setName("schedule4");
        //timer.schedule(myTimerTask,1000L,2000L);

        // scheduleAtFixedRate的两种用法
        /**
         * 1.时间等于或超过time时首次执行task
         *   之后每隔period毫秒重复执行一次task
         *   如在2016-11-11 00:00:00第一次执行task，打印任务名字
         *   之后每隔两秒执行一次task
         */
        //myTimerTask.setName("scheduleAtFixedRate1");
        //timer.scheduleAtFixedRate(myTimerTask,calendar.getTime(),2000L);

        /**
         * 2.等待delay毫秒后首次执行task
         *   之后每隔period毫秒重复执行一次task
         *   如现在是2016-11-11 00:00:00
         *   则在2016-11-11 00:00:01第一次执行task，打印任务名字
         *   之后每隔两秒执行一次task
         */
        //myTimerTask.setName("scheduleAtFixedRate2");
        //timer.scheduleAtFixedRate(myTimerTask,1000L,2000L);

    }

}

```

2、**知识总结**

> TimerTask类的cancel()

作用
    取消当前TimerTask里的任务
    
> TimerTask类的scheduleExecutionTime()

作用
    返回此任务最近实际执行的已安排执行的时间
返回值
    最近发生此任务执行安排的时间，为long型
    
> Timer的cancel()

作用
    终止此计时器，丢弃所有当前已安排的任务
    
> Timer的purge()

作用
    从此计时器的任务队列中移除所有已取消的任务
返回值
    从队列中移除的任务数


3、schedule和scheduleAtFixedRate的区别

两种情况看区别

  首次计划执行的时间早于当前的时间
  任务执行所需时间超出任务的执行周期间隔

首次计划执行的时间早于当前的时间

  1.schedule方法
  “fixed-delay”，如果第一次执行时间被delay了，
  随后的执行时间按照上一次实际执行完成的时间点进行计算
  2.scheduleAtFixedRate方法
  “fixed-rate”，如果第一次执行时间被delay了，
  随后的执行时间按照上一次开始的时间点进行计算，
  并且为了赶上进度会多次执行任务，因此TimerTask中的执行体需要考虑同步

任务执行所需时间超出任务的执行周期间隔

  1. schedule方法
  下一次执行时间相对于上一次实际执行完成的时间点，因此执行时间会不断延后
  2.scheduleAtFixedRate方法
  下一次执行时间相对于上一次开始的时间点，因此执行时间一般不会延后，因此存在并发性


4、Timer缺陷

（1）、管理并发任务的缺陷

Timer有且仅有一个线程去执行定时任务，如果存在多个任务，且任务时间过长，会导致执行效果与预期不符

（2）、当任务抛出异常时的缺陷

如果TimerTask抛出RuntimeException，Timer会停止所有任务的运行


总结：Timer的使用禁区

对时效性要求较高的多任务并发作业

对复杂任务的调度


###  Java定时任务调度工具详解之Quartz


### Quartz介绍

1.Quartz特点

    强大的调度功能
    灵活的应用方式
    分布式和集群能力
    Quartz的设计模式

2.Builder模式

    Factory模式
    组件模式
    链式写法

3.Quartz三个核心概念

    调度器
    任务
    触发器
    
4.Quartz体系结构

    JobDetail
    trigger
        SimpleTrigger
        CronTrigger
    scheduler
        start
        stop
        pause
        resume
        
5.Quartz重要组成

    Job
    JobDetail
    JobBuilder
    JobStore
    Trigger
    TriggerBuilder
    ThreadPool
    Scheduler
    Calendar：一个Trigger可以和多个Calendar关联，以排除或包含某些时间点
    监听器
        JobListener
        TriggerListener
        SchedulerListener










