

##### 1.springmvc4.3.13整合swagger2

1.添加依赖

```
//swagger
compile 'io.springfox:springfox-swagger2:2.8.0'
//compile 'io.springfox:springfox-swagger-ui:2.8.0'

```
将https://github.com/swagger-api/swagger-ui   dist 里面的资源复制到  apidoc/静态资源文件下

2.spring-mvc配置

```
<!-- swagger2静态资源 -->
<mvc:resources location="/apidoc/" mapping="/apidoc/**" />
```

3.创建SwaggerConfig.java

```
@Configuration //必须存在
@EnableSwagger2 //必须存在    -> 启用Swagger2，毕竟SpringFox的核心依旧是Swagger
@EnableWebMvc //必须存在 ->  配置注解
public class SwaggerConfig {
  ....
}
```

4.替换apidoc/index.html的url

```
http://localhost:8080/web/v2/api-docs  //需要http://localhost:8080/web/这个，否则Failed to load API definition.

```

5.访问  http://localhost:8080/web/apidoc/index.html



