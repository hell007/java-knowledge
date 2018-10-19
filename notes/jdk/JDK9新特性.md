
## JDK9新特性

  模块系统
  JShell：交互式Java REPL
  不可变集合类工厂方法
  接口中的私有方法
  进程API改进
  try-with-resources改进
  HTTP2支持
  平台日志API和服务
  垃圾回收器
  增加加密算法


### 模块系统

Java 9 新特性中最大的一个变化就是 Module System

简单 Module 示例

module com.foo.bar { }

这里我们使用 module 关键字创建了一个简单的 module。

每个 module 都有一个名字，对应代码和其它一些资源


### JShell: 交互式 Java REPL

REPL是一种快速运行语句的命令行工具。

类似Python IDLE、windows cmd

  G:\>jshell
  |  Welcome to JShell -- Version 9-ea
  |  For an introduction type: /help intro
  jshell> int a = 10
  a ==> 10
  jshell> System.out.println("a value = " + a )
  a value = 10


### 不可变集合类的工厂方法

Oracle 公司引入一些方便使用的工厂方法，用于创建不可变集合 List，Set，Map 和 Map.Entry 对象。
这些高效实用的方法可用来创建空或者非空集合对象。

空 List 示例

    List immutableList = List.of();

非空 List 示例

    List immutableList = List.of("one","two","three");


### 接口中的私有方法

从 Java 9 开始，我们也能够在接口类中使用 ‘private’ 关键字写私有化方法和私有化静态方法。

接口中的私有方法与 class 类中的私有方法在写法上并无差异,如：

    public interface Card{
      private Long createCardID(){
        // Method implementation goes here.
      }
      private static void displayCardDetails(){
        // Method implementation goes here.
      }
    }


### 进程API的改进

Java 9 迎来一些 Process API 的改进，通过添加一些新的类和方法来优化系统级进程的管控。

Process API 中的两个新接口：

    java.lang.ProcessHandle
    java.lang.ProcessHandle.Info
  
Process API 示例

    ProcessHandle currentProcess = ProcessHandle.current();
    System.out.println("Current Process Id: = " + currentProcess.getPid());


### Try-With-Resources 改进

我们知道，Java 7 引入了一个新的异常处理结构：Try-With-Resources，来自动管理资源。
这个新的声明结构主要目的是实现“Automatic Better Resource Management”（“自动资源管理”）。

Java 9 将对这个声明作出一些改进来避免一些冗长写法，同时提高可读性


### HTTP2支持

在 Java 9 中，Oracle 公司将发布新的 HTTP 2 Client API 来支持 HTTP/2 协议和 WebSocket 特性

    jshell> import java.net.http.*
    jshell> import static java.net.http.HttpRequest.*
    jshell> import static java.net.http.HttpResponse.*
    jshell> URI uri = new URI("http://rams4java.blogspot.co.uk/2016/05/java-news.html")
    uri ==> http://rams4java.blogspot.co.uk/2016/05/java-news.html
    jshell> HttpResponse response = HttpRequest.create(uri).body(noBody()).GET().response()
    response ==> java.net.http.HttpResponseImpl@79efed2d
    jshell> System.out.println("Response was " + response.body(asString()))


### 平台日志 API 和 服务

Java 9 中 ，JVM 有了统一的日志记录系统，可以使用新的命令行选项-Xlog 来控制 JVM 上 所有组件的日志记录。
该日志记录系统可以设置输出的日志消息的标签、级别、修饰符和输出目标等。

Java 9 允许为 JDK 和应用配置同样的日志实现。

新增的 System.LoggerFinder 用来管理 JDK 使 用的日志记录器实现。

JVM 在运行时只有一个系统范围的 LoggerFinder 实例。LoggerFinder 通 过服务查找机制来加载日志记录器实现。

默认情况下，JDK 使用 java.logging 模块中的 java.util.logging 实现。
通过 LoggerFinder 的 getLogger()方法就可以获取到表示日志记录器的 System.Logger 实现。
应用同样可以使用 System.Logger 来记录日志。这样就保证了 JDK 和应用使用同样的日志实现。
我们也可以通过添加自己的 System.LoggerFinder 实现来让 JDK 和应用使用 SLF4J 等其他日志记录框架。

代码清单 9 中给出了平台日志 API 的使用示例。

    private static final System.Logger LOGGER = System.getLogger("Main");
    public static void main(final String[] args) {
        LOGGER.log(Level.INFO, "Run!");
    }


### 垃圾回收器

JDK1.8 默认垃圾收集器是Parallel

Java 9 移除了在 Java 8 中 被废弃的垃圾回收器配置组合，同时 把 G1 设为默认的垃圾回收器实现。
另外，CMS 垃圾回收器已经被声明为废弃。


### 增加加密算法

Java 9 新增了 4 个 SHA- 3 哈希算法，SHA3-224、SHA3-256、SHA3-384 和 S HA3-512。

另外也增加了通过 java.security.SecureRandom 生成使用 DRBG 算法的强随机数。 

代码清单 13 中给出了 SHA-3 哈希算法的使用示例:

    public static void main(final String[] args) throws NoSuchAlgorithmException {
        final MessageDigest instance = MessageDigest.getInstance("SHA3-224");
        final byte[] digest = instance.digest("".getBytes());
        System.out.println(Hex.encodeHexString(digest));
    }






















