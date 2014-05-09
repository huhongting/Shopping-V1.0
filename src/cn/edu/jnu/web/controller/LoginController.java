package cn.edu.jnu.web.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.edu.jnu.web.dao.impl.UserDaoImpl;
import cn.edu.jnu.web.entity.emp.Notification;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.exception.InterceptorException;
import cn.edu.jnu.web.service.NotificationService;
import cn.edu.jnu.web.util.PasswordUtil;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.UserUtil;
import cn.edu.jnu.web.util.ValidateCodeUtil;

/**
 * ��̨�����������������֤������͵�������
 * @author HHT
 *
 */
@Controller
public class LoginController {
	
	private UserDaoImpl userDao;
	private ObjectMapper mapper = new ObjectMapper();
	private NotificationService noteService;
	
	/**
	 * ��ת����̨����������
	 * @return
	 */
	@RequestMapping(value="/control/admin/index")
	public String hadelIndexView() {
		return "/control/admin/default";
	}
	
	/**
	 * ��ת����̨����������
	 * @param req
	 * @return
	 * @throws InterceptorException
	 */
	@RequestMapping(value="/control/login")
	public String hadelLoginView(HttpServletRequest req) 
			throws InterceptorException {
		HttpSession session = req.getSession();
		Object obj = session.getAttribute("employee");
		if(obj == null) {
			QueryResult<Notification> res = noteService.listNotifications(0, 1, null);
			req.setAttribute("notes", res);
			return "/control/login/default";// ���û�е����û�����ת��������档
		} else {
			User user = (User) obj;
			if(UserUtil.isEmployee(user)) return "redirect:/control/admin/index";
			else throw new InterceptorException("Ȩ�޲���");// �û�Ȩ�޲�����
		}
	}
	
	/**
	 * ��֤�û���������
	 * @param username
	 * @param password
	 * @param validatecode
	 * @param req
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping(value="/control/login/login.do", method=RequestMethod.POST)
	public @ResponseBody ObjectNode handelLogin(String username, 
			String password, String validatecode, HttpServletRequest req) 
					throws NoSuchAlgorithmException {
		
		HttpSession session = req.getSession();
		
		User user = userDao.findByName(username);
		if(user != null && user.getDelete() == false && UserUtil.isEmployee(user)) {
			if(user.getPassword().equals(PasswordUtil.getEncodeString(password)) &&
					validatecode.equals(session.getAttribute("validcode"))) {
				session.setAttribute("employee", user);
				
				return mapper.createObjectNode().put("success", true);
			}
		}
		
		return mapper.createObjectNode().put("success", false);// �û�����ʧ�ܡ�
	}
	
	/**
	 * ��̨����ϵͳ�˳�
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/control/login/logout.do", method=RequestMethod.POST)
	public @ResponseBody ObjectNode handleExit(HttpServletRequest req) {
		//req.getSession().invalidate();// ע��Session
		req.getSession().setAttribute("employee", null);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * ���������λ����������֤��
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/control/login/validate.do")
	public void handelValidCode(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		// ���ڴ��д���ͼ��
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		String sRand = ValidateCodeUtil.createImage(image);
		
		// ����֤�����SESSION
		request.getSession(true).setAttribute("validcode", sRand);
		
		OutputStream output = response.getOutputStream();

		// ���ͼ��ҳ��
		ImageIO.write(image, "JPEG", output);
		output.flush();
	}

	public UserDaoImpl getUserDao() {
		return userDao;
	}

	@Resource(name="userDaoImpl")
	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	public NotificationService getNoteService() {
		return noteService;
	}
	@Resource(name="notificationServiceImpl")
	public void setNoteService(NotificationService noteService) {
		this.noteService = noteService;
	}
}
