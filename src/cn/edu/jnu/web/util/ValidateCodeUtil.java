package cn.edu.jnu.web.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码工具类
 * @author HHT
 **/
public class ValidateCodeUtil {
	private static Random random = new Random();
	/**
	 * 获取指定范围内随机颜色
	 * @param fc
	 * @param bc
	 * @return
	 */
	public static Color getRandColor(int fc,int bc) {
		if(fc > 255) fc = 255;
		if(bc > 255) bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	/**
	 * 生成验证码图片，并返回验证码字符串
	 * @param image
	 * @return
	 */
	public static String createImage(BufferedImage image) {
		// 获取图形上下文
		Graphics g = image.getGraphics();
		//生成随机类
		//Random random = new Random();
		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		//设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		//画边框
		//g.setColor(new Color());
		//g.drawRect(0, 0, width-1, height-1);
		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160,200));
		for (int i=0; i<155; i++) {
			int x = random.nextInt(image.getWidth());
			int y = random.nextInt(image.getHeight());
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x+xl, y+yl);
		}
		// 取随机产生的认证码(4位数字)
		String sRand="";
		for (int i=0;i<4;i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			// 将认证码显示到图象中
			g.setColor(
					new Color(20+random.nextInt(110), 
							  20+random.nextInt(110), 
							  20+random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.drawString(rand, 13*i+6, 16);
		}
		// 图象生效
		g.dispose();
		return sRand;
	}
}
