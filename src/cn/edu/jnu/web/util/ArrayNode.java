package cn.edu.jnu.web.util;

import java.util.ArrayList;
import java.util.List;

/**
 * XML����������࣬���ڴ洢����ڵ���Ϣ
 * @author HHT
 *
 */
public class ArrayNode {
	private String tag;
	private List<Object> childNodes = new ArrayList<Object>();
	
	public ArrayNode() {
		this.tag = "root";
	}
	public ArrayNode(String tag) {
		this.tag = tag;
	}
	/**
	 * ����ӽڵ�
	 * @param node �ӽڵ����
	 * @return
	 */
	public ArrayNode put(ObjectNode node) {
		childNodes.add(node);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("<");
		res.append(tag);
		res.append(">");
		res.append(listChildNodes(childNodes));
		res.append("</");
		res.append(tag);
		res.append(">");
		return res.toString();
	}
	private Object listChildNodes(List<Object> childNodes) {
		StringBuffer res = new StringBuffer();
		for(Object node : childNodes) {
			res.append(node);
		}
		return res.toString();
	}
}
