package cn.edu.jnu.web.view.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.util.ArrayNode;
import cn.edu.jnu.web.util.MailSender;
import cn.edu.jnu.web.util.PasswordUtil;
import cn.edu.jnu.web.util.XMLUtil;

/**
 * �����û����������
 * @author HHT
 *
 */
@Controller
public class PassbackController {
	private UserService userService;
	
	public UserService getUserService() {
		return userService;
	}
	@Resource(name="userServiceImpl")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * �����û���������������
	 * @param email
	 * @param yzm
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/user/passback", method=RequestMethod.POST)
	public void sendNewPass(@RequestParam String email, @RequestParam String yzm,
			HttpServletRequest request, HttpServletResponse response) 
					throws IOException {
		response.setContentType("application/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		
		int flag = 0;
		User user = userService.findByEmail(email);
		if(user != null && user.getUserType() != User.CUSTOMER) user = null;

		ArrayNode root = XMLUtil.createArrayNode();
		Object valid = request.getSession().getAttribute("validcode");
		if(yzm.equals(valid)) {// ��֤����
			if(user != null) {// �û����
				try {
					String pass = PasswordUtil.createPass(6);// ��������
					String s = PasswordUtil.getEncodeString(pass);
					
					sendMail(user.getName(), user.getEmail(), "IShare.com-��������", pass);
	
					user.setPassword(s);
					userService.updateUser(user);// �����û�����
				} catch (Exception e) {
					//e.printStackTrace();
					flag = 2;
				}
			} else {
				flag = 1;
			}
			if(flag == 0) {
				root.put(XMLUtil.createObjectNode().put("success", true));
			} else if(flag == 1) {
				root.put(XMLUtil.createObjectNode().put("success", false));
				root.put(XMLUtil.createObjectNode().put("msg", "������û�б�ע�ᣡ"));
			} else {
				root.put(XMLUtil.createObjectNode().put("success", false));
				root.put(XMLUtil.createObjectNode().put("msg", "ϵͳ��æ�����Ժ����ԣ�"));
			}
		} else {
			root.put(XMLUtil.createObjectNode().put("success", false));
			root.put(XMLUtil.createObjectNode().put("msg", "��֤�����"));
		}
		out.println(root);
		out.flush();
		out.close();
	}
	
	/**
	 * ����Ա����������������
	 * @param email
	 * @param yzm
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/employee/passback", method=RequestMethod.POST)
	public @ResponseBody ObjectNode sendNewPass(@RequestParam String umail, 
			@RequestParam String uname) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		User user = userService.findByName(uname);
		if(user != null && user.getUserType() == User.CUSTOMER) user = null;

		if(user != null) {// �û����
			if(user.getEmail().equals(umail)) {
				try {
					String pass = PasswordUtil.createPass(6);// ��������
					String s = PasswordUtil.getEncodeString(pass);
					
					sendMail(user.getName(), user.getEmail(), "IShare.com-��������", pass);

					user.setPassword(s);
					userService.updateUser(user);// �����û�����
				} catch (Exception e) {
					//e.printStackTrace();
				}
				return mapper.createObjectNode()
						.put("success", true);
			}
			return mapper.createObjectNode()
					.put("success", false)
					.put("msg", "������δע�ᣡ");
		} else {
			return mapper.createObjectNode()
					.put("success", false)
					.put("msg", "�����ڸ��˻���");
		}
	}
	
	private void sendMail(final String empname, 
			final String empmail, final String subject, final String pass) {
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					MailSender.send(
							empmail, 
							subject, 
							PassbackController.createNewEmpContent(empname, pass), 
							MailSender.MIMETYPE_TEXT_HTML
						);// �������������ʼ�
				} catch (Exception e) {
				}				
			}
		});
		th.setDaemon(true);
		th.start();			
	}

	/**
	 * ���������ʼ�����
	 * @param name
	 * @param pass
	 * @return
	 */
	public static String createPassBackContent(String name, String pass) {
		StringBuffer content = new StringBuffer();
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">");
		content.append("�װ���");
		content.append(name);
		content.append("��</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">��ĵ�¼�������Ҫ���Ѿ��յ���������ϵͳ���ɵ��������룺</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<font color=blue>");
		content.append(pass);
		content.append("</font>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">Ϊ������ʺŰ�ȫ���뾡������˻����޸����롣</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">��л��IShare.com��֧�֡�</span>");
		
		return content.toString();
	}
	
	/**
	 * ��Ա����ʼ�����ʼ�����
	 * @param name
	 * @param pass
	 * @return
	 */
	public static String createNewEmpContent(String name, String pass) {
		StringBuffer content = new StringBuffer();
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">");
		content.append("�װ���");
		content.append(name);
		content.append("��</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">��ӭ����ΪIShare.com��һԱ������������ϵͳ�������룺</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<font color=blue>");
		content.append(pass);
		content.append("</font>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">Ϊ�������ʺŰ�ȫ���뾡�����ϵͳ���޸����롣</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">��л��IShare.com��֧�֡�</span>");
		
		return content.toString();
	}
}
