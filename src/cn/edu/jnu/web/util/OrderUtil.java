package cn.edu.jnu.web.util;

import java.util.Iterator;
import java.util.Set;

import cn.edu.jnu.web.entity.order.OrderItem;

/**
 * ����������
 * @author HHT
 *
 */
public class OrderUtil {
	/**
	 * ���㶩����Ʒ�ܶ�
	 * @param items
	 * @return
	 */
	public static float getTotalPrice(Set<OrderItem> items) {
		Iterator<OrderItem> it = items.iterator();
		float total = 0;
		while(it.hasNext()) {
			OrderItem oi = it.next();
			total += oi.getAmount() * oi.getProductPrice();
		}
		return total;
	}
}
