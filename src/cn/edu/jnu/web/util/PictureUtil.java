package cn.edu.jnu.web.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * ͼƬ������
 * @author HHT
 *
 */
public class PictureUtil {
	/**
	 * ѹ��ͼƬ
	 * @param is ͼƬ������
	 * @param outputPath ͼƬ����·��
	 * @param outputWidth ������
	 * @param outputHeight ������
	 * @param proportion �ȱ�����
	 * @return
	 */
	public static boolean compressPic(InputStream is, String outputPath,
			int outputWidth, int outputHeight, boolean proportion) {
		try {
			Image img = ImageIO.read(is);
			// �ж�ͼƬ��ʽ�Ƿ���ȷ
			if (img.getWidth(null) == -1) {
				return false;
			} else {
				int newWidth;
				int newHeight;
				// �ж��Ƿ��ǵȱ�����
				if (proportion == true) {
					// Ϊ�ȱ����ż��������ͼƬ��ȼ��߶�
					double rate1 = ((double) img.getWidth(null))
							/ (double) outputWidth + 0.1;
					double rate2 = ((double) img.getHeight(null))
							/ (double) outputHeight + 0.1;
					// �������ű��ʴ�Ľ������ſ���
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					newWidth = img.getWidth(null); // �����ͼƬ���
					newHeight = img.getHeight(null); // �����ͼƬ�߶�
				}
				BufferedImage tag = new BufferedImage((int) newWidth,
						(int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * Image.SCALE_SMOOTH �������㷨 ��������ͼƬ��ƽ���ȵ� ���ȼ����ٶȸ� ���ɵ�ͼƬ�����ȽϺ� ���ٶ���
				 */
				tag.getGraphics().drawImage(
						img.getScaledInstance(newWidth, newHeight,
								Image.SCALE_SMOOTH), 0, 0, null);
				FileOutputStream out = new FileOutputStream(outputPath);
				// JPEGImageEncoder������������ͼƬ���͵�ת��
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(tag);
				out.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}
}
