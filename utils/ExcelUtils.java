package com.jie.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jie.model.Admin;


/**
 * @ClassName: ExcelUtils
 * @Description: 导入导出工具类. <br/>
 * @author jie
 * @date 2017年11月28日
 */
public class ExcelUtils {
	
	
	private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);


	/**
	 * 
	 * @param req
	 * @param res
	 * @param fileName 文件名
	 * @param sheetName 表名
	 * @param headers 表头（注：表头定义的字段的与obj对应的Model定义的字段顺序必须一致）
	 * @param obj 不定参数，现在只接受一个excel要显示的Model的一个List
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static void download(HttpServletRequest req, HttpServletResponse res, 
    		String fileName, String sheetName, String[] headers, Object... obj) {
        // 设置下载请求头和文件名
        setResponseHeader(req, res, fileName);

        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();

        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(40);

        // 表格字体样式
        XSSFCellStyle[] styles = createStyle(workbook);
        XSSFCellStyle style = styles[0];
        XSSFCellStyle style2 = styles[1];

        XSSFRow row = sheet.createRow(0);

        // 创建表头,并设置样式、填充内容
        for (short i = 0; i < headers.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);

            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

		List<Object> list = (List<Object>) obj[0]; // 获取目标参数（即需要导出的数据）

        for (int j = 0; j < list.size(); j++) {
            XSSFRow r = sheet.createRow(j + 1);
            row.setHeightInPoints(20);

            Object o = list.get(j);
            Field[] fields = o.getClass().getDeclaredFields(); // 获取对象的所有字段

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldName = field.getName(); // 得到某一字段的字段名
                String getMethodName = "get" // 拼接该字段对应的get方法的方法名
                     +fieldName.substring(0, 1).toUpperCase() +
                    fieldName.substring(1);
                Class cls = o.getClass();

                try {
                    Method getMethod = cls.getMethod(getMethodName,new Class[] {});

                    try {
                    	// 利用Java反射机制调用get方法获取字段对应的值
                        Object target = getMethod.invoke(o,new Object[] {}); 
                        XSSFCell c = r.createCell(i);
                        c.setCellStyle(style2);

                        if (target instanceof Integer) {
                            c.setCellValue((Integer) target);
                        } else if (target instanceof String) {
                            c.setCellValue(target.toString());
                        } else if (target instanceof Date) {
                        	// 如果target为Date类型，则按该规则格式化
                            c.setCellValue(DateUtils.formatDate((Date) target, "yyyy-MM-dd  HH:mm:ss")); 
                        } else if (target instanceof Byte) {
                            c.setCellValue((Byte) target);
                        } else if(target instanceof Short) {
                            c.setCellValue((Short) target);
                        } else if(target instanceof Double) {
                        	// 如果target为Double类型的数据，则默认保留两位小数
                            c.setCellValue(decimal((Double) target)); 
                        } else if (target instanceof Long) {
                            c.setCellValue((Long) target);
                        } else if (target instanceof BigDecimal) {
                            c.setCellValue(decimal(((BigDecimal) target).doubleValue()));
                        }
                    } catch (IllegalAccessException e1) {
                        logger.error("download e1",e1);
                    } catch (IllegalArgumentException e2) {
                    	logger.error("download e2",e2);
                    } catch (InvocationTargetException e3) {
                    	logger.error("download e3",e3);
                    }
                } catch (NoSuchMethodException e4) {
                	logger.error("download e4",e4);
                } catch (SecurityException e5) {
                	logger.error("download e5",e5);
                }
            }
        }

        OutputStream ouputStream;

        try {
            ouputStream = res.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (IOException e) {
        	logger.error("download e",e);
        }
    }


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
        // 设置下载请求头和文件名
        setResponseHeader(req, res, fileName);

        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();

        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(40);
        
        // 表格字体样式
        XSSFCellStyle[] styles = createStyle(workbook);
        XSSFCellStyle style = styles[0];
        XSSFCellStyle style2 = styles[1];

        XSSFRow row = sheet.createRow(0);

        // 创建表头,并设置样式、填充内容
        for (short i = 0; i < headers.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);

            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        if (ArrayUtils.isNotEmpty(properties)) {
            List<Object> list = (List<Object>) obj[0]; // 获取目标参数（即需要导出的数据）

            for (int j = 0; j < list.size(); j++) {
                XSSFRow r = sheet.createRow(j + 1);
                row.setHeightInPoints(20);

                Object o = list.get(j);

                for (int i = 0; i < properties.length; i++) {
                    String property = properties[i];
                    String getMethodName = "get" // 拼接该字段对应的get方法的方法名
                         +property.substring(0, 1).toUpperCase() +
                        property.substring(1);
                    Class cls = o.getClass();

                    try {
                        Method getMethod = cls.getMethod(getMethodName,new Class[] {});

                        try {
                        	// 利用Java反射机制调用get方法获取字段对应的值
                            Object target = getMethod.invoke(o,new Object[] {}); 
                            XSSFCell c = r.createCell(i);
                            c.setCellStyle(style2);
                            
                            c.setCellValue(target.toString());
                            
                            //其实这里只要处理时间即可
                            if (target instanceof Integer) {
                                c.setCellValue((Integer) target);
                            } else if (target instanceof String) {
                                c.setCellValue(target.toString());
                            } else if (target instanceof Date) {
                            	// 如果target为Date类型，则按该规则格式化
                                c.setCellValue(DateUtils.formatDate((Date) target, "yyyy-MM-dd  HH:mm:ss")); 
                            } else if (target instanceof Byte) {
                                c.setCellValue((Byte) target);
                            } else if(target instanceof Short) {
                                c.setCellValue((Short) target);
                            } else if(target instanceof Double) {
                            	// 如果target为Double类型的数据，则默认保留两位小数
                                c.setCellValue(decimal((Double) target)); 
                            } else if (target instanceof Long) {
                                c.setCellValue((Long) target);
                            } else if (target instanceof BigDecimal) {
                                c.setCellValue(decimal(((BigDecimal) target).doubleValue()));
                            }
                        } catch (IllegalAccessException e1) {
                            logger.error("download e1",e1);
                        } catch (IllegalArgumentException e2) {
                        	logger.error("download e2",e2);
                        } catch (InvocationTargetException e3) {
                        	logger.error("download e3",e3);
                        }
                    } catch (NoSuchMethodException e4) {
                    	logger.error("download e4",e4);
                    } catch (SecurityException e5) {
                    	logger.error("download e5",e5);
                    }
                }
            }
        }

        OutputStream ouputStream;

        try {
            ouputStream = res.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (IOException e) {
        	logger.error("download e",e);
        }
    }


    /**
     * 导入excel
     * 注意导入的excel的字段顺序与javabean的字段要一一对应，否则反射拿到的类型不匹配报错
     * @param clazz 传入类对象 （字段顺序与导入的excel列顺序一致）
     * @param is 文件流
     * @param excelFileName 文件名称
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	public static <T> List<T> importExcel(Class clazz, InputStream is, String excelFileName) {
        List<T> list = new ArrayList<T>();
        T t = (T) create(clazz);

        try {
            // 创建工作簿
            Workbook workbook = createWorkbook(is, excelFileName);

            // 创建工作表sheet
            Sheet sheet = getSheet(workbook, 0);

            // 获取sheet中数据的行数
            int rows = sheet.getPhysicalNumberOfRows();

            // 获取表头单元格个数
            int cells = sheet.getRow(0).getPhysicalNumberOfCells();

            // 利用反射，给JavaBean的属性进行赋值
            Field[] fields = t.getClass().getDeclaredFields();

            for (int i = 1; i < rows; i++) { // 第一行为标题栏，从第二行开始取数据

                Row row = sheet.getRow(i);
                int index = 0;

                while (index < cells) {
                    Cell cell = row.getCell(index);

                    if (null == cell) {
                        cell = row.createCell(index);
                    }

                    cell.setCellType(Cell.CELL_TYPE_STRING);

                    String value = cell.getStringCellValue();
                    Field field = fields[index];
                    field.setAccessible(true); //设置属性可访问

                    String type = field.getType().getName(); //得到此属性的类型 

                    if (StringUtils.isEmpty(value)) {
                        field.set(t, null);
                    } else if (type.endsWith("String")) {
                        field.set(t, value);
                    } else if (type.endsWith("Byte")) {
                        field.set(t, toByte(Double.parseDouble(value)));
                    } else if (type.endsWith("Short")) {
                        field.set(t, toShort(Double.parseDouble(value)));
                    } else if (type.endsWith("int") || type.endsWith("Integer")) {
                        field.set(t,toInteger(Double.parseDouble(value)));
                    } else if (type.endsWith("double") || type.endsWith("Double")) {
                        field.set(t, Double.parseDouble(value));
                    } else if (type.endsWith("BigDecimal")) {
                        field.set(t, new BigDecimal(value));
                    } else if (type.endsWith("boolean") || type.endsWith("Boolean")) {
                        field.set(t, value.equals("1") ? true : false);
                    } else if (type.endsWith("long") || type.endsWith("Long")) {
                        field.set(t, Long.parseLong(value));
                    } else if(type.endsWith("date") || type.endsWith("Date")){
                        field.set(t, DateUtils.parseDate(value,"yyyy-MM-dd HH:mm:ss"));
                    }

                    index++;
                }

                list.add(t);
                t = (T) create(clazz); // 重新创建一个对象
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                is.close(); // 关闭流
            } catch (Exception e2) {
                logger.error(e2.getMessage());
            }
        }

        return list;
    }


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
        List<T> list = new ArrayList<T>();
        T t = (T) create(clazz);

        //路径
        File file = new File(filePath);
        InputStream is = null;

        try {
            is = new FileInputStream(file);

            // 创建工作簿
            Workbook workbook = createWorkbook(is, file.getName());

            // 创建工作表sheet
            Sheet sheet = getSheet(workbook, 0);

            // 获取sheet中数据的行数
            int rows = sheet.getPhysicalNumberOfRows();

            // 获取表头单元格个数
            int cells = sheet.getRow(0).getPhysicalNumberOfCells();

            // 利用反射，给JavaBean的属性进行赋值
            Field[] fields = t.getClass().getDeclaredFields();

            for (int i = 1; i < rows; i++) { // 第一行为标题栏，从第二行开始取数据

                Row row = sheet.getRow(i);
                int index = 0;

                while (index < cells) {
                    Cell cell = row.getCell(index);

                    if (null == cell) {
                        cell = row.createCell(index);
                    }
                            
                    cell.setCellType(Cell.CELL_TYPE_STRING);

                    String value  = cell.getStringCellValue();
                    Field field = fields[index];
                    field.setAccessible(true); //设置属性可访问

                    String type = field.getType().getName(); //得到此属性的类型 
                    
                    logger.info("field=="+field+";type=="+type);
                    
                    if (StringUtils.isEmpty(value)) {
                        field.set(t, null);
                    } else if (type.endsWith("String")) {
                        field.set(t, value);
                    } else if (type.endsWith("Byte")) {
                        field.set(t, toByte(Double.parseDouble(value)));
                    } else if (type.endsWith("Short")) {
                        field.set(t, toShort(Double.parseDouble(value)));
                    } else if (type.endsWith("int") || type.endsWith("Integer")) {
                        field.set(t,toInteger(Double.parseDouble(value)));
                    } else if (type.endsWith("double") || type.endsWith("Double")) {
                        field.set(t, Double.parseDouble(value));
                    } else if (type.endsWith("BigDecimal")) {
                        field.set(t, new BigDecimal(value));
                    } else if (type.endsWith("boolean") || type.endsWith("Boolean")) {
                        field.set(t, value.equals("1") ? true : false);
                    } else if (type.endsWith("long") || type.endsWith("Long")) {
                        field.set(t, Long.parseLong(value));
                    } else if(type.endsWith("date") || type.endsWith("Date")){
                        field.set(t, DateUtils.parseDate(value,"yyyy-MM-dd HH:mm:ss"));
                    }

                    index++;
                }

                list.add(t);
                t = (T) create(clazz); // 重新创建一个对象
            }
        } catch (Exception e) {    	
            logger.error("importExcel",e);
        } finally {
            try {
                is.close(); // 关闭流
            } catch (Exception e2) {
                logger.error("importExcel",e2);
            }
        }

        return list;
    }
    
    

    /**
     * 设置下载请求头和文件名
     * @param request
     * @param response
     * @param fileName 文件名称
     */
    private static void setResponseHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {
            try {
                if (request.getHeader("USER-AGENT").toLowerCase().contains("firefox")) {
                    response.setCharacterEncoding("utf-8");
                    response.setHeader("content-disposition","attachment;filename=" +new String(fileName.getBytes(), "ISO8859-1") + ".xls");
                } else {
                    response.setCharacterEncoding("utf-8");
                    response.setHeader("content-disposition","attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
                }
            } catch (Exception e) {
            	logger.error("setResponseHeader e",e);
                e.printStackTrace();
            }
            response.setContentType("application/msexcel;charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
        } catch (Exception ex) {
        	logger.error("setResponseHeader ex",ex);
            ex.printStackTrace();
        }
    }
    
    

    /**
     * 设置样式
     * @param workbook
     * @return
     */
    private static XSSFCellStyle[] createStyle(XSSFWorkbook workbook) {
        XSSFCellStyle[] styles = new XSSFCellStyle[2];

        // 设置表格默认列宽度为15个字节
        // 设置单元格样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        // 设置单元格字体
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 设置另一个样式
        XSSFCellStyle style2 = workbook.createCellStyle();
        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        // 生成另一个字体
        XSSFFont font2 = workbook.createFont();
        font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        styles[0] = style;
        styles[1] = style2;

        return styles;
    }
    

    /**
     * 创建一个工作簿
     * @param is
     * @param excelFileName
     * @return
     * @throws IOException
     */
    private static Workbook createWorkbook(InputStream is, String excelFileName) throws IOException {
        if (excelFileName.endsWith(".xls")) {
            return new HSSFWorkbook(is);
        } else if (excelFileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(is);
        }
        return null;
    }
    
    
    /**
     * 根据sheet索引号获取对应的sheet
     * @param workbook
     * @param sheetIndex
     * @return
     */
    private static Sheet getSheet(Workbook workbook, int sheetIndex) {
        return workbook.getSheetAt(0);
    }
    


    /**
    * 保留两位小数
    * @param v
    * @return
    */
    public static double decimal(double v) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, 2, 1).doubleValue();
    }
   
    /**
     * 解决excel integer import 是 double  1 => 1.0
     * @param value
     * @return
     */
    public static Integer toInteger(double value) {  
    	DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数  
        return Integer.parseInt(df.format(value)); 
    }
    
    public static Byte toByte(double value) {  
    	DecimalFormat df = new DecimalFormat("######0");
        return Byte.parseByte(df.format(value)); 
    }
    
    public static Short toShort(double value) {  
    	DecimalFormat df = new DecimalFormat("######0");  
        return Short.parseShort(df.format(value)); 
    }

    
    /**
     * 构建对象
     * @param clazz
     * @return
     */
    @SuppressWarnings({ "unchecked", "hiding" })
	public static <T> T create(Class<T> clazz) {
        Object t = null;
        try {
            t = clazz.newInstance();
        } catch (Exception e) {
        	logger.error("create e",e);
            e.printStackTrace();
        }
        return (T) t;
    }
    
    
    public static void main(String[] args) {
    	
    	/*
         *导出
         List<Admin> list = adminService.select(null);
         String headers[] = new String[]{"编号","名称","密码","邮箱","手机号","状态","创建时间","登录时间"};
         String properties[] = new String[]{"id","name","password","email","mobile","status","createTime","loginTime"};
         ExcelUtils.download(request, response, "管理员列表", "sheet", headers, properties, list);
         */
    	
        
    	/**
    	 * 注意导入的excel的字段顺序与javabean的字段要一一对应，否则反射拿到的类型不匹配报错
    	 */
    	List<Admin> list = importExcel(null,"F:/readExcel.xlsx",Admin.class);
        System.out.println("list中的数据打印出来");
        for(Admin s : list){
        	System.out.println("id="+s.getId());
        	System.out.println("name="+s.getName());
        	System.out.println("mobile="+s.getMobile());
        	System.out.println("email="+s.getEmail());
        	System.out.println("ip="+s.getIp());
        	System.out.println("status="+s.getStatus());
        	System.out.println("CreateTime="+ DateUtils.formatDate(s.getCreateTime(),"yyyy-MM-dd HH:mm:s"));
        }
        
    }
    
    
}
