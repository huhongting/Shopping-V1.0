package cn.edu.jnu.web.util;

/**
 * XML������
 * @author HHT
 *
 */
public class XMLUtil {
	/**
	 * ��ȡXML�ڵ����
	 * @return
	 */
	public static ObjectNode createObjectNode() {
		return new ObjectNode();
	}
	/**
	 * ��ȡXML�ڵ���󣬿�����ӽڵ�
	 * @param tag �ڵ�����
	 * @return
	 */
	public static ArrayNode createArrayNode(String tag) {
		return new ArrayNode(tag);
	}
	/**
	 * ��ȡXML�ڵ���󣬿�����ӽڵ�<br>
	 * Ĭ�Ͻڵ�����Ϊroot
	 * @return
	 */
	public static ArrayNode createArrayNode() {
		return new ArrayNode();
	}
}
