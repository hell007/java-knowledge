## Mysql winx64 解压缩版配置安装

具体安装如下：
1、把 mysql-5.7.19-winx64.zip 压缩文件解压到 D:\MySQL\ 目录下；
2、在 D:\MySQL\ 目录下新建 my.ini 配置文件；
3、用文本编辑器或其他编辑器打开 my.ini 文件，把以下代码复制粘贴进去，保存退出；

**-----代码开始-----**
[Client]
##### 设置3306端口
port = 3306
 
[mysqld]
##### 设置3306端口
port = 3306
##### 设置mysql的安装目录
basedir=D:\mysql
##### 设置mysql数据库的数据的存放目录
datadir=D:\mysql\data
##### 允许最大连接数
max_connections=200
##### 服务端使用的字符集默认为8比特编码的latin1字符集
character-set-server=utf8
##### 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
 
[mysql]
##### 设置mysql客户端默认字符集
default-character-set=utf8
**----代码结束----**

4、配置环境变量；
4.1、新建系统变量 MYSQL_HOME ，并配置变量值为 D:\mysql ；
4.2、编辑系统变量 Path ，将 ;%MYSQL_HOME%\bin 添加到 Path 变量值后面。
5、以管理员身份运行命令提示符cmd（一定要用管理员身份运行，不然权限不够）；
5.1、使用dos指令，进入D:\mysql 目录，运行以下指令

mysqld --defaults-file=my.ini --initialize-insecure
mysqld --install
net start mysql
5.2、设置 mysql 的 root 密码，运行以下指令

mysql -u root -p  
//(默认密码为空)
use mysql;
update user set authentication_string=password('admin') where user='root';
flush privileges;
exit
至此，Mysql 5.7.19 winx64 解压缩版配置安装已经完成！祝您成功！


## cmd操作mysql

1.mysql服务的启动和停止

	net stop mysql
 	net start mysql

2.登陆

	mysql –u root -p


