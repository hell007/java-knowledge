
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



### 案列


任务类

```
package com.self.quartz.one;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 编写 自定义任务
 */
public class HelloJob implements Job{
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 打印当前的执行时间，格式为2017-01-01 00:00:00
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current Exec Time Is : " + sf.format(date));
        
        // 编写具体的业务逻辑
        System.out.println("Hello World!");
    }
    
}


/**
 * 编写 自定义任务
 */
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

/**
 * 改造 自定义任务
 * @author ZhangCheng on 2017-06-26
 *
 */
public class HelloJob implements Job{
    
    // 方式二：getter和setter获取
    // 成员变量 与 传入参数的key一致
    private String message;
    private Float floatJobValue;
    private Double doubleTriggerValue;
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Float getFloatJobValue() {
        return floatJobValue;
    }
    public void setFloatJobValue(Float floatJobValue) {
        this.floatJobValue = floatJobValue;
    }
    public Double getDoubleTriggerValue() {
        return doubleTriggerValue;
    }
    public void setDoubleTriggerValue(Double doubleTriggerValue) {
        this.doubleTriggerValue = doubleTriggerValue;
    }
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 打印当前的执行时间，格式为2017-01-01 00:00:00
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current Exec Time Is : " + sf.format(date));
        
        // 编写具体的业务逻辑
        //System.out.println("Hello World!");
        
        JobKey key = context.getJobDetail().getKey();
        System.out.println("My name and group are : " + key.getName() + " : " + key.getGroup());
        
        TriggerKey trkey = context.getTrigger().getKey();
        System.out.println("My Trigger name and group are : " + trkey.getName() + " : " + trkey.getGroup());
        
        // 方式一：Map中直接  获取自定义参数
        JobDataMap jdataMap = context.getJobDetail().getJobDataMap();
        JobDataMap tdataMap = context.getTrigger().getJobDataMap();
        String jobMsg = jdataMap.getString("message");
        Float jobFloatValue = jdataMap.getFloat("floatJobValue");
        
        String triMsg = tdataMap.getString("message");
        Double triDoubleValue = tdataMap.getDouble("doubleTriggerValue");
        
        System.out.println("jobMsg is : " + jobMsg);
        System.out.println("jobFloatValue is : " + jobFloatValue);
        System.out.println("triMsg is : " + triMsg);
        System.out.println("triDoubleValue is : " + triDoubleValue);
        
        // 方式一：Map中直接获取 获取自定义参数
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        jobMsg = jobDataMap.getString("message");
        jobFloatValue = jobDataMap.getFloat("floatJobValue");
        
        triMsg = jobDataMap.getString("message");
        triDoubleValue = jobDataMap.getDouble("doubleTriggerValue");
        
        System.out.println("jobMsg is : " + jobMsg);
        System.out.println("jobFloatValue is : " + jobFloatValue);
        System.out.println("triMsg is : " + triMsg);
        System.out.println("triDoubleValue is : " + triDoubleValue);
        
        // 方式二：getter和setter获取
        System.out.println("message is : " + this.message);
        System.out.println("jobFloatValue is : " + this.floatJobValue);
        System.out.println("triDoubleValue is : " + this.doubleTriggerValue);
    }
    
}

````

任务调度类

```
package com.self.quartz.one;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 编写 任务调度类
 */
public class HelloScheduler {
    
    public static void main(String[] args) throws SchedulerException {
        
        // 创建一个 JobDetail 实例，将该实例与 HelloJob 实例绑定
        JobDetail jobDeatil = JobBuilder.newJob(HelloJob.class)
                .withIdentity("myjob", "jobgroup1")// 定义标识符
                .build();
        
        System.out.println("jobDetail's name : " + jobDeatil.getKey().getName());
        System.out.println("jobDetail's group : " + jobDeatil.getKey().getGroup());
        System.out.println("jobDetail's jobClass : " + jobDeatil.getJobClass().getName());
        
        // 创建一个 Trigger 实例，定义该 job 立即执行，并且每隔两秒重复执行一次，直到永远
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger","trigroup1")// 定义标识符
                .startNow()// 定义立即执行
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInSeconds(2)
                        .repeatForever())// 定义执行频度
                .build();
        
        // 创建 Scheduler 实例
        SchedulerFactory sfact = new StdSchedulerFactory();
        Scheduler scheduler = sfact.getScheduler();
        
        // 绑定 JobDetail 和 trigger
        scheduler.scheduleJob(jobDeatil, trigger);
        
