
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
  
局部变量类型推断将引入“ var ”关键字
  
