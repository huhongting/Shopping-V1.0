package cn.edu.jnu.web.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.OrderDaoImpl;
import cn.edu.jnu.web.entity.order.Order;
import cn.edu.jnu.web.service.OrderService;
import cn.edu.jnu.web.util.QueryResult;

@Service
public class OrderServiceImpl implements OrderService {

	private OrderDaoImpl orderDao;
	
	@Override
	public void saveOrder(Order order) {
		orderDao.save(order);
	}

	@Override
	public Order findById(String id) {
		return orderDao.find(Order.class, id);
	}

	@Override
	public List<Order> listOrders(int userId) {
		return orderDao.list(Order.class, new String[]{"userid=" + userId});
	}
	
	@Override
	public Order findById(int uid, String id) {
		List<Order> orders = orderDao.list(Order.class, 
				new String[]{"userid=" + uid, "orderid=" + id});
		if(orders == null || orders.isEmpty()) return null;
		return orders.get(0);
	}

	@Override
	public QueryResult<Order> listOrders(int userId, int start, int limit) {
		String[] ands = new String[]{"userid="+userId};
		return listOrders(start, limit, ands);
	}

	@Override
	public QueryResult<Order> listOrders(int start, int limit, String[] ands) {
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		sorts.put("createdate", "desc");
		List<Order> orders = orderDao.pagingQuery(Order.class, start, limit, ands, sorts);
		QueryResult<Order> res = new QueryResult<Order>();
		res.setResults(orders);
		res.setTotal(orderDao.list(Order.class, ands).size());
		return res;
	}

	@Override
	public void updateOrder(Order order) {
		orderDao.update(order);
	}

	public OrderDaoImpl getOrderDao() {
		return orderDao;
	}
	
	@Resource(name="orderDaoImpl")
	public void setOrderDao(OrderDaoImpl orderDao) {
		this.orderDao = orderDao;
	}

}
