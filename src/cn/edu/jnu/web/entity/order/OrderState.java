package cn.edu.jnu.web.entity.order;
/**
 * 订单的状态
 */
public enum OrderState {
	/** 已取消 **/
    CANCEL {public String getName(){return "已取消";}public String toString() {return "CANCEL";}},
    /** 待审核 **/
    WAITCONFIRM {public String getName(){return "待审核";}public String toString() {return "WAITCONFIRM";}},
    /** 等待付款 **/
    WAITPAYMENT {public String getName(){return "等待付款";}public String toString() {return "WAITPAYMENT";}},
    /** 正在配货 **/
    ADMEASUREPRODUCT {public String getName(){return "正在配货";}public String toString() {return "ADMEASUREPRODUCT";}},
    /** 等待发货 **/
    WAITDELIVER {public String getName(){return "等待发货";}public String toString() {return "WAITDELIVER";}},
    /** 已发货 **/
    DELIVERED {public String getName(){return "已发货";}public String toString() {return "DELIVERED";}},
    /** 已收货 **/
    RECEIVED {public String getName(){return "已收货";}public String toString() {return "RECEIVED";}};
    
    public abstract String getName();
    public abstract String toString();
    
    /**
     * 将订单从当前状态切换到下一个状态
     * @param order
     * @return
     */
    public static OrderState nextState(Order order) {
    	OrderState os = order.getState();
    	if(os == OrderState.WAITCONFIRM) {
    		if(order.getPaymentWay() == PaymentWay.CASH) {
    			return OrderState.ADMEASUREPRODUCT;
    		} else {
    			return OrderState.WAITPAYMENT;
    		}
    	} else if(os == OrderState.WAITPAYMENT) {
    		return OrderState.ADMEASUREPRODUCT;
    	} else if(os == OrderState.ADMEASUREPRODUCT) {
    		return OrderState.WAITDELIVER;
    	} else if(os == OrderState.WAITDELIVER) {
    		return OrderState.DELIVERED;
    	} else if(os == OrderState.DELIVERED) {
    		return OrderState.RECEIVED;
    	} else {
    		return OrderState.CANCEL;
    	}
    }
    /**
     * 根据订单状态的名称或者字符串查找订单状态
     * @param os 订单状态的名称或者字符串
     * @return
     */
    public static OrderState getOrderState(String os) {
    	for(OrderState s : OrderState.values()) {
    		if(s.toString().equals(os) || s.getName().equals(os)) return s;
    	}
    	return null;
    }
}
