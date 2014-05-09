package cn.edu.jnu.web.view.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.order.Comment;
import cn.edu.jnu.web.entity.order.Order;
import cn.edu.jnu.web.entity.order.OrderItem;
import cn.edu.jnu.web.entity.order.OrderState;
import cn.edu.jnu.web.entity.user.AccountRecord;
import cn.edu.jnu.web.entity.user.ContactInfo;
import cn.edu.jnu.web.entity.user.OrderDeliverInfo;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.AccountRecordService;
import cn.edu.jnu.web.service.BookService;
import cn.edu.jnu.web.service.CommentService;
import cn.edu.jnu.web.service.OrderDeliverService;
import cn.edu.jnu.web.service.OrderItemService;
import cn.edu.jnu.web.service.OrderService;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.util.ArrayNode;
import cn.edu.jnu.web.util.ObjectNode;
import cn.edu.jnu.web.util.PasswordUtil;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.UserUtil;
import cn.edu.jnu.web.util.WebUtil;
import cn.edu.jnu.web.util.XMLUtil;
import cn.edu.jnu.web.view.dao.CommentProduct;

/**
 * 用户账户控制器
 * @author HHT
 *
 */
@Controller
public class AccountController {
	
	private OrderService orderService;
	private UserService userService;
	private OrderDeliverService orderDeliverService;
	private OrderItemService orderItemService;
	private BookService bookService;
	private CommentService commentService;
	private AccountRecordService accountRecordService;

