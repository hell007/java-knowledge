
## Java集合框架

1、概念

Java中类集框架：在Java中类集框架实际上也就是对数据结构的Java实现。集合框架是为表示和操作集合而规定的一种统一的标准的体系结构。
任何集合框架都包含三大块内容：对外的接口、接口的实现和对集合运算的算法。

接口：即表示集合的抽象数据类型。接口提供了让我们对集合中所表示的内容进行单独操作的可能。

实现：也就是集合框架中接口的具体实现。实际它们就是那些可复用的数据结构。

算法：在一个实现了某个集合框架中的接口的对象身上完成某种有用的计算的方法，例如查找、排序等。
这些算法通常是多态的，因为相同的方法可以在同一个接口被多个类实现时有不同的表现。



Java集合主要有两个接口派生而出：Collection(List，Set，Queue)，Map，这个两个接口是Java集合框架的根接口。

![](./images/java-Collection.png)

其中HashSet、TreeSet、ArrayList、LinkedList是经常用到的实现类。

![](./images/java-Map.png)

Map实现类是用于保存具有映射关系的数据。Map保存的每项数据都是键值对（key-value），Map中的key是不可重复的，key用于标识集合里的每项数据。


2、使用集合框架的好处？

1）它减少了程序设计的辛劳

集合框架通过提供有用的数据结构和算法使你能集中注意力于你的程序的重要部分上，而不是为了让程序能正常运转而将注意力于低层设计上。通过这些在无关API之间的简易的互用性，使你免除了为改编对象或转换代码以便联合这些API而去写大量的代码。

2）它提高了程序速度和质量

集合框架通过提供对有用的数据结构和算法的高性能和高质量的实现使你的程序速度和质量得到提高。因为每个接口的实现是可互换的，所以你的程序可以很容易的通过改变一个实现而进行调整。另外，你将可以从写你自己的数据结构的苦差事中解脱出来，从而有更多时间关注于程序其它部分的质量和性能。

3）减少去学习和使用新的API 的辛劳

许多API天生的有对集合的存储和获取。在过去，这样的API都有一些子API帮助操纵它的集合内容，因此在那些特殊的子API之间就会缺乏一致性，你也不得不从零开始学习，并且在使用时也很容易犯错。而标准集合框架接口的出现使这个问题迎刃而解。

4）减少了设计新API的努力

设计者和实现者不用再在每次创建一种依赖于集合内容的API时重新设计，他们只要使用标准集合框架的接口即可。

5）集合框架鼓励软件的复用

对于遵照标准集合框架接口的新的数据结构天生即是可复用的。同样对于操作一个实现了这些接口的对象的算法也是如此。


## List

List里存放的对象是有序的，同时也是可以重复的，List关注的是索引，拥有一系列和索引相关的方法，查询速度快。
因为往list集合里插入或删除数据时，会伴随着后面数据的移动，所有插入删除数据速度慢。

- ArrayList

ArrayList：一个用数组实现的List。能进行快速的随机访问，但是往列表中间插入和删除元素的时候比较慢。


- LinkedList

LinkedList：内部实现是链表，它适合于在链表中间需要频繁进行插入和删除操作。

LinkedList对顺序访问进行了优化。在List中间插入和删除元素的代价也不高。随机访问的速度相对较慢。
此外它还有addFirst()，addLast()，getFirst()，getLast()，removeFirst()和removeLast()等方法，
你能把它当成栈（stack），队列（queue）或双向队列（deque）来用。

LinkedList的用途：

    1）用LinkedList做一个栈

    “栈（stack）”有时也被称为“后进先出”（LIFO）的容器。就是说，最后一个被“压”进栈中的东西，会第一个“弹”出来。
    LinkedList的方法能直接实现栈的功能，所以你完全可以不写Stack而直接使用LinkedList。

    2）用LinkedList做一个队列

    队列（queue）是一个“先进先出”（FIFO）容器。也就是，你把一端把东西放进去，从另一端把东西取出来。
    所以你放东西的顺序也就是取东西的顺序。LinkedList有支持队列的功能的方法，所以它也能被当作Queue来用。

    3）用LinkedList做一个deque（双向队列）

    它很像队列，只是你可以从任意一端添加和删除元素


- Vector (jdk7)

Vector 类可以实现可增长的对象数组。与数组一样，它包含可以使用整数索引进行访问的组件。
但是，Vector 的大小可以根据需要增大或缩小，以适应创建 Vector 后进行添加或移除项的操作。
Vector 是同步的，可用于多线程。

Vector 继承了AbstractList，实现了List；所以，它是一个队列，支持相关的添加、删除、修改、遍历等功能。

