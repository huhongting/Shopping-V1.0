package cn.edu.jnu.web.controller;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.UserUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 用户管理控制器
 * @author HHT
 *
 */
@Controller
public class UserController {
	
	private UserService userService;
	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 从已删除用户中恢复用户
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/control/admin/user.do", params="method=reset")
	public @ResponseBody ObjectNode handlerReset(
			@RequestParam int id) {
		if(userService.reset(id))
			return mapper.createObjectNode().put("success", true);
		else
			return mapper.createObjectNode().put("success", false);
	}
	
	/**
	 * 显示已删除用户
	 * @param start
	 * @param limit
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/user.do", params="method=listdel")
	public @ResponseBody ObjectNode handlerQueryDel(
			@RequestParam int start, @RequestParam int limit, HttpServletRequest request) {
		String query = UserUtil.getQueryString(request, "query");
		String[] ands = new String[]{"deleted=true", "usertype='" + User.CUSTOMER + "'"};
		if(query == null) {
			return encodeJSON(userService.listUsers(start, limit, ands));
		} else {
			List<String> keys = UserUtil.getQueryKeys(query);
			String[] ors = UserUtil.createSearchQuerySQL(keys, new String[]{"name"});
			return encodeJSON(userService.listUsers(start, limit, ands, ors));
		}
	}
	
	/**
	 * 显示当前可用用户
	 * @param start
	 * @param limit
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/user.do", params="method=list")
	public @ResponseBody ObjectNode handlerQueryUser(
			@RequestParam int start, @RequestParam int limit, HttpServletRequest request) {
		String query = UserUtil.getQueryString(request, "query");
		String[] ands = new String[]{"deleted=false", "usertype='" + User.CUSTOMER + "'"};
		if(query == null) {
			return encodeJSON(userService.listUsers(start, limit, ands));
		} else {
			List<String> keys = UserUtil.getQueryKeys(query);
			String[] ors = UserUtil.createSearchQuerySQL(keys, new String[]{"name"});
			return encodeJSON(userService.listUsers(start, limit, ands, ors));
		}
	}
	
	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/control/admin/user.do", params="method=del")
	public @ResponseBody ObjectNode handlerDel(
			@RequestParam int id) {
		//userService.deleteUser(id);
		User user = userService.findById(id, false);
		if(user == null || user.getUserType() == User.ADMINISTRATOR) 
			return mapper.createObjectNode().put("success", false);
		userService.deleteUser(id);
		return mapper.createObjectNode().put("success", true);
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
			node.put("lastlogin", user.getLastLogin() == null ? "" : sdf.format(user.getLastLogin()));
			node.put("note", user.getNote());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}

	public UserService getUserService() {
		return userService;
	}
	@Resource(name="userServiceImpl")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}
