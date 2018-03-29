

## Mycat学习

![](https://ws2.sinaimg.cn/large/006tNc79gy1fjglurmknmj30m80ipmyr.jpg)


### Mycat 安装配置

1.安装

 ```
cd /usr/local/src 
tar xvf Mycat-server-1.5.1-RELEASE-20160405120037-linux.tar.gz   
mv /usr/local/src/mycat /usr/local/  
  
groupadd mycat  
useradd -g mycat mycatadmin  
passwd mycat123456  
chown -R mycat.mycat /usr/local/mycat 
```

2.添加环境变量

```
vi /etc/profile  
vi ~/.bash_profile  
  
export MYCAT_HOME=/usr/local/mycat  
source ~/.bash_profile 

```

3.Mycat的启动也很简单，启动命令在Bin目录

```
##启动
mycat start

##停止
mycat stop

##重启
mycat restart
```

### mycat 配置

server.xml 和 schema.xml 配置情况

# vi /usr/local/mycat/conf/server.xml  
  
<?xml version="1.0" encoding="UTF-8"?>  
<!DOCTYPE mycat:server SYSTEM "server.dtd">  
<mycat:server xmlns:mycat="http://org.opencloudb/">  
        <system>  
            <!--   
                <property name="processors">32</property>  
                <property name="processorExecutor">32</property>   
                <property name="bindIp">0.0.0.0</property>   
                <property name="frontWriteQueueSize">4096</property>  
                <property name="idleTimeout">300000</property>  
                <property name="mutiNodePatchSize">100</property>  
            -->  
                <property name="defaultSqlParser">druidparser</property>  
                <property name="mutiNodeLimitType">1</property>  
                <property name="serverPort">8066</property>  
                <property name="managerPort">9066</property>   
        </system>  
        <!-- 任意设置登陆 mycat 的用户名,密码,数据库  -->  
        <user name="test">  
                <property name="password">test</property>  
                <property name="schemas">TESTDB</property>  
        </user>  
  
        <user name="user">  
                <property name="password">user</property>  
                <property name="schemas">TESTDB</property>  
                <property name="readOnly">true</property>  
        </user>  
        <!--   
        <quarantine>   
           <whitehost>  
              <host host="127.0.0.1" user="mycat"/>  
              <host host="127.0.0.2" user="mycat"/>  
           </whitehost>  
       <blacklist check="false"></blacklist>  
        </quarantine>  
        -->  
</mycat:server>  


# vi /usr/local/mycat/conf/schema.xml  
  
<?xml version="1.0"?>  
<!DOCTYPE mycat:schema SYSTEM "schema.dtd">  
<mycat:schema xmlns:mycat="http://org.opencloudb/">  
  
    <!-- 设置表的存储方式.schema name="TESTDB" 与 server.xml中的 TESTDB 设置一致  -->  
    <schema name="TESTDB" checkSQLschema="false" sqlMaxLimit="100">  
        <table name="users" primaryKey="id" type="global" dataNode="node_db01" />  
  
        <table name="item" primaryKey="id" dataNode="node_db02,node_db03" rule="mod-long">  
                <childTable name="item_detail" primaryKey="id" joinKey="item_id" parentKey="id" />  
        </table>  
    </schema>  
  
    <!-- 设置dataNode 对应的数据库,及 mycat 连接的地址dataHost -->  
    <dataNode name="node_db01" dataHost="dataHost01" database="db01" />  
    <dataNode name="node_db02" dataHost="dataHost01" database="db02" />  
    <dataNode name="node_db03" dataHost="dataHost01" database="db03" />  
  
    <!-- mycat 逻辑主机dataHost对应的物理主机.其中也设置对应的mysql登陆信息 -->  
    <dataHost name="dataHost01" maxCon="1000" minCon="10" balance="0" writeType="0" dbType="mysql" dbDriver="native">  
            <heartbeat>select user()</heartbeat>  
            <writeHost host="server1" url="127.0.0.1:3306" user="root" password="mysql"/>  
    </dataHost>  
</mycat:schema>  



[Mycat入门教程](https://blog.csdn.net/u013467442/article/details/56955846)

[MySQL 高可用：mysql+mycat实现数据库分片（分库分表）](https://blog.csdn.net/kk185800961/article/details/51147029)

[学会数据库读写分离、分表分库——用Mycat](https://www.cnblogs.com/joylee/p/7513038.html)


















