
## Java10新特性

  局部变量推断
  整个JDK代码仓库
  统一的垃圾回收接口
  并行垃圾回收器G1
  线程局部管控
  
  
### 局部变量推断

它向 Java 中引入在其他语言中很常见的  var ，比如 JavaScript 。
只要编译器可以推断此种类型，你不再需要专门声明一个局部变量的类型。

开发者将能够声明变量而不必指定关联的类型。比如：

    List <String> list = new ArrayList <String>();
    Stream <String> stream = getStream();

它可以简化为：

    var list = new ArrayList();
    var stream = getStream();
  
局部变量类型推断将引入“ var ”关键字的使用，而不是要求明确指定变量的类型，我们俗称“语法糖”。

这就消除了我们之前必须执行的 ArrayList<String> 类型定义的重复。
  
  
- 局部变量类型推荐仅限于如下使用场景：

      局部变量初始化
      for循环内部索引变量
      传统的for循环声明变量

Java官方表示，它不能用于以下几个地方：

    方法参数
    构造函数参数
    方法返回类型
    字段
    捕获表达式（或任何其他类型的变量声明）

**注意**： Java的var和JavaScript的完全不同，不要这样去类比。
Java的var是用于局部类型推断的，而不是JS那样的动态类型，所以下面这个样子是不行的：

    var a = 10;
    a = "abc"; //error!
    
其次，这个var只能用于局部变量声明，在其他地方使用都是错误的。

    class C {
        public var a = 10; //error
        public var f() { //error
            return 10;
        }
    }


### 整合 JDK 代码

  为了简化开发流程，Java 10 中会将多个代码库合并到一个代码仓库中


### 统一的垃圾回收接口


### 并行垃圾回收器 G1


### 线程局部管控








