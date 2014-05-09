package cn.edu.jnu.web.controller;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.entity.emp.Notification;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.NotificationService;
import cn.edu.jnu.web.util.QueryResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 系统通知控制器
 * @author HHT
 *
 */
@Controller
public class NotificationController {
	private ObjectMapper mapper = new ObjectMapper();
	
	private NotificationService notifyService;
	
	/**
	 * 显示通知状态列表
	 * @return
	 */
	@RequestMapping(value="/control/admin/notification/status.do", params="method=list")
	public @ResponseBody ObjectNode listStatus() {
		ObjectNode json = mapper.createObjectNode();
		json.put("count", 2);
		ArrayNode records = mapper.createArrayNode();
		records.add(
				mapper.createObjectNode()
					  .put("value", Notification.NOTIFICATIONTYPE_NORMAL)
					  .put("name", "普通"));
		records.add(
				mapper.createObjectNode()
					  .put("value", Notification.NOTIFICATIONTYPE_IMPORTANT)
					  .put("name", "重要"));
		json.put("records", records);
		return json;
	}
	
	/**
	 * 添加新通知
	 * @param title
	 * @param content
	 * @param status
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/notification.do", params="method=add")
	public @ResponseBody ObjectNode handlernotificationAdd(@RequestParam String title,
			@RequestParam String content, @RequestParam String status, 
			HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		Notification no = new Notification();
		no.setTitle(title);
		no.setContent(content);
		no.setCreater(emp);
		no.setStatus(status);
		notifyService.AddNotification(no);
		
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 删除已有通知
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/notification.do", params="method=del")
	public @ResponseBody ObjectNode handlernotificationDel(@RequestParam int[] ids, 
			HttpServletRequest request) {
		//User emp = (User) request.getSession().getAttribute("employee");
		
		for(int id : ids) {
			notifyService.deleteNotification(id);
		}
		
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 列表显示通知
	 * @param start
	 * @param limit
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/notification.do", params="method=list")
	public @ResponseBody ObjectNode handlernotificationList(@RequestParam int start,
			@RequestParam int limit, HttpServletRequest request) {
		//User emp = (User) request.getSession().getAttribute("employee");
		QueryResult<Notification> res = notifyService.listNotifications(start, limit, null);
		return encode2JSON(res);
	}

	private ObjectNode encode2JSON(QueryResult<Notification> res) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ObjectNode json = mapper.createObjectNode();
		json.put("count", res.getTotal());
		ArrayNode records = mapper.createArrayNode();
		for(Notification no : res.getResults()) {
			ObjectNode node = mapper.createObjectNode();
			node.put("id", no.getId());
			node.put("title", no.getTitle());
			node.put("content", no.getContent());
			node.put("creater", no.getCreater().getName());
			node.put("createdate", sdf.format(no.getCreateDate()));
			node.put("status", no.getStatus());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}

	public NotificationService getNotifyService() {
		return notifyService;
	}
	@Resource(name="notificationServiceImpl")
	public void setNotifyService(NotificationService notifyService) {
		this.notifyService = notifyService;
	}
}
