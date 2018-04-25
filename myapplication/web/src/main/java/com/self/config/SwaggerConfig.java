package com.self.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration //必须存在
@EnableSwagger2 //必须存在    -> 启用Swagger2，毕竟SpringFox的核心依旧是Swagger
@EnableWebMvc //必须存在 ->  配置注解
public class SwaggerConfig {
	
	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.self.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("springmvc整合swagger2构建api文档")
                .description("简单优雅的restfun风格")
                //服务条款网址
                //.termsOfServiceUrl("http://jie.com")
                .version("1.0")
                //.contact(new Contact("帅呆了", "url", "email"))
                .build();
    }
    
}
