package cn.edu.jnu.web.service;

import java.util.List;

import cn.edu.jnu.web.entity.order.Message;

public interface OrderMsgService {
	/**
	 * ���Ҷ�������
	 * @param id
	 * @return
	 */
	Message findById(int id);
	/**
	 * ���ݶ�����Ų��ҿͷ�����
	 * @param oid
	 * @return
	 */
	List<Message> findByOrderId(String oid);
	/**
	 * ��������
	 * @param msg
	 */
	void saveMessage(Message msg);
}
