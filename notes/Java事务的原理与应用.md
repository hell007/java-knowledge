
## Java事务的原理与应用

#### 一、什么是JAVA事务

  通常的观念认为，事务仅与数据库相关。

  事务必须服从ISO/IEC所制定的ACID原则。ACID是原子性（atomicity）、一致性（consistency）、隔离性 （isolation）和持久性（durability）的缩写。
  
  事务的原子性表示事务执行过程中的任何失败都将导致事务所做的任何修改失效。
  一致性表示当事务执行失败时，所有被该事务影响的数据都应该恢复到事务执行前的状态。
  隔离性表示在事务执行过程中对数据的修改，在事务提交之前对其他事务不可见。
  持久性表示已提交的数据在事务执行失败时，数据的状态都应该正确。

  通俗的理解，事务是一组原子操作单元，从数据库角度说，就是一组SQL指令，要么全部执行成功，若因为某个原因其中一条指令执行有错误，则撤销先前执行过的所有指令。更简答的说就是：要么全部执行成功，要么撤销不执行。

  既然事务的概念从数据库而来，那Java事务是什么？之间有什么联系？

  实际上，一个Java应用系统，如果要操作数据库，则通过JDBC来实现的。增加、修改、删除都是通过相应方法间接来实现的，事务的控制也相应转移到Java程序代码中。因此，数据库操作的事务习惯上就称为Java事务。
  
#### 二、为什么需要Java事务

事务是为解决数据安全操作提出的，事务控制实际上就是控制数据的安全访问。举一个简单例子：比如银行转帐业务，账户A要将自己账户上的1000元转到B账 户下面，A账户余额首先要减去1000元，然后B账户要增加1000元。假如在中间网络出现了问题，A账户减去1000元已经结束，B因为网络中断而操作失败，那么整个业务失败，必须做出控制，要求A账户转帐业务撤销。这才能保证业务的正确性，完成这个操作就需要事务，将A账户资金减少和B账户资金增加方。 到一个事务里面，要么全部执行成功，要么操作全部撤销，这样就保持了数据的安全性。

#### 三、Java事务的类型

Java事务的类型有三种：JDBC事务、JTA（Java Transaction API）事务、容器事务。


1、JDBC事务

JDBC 事务是用 Connection 对象控制的。JDBC Connection 接口（ java.sql.Connection ）提供了两种事务模式：自动提交和手工提交。 java.sql.Connection 提供了以下控制事务的方法：

```
public void setAutoCommit(boolean)
public boolean getAutoCommit()
public void commit()
public void rollback()
```
使用 JDBC 事务界定时，您可以将多个 SQL 语句结合到一个事务中。JDBC 事务的一个缺点是事务的范围局限于一个数据库连接。一个 JDBC 事务不能跨越多个数据库。


2、JTA（Java Transaction API）事务

JTA是一种高层的，与实现无关的，与协议无关的API，应用程序和应用服务器可以使用JTA来访问事务。

JTA允许应用程序执行分布式事务处理——在两个或多个网络计算机资源上访问并且更新数据，这些数据可以分布在多个数据库上。JDBC驱动程序的JTA支持极大地增强了数据访问能力。

如果计划用 JTA 界定事务，那么就需要有一个实现 javax.sql.XADataSource 、 javax.sql.XAConnection 和 javax.sql.XAResource 接口的 JDBC 驱动程序。一个实现了这些接口的驱动程序将可以参与 JTA 事务。一个 XADataSource 对象就是一个 XAConnection 对象的工厂。 XAConnection s 是参与 JTA 事务的 JDBC 连接。

您将需要用应用服务器的管理工具设置 XADataSource .从应用服务器和 JDBC 驱动程序的文档中可以了解到相关的指导。

J2EE应用程序用 JNDI 查询数据源。一旦应用程序找到了数据源对象，它就调用 javax.sql.DataSource.getConnection（） 以获得到数据库的连接。

XA 连接与非 XA 连接不同。一定要记住 XA 连接参与了 JTA 事务。这意味着 XA 连接不支持 JDBC 的自动提交功能。同时，应用程序一定不要对 XA 连接调用 java.sql.Connection.commit（） 或者 java.sql.Connection.rollback（）

相反，应用程序应该使用 UserTransaction.begin（）、 UserTransaction.commit（） 和 serTransaction.rollback（） 


3、容器事务

容器事务主要是J2EE应用服务器提供的，容器事务大多是基于JTA完成，这是一个基于JNDI的，相当复杂的API实现。相对编码实现JTA事务管理， 我们可以通过EJB容器提供的容器事务管理机制（CMT）完成同一个功能，这项功能由J2EE应用服务器提供。这使得我们可以简单的指定将哪个方法加入事务，一旦指定，容器将负责事务管理任务。这是我们常见的解决方式，因为通过这种方式我们可以将事务代码排除在逻辑编码之外，同时将所有困难交给J2EE容器去解决。使用EJB CMT的另外一个好处就是程序员无需关心JTA API的编码，不过，理论上我们必须使用EJB


#### 四、三种Java事务差异

1、JDBC事务控制的局限性在一个数据库连接内，但是其使用简单。

2、JTA事务的功能强大，事务可以跨越多个数据库或多个DAO，使用也比较复杂。

3、容器事务，主要指的是J2EE应用服务器提供的事务管理，局限于EJB应用使用。


#### 五、总结

Java事务控制是构建J2EE应用不可缺少的一部分，合理选择应用何种事务对整个应用系统来说至关重要。一般说来，在单个JDBC 连接连接的情况下可以选择JDBC事务，在跨多个连接或者数据库情况下，需要选择使用JTA事务，如果用到了EJB，则可以考虑使用EJB容器事务



## Java的JDBC事务详解

