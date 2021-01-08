

```

worker_processes  1;
events {
    worker_connections  1024;
}
http {
    include       mime.types;
    default_type  application/octet-stream;
    fastcgi_intercept_errors on;
    server {
        listen       80;
        server_name  127.0.0.1;
        
        location /flep/web/ {
            alias  D:/Dev/cygwin/work/vue/yn_flep_web/dist/;
            try_files $uri / /index.html;
        }
        location /flep/web/api/ {
            proxy_pass http://ynyd.ynicity.cn:9080/flep/web/api/;
        }
        location =html/weihu.html {
            alias  html/weihu.html;
        }
    }
}
```
