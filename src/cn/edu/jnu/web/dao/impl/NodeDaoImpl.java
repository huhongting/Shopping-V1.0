package cn.edu.jnu.web.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.menu.Node;

/**
 * 功能菜单节点实体类的DAO，继承DaoSupport。<br>
 * 提供findAll方法，根据查询条件查询节点。
 * @author HHT
 *
 */
@Repository
public class NodeDaoImpl extends DaoSupport {
	/**
	 * 根据指定条件查询节点信息
	 * @param wheres
	 * @return
	 */
	public List<Node> findAll(String[] wheres) {
		return list(Node.class, wheres);
	}
}
