
## nginx 静态资源配置


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
        
        location / {
            root   html;
            index  index.html index.htm;
        }   
        location /xw-pad/{
            proxy_pass http://127.0.0.1:8085/xw-pad/;
            proxy_redirect default;
        }
        location /static/ {
            alias  D:/Eclipse/pad/csms/xw-static/src/main/resources/static/;
        }
    }
}

```









