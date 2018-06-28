

## Java多线程基础—对象及变量并发访问 ---> 线程安全

在开发多线程程序时，如果每个多线程处理的事情都不一样，每个线程都互不相关，这样开发的过程就非常轻松。
但是很多时候，多线程程序是需要同时访问同一个对象，或者变量的。这样，一个对象同时被多个线程访问，会出现处理的结果和预期不一致的可能。

因此，需要了解如何对对象及变量并发访问，写出线程安全的程序，所谓线程安全就是处理的对象及变量的时候是同步处理的，
在处理的时候其他线程是不会干扰

```

1. 对于方法的同步处理
2. 对于语句块的同步处理
3. 对类加锁的同步处理
4. 保证可见性的关键字——volatile

```

#### 对于方法的同步处理

对于一个对象的方法，如果有两个线程同时访问，如果不加控制，访问的结果会出乎意料。

所以我们需要对方法进行同步处理，让一个线程先访问，等访问结束，在让另一个线程去访问。

对于要处理的方法，用**synchronized修饰该方法**。我们下面看一下对比的例子。 首先是没有同步修饰的方法，看看会有什么意料之外的事情

```

public class HasSelfPrivateNum {
    private int num = 0;
    public void addI(String username){
        try{
            if (username.equals("a")){
                num = 100;
                System.out.println("a set over!");
                Thread.sleep(2000);
            }else {
                num = 200;
                System.out.println("b set over!");
            }
            System.out.println(username + "  num=" + num);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class SelfPrivateThreadA  extends Thread{
    private HasSelfPrivateNum num;
    public SelfPrivateThreadA(HasSelfPrivateNum num){
        this.num = num;
    }
    @Override
    public void run() {
        super.run();
        num.addI("a");
    }
}

public class SelfPrivateThreadB extends Thread{
    private HasSelfPrivateNum num;
    public SelfPrivateThreadB(HasSelfPrivateNum num){
        this.num = num;
    }
    @Override
    public void run() {
        super.run();
        num.addI("b");
    }
}

```

测试的方法如下：

```

public class HasSelfPrivateNumTest extends TestCase {
    public void testAddI() throws Exception {
        HasSelfPrivateNum numA = new HasSelfPrivateNum();
        SelfPrivateThreadA threadA = new SelfPrivateThreadA(numA);
        threadA.start();
        SelfPrivateThreadB threadB = new SelfPrivateThreadB(numA);
        threadB.start();

        Thread.sleep(1000 * 3);
    }

}

```

在这个对象中，有一个成员变量num, 如果username是a,则num应该等于100，如果是b，则num应该等于200，

threadA与threadB同时去访问addI方法，预期的结果应该是a num=100 b num=200。但是实际的结果如下：

```
a set over!
b set over!
b  num=200
a  num=200

```

这是为什么呢？因为threadA先调用addI方法，但是因为传入的参数的是a,所示ThreadA线程休眠2s,

这时B线程也已经调用了addI方法，然后将num的值改为了200,这是输出语句输出的是b改之后的num的值也就是200，a的值被b再次修改覆盖了。 

这个方法是线程不安全的


给方法添加 **synchronized** ，修改如下：

```

 synchronized public void addI(String username){
        try{
            if (username.equals("a")){
                num = 100;
                System.out.println("a set over!");
                Thread.sleep(2000);
            }else {
                num = 200;
                System.out.println("b set over!");
            }
            System.out.println(username + "  num=" + num);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
```

其他地方保持不变，现在我们在看一下，结果：

```

a set over!
a  num=100
b set over!
b  num=200

```












