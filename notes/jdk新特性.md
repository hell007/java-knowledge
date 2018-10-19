
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


2、Instant 

Instant用于表示一个时间戳，它与我们常使用的System.currentTimeMillis()有些类似，不过Instant可以精确到纳秒（Nano-Second），System.currentTimeMillis()方法只精确到毫秒（Milli-Second）

Instant除了使用now()方法创建外，还可以通过ofEpochSecond方法创建：
  
	Instant instant = Instant.ofEpochSecond(120, 100000);
  
ofEpochSecond()方法的第一个参数为秒，第二个参数为纳秒，上面的代码表示从1970-01-01 00:00:00开始后两分钟的10万纳秒的时刻，
控制台上的输出为： 
  
  	instant====1970-01-01 T 00:02:00.000100Z
  
3、Duration
 
Duration的内部实现与Instant类似，也是包含两部分：seconds表示秒，nanos表示纳秒。
 
两者的区别是Instant用于表示一个时间戳（或者说是一个时间点），而Duration表示一个时间段，所以Duration类中不包含now()静态方法。
可以通过Duration.between()方法创建Duration对象：

	LocalDateTime from = LocalDateTime.of(2017, Month.JANUARY, 5, 10, 7, 0);    // 2017-01-05 10:07:00
	LocalDateTime to = LocalDateTime.of(2017, Month.FEBRUARY, 5, 10, 7, 0);     // 2017-02-05 10:07:00
	Duration duration = Duration.between(from, to);     // 表示从 2017-01-05 10:07:00 到 2017-02-05 10:07:00 这段时间
	long days = duration.toDays();              // 这段时间的总天数
	long hours = duration.toHours();            // 这段时间的小时数
	long minutes = duration.toMinutes();        // 这段时间的分钟数
	long seconds = duration.getSeconds();       // 这段时间的秒数
	long milliSeconds = duration.toMillis();    // 这段时间的毫秒数
	long nanoSeconds = duration.toNanos();      // 这段时间的纳秒数
	Duration对象还可以通过of()方法创建，该方法接受一个时间段长度，和一个时间单位作为参数：
	Duration duration1 = Duration.of(5, ChronoUnit.DAYS);       // 5天
	Duration duration2 = Duration.of(1000, ChronoUnit.MILLIS);  // 1000毫秒


4、Period

Period在概念上和Duration类似，区别在于Period是以年月日来衡量一个时间段，比如2年3个月6天：
  
	Period period = Period.of(2, 3, 6);

Period对象也可以通过between()方法创建，值得注意的是，由于Period是以年月日衡量时间段，
所以between()方法只能接收LocalDate类型的参数： 
  
  
5、日期的操作和格式化

- 增加和减少日期：

		LocalDate date = LocalDate.of(2017, 1, 5); // 2017-01-05
		LocalDate date1 = date.withYear(2016); // 修改为 2016-01-05
		LocalDate date2 = date.withMonth(2);// 修改为 2017-02-05
		LocalDate date3 = date.withDayOfMonth(1); // 修改为 2017-01-01
		LocalDate date4 = date.plusYears(1);// 增加一年 2018-01-05
		LocalDate date5 = date.minusMonths(2);// 减少两个月 2016-11-05
		LocalDate date6 = date.plus(5, ChronoUnit.DAYS);// 增加5天 2017-01-10
  
- 格式化日期：

新的日期API中提供了一个DateTimeFormatter类用于处理日期格式化操作，它被包含在java.time.format包中，
Java 8的日期类有一个format()方法用于将日期格式化为字符串，该方法接收一个DateTimeFormatter类型参数：

	LocalDateTime dateTime = LocalDateTime.now();
	String strDate1 = dateTime.format(DateTimeFormatter.BASIC_ISO_DATE);// 20170105
	String strDate2 = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);// 2017-01-05
	String strDate3 = dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME);// 14:20:16.998
	String strDate4 = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));// 2017-01-05 10:25:43

		
同样，日期类也支持将一个字符串解析成一个日期对象，例如：

	String strDate6 = "2017-01-05";
	String strDate7 = "2017-01-05 12:30:05";

	LocalDate date = LocalDate.parse(strDate6, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	LocalDateTime dateTime1 = LocalDateTime.parse(strDate7, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


6、时区

Java 8中的时区操作被很大程度上简化了，新的时区类java.time.ZoneId是原有的java.util.TimeZone类的替代品。
ZoneId对象可以通过ZoneId.of()方法创建，也可以通过ZoneId.systemDefault()获取系统默认时区：
	
	ZoneId shanghaiZoneId = ZoneId.of("Asia/Shanghai");
	ZoneId systemZoneId = ZoneId.systemDefault();

of()方法接收一个“区域/城市”的字符串作为参数，你可以通过getAvailableZoneIds()方法获取所有合法的“区域/城市”字符串：
  
  	Set<String> zoneIds = ZoneId.getAvailableZoneIds();
  
对于老的时区类TimeZone，Java 8也提供了转化方法：

	ZoneId oldToNewZoneId = TimeZone.getDefault().toZoneId();
  
  
有了ZoneId，我们就可以将一个LocalDate、LocalTime或LocalDateTime对象转化为ZonedDateTime对象：
  
	ZoneId shanghaiZoneId = ZoneId.of("Asia/Shanghai");

	LocalDateTime localDateTime = LocalDateTime.now();
	ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, shanghaiZoneId);
  
  
  
  
  
  
  
  
  
  
  
## 参考文档

  [日期](https://blog.csdn.net/CrankZ/article/details/81222507)















