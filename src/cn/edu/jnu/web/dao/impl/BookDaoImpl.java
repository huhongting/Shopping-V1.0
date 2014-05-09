package cn.edu.jnu.web.dao.impl;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.Book;

/**
 * ͼ��ʵ�����DAO���̳�DaoSupport
 * Ϊͼ���ṩ��������ɾ�Ĳ����
 * ��дdelete��������ɾ��������дΪ�߼�ɾ��������ͼ����Ϊ���ɼ�
 * @author HHT
 *
 */
@Repository
public class BookDaoImpl extends DaoSupport {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T delete(Class<T> clazz, Serializable entityId) {
		Book book = (Book) find(clazz, entityId);
		book.setDelFlag(true);
		book.setDownTime(new Date());
		update(book);
		return (T) book;
	}

}
