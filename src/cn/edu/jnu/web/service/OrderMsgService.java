package cn.edu.jnu.web.service;

import java.util.List;

import cn.edu.jnu.web.entity.order.Message;

public interface OrderMsgService {
	/**
	 * 查找订单留言
	 * @param id
	 * @return
	 */
	Message findById(int id);
	/**
	 * 根据订单编号查找客服留言
	 * @param oid
	 * @return
	 */
	List<Message> findByOrderId(String oid);
	/**
	 * 保存留言
	 * @param msg
	 */
	void saveMessage(Message msg);
}
