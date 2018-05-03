
## springboot-dubbo-zookeeper

1.Zookeeper 安装

[下载地址](http://zookeeper.apache.org/releases.html)

下载最新安装包，文件格式是tar.gz，可见Apache还是推荐在Linux上使用，但也提供了在windows上使用的脚本。 
解压之后，修改数据和日志配置，注意在3.5.3版本上，conf文件夹里不在直接提供zoo.cfg，而是提供了一个example，zoo_sample.cfg。复制重命名为zoo.cfg，然后修改data位置新增log位置。注意，windows目录要用//分割。

```
# The number of milliseconds of each tick
tickTime=2000
# The number of ticks that the initial 
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between 
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just 
# example sakes.
dataDir=D:\\Dev\\zookeeper-3.4.12\\zookeeper\\data
dataLogDir=D:\\Dev\\zookeeper-3.4.12\\zookeeper\\log
# the port at which the clients will connect
clientPort=2181
# the maximum number of client connections.
# increase this if you need to handle more clients
#maxClientCnxns=60
#
# Be sure to read the maintenance section of the 
# administrator guide before turning on autopurge.
#
# http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
#
# The number of snapshots to retain in dataDir
#autopurge.snapRetainCount=3
# Purge task interval in hours
# Set to "0" to disable auto purge feature
#autopurge.purgeInterval=1

```

2. dubbo-provider.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://code.alibabatech.com/schema/dubbo
       http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 配置可参考 http://dubbo.io/User+Guide-zh.htm -->
    <!-- 服务提供方应用名，用于计算依赖关系 -->
    <dubbo:application name="dubbo-provider" owner="dubbo-provider" />

    <!-- 定义 zookeeper 注册中心地址及协议 -->
    <dubbo:registry protocol="zookeeper" address="127.0.0.1:2181" client="zkclient" />

    <!-- 定义 Dubbo 协议名称及使用的端口，dubbo 协议缺省端口为 20880，如果配置为 -1 或者没有配置 port，则会分配一个没有被占用的端口 -->
    <dubbo:protocol name="dubbo" port="-1" />

    <!-- 声明需要暴露的服务接口 -->
    <dubbo:service interface="com.self.service.DemoService"
        ref="demoService" timeout="10000" />

    <!-- 和本地 bean 一样实现服务 -->
    <bean id="demoService" class="com.self.service.impl.DemoServiceImpl" />

</beans>
```

3.dubbo-consumer.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://code.alibabatech.com/schema/dubbo
       http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 配置可参考 http://dubbo.io/User+Guide-zh.htm -->
    <!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样 -->
    <dubbo:application name="dubbo-consumer" owner="dubbo-consumer" />

    <!-- 定义 zookeeper 注册中心地址及协议 -->
    <dubbo:registry protocol="zookeeper" address="127.0.0.1:2181" client="zkclient" />

    <!-- 生成远程服务代理，可以和本地 bean 一样使用 testService -->
    <dubbo:reference id="demoService" interface="com.self.service.DemoService" />

</beans>

```

4.使用










