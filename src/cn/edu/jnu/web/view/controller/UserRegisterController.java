package cn.edu.jnu.web.view.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.edu.jnu.web.entity.user.Account;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.util.ArrayNode;
import cn.edu.jnu.web.util.PasswordUtil;
import cn.edu.jnu.web.util.ValidateCodeUtil;
import cn.edu.jnu.web.util.WebUtil;
import cn.edu.jnu.web.util.XMLUtil;

/**
 * 用户注册控制器
 * @author HHT
 *
 */
@Controller
public class UserRegisterController {
	private UserService userService;
	
	/**
	 * 返回用户注册视图
	 * @return
	 */
	@RequestMapping(value="/view/register")
	public String handlerRegView() {
		return "/pages/product/register";
	}
	
	/**
	 * 处理用户注册请求
	 * @param name
	 * @param pass
	 * @param email
	 * @param yzm
	 * @param request
	 * @param response
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	@RequestMapping(value="/view/register/reg")
	public void handlerReg(@RequestParam String name,
			@RequestParam String pass, @RequestParam String email,
			@RequestParam String yzm, HttpServletRequest request,
			HttpServletResponse response)
					throws NoSuchAlgorithmException, IOException {
		response.setContentType("application/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		ArrayNode root = XMLUtil.createArrayNode();
		
		if(request.getSession().getAttribute("validcode").equals(yzm)) {
			if(userService.findByName(name) != null) {
				root.put(XMLUtil.createObjectNode().put("success", false));
				root.put(XMLUtil.createObjectNode().put("msg", "用户名已经被注册！"));
				root.put(XMLUtil.createObjectNode().put("url", ""));
			} else if(userService.findByEmail(email) != null) {
				root.put(XMLUtil.createObjectNode().put("success", false));
				root.put(XMLUtil.createObjectNode().put("msg", "邮箱已经被注册！"));
				root.put(XMLUtil.createObjectNode().put("url", ""));
			} else {
				User user = new User();
				user.setName(name);
				user.setPassword(PasswordUtil.getEncodeString(pass));
				user.setEmail(email);
				user.setRegTime(new Date());
				user.setUserType(User.CUSTOMER);
				user.setAccount(new Account());
				userService.saveUser(user);
				WebUtil.setCookie(response, "login.ishare", user.getName());
				root.put(XMLUtil.createObjectNode().put("success", true));
				root.put(XMLUtil.createObjectNode().put("msg", ""));
				root.put(XMLUtil.createObjectNode().put("url", "view/login"));
			}
		} else {
			root.put(XMLUtil.createObjectNode().put("success", false));
			root.put(XMLUtil.createObjectNode().put("msg", "验证码错误！"));
			root.put(XMLUtil.createObjectNode().put("url", ""));
		}
		
		out.println(root);
		out.flush();
		out.close();
	}
	
	/**
	 * 验证用户名，电子邮箱和验证码
	 * @param v
	 * @param cmd 验证类型：name，email，validcode
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@RequestMapping(value="/view/register/check")
	public void checkUserInfo(@RequestParam String v, @RequestParam String cmd,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		ArrayNode root = XMLUtil.createArrayNode();
		
		boolean flag = false;
		if(cmd.equals("name")) {// 验证用户名，用户名不能重复
			User user = userService.findByName(v);
			if(user == null) flag = true;
		} else if(cmd.equals("email")) {// 验证电子邮箱，每个电子邮箱只能注册一个帐号
			User user = userService.findByEmail(v);
			if(user == null) flag = true;
		} else {// 验证验证码
			if(v.equals(req.getSession().getAttribute("validcode")))
				flag = true;
		}
		resp.setContentType("text/xml;charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");

		if(flag == true) {
			root.put(XMLUtil.createObjectNode().put("num", 0));
		} else {
			root.put(XMLUtil.createObjectNode().put("num", 1));
		}
		out.println(root);
		out.flush();
		out.close();
	}
	
	/**
	 * 返回随机数字验证码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/view/validate")
	public void handelValidCode(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		// 在内存中创建图象
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		String sRand = ValidateCodeUtil.createImage(image);
		
		// 将认证码存入request
		request.getSession().setAttribute("validcode", sRand);
		
		OutputStream output = response.getOutputStream();

		// 输出图象到页面
		ImageIO.write(image, "JPEG", output);
		output.flush();
	}

	public UserService getUserService() {
		return userService;
	}

	@Resource(name="userServiceImpl")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
