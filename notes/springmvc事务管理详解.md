
## springmvc事务管理详解

spring支持编程式事务管理和声明式事务管理两种方式。

#### 编程式事务管理

编程式事务管理使用TransactionTemplate或者直接使用底层的PlatformTransactionManager。对于编程式事务管理，spring推荐使用TransactionTemplate。


1.Spring使用事务管理器，每个不同平台的事务管理器都实现了接口：PlatformTransactionManager
此接口是事务管理的核心，提供了三个需要实现的函数：

```
commit(TransactionStatus status) ;     
getTransaction(TransactionDefinition definition) ;     
rollback(TransactionStatus status) ;  

```

2.如果我们使用的是JDBC来处理事务，那么这个事务管理器就是DataSourceTransactionManager。

通过Spring文档查找到这个类，发现其需要DataSource这个类。也就是只要实现了javax.sql.DataSource这个接口的类，
都可以作为参数传入到DataSourceTransactionManager。

然后，找到包org.springframework.transaction.support中的 TransactionTemplate。发现TransactionTemplate中有一个重要的方法：

```
execute(TransactionCallback action) ; 

```

就是利用这个方法，我们可以在这个方法中添加事务。这个方法需要传入参数 TransactionCallback。

TransactionCallback，顾名思义，就是事务回调然后查到TransactionCallback。
发现这是一个接口（这也必须是接口，因为任务都是自己具体定义的）里面只有一个方法：

```
doInTransaction(TransactionStatus status) ; 

```

很明显，就是在一个事务中需要做的事情都包括这这个方法中了。
而这个doInTransaction 又传入了一个参数，这次是 TransactionStatus，继续顾名思义，也就是事务状态。
查询下去，这个 TransactionStatus 还是一个接口。 看看这个接口定义了哪些服务（方法）：

```
hasSavepoint() ;  
isCompleted() ;    
isNewTransaction() ;  
setRollbackOnly() ;

```
当需要回滚的时候，需要在调用 setRoolbackOnly(); 就OK了。



总结一下编程式事务管理

首先： 因为我们使用的是特定的平台，所以，我们需要创建一个合适我们的平台事务管理PlateformTransactionManager。
如果使用的是JDBC的话，就用DataSourceTransactionManager。
注意需要传入一个DataSource，这样，平台才知道如何和数据库打交道。

第二： 为了使得平台事务管理器对我们来说是透明的，就需要使用 TransactionTemplate。
使用TransactionTemplat需要传入一个 PlateformTransactionManager 进入，
这样，我们就得到了一个 TransactionTemplate，而不用关心到底使用的是什么平台了

第三： TransactionTemplate 的重要方法就是 execute 方法，此方法就是调用 TransactionCallback 进行处理。
也就是说，实际上我们需要处理的事情全部都是在 TransactionCallback 中编码的。

第四： 也就是 TransactionCallback 接口，我们可以定义一个类并实现此接口，然后作为 TransactionTemplate.execute 的参数。
把需要完成的事情放到 doInTransaction中，并且传入一个 TransactionStatus 参数。此参数是来调用回滚的。
也就是说 ，PlateformTransactionManager 和 TransactionTemplate 只需在程序中定义一次，而TransactionCallback 和 TransactionStatus 就要针对不同的任务多次定义了。
这就是Spring的编程式事务管理。


#### 声明式事务管理

声明式事务管理建立在AOP之上的。其本质是对方法前后进行拦截，然后在目标方法开始之前创建或者加入一个事务，在执行完目标方法之后根据执行情况提交或者回滚事务。

声明式事务最大的优点就是不需要通过编程的方式管理事务，这样就不需要在业务逻辑代码中掺杂事务管理的代码，只需在配置文件中做相关的事务规则声明(或通过基于@Transactional注解的方式)，便可以将事务规则应用到业务逻辑中。

显然声明式事务管理要优于编程式事务管理，这正是spring倡导的非侵入式的开发方式。

声明式事务管理使业务代码不受污染，一个普通的POJO对象，只要加上注解就可以获得完全的事务支持。

和编程式事务相比，声明式事务唯一不足地方是，后者的最细粒度只能作用到方法级别，无法做到像编程式事务那样可以作用到代码块级别。但是即便有这样的需求，也存在很多变通的方法，比如，可以将需要进行事务管理的代码块独立为方法等等。

声明式事务管理也有两种常用的方式，一种是基于tx和aop名字空间的xml配置文件，另一种就是基于@Transactional注解。显然基于注解的方式更简单易用，更清爽。


