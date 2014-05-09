package cn.edu.jnu.web.controller;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.edu.jnu.web.entity.Press;
import cn.edu.jnu.web.service.PressService;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.UserUtil;

/**
 * 出版社控制器
 * @author HHT
 *
 */
@Controller
public class PressController {
	
	private PressService pressService;
	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 添加新出版社
	 * @param pressname
	 * @param remark
	 * @return
	 */
	@RequestMapping(value="/control/admin/press.do", params="method=add")
	public @ResponseBody ObjectNode handlerAdd(@RequestParam String pressname, 
			@RequestParam String remark) {
		Press p = pressService.findPress(pressname);
		if(p == null) {
			Press press = new Press();
			press.setName(pressname);
			press.setRemark(remark);
			pressService.savePress(press);
			return mapper.createObjectNode().put("success", true);
		} else {// 如果已经存在，则添加失败。
			return mapper.createObjectNode().put("success", false);
		}
	}
	
	/**
	 * 删除出版社
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/control/admin/press.do", params="method=del")
	public @ResponseBody ObjectNode handlerDel(@RequestParam int[] ids) {
		pressService.deletePress(ids);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 更新出版社
	 * @param id
	 * @param pressname
	 * @param remark
	 * @param delflag
	 * @return
	 */
	@RequestMapping(value="/control/admin/press.do", params="method=update")
	public @ResponseBody ObjectNode handlerUpdate(@RequestParam int id, 
			@RequestParam String pressname, @RequestParam String remark,
			@RequestParam boolean delflag) {
		Press press = pressService.findPress(id);
		if(press == null) {// 如果不存在，则更新失败。
			return mapper.createObjectNode().put("success", false);
		}
		press.setName(pressname);
		press.setRemark(remark);
		press.setDeleted(delflag);
		pressService.updatePress(press);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 查询出版社
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/control/admin/press.do", params="method=list")
	public @ResponseBody ObjectNode handlerQuery(@RequestParam String start, 
			@RequestParam String limit, HttpServletRequest request) {
		//return encodeJSON(pressService.list(start, limit));
		String query = UserUtil.getQueryString(request, "query");
		//String[] ands = new String[] {"deleted=false"};
		if(query == null) {
			return encodeJSON(pressService.list(start, limit, null));
		} else {
			List<String> keys = UserUtil.getQueryKeys(query);
			String[] ors = UserUtil.createSearchQuerySQL(keys, new String[]{"pressname"});
			return encodeJSON(pressService.list(start, limit, null, ors));
		}
	}
	

	/**
	 * 将查询结果转换成JSON格式。
	 * @param list
	 * @return
	 */
	public ObjectNode encodeJSON(QueryResult<Press> list) {
		ObjectNode json = mapper.createObjectNode();
		ArrayNode records = mapper.createArrayNode();
		json.put("count", list.getTotal());
		Iterator<Press> it = list.getIterator();
		while(it.hasNext()) {
			ObjectNode node = mapper.createObjectNode();
			Press press = it.next();
			node.put("pressid", press.getId());
			node.put("pressname", press.getName());
			node.put("remark", press.getRemark());
			node.put("delflag", press.getDeleted());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}
	
	/**
	 * 查询可用出版社名称，供客户端生成下拉选择列表。
	 * @return
	 */
	@RequestMapping(value="/control/admin/press.do", params="method=listcombo")
	public @ResponseBody ObjectNode handlerCombo() {
		return encodeComboJSON(pressService.list("-1", "-1", new String[] {"deleted=false"}));
	}
	
	/**
	 * 将查询结果转换成JSON格式，供客户端生成下拉选择列表。
	 * @param list
	 * @return
	 */
	private ObjectNode encodeComboJSON(QueryResult<Press> list) {
		ObjectNode json = mapper.createObjectNode();
		ArrayNode records = mapper.createArrayNode();
		json.put("count", list.getTotal());
		
		Iterator<Press> it = list.getIterator();
		while(it.hasNext()) {
			ObjectNode node = mapper.createObjectNode();
			Press press = it.next();
			node.put("name", press.getName());
			node.put("id", press.getId());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}

	public PressService getPressService() {
		return pressService;
	}

	@Resource(name="pressServiceImpl")
	public void setPressService(PressService pressService) {
		this.pressService = pressService;
	}
}