Vector实现了RandmoAccess接口，即提供了随机访问功能。RandmoAccess是java中用来被List实现，为List提供快速访问功能的。
在Vector中，我们即可以通过元素的序号快速获取元素对象；这就是快速随机访问。

Vector 实现了Cloneable接口，即实现clone()函数。它能被克隆。

Vector 实现Serializable接口，支持序列化。

- 面试题：

ArrayList、LinkedList、Vector的区别

1）List的三个子类的特点

    ArrayList:
    底层数据结构是数组，查询快，增删慢。
    线程不安全，效率高。

    Vector:
    底层数据结构是数组，查询快，增删慢。
    线程安全，效率低。

    Vector相对ArrayList查询慢(线程安全的)。
    Vector相对LinkedList增删慢(数组结构)。

    LinkedList：
    底层数据结构是链表，查询慢，增删快。
    线程不安全，效率高。

2）Vector和ArrayList的区别

    Vector是线程安全的,效率低。
    ArrayList是线程不安全的,效率高。
    共同点:底层数据结构都是数组实现的,查询快,增删慢。
    
3）ArrayList和LinkedList的区别

    ArrayList底层是数组结果,查询和修改快。
    LinkedList底层是链表结构的,增和删比较快,查询和修改比较慢。
    共同点:都是线程不安全的

4）List有三个子类使用

    查询多用ArrayList
    增删多用LinkedList
    如果都多ArrayList


## Set

Set里存放的对象是无序，不能重复的，集合中的对象不按特定的方式排序，只是简单地把对象加入集合中。

Set中最多包含一个null元素,只能用Lterator实现单项遍历，Set中没有同步方法。

- HashSet：

为优化查询速度而设计的Set，能快速定位一个元素。要放进HashSet里面的Object还得定义hashCode()。


- TreeSet：

TreeSet则将放入其中的元素按序存放，这就要求你放入其中的对象是可排序的，
这就用到了集合框架提供的另外两个实用类Comparable和Comparator。一个类是可排序的，它就应该实现Comparable接口。


- LinkedHashSet：

一个在内部使用链表的Set，既有HashSet的查询速度，又能保存元素被加进去的顺序（插入顺序）。用Iterator遍历Set的时候，它是按插入顺序进行访问的。


## Map

Map集合中存储的是键值对，键不能重复，值可以重复。根据键得到值，对map集合遍历时先得到键的set集合，对set集合进行遍历，得到相应的值。

键和值的关联很简单，用put(Objectkey,Objectvalue)方法即可将一个键与一个值对象相关联。
用get(Object key)可得到与此key对象所对应的值对象。也可以用containsKey()和containsValue()测试Map是否包含有某个键或值。


- Map接口定义了如下常用的方法：

1. `void clear()`:删除Map中所以键值对。
2. `boolean containsKey(Object key)`:查询Map中是否包含指定key，如果包含则返回true。
3. `boolean containsValue(Object value)`:查询Map中是否包含指定value，如果包含则返回true。
4. `Set entrySet()`:返回Map中所包含的键值对所组成的Set集合，每个集合元素都是Map.Entry对象(Entry是Map的内部类)。
5. `Object get(Object key)`:返回指定key所对应的value，如Map中不包含key则返回null。
6. `boolean isEmpty()`:查询Map是否为空，如果空则返回true。
7. `Set keySet()`:返回该Map中所有key所组成的set集合。
8. `Object put(Object key,Object value)`:添加一个键值对，如果已有一个相同的key值则新的键值对覆盖旧的键值对。
9. `void putAll(Map m)`:将指定Map中的键值对复制到Map中。
10. `Object remove(Object key)`:删除指定key所对应的键值对，返回可以所关联的value，如果key不存在，返回null。
11. `int size()`:返回该Map里的键值对的个数。
12. `Collection values()`:返回该Map里所有value组成的Collection。

- Map中包含一个内部类：Entry。

该类封装了一个键值对，它包含了三个方法：

1. Object getKey():返回该Entry里包含的key值。
2. Object getValeu():返回该Entry里包含的value值。
3. Object setValue(V value):设置该Entry里包含的value值，并返回新设置的value值。


- HashMap：

基于hash表的实现。（用它来代替Hashtable）提供时间恒定的插入与查询。在构造函数种可以设置hash表的capacity和load factor。可以通过构造函数来调节其性能。


- TreeMap：

基于红黑树数据结构的实现。它们是按顺序排列的。TreeMap是Map中唯一有subMap()方法的实现。这个方法能让你获取这个树中的一部分。

Map接口派生了一个SortMap子接口，SortMap的实现类为TreeMap。
TreeMap也是基于红黑树对所有的key进行排序，有两种排序 方式：自然排序和定制排序。
Treemap的key以TreeSet的形式存储，对key的要求与TreeSet对元素的要求基本一致。

