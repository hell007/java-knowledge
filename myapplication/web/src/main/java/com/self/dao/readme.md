
## userMapper 解释

```
@CacheNamespace(size = 512) 

```
定义在该命名空间内允许使用内置缓存，最大值为512个对象引用，读写默认是开启的，缓存内省刷新时间为默认3600000毫秒，

写策略是拷贝整个对象镜像到全新堆（如同CopyOnWriteList）因此线程安全。 

```
@SelectProvider(type = UserSqlProvider.class, method = "getAll") 

```
提供查询的SQL语句，如果你不用这个注解，你也可以直接使用@Select("select * from ....")注解，把查询SQL抽取到一个类里面，方便管理，

同时复杂的SQL也容易操作，type = UserSqlProvider.class就是存放SQL语句的类，

而method = "getAll"表示get接口方法需要到 UserSqlProvider 类的 getAll 方法中获取SQL语句。 

```
@Options(useCache = true, flushCache = false, timeout = 10000) 

```
一些查询的选项开关，比如useCache = true表示本次查询结果被缓存以提高下次查询速度，
flushCache = false表示下次查询时不刷新缓存，
timeout = 10000表示查询结果缓存10000秒。 

```
@Results(value = {  
        @Result(id = true, property = "id", column = "id", javaType = String.class, jdbcType = JdbcType.VARCHAR),  
        @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(property = "phone", column = "phone", javaType = String.class, jdbcType = JdbcType.VARCHAR)
})  
```
表示sql查询返回的结果集，
@Results是以@Result为元素的数组，
@Result表示单条属性-字段的映射关系，

如：@Result(id = true, property = "id", column = "id", javaType = String.class, jdbcType = JdbcType.VARCHAR)

可以简写为：@Result(id = true, property = "id", column = "id")，id = true表示这个id字段是个PK，

查询时mybatis会给予必要的优化，应该说数组中所有的@Result组成了单个记录的映射关系，而@Results则单个记录的集合。

> 注意： 返回的结果集是javabean对象，id,name,phone字段是含有值的，其他字段值为null
 
```
@Param("id") 
```
全局限定别名，定义查询参数在sql语句中的位置不再是顺序下标0,1,2,3....的形式，而是对应名称，该名称就在这里定义。 


```
@ResultMap(value = "queryUserAddress") 
List<User> queryUserAddress();
```
重要的注解，可以解决复杂的映射关系，包括resultMap嵌套，鉴别器discriminator等等。

注意一旦你启用该注解，**你将不得不在你的映射文件中配置你的resultMap**，

而value = "queryUserAddress"即为映射文件中的resultMap ID(注意此处的value = "queryUserAddress"，必须是在映射文件中指定命名空间路径)。

@ResultMap在某些简单场合可以用@Results代替，但是**复杂查询，比如联合、嵌套查询** @ResultMap就会显得解耦方便更容易管理。 


注意：@ResultMap 映射文件中配置你的resultMap跟xml映射一样， 

但不写 <select id="getUserAddress" resultMap="queryUserAddress" parameterType="java.lang.String">语句，sqlprovider里写



 
```
@InsertProvider(type = UserSqlProvider.class, method = "insertUser") 

```
用法和含义@SelectProvider一样，只不过是用来插入数据库而用的。 

```
@Options(flushCache = true, timeout = 20000) 
```
对于需要更新数据库的操作，需要重新刷新缓存flushCache = true使缓存同步。 

```
@UpdateProvider(type = TestSqlProvider.class, method = "updateUser") 

```
用法和含义@SelectProvider一样，只不过是用来更新数据库而用的。 

```
@Param("user") 
````
是一个自定义的对象，指定了sql语句中的表现形式，如果要在sql中引用对象里面的属性，

只要使用user.id，user.name即可，mybatis会通过反射找到这些属性值。 

```
@DeleteProvider(type = TestSqlProvider.class, method = "deleteSql") 
````
用法和含义@SelectProvider一样，只不过是用来删除数据而用的

<pre>
<resultMap id="queryUserAddress" type="com.self.model.User"> 
    <id column="id" property="id" jdbcType="VARCHAR"/>  
    <result column="name" property="name" jdbcType="VARCHAR"/>  
    <result column="passwd" property="passwd" jdbcType="VARCHAR"/>  
    <collection property="UserAddress" javaType="java.util.List" ofType="com.self.model.UserAddress">  
        <id column="id" property="id" jdbcType="VARCHAR" />  
        <result column="addressname" property="adressname" jdbcType="VARCHAR" />   
    </collection>  
</resultMap>

<select id="getUserAddress" resultMap="queryUserAddress" parameterType="java.lang.String">
        select u.id,u.name,
                a.id,a.addressname
        form myse_user u
        left join 
        myself_user_address
        on u.id = a.uid
        where id = #{id, jdbcType=VARCHAR}
</select>
</pre>     

###### 注意

1.如果你在userMapper中调用该方法的某个接口方法已经定义了参数

```
@Param()
```

那么该方法的参数Map<String, Object> parameters即组装了@Param()定义的参数，

比如userMapper接口方法中定义参数为

```
@Param("name"),@Param("passwd")
```

那么parameters的形态就是：

```
[key="name",value=object1],[key="passwd",value=object2]
```

2.如果接口方法没有定义
```
@Param()
```

那么parameters的key就是参数的顺序小标：

```
[key=0,value=object1],[key=1,value=object2]，
```

SQL()将返回最终append结束的字符串，

sql语句中的形如 #{id,javaType=string,jdbcType=VARCHAR}完全可简写为#{id}，

另外，对于复杂查询还有很多标签可用，比如：JOIN，INNER_JOIN，GROUP_BY，ORDER_BY等等