        // 执行任务
        scheduler.start();
        
        // 打印当前的执行时间，格式为2017-01-01 00:00:00
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current Time Is : " + sf.format(date));
    }
    
}


/**
 * 改造 任务调度类
 */
 
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 编写 任务调度类
 * @author ZhangCheng on 2017-06-26
 *
 */
public class HelloScheduler {
    
    public static void main(String[] args) throws SchedulerException {
        
        // 创建一个 JobDetail 实例，将该实例与 HelloJob 实例绑定
        JobDetail jobDeatil = JobBuilder.newJob(HelloJob.class)
                .withIdentity("myjob", "jobgroup1")// 定义标识符
                .usingJobData("message", "hello myjob1")// 传入自定义参数
                .usingJobData("floatJobValue", 3.14F)// 传入自定义参数
                .build();
        
        System.out.println("jobDetail's name : " + jobDeatil.getKey().getName());
        System.out.println("jobDetail's group : " + jobDeatil.getKey().getGroup());
        System.out.println("jobDetail's jobClass : " + jobDeatil.getJobClass().getName());
        
        // 创建一个 Trigger 实例，定义该 job 立即执行，并且每隔两秒重复执行一次，直到永远
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger","trigroup1")// 定义标识符
                .usingJobData("message", "hello mytrigger1")// 传入自定义参数
                .usingJobData("doubleTriggerValue", 2.0D)// 传入自定义参数
                .startNow()// 定义立即执行
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInSeconds(2)
                        .repeatForever())// 定义执行频度
                .build();
        
        // 创建 Scheduler 实例
        SchedulerFactory sfact = new StdSchedulerFactory();
        Scheduler scheduler = sfact.getScheduler();
        
        // 绑定 JobDetail 和 trigger
        scheduler.scheduleJob(jobDeatil, trigger);
        
        // 执行任务
        scheduler.start();
        
        // 打印当前的执行时间，格式为2017-01-01 00:00:00
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current Time Is : " + sf.format(date));
    }
    
}
```


### 分析

Job定义：实现业务逻辑的任务接口

Job：Job接口非常容易实现，只有一个execute方法，类似TimerTask的run方法，在里面编写业务逻辑

Job接口源码

```
public interface Job {
    void execute(JobExecutionContext context) throws JobExecutionException;
}
```

Job实例在Quartz中的生命周期

每次调度器执行job时，它在调用execute方法前会创建一个新的job实例
当调用完成后，关联的job对象实例会被释放，释放的实例会被垃圾回收机制回收

JobDetail

JobDetail为Job实例提供了许多设置属性，以及JobDetailMap成员变量属性，它用来存储特定Job实例的状态信息，
调度器需要借助JobDetail对象来添加Job实例。

JobDetail属性

    name：任务名称
    group：任务所属组
    jobClass：任务实现类
    jobDataMap：传参的作用

JobExecutionContext是什么

    当Scheduler调用一个Job，就会将JobExecutionContext传递给Job的execute()方法；
    Job能通过JobExecutionContext对象访问到Quartz运行时候的环境以及Job本身的明细数据。

JobDataMap是什么

    在进行任务调度时JobDataMap存储在JobExecutionContext中，非常方便获取
    JobDataMap可以用来装载任务可序列化的数据对象，当job实例对象被执行时这些参数对象会传递给它
    JobDataMap实现了JDK的Map接口，并且添加了一些非常方便的方法用来存取基本数据类型

获取JobDataMap的两种方式

    从Map中直接获取
    
    Job实现类中添加setter方法对应JobDataMap的键值
    （Quartz框架默认的JobFactory实现类在初始化job实例对象时会自动地调用这些setter方式）

Trigger是什么

    Quartz中的触发器用来告诉调度程序作业什么时候触发
    即Trigger对象时用来触发执行Job的


触发器通用属性

    JobKey：表示job实例的标识，触发器被触发时，该指定的job实例会执行
    StartTime：表示触发器的时间表首次被触发的时间，它的值的类型是Java.util.Date
    EndTime：指定触发器的不再被触发的时间，它的值的类型是Java.util.Date

```
/**
 * 自定义任务  触发器通用属性
 */
public class HelloJob implements Job{
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 打印当前的执行时间，格式为2017-01-01 00:00:00
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current Exec Time Is : " + sf.format(date));
        
        Trigger currentTrigger = context.getTrigger();
        System.out.println("Start Time Is : " + sf.format(currentTrigger.getStartTime()));
        System.out.println("End Time Is : " + sf.format(currentTrigger.getEndTime()));
        
