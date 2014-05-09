package cn.edu.jnu.web.controller;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.entity.emp.EmpDate;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.EmpDateService;
import cn.edu.jnu.web.util.QueryResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 员工备忘录控制器
 * @author HHT
 *
 */
@Controller
public class EmpDateController {
	private ObjectMapper mapper = new ObjectMapper();
	
	private EmpDateService empDateService;
	
	/**
	 * 新增备忘录
	 * @param title
	 * @param activedate
	 * @param activetime
	 * @param content
	 * @param remark
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/empdate.do", params="method=add")
	public @ResponseBody ObjectNode handlerEmpDateAdd(@RequestParam String title,
			@RequestParam String activedate, @RequestParam String activetime,
			@RequestParam String content, @RequestParam String remark, HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		
		EmpDate ed = new EmpDate();
		ed.setContent(content);
		ed.setTitle(title);
		ed.setRemark(remark);
		ed.setCreateEmp(emp);
		ed.setActiveTime(activedate + " " + activetime);
		empDateService.AddEmpDate(ed);
		
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 删除备忘录
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/empdate.do", params="method=del")
	public @ResponseBody ObjectNode handlerEmpDateDel(@RequestParam int[] ids, 
			HttpServletRequest request) {
		//User emp = (User) request.getSession().getAttribute("employee");
		
		for(int id : ids) {
			empDateService.deleteEmpDate(id);
		}
		
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 列表显示备忘录
	 * @param start
	 * @param limit
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/empdate.do", params="method=list")
	public @ResponseBody ObjectNode handlerEmpDateList(@RequestParam int start,
			@RequestParam int limit, HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		String[] ands = new String[] {"createEmp.name='" + emp.getName() + "'"};
		QueryResult<EmpDate> res = empDateService.listEmpDates(start, limit, ands);
		return encode2JSON(res);
	}

	private ObjectNode encode2JSON(QueryResult<EmpDate> res) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ObjectNode json = mapper.createObjectNode();
		json.put("count", res.getTotal());
		ArrayNode records = mapper.createArrayNode();
		for(EmpDate ed : res.getResults()) {
			ObjectNode node = mapper.createObjectNode();
			node.put("id", ed.getId());
			node.put("title", ed.getTitle());
			node.put("content", ed.getContent());
			node.put("logger", ed.getCreateEmp().getName());
			node.put("logtime", sdf.format(ed.getCreateTime()));
			node.put("activetime", ed.getActiveTime());
			node.put("remark", ed.getRemark());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}

	public EmpDateService getEmpDateService() {
		return empDateService;
	}
	@Resource(name="empDateServiceImpl")
	public void setEmpDateService(EmpDateService empDateService) {
		this.empDateService = empDateService;
	}
}
