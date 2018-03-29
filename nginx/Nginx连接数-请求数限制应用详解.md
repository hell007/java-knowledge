

## Nginx连接数-请求数限制应用详解

1.场景

秒杀、抢购并发限制、队列缓冲

下载带宽限制

防止攻击


2.nginx连接数限制模块

说明：nginx有很多模块、模块下面又分很多指令，下面就说说limit_conn_zone和limit_conn两指令

a. limit_conn_zone

语法：

```
Syntax:     limit_conn_zone key zone=name:size;
Default:     —
Context:     http

```

nginx配置分为三个段：http、server、location，大概格式如下

```
http {

    server {
        listen       80;
        server_name  www.jie.com jie.com;
        location / {
            root   /var/www/jie;
            index  index.jsp index.html index.htm;
            set    $limit_key $binary_remote_addr;
            set    $limit_number 10;
        }
    }

}
```
>limit_conn_zone只能用在http段

```
http {
    limit_conn_zone $binary_remote_addr zone=addr:10m;
    server {
        listen       80;
        server_name  www.jie.com jie.com;
        location / {
            root   /var/www/jie;
            index  index.jsp index.html index.htm;
            limit_conn addr 5; #是限制每个IP只能发起5个连接
            limit_rate 100k; #限速为 100KB/秒
        }
    }
}
```

限制每个name对应客服端的连接数，比如上面的limit_conn addr 5;意思就是现在addr这个name对应的客服端的连接数，比如name对应的是45.168.68.202这个ip地址，那么这个ip最多有5个并发连接，
那什么并发呢？像这样的连接，请求到达并已经读取了请求头信息到响应头信息发送完毕，在这个过程中的连接，当一个客服端的并发连接达到我们设置的5个以上时，
会返回503 (Service Temporarily Unavailable) 错误


b. limit_conn指令

```
Syntax:     limit_conn zone number;
Default:     —
Context:     http, server, location
```

c.limit_conn_log_level指令

```
Syntax:     limit_conn_log_level info | notice | warn | error;
Default:     limit_conn_log_level error;
Context:     http, server, location
```
说明：当达到最大限制连接数后，记录日志的等级


d.limit_conn_status指令

```
Syntax:     limit_conn_status code;
Default:     limit_conn_status 503;
Context:     http, server, location
```

说明：当超过限制后，返回的响应状态码，默认是503，现在你就知道上面为什么会返回503（Service Temporarily Unavailable）服务暂时不可用


例1、同时限制ip和虚拟主机最大并发连接

http {
    limit_conn_zone $binary_remote_addr zone=perip:10m;
    limit_conn_zone $server_name zone=perserver:10m;
    server {
        location / {
            limit_conn perip 10;
            limit_conn perserver 1000;
        }
    }
}

例2、根据请求参数来限制

#请求：http://www.jie.com/item.html?mp=1967464354&id=43566929485
limit_conn_zone $mp_limit_key zone=mp:10m;

```
server {
    set $mp_limit_key $binary_remote_addr; #key设置默认值
    if ( $query_string ~ .*mp=(\d+).* ) {
        set $mp_limit_key $1;
    }
    location / {
        limit_conn mp 10;
    }
}
```


### Nginx限制访问速率和最大并发连接数模块--limit (防止DDOS攻击)

```
http：
##zone=one或allips 表示设置名为"one"或"allips"的存储区，大小为10兆字节
##rate=2r/s 允许1秒钟不超过2个请求
limit_conn_log_level error;
limit_conn_status 503; 
limit_conn_zone $binary_remote_addr zone=one:10m;
limit_conn_zone $server_name zone=perserver:10m;
limit_req_zone $binary_remote_addr zone=allips:100m rate=2r/s;  

server：
##burst=5 表示最大延迟请求数量不大于5。如果过多的请求被限制延迟是不需要的，这时需要使用nodelay参数，服务器会立刻返回503状态码。
limit_conn  one  100;   ##表示最大并发连接数100                                          
limit_conn perserver 1000; 
limit_req   zone=allips  burst=5  nodelay; 

参数解释：
$binary_remote_addr限制同一客户端ip地址；
$server_name限制同一server最大并发数；
limit_conn为限制并发连接数；
limit_rate为限制下载速度；
```




