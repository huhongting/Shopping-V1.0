package cn.edu.jnu.web.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.order.Comment;

/**
 * �û����������DAO���̳�DaoSupport
 * Ϊ�û������ṩ��������ɾ�Ĳ����
 * ��дdelete��������ɾ��������дΪ�߼�ɾ�����������۱��Ϊ���û��ɼ�
 * @author HHT
 *
 */
@Repository
public class CommentDaoImpl extends DaoSupport {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T delete(Class<T> clazz, Serializable entityId) {
		Comment comm = (Comment) find(clazz, entityId);
		comm.setDelflag(true);
		update(comm);
		return (T) comm;
	}

}
