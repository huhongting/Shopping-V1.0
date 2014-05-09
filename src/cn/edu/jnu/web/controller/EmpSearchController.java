package cn.edu.jnu.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.entity.user.ContactInfo;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.UserUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 员工查询控制器
 * @author HHT
 *
 */
@Controller
public class EmpSearchController {
	private ObjectMapper mapper = new ObjectMapper();
	
	private UserService userService;
	
	/**
	 * 根据姓名、邮件、手机、地址查询员工信息
	 * @param request
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/control/admin/empsearch.do")
	public @ResponseBody ObjectNode handlerSearch(HttpServletRequest request,
			@RequestParam int start, @RequestParam int limit) {
		
		String empname = UserUtil.getQueryString(request, "empname");
		String empmail = UserUtil.getQueryString(request, "empmail");
		String mobile = UserUtil.getQueryString(request, "mobile");
		String delflag = UserUtil.getQueryString(request, "delflag");
		String address = UserUtil.getQueryString(request, "address");
		
		String[] ands = createQuerySQL(empname, empmail, mobile, delflag, address);
		QueryResult<User> emps = userService.listUsers(start, limit, ands, null);
		return encode2JSON(emps);
	}

	private ObjectNode encode2JSON(QueryResult<User> emps) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ObjectNode json = mapper.createObjectNode();
		json.put("count", emps.getTotal());
		ArrayNode records = mapper.createArrayNode();
		for(User emp : emps.getResults()) {
			ObjectNode node = mapper.createObjectNode();
			node.put("userid", emp.getId());
			node.put("username", emp.getName());
			node.put("email", emp.getEmail());
			node.put("usertype", emp.getUserType());
			node.put("regtime", sdf.format(emp.getRegTime()));
			//node.put("lastlogin", emp.getLastLogin() == null ? "" : sdf.format(emp.getLastLogin()));
			node.put("note", emp.getNote());
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
			records.add(node);
		}
		json.put("records", records);
		return json;
	}

	/**
	 * 生成查询语句
	 * @param empname
	 * @param empmail
	 * @param mobile
	 * @param delflag
	 * @param address
	 * @return
	 */
	private String[] createQuerySQL(String empname, String empmail,
			String mobile, String delflag, String address) {
		List<String> ands = new ArrayList<String>();
		if(null != empname && !empname.equals("null") && !empname.equals("")) {
			ands.add("name like '%" + empname + "%'");
		}
		if(null != empmail && !empmail.equals("null") && !empmail.equals("")) {
			ands.add("email like '%" + empmail + "%'");
		}
		if(null != mobile && !mobile.equals("null") && !mobile.equals("")) {
			ands.add("contactInfo.mobile like '%" + mobile + "%'");
		}
		if(null != address && !address.equals("null") && !address.equals("")) {
			ands.add("contactInfo.address like '%" + address + "%'");
		}
		if(null != delflag && delflag.equals("on")) {
			ands.add("deleted = true");
		} else {
			ands.add("deleted = false");
		}
		ands.add("usertype != " + User.CUSTOMER);
		return ands.toArray(new String[]{});
	}

	public UserService getUserService() {
		return userService;
	}
	@Resource(name="userServiceImpl")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
