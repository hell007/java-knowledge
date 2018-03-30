

## nginx配置url重写


```
location /jie-web/{
    rewrite ^/jie-web/([a-zA-Z0-9|_]+).html$ /jie-web/$1.html?pageNum=$1 break;
    rewrite ^/jie-web/list/([a-zA-Z0-9|_|-]+).html$ /jie-web/list/index.html?filterParams=$1 break;
    rewrite ^/jie-web/product/([a-zA-Z0-9|_|-]+).html$ /jie-web/product/index.html?goodsSn=$1 break;
    rewrite ^/jie-web/news/([a-zA-Z0-9|_|-]+).html$ /jie-web/news/detail.html?id=$1 break;
    rewrite ^/jie-web/([a-zA-Z0-9|_|-]+)/([a-zA-Z0-9|_|-]+).html$ /jie-web/$1/$2.html?pageNum=$2 break ;
    proxy_pass  http://127.0.0.1:3001/jie-web/;
    proxy_redirect default;
}
```

[参考文档](https://www.cnblogs.com/likwo/p/6513117.html)
