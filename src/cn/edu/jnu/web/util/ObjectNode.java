package cn.edu.jnu.web.util;

/**
 * XML工具类相关类，用于存储节点信息
 * @author HHT
 *
 */
public class ObjectNode {
	private String tag;
	private Object value;
	
	/**
	 * 设置节点名称和节点值
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
