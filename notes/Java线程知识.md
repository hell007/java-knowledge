
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

**总结** 

      继承Thread类，覆盖run()方法。

      创建线程对象并用start()方法启动线程
    
2. 另一种是编写一个类来实现Runnable接口，然后实现接口方法run，然后创造一个Thread对象，把实现了Runnable接口的类当做构造参数，
传入Thread对象，最后该Thread对象调用start方法。 这里的start方法是一个有启动功能的方法，该方法内部回调run方法。

```
 public class ExampleRunable  implements Runnable{

    public void run() {
        System.out.println("这是实现Runnable接口的类");
    }
  }

```



