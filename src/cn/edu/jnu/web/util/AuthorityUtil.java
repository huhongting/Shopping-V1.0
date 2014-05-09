package cn.edu.jnu.web.util;

import cn.edu.jnu.web.authority.AuthorityType;
import cn.edu.jnu.web.entity.permission.Group;
import cn.edu.jnu.web.entity.user.User;

/**
 * 权限组工具类
 * @author HHT
 *
 */
public class AuthorityUtil {
	/**
	 * 验证用户是否具有指定权限
	 * @param user
	 * @param authorityType
	 * @return
	 */
	public static boolean hasAuthority(User user, AuthorityType authorityType) {
		Group group = user.getGroup();
		try {
			String authority = group.getAuthority();
			if(authority.charAt(authorityType.getIndex() - 1) == '1') {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	/**
	 * 拼接权限组所有权限信息
	 * @param group
	 * @return
	 */
	public static String createAuthorityString(Group group) {
		if(group == null) return "";
		String authority = group.getAuthority();
		StringBuffer buff = new StringBuffer();
		int n = 1;
		for(int i=0; i<authority.length(); i++) {
			if(authority.charAt(i) == '1') {
				buff.append((i + 1) + ".");
				buff.append(AuthorityType.getAuthorityType(i + 1).getName());
				if(n % 2 == 0) buff.append("<br>");
				else buff.append("&nbsp;&nbsp;&nbsp;&nbsp;");
				n++;
			}
		}
		return buff.toString();
	}
}