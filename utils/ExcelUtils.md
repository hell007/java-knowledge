##  java poi对excel的导入导出操作

1.依赖  

//poi
"poi"                   : "org.apache.poi:poi:3.9",
"poi-ooxml"             : "org.apache.poi:poi-ooxml:3.9",

2.使用

export

```
/**
     * 导出excel
     * @param req
     * @param res
     * @param fileName
     * @param sheetName
     * @param headers 表头
     * @param properties 表头对应obj属性名(注：与表头字段顺序一致)
     * @param obj 不定参数，现在只接受一个excel要显示的Model的一个List
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void download(HttpServletRequest req, HttpServletResponse res, 
    	String fileName, String sheetName, String[] headers, String[] properties, Object... obj) {
      
  }
  ```
  
 （1）、 设置下载请求头和文件名 ：由于浏览器的不同，会存在中文乱码问题，需要设置下载请求头和文件名
 
 （2）、声明一个工作薄，生成一个表格，设置字体样式；创建表头,并设置样式、填充内容
 
 （3）、// 利用Java反射机制调用get方法获取字段对应的值,这里主要是处理Date,Boolean等需求
 
  ```
  Object target = getMethod.invoke(o,new Object[] {}); 
  ```
  
  （4）、以输出流的方式写入工作簿
  
  
  import
  
  ```
  /**
     * 导入excel
     * 注意导入的excel的字段顺序与javabean的字段要一一对应，否则反射拿到的类型不匹配报错
     * @param request
     * @param filePath 文件路径
     * @param clazz 传入类对象 （字段顺序与导入的excel列顺序一致）
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	public static <T> List<T> importExcel(HttpServletRequest request, String filePath, Class clazz) {
  
  ```
  
  （1）、根据文件路径创建工作簿，创建工作表sheet，获取sheet中数据的行数，获取表头单元格个数
  
  （2）、 利用反射，给JavaBean的属性进行赋值
   
   ```
   Field[] fields = t.getClass().getDeclaredFields();
   ...
   String type = field.getType().getName(); //得到此属性的类型 
   ```
   （3）、根据javaBean的属性类型，对excel内容进行类型装换
   
   >注意导入的excel的字段顺序与javabean的字段要一一对应，否则反射拿到的类型不匹配报错
   
   >excel导入的数字默认是double类型，,byte,short,int (1=》1.0)需要进行转换，否则与javaBean类型不匹配报错
   
   （4）、创建class，添加list,进入数据库insert等操作
  
  
  
  
  
