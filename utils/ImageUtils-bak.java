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
	
	//根据配置文件获取服务器图片存放路径前缀
	private static String IMAGE_PREFIX = PropertiesUtils.getFileIO("savePicUrl", "properties/config.properties");
	

	/**
	 * 功能描述   文件类型判断
	 * @param filename
	 * @param allowType => image flash media file
	 * @return
	 */
	public static boolean isAllowFile(String filename,String allowType) {  
		// 定义允许上传的文件扩展名
		Map<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		// 检查扩展名
		String fileExt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
		if (Arrays.<String> asList(extMap.get(allowType).split(",")).contains(fileExt)) return true; 
        return false;  
    }
	
	
	/**
	 * 功能描述   保存图片
	 * @param path 保存图片的子目录
	 * @param file springMVC的MultipartFile
	 * @param bool 是否生成缩略图 true 是  false 否
	 * @return
	 */
	public static Map<String,String> saveImage(String path, MultipartFile file, boolean bool) {		
		// 构建文件目录 ，检查目录写权限
        File fileDir = new File(new File(IMAGE_PREFIX), path);
        if (!fileDir.exists()) {fileDir.mkdirs();}
 		if (!fileDir.canWrite()) {return null;}	
 		
        Map<String,String> result = new HashMap<String,String>();
        //生成缩略图并进行图片压缩
        if(bool){
        	MultipartFile temp = file;
        	result.put("small", generateFixedSizeImage(temp,path,100,100,0.8));
        	result.put("medium", generateFixedSizeImage(temp,path,400,400,0.8));
        	result.put("source", generateFixedSizeImage(temp,path,800,800,0.8));
        	temp = null;
        }else{
        	result.put("medium", generateScale(file,path,1f,0.8));
        }          
        return result; 
    }
	
	
	/**
	 * 功能描述：删除图片
	 * @param imageName 待删除图片名
	 */
    public static void deleteImage(String imageName) {
        File imageFile= new File(IMAGE_PREFIX+"/"+imageName);
        if (imageFile.exists()) {imageFile.delete();}
    }

    
    /**
     * 使用给定的图片生成指定大小的图片
     * @param file
     * @param imageName
     * @param width
     * @param height
     * @return
     */
    private static String generateFixedSizeImage(MultipartFile file, String path,
    		int width, int height, double quality){
        try {    
        	//拼接图片全路径
        	String imageName = path + getImagePrefix(width,height) + "." 
        					 + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);	
            
        	Thumbnails.of(file.getInputStream()).
            			size(width,height).
            			outputQuality(quality).
            			toFile(new File(IMAGE_PREFIX+imageName));
            return imageName;
        } catch (IOException e) {
        	logger.error("generateFixedSizeImage",e);
            return null;
        }
    }


    /**
     * 按比例缩放图片
     * @param file
     * @param imageName
     * @param scale
     * @param quality
     * @return
     */
    private static String generateScale(MultipartFile file, String path, double scale, double quality){    	
        try {
        	//拼接图片全路径
        	String imageName = path + DateUtils.formatDate(new Date(), "yyyyMMddhhmmss") + ((int)(Math.random()*(99999-10000)+10000)) + "." 
					 + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);	
        	
            Thumbnails.of(file.getInputStream()).
                    scale(scale). // 图片缩放80%, 不能和size()一起使用
                    outputQuality(quality). // 图片质量压缩80%
                    toFile(new File(IMAGE_PREFIX+imageName));   
            return imageName;
        } catch (IOException e) {
            logger.error("generateFixedSizeImage",e);
            return null;
        }
    }
    
    /**
     * 生成图片前缀策略
     * @param width
     * @param height
     * @return
     */
    private static String getImagePrefix(int width, int height){
    	return DateUtils.formatDate(new Date(), "yyyyMMddhhmmss") + ((int)(Math.random()*(99999-10000)+10000)) + "_" + width + "_" +height;   
    }
    
    
}
