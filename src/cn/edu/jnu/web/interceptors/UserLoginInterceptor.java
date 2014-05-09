package cn.edu.jnu.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.edu.jnu.web.exception.LoginException;
import cn.edu.jnu.web.util.WebUtil;

/**
 * 用户登入拦截器<br>
 * 对订单操作和用户控制中心操作时，检查用户是否登入，没有登入则跳转到登入页面，
 * 并记录当前页面，登入后恢复到当前操作页面
 * @author HHT
 *
 */
public class UserLoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");// 获取当前用户信息
		if(obj == null) {
			String params = request.getQueryString();
			String url = request.getRequestURI();

			if(params != null) url += "?" + params;
			if(url.indexOf("view/user/account/") > -1) {
				url = url.substring(0, url.lastIndexOf("/"));
			}
			WebUtil.setCookie(response, "prehis.ishare", url, -1);
			throw new LoginException("请登入后再进行相关操作");// 未登入
		} else {
			//throw new LoginException("请登入后再进行相关操作");
			return true;
		}
	}

}
