package cn.edu.jnu.web.controller;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.entity.permission.Group;
import cn.edu.jnu.web.entity.user.Account;
import cn.edu.jnu.web.entity.user.ContactInfo;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.service.permission.GroupService;
import cn.edu.jnu.web.util.AuthorityUtil;
import cn.edu.jnu.web.util.MailSender;
import cn.edu.jnu.web.util.PasswordUtil;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.UserUtil;
import cn.edu.jnu.web.view.controller.PassbackController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 员工管理控制器
 * @author HHT
 *
 */
@Controller
public class EmployeeController {
	private ObjectMapper mapper = new ObjectMapper();
	
	private UserService userService;
	private GroupService groupService;

	/**
	 * 显示员工列表
	 * @param start
	 * @param limit
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/employee.do", params="method=list")
	public @ResponseBody ObjectNode handlerQueryEmployee(
			@RequestParam int start, @RequestParam int limit, HttpServletRequest request) {
		String query = UserUtil.getQueryString(request, "query");
		String[] ands = new String[]{"deleted=false", "usertype!='" + User.CUSTOMER + "'"};
		if(query == null) {
			return encodeJSON(userService.listUsers(start, limit, ands));
		} else {
			List<String> keys = UserUtil.getQueryKeys(query);
			String[] ors = UserUtil.createSearchQuerySQL(keys, new String[]{"name"});
			return encodeJSON(userService.listUsers(start, limit, ands, ors));
		}
	}
	
	/**
	 * 批量删除员工
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/employee.do", params="method=del")
	public @ResponseBody ObjectNode handlerDel(
			@RequestParam int[] id, HttpServletRequest request) {
		//userService.deleteUser(id);
		User emp = (User) request.getSession().getAttribute("employee");
		for(int i : id) {
			User user = userService.findById(i, false);
			if(user == null || user.getId() == emp.getId()) 
				return mapper.createObjectNode().put("success", false);
			userService.deleteUser(i);
		}
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 从已删除员工中恢复员工信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/control/admin/employee.do", params="method=reset")
	public @ResponseBody ObjectNode handlerReset(
			@RequestParam int[] id) {
		//userService.deleteUser(id);
		for(int i : id) {
			User user = userService.findById(i, true);
			if(user == null) 
				return mapper.createObjectNode().put("success", false);
			user.setDelete(false);
			userService.updateUser(user);
		}
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 列表显示已删除员工
	 * @param start
	 * @param limit
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/employee.do", params="method=listdel")
	public @ResponseBody ObjectNode handlerQueryDelEmployee(
			@RequestParam int start, @RequestParam int limit, HttpServletRequest request) {
		String query = UserUtil.getQueryString(request, "query");
		String[] ands = new String[]{"deleted=true", "usertype!='" + User.CUSTOMER + "'"};
		if(query == null) {
			return encodeJSON(userService.listUsers(start, limit, ands));
		} else {
			List<String> keys = UserUtil.getQueryKeys(query);
			String[] ors = UserUtil.createSearchQuerySQL(keys, new String[]{"name"});
			return encodeJSON(userService.listUsers(start, limit, ands, ors));
		}
	}
	
	/**
	 * 添加新员工
	 * @param empname
	 * @param empmail
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping(value="/control/admin/employee.do", params="method=add")
	public @ResponseBody ObjectNode handlerAddEmployee(
			@RequestParam String empname, @RequestParam String empmail) 
					throws NoSuchAlgorithmException {

		User user = userService.findByEmail(empmail);
		if(user != null) {
			return mapper.createObjectNode()
					.put("success", false)
					.put("msg", "该邮箱已经被注册！");
		}
		User emp = new User();
		emp.setAccount(new Account());
		emp.setUserType(User.EMPLOYEE);
		emp.setName(empname);
		emp.setEmail(empmail);

		String pass = PasswordUtil.createPass(6);// 重置密码
		String s = PasswordUtil.getEncodeString(pass);
		emp.setPassword(s);
		
		sendMail(empname, empmail, pass);
		
		userService.saveUser(emp);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 显示员工联系信息
	 * @param uname
	 * @param mobile
	 * @param phone
	 * @param address
	 * @param postcode
	 * @return
	 */
	@RequestMapping(value="/control/admin/empcontact.do")
	public @ResponseBody ObjectNode handlerEmpInfo(@RequestParam String uname,
			@RequestParam String mobile, @RequestParam String phone, 
			@RequestParam String address, @RequestParam String postcode) {
		User emp = userService.findByName(uname);
		if(emp == null) return mapper.createObjectNode().put("success", false);
		ContactInfo ci = new ContactInfo();
		ci.setAddress(address);
		ci.setMobile(mobile);
		ci.setPhone(phone);
		ci.setPostalcode(postcode);
		emp.setContactInfo(ci);
		userService.updateUser(emp);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 显示员工基本信息
	 * @param uid
	 * @return
	 */
	@RequestMapping(value="/control/admin/employeeinfo.do")
	public @ResponseBody ObjectNode handlerEmpInfo(@RequestParam int uid) {
		User emp = userService.findById(uid, true);
		if(emp == null) return null;
		return encodeInfo2JSON(emp);
	}
	
	/**
	 * 根据姓名查询员工
	 * @param uname
	 * @return
	 */
	@RequestMapping(value="/control/admin/employeeinfo.do", params="tag=name")
	public @ResponseBody ObjectNode handlerEmpInfo(@RequestParam String uname) {
		User emp = userService.findByName(uname);
		if(emp == null) return null;
		return encodeInfo2JSON(emp);
	}
	
	/**
	 * 已废弃
	 * @return
	 */
	@RequestMapping(value="/control/admin/employee.do", params="method=listutype")
	public @ResponseBody ObjectNode handlerEmpType() {
		ObjectNode json = mapper.createObjectNode();
		ArrayNode records = mapper.createArrayNode();
		records.add(mapper.createObjectNode()
							.put("type", User.ADMINISTRATOR)
							.put("value", "超级管理员"));
		records.add(mapper.createObjectNode()
				.put("type", User.EMPLOYEE)
				.put("value", "公司职员"));
		json.put("records", records);
		return json;
	}
	
	/**
	 * 为员工配置权限组
	 * @param ids
	 * @param utype
	 * @param group
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/employee.do", params="method=setgroup")
	public @ResponseBody ObjectNode setEmpType(@RequestParam int[] ids, @RequestParam int group, 
			HttpServletRequest request) {
		Group g = groupService.findGroup(group);
		for(int id : ids) {
			User emp = userService.findById(id, false);
			emp.setGroup(g);
			//emp.setUserType(utype);
			userService.updateUser(emp);
			//g.getEmps().add(emp);
		}
		//groupService.updateGroup(g);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 更新员工信息
	 * @param uname
	 * @param username
	 * @param email
	 * @param phone
	 * @param mobile
	 * @param address
	 * @param postcode
	 * @param note
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/employee.do", params="method=update")
	public @ResponseBody ObjectNode setUpdateEmp(@RequestParam String uname,
			@RequestParam String username, @RequestParam String email, 
			@RequestParam String phone, @RequestParam String mobile,
			@RequestParam String address, @RequestParam String postcode,
			@RequestParam String note, HttpServletRequest request) {
		User emp = userService.findByName(uname);
		if(emp == null) return mapper.createObjectNode().put("success", false);
		emp.setName(username);
		emp.setEmail(email);
		emp.setNote(note);
		ContactInfo ci = emp.getContactInfo();
		if(ci == null) ci = new ContactInfo();
		ci.setAddress(address);
		ci.setMobile(mobile);
		ci.setPhone(phone);
		ci.setPostalcode(postcode);
		emp.setContactInfo(ci);
		userService.updateUser(emp);
		request.getSession().setAttribute("employee", emp);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 重置密码
	 * @param uname
	 * @param pass
	 * @param newpass
	 * @param request
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping(value="/control/admin/employee.do", params="method=resetpass")
	public @ResponseBody ObjectNode setEmpType(@RequestParam String uname,
			@RequestParam String pass, @RequestParam String newpass,
			HttpServletRequest request) 
					throws NoSuchAlgorithmException {
		User emp = userService.findByName(uname);
		if(emp == null) return mapper.createObjectNode().put("success", false);
		String p = PasswordUtil.getEncodeString(pass);
		if(emp.getPassword().equals(p)) {
			emp.setPassword(PasswordUtil.getEncodeString(newpass));
			userService.updateUser(emp);
			request.getSession().setAttribute("employee", emp);
			return mapper.createObjectNode().put("success", true);
		} else {
			return mapper.createObjectNode().put("success", false);
		}
	}
	
	private ObjectNode encodeInfo2JSON(User emp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ObjectNode json = mapper.createObjectNode();
		json.put("success", true);
		ObjectNode node = mapper.createObjectNode();
		node.put("userid", emp.getId());
		node.put("username", emp.getName());
		node.put("email", emp.getEmail());
		node.put("usertype", emp.getUserType() == User.ADMINISTRATOR ? "超级管理员" : "公司职员");
		node.put("regtime", sdf.format(emp.getRegTime()));
		node.put("lastlogin", emp.getLastLogin() == null ? "" : sdf.format(emp.getLastLogin()));
		node.put("note", emp.getNote());
		node.put("authority", AuthorityUtil.createAuthorityString(emp.getGroup()));
		ContactInfo ci = emp.getContactInfo();
		if(ci == null) {
			node.put("mobile", "");
			node.put("phone", "");
			node.put("address", "");
			node.put("postcode", "");
		} else {
			node.put("mobile", ci.getMobile());
			node.put("phone", ci.getPhone());
			node.put("address", ci.getAddress());
			node.put("postcode", ci.getPostalcode());			
		}
		json.put("data", node);
		return json;
	}
	
	/**
	 * 将查询结果转换成JSON格式。
	 * @param list
	 * @return
	 */
	public ObjectNode encodeJSON(QueryResult<User> list) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ObjectNode json = mapper.createObjectNode();
		ArrayNode records = mapper.createArrayNode();
		json.put("count", list.getTotal());
		Iterator<User> it = list.getIterator();
		while(it.hasNext()) {
			ObjectNode node = mapper.createObjectNode();
			User user = it.next();
			node.put("userid", user.getId());
			node.put("username", user.getName());
			node.put("email", user.getEmail());
			node.put("usertype", user.getUserType());
			node.put("regtime", sdf.format(user.getRegTime()));
			node.put("group", user.getGroup() == null ? "" : user.getGroup().getName());
			node.put("authority", AuthorityUtil.createAuthorityString(user.getGroup()));
			//node.put("lastlogin", user.getLastLogin() == null ? "" : sdf.format(user.getLastLogin()));
			node.put("note", user.getNote());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}

	/**
	 * 后台发送重置密码邮件
	 * @param empname
	 * @param empmail
	 * @param pass
	 */
	private void sendMail(final String empname, 
			final String empmail, final String pass) {
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					MailSender.send(
							empmail, 
							"IShare.com-新员工初始密码", 
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
	public UserService getUserService() {
		return userService;
	}
	@Resource(name="userServiceImpl")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public GroupService getGroupService() {
		return groupService;
	}

	@Resource(name="groupServiceImpl")
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
}
