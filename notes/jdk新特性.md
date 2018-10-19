
# jdk新特性

## JDK8新特性

  Lambda 表达式
  新的日期时间 API
  Optional
  Base64
  HashMap的改进
  接口的默认方法和静态方法
  Stream
  Lambda表
  
 
### Lambda 表达式
  
Lambda 表达式，也可称为闭包，它是推动 Java 8 发布的最重要新特性。
Lambda 允许把函数作为一个方法的参数（函数作为参数传递进方法中）。
使用 Lambda 表达式可以使代码变的更加简洁紧凑

1、什么时候能用Lambda表达式？


函数式接口：Functional Interface. 
定义的一个接口，接口里面必须 有且只有一个抽象方法 ，这样的接口就成为函数式接口。 

在可以使用lambda表达式的地方，方法声明时必须包含一个函数式的接口。 （JAVA8的接口可以有多个default方法）

任何函数式接口都可以使用lambda表达式替换。 例如：ActionListener、Comparator、Runnable

lambda表达式 **只能** 出现在目标类型为函数式接口的上下文中。






























