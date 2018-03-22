package com.jie.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: ImageUtils
 * @Description: 缩略图、图片水印图、文字水印图 类. <br/>
 * @author jie
 * @date 2017年11月28日
 */
public class ImageUtils {
	
	private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);
	
	private static int width;

	private static int height;

	private static int scaleWidth;

	private static double support = (double) 3.0;

	private static double PI = (double) 3.14159265358978;

	private static double[] contrib;

	private static double[] normContrib;

	private static double[] tmpContrib;

	private static int nDots;

	private static int nHalfDots;
	
	
	/**
	 * 生成缩略图，返回路径
	 * @author wzh
	 * @param filePath
	 * @param file
	 * @param saveFilePath
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public static String getThumImage(String filePath, MultipartFile file, String saveFilePath,int size) throws IOException{
		//尺寸处理
		int w=0;
		int h=0;
		switch(size){
			case 1:
				w=100;
				h=100;
				break;
			case 2:
				w=200;
				h=200;
				break;
			case 4:
				w=400;
				h=400;
				break;
			case 8:
				w=800;
				h=800;
				break;
			default:
				w=50;
				h=50;
		}
		//缩略图名称 
        String fileThumName = filePath + DateUtils.formatDate(new Date(), "yyyyMMddhhmmss")
    			+ ((int)(Math.random()*(99999-10000)+10000)) + "_"+ w +"x_"+ h + "." 
        		+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        //生成缩略图
		BufferedImage srcBufferImage = null;
		srcBufferImage = ImageIO.read(file.getInputStream());
		BufferedImage scaledImage = ImageUtils.imageZoomOut(srcBufferImage, w, h); 
		FileOutputStream out = new FileOutputStream(new File(saveFilePath+fileThumName));
		ImageIO.write(scaledImage, "png", out);
		out.close();
		return fileThumName;
	}
	
	
	/**
	 * 生成压缩图，返回路径
	 * @param filePath
	 * @param file
	 * @param saveFilePath
	 * @return
	 * @throws IOException
	 */
	public static String getCompressedImage(String filePath, MultipartFile file, String saveFilePath) throws IOException{	
		//压缩图名称
        String fileCompressedName = filePath + DateUtils.formatDate(new Date(), "yyyyMMddhhmmss")
    			+ ((int)(Math.random()*(99999-10000)+10000)) + "." 
        		+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        //生成压缩图
		BufferedImage srcBufferImage = null;
		srcBufferImage = ImageIO.read(file.getInputStream());
		BufferedImage scaledImage = ImageUtils.imageZoomOut(srcBufferImage, srcBufferImage.getWidth(), srcBufferImage.getHeight()); 
		FileOutputStream out = new FileOutputStream(new File(saveFilePath+fileCompressedName));
		ImageIO.write(scaledImage, "png", out);
		out.close();
		return fileCompressedName;
	}
	
	
	
	/**
     * 图片水印
     * 
     * @param imgPath 待处理图片
     * @param markPath 水印图片
     * @param x 水印位于图片左上角的 x 坐标值
     * @param y 水印位于图片左上角的 y 坐标值
     * @param alpha 水印透明度 0.1f ~ 1.0f
     * 
     */
	public static void waterMark(String imgPath, String markPath, int x, int y, float alpha) {
        try {
            // 加载待处理图片文件
            Image img = ImageIO.read(new File(imgPath));
            BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(img, 0, 0, null);

            // 加载水印图片文件
            Image src_biao = ImageIO.read(new File(markPath));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(src_biao, x, y, null);
            g.dispose();

            // 保存处理后的文件
            /*FileOutputStream out = new FileOutputStream(imgPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();*/
            //注释代码更改
            String formatName = imgPath.substring(imgPath.lastIndexOf(".") + 1);
            ImageIO.write(image,formatName, new File(imgPath));
            
        } catch (Exception e) {
        	logger.error("waterMark e",e);
            e.printStackTrace();
        }
    }
	
	
	/**
     * 文字水印
     * 
     * @param imgPath 待处理图片
     * @param text 水印文字
     * @param font 水印字体信息
     * @param color 水印字体颜色
     * @param x 水印位于图片左上角的 x 坐标值
     * @param y 水印位于图片左上角的 y 坐标值
     * @param alpha 水印透明度 0.1f ~ 1.0f
     */
	public static void textMark(String imgPath, String text, Font font, Color color, int x, int y, float alpha) {
        try {
            Font Dfont = (font == null) ? new Font("宋体", 20, 13) : font;
            Image img = ImageIO.read(new File(imgPath));

            BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();

            g.drawImage(img, 0, 0, null);
            g.setColor(color);
            g.setFont(Dfont);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawString(text, x, y);
            g.dispose();
            
            /*FileOutputStream out = new FileOutputStream(imgPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();*/
            //注释代码更改
            String formatName = imgPath.substring(imgPath.lastIndexOf(".") + 1);
            ImageIO.write(image,formatName, new File(imgPath));
        } catch (Exception e) {
        	logger.error("textMark e",e);
        	e.printStackTrace();
        }
    }
	
	
	
	/**
	 * imageZoomOut
	 * @param srcBufferImage
	 * @param w
	 * @param h
	 * @return
	 */
	private static BufferedImage imageZoomOut(BufferedImage srcBufferImage, int w, int h) {
		width = srcBufferImage.getWidth();
		height = srcBufferImage.getHeight();
		scaleWidth = w;

		if (DetermineResultSize(w, h) == 1) {
			return srcBufferImage;
		}
		CalContrib();
		BufferedImage pbOut = HorizontalFiltering(srcBufferImage, w);
		BufferedImage pbFinalOut = VerticalFiltering(pbOut, h);
		return pbFinalOut;
	}


	/**
	 * 决定图像尺寸
	 * @param w
	 * @param h
	 * @return
	 */
	private static int DetermineResultSize(int w, int h) {
		double scaleH, scaleV;
		scaleH = (double) w / (double) width;
		scaleV = (double) h / (double) height;
		if (scaleH >= 1.0 && scaleV >= 1.0) {
			return 1;
		}
		return 0;
	}
	
	
	/**
	 * 
	 * @param i
	 * @param inWidth
	 * @param outWidth
	 * @param Support
	 * @return
	 */
	private static double Lanczos(int i, int inWidth, int outWidth, double Support) {
		double x;
		x = (double) i * (double) outWidth / (double) inWidth;
		return Math.sin(x * PI) / (x * PI) * Math.sin(x * PI / Support)
				/ (x * PI / Support);
	} 
  
	
	// Assumption: same horizontal and vertical scaling factor
	private static void CalContrib() {
		nHalfDots = (int) ((double) width * support / (double) scaleWidth);
		nDots = nHalfDots * 2 + 1;
		try {
			contrib = new double[nDots];
			normContrib = new double[nDots];
			tmpContrib = new double[nDots];
		} catch (Exception e) {
			logger.error("init   contrib,normContrib,tmpContrib",e);
			e.printStackTrace();
		}

		int center = nHalfDots;
		contrib[center] = 1.0;

		double weight = 0.0;
		int i = 0;
		for (i = 1; i <= center; i++) {
			contrib[center + i] = Lanczos(i, width, scaleWidth, support);
			weight += contrib[center + i];
		}

		for (i = center - 1; i >= 0; i--) {
			contrib[i] = contrib[center * 2 - i];
		}

		weight = weight * 2 + 1.0;

		for (i = 0; i <= center; i++) {
			normContrib[i] = contrib[i] / weight;
		}

		for (i = center + 1; i < nDots; i++) {
			normContrib[i] = normContrib[center * 2 - i];
		}
	} 

	// 处理边缘
	private static void CalTempContrib(int start, int stop) {
		double weight = 0;

		int i = 0;
		for (i = start; i <= stop; i++) {
			weight += contrib[i];
		}

		for (i = start; i <= stop; i++) {
			tmpContrib[i] = contrib[i] / weight;
		}

	} 

	private static int GetRedValue(int rgbValue) {
		int temp = rgbValue & 0x00ff0000;
		return temp >> 16;
	}

	private static int GetGreenValue(int rgbValue) {
		int temp = rgbValue & 0x0000ff00;
		return temp >> 8;
	}

	private static int GetBlueValue(int rgbValue) {
		return rgbValue & 0x000000ff;
	}

	private static int ComRGB(int redValue, int greenValue, int blueValue) {

		return (redValue << 16) + (greenValue << 8) + blueValue;
	}

	// 行水平滤
	private static int HorizontalFilter(BufferedImage bufImg, int startX, int stopX,
			int start, int stop, int y, double[] pContrib) {
		double valueRed = 0.0;
		double valueGreen = 0.0;
		double valueBlue = 0.0;
		int valueRGB = 0;
		int i, j;

		for (i = startX, j = start; i <= stopX; i++, j++) {
			valueRGB = bufImg.getRGB(i, y);

			valueRed += GetRedValue(valueRGB) * pContrib[j];
			valueGreen += GetGreenValue(valueRGB) * pContrib[j];
			valueBlue += GetBlueValue(valueRGB) * pContrib[j];
		}

		valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen),
				Clip((int) valueBlue));
		return valueRGB;

	} 

	// 图片水平滤波
	private static BufferedImage HorizontalFiltering(BufferedImage bufImage, int iOutW) {
		int dwInW = bufImage.getWidth();
		int dwInH = bufImage.getHeight();
		int value = 0;
		BufferedImage pbOut = new BufferedImage(iOutW, dwInH, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < iOutW; x++) {

			int startX;
			int start;
			int X = (int) (((double) x) * ((double) dwInW) / ((double) iOutW) + 0.5);
			int y = 0;

			startX = X - nHalfDots;
			if (startX < 0) {
				startX = 0;
				start = nHalfDots - X;
			} else {
				start = 0;
			}

			int stop;
			int stopX = X + nHalfDots;
			if (stopX > (dwInW - 1)) {
				stopX = dwInW - 1;
				stop = nHalfDots + (dwInW - 1 - X);
			} else {
				stop = nHalfDots * 2;
			}

			if (start > 0 || stop < nDots - 1) {
				CalTempContrib(start, stop);
				for (y = 0; y < dwInH; y++) {
					value = HorizontalFilter(bufImage, startX, stopX, start,
							stop, y, tmpContrib);
					pbOut.setRGB(x, y, value);
				}
			} else {
				for (y = 0; y < dwInH; y++) {
					value = HorizontalFilter(bufImage, startX, stopX, start,
							stop, y, normContrib);
					pbOut.setRGB(x, y, value);
				}
			}
		}

		return pbOut;

	}

	private static int VerticalFilter(BufferedImage pbInImage, int startY, int stopY,
			int start, int stop, int x, double[] pContrib) {
		double valueRed = 0.0;
		double valueGreen = 0.0;
		double valueBlue = 0.0;
		int valueRGB = 0;
		int i, j;

		for (i = startY, j = start; i <= stopY; i++, j++) {
			valueRGB = pbInImage.getRGB(x, i);

			valueRed += GetRedValue(valueRGB) * pContrib[j];
			valueGreen += GetGreenValue(valueRGB) * pContrib[j];
			valueBlue += GetBlueValue(valueRGB) * pContrib[j];
			// System.out.println(valueRed+"->"+Clip((int)valueRed)+"<-");
			//  
			// System.out.println(valueGreen+"->"+Clip((int)valueGreen)+"<-");
			// System.out.println(valueBlue+"->"+Clip((int)valueBlue)+"<-"+"-->");
		}

		valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen), Clip((int) valueBlue));
		// System.out.println(valueRGB);
		return valueRGB;

	} 

	private static BufferedImage VerticalFiltering(BufferedImage pbImage, int iOutH) {
		int iW = pbImage.getWidth();
		int iH = pbImage.getHeight();
		int value = 0;
		BufferedImage pbOut = new BufferedImage(iW, iOutH, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < iOutH; y++) {

			int startY;
			int start;
			int Y = (int) (((double) y) * ((double) iH) / ((double) iOutH) + 0.5);

			startY = Y - nHalfDots;
			if (startY < 0) {
				startY = 0;
				start = nHalfDots - Y;
			} else {
				start = 0;
			}

			int stop;
			int stopY = Y + nHalfDots;
			if (stopY > (int) (iH - 1)) {
				stopY = iH - 1;
				stop = nHalfDots + (iH - 1 - Y);
			} else {
				stop = nHalfDots * 2;
			}

			if (start > 0 || stop < nDots - 1) {
				CalTempContrib(start, stop);
				for (int x = 0; x < iW; x++) {
					value = VerticalFilter(pbImage, startY, stopY, start, stop,
							x, tmpContrib);
					pbOut.setRGB(x, y, value);
				}
			} else {
				for (int x = 0; x < iW; x++) {
					value = VerticalFilter(pbImage, startY, stopY, start, stop,
							x, normContrib);
					pbOut.setRGB(x, y, value);
				}
			}

		}

		return pbOut;

	}

	private static int Clip(int x) {
		if (x < 0)
			return 0;
		if (x > 255)
			return 255;
		return x;
	}

	/**
	 * End: Use Lanczos filter to replace the original algorithm for image
	 * scaling. Lanczos improves quality of the scaled image modify by :blade
	 */
	
	//图片上传
	
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
	 * @return 返回文件存放路径
	 * @throws IOException
	 */
    @SuppressWarnings("unused")
	public static Map<String,String> saveFile(String filePath,MultipartFile file,boolean isThumb) throws IOException {	  
		// 根据配置文件获取服务器图片存放路径
		//String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/brand/" + file.getOriginalFilename();
        String saveFilePath = PropertiesUtils.getFileIO("savePicUrl", "properties/config.properties");

        // 构建文件目录 
        File fileDir = new File(new File(saveFilePath), filePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        // 检查目录写权限
 		if (!fileDir.canWrite()) {
 			return null;
 		}	
        // 构建源图文件名
        String fileName = filePath + DateUtils.formatDate(new Date(), "yyyyMMddhhmmss")
    			+ ((int)(Math.random()*(99999-10000)+10000)) + "." 
        		+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        //String newFileName = saveFilePath + fileName;
   
        //返回类型
        Map<String,String> fileMap = new HashMap<String,String>();

        //生成缩略图并进行图片压缩
        // S:100 M:400 B:800
        if(isThumb){
        	String thumb4 = ImageUtils.getThumImage(filePath,file,saveFilePath,4);
        	if(thumb4!=null){
        		fileMap.put("thumbM", thumb4);
        	}
        	String thumb1 = ImageUtils.getThumImage(filePath,file,saveFilePath,1);
        	if(thumb1!=null){
        		fileMap.put("thumbS", thumb1);
        	}
        	//源图压缩
        	String thumb8 = ImageUtils.getThumImage(filePath,file,saveFilePath,8);      	
        	if(thumb8!=null){
        		//生成图片水印
            	//ImageUtil.getInstance().waterMark(saveFilePath+thumb8, saveFilePath+GolbalUtil.WATERMARK_IMG_LOGO, 100, 300, 0.5f);
        		fileMap.put("thumbB", thumb8);
        	}
        }else{//图片仅仅只进行压缩
        	String compressed = ImageUtils.getCompressedImage(filePath,file,saveFilePath);
        	if(compressed!=null){
        		fileMap.put("thumbB", compressed);
        	}
        }
        
        //保存文件方法二    
        //由于图片上传后都需要压缩或者生成缩略图，所以源图不要，注释下面的代码将不转移源图
        //springMVC的transferTo工作一定要放在生成缩略图之后
        //file.transferTo(new File(newFileName));      
        return fileMap; 
        
        //保存文件方法一
        /*try {
            FileOutputStream out = new FileOutputStream(saveFilePath + "\\"+ newFileName);
            // 写入文件
            out.write(filedata.getBytes());
            out.flush();
            out.close(); 
        } catch (Exception e) {	
            e.printStackTrace();
        } */    
       
    }
    
    
    /**
     * 功能描述：删除图片
     * @param oldPicName 修改之前的文件名
     */
    public static void deleteFile(String oldPicName) {
        // 根据配置文件获取服务器图片存放路径
    	String saveFilePath = PropertiesUtils.getFileIO("savePicUrl", "properties/config.properties");
        // 构建文件目录 
        File fileDir = new File(saveFilePath+"/"+oldPicName);
        if (fileDir.exists()) {
            //把修改之前的图片删除 以免太多没用的图片占据空间
            fileDir.delete();
        }
    }
    
    
    
    /**
     * main
     * @param args
     * @throws IOException
     * 出现错误的原因大多是图片的路径、名称错误
     */
	public static void main(String[] args) throws IOException {
		//图片水印
		waterMark("F:/test.jpg", "F:/icon.png", 100, 300, 0.5f);
		
    	//文字水印
    	Font titleFont = new Font("宋体", Font.BOLD, 30);
    	textMark("F:/test.jpg", "春来桃花开", titleFont, Color.red, 100, 300, 1.0f);
	}
	

}
















