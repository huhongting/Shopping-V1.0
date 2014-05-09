package cn.edu.jnu.web.service;

import java.util.List;

import cn.edu.jnu.web.entity.user.OrderDeliverInfo;

public interface OrderDeliverService {
	/**
	 * ����������Ϣ
	 * @param info
	 */
	void saveDeliverInfo(OrderDeliverInfo info);
	/**
	 * �����ض��û�������������Ϣ
	 * @param uid
	 * @return
	 */
	List<OrderDeliverInfo> listDelivers(int uid);
	/**
	 * ɾ��������Ϣ
	 * @param id
	 */
	void delDeliver(int id);
	/**
	 * ����������Ϣ
	 * @param id
	 * @return
	 */
	OrderDeliverInfo findById(int id);
}
