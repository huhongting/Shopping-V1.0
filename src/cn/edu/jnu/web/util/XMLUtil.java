package cn.edu.jnu.web.util;

/**
 * XML工具类
 * @author HHT
 *
 */
public class XMLUtil {
	/**
	 * 获取XML节点对象
	 * @return
	 */
	public static ObjectNode createObjectNode() {
		return new ObjectNode();
	}
	/**
	 * 获取XML节点对象，可添加子节点
	 * @param tag 节点名称
	 * @return
	 */
	public static ArrayNode createArrayNode(String tag) {
		return new ArrayNode(tag);
	}
	/**
	 * 获取XML节点对象，可添加子节点<br>
	 * 默认节点名称为root
	 * @return
	 */
	public static ArrayNode createArrayNode() {
		return new ArrayNode();
	}
}