        JobKey jobKey = currentTrigger.getJobKey();
        System.out.println("JobKey info : " + " jobName : " + jobKey.getName()
                + " jobGroup : " + jobKey.getGroup());
    }
    
}

```

```
/**
 * 任务调度类  触发器通用属性
 */
public class HelloScheduler {
    
    public static void main(String[] args) throws SchedulerException {
        // 打印当前的执行时间，格式为2017-01-01 00:00:00
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current Time Is : " + sf.format(date));
        
        // 创建一个 JobDetail 实例，将该实例与 HelloJob 实例绑定
        JobDetail jobDeatil = JobBuilder.newJob(HelloJob.class)
                .withIdentity("myjob", "jobgroup1")// 定义标识符
                .build();
        
        // 获取距离当前时间3秒后的时间
        date.setTime(date.getTime() + 3000);
        // 获取距离当前时间6秒后的时间
        Date endDate = new Date();
        endDate.setTime(endDate.getTime() + 6000);
        
        // 创建一个 Trigger 实例，定义该 job 立即执行，并且每隔两秒重复执行一次，直到永远
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger","trigroup1")// 定义标识符
                .startAt(date)// 定义3秒后执行
                .endAt(endDate)// 定义6秒后结束
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInSeconds(2)
                        .repeatForever())// 定义执行频度
                .build();
        
        // 创建 Scheduler 实例
        SchedulerFactory sfact = new StdSchedulerFactory();
        Scheduler scheduler = sfact.getScheduler();
        
        // 绑定 JobDetail 和 trigger
        scheduler.scheduleJob(jobDeatil, trigger);
        
        // 执行任务
        scheduler.start();
    }
    
}

```


Scheduler


Scheduler工厂模式

    所有的Scheduler实例应该由SchedulerFactory来创建


StdSchedulerFactory

    使用一组参数（Java.util.Properties）来创建和初始化Quartz调度器
    配置参数一般存储在quartz.properties中
    调用getScheduler方法就能创建和初始化调度器对象

Scheduler的主要函数

    // 绑定 jobDetail 和 trigger，将它注册进 Scheduler 当中
    Date scheduleJob(JobDetail jobDetail, Trigger trigger)
    // 启动 Scheduler
    void start()
    // 暂停 Scheduler
    void standby()
    // 关闭 Scheduler
    void shutdown()

QuartzProperties文件

quartz.properties组成部分

    调度器属性
    线程池属性
    作业存储设置
    插件配置
    
线程池属性

    threadCount：工作线程数量
    threadPriority：工作线程优先级
    org.quartz.threadPool.class：配置线程池实现类

作业存储设置

    描述了在调度器实例的生命周期中，Job和Trigger信息是如何被存储的

插件配置

    满足特定需求用到的Quartz插件的配置


```

