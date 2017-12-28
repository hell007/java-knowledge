package com.self.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Properties;


/**
 * @ClassName: PropertiesUtils
 * @Description: 对文件的读取、书写 类. <br/>
 * @author jie
 * @date 2017年12月28日 
 * 注意：文件所在的目录不同，获取文件的方式不同  wrong?
 */
@SuppressWarnings("all")
public class PropertiesUtils {


	/**
	 * 读文件
	 * @param name
	 * @param fileURL
	 * @return
	 */
    public static String getFileIO(String name,String fileURL){
         Properties prop = new Properties();
         //PropertiesUtil.class.getResourceAsStream(fileURL); wrong?
         InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileURL);
         try {
            prop.load(in);
            return prop.getProperty(name);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    

    
    /**
     * 写文件
     * @param key
     * @param value
     * @param fileURL
     */
    public static void writeData(String key, String value,String fileURL) {
        Properties prop = new Properties();
        InputStream fis = null;
        OutputStream fos = null;
        try {
        	//PropertiesUtil.class.getResource(fileURL) wrong?
        	java.net.URL  url = PropertiesUtils.class.getClassLoader().getResource(fileURL);
            File file = new File(url.toURI());
            if (!file.exists())
                file.createNewFile();
            fis = new FileInputStream(file);
            prop.load(fis);
            fis.close();//一定要在修改值之前关闭fis
            fos = new FileOutputStream(file);
            prop.setProperty(key, value);
            prop.store(fos, "Update '" + key + "' value");
            fos.close();
            
        } catch (IOException e) {
            System.err.println("Visit " + fileURL + " for updating "
            + value + " value error");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        finally{
            try {
                fos.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } 
    
    
    /**
     * main
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        //PropertiesUtil.getFileIO("name", "gxyTest.properties");
    	PropertiesUtils.writeData("name", "microsoft", "gxyTest.properties");
        System.out.println(PropertiesUtils.getFileIO("name", "gxyTest.properties"));
    }

}
