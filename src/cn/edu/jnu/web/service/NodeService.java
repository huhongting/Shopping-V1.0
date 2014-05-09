package cn.edu.jnu.web.service;

import cn.edu.jnu.web.entity.menu.Node;
import cn.edu.jnu.web.util.QueryResult;

/**
 * ���ܲ˵��ڵ�ķ���ӿڣ����幦�ܲ˵��ķ��񷽷���
 * @author HHT
 *
 */
public interface NodeService {
	/**
	 * ���湦�ܲ˵��ڵ�
	 * @param node
	 */
	void saveNode(Node node);
	/**
	 * ���¹��ܲ˵��ڵ�
	 * @param node
	 */
	void updateNode(Node node);
	/**
	 * ɾ�����ܲ˵��ڵ�
	 * @param nodeId
	 * @return
	 */
	Node deleteNode(int nodeId);
	/**
	 * ��ѯ���ܲ˵��ڵ�
	 * @param parent
	 * @return
	 */
	QueryResult<Node> findNodes(Object parent);
}
