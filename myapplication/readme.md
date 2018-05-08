#

## 小计点


#### 商品详情页只可以通过推广的活动页面进入

https://www.9ji.com/product/58206.html 

https://www.9ji.com/product/58206-0-0-0.html -0-0-0

http://www.9ji.com/list/2-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0- 2-0-0-2.html

筛选参数

销量/价格/上架时间 - 排序 - 有货 - 分页

#### beetl与shiro整合

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

####  tk.mybatis.mapper.common.Mapper  多表联合查询

1. 拓展mapper*.xml文件 参看userMapper.xml

2. 拓展userMapper.java

```
public interface UserMapper {  
    List<User> getUserAddress(String id);  
}
```

3. dao接口扩展 

```
public interface UserDao extends UserMapper { }

```
4. 使用

```
public class UserServiceImpl implements UserService {  
  
    @Resource  
    public UserDao userDao;  
    
    @Override  
    public List<User> getUserAddress(String id) {  
        return userDao.getUserAddress();  
    }  
  
}
```

> 总结： 

	1.多表查询就是使用比较老的xml映射查询
	
	2.使用jpa注解










