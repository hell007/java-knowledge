
## oracle

##### 1. oracle sys/system用户的默认密码

system默认:manager

sys默认:change_on_install

使用SQL Plus登录数据库时，system使用密码manager可直接登录。

但如果是sys用户，密码必须加上as sysdba，即完整密码为：change_on_install as sysdba


##### 2. 命令

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



```

