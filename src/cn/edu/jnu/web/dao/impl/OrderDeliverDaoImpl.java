package cn.edu.jnu.web.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.user.OrderDeliverInfo;

/**
 * �������ͷ�ʽʵ�����DAO���̳�DaoSupport
 * Ϊ�������ͷ�ʽ�ṩ��������ɾ�Ĳ����
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
