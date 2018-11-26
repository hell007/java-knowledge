
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




## 集合Map的知识

Map用于保存具有映射关系的数据，Map里保存着两组数据：key和value，
它们都可以使任何引用类型的数据，但key不能重复。所以通过指定的key就可以取出对应的value。

##### Map接口定义了如下常用的方法：

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

##### Map中包含一个内部类：Entry。

该类封装了一个键值对，它包含了三个方法：

1. Object getKey():返回该Entry里包含的key值。
2. Object getValeu():返回该Entry里包含的value值。
3. Object setValue(V value):设置该Entry里包含的value值，并返回新设置的value值。


##  HashMap和Hashtable实现类

####  HashMap与HashTable的区别：

1. 同步性：

Hashtable是同步的，这个类中的一些方法保证了Hashtable中的对象是线程安全的。
而HashMap则是异步的，因此 HashMap中的对象并不是线程安全的。
因为同步的要求会影响执行的效率，所以如果你不需要线程安全的集合那么使用HashMap是一个很好的选择，
这 样可以避免由于同步带来的不必要的性能开销，从而提高效率。

2. 值：

HashMap可以让你将空值作为一个表的条目的key或value，但是Hashtable是不能放入空值的。
HashMap最多只有一个key值为null，但可以有无数多个value值为null。


#### 两者的性能

HashMap的性能最好，HashTable的性能是最差（因为它是同步的）

**注意**

- 用作key的对象必须实现hashCode和equals方法。
- 不能保证其中的键值对的顺序
- 尽量不要使用可变对象作为它们的key值。


## LinkedHashMap

它的父类是HashMap，使用双向链表来维护键值对的次序，迭代顺序与键值对的插入顺序保持一致。
LinkedHashMap需要维护元素的插入顺序，so性能略低于HashMap，但在迭代访问元素时有很好的性能，因为它是以链表来维护内部顺序。


## TreeMap

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


## WeakHashMap

WeakHashMap与HashMap的用法基本相同，区别在于：后者的key保留对象的强引用，即只要HashMap对象不被销毁，其对象所有key所引用的对象不会被垃圾回收，
HashMap也不会自动删除这些key所对应的键值对对象。
但WeakHashMap的key所引用的对象没有被其他强引 用变量所引用，则这些key所引用的对象可能被回收。
WeakHashMap中的每个key对象保存了实际对象的弱引用，当回收了该key所对应的实际对象后，
WeakHashMap会自动删除该key所对应的键值对。

## IdentityHashMap

IdentityHashMap与HashMap基本相似，只是当两个key严格相等时，即key1==key2时，它才认为两个key是相等的 。
IdentityHashMap也允许使用null，但不保证键值对之间的顺序。





