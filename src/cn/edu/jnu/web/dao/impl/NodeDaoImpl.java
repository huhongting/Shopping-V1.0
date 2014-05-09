package cn.edu.jnu.web.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.menu.Node;

/**
 * ���ܲ˵��ڵ�ʵ�����DAO���̳�DaoSupport��<br>
 * �ṩfindAll���������ݲ�ѯ������ѯ�ڵ㡣
 * @author HHT
 *
 */
@Repository
public class NodeDaoImpl extends DaoSupport {
	/**
	 * ����ָ��������ѯ�ڵ���Ϣ
	 * @param wheres
	 * @return
	 */
	public List<Node> findAll(String[] wheres) {
		return list(Node.class, wheres);
	}
}
