package cn.edu.jnu.web.service;

import java.util.List;

import cn.edu.jnu.web.entity.order.Order;
import cn.edu.jnu.web.util.QueryResult;

public interface OrderService {
	/**
	 * ���涩��
	 * @param order
	 */
	void saveOrder(Order order);
	/**
	 * ���¶���
	 * @param order
	 */
	void updateOrder(Order order);
	/**
	 * ���ݶ�����Ų��Ҷ���
	 * @param id
	 * @return
	 */
	Order findById(String id);
	/**
	 * �����û��Ͷ�����Ų��Ҷ���
	 * @param uid
	 * @param id
	 * @return
	 */
	Order findById(int uid, String id);
	/**
	 * ��ѯ�ض��û������ж���
	 * @param userId
	 * @return
	 */
	List<Order> listOrders(int userId);
	/**
	 * ��ҳ��ѯ�ض��û�����
	 * @param userId
	 * @param start
	 * @param limit
	 * @return
	 */
	QueryResult<Order> listOrders(int userId, int start, int limit);
	/**
	 * ��ҳ������ѯ
	 * @param start
	 * @param limit
	 * @param ands
	 * @return
	 */
	QueryResult<Order> listOrders(int start, int limit, String[] ands);
}
