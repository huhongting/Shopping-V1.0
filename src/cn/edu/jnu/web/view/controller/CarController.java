package cn.edu.jnu.web.view.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.order.DeliverWay;
import cn.edu.jnu.web.entity.order.Order;
import cn.edu.jnu.web.entity.order.OrderItem;
import cn.edu.jnu.web.entity.order.OrderState;
import cn.edu.jnu.web.entity.order.PaymentWay;
import cn.edu.jnu.web.entity.user.OrderDeliverInfo;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.listener.SiteSessionListener;
import cn.edu.jnu.web.service.BookService;
import cn.edu.jnu.web.service.OrderDeliverService;
import cn.edu.jnu.web.service.OrderService;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.util.ArrayNode;
import cn.edu.jnu.web.util.OrderUtil;
import cn.edu.jnu.web.util.WebUtil;
import cn.edu.jnu.web.util.XMLUtil;
import cn.edu.jnu.web.view.dao.BookCart;
import cn.edu.jnu.web.view.dao.CarBook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 购物车控制器
 * @author HHT
 *
 */
@Controller
public class CarController {
	
	private BookService bookService;
	private UserService userService;
	private OrderDeliverService orderDeliverService;
	private OrderService orderService;
	
	/**
	 * 获取用户配送地址
	 * @param uid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/user/contact/list")
	public String handlerListContact(@RequestParam int uid, HttpServletRequest request) {
		List<OrderDeliverInfo> list = orderDeliverService.listDelivers(uid);
		request.setAttribute("delivers", list);
		return "/pages/product/order/orderdeliver";
	}
	
	/**
	 * 删除用户配送地址
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/view/user/contact/del")
	public void handlerListContact(@RequestParam int id, HttpServletResponse response)
			throws IOException {
		orderDeliverService.delDeliver(id);
		ArrayNode root = XMLUtil.createArrayNode();
		PrintWriter out = response.getWriter();
		out.println(root.put(XMLUtil.createObjectNode().put("success", true)));
		out.close();
	}
	
	/**
	 * 添加用户配送地址
	 * @param tell
	 * @param mobile
	 * @param email
	 * @param post
	 * @param addr
	 * @param uid
	 * @param name
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/view/user/contact/add")
	public void handlerAddContact(@RequestParam String tell, @RequestParam String mobile,
			@RequestParam String email, @RequestParam String post, @RequestParam String addr,
			@RequestParam int uid, @RequestParam String name, HttpServletResponse response) 
					throws IOException {
		name = URLDecoder.decode(name, "utf-8");
		addr = URLDecoder.decode(addr, "utf-8");
		User user = userService.findById(uid, false);
		OrderDeliverInfo odi = new OrderDeliverInfo();
		odi.setName(name);
		odi.setAddress(addr);
		odi.setEmail(email);
		odi.setMobile(mobile);
		odi.setTel(tell);
		odi.setPostalcode(post);
		odi.setUser(user);
		orderDeliverService.saveDeliverInfo(odi);
		
		ArrayNode root = XMLUtil.createArrayNode();
		PrintWriter out = response.getWriter();
		out.println(root.put(XMLUtil.createObjectNode().put("success", true)));
		out.close();
	}
	
	@RequestMapping(value="/view/user/orderend")
	public String handlerOrderEnd() {
		return "/pages/product/order/submitorder";
	}

	/**
	 * 处理用户提交的订单
	 * @param uid
	 * @param deliverid
	 * @param deliverway
	 * @param paymentway
	 * @param fapiao
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/view/user/submitorder")
	public String handlerSubmitOrder(@RequestParam String uid, @RequestParam String deliverid,
			@RequestParam String deliverway, @RequestParam String paymentway,
			@RequestParam String fapiao, @RequestParam float epay, 
			HttpServletRequest request, HttpServletResponse response) {
		/*
		 * 中文乱码处理
		 */
		try {
			uid = new String(uid.getBytes("iso-8859-1"), "utf-8");
			deliverid = new String(deliverid.getBytes("iso-8859-1"), "utf-8");
			deliverway = new String(deliverway.getBytes("iso-8859-1"), "utf-8");
			paymentway = new String(paymentway.getBytes("iso-8859-1"), "utf-8");
			fapiao = new String(fapiao.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		User buyer = userService.findById(Integer.valueOf(uid), false);
		Order order = new Order();
		order.setOrderid(Order.createOrderId());
		order.setBuyer(buyer);
		order.setCreateDate(new Date());
		DeliverWay dw = DeliverWay.findDeliverWay(deliverway);
		order.setDeliverWay(dw);
		order.setPaymentstate(false);
		order.setPaymentWay(PaymentWay.getPaymentWay(paymentway));
		order.setOrderDeliverInfo(orderDeliverService.findById(Integer.valueOf(deliverid)));
		order.setState(OrderState.WAITCONFIRM);
		//order.setNote(fapiao);
		if(fapiao == null || fapiao.equals("")) order.setFapiao(null);
		else order.setFapiao(fapiao);
		
		/*
		 * 处理订单商品
		 */
		Set<OrderItem> items = new HashSet<OrderItem>();
		BookCart cart = WebUtil.getSessionCart(request, response);
		for(CarBook cb : cart.getCarBooks()) {
			OrderItem i = new OrderItem(cb.getBook().getBookName(), 
					cb.getBook().getId(), cb.getBook().getPrice(), 
					cb.getNum(), order);
			items.add(i);
			cb.getBook().setSaleCount(cb.getBook().getSaleCount() + cb.getNum());
			bookService.updateBook(cb.getBook());
		}
		
		order.setItems(items);
		float total = OrderUtil.getTotalPrice(items);
		order.setProductTotalPrice(total);
		order.setTotalPrice(total + dw.getPrice());
		order.setEpay(epay);
		order.setPayable(order.getTotalPrice() - order.getEpay());
		order.setAlreadyPay(order.getEpay());
		
		orderService.saveOrder(order);
		
		if(order.getEpay() > 0) {
			User user = order.getBuyer();
			user.getAccount().payMoney(order.getEpay(), "为订单" + order.getOrderid() + "支付￥" + order.getEpay());
			userService.updateUser(user);
		}
		
		WebUtil.delCookie(request, response, uid + ".cart.ishare.com");// 删除购物车内容
		request.getSession().setAttribute("cart.ishare.com", null);
		HttpSession session = SiteSessionListener.getSession(WebUtil.getCookie(request, "cart.ishare.com"));
		if(session != null) {
			session.setAttribute("cart.ishare.com", null);
		}
		
		return "redirect:/view/user/orderend";
	}
	
	@RequestMapping(value="/view/load/cart")
	public void loadCart2Site(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		BookCart cart = WebUtil.getSessionCart(request, response);
		ArrayNode root = XMLUtil.createArrayNode();
		cn.edu.jnu.web.util.ObjectNode node = XMLUtil.createObjectNode();
		node.put("num", cart.getCartNumber());
		root.put(node);
		response.getWriter().println(root);
	}
	
	@RequestMapping(value="/view/shopping/cart/add")
	public @ResponseBody ObjectNode addShoppingCart(HttpServletRequest request, HttpServletResponse response,
			@RequestParam int bid, @RequestParam int num) {
		User user = (User) request.getSession().getAttribute("user");
		ObjectMapper mapper = new ObjectMapper();
		Book book = bookService.findBook(bid);
		if(book == null) return mapper.createObjectNode().put("success", false);
		BookCart cart = WebUtil.getSessionCart(request, response);
		CarBook cb = new CarBook(book, num);
		cart.addBook(cb);
		if(user != null) {
			WebUtil.addSessionCart2Cookie(
					response, 
					user.getId() + ".cart.ishare.com", 
					cart);
		}
		return mapper.createObjectNode().put("success", true);
	}
	
	@RequestMapping(value="/view/shopping/cart/update")
	public @ResponseBody ObjectNode updateShoppingCart(HttpServletRequest request, HttpServletResponse response,
			@RequestParam int bid, @RequestParam int num) {
		User user = (User) request.getSession().getAttribute("user");
		ObjectMapper mapper = new ObjectMapper();
		Book book = bookService.findBook(bid);
		if(book == null) return mapper.createObjectNode().put("success", false);
		BookCart cart = WebUtil.getSessionCart(request, response);
		CarBook cb = new CarBook(book, num);
		cart.updateBook(cb);
		WebUtil.addSessionCart2Cookie(
				response, 
				user.getId() + ".cart.ishare.com", 
				cart);
		return mapper.createObjectNode().put("success", true);
	}
	
	@RequestMapping(value="/view/shopping/cart/del")
	public @ResponseBody ObjectNode delShoppingCart(HttpServletRequest request, HttpServletResponse response,
			@RequestParam int bid) {
		User user = (User) request.getSession().getAttribute("user");
		ObjectMapper mapper = new ObjectMapper();
		Book book = bookService.findBook(bid);
		if(book == null) return mapper.createObjectNode().put("success", false);
		BookCart cart = WebUtil.getSessionCart(request, response);
		CarBook cb = new CarBook(book, 0);
		cart.removeBook(cb);
		WebUtil.addSessionCart2Cookie(
				response, 
				user.getId() + ".cart.ishare.com", 
				cart);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 确认订单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/user/confirmorder")
	public String handlerConfirmOrder(HttpServletRequest request, HttpServletResponse response) {
		BookCart cart = WebUtil.getSessionCart(request, response);
		
		if(cart.getCarBooks().isEmpty()) return "redirect: /view/user/shoppingcar";
		
		request.setAttribute("cart", cart);
		return "/pages/product/order/confirmorder";
	}
	
	/**
	 * 返回购物车视图
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/user/shoppingcar")
	public String handlerCarView(HttpServletRequest request, HttpServletResponse response) {
		BookCart cart = WebUtil.getSessionCart(request, response);
		
		request.setAttribute("cart", cart);
		
		return "/pages/product/order/shoppingcar";
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

	public OrderDeliverService getOrderDeliverService() {
		return orderDeliverService;
	}
	@Resource(name="orderDeliverServiceImpl")
	public void setOrderDeliverService(OrderDeliverService orderDeliverService) {
		this.orderDeliverService = orderDeliverService;
	}

	public OrderService getOrderService() {
		return orderService;
	}
	@Resource(name="orderServiceImpl")
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
}
