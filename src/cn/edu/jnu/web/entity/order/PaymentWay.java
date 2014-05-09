package cn.edu.jnu.web.entity.order;
/**
 * 支付方式
 */
public enum PaymentWay {
	ONLINE {
		public String getName(){
			return "在线支付";
		}
		public String toString() {
			return "ONLINE";
		}
	},
	ZHUANZHANG {
		public String getName(){
			return "银行转账";
		}
		public String toString() {
			return "ZHUANZHANG";
		}
	},
	HUIKUAN {
		public String getName(){
			return "邮政汇款";
		}
		public String toString() {
			return "HUIKUAN";
		}
	},
	CASH {
		public String getName(){
			return "货到付款";
		}
		public String toString() {
			return "CASH";
		}
	};
	public abstract String getName();
	
	/**
	 * 根据支付方式名称或者字符串查找支付方式
	 * @param name 支付方式名称或者字符串
	 * @return
	 */
	public static PaymentWay getPaymentWay(String name) {
		for(PaymentWay pw : PaymentWay.values())
			if(pw.getName().equals(name) || pw.toString().equals(name)) return pw;
		return null;
	}
}
