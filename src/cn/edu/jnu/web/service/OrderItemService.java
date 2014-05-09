package cn.edu.jnu.web.service;

import java.util.List;

import cn.edu.jnu.web.entity.order.OrderItem;
import cn.edu.jnu.web.util.QueryResult;

public interface OrderItemService {
	/**
	 * ����id���Ҷ�����Ʒ
	 * @param id
	 * @return
	 */
	OrderItem findById(String id);
	/**
	 * ���ݶ�����Ų��Ҷ�����Ʒ
	 * @param oid
	 * @return
	 */
	List<OrderItem> findByOrderId(String oid);
	/**
	 * ��ҳ������ѯ������Ʒ
	 * @param start
	 * @param limit
	 * @param oids
	 * @return
	 */
	QueryResult<OrderItem> listOrderItems(int start, int limit, String[] oids);
}
