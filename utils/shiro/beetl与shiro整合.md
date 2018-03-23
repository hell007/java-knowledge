## beetl与shiro整合

shrio 提供有jsp 标签，供在jsp 里使用，如果想在beetl中使用，有俩种方法，

一是beetl支持集成jsp页面，所以你可以在在jsp里使用shrio标签

另外，beetl 使用自定义函数写了shiro tag功能，你可以像使用shiro标签那样使用shiro

1.配置

	```
	<!-- beetl配置文件 -->
	<bean id="escapeFun" class="com.jie.utils.EscapeHtml"></bean>
	<bean id="beetlConfig" class="com.jie.admin.utils.BeetlConfiguration" init-method="init">
	 	<property name="functions">
	         <map>
	           <entry key="escape" value-ref="escapeFun"/>
	        </map>
	    </property>
	 	<property name="sharedVars">
	         <map>
	            <!-- 是否开发模式 -->
	           <entry key="isDev" value-type="boolean" value="true"/>
	          	<!-- 版本号，用于静态资源加载刷新缓存 -->
	           <entry key="version" value-type="double" value="1.0"/>
	        </map>
	    </property>
	</bean>
	
	<!-- beetl视图解析器 -->
	<bean id="viewResolver" class="org.beetl.ext.spring.BeetlSpringViewResolver">
	    <property name="contentType" value="text/html;charset=UTF-8"/>
	    <property name="prefix" value="/WEB-INF/views/"/>
	    <property name="suffix" value=".html"/>
	    <property name="order" value="1" />
	    <property name="config" ref="beetlConfig"/>
	</bean>
	```
	
2. BeetlConfiguration.java

3.ShiroExt.java	

4.使用

	${isDev  ? '11' : '22'}
				
	${version!}
	
	<%if(shiro.hasPermission(permission.code!) ){%>
		1111
	<%}else{%>
		2222
	<%}%>
