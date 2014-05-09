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
 * 重置用户密码控制器
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
	 * 处理用户的重置密码请求
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
		if(yzm.equals(valid)) {// 验证码检查
			if(user != null) {// 用户检查
				try {
					String pass = PasswordUtil.createPass(6);// 重置密码
					String s = PasswordUtil.getEncodeString(pass);
					
					sendMail(user.getName(), user.getEmail(), "IShare.com-密码重置", pass);
	
					user.setPassword(s);
					userService.updateUser(user);// 更新用户密码
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
				root.put(XMLUtil.createObjectNode().put("msg", "该邮箱没有被注册！"));
			} else {
				root.put(XMLUtil.createObjectNode().put("success", false));
				root.put(XMLUtil.createObjectNode().put("msg", "系统繁忙，请稍候再试！"));
			}
		} else {
			root.put(XMLUtil.createObjectNode().put("success", false));
			root.put(XMLUtil.createObjectNode().put("msg", "验证码错误！"));
		}
		out.println(root);
		out.flush();
		out.close();
	}
	
	/**
	 * 处理员工的重置密码请求
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

		if(user != null) {// 用户检查
			if(user.getEmail().equals(umail)) {
				try {
					String pass = PasswordUtil.createPass(6);// 重置密码
					String s = PasswordUtil.getEncodeString(pass);
					
					sendMail(user.getName(), user.getEmail(), "IShare.com-密码重置", pass);

					user.setPassword(s);
					userService.updateUser(user);// 更新用户密码
				} catch (Exception e) {
					//e.printStackTrace();
				}
				return mapper.createObjectNode()
						.put("success", true);
			}
			return mapper.createObjectNode()
					.put("success", false)
					.put("msg", "该邮箱未注册！");
		} else {
			return mapper.createObjectNode()
					.put("success", false)
					.put("msg", "不存在该账户！");
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
						);// 发送重置密码邮件
				} catch (Exception e) {
				}				
			}
		});
		th.setDaemon(true);
		th.start();			
	}

	/**
	 * 重置密码邮件内容
	 * @param name
	 * @param pass
	 * @return
	 */
	public static String createPassBackContent(String name, String pass) {
		StringBuffer content = new StringBuffer();
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">");
		content.append("亲爱的");
		content.append(name);
		content.append("：</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">你的登录密码更改要求已经收到，下面是系统生成的重置密码：</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<font color=blue>");
		content.append(pass);
		content.append("</font>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">为了你的帐号安全，请尽快登入账户并修改密码。</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">感谢对IShare.com的支持。</span>");
		
		return content.toString();
	}
	
	/**
	 * 新员工初始密码邮件内容
	 * @param name
	 * @param pass
	 * @return
	 */
	public static String createNewEmpContent(String name, String pass) {
		StringBuffer content = new StringBuffer();
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">");
		content.append("亲爱的");
		content.append(name);
		content.append("：</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">欢迎您成为IShare.com的一员，下面是您的系统登入密码：</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<font color=blue>");
		content.append(pass);
		content.append("</font>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">为了您的帐号安全，请尽快登入系统并修改密码。</span>");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<br style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255);\">");
		content.append("<span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: 14px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: normal; orphans: auto; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); display: inline !important; float: none;\">感谢对IShare.com的支持。</span>");
		
		return content.toString();
	}
}
