package cn.edu.jnu.web.controller;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.order.OrderItem;
import cn.edu.jnu.web.service.BookService;
import cn.edu.jnu.web.service.OrderItemService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 订单商品控制器
 * @author HHT
 *
 */
@Controller
public class OrderItemController {
	
	private ObjectMapper mapper = new ObjectMapper();
	private OrderItemService orderItemService;
	private BookService bookService;
	
	/**
	 * 列表显示订单商品
	 * @param oid
	 * @return
	 */
	@RequestMapping(value="/control/admin/orderitem.do", params="method=list")
	public @ResponseBody ObjectNode handlerList(@RequestParam String oid) {
		return encode2JSON(orderItemService.findByOrderId(oid));
	}
	
	private ObjectNode encode2JSON(List<OrderItem> items) {
		ObjectNode json = mapper.createObjectNode();
		ArrayNode records = mapper.createArrayNode();
		Iterator<OrderItem> it = items.iterator();
		while(it.hasNext()) {
			ObjectNode node = mapper.createObjectNode();
			OrderItem oi = it.next();
			
			Book b = bookService.findBook(oi.getProductid());
			node.put("kucun", b.getNumber());
			
			node.put("id", oi.getItemid());
			node.put("bookname", oi.getProductName());
			node.put("price", oi.getProductPrice());
			node.put("number", oi.getAmount());
			node.put("count", oi.getAmount() * oi.getProductPrice());
			records.add(node);
		}
		json.put("records", records);
		return json;
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
}
