package cn.edu.jnu.web.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.BookType;

/**
 * ͼ������ʵ�����DAO���̳�DaoSupport
 * Ϊͼ�������ṩ��������ɾ�Ĳ����
 * ��дdelete��������ɾ��������дΪ�߼�ɾ��������ͼ�����ͱ��Ϊ���ɼ�
 * @author HHT
 *
 */
@Repository
public class BookTypeDaoImpl extends DaoSupport {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T delete(Class<T> clazz, Serializable entityId) {
		BookType type = (BookType) find(clazz, entityId);
		type.setDeleted(true);
		update(type);
		return (T) type;
	}

}