#### spring事务特性

spring所有的事务管理策略类都继承自org.springframework.transaction.PlatformTransactionManager接口,其中TransactionDefinition接口定义以下特性：

1.事务隔离级别

隔离级别是指若干个并发的事务之间的隔离程度。TransactionDefinition 接口中定义了五个表示隔离级别的常量：

```
TransactionDefinition.ISOLATION_DEFAULT：这是默认值，表示使用底层数据库的默认隔离级别。对大部分数据库而言，通常这值就是TransactionDefinition.ISOLATION_READ_COMMITTED。

TransactionDefinition.ISOLATION_READ_UNCOMMITTED：该隔离级别表示一个事务可以读取另一个事务修改但还没有提交的数据。该级别不能防止脏读，不可重复读和幻读，因此很少使用该隔离级别。比如PostgreSQL实际上并没有此级别。

TransactionDefinition.ISOLATION_READ_COMMITTED：该隔离级别表示一个事务只能读取另一个事务已经提交的数据。该级别可以防止脏读，这也是大多数情况下的推荐值。

TransactionDefinition.ISOLATION_REPEATABLE_READ：该隔离级别表示一个事务在整个过程中可以多次重复执行某个查询，并且每次返回的记录都相同。该级别可以防止脏读和不可重复读。

TransactionDefinition.ISOLATION_SERIALIZABLE：所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，该级别可以防止脏读、不可重复读以及幻读。但是这将严重影响程序的性能。通常情况下也不会用到该级别。

```

2.事务传播行为

所谓事务的传播行为是指，如果在开始当前事务之前，一个事务上下文已经存在，此时有若干选项可以指定一个事务性方法的执行行为。在TransactionDefinition定义中包括了如下几个表示传播行为的常量：

```
TransactionDefinition.PROPAGATION_REQUIRED：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。

TransactionDefinition.PROPAGATION_REQUIRES_NEW：创建一个新的事务，如果当前存在事务，则把当前事务挂起。

TransactionDefinition.PROPAGATION_SUPPORTS：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。

TransactionDefinition.PROPAGATION_NOT_SUPPORTED：以非事务方式运行，如果当前存在事务，则把当前事务挂起。

TransactionDefinition.PROPAGATION_NEVER：以非事务方式运行，如果当前存在事务，则抛出异常。

TransactionDefinition.PROPAGATION_MANDATORY：如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。

TransactionDefinition.PROPAGATION_NESTED：如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于TransactionDefinition.PROPAGATION_REQUIRED。

```

3.事务超时

所谓事务超时，就是指一个事务所允许执行的最长时间，如果超过该时间限制但事务还没有完成，则自动回滚事务。在 TransactionDefinition 中以 int 的值来表示超时时间，其单位是秒。

默认设置为底层事务系统的超时值，如果底层数据库事务系统没有设置超时值，那么就是none，没有超时限制。


4.事务只读属性

只读事务用于客户代码只读但不修改数据的情形，只读事务用于特定情景下的优化，比如使用Hibernate的时候。

默认为读写事务。

“只读事务”并不是一个强制选项，它只是一个“暗示”，提示数据库驱动程序和数据库系统，这个事务并不包含更改数据的操作，那么JDBC驱动程序和数据库就有可能根据这种情况对该事务进行一些特定的优化，比方说不安排相应的数据库锁，以减轻事务对数据库的压力，毕竟事务也是要消耗数据库的资源的。 

但是你非要在“只读事务”里面修改数据，也并非不可以，只不过对于数据一致性的保护不像“读写事务”那样保险而已。 

因此，“只读事务”仅仅是一个性能优化的推荐配置而已，并非强制你要这样做不可



#### spring事务

1.myBatis为例   基于注解的声明式事务管理配置@Transactional回滚规则


指示spring事务管理器回滚一个事务的推荐方法是在当前事务的上下文内抛出异常。spring事务管理器会捕捉任何未处理的异常，然后依据规则决定是否回滚抛出异常的事务。

默认配置下，spring只有在抛出的异常为运行时unchecked异常时才回滚该事务，也就是抛出的异常为RuntimeException的子类(Errors也会导致事务回滚)，而抛出checked异常则不会导致事务回滚。可以明确的配置在抛出那些异常时回滚事务，包括checked异常。也可以明确定义那些异常抛出时不回滚事务。还可以编程性的通过setRollbackOnly()方法来指示一个事务必须回滚，在调用完setRollbackOnly()后你所能执行的唯一操作就是回滚。


