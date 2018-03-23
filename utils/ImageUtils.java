package com.jie.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
 * @Description: 图片处理工具类:缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印等. <br/>
 * @author jie
 * @date 2017年12月28日
 */
public class ImageUtils {
	
	private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);
	
  
     
    /**
     * 缩放图像（按比例缩放）
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param scale 缩放比例
     * @param flag 缩放选择:true 放大; false 缩小;
     */
    public final static void scale(String srcImageFile, String result, int scale, boolean flag) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图高
            if (flag) {// 放大
                width = width * scale;
                height = height * scale;
            } else {// 缩小
                width = width / scale;
                height = height / scale;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, "png", new File(result));// 输出到文件流
        } catch (IOException e) {
        	logger.error("ImageUtils scale",e);
            e.printStackTrace();
        }
    }
    
    /**
     * 缩放图像（按高度和宽度缩放）
     * @param srcImageFile 源图像文件地址
     * @param width 缩放后的宽度
     * @param height 缩放后的高度
     * @param bool 比例不对时是否需要补白：true为补白; false为不补白;
     * @return
     */
    public final static byte[] scale2(InputStream srcImageFile, int width, int height, boolean bool) {
        try {
            double ratio = 0.0; // 缩放比例
            BufferedImage bi = ImageIO.read(srcImageFile);
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getWidth() > width) || (bi.getHeight() > height)) {
                if(bi.getWidth() < bi.getHeight()){
                	ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }else{
                	ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                }  
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bool) {//补白
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                g.dispose();
                itemp = image;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write((BufferedImage) itemp, "jpg", out); 
            return out.toByteArray();
        } catch (IOException e) {
        	logger.error("ImageUtils scale2 byte[]",e);
            e.printStackTrace();
            return null;
        }
    }
     
    /**
     * 缩放图像（按高度和宽度缩放）
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param width 缩放后的宽度
     * @param height 缩放后的高度    
     * @param bool 比例不对时是否需要补白：true为补白; false为不补白;
     */
    public final static void scale2(String srcImageFile, String result, int width, int height, boolean bool) {
        try {
            double ratio = 0.0; // 缩放比例
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getWidth() > width) || (bi.getHeight() > height)) {
                if(bi.getWidth() < bi.getHeight()){
                	ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }else{
                	ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                }  
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bool) {//补白
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                g.dispose();
                itemp = image;
            }
            ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
        } catch (IOException e) {
        	logger.error("ImageUtils scale2 void",e);
            e.printStackTrace();
        }
    }
     
    /**
     * 图像切割(按指定起点坐标和宽高切割)
     * @param srcImageFile 源图像地址
     * @param result 切片后的图像地址
     * @param x 目标切片起点坐标X
     * @param y 目标切片起点坐标Y
     * @param width 目标切片宽度
     * @param height 目标切片高度
     */
    public final static void cut(String srcImageFile, String result, int x, int y, int width, int height) {
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(),
                                cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "JPEG", new File(result));
            }
        } catch (Exception e) {
        	logger.error("ImageUtils cut",e);
            e.printStackTrace();
        }
    }
     
    /**
     * 图像切割（指定切片的行数和列数）
     * @param srcImageFile 源图像地址
     * @param descDir 切片目标文件夹
     * @param rows 目标切片行数。默认2，必须是范围 [1, 20] 之内
     * @param cols 目标切片列数。默认2，必须是范围 [1, 20] 之内
     */
    public final static void cut2(String srcImageFile, String descDir, int rows, int cols) {
        try {
            if(rows<=0||rows>20) rows = 2; // 切片行数
            if(cols<=0||cols>20) cols = 2; // 切片列数
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int destWidth = srcWidth; // 每张切片的宽度
                int destHeight = srcHeight; // 每张切片的高度
                // 计算切片的宽度和高度
                if (srcWidth % cols == 0) {
                    destWidth = srcWidth / cols;
                } else {
                    destWidth = (int) Math.floor(srcWidth / cols) + 1;
                }
                if (srcHeight % rows == 0) {
                    destHeight = srcHeight / rows;
                } else {
                    destHeight = (int) Math.floor(srcWidth / rows) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
                                destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(image.getSource(),
                                        cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth,
                                destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
        	logger.error("ImageUtils cut2",e);
            e.printStackTrace();
        }
    }
    
    /**
     * 图像切割（指定切片的宽度和高度）
     * @param srcImageFile 源图像地址
     * @param descDir 切片目标文件夹
     * @param destWidth 目标切片宽度。默认200
     * @param destHeight 目标切片高度。默认150
     */
    public final static void cut3(String srcImageFile, String descDir, int destWidth, int destHeight) {
        try {
            if(destWidth<=0) destWidth = 200; // 切片宽度
            if(destHeight<=0) destHeight = 150; // 切片高度
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > destWidth && srcHeight > destHeight) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int cols = 0; // 切片横向数量
                int rows = 0; // 切片纵向数量
                // 计算切片的横向和纵向数量
                if (srcWidth % destWidth == 0) {
                    cols = srcWidth / destWidth;
                } else {
                    cols = (int) Math.floor(srcWidth / destWidth) + 1;
                }
                if (srcHeight % destHeight == 0) {
                    rows = srcHeight / destHeight;
                } else {
                    rows = (int) Math.floor(srcHeight / destHeight) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
                                destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(image.getSource(),
                                        cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth,
                                destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir
                                + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
        	logger.error("ImageUtils cut3",e);
            e.printStackTrace();
        }
    }
    
    /**
     * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
     * @param srcImageFile 源图像地址
     * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
     * @param destImageFile 目标图像地址
     */
    public final static void convert(String srcImageFile, String formatName, String destImageFile) {
        try {
            File f = new File(srcImageFile);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(destImageFile));
        } catch (Exception e) {
        	logger.error("ImageUtils convert",e);
            e.printStackTrace();
        }
    }
    
    /**
     * 彩色转为黑白 
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     */
    public final static void gray(String srcImageFile, String destImageFile) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            ImageIO.write(src, "JPEG", new File(destImageFile));
        } catch (IOException e) {
        	logger.error("ImageUtils gray",e);
            e.printStackTrace();
        }
    }
    
    /**
     * 给图片添加文字水印
     * @param text 水印文字
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param fontName 水印的字体名称
     * @param fontStyle 水印的字体样式
     * @param color 水印的字体颜色
     * @param fontSize 水印的字体大小
     * @param x 修正值
     * @param y 修正值
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public final static void textMark(String text,
            String srcImageFile, String destImageFile, String fontName,
            int fontStyle, Color color, int fontSize,int x,
            int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 在指定坐标绘制水印文字
            g.drawString(text, (width - (getLength(text) * fontSize))
                    / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write((BufferedImage) image, "png", new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
        	logger.error("ImageUtils textMark",e);
            e.printStackTrace();
        }
    }
    
    
    /**
     * 给图片添加图片水印
     * @param iamge 水印图片
     * @param srcImageFile 源图像地址
     * @param destImageFile 目标图像地址
     * @param x 修正值。 默认在中间
     * @param y 修正值。 默认在中间
     * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
     */
    public final static void waterMark(String iamge, String srcImageFile,String destImageFile,
            int x, int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            Image src_biao = ImageIO.read(new File(iamge));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));
            g.drawImage(src_biao, (wideth - wideth_biao) / 2,
                    (height - height_biao) / 2, wideth_biao, height_biao, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write((BufferedImage) image,  "JPEG", new File(destImageFile));
        } catch (Exception e) {
        	logger.error("ImageUtils waterMark",e);
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * 计算text的长度（一个中文算两个字符）
     * @param text
     * @return
     */
    public final static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }
    
    /**
     * png图片处理
     * @param fromFile
     * @param toFile
     */
    public static void resizePNG(String fromFile, String toFile) {
    	try {
            File f2 = new File(fromFile);
            BufferedImage bi2 = ImageIO.read(f2);
            
            BufferedImage to = new BufferedImage(bi2.getWidth(), bi2.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = to.createGraphics();
            to = g2d.getDeviceConfiguration().createCompatibleImage(bi2.getWidth(), bi2.getHeight(), Transparency.TRANSLUCENT);
            g2d.dispose();
            g2d = to.createGraphics();

            Image from = bi2.getScaledInstance(bi2.getWidth(), bi2.getHeight(), bi2.SCALE_DEFAULT);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();
            ImageIO.write(to, "png", new File(toFile));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
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
   
        //返回类型
        Map<String,String> fileMap = new HashMap<String,String>();

        //生成缩略图并进行图片压缩
        // S:100 M:400 B:800
//        if(isThumb){
//        	String thumb4 = ImageUtils.getThumImage(filePath,file,saveFilePath,4);
//        	if(thumb4!=null){
//        		fileMap.put("thumbM", thumb4);
//        	}
//        	String thumb1 = ImageUtils.getThumImage(filePath,file,saveFilePath,1);
//        	if(thumb1!=null){
//        		fileMap.put("thumbS", thumb1);
//        	}
//        	//源图压缩
//        	String thumb8 = ImageUtils.getThumImage(filePath,file,saveFilePath,8);      	
//        	if(thumb8!=null){
//        		fileMap.put("thumbB", thumb8);
//        	}
//        }else{//图片仅仅只进行压缩
//        	String compressed = ImageUtils.getCompressedImage(filePath,file,saveFilePath);
//        	if(compressed!=null){
//        		fileMap.put("thumbB", compressed);
//        	}
//        }
            
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
        File fileDir = new File(saveFilePath+"/"+oldPicName);
        if (fileDir.exists()) {
            //把修改之前的图片删除 以免太多没用的图片占据空间
            fileDir.delete();
        }
    }
     

    public static void main(String[] args) {
        // 1-缩放图像：
        // 方法一：按比例缩放 此方法生成的图片质量减小
        //ImageUtils.scale("F:/rotate-img.png", "F:/rotate-img1_scale.png", 1, true);//测试OK
        // 方法二：按宽度和高度缩放
        //ImageUtils.scale2("F:/test.jpg", "F:/test-scale.jpg", 400, 300, true);//测试OK
        // 2-切割图像：
        // 方法一：按指定起点坐标和宽高切割
        //ImageUtils.cut("F:/test.jpg", "F:/test_cut.jpg", 0, 0, 400, 400 );//测试OK
        // 方法二：指定切片的行数和列数
        //ImageUtils.cut2("F:/test.jpg", "F:/", 2, 2 );//测试OK
        // 方法三：指定切片的宽度和高度
        //ImageUtils.cut3("F:/test.jpg", "F:/", 300, 300 );//测试OK
        // 3-图像类型转换：png不会变黑
        //ImageUtils.convert("F:/rotate-img.png", "PNG", "F:/rotate-img_convert.png");//测试OK
        // 4-彩色转黑白：
        //ImageUtils.gray("F:/test.jpg", "F:/test_gray.jpg");//测试OK
        // 5-给图片添加文字水印：
        //ImageUtils.textMark("京东商城","F:/rotate-img.png","F:/rotate-img-textmark.png","宋体",Font.BOLD,Color.red,60,0,0,0.5f);//测试OK
        // 6-给图片添加图片水印：
        //ImageUtils.waterMark("F:/test.jpg", "F:/icon.png","F:/test_pressImage.jpg", 0, 0, 0.5f);//测试OK
    }
    
}
