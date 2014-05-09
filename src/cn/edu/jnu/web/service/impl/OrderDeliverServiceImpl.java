package cn.edu.jnu.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.OrderDeliverDaoImpl;
import cn.edu.jnu.web.entity.user.OrderDeliverInfo;
import cn.edu.jnu.web.service.OrderDeliverService;

@Service
public class OrderDeliverServiceImpl implements OrderDeliverService {

	private OrderDeliverDaoImpl deliverDao;
	
	@Override
	public void saveDeliverInfo(OrderDeliverInfo info) {
		deliverDao.save(info);
	}

	public OrderDeliverDaoImpl getDeliverDao() {
		return deliverDao;
	}
	@Resource(name="orderDeliverDaoImpl")
	public void setDeliverDao(OrderDeliverDaoImpl deliverDao) {
		this.deliverDao = deliverDao;
	}

	@Override
	public List<OrderDeliverInfo> listDelivers(int uid) {
		return deliverDao.listDeliver(uid);
	}

	@Override
	public void delDeliver(int id) {
		deliverDao.delete(OrderDeliverInfo.class, id);
	}

	@Override
	public OrderDeliverInfo findById(int id) {
		return deliverDao.find(OrderDeliverInfo.class, id);
	}

}