#### spring事务配置

1.myBatis为例，基于注解的声明式事务管理配置@Transactional

```   
<!-- 让spring管理sqlsessionfactory 使用mybatis和spring整合包中的 -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
	<property name="dataSource" ref="dataSource"/>
		<property name="typeAliasesPackage" value="com.self.model"/>
		<property name="configLocation" value="classpath:mybatis/mybatis-config.xml" />
		<property name="mapperLocations" value="classpath:com/self/dao/mapping/*.xml"/>
		<property name="plugins">
	    <array>
	        <bean class="com.github.pagehelper.PageHelper">
	            <property name="properties">
	                <value>
	                    dialect=mysql
	                    reasonable=true
	                </value>
	            </property>
	        </bean>
	    </array>
	</property>
</bean>

<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate" scope="prototype">
    	<constructor-arg index="0" ref="sqlSessionFactory"/>
</bean>

<!-- mybatis直接写sql查 -->
<bean id="sqlMapper" class=" com.self.dao.SqlMapper" scope="prototype">
	<constructor-arg ref="sqlSession"/>
</bean>
  
<!-- spring与mybatis整合配置，扫描所有dao -->
<!-- mapper3插件支持 -->
	<bean class="tk.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.self.dao"/>
        <property name="properties">
	        <value>
	            mappers=tk.mybatis.mapper.common.Mapper
	        </value>
	    </property>
 	</bean>
 
<!-- 配置spring的PlatformTransactionManager，名字为默认值 -->  
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">  
    <property name="dataSource" ref="dataSource" />  
</bean>  
  
<!-- 开启事务控制的注解支持 -->  
<tx:annotation-driven transaction-manager="transactionManager"/>
    
```

MyBatis自动参与到spring事务管理中，无需额外配置，只要org.mybatis.spring.SqlSessionFactoryBean引用的数据源与DataSourceTransactionManager引用的数据源一致即可，否则事务管理会不起作用

**用法**

@Transactional 可以作用于接口、接口方法、类以及类方法上。当作用于类上时，该类的所有 public 方法将都具有该类型的事务属性，同时，我们也可以在方法级别使用该标注来覆盖类级别的定义。

虽然 @Transactional 注解可以作用于接口、接口方法、类以及类方法上，但是 Spring 建议不要在接口或者接口方法上使用该注解，因为这只有在使用基于接口的代理时它才会生效。

另外， @Transactional 注解应该只被应用到 public 方法上，这是由 Spring AOP 的本质决定的。如果你在 protected、private 或者默认可见性的方法上使用 @Transactional 注解，这将被忽略，也不会抛出任何异常。

默认情况下，只有来自外部的方法调用才会被AOP代理捕获，也就是，类内部方法调用本类内部的其他方法并不会引起事务行为，即使被调用方法使用@Transactional注解进行修饰。
	

2.myBatis为例，基于注解的声明式事务管理配置,xml配置

主要为aop切面配置，只看xml就可以了

```
  <!-- 事务管理器 => 配置spring的PlatformTransactionManager，名字为默认值 --> 
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<!-- 数据源 -->
		<property name="dataSource" ref="dataSource" />
	</bean>
	
	<!-- 事物切面配置 -->
	<tx:advice id="txAdvice" transaction-manager="transactionManager">
		<tx:attributes>
			<!-- 传播行为 -->
			<tx:method name="save*" propagation="REQUIRED" />
			<tx:method name="insert*" propagation="REQUIRED" />
			<tx:method name="add*" propagation="REQUIRED" />
			<tx:method name="create*" propagation="REQUIRED" />
			<tx:method name="delete*" propagation="REQUIRED" />
			<tx:method name="update*" propagation="REQUIRED" />
			<tx:method name="find*" propagation="SUPPORTS" read-only="true" />
			<tx:method name="select*" propagation="SUPPORTS" read-only="true" />
			<tx:method name="get*" propagation="SUPPORTS" read-only="true" />
		</tx:attributes>
	</tx:advice>
	
	<!-- 切面 -->
	<aop:config>
		<aop:advisor advice-ref="txAdvice" pointcut="execution(* com.self.service.*.*(..))" />
	</aop:config>
  
  ```



##### 参考文章

[spring事物配置，声明式事务管理和基于@Transactional注解的使用](http://blog.csdn.net/bao19901210/article/details/41724355)

