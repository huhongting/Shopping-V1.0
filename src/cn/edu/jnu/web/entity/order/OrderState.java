package cn.edu.jnu.web.entity.order;
/**
 * ������״̬
 */
public enum OrderState {
	/** ��ȡ�� **/
    CANCEL {public String getName(){return "��ȡ��";}public String toString() {return "CANCEL";}},
    /** ����� **/
    WAITCONFIRM {public String getName(){return "�����";}public String toString() {return "WAITCONFIRM";}},
    /** �ȴ����� **/
    WAITPAYMENT {public String getName(){return "�ȴ�����";}public String toString() {return "WAITPAYMENT";}},
    /** ������� **/
    ADMEASUREPRODUCT {public String getName(){return "�������";}public String toString() {return "ADMEASUREPRODUCT";}},
    /** �ȴ����� **/
    WAITDELIVER {public String getName(){return "�ȴ�����";}public String toString() {return "WAITDELIVER";}},
    /** �ѷ��� **/
    DELIVERED {public String getName(){return "�ѷ���";}public String toString() {return "DELIVERED";}},
    /** ���ջ� **/
    RECEIVED {public String getName(){return "���ջ�";}public String toString() {return "RECEIVED";}};
    
    public abstract String getName();
    public abstract String toString();
    
    /**
     * �������ӵ�ǰ״̬�л�����һ��״̬
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
     * ���ݶ���״̬�����ƻ����ַ������Ҷ���״̬
     * @param os ����״̬�����ƻ����ַ���
     * @return
     */
    public static OrderState getOrderState(String os) {
    	for(OrderState s : OrderState.values()) {
    		if(s.toString().equals(os) || s.getName().equals(os)) return s;
    	}
    	return null;
    }
}
