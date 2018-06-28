
## Java线程知识

#### 概念

线程：进程中负责程序执行的执行单元。一个进程中至少有一个线程。

多线程：解决多任务同时执行的需求，合理使用CPU资源。多线程的运行是根据CPU切换完成，如何切换由CPU决定，因此多线程运行具有不确定性。

线程池：基本思想还是一种对象池的思想，开辟一块内存空间，里面存放了众多(未死亡)的线程，池中线程执行调度由池管理器来处理。
当有线程任务时，从池中取一个，执行完成后线程对象归池，这样可以避免反复创建线程对象所带来的性能开销，节省了系统的资源


#### 创建线程的两种方式


1. 一种是编写一个类来继承Thread,然后覆写run方法，然后调用start方法来启动线程。这时这个类就会以另一个线程的方式来运行run方法里面的代码

```
public class ExampleThread extends Thread{

    @Override
    public void run() {
        super.run();
        System.out.println("这是一个继承自Thread的ExampleThread");
    }
}

ExampleThread t = new ExampleThread();
t.start();

```
    
2. 另一种是编写一个类来实现Runnable接口，然后实现接口方法run，然后创造一个Thread对象，把实现了Runnable接口的类当做构造参数，
传入Thread对象，最后该Thread对象调用start方法。 这里的start方法是一个有启动功能的方法，该方法内部回调run方法。

```
 public class ExampleRunable  implements Runnable{

    public void run() {
        System.out.println("这是实现Runnable接口的类");
    }
  }

```

#### 从线程中（Thread）可以得到的一些信息

线程的名字 —— getName()

线程的ID —— getId()

线程是否存活 —— isAlive()


**注意**： 这些方法是属于Thread的内部方法，所以我们可以用两种方式调用这些方法，一个是我们的类继承Thread来使用多线程的时候，可以用过this来调用。
另一种是通过Thread.currentThread() 来调用这些方法。但是这两个方法在不同的使用场景下是有区别的。

```

public class ExampleCurrentThread extends Thread{

    public ExampleCurrentThread(){
        System.out.println("构造方法的打印：" + Thread.currentThread().getName());
        
        //System.out.println("this.getName()=" + this.getName());
    }

    @Override
    public void run() {
        super.run();
        System.out.println("run方法的打印：" + Thread.currentThread().getName());
        
        //System.out.println("this.getName()=" + this.getName());
    }
}


测试的代码如下:

public class ExampleCurrentThreadTest extends TestCase {

    public void testInit() throws Exception{
        ExampleCurrentThread thread = new ExampleCurrentThread();
    }

    public void testRun() throws Exception {
        ExampleCurrentThread thread = new ExampleCurrentThread();
        
        //thread.setName("byhieg");
        
        thread.start();
        Thread.sleep(1000);
    }
}

```


1. 首先在创建对象的时候，构造器还是被main线程所执行，所以Thread.currentThread()得到的就是Main线程的名字，

但是this方法指的是调用方法的那个对象，也就是ComplexCurrentThread的线程信息，还没有setName,所以是默认的名字。

然后run方法无论是Thread.currentThread()还是this返回的都是设置了byhieg名字的线程信息。 

所以Thread.currentThread指的是具体执行这个代码块的线程信息。构造器是main执行的，而run方法则是哪个线程start，哪个线程执行run。

这么看来，this能得到的信息是不准确的，因为如果我们在run中执行了this.getName()，但是run方法却是由另一个线程start的，
我们是无法通过this.getName得到运行run方法的新城的信息的。

而且只有继承了Thread的类才能有getName等方法，这对于Java没有多继承的特性语言来说，是个灾难。所有后面凡是要得到线程的信息，我们都用**Thread.currentThread()来调用API**

2. 方法isAlive()的作用是测试线程是否处于活动状态。所谓活动状态，就是线程已经启动但是没有终止。即该线程start之后，被认为是存活的

```

public class AliveThread extends Thread{

    @Override
    public void run() {
        super.run();
        System.out.println("run方法中是否存活" + "   "  + Thread.currentThread().isAlive());
    }
}
测试方法如下：

public class AliveThreadTest extends TestCase {
    public void testRun() throws Exception {
        AliveThread thread = new AliveThread();
        System.out.println("begin == " + thread.isAlive());
        thread.start();
        Thread.sleep(1000);
        System.out.println("end ==" + thread.isAlive());

        Thread.sleep(3000);
    }
}

结果如下：

begin == false
run方法中是否存活   true
end ==false

```

我们可以发现在start之前，该线程被认为是没有存活，然后run的时候，是存活的，等run方法执行完，又被认为是不存活的。


#### 如何停止线程


##### 判断线程是否终止

JDK提供了一些方法来判断线程是否终止 —— isInterrupted()和interrupted()


##### 停止线程的方式

这个是得到线程信息中比较重要的一个方法了，因为这个和终止线程的方法相关联。先说一下终止线程的几种方式：

1. 等待run方法执行完

2. 线程对象调用stop() (该方法已经废弃了)

