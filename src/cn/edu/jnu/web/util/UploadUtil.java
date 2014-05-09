package cn.edu.jnu.web.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadUtil {
	public static List<String> SUPPORTEDTYPE = Arrays.asList(
		".jpg", ".jpeg", ".png", ".bmp", ".gif", ".ico"
	);
	
	/**
	 * ͼƬ�ϴ�<br>
	 * ֧�ָ�ʽ��jpg, jpeg, png, bmp, gif, ico
	 * @param request
	 * @param pic
	 * @param savePath ͼƬ����·�������·������
	 * @param returnPath �����ݿⱣ���·��
	 * @param name ͼƬǰ׺���ƣ�����ͼƬ���Ƹ�ʽΪ��ͼƬǰ׺����+ʱ���+��׺
	 * @return arr[0]: ҳ����ʾ·��; arr[1]: �洢����·��
	 * @throws FileNotFoundException
	 */
	public static String[] uploadPicture(HttpServletRequest request, 
			CommonsMultipartFile pic, String savePath, String returnPath, String name) 
			throws FileNotFoundException {
		String[] urls = null;
		if(pic != null && !pic.isEmpty()) {
			// ��ȡ�洢·��
			String localPath = request.getSession()
					.getServletContext()
					.getRealPath(savePath);
			String fileName = pic.getOriginalFilename();// ��ȡͼƬ����
			String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());// ��ȡͼƬ��׺
			
			if(UploadUtil.SUPPORTEDTYPE.contains(suffix.toLowerCase())) {// �жϸ�ʽ
			
				String fn = name + System.currentTimeMillis() + suffix;
				localPath += File.separator + fn;
				
				InputStream is = null;
				try {
					is = pic.getInputStream();
					PictureUtil.compressPic(is, localPath, 600, 300, true);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if(is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				urls = new String[2];
				urls[0] = returnPath + fn;
				urls[1] = localPath;
				return urls;
			} else {
				return urls;
			}
		}
		return urls;
	}
	
	public static boolean deletePicture(String path) {
		File file = new File(path);
		if(file.exists() && file.isFile()) {
			return file.delete();
		}
		return false;
	}
}
