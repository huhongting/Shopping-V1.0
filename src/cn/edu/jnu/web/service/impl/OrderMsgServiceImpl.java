package cn.edu.jnu.web.service.impl;

import java.util.List;

import javax.annotation.Resource;


import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.OrderMsgDaoImpl;
import cn.edu.jnu.web.entity.order.Message;
import cn.edu.jnu.web.service.OrderMsgService;


@Service
public class OrderMsgServiceImpl implements OrderMsgService {
	
	private OrderMsgDaoImpl orderMsgDao;

	@Override
	public Message findById(int id) {
		return orderMsgDao.find(Message.class, id);
	}

	@Override
	public List<Message> findByOrderId(String oid) {
		return orderMsgDao.list(Message.class, new String[]{"orderid=" + oid});
	}
	

	@Override
	public void saveMessage(Message msg) {
		orderMsgDao.save(msg);
	}


	public OrderMsgDaoImpl getOrderMsgDao() {
		return orderMsgDao;
	}
	@Resource(name="orderMsgDaoImpl")
	public void setOrderMsgDao(OrderMsgDaoImpl orderMsgDao) {
		this.orderMsgDao = orderMsgDao;
	}

}
