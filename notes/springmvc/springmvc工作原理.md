
## SpringMVC工作原理

 SpringMvc是基于过滤器对servlet进行了封装的一个框架，我们使用的时候就是在web.xml文件中配置DispatcherServlet类；
 
 SpringMvc工作时主要是通过DispatcherServlet管理接收到的请求并进行处理。
 
具体执行流程如下：
   
#### SpringMVC工程流程描述

1. 用户向服务器发送请求，请求被Spring 前端控制Servelt DispatcherServlet捕获；

2. DispatcherServlet对请求URL进行解析，得到请求资源标识符（URI）。
然后根据该URI，调用HandlerMapping获得该Handler配置的所有相关的对象（包括Handler对象以及Handler对象对应的拦截器），
最后以HandlerExecutionChain对象的形式返回；

3. DispatcherServlet根据获得的Handler，选择一个合适的HandlerAdapter。
（附注：如果成功获得HandlerAdapter后，此时将开始执行拦截器的preHandler(...)方法）

4. 提取Request中的模型数据，填充Handler入参，开始执行Handler（Controller)。 
在填充Handler的入参过程中，根据你的配置，Spring将帮你做一些额外的工作：

      HttpMessageConveter： 将请求消息（如Json、xml等数据）转换成一个对象，将对象转换为指定的响应信息
      
      数据转换：对请求消息进行数据转换。如String转换成Integer、Double等
      
      数据根式化：对请求消息进行数据格式化。 如将字符串转换成格式化数字或格式化日期等
      
      数据验证： 验证数据的有效性（长度、格式等），验证结果存储到BindingResult或Error中
      
5.  Handler执行完成后，向DispatcherServlet 返回一个ModelAndView对象；

6.  根据返回的ModelAndView，选择一个适合的ViewResolver（必须是已经注册到Spring容器中的ViewResolver)返回给DispatcherServlet ；

7. ViewResolver 结合Model和View，来渲染视图

8. 将渲染结果返回给客户端


```

package com.self.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//告诉DispatcherServlet相关的容器， 这是一个Controller， 管理好这个bean哦
@Controller
//类级别的RequestMapping，告诉DispatcherServlet由这个类负责处理以跟URL。
//HandlerMapping依靠这个标签来工作
@RequestMapping("/hello")
public class HelloMvcController {
	
	//方法级别的RequestMapping， 限制并缩小了URL路径匹配，同类级别的标签协同工作，最终确定拦截到的URL由那个方法处理
	@RequestMapping("/mvc")
	public String helloMvc() {
		
		//视图渲染，/WEB-INF/jsps/home.jsp
		return "home";
	}

}

```
