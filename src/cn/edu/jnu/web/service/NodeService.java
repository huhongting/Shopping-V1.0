package cn.edu.jnu.web.service;

import cn.edu.jnu.web.entity.menu.Node;
import cn.edu.jnu.web.util.QueryResult;

/**
 * 功能菜单节点的服务接口，定义功能菜单的服务方法。
 * @author HHT
 *
 */
public interface NodeService {
	/**
	 * 保存功能菜单节点
	 * @param node
	 */
	void saveNode(Node node);
	/**
	 * 更新功能菜单节点
	 * @param node
	 */
	void updateNode(Node node);
	/**
	 * 删除功能菜单节点
	 * @param nodeId
	 * @return
	 */
	Node deleteNode(int nodeId);
	/**
	 * 查询功能菜单节点
	 * @param parent
	 * @return
	 */
	QueryResult<Node> findNodes(Object parent);
}
