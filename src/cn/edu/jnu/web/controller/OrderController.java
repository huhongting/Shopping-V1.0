package cn.edu.jnu.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.authority.AuthorityType;
import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.order.Order;
import cn.edu.jnu.web.entity.order.OrderItem;
import cn.edu.jnu.web.entity.order.OrderState;
import cn.edu.jnu.web.entity.order.PaymentWay;
import cn.edu.jnu.web.entity.user.Account;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.BookService;
import cn.edu.jnu.web.service.OrderService;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.util.AuthorityUtil;
import cn.edu.jnu.web.util.OrderUtil;
import cn.edu.jnu.web.util.QueryResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 订单控制器
 * @author HHT
 *
 */
@Controller
public class OrderController {
	private ObjectMapper mapper = new ObjectMapper();
	
	private OrderService orderService;
	private BookService bookService;
	private UserService userService;
	
	/**
	 * 定义定时器，将发货超过30天用户未确认收货的订单状态修改为已收货状态
	 */
	public OrderController() {
		new Timer(true).schedule(new TimerTask() {
			QueryResult<Order> orders = null;
			
			@Override
			public void run() {
				if(orderService == null) return;
				Date now = new Date();
				orders = orderService.listOrders(-1, -1, new String[]{
						"state='" + OrderState.DELIVERED.toString() + "'"
				});
				for(Order order : orders.getResults()) {
					long betweenTime = now.getTime() - order.getCreateDate().getTime();
					betweenTime = betweenTime / 1000 / 60 / 60 / 24;
					if(betweenTime >= 30) {
						order.setState(OrderState.RECEIVED);
						order.addOrderMsg("系统确认已收货订单。", "系统");
						orderService.updateOrder(order);
					}
				}
			}
		}, 0, 30*24*60*60*1000L);
	}
	
