package cn.edu.jnu.web.interceptors;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.exception.InterceptorException;

/**
 * 后台登入控制器<br>
 * 检查用户是否为公司员工，非公司员工不能登入。
 * 以及检查Session是否超时，超时则需重新登入。
 * @author HHT
 *
 */
public class ControlInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("employee");// 获取当前用户信息
		if(obj == null) {
			PrintWriter out = response.getWriter();
			if(request.getHeader("x-requested-with") != null &&
					request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
				out.println("{sessionStatus: 'timeout'}");
			} else {
				out.println("<script style='text/javascript'>");
				out.println("window.location.href='/control/login';");
				out.println("</script>");
			}
			out.flush();
			out.close();
			return false;
			//throw new InterceptorException("没有权限");// 未登入
		} else {
			User user = (User) obj;
			if(user.getUserType() == User.EMPLOYEE || 
					user.getUserType() == User.ADMINISTRATOR)
				return true;
			else
				throw new InterceptorException("没有权限");// 非员工用户
		}
	}

}
