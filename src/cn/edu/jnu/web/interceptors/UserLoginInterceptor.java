package cn.edu.jnu.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.edu.jnu.web.exception.LoginException;
import cn.edu.jnu.web.util.WebUtil;

/**
 * �û�����������<br>
 * �Զ����������û��������Ĳ���ʱ������û��Ƿ���룬û�е�������ת������ҳ�棬
 * ����¼��ǰҳ�棬�����ָ�����ǰ����ҳ��
 * @author HHT
 *
 */
public class UserLoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");// ��ȡ��ǰ�û���Ϣ
		if(obj == null) {
			String params = request.getQueryString();
			String url = request.getRequestURI();

			if(params != null) url += "?" + params;
			if(url.indexOf("view/user/account/") > -1) {
				url = url.substring(0, url.lastIndexOf("/"));
			}
			WebUtil.setCookie(response, "prehis.ishare", url, -1);
			throw new LoginException("�������ٽ�����ز���");// δ����
		} else {
			//throw new LoginException("�������ٽ�����ز���");
			return true;
		}
	}

}