1. `Map.Entry firstEntry()`:返回最小key所对应的键值对，如Map为空，则返回null。
2. `Object firstKey()`:返回最小key，如果为空，则返回null。
3. `Map.Entry lastEntry()`:返回最大key所对应的键值对，如Map为空，则返回null。
4. `Object lastKey()`:返回最大key，如果为空，则返回null。
5. `Map.Entry higherEntry(Object key)`:返回位于key后一位的键值对，如果为空，则返回null。
6. `Map.Entry lowerEntry(Object key)`:返回位于key前一位的键值对，如果为空，则返回null。
7. `Object lowerKey(Object key)`:返回位于key前一位key值，如果为空，则返回null。
8. `NavigableMap subMap(Object fromKey,boolean fromlnclusive,Object toKey,boolean toInciusive)`:返回该Map的子Map，其key范围从fromKey到toKey。
9. `SortMap subMap(Object fromKey,Object toKey )`;返回该Map的子Map，其key范围从fromkey（包括）到tokey（不包括）。
10. `SortMap tailMap（Object fromkey ,boolean inclusive）`:返回该Map的子Map，其key范围大于fromkey（是否包括取决于第二个参数）的所有key。
11. `SortMap headMap（Object tokey ,boolean inclusive）`:返回该Map的子Map，其key范围小于tokey（是否包括取决于第二个参数）的所有key。



- LinkedHashMap：

它的父类是HashMap，但是用Iterator进行遍历的时候，它会按插入顺序或最先使用的顺序（least-recently-used(LRU)order）进行访问。
除了用Iterator外，其他情况下，只是比HashMap稍慢一点。用Iterator的情况下，由于是使用链表来保存内部顺序，因此速度会更快。


- WeakHashMap：

WeakHashMap与HashMap的用法基本相同，区别在于：后者的key保留对象的强引用，即只要HashMap对象不被销毁，其对象所有key所引用的对象不会被垃圾回收，
HashMap也不会自动删除这些key所对应的键值对对象。
但WeakHashMap的key所引用的对象没有被其他强引 用变量所引用，则这些key所引用的对象可能被回收。
WeakHashMap中的每个key对象保存了实际对象的弱引用，当回收了该key所对应的实际对象后，
WeakHashMap会自动删除该key所对应的键值对。


- IdentityHashMap：

IdentityHashMap与HashMap基本相似，只是当两个key严格相等时，即key1==key2时，它才认为两个key是相等的 。
IdentityHashMap也允许使用null，但不保证键值对之间的顺序。



- 面试题：

1.HashMap与HashTable的区别：

    1）同步性：  
    Hashtable是同步的，这个类中的一些方法保证了Hashtable中的对象是线程安全的。
    而HashMap则是异步的，因此 HashMap中的对象并不是线程安全的。
    因为同步的要求会影响执行的效率，所以如果你不需要线程安全的集合那么使用HashMap是一个很好的选择，
    这 样可以避免由于同步带来的不必要的性能开销，从而提高效率。

    2）值：
    HashMap可以让你将空值作为一个表的条目的key或value，但是Hashtable是不能放入空值的。
    HashMap最多只有一个key值为null，但可以有无数多个value值为null。


    3）两者的性能：
    HashMap的性能最好，HashTable的性能是最差（因为它是同步的）

2.讲一讲TreeMap、HashMap、LindedHashMap的区别：

    LinkedHashMap可以保证HashMap集合有序，存入的顺序和取出的顺序一致。

    TreeMap实现SortMap接口，能够把它保存的记录根据键排序,默认是按键值的升序排序，也可以指定排序的比较器，
    当用Iterator遍历TreeMap时，得到的记录是排过序的。

    HashMap不保证顺序，即为无序的，具有很快的访问速度。
    HashMap最多只允许一条记录的键为Null;允许多条记录的值为Null。HashMap不支持线程的同步。

    我们在开发的过程中使用HashMap比较多，在Map 中插入、删除和定位元素，HashMap 是最好的选择。
    但如果您要按自然顺序或自定义顺序遍历键，那么TreeMap会更好。

    如果需要输出的顺序和输入的相同,那么用LinkedHashMap 可以实现,它还可以按读取顺序来排列。


## Queue

队列通常（但并非一定）以 FIFO（先进先出）的方式排序各个元素。

- LinkedList:

提供了方法支持队列的行为，因此可以作为Queue的一种实现，将LinkedList向上转型为Queue使用。

- PriorityQueue：

一个基于优先级堆的无界优先级队列。优先级队列的元素按照其自然顺序进行排序，或者根据构造队列时提供的Comparator进行排序，
具体取决于所使用的构造方法。优先级队列不允许使用null元素。
依靠自然顺序的优先级队列还不允许插入不可比较的对象（这样做可能导致ClassCastException）。





