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

import cn.edu.jnu.web.entity.order.DeliverWay;
import cn.edu.jnu.web.entity.order.Order;
import cn.edu.jnu.web.entity.order.OrderState;
import cn.edu.jnu.web.entity.order.PaymentWay;
import cn.edu.jnu.web.service.OrderService;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.UserUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 订单搜索控制器
 * @author HHT
 *
 */
@Controller
public class OrderSearchController {
	private ObjectMapper mapper = new ObjectMapper();
	
	private OrderService orderService;
	
	/**
	 * 显示订单状态，供搜索时下拉选择
	 * @return
	 */
	@RequestMapping(value="/control/admin/ordersearch.do", params="method=state")
	public @ResponseBody ObjectNode handlerState() {
		OrderState[] oss = OrderState.values();
		ObjectNode json = mapper.createObjectNode();
		json.put("count", oss.length);
		ArrayNode records = mapper.createArrayNode();
		for(OrderState os : oss) {
			ObjectNode node = mapper.createObjectNode();
			node.put("value", os.toString());
			node.put("name", os.getName());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}
	
	/**
	 * 显示支付方式，供搜索时下拉选择
	 * @return
	 */
	@RequestMapping(value="/control/admin/ordersearch.do", params="method=paymentway")
	public @ResponseBody ObjectNode handlerPaymentWay() {
		PaymentWay[] pws = PaymentWay.values();
		ObjectNode json = mapper.createObjectNode();
		json.put("count", pws.length);
		ArrayNode records = mapper.createArrayNode();
		for(PaymentWay pw : pws) {
			ObjectNode node = mapper.createObjectNode();
			node.put("value", pw.toString());
			node.put("name", pw.getName());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}
	
	/**
	 * 显示配送方式，供搜索时下拉选择
	 * @return
	 */
	@RequestMapping(value="/control/admin/ordersearch.do", params="method=deliverway")
	public @ResponseBody ObjectNode handlerDeliverWay() {
		DeliverWay[] dws = DeliverWay.values();
		ObjectNode json = mapper.createObjectNode();
		json.put("count", dws.length);
		ArrayNode records = mapper.createArrayNode();
		for(DeliverWay dw : dws) {
			ObjectNode node = mapper.createObjectNode();
			node.put("value", dw.toString());
			node.put("name", dw.getName());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}
	
	/**
	 * 订单搜索
	 * @param request
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/control/admin/ordersearch.do")
	public @ResponseBody ObjectNode handlerOrderSearch(HttpServletRequest request,
			@RequestParam int start, @RequestParam int limit) {
		
		String[] ands = getQuerySQL(request);// 生成搜索语句
		
		QueryResult<Order> orders = orderService.listOrders(start, limit, ands);
		return encode2JSON(orders);
	}
	
	/**
	 * 生成订单搜索语句
	 * @param request
	 * @return
	 */
	private String[] getQuerySQL(HttpServletRequest request) {
		List<String> ands = new ArrayList<String>();
		
		String orderid = UserUtil.getQueryString(request, "orderid");
		String fapiao = UserUtil.getQueryString(request, "fapiao");
		String lock = UserUtil.getQueryString(request, "lock");
		String employee = UserUtil.getQueryString(request, "employee");
		String startdate = UserUtil.getQueryString(request, "startdate");
		String starttime = UserUtil.getQueryString(request, "starttime");
		String enddate = UserUtil.getQueryString(request, "enddate");
		String endtime = UserUtil.getQueryString(request, "endtime");
		String state = UserUtil.getQueryString(request, "state");
		String paymentway = UserUtil.getQueryString(request, "paymentway");
		String deliverway = UserUtil.getQueryString(request, "deliverway");
		String buyer = UserUtil.getQueryString(request, "buyer");
		String mintotalprice = UserUtil.getQueryString(request, "mintotalprice");
		String maxtotalprice = UserUtil.getQueryString(request, "maxtotalprice");
		String epay = UserUtil.getQueryString(request, "epay");
		
		if(null != orderid && !orderid.equals("") && !orderid.equals("null")) {
			ands.add("orderid like '%" + orderid + "%'");
		}
		if(null != fapiao && fapiao.equals("on")) {
			ands.add("(fapiao is not null or fapiao != '')");
		}
		if(null != lock && lock.equals("on")) {
			if(null != employee && !employee.equals("") && !employee.equals("null")) {
				ands.add("employee like '%" + employee + "%'");
			} else {
				ands.add("(employee is not null or employee != '')");
			}
		}
		if(null != startdate && !startdate.equals("") && !startdate.equals("null")) {
			if(starttime == null || starttime.equals("null")) starttime = "";
			ands.add("createdate >= '" + startdate + " " + starttime + "'");
		}
		if(null != enddate && !enddate.equals("") && !enddate.equals("null")) {
			if(endtime == null || endtime.equals("null")) endtime = "";
			ands.add("createdate <= '" + enddate + " " + endtime + "'");
		}
		if(null != state && !state.equals("") && !state.equals("null")) {
			ands.add("state = '" + state + "'");
		}
		if(null != paymentway && !paymentway.equals("") && !paymentway.equals("null")) {
			ands.add("paymentway = '" + paymentway + "'");
		}
		if(null != deliverway && !deliverway.equals("") && !deliverway.equals("null")) {
			ands.add("deliverway = '" + deliverway + "'");
		}
		if(null != buyer && !buyer.equals("") && !buyer.equals("null")) {
			ands.add("buyer.name like '%" + buyer + "%'");
		}
		if(null != mintotalprice && !mintotalprice.equals("") && !mintotalprice.equals("null")) {
			ands.add("producttotalprice >= " + mintotalprice);
		}
		if(null != maxtotalprice && !maxtotalprice.equals("") && !maxtotalprice.equals("null")) {
			ands.add("producttotalprice < " + maxtotalprice);
		}
		if(null != epay && epay.equals("on")) {
			ands.add("epay != 0");
		}
		
		return ands.toArray(new String[]{});
	}

	private ObjectNode encode2JSON(QueryResult<Order> orders) {
		ObjectNode json = mapper.createObjectNode();
		json.put("count", orders.getTotal());
		ArrayNode records = mapper.createArrayNode();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for(Order o : orders.getResults()) {
			ObjectNode node = mapper.createObjectNode();
			node.put("orderid", o.getOrderid());
			node.put("username", o.getBuyer().getName());
			node.put("createdate", sdf.format(o.getCreateDate()));
			node.put("state", o.getState().getName());
			node.put("producttotalprice", o.getProductTotalPrice());
			node.put("deliverway", o.getDeliverWay().getName());
			node.put("deliverfee", o.getDeliverWay().getPrice());
			node.put("delivername", o.getOrderDeliverInfo().getName());
			node.put("deliveraddr", o.getOrderDeliverInfo().getAddress());
			node.put("postcode", o.getOrderDeliverInfo().getPostalcode());
			node.put("contact", o.getOrderDeliverInfo().getMobile());
			node.put("paymentway", o.getPaymentWay().getName());
			node.put("epay", o.getEpay());
			node.put("note", o.getNote());
			node.put("employee", o.getEmployee());
			node.put("payable", o.getPayable());
			node.put("alreadypay", o.getAlreadyPay());
			node.put("fapiao", o.getFapiao());
			records.add(node);
		}
		
		json.put("records", records);
		return json;
	}

	public OrderService getOrderService() {
		return orderService;
	}
	@Resource(name="orderServiceImpl")
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
}
