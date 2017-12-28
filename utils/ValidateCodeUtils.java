package com.self.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.DigestUtils;

/**
 * @ClassName: ValidateCodeUtils
 * @Description: 验证码类、加密类. <br/>
 * @author jie
 * @date 2017年11月28日
 */
public class ValidateCodeUtils {
	
    private static int width = 100;//定义图片的width
    private static int height = 34;//定义图片的height
    private static int codeCount = 5;//定义图片上显示验证码的个数
    private static int xx = 15;
    private static int fontHeight = 22;
    private static int codeY = height/2+(height-fontHeight)/2+2; //y方向偏移量
    static char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    
    /**
     * 生成验证码
     * @param req
     * @param resp
     * @throws IOException
     */
    public static void getCode(HttpServletRequest req, HttpServletResponse resp)throws IOException {
 
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //Graphics2D gd = buffImg.createGraphics();
        //Graphics2D gd = (Graphics2D) buffImg.getGraphics();
        Graphics gd = buffImg.getGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, width, height);
 
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        // 设置字体。
        gd.setFont(font);
 
        // 画边框。
        gd.setColor(Color.gray);
        gd.drawRect(0, 0, width - 1, height - 1);
 
        // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.BLACK);
        for (int i = 0; i < 40; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }
 
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
 
        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String code = String.valueOf(codeSequence[random.nextInt(36)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
 
            // 用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(red, green, blue));
            gd.drawString(code, (i + 1) * xx, codeY);
 
            // 将产生的四个随机数组合在一起。
            randomCode.append(code);
        }
        // 将数字的验证码保存到Session中
        //转为小写
        HttpSession session = req.getSession();
        //System.out.println(randomCode);
        session.setAttribute("code", randomCode.toString().toLowerCase());
 
        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpeg");
 
        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = resp.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
    }
    
    
    /**
	 * MD5摘要
	 * @param str 原始字符串
	 * @return 加密字符串
	 */
	public static String md5Encryption(String str){
		return DigestUtils.md5DigestAsHex(str.getBytes());
	}
    
    
    /**
	 * main
	 * @param args
	 * @throws ParseException
     * @throws IOException 
	 */
	public static void main(String[] args) throws ParseException, IOException {
		System.out.println("md5加密："+md5Encryption("admin"));
	}
 
	
}

