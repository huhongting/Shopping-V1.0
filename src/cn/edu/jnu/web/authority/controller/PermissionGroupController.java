package cn.edu.jnu.web.authority.controller;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.authority.AuthorityType;
import cn.edu.jnu.web.entity.permission.Group;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.service.permission.GroupService;
import cn.edu.jnu.web.util.AuthorityUtil;
import cn.edu.jnu.web.util.QueryResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 权限组管理控制器
 * @author HHT
 *
 */
@Controller
public class PermissionGroupController {
	private ObjectMapper mapper = new ObjectMapper();
	
	private GroupService groupService;
	private UserService userService;
	
	/**
	 * 添加新权限组
	 * @param request
	 * @param permissionName
	 * @param remark
	 * @return
	 */
	@RequestMapping(value="/control/admin/permissiongroup.do", params="method=add")
	public @ResponseBody ObjectNode addPermission(HttpServletRequest request,
			@RequestParam String permissionName, @RequestParam String remark) {
		User emp = (User) request.getSession().getAttribute("employee");
		Group group = new Group();
		group.setCreater(emp);
		group.setName(permissionName);
		group.setRemark(remark);
		String authority = createAuthority(request);
		group.setAuthority(authority);
		groupService.saveGroup(group);
		
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 删除权限组
	 * @param request
	 * @param gid
	 * @return
	 */
	@RequestMapping(value="/control/admin/permissiongroup.do", params="method=del")
	public @ResponseBody ObjectNode addPermission(HttpServletRequest request,
			@RequestParam int[] gid) {
		User emp = (User) request.getSession().getAttribute("employee");
		for(int id : gid) {
			Group group = groupService.findGroup(id);
			Set<User> emps = group.getEmps();
			Iterator<User> it = emps.iterator();
			while(it.hasNext()) {
				User user = it.next();
				user.setGroup(null);
				userService.updateUser(user);
			}
			groupService.deleteGroup(id);
		}
		request.setAttribute("employee", userService.findById(emp.getId(), false));
		
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 权限显示
	 * @param request
	 * @return
	 */
	private String createAuthority(HttpServletRequest request) {
		StringBuffer authority = new StringBuffer();
		for(AuthorityType at : AuthorityType.values()) {
			String s = request.getParameter(""+at.getIndex());
			if(s != null && s.equalsIgnoreCase("on")) {
				authority.append(1);
			} else {
				authority.append(0);
			}
		}
		return authority.toString();
	}

	/**
	 * 显示系统权限
	 * @return
	 */
	@RequestMapping(value="/control/admin/permissiongroup.do", params="method=listpermission")
	public @ResponseBody ObjectNode listPermissions() {
		ObjectNode json = mapper.createObjectNode();
		AuthorityType[] ats = AuthorityType.values();
		json.put("count", ats.length);
		ArrayNode records = mapper.createArrayNode();
		for(AuthorityType at : ats) {
			ObjectNode node = mapper.createObjectNode();
			node.put("index", at.getIndex());
			node.put("name", at.getName());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}
	
	/**
	 * 显示权限组
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/control/admin/permissiongroup.do", params="method=list")
	public @ResponseBody ObjectNode listPermissionGroup(@RequestParam int start,
			@RequestParam int limit) {
		QueryResult<Group> res = groupService.listGroups(start, limit, null);
		
		return encode2JSON(res);
	}

	private ObjectNode encode2JSON(QueryResult<Group> res) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ObjectNode json = mapper.createObjectNode();
		json.put("count", res.getTotal());
		ArrayNode records = mapper.createArrayNode();
		for(Group group : res.getResults()) {
			ObjectNode node = mapper.createObjectNode();
			node.put("id", group.getId());
			node.put("name", group.getName());
			node.put("creater", group.getCreater().getName());
			node.put("createdate", sdf.format(group.getCreateDate()));
			node.put("authority", AuthorityUtil.createAuthorityString(group));
			node.put("remark", group.getRemark());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}

	public GroupService getGroupService() {
		return groupService;
	}
	@Resource(name="groupServiceImpl")
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public UserService getUserService() {
		return userService;
	}
	@Resource(name="userServiceImpl")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
