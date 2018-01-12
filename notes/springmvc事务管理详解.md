
## springmvc事务管理详解

#### spring可以支持编程式事务和声明式事务。

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







