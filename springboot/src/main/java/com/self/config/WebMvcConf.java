package com.self.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebMvcConf extends  WebMvcConfigurationSupport{
	
	private static final Logger logger = LoggerFactory.getLogger(WebMvcConf.class);
	
	@Value("${web.staticUrl}")
    String webStaticUrl;
	
	@Value("${web.staticVersion}")
    String staticVersion;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将所有/static/** 访问都映射到classpath:/static/ 目录下
        registry.addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
        
        registry.addResourceHandler("/favicon.ico")
        .addResourceLocations("/")
        .setCachePeriod(0);

        super.addResourceHandlers(registry);
    }
    
    
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        logger.info("自定义json格式化");
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        converter.setObjectMapper(objectMapper);
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        converter.setSupportedMediaTypes(mediaTypes);
        converters.add(converter);

        super.extendMessageConverters(converters);
    }
    
    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.setAttribute("webStaticUrl", webStaticUrl);
        servletContext.setAttribute("staticVersion", staticVersion);
        super.setServletContext(servletContext);
    }
    
  
  
}
