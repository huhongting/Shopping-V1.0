package cn.edu.jnu.web.service;

import java.util.List;

import cn.edu.jnu.web.entity.order.Order;
import cn.edu.jnu.web.util.QueryResult;

public interface OrderService {
	/**
	 * 保存订单
	 * @param order
	 */
	void saveOrder(Order order);
	/**
	 * 更新订单
	 * @param order
	 */
	void updateOrder(Order order);
	/**
	 * 根据订单编号查找订单
	 * @param id
	 * @return
	 */
	Order findById(String id);
	/**
	 * 根据用户和订单编号查找订单
	 * @param uid
	 * @param id
	 * @return
	 */
	Order findById(int uid, String id);
	/**
	 * 查询特定用户的所有订单
	 * @param userId
	 * @return
	 */
	List<Order> listOrders(int userId);
	/**
	 * 分页查询特定用户订单
	 * @param userId
	 * @param start
	 * @param limit
	 * @return
	 */
	QueryResult<Order> listOrders(int userId, int start, int limit);
	/**
	 * 分页条件查询
	 * @param start
	 * @param limit
	 * @param ands
	 * @return
	 */
	QueryResult<Order> listOrders(int start, int limit, String[] ands);
}
