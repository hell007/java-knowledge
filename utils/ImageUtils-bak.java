package com.jie.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.coobird.thumbnailator.Thumbnails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: ImageUtils
 * @Description: 图片处理工具类. <br/>
 * @author jie
 * @date 2017年12月28日
 */
public class ImageUtils {
	
	private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);
	
     
	/**
	 * 功能描述   文件类型判断
	 * @param fileName
	 * @param allowType => image flash media file
	 * @return
	 */
	public static boolean isAllowFile(String fileName,String allowType) {  
		// 定义允许上传的文件扩展名
		Map<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		boolean isAllow = false;
		// 检查扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if (Arrays.<String> asList(extMap.get(allowType).split(",")).contains(fileExt)) {
			isAllow = true;
		} 
        return isAllow;  
    }
	
	
	/**
	 * 功能描述   保存图片 
	 * @param filePath 保存文件的子目录
	 * @param file    文件文件
	 * @param isThumb
	 * @return 返回文件存放信息	
	 */
	public static Map<String,String> saveFile(String filePath, MultipartFile file, boolean isThumb) {	   	
    	// 根据配置文件获取服务器图片存放路径
        String saveFilePath = PropertiesUtils.getFileIO("savePicUrl", "properties/config.properties");

        // 构建文件目录 
        File fileDir = new File(new File(saveFilePath), filePath);
        if (!fileDir.exists()) {fileDir.mkdirs();}
        // 检查目录写权限
 		if (!fileDir.canWrite()) {return null;}	
        // 构建源图文件名
        String filename = filePath + DateUtils.formatDate(new Date(), "yyyyMMddhhmmss")
    			+ ((int)(Math.random()*(99999-10000)+10000)) + "." 
        		+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String result = saveFilePath+filename;
        
        Map<String,String> fileMap = new HashMap<String,String>();
        //生成缩略图并进行图片压缩
        // S:100 M:400 B:800
        if(isThumb){
        	String thumb4 = generateFixedSizeImage(file,result,400,400,0.8);
        	if(thumb4!=null){fileMap.put("thumbM", thumb4);}
        	String thumb1 = generateFixedSizeImage(file,result,100,100,0.8);
        	if(thumb1!=null){fileMap.put("thumbS", thumb1);}
        	String thumb8 = generateFixedSizeImage(file,result,800,800,0.8);      	
        	if(thumb8!=null){fileMap.put("thumbB", thumb8);}
        }else{
        	String thumb = generateScale(file,result,1f,0.8);
        	if(thumb!=null){fileMap.put("thumbB", thumb);}
        }          
        return fileMap; 
    }
	
	
	/**
     * 功能描述：删除图片
     * @param oldPicName 修改之前的文件名
     */
    public static void deleteFile(String oldPicName) {
        // 根据配置文件获取服务器图片存放路径
    	String saveFilePath = PropertiesUtils.getFileIO("savePicUrl", "properties/config.properties");
        // 构建文件目录 
        File file= new File(saveFilePath+"/"+oldPicName);
        if (file.exists()) {file.delete();}
    }

    
    /**
     * 使用给定的图片生成指定大小的图片
     * @param file
     * @param result
     * @param width
     * @param height
     * @return
     */
    private static String generateFixedSizeImage(MultipartFile file, String result, 
    		int width, int height, double quality){
        try {
            Thumbnails.of(file.getInputStream()).
            			size(width,height).
            			outputQuality(quality).
            			toFile(new File(result));
            return result;
        } catch (IOException e) {
        	logger.error("generateFixedSizeImage",e);
            return null;
        }
    }


    /**
     * 按比例缩放图片
     * @param file
     * @param result
     * @param scale
     * @param quality
     * @return
     */
    private static String generateScale(MultipartFile file, String result, 
    		double scale, double quality){    	
        try {
            Thumbnails.of(file.getInputStream()).
                    scale(scale). // 图片缩放80%, 不能和size()一起使用
                    outputQuality(quality). // 图片质量压缩80%
                    toFile(new File(result));   
            return result;
        } catch (IOException e) {
            logger.error("generateFixedSizeImage",e);
            return null;
        }
    }
    
    
}