	/**
	 * 返回用户账户视图
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/user/account")
	public String handlerAccount(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		user = userService.findById(user.getId(), false);
		request.getSession().setAttribute("user", user);

		handlerComments(request);
		
		return "/pages/product/myishare/account";
	}
	
	private void handlerComments(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		List<Order> orders = orderService.listOrders(-1, -1, 
				new String[] {
					"userid=" + user.getId(), 
					"state = '" + OrderState.RECEIVED + "'"
				}).getResults();// 获取用户有效订单
		int count = 0;
		for(Order order : orders) {
			Set<OrderItem> ois = order.getItems();
			Iterator<OrderItem> it = ois.iterator();
			while(it.hasNext()) {
				OrderItem oi = it.next();
				if(oi.getIscomment() == false)
					count++;
			}
		}
		request.setAttribute("reccount", count);
	}
	
	@RequestMapping(value="/view/user/account/mycommentlist")
	public String handlerListComments(HttpServletRequest request,
			@RequestParam int start, @RequestParam int limit) {
		User user = (User) request.getSession().getAttribute("user");

		String[] ands = new String[]{
			"user.id=" + user.getId()
		};
		QueryResult<Comment> comments = commentService.listComments(start, limit, ands);
		
		request.setAttribute("comments", comments);
		
		return "/pages/product/myishare/commentlist";
	}
	
	@RequestMapping(value="/view/user/account/comment/add")
	public void handlerCommentAdd(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		String title = UserUtil.getQueryString(request, "title");
		title = WebUtil.Html2Text(title);
		String content = UserUtil.getQueryString(request, "content");
		content = WebUtil.Html2Text(content);
		String oid = UserUtil.getQueryString(request, "oid");
		int bid = Integer.valueOf(UserUtil.getQueryString(request, "bid"));
		
		
		PrintWriter out = response.getWriter();
		ArrayNode root = XMLUtil.createArrayNode();
		ObjectNode node = XMLUtil.createObjectNode();
		User user = (User) request.getSession().getAttribute("user");
		Order order = orderService.findById(oid);
		Book book = bookService.findBook(bid);
		if(user != null && order != null && book != null) {
			Comment comm = new Comment();
			comm.setContent(content);
			comm.setTitle(title);
			comm.setUser(user);
			comm.setBook(book);
			comm.setOrder(order);
			commentService.saveComment(comm);
			
			Set<OrderItem> ois = order.getItems();
			Iterator<OrderItem> it = ois.iterator();
			while(it.hasNext()) {
				OrderItem oi = it.next();
				if(oi.getProductid() == bid) {
					oi.setIscomment(true);
					orderService.updateOrder(order);
					break;
				}
			}
			
			node.put("success", true);
		} else {
			node.put("success", false);
		}
		root.put(node);
		out.println(root);
		out.flush();
		out.close();
	}
	
	@RequestMapping(value="/view/user/account/waitcommentlist")
	public String handlerWaitCommentList(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		List<Order> orders = orderService.listOrders(-1, -1, 
				new String[] {
					"userid=" + user.getId(), 
					"state = '" + OrderState.RECEIVED + "'"
				}).getResults();// 获取用户有效订单
		LinkedList<CommentProduct> comms = new LinkedList<CommentProduct>();
		for(Order order : orders) {
			Set<OrderItem> ois = order.getItems();
			Iterator<OrderItem> it = ois.iterator();
			while(it.hasNext()) {
				OrderItem oi = it.next();
				if(oi.getIscomment() == false) {
					CommentProduct cp = new CommentProduct();
					cp.setBookId(oi.getProductid());
					cp.setOrderId(oi.getOrder().getOrderid());
					cp.setBookName(oi.getProductName());
					cp.setOrderCreateDate(oi.getOrder().getCreateDate());
					comms.add(cp);
				}
			}
		}
		request.setAttribute("comms", comms);
		return "/pages/product/myishare/waitcommentlist";
	}
	
	/**
	 * 返回用户帐户密码重置视图
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/user/account/resetpass")
	public String handlerResetPass(HttpServletRequest request) {
		return "/pages/product/myishare/resetpass";
	}
	/**
	 * 重置用户密码
	 * @param request
	 * @param response
	 * @param newpass
	 * @param pass
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	@RequestMapping(value="/view/user/account/resetpass", params="cmd=reset")
	public void handlerResetPass(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String newpass, @RequestParam String pass) 
					throws NoSuchAlgorithmException, IOException {
		response.setContentType("application/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-Control", "no-cache");
		
		User user = (User) request.getSession().getAttribute("user");
		User u = userService.findById(user.getId(), false);
		ArrayNode root = XMLUtil.createArrayNode();
		if(PasswordUtil.getEncodeString(pass).equals(u.getPassword())) {// 判断原始密码是否正确
			u.setPassword(PasswordUtil.getEncodeString(newpass));
			userService.updateUser(u);
			request.getSession().setAttribute("user", u);
			root.put(XMLUtil.createObjectNode().put("success", true));
			root.put(XMLUtil.createObjectNode().put("msg", "密码修改成功！"));
		} else {
			root.put(XMLUtil.createObjectNode().put("success", false));
			root.put(XMLUtil.createObjectNode().put("msg", "密码修改失败！"));
		}

		out.println(root);
		out.flush();
		out.close();
	}
	
	/**
	 * 显示用户购买历史
	 * @param request
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/view/user/account/buylist")
	public String handlerAccountBuys(HttpServletRequest request,
			@RequestParam int start, @RequestParam int limit) {
		User user = (User) request.getSession().getAttribute("user");
		
		List<Order> orders = orderService.listOrders(-1, -1, 
				new String[] {
					"userid=" + user.getId(), 
					"state != '" + OrderState.CANCEL + "'"
				}).getResults();// 获取用户有效订单
		
		/*
		 * 查询用户有效订单下的商品
		 */
		if(orders == null || orders.isEmpty()) {
			request.setAttribute("items", new QueryResult<OrderItem>());
		} else {
			String[] oids = new String[orders.size()];
			int i = 0;
			for(Order o : orders) {
				oids[i++] = o.getOrderid();
			}
			
			QueryResult<OrderItem> res = orderItemService.listOrderItems(start, limit, oids);
			request.setAttribute("items", res);
		}
		
