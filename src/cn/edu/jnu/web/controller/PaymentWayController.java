package cn.edu.jnu.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.entity.order.Order;
import cn.edu.jnu.web.entity.order.OrderState;
import cn.edu.jnu.web.entity.order.PaymentWay;
import cn.edu.jnu.web.service.OrderService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 支付方式控制器
 * @author HHT
 *
 */
@Controller
public class PaymentWayController {
	private ObjectMapper mapper = new ObjectMapper();
	
	private OrderService orderService;
	
	/**
	 * 显示所有支付方式，供前台订单生成页面选择
	 * @return
	 */
	@RequestMapping(value="/control/admin/paymentway.do", params="method=listcombo")
	public @ResponseBody ObjectNode handlerPaymentWayList() {
		ObjectNode json = mapper.createObjectNode();
		ArrayNode records = mapper.createArrayNode();
		for(PaymentWay pw : PaymentWay.values()) {
			ObjectNode node = mapper.createObjectNode();
			node.put("name", pw.getName());
			node.put("value", pw.toString());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}
	
	/**
	 * 修改订单支付方式
	 * @param oid
	 * @param paymentway
	 * @return
	 */
	@RequestMapping(value="/control/admin/paymentway.do", params="method=setpaymentway")
	public @ResponseBody ObjectNode setPaymentWay(@RequestParam String oid,
			@RequestParam String paymentway) {
		Order order = orderService.findById(oid);
		if(order == null || 
				!order.getState().getName().equals(OrderState.WAITCONFIRM.getName())) {
			return mapper.createObjectNode().put("success", false);
		}
		PaymentWay pw = PaymentWay.getPaymentWay(paymentway);
		if(pw == null) return mapper.createObjectNode().put("success", false);
		order.setPaymentWay(pw);
		orderService.updateOrder(order);
		return mapper.createObjectNode().put("success", true).put("msg", pw.getName());
	}

	public OrderService getOrderService() {
		return orderService;
	}
	@Resource(name="orderServiceImpl")
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
}
