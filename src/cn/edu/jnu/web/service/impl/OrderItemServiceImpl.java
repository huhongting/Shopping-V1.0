package cn.edu.jnu.web.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.OrderItemDaoImpl;
import cn.edu.jnu.web.entity.order.OrderItem;
import cn.edu.jnu.web.service.OrderItemService;
import cn.edu.jnu.web.util.QueryResult;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	private OrderItemDaoImpl orderItemDao;
	
	@Override
	public OrderItem findById(String id) {
		return orderItemDao.find(OrderItem.class, id);
	}

	@Override
	public List<OrderItem> findByOrderId(String oid) {
		return orderItemDao.list(OrderItem.class, new String[]{"orderid=" + oid});
	}

	@Override
	public QueryResult<OrderItem> listOrderItems(int start, int limit, String[] oids) {
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		//sorts.put("order.createDate", "desc");
		sorts.put("orderid", "desc");
		String[] ors = createSQL(oids);
		List<OrderItem> items = orderItemDao.pagingQuery(OrderItem.class, start, 
				limit, null, ors, sorts);
		QueryResult<OrderItem> res = new QueryResult<OrderItem>();
		res.setResults(items);
		List<OrderItem> list = orderItemDao.pagingQuery(OrderItem.class, -1, -1, null, ors, null);
		res.setTotal(list.size());
		return res;
	}
	
	private String[] createSQL(String[] oids) {
		if(oids == null || oids.length == 0) return null;
		
		String[] sql = new String[oids.length];
		int i = 0;
		for(String oid : oids) {
			sql[i++] = "orderid=" + oid;
		}
		return sql;
	}

	public OrderItemDaoImpl getOrderItemDao() {
		return orderItemDao;
	}
	@Resource(name="orderItemDaoImpl")
	public void setOrderItemDao(OrderItemDaoImpl orderItemDao) {
		this.orderItemDao = orderItemDao;
	}

}