		return "/pages/product/myishare/buylist";
	}
	
	/**
	 * 显示用户配送地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/user/account/address")
	public String handlerAccountAddress(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		List<OrderDeliverInfo> addr = orderDeliverService.listDelivers(user.getId());
		request.setAttribute("address", addr);
		return "/pages/product/myishare/address";
	}
	
	/**
	 * 显示用户帐户余额
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/user/account/emoney")
	public String handlerAccountMoney(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		user = userService.findById(user.getId(), false);
		request.getSession().setAttribute("user", user);
		
		String[] ands = new String[]{"account.id=" + user.getAccount().getId()};
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		sorts.put("time", "desc");
		QueryResult<AccountRecord> res = accountRecordService.listRecords(-1, -1, ands, sorts);
		request.setAttribute("records", res);
		return "/pages/product/myishare/emoney";
	}

	/**
	 * 显示用户帐户基本信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/user/account/info")
	public String handlerAccountInfo(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		int uid = user.getId();
		User currentUser = userService.findById(uid, false);
		request.setAttribute("currentUser", currentUser);
		return "/pages/product/myishare/accountinfo";
	}
	
	/**
	 * 保存帐户信息
	 * @param request
	 * @param mobile
	 * @param phone
	 * @param address
	 * @param postcode
	 * @param note
	 * @return
	 */
	@RequestMapping(value="/view/user/account/info", params="cmd=save")
	public String handlerSaveAccountInfo(HttpServletRequest request,
			@RequestParam String mobile, @RequestParam String phone,
			@RequestParam String address, @RequestParam String postcode,
			@RequestParam String note) {
		
		try {
			address = new String(address.getBytes("iso-8859-1"), "utf-8");
			note = new String(note.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		User user = (User) request.getSession().getAttribute("user");
		int uid = user.getId();
		user = userService.findById(uid, false);
		
		user.setNote(note);
		ContactInfo cinfo = user.getContactInfo();
		if(cinfo == null) cinfo = new ContactInfo();
		cinfo.setAddress(address);
		cinfo.setPostalcode(postcode);
		cinfo.setMobile(mobile);
		cinfo.setPhone(phone);
		user.setContactInfo(cinfo);
		userService.updateUser(user);
		
		request.getSession().setAttribute("user", user);
		
		return "redirect:/view/user/account/info";
	}
	
	/**
	 * 显示订单详细信息
	 * @param request
	 * @param oid
	 * @return
	 */
	@RequestMapping(value="/view/user/account/order")
	public String handlerOrderDetail(HttpServletRequest request, 
			@RequestParam String oid) {
		User user = (User) request.getSession().getAttribute("user");
		int uid = user.getId();
		Order order = orderService.findById(uid, oid);
		request.setAttribute("order", order);
		return "/pages/product/myishare/orderdetail";
	}
	
	/**
	 * 显示用户订单列表
	 * @param request
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/view/user/account/orderlist")
	public String handlerOrderList(HttpServletRequest request,
			@RequestParam int start, @RequestParam int limit) {
		User user = (User) request.getSession().getAttribute("user");
		int uid = user.getId();
		request.setAttribute("orders", orderService.listOrders(uid, start, limit));
		return "/pages/product/myishare/orderlist";
	}

	public OrderService getOrderService() {
		return orderService;
	}
	@Resource(name="orderServiceImpl")
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
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

	public OrderItemService getOrderItemService() {
		return orderItemService;
	}
	@Resource(name="orderItemServiceImpl")
	public void setOrderItemService(OrderItemService orderItemService) {
		this.orderItemService = orderItemService;
	}

	public BookService getBookService() {
		return bookService;
	}
	@Resource(name="bookServiceImpl")
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}

	public CommentService getCommentService() {
		return commentService;
	}
	@Resource(name="commentServiceImpl")
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	public AccountRecordService getAccountRecordService() {
		return accountRecordService;
	}
	@Resource(name="accountRecordServiceImpl")
	public void setAccountRecordService(AccountRecordService accountRecordService) {
		this.accountRecordService = accountRecordService;
	}


}
