package cn.edu.jnu.web.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.user.OrderDeliverInfo;

/**
 * 订单配送方式实体类的DAO，继承DaoSupport
 * 为订单配送方式提供基本的增删改查操作
 * @author HHT
 *
 */
@Repository
public class OrderDeliverDaoImpl extends DaoSupport {
	public List<OrderDeliverInfo> listDeliver(int uid) {
		return pagingQuery(OrderDeliverInfo.class, -1, -1, 
				new String[]{"uid=" + uid}, null);
	}
}
