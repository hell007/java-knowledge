
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
  
  
### Optional

Optional 类是一个可以为null的容器对象。如果值存在则isPresent()方法会返回true，调用get()方法会返回该对象

Optional 类的引入很好的解决空指针异常。

实例:

	public static Integer sum(Optional<Integer>a, Optional<Integer>b) {
	    // 判断值是否存在
	    System.out.println("第一个参数值存在:" + a.isPresent());
	    System.out.println("第二个参数值存在:" + b.isPresent());
	    // 如果值存在，返回它，否则返回默认值
	    Integer value1 = a.orElse(new Integer(0));
	    // 获取值，值需要存在
	    Integer value2 = b.get();
	    
	    System.out.println(value1+"===="+value2);
	    return value1 + value2;
	}


	/**
	 * main
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		
		Integer value1 = null;
	    Integer value2 = new Integer(10);
	    // 允许传递为null参数
	    Optional<Integer> a = Optional.ofNullable(value1);
	    // 如果传递的参数是null，抛出异常NullPointerException
	    Optional<Integer> b = Optional.of(value2);
	    System.out.println(sum(a, b));
	
	}
  
 结果：
 
	第一个参数值存在:false
	第二个参数值存在:true
	0====10
	10
  
  
### Base64

在Java 8中，Base64编码已经成为Java类库的标准。

Java 8 内置了 Base64 编码的编码器和解码器。

Base64工具类提供了一套静态方法获取下面三种BASE64编解码器：

- 基本：输出被映射到一组字符A-Za-z0-9+/，编码不添加任何行标，输出的解码仅支持A-Za-z0-9+/。

- URL：输出映射到一组字符A-Za-z0-9+_，输出是URL和文件。

- MIME：输出隐射到MIME友好格式。输出每行不超过76字符，并且使用'\r'并跟随'\n'作为分割。编码输出最后没有行分割。


实例：

	public static void base64Test() throws UnsupportedEncodingException {
	    // 使用基本编码
	    String base64encodedString = Base64.getEncoder().encodeToString("测试文字".getBytes("utf-8"));
	    System.out.println("Base64 编码字符串 (基本) :" + base64encodedString);
	    // 解码
	    byte[] base64decodedBytes = Base64.getDecoder().decode(base64encodedString);
	    System.out.println("原始字符串: " + new String(base64decodedBytes, "utf-8"));
	}
  
  
### HashMap的改进  
  
  
  
### 接口的默认方法和静态方法  
  
Java 8 新增了接口的默认方法。

简单说，默认方法就是接口可以有实现方法，而且不需要实现类去实现其方法。我们只需在方法名前面加个default关键字即可实现默认方法。


1、为什么要有这个特性？

以前当需要修改接口时候，需要修改全部实现该接口的类。所以引进的默认方法。他们的目的是为了解决接口的修改与现有的实现不兼容的问题。

a.默认方法语法格式如下：

	public interface Vehicle {
	   default void print(){
	      System.out.println("我是一辆车!");
	   }
	}

b.多个默认方法

一个接口有默认方法，考虑这样的情况，一个类实现了多个接口，且这些接口有相同的默认方法，以下实例说明了这种情况的解决方法

	public interface Vehicle {
	   default void print(){
	      System.out.println("我是一辆车!");
	   }
	}
	public interface FourWheeler {
	   default void print(){
	      System.out.println("我是一辆四轮车!");
	   }
	}

第一个解决方案是创建自己的默认方法，来覆盖重写接口的默认方法：

	public class Car implements Vehicle, FourWheeler {
	   default void print(){
	      System.out.println("我是一辆四轮汽车!");
	   }
	}

第二种解决方案可以使用 super 来调用指定接口的默认方法：

	public class Car implements Vehicle, FourWheeler {
	   public void print(){
	      Vehicle.super.print();
	   }
	}


c.静态默认方法

Java 8 的另一个特性是接口可以声明（并且可以提供实现）静态方法。例如

	public interface Vehicle {
	   default void print(){
	      System.out.println("我是一辆车!");
	   }
	   // 静态方法
	   static void blowHorn(){
	      System.out.println("按喇叭!!!");
	   }
	}


### Stream

Java 8 API添加了一个新的抽象称为流Stream，可以让你以一种声明的方式处理数据。

Stream 使用一种类似用 SQL 语句从数据库查询数据的直观方式来提供一种对 Java 集合运算和表达的高阶抽象。

这种风格将要处理的元素集合看作一种流， 流在管道中传输， 并且可以在管道的节点上进行处理， 比如筛选， 排序，聚合等。

所以这边有两个概念

- 流、管道

Stream API可以提高Java程序员的生产力，让程序员写出高效率、干净、简洁的代码。

元素流在管道中经过中间操作（intermediate operation）的处理，最后由最终操作(terminal operation)得到前面处理的结果。

这里有两个操作

- 中间操作、最终操作


1、在 Java 8 中, 集合接口有两个方法来生成流：

- stream()：为集合创建串行流。

- parallelStream()：为集合创建并行流


2、forEach

Stream 提供了新的方法 'forEach' 来迭代流中的每个数据。以下代码片段使用 forEach 输出了10个随机数：


3.中间操作

map（映射）: map 方法用于映射每个元素到对应的结果

	List<Integer> numbers = Arrays.asList(3, 2, 7, 5);
	
	// 获取对应的平方数
	List<Integer> squaresList = numbers.stream()
	    .map(i -> i * i)
	    .distinct()
	    .collect(Collectors.toList());

	squaresList.stream().forEach(System.out::println);


Filter（过滤）: filter 方法用于通过设置的条件过滤出元素

	List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
	// 获取空字符串的数量
	long count = strings.stream().filter(str -> str.isEmpty()).count();
	System.out.println(count);

	//查找a打头的元素
	strings.stream()
	.filter(str -> str.contains("a"))
	.forEach(System.out::println);


Limit（限制）: limit 方法用于获取指定数量的流

	List<Integer> numbers = Arrays.asList(3, 2, 7, 5, 4, 6, 9);
	numbers.stream().limit(4).forEach(System.out::println);

Sorted（排序）: sorted 方法用于对流进行排序

	List<Integer> numbers = Arrays.asList(3, 2, 7, 5, 4, 6, 9);
	numbers.stream().sorted().forEach(System.out::println);


4、最终操作

Match（匹配）: 用来判断某个predicate是否和流对象相匹配，最终返回Boolean类型结果

	List<String> list = new ArrayList<String>();
	list.add("a1");
	list.add("b1");

	// 流对象中只要有一个元素匹配就返回true
	boolean anyStartWithA = list.stream().anyMatch((s -> s.startsWith("a")));
	System.out.println(anyStartWithA);

	// 流对象中每个元素都匹配,全都符合条件 就返回true
	boolean allStartWithA = list.stream().allMatch((s -> s.startsWith("a")));
	System.out.println(allStartWithA);
  
  
  Collectors（收集）:  Collectors 类实现了很多归约操作，例如将流转换成集合和聚合元素
  
	List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
	List<String> filtered = strings.stream()
		.filter(string -> !string.isEmpty())
		.collect(Collectors.toList());
	System.out.println("筛选列表: " + filtered);


	String mergedString = strings.stream()
		.filter(string -> !string.isEmpty())
		.collect(Collectors.joining(", ")); 
	System.out.println("合并字符串: " + mergedString);
  
  
Count（计数）: 类似sql的count，用来统计流中元素的总数  
  
	List<String> strings = Arrays.asList("a", "b", "c");
	long count = strings.stream().filter((s -> s.startsWith("a"))).count();
	System.out.println(count);

Reduce（规约）: reduce方法允许我们用自己的方式去计算元素或者将一个Stream中的元素以某种规律关联
	
	List<String> strings = Arrays.asList("a", "b", "c");
	strings.stream().sorted().reduce((s1, s2) -> {
		System.out.println(s1 + "|" + s2);
		return s1 + "|" + s2;
	});
  
parallel（并行）: parallelStream 是流并行处理程序的代替方法
  
	List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
	// 获取空字符串的数量
	long count = strings.parallelStream().filter(string -> string.isEmpty()).count();
	System.out.println(count);
  

### 并行Stream VS 串行Stream
  
并行Stream（parallel Stream）。并行Stream基于Fork-join并行分解框架实现，
将大数据集合切分为多个小数据结合交给不同的线程去处理，这样在多核处理情况下，性能会得到很大的提高。
这和MapReduce的设计理念一致：大任务化小，小任务再分配到不同的机器执行。只不过这里的小任务是交给不同的处理器。

如果你现在还是单核处理器，而数据量又不算很大的情况下，串行流仍然是这种不错的选择

  
  
 
  
  
  
  
## 参考文档

  [日期](https://blog.csdn.net/CrankZ/article/details/81222507)















