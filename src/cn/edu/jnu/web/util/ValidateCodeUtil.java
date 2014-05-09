package cn.edu.jnu.web.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * ��֤�빤����
 * @author HHT
 **/
public class ValidateCodeUtil {
	private static Random random = new Random();
	/**
	 * ��ȡָ����Χ�������ɫ
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
	 * ������֤��ͼƬ����������֤���ַ���
	 * @param image
	 * @return
	 */
	public static String createImage(BufferedImage image) {
		// ��ȡͼ��������
		Graphics g = image.getGraphics();
		//���������
		//Random random = new Random();
		// �趨����ɫ
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		//�趨����
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		//���߿�
		//g.setColor(new Color());
		//g.drawRect(0, 0, width-1, height-1);
		// �������155�������ߣ�ʹͼ���е���֤�벻�ױ���������̽�⵽
		g.setColor(getRandColor(160,200));
		for (int i=0; i<155; i++) {
			int x = random.nextInt(image.getWidth());
			int y = random.nextInt(image.getHeight());
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x+xl, y+yl);
		}
		// ȡ�����������֤��(4λ����)
		String sRand="";
		for (int i=0;i<4;i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			// ����֤����ʾ��ͼ����
			g.setColor(
					new Color(20+random.nextInt(110), 
							  20+random.nextInt(110), 
							  20+random.nextInt(110)));// ���ú�����������ɫ��ͬ����������Ϊ����̫�ӽ�������ֻ��ֱ������
			g.drawString(rand, 13*i+6, 16);
		}
		// ͼ����Ч
		g.dispose();
		return sRand;
	}
}