**事务的特性：**

1) 原子性（atomicity）：事务是数据库的逻辑工作单位，而且是必须是原子工作单位，对于其数据修改，要么全部执行，要么全部不执行。

2) 一致性（consistency）：事务在完成时，必须是所有的数据都保持一致状态。在相关数据库中，所有规则都必须应用于事务的修改，以保持所有数据的完整性。

3) 隔离性（isolation）：一个事务的执行不能被其他事务所影响。

4) 持久性（durability）：一个事务一旦提交，事物的操作便永久性的保存在DB中。即使此时再执行回滚操作也不能撤消所做的更改。

**事务(Transaction):**

是并发控制的单元，是用户定义的一个操作序列。这些操作要么都做，要么都不做，是一个不可分割的工作单位。

通过事务，sql server 能将逻辑相关的一组操作绑定在一起，以便服务器保持数据的完整性。

事务通常是以begin transaction开始，以commit或rollback结束。

Commint表示提交，即提交事务的所有操作。具体地说就是将事务中所有对数据的更新写回到磁盘上的物理数据库中去，事务正常结束。

Rollback表示回滚，即在事务运行的过程中发生了某种故障，事务不能继续进行，系统将事务中对数据库的所有已完成的操作全部撤消，滚回到事务开始的状态。


自动提交事务：每条单独的语句都是一个事务。每个语句后都隐含一个commit。 （默认）

显式事务：以begin transaction显示开始，以commit或rollback结束。

隐式事务：当连接以隐式事务模式进行操作时，sql server数据库引擎实例将在提交或回滚当前事务后自动启动新事务。无须描述事物的开始，只需提交或回滚每个事务。但每个事务仍以commit或rollback显式结束。连接将隐性事务模式设置为打开之后，当数据库引擎实例首次执行下列任何语句时，都会自动启动一个隐式事务：alter table，insert，create，open ，delete，revoke ，drop，select， fetch ，truncate table，grant，update在发出commit或rollback语句之前，该事务将一直保持有效。在第一个事务被提交或回滚之后，下次当连接执行以上任何语句时，数据库引擎实例都将自动启动一个新事务。该实例将不断地生成隐性事务链，直到隐性事务模式关闭为止。


**事务并发处理可能引起的问题**

脏读(dirty read)：一个事务读取了另一个事务尚未提交的数据，

不可重复读(non-repeatable read)：一个事务的操作导致另一个事务前后两次读取到不同的数据

幻读(phantom read)： 一个事务的操作导致另一个事务前后两次查询的结果数据量不同。

举例：

事务A、B并发执行时，

当A事务update后，B事务select读取到A尚未提交的数据，此时A事务rollback，则B读到的数据是无效的"脏"数据。

当B事务select读取数据后，A事务update操作更改B事务select到的数据，此时B事务再次读去该数据，发现前后两次的数据不一样。

当B事务select读取数据后，A事务insert或delete了一条满足A事务的select条件的记录，此时B事务再次select，发现查询到前次不存在的记录("幻影")，或者前次的某个记录不见了


**JDBC对事务的支持体现在三个方面：**

>1.自动提交模式(Auto-commit mode)

Connection提供了一个auto-commit的属性来指定事务何时结束。

a.当auto-commit为true时，当每个独立SQL操作的执行完毕，事务立即自动提交，也就是说每个SQL操作都是一个事务。

一个独立SQL操作什么时候算执行完毕，JDBC规范是这样规定的：

对数据操作语言(DML，如insert,update,delete)和数据定义语言(如create,drop)，语句一执行完就视为执行完毕。

对select语句，当与它关联的ResultSet对象关闭时，视为执行完毕。

对存储过程或其他返回多个结果的语句，当与它关联的所有ResultSet对象全部关闭，所有update count(update,delete等语句操作影响的行数)和output parameter(存储过程的输出参数)都已经获取之后，视为执行完毕。

b. 当auto-commit为false时，每个事务都必须显示调用commit方法进行提交，或者显示调用rollback方法进行回滚。auto-commit默认为true。

JDBC提供了5种不同的事务隔离级别，在Connection中进行了定义。


>2.事务隔离级别(Transaction Isolation Levels)

JDBC定义了五种事务隔离级别：

TRANSACTION_NONE JDBC驱动不支持事务

TRANSACTION_READ_UNCOMMITTED 允许脏读、不可重复读和幻读。

TRANSACTION_READ_COMMITTED 禁止脏读，但允许不可重复读和幻读。

TRANSACTION_REPEATABLE_READ 禁止脏读和不可重复读，单运行幻读。

TRANSACTION_SERIALIZABLE 禁止脏读、不可重复读和幻读。


>3.保存点(SavePoint)

JDBC定义了SavePoint接口，提供在一个更细粒度的事务控制机制。当设置了一个保存点后，可以rollback到该保存点处的状态，而不是rollback整个事务。

Connection接口的setSavepoint和releaseSavepoint方法可以设置和释放保存点

**注意点**

JDBC规范虽然定义了事务的以上支持行为，但是各个JDBC驱动，数据库厂商对事务的支持程度可能各不相同。如果在程序中任意设置，可能得不到想要的效果。为此，JDBC提供了DatabaseMetaData接口，提供了一系列JDBC特性支持情况的获取方法。比如，通过DatabaseMetaData.supportsTransactionIsolationLevel方法可以判断对事务隔离级别的支持情况，通过DatabaseMetaData.supportsSavepoints方法可以判断对保存点的支持情况。


##### 参考文章

[java事务 深入Java事务的原理与应用](http://blog.csdn.net/sinat_33536912/article/details/51200630)

[Java面试准备十三：事务](http://blog.csdn.net/u013349237/article/details/70197013)



