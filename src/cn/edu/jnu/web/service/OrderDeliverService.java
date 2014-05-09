package cn.edu.jnu.web.service;

import java.util.List;

import cn.edu.jnu.web.entity.user.OrderDeliverInfo;

public interface OrderDeliverService {
	/**
	 * 保存配送信息
	 * @param info
	 */
	void saveDeliverInfo(OrderDeliverInfo info);
	/**
	 * 查找特定用户的所有配送信息
	 * @param uid
	 * @return
	 */
	List<OrderDeliverInfo> listDelivers(int uid);
	/**
	 * 删除配送信息
	 * @param id
	 */
	void delDeliver(int id);
	/**
	 * 查找配送信息
	 * @param id
	 * @return
	 */
	OrderDeliverInfo findById(int id);
}
