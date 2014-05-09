package cn.edu.jnu.web.controller;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.entity.order.Message;
import cn.edu.jnu.web.entity.order.Order;
import cn.edu.jnu.web.service.OrderMsgService;
import cn.edu.jnu.web.service.OrderService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 订单客服留言控制器
 * @author HHT
 *
 */
@Controller
public class OrderMsgController {
	
	private ObjectMapper mapper = new ObjectMapper();
	private OrderMsgService orderMsgService;
	private OrderService orderService;
	
	/**
	 * 列表显示客服留言
	 * @param oid
	 * @return
	 */
	@RequestMapping(value="/control/admin/ordermsg.do", params="method=list")
	public @ResponseBody ObjectNode handlerList(@RequestParam String oid) {
		return encode2JSON(orderMsgService.findByOrderId(oid));
	}
	
	/**
	 * 添加客服留言
	 * @param oid
	 * @param msg
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/control/admin/ordermsg.do", params="method=add")
	public @ResponseBody ObjectNode handlerAdd(@RequestParam String oid,
			@RequestParam String msg, @RequestParam String name) {
		Order order = orderService.findById(oid);
		if(order == null) {
			return mapper.createObjectNode().put("success", false);
		} else {
			/*Message message = new Message();
			message.setContent(msg);
			message.setUsername(name);
			message.setOrder(order);
			orderMsgService.saveMessage(message);*/
			order.addOrderMsg(msg, name);
			orderService.updateOrder(order);
			return mapper.createObjectNode().put("success", true);
		}
	}

	private ObjectNode encode2JSON(List<Message> items) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ObjectNode json = mapper.createObjectNode();
		ArrayNode records = mapper.createArrayNode();
		Iterator<Message> it = items.iterator();
		while(it.hasNext()) {
			ObjectNode node = mapper.createObjectNode();
			Message msg = it.next();
			node.put("id", msg.getId());
			node.put("name", msg.getUsername());
			node.put("content", msg.getContent());
			node.put("time", sdf.format(msg.getCreatetime()));
			records.add(node);
		}
		json.put("records", records);
		return json;
	}

	public OrderMsgService getOrderItemService() {
		return orderMsgService;
	}
	@Resource(name="orderMsgServiceImpl")
	public void setOrderMsgService(OrderMsgService orderMsgService) {
		this.orderMsgService = orderMsgService;
	}

	public OrderService getOrderService() {
		return orderService;
	}
	@Resource(name="orderServiceImpl")
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
}
