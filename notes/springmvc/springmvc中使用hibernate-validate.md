
1、springmvc.xml配置

```
<!-- 国际化配置 -->    
<bean id="localeResolver" class="org.springframework.web.servlet.i18n.CookieLocaleResolver" />
<bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
    <property name="basenames">
        <list>
            <value>classpath:resource/ValidationMessages</value>
        </list>
    </property>
    <property name="useCodeAsDefaultMessage" value="true" />
</bean>
<!-- 注册验证器 -->
<mvc:annotation-driven validator="validator" />

<bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
    <property name="providerClass" value="org.hibernate.validator.HibernateValidator" />
    <!-- 这里配置将使用上面国际化配置的messageSource -->
    <property name="validationMessageSource" ref="messageSource" />
</bean>
```


2、资源文件

```
validation.length=字段的长度范围为{min}-{max}！
validation.not.null=字段不能为空！
```


3、被验证的bean

```
public class ValBean {
    
    
    /**
     * Bean Validation 中内置的 constraint       
     * @Null   被注释的元素必须为 null       
     * @NotNull    被注释的元素必须不为 null       
     * @AssertTrue     被注释的元素必须为 true       
     * @AssertFalse    被注释的元素必须为 false       
     * @Min(value)     被注释的元素必须是一个数字，其值必须大于等于指定的最小值       
     * @Max(value)     被注释的元素必须是一个数字，其值必须小于等于指定的最大值       
     * @DecimalMin(value)  被注释的元素必须是一个数字，其值必须大于等于指定的最小值       
     * @DecimalMax(value)  被注释的元素必须是一个数字，其值必须小于等于指定的最大值       
     * @Size(max=, min=)   被注释的元素的大小必须在指定的范围内       
     * @Digits (integer, fraction)     被注释的元素必须是一个数字，其值必须在可接受的范围内       
     * @Past   被注释的元素必须是一个过去的日期       
     * @Future     被注释的元素必须是一个将来的日期       
     * @Pattern(regex=,flag=)  被注释的元素必须符合指定的正则表达式       
     * Hibernate Validator 附加的 constraint       
     * @NotBlank(message =)   验证字符串非null，且长度必须大于0       
     * @Email  被注释的元素必须是电子邮箱地址       
     * @Length(min=,max=)  被注释的字符串的大小必须在指定的范围内       
     * @NotEmpty   被注释的字符串的必须非空       
     * @Range(min=,max=,message=)  被注释的元素必须在合适的范围内 
     */
    private Long id;

    @Max(value=20, message="{val.age.message}")   
    private Integer age;
    
    @NotBlank(message="{username.not.null}")
    @Length(max=6, min=3, message="{username.length}")
    private String username;

    @NotBlank(message="{pwd.not.null}")
    @Pattern(regexp="/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$/", message="密码必须是6~10位数字和字母的组合")
    private String password;
    
    
    @Pattern(regexp="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message="手机号格式不正确")
    private String phone;

    @Email(message="{email.format.error}")
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
```

4、controller使用

```
/**
	 * 保存
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "save",method = RequestMethod.POST)
	public String save(Model mode, @Valid GoodsType goodsType, BindingResult result) {
		if(result.hasErrors()){
			this.getErrors(mode,result);
	        return "errors/field";
		}
		//添加
		if(StringUtils.isIntegerBlank(goodsType.getTypeId())){
			goodsTypeService.saveNotNull(goodsType);
			logger.info("管理员添加了商品类型！");
		}else{
		//更新
			goodsTypeService.updateNotNull(goodsType);
			logger.info("管理员修改了商品类型！");
		}
		return "redirect:list";
	}
  
  /**
	 * validation
	 * @param model
	 * @param result
	 */
	public void getErrors(Model model, BindingResult result) {   
        List<FieldError> list = result.getFieldErrors();
        for (FieldError error : list) {
            System.out.println("error.getField():" + error.getField());
            System.out.println("error.getDefaultMessage():" + error.getDefaultMessage());
        }
        model.addAttribute("field",list.get(0).getField());
        model.addAttribute("message",list.get(0).getDefaultMessage());
    }
```

