package cn.edu.jnu.web.entity.order;
/**
 * ֧����ʽ
 */
public enum PaymentWay {
	ONLINE {
		public String getName(){
			return "����֧��";
		}
		public String toString() {
			return "ONLINE";
		}
	},
	ZHUANZHANG {
		public String getName(){
			return "����ת��";
		}
		public String toString() {
			return "ZHUANZHANG";
		}
	},
	HUIKUAN {
		public String getName(){
			return "�������";
		}
		public String toString() {
			return "HUIKUAN";
		}
	},
	CASH {
		public String getName(){
			return "��������";
		}
		public String toString() {
			return "CASH";
		}
	};
	public abstract String getName();
	
	/**
	 * ����֧����ʽ���ƻ����ַ�������֧����ʽ
	 * @param name ֧����ʽ���ƻ����ַ���
	 * @return
	 */
	public static PaymentWay getPaymentWay(String name) {
		for(PaymentWay pw : PaymentWay.values())
			if(pw.getName().equals(name) || pw.toString().equals(name)) return pw;
		return null;
	}
}
