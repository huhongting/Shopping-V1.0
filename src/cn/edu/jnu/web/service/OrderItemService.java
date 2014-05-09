package cn.edu.jnu.web.service;

import java.util.List;

import cn.edu.jnu.web.entity.order.OrderItem;
import cn.edu.jnu.web.util.QueryResult;

public interface OrderItemService {
	/**
	 * 根据id查找订单商品
	 * @param id
	 * @return
	 */
	OrderItem findById(String id);
	/**
	 * 根据订单编号查找订单商品
	 * @param oid
	 * @return
	 */
	List<OrderItem> findByOrderId(String oid);
	/**
	 * 分页条件查询订单商品
	 * @param start
	 * @param limit
	 * @param oids
	 * @return
	 */
	QueryResult<OrderItem> listOrderItems(int start, int limit, String[] oids);
}
