package cn.edu.jnu.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.NodeDaoImpl;
import cn.edu.jnu.web.entity.menu.Node;
import cn.edu.jnu.web.service.NodeService;
import cn.edu.jnu.web.util.QueryResult;

@Service
public class NodeServiceImpl implements NodeService {
	private NodeDaoImpl nodeDao;
	
	public NodeDaoImpl getNodeDao() {
		return nodeDao;
	}

	@Resource(name="nodeDaoImpl")
	public void setNodeDao(NodeDaoImpl nodeDao) {
		this.nodeDao = nodeDao;
	}

	@Override
	public QueryResult<Node> findNodes(Object parent) {
		String[] ands;
		if(parent == null) ands = new String[] {"parent is null"};
		else ands = new String[] {"parent="+parent};
		QueryResult<Node> res = new QueryResult<Node>();
		res.setResults(nodeDao.findAll(ands));
		return res;
	}

	@Override
	public void saveNode(Node node) {
		nodeDao.save(node);
	}

	@Override
	public void updateNode(Node node) {
		nodeDao.update(node);
	}

	@Override
	public Node deleteNode(int nodeId) {
		return nodeDao.delete(Node.class, nodeId);
	}
}