```
强制结束线程，该线程应该做的清理工作，无法完成。

强制结束线程，该线程已操作的加锁对象强制解锁，造成数据不一致。 具体的例子如下：

public class StopLockThread extends Thread{

    private SynchronizedObject object;

    public StopLockThread(SynchronizedObject object){
        this.object = object;
    }


    @Override
    public void run() {
        super.run();
        object.printString("b","bbb");
    }
}


```

3. 线程对象调用interrupt(),在该线程的run方法中判断是否终止，抛出一个终止异常终止。

**推荐的终止方法**

调用interrupt,然后在run方法中判断是否终止。

判断终止的方式有两种，一种是Thread类的静态方法interrupted(),另一种是Thread的成员方法isInterrupted()。

这两个方法是有所区别的，第一个方法是会自动重置状态的，如果连续两次调用interrupted()，第一次如果是false,第二次一定是true。
而isInterrupted()是不会的

```
public class ExampleInterruptThread extends Thread{

    @Override
    public void run() {
        super.run();
        try{
            for(int i = 0 ; i < 50000000 ; i++){
                if (interrupted()){//here
                    System.out.println("已经是停止状态，我要退出了");
                    throw new InterruptedException("停止.......");
                }
                System.out.println("i=" + (i + 1));
            }
        }catch (InterruptedException e){
            System.out.println("顺利停止");
        }


    }
}

测试的代码如下：

public class ExampleInterruptThreadTest extends TestCase {
    public void testRun() throws Exception {
        ExampleInterruptThread thread = new ExampleInterruptThread();
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();//here
    }
}

```

注意一点，我们上面抛出的异常是InterruptedException，这里简单说一下可能产生这个异常的原因，在原有线程sleep的情况下，调用interrupt终止线程，或者先终止线程，再让线程sleep

4. 线程对象调用interrupt(),在该线程的run方法中判断是否终止，以return语句结束。


第四种方法和第三种一样，唯一的区别就是将上面的代码中的抛出异常换成return



#### 如何暂停线程

在JDK中提供了以下两个方法用来暂停线程和恢复线程。

```
suspend()——暂停线程
resume()——恢复线程

```

这两个方法和stop方法一样是被废弃的方法，其用法和stop一样，暴力的暂停线程和恢复线程。这两个方法之所以是废弃的主要由以下两个原因：

线程持有锁定的公共资源的情况下，一旦被暂停，则公共资源无法被其他线程所持有。

线程强制暂停，导致该线程执行的操作没有执行完全，这时访问该线程的数据会出现数据不一致。


#### 线程的一些其他用法


线程的其他的一些基础用法如下：

```

线程让步

设置线程的优先级

守护线程

```

##### 线程让步

JDK提供yield()方法来让线程放弃当前的CPU资源，将它让给其他的任务去占用CPU时间，但是这也是随机的事情，有可能刚放弃资源，又马上占用时间片了。 

具体的例子

```
public class ExampleYieldThread extends Thread{

    @Override
    public void run() {
        super.run();
        long beginTime = System.currentTimeMillis();
        int count = 0;
        for (int i = 0 ; i< 500000;i++){
            Thread.yield();
            count = count + (i + 1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(" 用时：" + (endTime - beginTime) + "毫秒！");
    }
}

```


##### 设置线程的优先级

我们可以设置线程的优先级来让CPU尽可能的将执行的资源给优先级高的线程。Java设置了1-10这10个优先级，又有三个静态变量来提供三个优先级

```
/**
 * The minimum priority that a thread can have.
 */
public final static int MIN_PRIORITY = 1;

/**
 * The default priority that is assigned to a thread.
 */
public final static int NORM_PRIORITY = 5;

/**
 * The maximum priority that a thread can have.
 */
public final static int MAX_PRIORITY = 10;
    
  ```

我们可以通过setPriority来设置线程的优先级，可以直接传入上诉三个静态变量，也可以直接传入1-10的数字。设置后线程就会有不同的优先级。

如果我们不设置优先级，会是什么情况？ 线程的优先级是有继承的特性，如果我们在A线程中启动了B线程，则AB具有相同的优先级。一般我们在main线程中启动线程，就和main线程有一致的优先级。main线程的优先级默认是5。

下面说一下优先级的一些规则：

优先级高的线程一般会比优先级低的线程获得更多的CPU资源，但是不代表优先级高的任务一定先于优先级低的任务先执行完。
因为不同优先级的线程中run方法内容可能不一样。

优先级高的线程一定会比优先级低的线程执行的快。如果两个线程是一样的run方法，但是优先级不一样，确实优先级高的线程先执行完

```
public class ExamplePriorityThread extends Thread{

    private int count = 0;
    @Override
    public void run() {
        super.run();
        while (true){
            count++;
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

```


##### 线程守护

JDK中提供setDaemon的方法来设置一个线程变成守护线程。

守护线程的特点是其他非守护线程执行完，守护线程就自动销毁，典型的例子是GC回收器。 

具体列子

```
public class ExampleDaemonThread extends Thread{

    private int i = 0;

    @Override
    public void run() {
        super.run();
        try{
            while (true){
                i++;
                System.out.println("i=" + (i));
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

```

















