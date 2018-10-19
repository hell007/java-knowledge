
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


- 函数式接口：Functional Interface. 

定义的一个接口，接口里面必须 有且只有一个抽象方法 ，这样的接口就成为函数式接口。 

在可以使用lambda表达式的地方，方法声明时必须包含一个函数式的接口。 （JAVA8的接口可以有多个default方法）

任何函数式接口都可以使用lambda表达式替换。 例如：ActionListener、Comparator、Runnable

lambda表达式 **只能** 出现在目标类型为函数式接口的上下文中。


2、lambda表达式有三部分组成：

  参数列表
  箭头（->）
  表达式或语句块。

- 实例：

a.ambda表达式本质上是一个匿名方法

    public int add(int x, int y) {
        return x + y;
    }

转成lambda表达式后

    (int x, int y) -> x + y;

    参数类型也可以省略

    (x, y) -> x + y; //返回两数之和 

    (x, y) -> { return x + y; } //显式指明返回值


    只有一个参数且可以被Java推断出类型，那么参数列表的括号也可以省略

    c -> { return c.size(); }


b.lambda表达式没有参数，也没有返回值

    () -> { System.out.println("Hello Lambda!"); }
  
  
c.用lambda表达式实现Runnable

Java 8之前：

    new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("Before Java8, too much code for too little to do");
      }
    }).start();

Java 8方式：

    new Thread( () -> System.out.println("In Java8, Lambda expression rocks !!") ).start();


- 总结：Lambda 表达式免去了使用匿名方法的麻烦，并且给予Java简单但是强大的函数化的编程能力



### 日期

Java 8通过发布新的Date-Time API (JSR 310)来进一步加强对日期与时间的处理

在旧版的 Java 中，日期时间 API 存在诸多问题，其中有：

- 非线程安全 − java.util.Date 是非线程安全的，所有的日期类都是可变的，这是Java日期类最大的问题之一。

- 设计很差 − Java的日期/时间类的定义并不一致，在java.util和java.sql的包中都有日期类，此外用于格式化和解析的类在java.text包中定义。java.util.Date同时包含日期和时间，而java.sql.Date仅包含日期，将其纳入java.sql包并不合理。另外这两个类都有相同的名字，这本身就是一个非常糟糕的设计。

- 时区处理麻烦 − 日期类并不提供国际化，没有时区支持，因此Java引入了java.util.Calendar和java.util.TimeZone类，但他们同样存在上述所有的问题。


Java 8 在 java.time 包下提供了很多新的 API。以下为两个比较重要的 API：

- Local(本地) − 简化了日期时间的处理，没有时区的问题。

- Zoned(时区) − 通过制定的时区处理日期时间。

新的java.time包涵盖了所有处理日期，时间，日期/时间，时区，时刻（instants），过程（during）与时钟（clock）的操作

1、LocalDate和LocalTime

LocalDate类表示一个具体的日期，但不包含具体时间，也不包含时区信息。
可以通过LocalDate的静态方法of()创建一个实例，LocalDate也包含一些方法用来获取年份，月份，天，星期几等：

实例：

    LocalDate localDate = LocalDate.of(2017, 1, 4);     // 初始化一个日期：2017-01-04
    int year = localDate.getYear();                     // 年份：2017
    Month month = localDate.getMonth();                 // 月份：JANUARY
    int dayOfMonth = localDate.getDayOfMonth();         // 月份中的第几天：4
    DayOfWeek dayOfWeek = localDate.getDayOfWeek();     // 一周的第几天：WEDNESDAY
    int length = localDate.lengthOfMonth();             // 月份的天数：31
    boolean leapYear = localDate.isLeapYear();          // 是否为闰年：false

也可以调用静态方法now()来获取当前日期：

    LocalDate now = LocalDate.now();
  
LocalTime和LocalDate类似，他们之间的区别在于LocalDate不包含具体时间，而LocalTime包含具体时间，

例如：  

    LocalTime localTime = LocalTime.of(17, 23, 52);     // 初始化一个时间：17:23:52
    int hour = localTime.getHour();                     // 时：17
    int minute = localTime.getMinute();                 // 分：23
    int second = localTime.getSecond();                 // 秒：52
  
LocalDateTime

LocalDateTime类是LocalDate和LocalTime的结合体，可以通过of()方法直接创建，

也可以调用LocalDate的atTime()方法或LocalTime的atDate()方法将LocalDate或LocalTime合并成一个LocalDateTime：

    LocalDateTime ldt1 = LocalDateTime.of(2017, Month.JANUARY, 4, 17, 23, 52);
		System.out.println("ldt1===="+ldt1);
		
		LocalDate localDate = LocalDate.of(2017, Month.JANUARY, 4);
		LocalTime localTime = LocalTime.of(17, 23, 52);
		LocalDateTime ldt2 = localDate.atTime(localTime);
		System.out.println("ldt2===="+ldt2);

		LocalDate date = ldt1.toLocalDate();
		LocalTime time = ldt1.toLocalTime();
		
		System.out.println("date===="+date);
		System.out.println("time===="+time);

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  















