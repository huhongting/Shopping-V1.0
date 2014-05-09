package cn.edu.jnu.web.util;

/**
 * XML����������࣬���ڴ洢�ڵ���Ϣ
 * @author HHT
 *
 */
public class ObjectNode {
	private String tag;
	private Object value;
	
	/**
	 * ���ýڵ����ƺͽڵ�ֵ
	 * @param tag
	 * @param value
	 * @return
	 */
	public ObjectNode put(String tag, Object value) {
		this.tag = tag;
		this.value = value;
		return this;
	}
	
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("<");
		res.append(tag);
		res.append(">");
		res.append(value);
		res.append("</");
		res.append(tag);
		res.append(">");
		return res.toString();
	}
}