# Default Properties file for use by StdSchedulerFactory
# to create a Quartz Scheduler Instance, if a different
# properties file is not explicitly specified.
#
# ===========================================================================
# Configure Main Scheduler Properties 调度器属性
# ===========================================================================
org.quartz.scheduler.instanceName: DefaultQuartzScheduler
org.quartz.scheduler.instanceid:AUTO
org.quartz.scheduler.rmi.export: false
org.quartz.scheduler.rmi.proxy: false
org.quartz.scheduler.wrapJobExecutionInUserTransaction: false
# ===========================================================================  
# Configure ThreadPool 线程池属性  
# ===========================================================================
#线程池的实现类（一般使用SimpleThreadPool即可满足几乎所有用户的需求）
org.quartz.threadPool.class: org.quartz.simpl.SimpleThreadPool
#指定线程数，至少为1（无默认值）(一般设置为1-100直接的整数合适)
org.quartz.threadPool.threadCount: 10
#设置线程的优先级（最大为java.lang.Thread.MAX_PRIORITY 10，最小为Thread.MIN_PRIORITY 1，默认为5）
org.quartz.threadPool.threadPriority: 5
#设置SimpleThreadPool的一些属性
#设置是否为守护线程
#org.quartz.threadpool.makethreadsdaemons = false
#org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread: true
#org.quartz.threadpool.threadsinheritgroupofinitializingthread=false
#线程前缀默认值是：[Scheduler Name]_Worker
#org.quartz.threadpool.threadnameprefix=swhJobThead;
# 配置全局监听(TriggerListener,JobListener) 则应用程序可以接收和执行 预定的事件通知
# ===========================================================================
# Configuring a Global TriggerListener 配置全局的Trigger监听器
# MyTriggerListenerClass 类必须有一个无参数的构造函数，和 属性的set方法，目前2.2.x只支持原始数据类型的值（包括字符串）
# ===========================================================================
#org.quartz.triggerListener.NAME.class = com.swh.MyTriggerListenerClass
#org.quartz.triggerListener.NAME.propName = propValue
#org.quartz.triggerListener.NAME.prop2Name = prop2Value
# ===========================================================================
# Configuring a Global JobListener 配置全局的Job监听器
# MyJobListenerClass 类必须有一个无参数的构造函数，和 属性的set方法，目前2.2.x只支持原始数据类型的值（包括字符串）
# ===========================================================================
#org.quartz.jobListener.NAME.class = com.swh.MyJobListenerClass
#org.quartz.jobListener.NAME.propName = propValue
#org.quartz.jobListener.NAME.prop2Name = prop2Value
# ===========================================================================  
# Configure JobStore 存储调度信息（工作，触发器和日历等）
# ===========================================================================
# 信息保存时间 默认值60秒
org.quartz.jobStore.misfireThreshold: 60000
#保存job和Trigger的状态信息到内存中的类
org.quartz.jobStore.class: org.quartz.simpl.RAMJobStore
# ===========================================================================  
# Configure SchedulerPlugins 插件属性 配置
# ===========================================================================
# 自定义插件  
#org.quartz.plugin.NAME.class = com.swh.MyPluginClass
#org.quartz.plugin.NAME.propName = propValue
#org.quartz.plugin.NAME.prop2Name = prop2Value
#配置trigger执行历史日志（可以看到类的文档和参数列表）
org.quartz.plugin.triggHistory.class = org.quartz.plugins.history.LoggingTriggerHistoryPlugin  
org.quartz.plugin.triggHistory.triggerFiredMessage = Trigger {1}.{0} fired job {6}.{5} at: {4, date, HH:mm:ss MM/dd/yyyy}  
org.quartz.plugin.triggHistory.triggerCompleteMessage = Trigger {1}.{0} completed firing job {6}.{5} at {4, date, HH:mm:ss MM/dd/yyyy} with resulting trigger instruction code: {9}  
#配置job调度插件  quartz_jobs(jobs and triggers内容)的XML文档  
#加载 Job 和 Trigger 信息的类   （1.8之前用：org.quartz.plugins.xml.JobInitializationPlugin）
org.quartz.plugin.jobInitializer.class = org.quartz.plugins.xml.XMLSchedulingDataProcessorPlugin
#指定存放调度器(Job 和 Trigger)信息的xml文件，默认是classpath下quartz_jobs.xml
org.quartz.plugin.jobInitializer.fileNames = my_quartz_job2.xml  
#org.quartz.plugin.jobInitializer.overWriteExistingJobs = false  
org.quartz.plugin.jobInitializer.failOnFileNotFound = true  
#自动扫描任务单并发现改动的时间间隔,单位为秒
org.quartz.plugin.jobInitializer.scanInterval = 10
#覆盖任务调度器中同名的jobDetail,避免只修改了CronExpression所造成的不能重新生效情况
org.quartz.plugin.jobInitializer.wrapInUserTransaction = false
# ===========================================================================  
# Sample configuration of ShutdownHookPlugin  ShutdownHookPlugin插件的配置样例
# ===========================================================================
#org.quartz.plugin.shutdownhook.class = \org.quartz.plugins.management.ShutdownHookPlugin
#org.quartz.plugin.shutdownhook.cleanShutdown = true
#
# Configure RMI Settings 远程服务调用配置
#
#如果你想quartz-scheduler出口本身通过RMI作为服务器，然后设置“出口”标志true(默认值为false)。
#org.quartz.scheduler.rmi.export = false
#主机上rmi注册表(默认值localhost)
#org.quartz.scheduler.rmi.registryhost = localhost
#注册监听端口号（默认值1099）
#org.quartz.scheduler.rmi.registryport = 1099
#创建rmi注册，false/never：如果你已经有一个在运行或不想进行创建注册
# true/as_needed:第一次尝试使用现有的注册，然后再回来进行创建
# always:先进行创建一个注册，然后再使用回来使用注册
#org.quartz.scheduler.rmi.createregistry = never
#Quartz Scheduler服务端端口，默认是随机分配RMI注册表
#org.quartz.scheduler.rmi.serverport = 1098
#true:链接远程服务调度(客户端),这个也要指定registryhost和registryport，默认为false
# 如果export和proxy同时指定为true，则export的设置将被忽略
#org.quartz.scheduler.rmi.proxy = false

