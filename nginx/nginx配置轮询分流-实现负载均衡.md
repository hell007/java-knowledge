

## nginx配置轮询分流-实现负载均衡


### 准备工作

3台服务器，或者开虚拟机吧

ip分别为：192.168.1.10  192.168.1.11  192.168.1.12   （环境：安装了ngixn 没有做任何配置）


### 配置工作

1. 192.168.1.10  作为  负载均衡服务器  （一会负载均衡就在这里台服务器做配置，另外2台不用做配置）

打开192.168.1.10 的nginx.conf进行简要配置

```
upstream jie.com{  
          server   192.168.1.11:80;  
          server   192.168.1.12:80;  
} 

server {  
  listen 80;  
  server_name www.jie.com;  
  location / {  
     proxy_pass        http://www.jie.com;  
     proxy_set_header   Host             $host;  
     proxy_set_header   X-Real-IP        $remote_addr;  
     proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;  
  }  
  access_log logs/access_log;  
  error_log logs/error_log;  
}  

```

2. www.jie.com 修改host 指向了  192.168.1.10

3.www.jie.com 每次刷新，都会跳转到不同的服务器上页面 (192.168.1.11,  192.168.1.12)








