

## nginx根据IP实现分流


1.根据特定IP来实现分流

>将IP地址的最后一段最后一位为0或2或6的转发至test-01.com来执行，否则转发至test-02.com来执行

```
upstream test-01.com {
  server 192.168.1.100:8080;
}
 
upstream test-02.com {
  server 192.168.1.200:8080;
}
 
server {
  listen 80;
  server_name www.test.com;
   location / {
      if ( $remote_addr ~* ^(.*)\.(.*)\.(.*)\.*[026]$){
        proxy_pass http://test-01.com;
        break;
      }
      proxy_pass http://test-02.com;
    }
}
```

>将IP地址前3段为192.168.202.*转发至test-01.com来执行，否则转发至test-02.com来执行

```
upstream test-01.com {
  server 192.168.1.100:8080;
}
 
upstream test-02.com {
  server 192.168.1.200:8080;
}
 
server {
   listen 80;
   server_name www.test.com;
   location / {
      if ( $remote_addr ~* ^(192)\.(168)\.(202)\.(.*)$) {
        proxy_pass http://test-01.com;
        break;
      }
      proxy_pass http://test-02.com;
    }
}

```

2.根据指定范围IP来实现分流

>将IP地址的最后一段为1-100的转发至test-01.com来执行，否则转发至test-02.com执行

```
upstream test-01.com {
  server 192.168.1.100:8080;
}
 
upstream test-02.com {
  server 192.168.1.200:8080;
}
 
server {
   listen 80;
   server_name www.test.com;
   location / {
     if ( $remote_addr ~* ^(.*)\.(.*)\.(.*)\.[1,100]$){
       proxy_pass http://test-01.com;
       break;
     }
     proxy_pass http://test-02.com;
   }
}
```

3.根据forwarded地址分流

>将IP地址的第1段为212开头的访问转发至test-01.com来执行，否则转发至test-02.com执行

```
upstream test-01.com {
  server 192.168.1.100:8080;
}
 
upstream test-02.com {
  server 192.168.1.200:8080;
}
 
server {
   listen 80;
   server_name www.test.com;
   location / {
     if ( $http_x_forwarded_for ~* ^(212)\.(.*)\.(.*)\.(.*)$){
       proxy_pass http://test-01.com;
       break;
     }
     proxy_pass http://test-02.com;
   }
}
```

if指令的作用

if指令: 判断表达式的值是否为真(true)， 如果为真则执行后面大括号中的内容。

以下是一些条件表达式的常用比较方法：

```

变量的完整比较可以使用=或!=操作符
部分匹配可以使用~或~*的正则表达式来表示
~表示区分大小写
~*表示不区分大小写(nginx与Nginx是一样的)
!~与!~*是取反操作，也就是不匹配的意思
检查文件是否存在使用-f或!-f操作符
检查目录是否存在使用-d或!-d操作符
检查文件、目录或符号连接是否存在使用-e或!-e操作符
检查文件是否可执行使用-x或!-x操作符
正则表达式的部分匹配可以使用括号，匹配的部分在后面可以用$1~$9变量代替

```



