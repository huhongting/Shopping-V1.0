package cn.edu.jnu.web.view.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.listener.SiteSessionListener;
import cn.edu.jnu.web.service.BookService;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.util.ArrayNode;
import cn.edu.jnu.web.util.PasswordUtil;
import cn.edu.jnu.web.util.WebUtil;
import cn.edu.jnu.web.util.XMLUtil;
import cn.edu.jnu.web.view.dao.BookCart;
import cn.edu.jnu.web.view.dao.CarBook;

/**
 * �û����������
 * @author HHT
 *
 */
@Controller
public class UserLoginController {
	
	private UserService userService;
	private BookService bookService;
	
	/**
	 * �����û�������ͼ
	 * @return
	 */
	@RequestMapping(value="/view/login")
	public String handlerLoginView() {
		return "/pages/product/login";
	}
	
	/**
	 * ��������������ͼ
	 * @return
	 */
	@RequestMapping(value="/view/passback")
	public String handlerPassBack() {
		return "/pages/product/passback";
	}
	
	/*@RequestMapping(value="/view/login", params="cmd=checkLogin")
	public void checkLogin(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		User user = WebUtil.getCurrentUser(request);
		ArrayNode root = XMLUtil.createArrayNode();
		if(user == null) {
			root.put(XMLUtil.createObjectNode().put("success", false));
		} else {
			root.put(XMLUtil.createObjectNode().put("success", true));
		}
		PrintWriter out = response.getWriter();
		out.println(root);
		out.close();
	}*/
	
	/**
	 * �����û���������
	 * @param name
	 * @param pass
	 * @param chk
	 * @param request
	 * @param response
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	@RequestMapping(value="/view/login", params="cmd=login")
	public void handlerLogin(@RequestParam String name, 
			@RequestParam String pass, @RequestParam boolean chk,
			HttpServletRequest request, HttpServletResponse response) 
					throws NoSuchAlgorithmException, IOException {
		response.setContentType("application/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		ArrayNode root = XMLUtil.createArrayNode();
		
		User user = userService.findByName(name);
		if(user != null && user.getDelete() == false 
				&& (user.getPassword().equals(pass)/* ��ס����ʱѡ����� */
						|| user.getPassword().equals(PasswordUtil.getEncodeString(pass))/* δ��ס����ʱѡ����� */)) {
			Date last = user.getLastLogin();
			user.setLastLogin(new Date());// �����������ʱ��
			userService.updateUser(user);
			user.setLastLogin(last);
			request.getSession().setAttribute("user", user);
			if(chk == true) {
				String value = user.getName() + "," + user.getPassword();
				WebUtil.setCookie(response, "login.ishare", value, 2*7*24*3600);// ��ס��������
			}
			root.put(XMLUtil.createObjectNode().put("success", true));
			
			WebUtil.addSessionHistory2Cookie(request, response);// ��session��ʷ��ӵ�cookie
			
			
			BookCart cart = WebUtil.getSessionCart(request, response);
			
			//��cookie��ȡ���ﳵ��session
			int[][] ses = WebUtil.getCartFromCookie(request, response, user.getId() + ".cart.ishare.com");
			if(ses != null) {
				for(int[] se : ses) {
					Book book = bookService.showBook(se[0]);
					if(book != null) {
						CarBook cb = new CarBook(book, se[1]);
						cart.addBook(cb);
					}
				}
			}				
			WebUtil.addSessionCart2Cookie(
					response, 
					user.getId() + ".cart.ishare.com", 
					cart);
		} else {
			root.put(XMLUtil.createObjectNode().put("success", false));
		}

		out.println(root);
		out.flush();
		out.close();
	}
	
	/**
	 * �����û��˳�����
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/view/user/logout")
	public String handlerLogout(HttpServletRequest request, HttpServletResponse response) {
		//request.getSession().invalidate();
		request.getSession().setAttribute("user", null);
		request.getSession().setAttribute("cart.ishare.com", null);
		HttpSession session = SiteSessionListener.getSession(WebUtil.getCookie(request, "cart.ishare.com"));
		if(session != null) {
			session.setAttribute("cart.ishare.com", null);
		}
		WebUtil.delCookie(request, response, "login.ishare");
		WebUtil.delCookie(request, response, "cart.ishare.com");
		return "redirect:/index";
	}
	
	public UserService getUserService() {
		return userService;
	}
	@Resource(name="userServiceImpl")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public BookService getBookService() {
		return bookService;
	}
	@Resource(name="bookServiceImpl")
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}
}