	/**
	 * 检查订单是否被锁定，防止一个订单被同时操作
	 * @param oid
	 * @param name
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/order.do", params="method=chklock")
	public @ResponseBody ObjectNode handlerCheckLock(@RequestParam String oid,
			@RequestParam String name, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("employee");
		Order order = orderService.findById(oid);
		if(user.getUserType() != User.ADMINISTRATOR) {
			if(order == null || 
					(order.getEmployee() != null && !name.equals(order.getEmployee())))
				return mapper.createObjectNode().put("success", false);
		}
		order.setEmployee(name);
		orderService.updateOrder(order);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 解锁订单，只有自己锁定的订单才能自己解锁，否则需要管理员统一解锁
	 * @param oid
	 * @param name
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/order.do", params="method=unlock")
	public @ResponseBody ObjectNode handlerUnlock(@RequestParam String oid,
			@RequestParam String name, HttpServletRequest request) {
		Order order = orderService.findById(oid);
		if(order == null || order.getEmployee() == null)
			return mapper.createObjectNode().put("success", false)
											.put("msg", "订单未被锁定！");
		else {
			User user = (User) request.getSession().getAttribute("employee");
			if(name.equals(order.getEmployee()) ||
					AuthorityUtil.hasAuthority(user, AuthorityType.ADMIN_UNLOCK_ORDER)
					/*user.getUserType() == User.ADMINISTRATOR*/) {
				order.setEmployee(null);
				orderService.updateOrder(order);
				return mapper.createObjectNode().put("success", true);
			} else {
				return mapper.createObjectNode().put("success", false)
												.put("msg", "权限不够！");
			}
		}
	}
	
	/**
	 * 财务确认接收部分账款
	 * @param oid
	 * @param arprice
	 * @return
	 */
	@RequestMapping(value="/control/admin/order.do", params="method=bufen")
	public @ResponseBody ObjectNode handlerBuFen(@RequestParam String oid,
			@RequestParam double arprice, HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		Order order = orderService.findById(oid);
		if(order == null) return mapper.createObjectNode().put("success", false);
		order.setAlreadyPay(order.getAlreadyPay() + arprice);
		order.addOrderMsg("操作员" + emp.getName() + "确认已收部分账款￥" + arprice + "。", emp.getName());
		orderService.updateOrder(order);
		return mapper.createObjectNode().put("success", true).put("msg", order.getAlreadyPay());
	}
	
	/**
	 * 取消订单
	 * @param oid
	 * @return
	 */
	@RequestMapping(value="/control/admin/order.do", params="method=cancel")
	public @ResponseBody ObjectNode handlerCancel(@RequestParam String oid, HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		
		Order order = orderService.findById(oid);
		if(order == null)
			return mapper.createObjectNode().put("success", false);
		else {
			payDeliverFee(order);
			
			order.setState(OrderState.CANCEL);
			Account account = order.getBuyer().getAccount();
			double d = order.getAlreadyPay();
			if(d != 0) {
				account.saveMoney(d, 
						"取消订单" + order.getOrderid() + "存入￥" + d);
			}
			order.setAlreadyPay(0);
			order.setEpay(0f);
			order.setPayable(order.getTotalPrice());
			
			for(OrderItem oi : order.getItems()) {
				Book b = bookService.findBook(oi.getProductid());
				if(b != null) {
					b.setNumber(b.getNumber() + oi.getAmount());
				}
				bookService.updateBook(b);
			}
			order.addOrderMsg("操作员" + emp.getName() + "取消订单。", emp.getName());
			
			orderService.updateOrder(order);
			return mapper.createObjectNode().put("success", true);
		}
	}

	private void payDeliverFee(Order order) {
		if(order.getState().getName().equals(OrderState.DELIVERED.getName())) {
			double df = order.getDeliverWay().getPrice();
			User buyer = order.getBuyer();
			Account account = buyer.getAccount();
			account.payMoney(df, "支付订单" + order.getOrderid() + "配送费用￥" + df);
			userService.updateUser(buyer);
		}
	}

	/**
	 * 从取消订单中重新恢复订单
	 * @param oid
	 * @return
	 */
	@RequestMapping(value="/control/admin/order.do", params="method=reset")
	public @ResponseBody ObjectNode handlerReset(@RequestParam String oid, HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		Order order = orderService.findById(oid);
		if(order == null)
			return mapper.createObjectNode().put("success", false);
		else {
			order.setState(OrderState.WAITCONFIRM);
			order.setCreateDate(new Date());
			order.addOrderMsg("操作员" + emp.getName() + "恢复订单。", emp.getName());
			orderService.updateOrder(order);
			return mapper.createObjectNode().put("success", true);
		}
	}
	
	/**
	 * 订单通过审核进入下一个状态
	 * @param oid
	 * @return
	 */
	@RequestMapping(value="/control/admin/order.do", params="method=next")
	public @ResponseBody ObjectNode handlerNext(@RequestParam String oid, HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		Order order = orderService.findById(oid);
		if(order == null)
			return mapper.createObjectNode().put("success", false);
		else {
			OrderState pos = order.getState();
			OrderState os = OrderState.nextState(order);
			order.setState(os);
			// 订单发货后减少对应书籍库存
			if(os.getName().equals(OrderState.DELIVERED.getName())) {
				Set<OrderItem> items = order.getItems();
				for(OrderItem oi : items) {
					Book book = bookService.findBook(oi.getProductid());
					book.setNumber(book.getNumber() - oi.getAmount());
					bookService.updateBook(book);
				}
			}
			if(os.getName().equals(OrderState.ADMEASUREPRODUCT.getName())) {
				if(order.getAlreadyPay() > order.getTotalPrice()) {
					User user = order.getBuyer();
					double d = order.getAlreadyPay() - order.getTotalPrice();
					user.getAccount().saveMoney(d, 
							"支付额超过订单" + order.getOrderid() + "应付额存入￥" + d);
					order.setAlreadyPay(order.getTotalPrice());
				}
			}
			if(order.getPaymentWay().getName().equals(PaymentWay.CASH.getName()) &&
					os.getName().equals(OrderState.RECEIVED.getName())) {
				order.setAlreadyPay(order.getTotalPrice());
			}
			if(!order.getPaymentWay().getName().equals(PaymentWay.CASH.getName()) &&
					os.getName().equals(OrderState.ADMEASUREPRODUCT.getName())) {
				order.setAlreadyPay(order.getTotalPrice());
			}
			order.addOrderMsg("操作员" + emp.getName() + "确认" + pos.getName() + "订单。", emp.getName());
			orderService.updateOrder(order);
			return mapper.createObjectNode().put("success", true);
		}
	}
	
	/**
	 * 配货时进行商品数量调整，重新生成订单替换原来的订单
	 * @param oid
	 * @param nums
	 * @param names
	 * @return
	 */
	@RequestMapping(value="/control/admin/order.do", params="method=reorder")
	public @ResponseBody ObjectNode handlerReorder(@RequestParam String oid,
			@RequestParam int[] nums, @RequestParam String[] names, HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		Order order = orderService.findById(oid);
		if(order == null)
			return mapper.createObjectNode()
							.put("success", false)
							.put("msg", "订单不存在！");
		else {
			if(nums == null || nums.length == 0) return mapper.createObjectNode()
																.put("success", false)
																.put("msg", "订单替换失败！");
			
			Order neworder = new Order();
			neworder.setOrderid(Order.createOrderId());
			neworder.setBuyer(order.getBuyer());
			neworder.setDeliverWay(order.getDeliverWay());
			neworder.setEpay(order.getEpay());
			neworder.setFapiao(order.getFapiao());
			neworder.setNote(order.getNote());
			neworder.setOrderDeliverInfo(order.getOrderDeliverInfo());
			neworder.setPaymentstate(order.getPaymentstate());
			neworder.setPaymentWay(order.getPaymentWay());
			neworder.setState(order.getState());

			/*
			 * 处理订单商品
			 */
			Set<OrderItem> items = new HashSet<OrderItem>();
			for(int i=0; i<nums.length; i++) {
				if(nums[i] == 0) continue;

				Book book = bookService.findBook(names[i]);
				if(book == null) continue;
				
				OrderItem oi = new OrderItem(
						book.getBookName(), book.getId(), book.getPrice(), nums[i], neworder);
				items.add(oi);
				//book.setSaleCount(book.getSaleCount() + nums[i]);
				OrderItem ooi = order.findOrderItemByBookName(book.getBookName());
				if(ooi != null) {
					book.setNumber(book.getNumber() + (ooi.getAmount() - nums[i]));
				}
				bookService.updateBook(book);
			}
			neworder.setItems(items);
			float total = OrderUtil.getTotalPrice(items);
			neworder.setProductTotalPrice(total);
			neworder.setTotalPrice(total + neworder.getDeliverWay().getPrice());
			double d = neworder.getTotalPrice() - neworder.getEpay();
			neworder.setPayable(d >= 0 ? d : 0);
			
			Account account = neworder.getBuyer().getAccount();
			if(neworder.getEpay() > neworder.getTotalPrice()) {
				double p = neworder.getEpay() - neworder.getTotalPrice();
				account.saveMoney(p, "替换旧订单" + oid + "存入￥" + p);
				neworder.setEpay(neworder.getTotalPrice());
				neworder.setPayable(0);
			} else {
				if(order.getAlreadyPay() > neworder.getTotalPrice()) {
					neworder.setAlreadyPay(neworder.getTotalPrice());
					double m = order.getAlreadyPay() - neworder.getAlreadyPay();
					account.saveMoney(m, "替换旧订单" + oid + "存入￥" + m);
					neworder.setPayable(0);
				}				
			}
			
			neworder.setNote("因库存不足，本订单替代订单号为" + oid + "的订单。");
			neworder.addOrderMsg("操作员" + emp.getName() + "替换旧订单" + oid + "。", emp.getName());
			orderService.saveOrder(neworder);
			
			order.setState(OrderState.CANCEL);
			order.addOrderMsg("操作员" + emp.getName() + "用新订单" + 
									neworder.getOrderid() + "替换本订单。", emp.getName());
			orderService.updateOrder(order);
			return mapper.createObjectNode().put("success", true);
		}
	}
	
	public static OrderState[] ORDERSTATE = new OrderState[] {
		OrderState.CANCEL,// 已取消 0
		OrderState.WAITCONFIRM,// 等待审核 1
		OrderState.WAITPAYMENT,// 等待付款 2
		OrderState.ADMEASUREPRODUCT,// 正在配货 3
		OrderState.WAITDELIVER,// 等待发货 4
		OrderState.DELIVERED,// 已发货 5
		OrderState.RECEIVED// 已收货 6
	};
	
	/**
	 * 显示每个状态下的订单列表
	 * @param start
	 * @param limit
	 * @param os
	 * @return
	 */
	@RequestMapping(value="/control/admin/order.do", params="method=list")
	public @ResponseBody ObjectNode handlerOrderList(@RequestParam int start, 
			@RequestParam int limit, @RequestParam int os) {
		QueryResult<Order> orders = orderService.listOrders(start, limit, 
				new String[]{"state='" + ORDERSTATE[os] + "'"});
		return encode2JSON(orders);
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
			node.put("fapiao", o.getFapiao());
			node.put("employee", o.getEmployee());
			node.put("payable", o.getPayable());
			node.put("alreadypay", o.getAlreadyPay());
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
	public BookService getBookService() {
		return bookService;
	}
	@Resource(name="bookServiceImpl")
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}

	public UserService getUserService() {
		return userService;
	}
	@Resource(name="userServiceImpl")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
