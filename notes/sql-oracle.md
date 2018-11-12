
## oracle

##### oracle sys/system用户的默认密码

system默认:manager

sys默认:change_on_install

使用SQL Plus登录数据库时，system使用密码manager可直接登录。

但如果是sys用户，密码必须加上as sysdba，即完整密码为：change_on_install as sysdba


##### 登录命令

sql plus

```
sys登录
conn / as sysdba

修改密码
> alter user system identified by manager;

system 登录
> conn system/manager

解锁scott
> alter user scott account unlock;
> alter user scott identified by tigger;
> conn scott/tigger

创建用户
> conn system/manager
> create user test identified by t123;

修改口令
> password test
> t1234
> t1234

```

##### 2.创建数据库和用户

- 3步:

      创建两个数据库的文件
      创建用户与上面创建的文件形成映射关系
      给用户添加权限

1.创建数据库的文件

    CREATE TABLESPACE jie LOGGING DATAFILE 'D:\Dev\oracle11\oradata\jie\jie.dbf' 
    SIZE 100M AUTOEXTEND ON NEXT 32M MAXSIZE 500M EXTENT MANAGEMENT LOCAL;
    
    create temporary tablespace jie_temp tempfile 'D:\Dev\oracle11\oradata\jie\jie_temp.dbf' 
    size 100m autoextend on next 32m maxsize 500m extent management local

2.创建用户与上面创建的文件形成映射关系（用户名为wzh,密码为w123）

    CREATE USER wzh IDENTIFIED BY w123 DEFAULT TABLESPACE jie TEMPORARY TABLESPACE jie_temp;

3.添加权限

    grant connect,resource,dba to wzh;
    grant create session to wzh;

- 拓展：

1.删除数据库

    DROP TABLESPACE jie INCLUDING CONTENTS AND DATAFILES;
     
 2.删除用户
 
    drop user wzh cascade;


##### 3.建表语句

```
1、创建表

create table t_user(  
id number(4) not null primary key,                
name varchar2(20) not null,
sex char(2),
age number(3),
mobile char(11),
job varchar2(20)
); 


2、创建自动增长序列

 create Sequence user_add_auto_sequence 
 Increment by 1     -- 每次加几个 
 start with 1       -- 从1开始计数     
 maxvalue 9999      -- 不设置最大值:nomaxvalue,设置最大值：maxvalue 9999  
 nocycle            -- 一直累加，不循环    
 cache 10;  

3、创建触发器

 create trigger user_add_auto_trigger before 
 insert on t_user for each row /*对每一行都检测是否触发*/
 begin
 select user_add_auto_sequence.nextval into:New.id from dual;
 end;      

4、提交 

commit;

5、测试 

insert into T_USER(NAME,SEX,AGE,MOBILE,JOB) values ('李红','女',34,'13677777777','开发');
```