//springMVC MultipartFile图片上传知识点
/*MultipartFile类常用的一些方法：
String getContentType()//获取文件MIME类型
InputStream getInputStream()//后去文件流
String getName() //获取表单中文件组件的名字
String getOriginalFilename() //获取上传文件的原名
long getSize()  //获取文件的字节大小，单位byte
boolean isEmpty() //是否为空
void transferTo(File dest)*/ 
/*public void upload(HttpServletRequest request,MultipartFile file)throws Exception {	
		if(file!=null && !file.isEmpty()){	
			try {  
	            // 文件保存路径  
	            //String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/brand/" + file.getOriginalFilename();
				String saveFilePath = PropertiesUtil.getFileIO("savePicUrl", "config.properties");
	            String newFileName = saveFilePath + GolbalUtil.BRAND_IMG_PATH + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
	            // 转存文件  
	            file.transferTo(new File(newFileName));  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } 
		}
	}
	//多文件上传
	public String filesUpload(@RequestParam("files") MultipartFile[] files) {  
      //判断file数组不能为空并且长度大于0  
      if(files!=null&&files.length>0){  
          //循环获取file数组中得文件  
          for(int i = 0;i<files.length;i++){  
              MultipartFile file = files[i];  
              //保存文件  
              saveFile(file);  
          }  
      }  
      // 重定向  
      return "redirect:/list.html";  
  }  
	*/
