package cn.edu.jnu.web.entity.order;

/**
 * ���ͷ�ʽ
 * @author HHT
 *
 */
public enum DeliverWay {
	PINGYOU{
		public String getName(){
			return "ƽ��";
		}
		public float getPrice() {
			return 5;
		}
		public String getNote() {
			return "֧���ɹ��Ҷ���״̬��Ϊ����������Ԥ��9--12���ʹ�ڼ���˳��";
		}
		public String toString() {
			return "PINGYOU";
		}
	},
	PUTONGKUAIDI{
		public String getName(){
			return "��ͨ���";
		}
		public float getPrice() {
			return 10;
		}
		public String getNote() {
			return "֧���ɹ��Ҷ���״̬��Ϊ����������Ԥ��2--4���ʹ�ڼ���˳��";
		}
		public String toString() {
			return "PUTONGKUAIDI";
		}
	},
	TEKUAIZHUANDI{
		public String getName(){
			return "�ؿ�ר��";
		}
		public float getPrice() {
			return 20;
		}
		public String getNote() {
			return "֧���ɹ��Ҷ���״̬��Ϊ����������Ԥ��1--3���ʹ�ڼ���˳��";
		}
		public String toString() {
			return "TEKUAIZHUANDI";
		}
	},
	EMS{
		public String getName(){
			return "����EMS";
		}
		public float getPrice() {
			return 38;
		}
		public String getNote() {
			return "֧���ɹ��Ҷ���״̬��Ϊ����������Ԥ��1--4���ʹ�ڼ���˳��";
		}
		public String toString() {
			return "EMS";
		}
	},
	ZHAIJISONG{
		public String getName(){
			return "լ����";
		}
		public float getPrice() {
			return 18;
		}
		public String getNote() {
			return "֧���ɹ��Ҷ���״̬��Ϊ����������Ԥ��1--3���ʹ�ڼ���˳��";
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
	 * �������ͷ�ʽ���Ʋ������ͷ�ʽ
	 * @param name ���ͷ�ʽ����
	 * @return
	 */
	public static DeliverWay findDeliverWay(String name) {
		for(DeliverWay dw : DeliverWay.values())
			if(dw.getName().equals(name)) return dw;
		return null;
	}
}
