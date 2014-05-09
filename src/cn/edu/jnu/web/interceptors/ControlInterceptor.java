package cn.edu.jnu.web.interceptors;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.exception.InterceptorException;

/**
 * ��̨���������<br>
 * ����û��Ƿ�Ϊ��˾Ա�����ǹ�˾Ա�����ܵ��롣
 * �Լ����Session�Ƿ�ʱ����ʱ�������µ��롣
 * @author HHT
 *
 */
public class ControlInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("employee");// ��ȡ��ǰ�û���Ϣ
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
			//throw new InterceptorException("û��Ȩ��");// δ����
		} else {
			User user = (User) obj;
			if(user.getUserType() == User.EMPLOYEE || 
					user.getUserType() == User.ADMINISTRATOR)
				return true;
			else
				throw new InterceptorException("û��Ȩ��");// ��Ա���û�
		}
	}

}
