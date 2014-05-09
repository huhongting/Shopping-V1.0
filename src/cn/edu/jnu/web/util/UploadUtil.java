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
	 * 图片上传<br>
	 * 支持格式：jpg, jpeg, png, bmp, gif, ico
	 * @param request
	 * @param pic
	 * @param savePath 图片保存路径，相对路径即可
	 * @param returnPath 供数据库保存的路径
	 * @param name 图片前缀名称，最终图片名称格式为：图片前缀名称+时间戳+后缀
	 * @return arr[0]: 页面显示路径; arr[1]: 存储绝对路径
	 * @throws FileNotFoundException
	 */
	public static String[] uploadPicture(HttpServletRequest request, 
			CommonsMultipartFile pic, String savePath, String returnPath, String name) 
			throws FileNotFoundException {
		String[] urls = null;
		if(pic != null && !pic.isEmpty()) {
			// 获取存储路径
			String localPath = request.getSession()
					.getServletContext()
					.getRealPath(savePath);
			String fileName = pic.getOriginalFilename();// 获取图片名称
			String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());// 获取图片后缀
			
			if(UploadUtil.SUPPORTEDTYPE.contains(suffix.toLowerCase())) {// 判断格式
			
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