```


###  Spring、Quartz大合体

##### 使用Quartz配置作业两种方式

    MethodInvokingJobDetailFactoryBean
    JobDetailFactoryBean
    
方式一：使用MethodInvokingJobDetailFactoryBean

```
package com.self.springquartz.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class MyBean {
    
    public void printMessage(){
        
        // 打印当前的执行时间，格式为2017-01-01 00:00:00
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        System.out.println("MyBean Executes!" + sf.format(date));
    }
    
}
```                


方式二：使用JobDetailFactoryBean


```

package com.myimooc.springquartz.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class FirstScheduledJob extends QuartzJobBean {
    
    private AnotherBean anotherBean;
    
    public void setAnotherBean(AnotherBean anotherBean) {
        this.anotherBean = anotherBean;
    }

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        System.out.println("FirstScheduledJob Excutes!" + sf.format(date));
        this.anotherBean.printAnotherMessage();
    }

}

```

QuartzConfig类

```

package com.self.springquartz.config;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.myimooc.springquartz.quartz.AnotherBean;
import com.myimooc.springquartz.quartz.FirstScheduledJob;
import com.myimooc.springquartz.quartz.MyBean;

/**
 * Quartz 配置类
 */
@Configuration
public class QuartzConfig {
    
    @Autowired
    private MyBean myBean;
    
    @Autowired
    private AnotherBean anotherBean;
    
    /**
     * 方式一：使用MethodInvokingJobDetailFactoryBean
     * @return
     */
    @Bean
    public MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean(){
        MethodInvokingJobDetailFactoryBean mb = new MethodInvokingJobDetailFactoryBean();
        mb.setTargetObject(myBean);// 指定要运行的类
        mb.setTargetMethod("printMessage");// 指定要允许类中的那个方法
        return mb;
    }
    
    /**
     * 方式二：使用JobDetailFactoryBean
     * @return
     */
    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean(){
        JobDetailFactoryBean  jb= new JobDetailFactoryBean();
        jb.setJobClass(FirstScheduledJob.class);// 指定要运行的类
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("anotherBean", anotherBean);
        
        jb.setJobDataMap(jobDataMap);// 设置传入自定义参数
        jb.setDurability(true);
        return jb;
    }
    
    /**
     * 配置 SimpleTriggerFactoryBean
     * @return
     */
    @Bean
    public SimpleTriggerFactoryBean simpleTriggerFactoryBean(){
        SimpleTriggerFactoryBean sb = new SimpleTriggerFactoryBean();
        sb.setJobDetail(methodInvokingJobDetailFactoryBean().getObject());// 设置需要绑定的 jobDetail
        sb.setStartDelay(1000L);// 距离当前时间1秒后执行
        sb.setRepeatInterval(2000L);// 之后每隔2秒执行一次
        return sb;
    }
    
    /**
     * 配置 SimpleTriggerFactoryBean
     * @return
     */
    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean(){
        CronTriggerFactoryBean cb = new CronTriggerFactoryBean();
        cb.setJobDetail(jobDetailFactoryBean().getObject());// 设置需要绑定的 jobDetail
        cb.setStartDelay(1000L);// 距离当前时间1秒后执行
        cb.setCronExpression("0/5 * * ? * *");// 设置 Cron 表达式，之后每隔5秒执行一次
        return cb;
    }
    
