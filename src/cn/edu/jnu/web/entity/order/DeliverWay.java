package cn.edu.jnu.web.entity.order;

/**
 * 配送方式
 * @author HHT
 *
 */
public enum DeliverWay {
	PINGYOU{
		public String getName(){
			return "平邮";
		}
		public float getPrice() {
			return 5;
		}
		public String getNote() {
			return "支付成功且订单状态变为“发货”后预计9--12天送达，节假日顺延";
		}
		public String toString() {
			return "PINGYOU";
		}
	},
	PUTONGKUAIDI{
		public String getName(){
			return "普通快递";
		}
		public float getPrice() {
			return 10;
		}
		public String getNote() {
			return "支付成功且订单状态变为“发货”后预计2--4天送达，节假日顺延";
		}
		public String toString() {
			return "PUTONGKUAIDI";
		}
	},
	TEKUAIZHUANDI{
		public String getName(){
			return "特快专递";
		}
		public float getPrice() {
			return 20;
		}
		public String getNote() {
			return "支付成功且订单状态变为“发货”后预计1--3天送达，节假日顺延";
		}
		public String toString() {
			return "TEKUAIZHUANDI";
		}
	},
	EMS{
		public String getName(){
			return "邮政EMS";
		}
		public float getPrice() {
			return 38;
		}
		public String getNote() {
			return "支付成功且订单状态变为“发货”后预计1--4天送达，节假日顺延";
		}
		public String toString() {
			return "EMS";
		}
	},
	ZHAIJISONG{
		public String getName(){
			return "宅急送";
		}
		public float getPrice() {
			return 18;
		}
		public String getNote() {
			return "支付成功且订单状态变为“发货”后预计1--3天送达，节假日顺延";
		}
		public String toString() {
			return "ZHAIJISONG";
		}
	};
	public abstract String getName();
	public abstract float getPrice();
	public abstract String getNote();
	public abstract String toString();
	
	/**
	 * 根据配送方式名称查找配送方式
	 * @param name 配送方式名称
	 * @return
	 */
	public static DeliverWay findDeliverWay(String name) {
		for(DeliverWay dw : DeliverWay.values())
			if(dw.getName().equals(name)) return dw;
		return null;
	}
}
