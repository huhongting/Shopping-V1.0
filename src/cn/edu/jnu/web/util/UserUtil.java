package cn.edu.jnu.web.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.edu.jnu.web.entity.user.User;

/**
 * 用户工具类
 * @author HHT
 *
 */
public class UserUtil {
	/**
	 * 判断用户是否为员工
	 * @param user
	 * @return
	 */
	public static boolean isEmployee(User user) {
		return user.getUserType() == User.CUSTOMER ? false : true;
	}
	
	/**
	 * 根据用户输入获取搜索关键字<br>
	 * 默认分隔符为：空格/tab空格/逗号
	 * @param key
	 * @return
	 */
	public static List<String> getQueryKeys(String key) {
		List<String> keys = new ArrayList<String>();
		String[] str = key.trim().split(" ");
		for(String s : str) {
			String[] k = s.split("	");
			for(String ss : k) {
				String[] ks = ss.split(",");
				for(String ts : ks) {
					if(!ts.trim().equals("")) keys.add(ts.trim());
				}
			}
		}
		return keys;
	}
	/**
	 * 获取URL参数，解决中文乱码问题
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getQueryString(HttpServletRequest request, String key) {
		String query = request.getParameter(key);
		if(query == null) return null;
		try {
			query = new String(query.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		}
		return query.trim();
	}
	
	public static String[] createSearchQuerySQL(List<String> keys, String[] cols) {
		List<String> ors = new ArrayList<String>();
		for(String s : keys) {
			for(String c : cols) {
				ors.add(c + " like '%" + s + "%'");
			}
		}
		return ors.toArray(new String[]{});
	}
}