    /**
     * 配置 SchedulerFactoryBean
     * @return
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean sb= new SchedulerFactoryBean();
        // 配置 JobDetails
        JobDetail[] jobDetails = new JobDetail[2];
        jobDetails[0] = methodInvokingJobDetailFactoryBean().getObject();
        jobDetails[1] = jobDetailFactoryBean().getObject();
        sb.setJobDetails(jobDetails);
        
        // 配置 Trigger
        Trigger[] triggers = new Trigger[2];
        triggers[0] = simpleTriggerFactoryBean().getObject();
        triggers[1] = cronTriggerFactoryBean().getObject();
        sb.setTriggers(triggers);
        return sb;
        
    }
}

```

###  定时任务@Scheduled表达式

1.我们使用@Schedu来创建定时任务 这个注解用来标注一个定时任务方法。官网：www.fhadmin.org 通过看@Scheduled源码可以看出它支持多种参数：


（1）cron：cron表达式，指定任务在特定时间执行；

    @Scheduled(cron = "0 0/1 * * * ?")  
    //cron接受cron表达式，根据cron表达式确定定时规则

（2）fixedDelay：表示上一次任务执行完成后多久再次执行，参数类型为long，单位ms；

    @Scheduled(fixedDelay = 5000)        
    //fixedDelay = 5000表示当前方法执行完毕5000ms后，Spring scheduling会再次调用该方法

（3）fixedDelayString：与fixedDelay含义一样，只是参数类型变为String；

（4）fixedRate：表示按一定的频率执行任务，参数类型为long，单位ms；

    @Scheduled(fixedRate = 5000)        
    //fixedRate = 5000表示当前方法开始执行5000ms后，Spring scheduling会再次调用该方法

（5）fixedRateString: 与fixedRate的含义一样，只是将参数类型变为String；

（6）initialDelay：表示延迟多久再第一次执行任务，参数类型为long，单位ms；

    @Scheduled(initialDelay = 1000, fixedRate = 5000)   
    //initialDelay = 1000表示延迟1000ms执行第一次任务

（7）initialDelayString：与initialDelay的含义一样，只是将参数类型变为String；

（8）zone：时区，默认为当前时区，一般没有用到。

2.cron详解

> cron表达式定义

    @Scheduled(cron = "0 0/1 * * * ?") 

Cron表达式是一个字符串，是由空格隔开的6或7个域组成，每一个域对应一个含义（秒 分 时 每月第几天 月 星期 年）其中年是可选字段。
但是需要注意，spring的schedule值支持6个域的表达式，也就是不能设定年，如果超过六个则会报错。

3. 每个域可出现的字符类型和各字符的含义

（1）各域支持的字符类型

    秒：可出现", - * /"四个字符，有效范围为0-59的整数  

    分：可出现", - * /"四个字符，有效范围为0-59的整数  

    时：可出现", - * /"四个字符，有效范围为0-23的整数  

    每月第几天：可出现", - * / ? L W C"八个字符，有效范围为0-31的整数  

    月：可出现", - * /"四个字符，有效范围为1-12的整数或JAN-DEc  

    星期：可出现", - * / ? L C #"四个字符，有效范围为1-7的整数或SUN-SAT两个范围。1表示星期天，2表示星期一， 依次类推

（2）特殊字符含义

    * : 表示匹配该域的任意值，比如在秒*, 就表示每秒都会触发事件。；

    ? : 只能用在每月第几天和星期两个域。表示不指定值，当2个子表达式其中之一被指定了值以后，为了避免冲突，需要将另一个子表达式的值设为“?”；

    - : 表示范围，例如在分域使用5-20，表示从5分到20分钟每分钟触发一次  

    / : 表示起始时间开始触发，然后每隔固定时间触发一次，例如在分域使用5/20,则意味着5分，25分，45分，分别触发一次.  

    , : 表示列出枚举值。例如：在分域使用5,20，则意味着在5和20分时触发一次。  

    L : 表示最后，只能出现在星期和每月第几天域，如果在星期域使用1L,意味着在最后的一个星期日触发。  

    W : 表示有效工作日(周一到周五),只能出现在每月第几日域，系统将在离指定日期的最近的有效工作日触发事件。注意一点，W的最近寻找不会跨过月份  

    LW : 这两个字符可以连用，表示在某个月最后一个工作日，即最后一个星期五。  

    # : 用于确定每个月第几个星期几，只能出现在每月第几天域。例如在1#3，表示某月的第三个星期日。

（3）表达式例子

    "秒 分 时  天 月  星期"
    
    "0 0 * * * *"              表示每小时0分0秒执行一次

    " */10 * * * * *"          表示每10秒执行一次

    "0 0 8-10 * * *"           表示每天8，9，10点执行

    "0 0/30 8-10 * * *"        表示每天8点到10点，每半小时执行

    "0 0 9-17 * * MON-FRI"     表示每周一至周五，9点到17点的0分0秒执行

    "0 0 0 25 12 ?"            表示每年圣诞节（12月25日）0时0分0秒执行
    
    

### 开发注意点

1.[Spring boot 集成Quartz的使用（解决quartz的job无法注入spring对象的问题）](https://blog.csdn.net/mengruobaobao/article/details/79106343)

2.[案列](https://github.com/hell007/java-knowledge/tree/master/springboot/src/main/java/com/self/schedule)


##### 参考地址

[Timer参考地址](https://segmentfault.com/a/1190000009972187#articleHeader3)

[Quartz参考地址](https://segmentfault.com/a/1190000009972187#articleHeader4)















