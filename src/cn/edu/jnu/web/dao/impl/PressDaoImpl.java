package cn.edu.jnu.web.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.Press;

/**
 * ������ʵ�����DAO���̳�DaoSupport
 * Ϊͼ�������ṩ��������ɾ�Ĳ����
 * ��дdelete��������ɾ��������дΪ�߼�ɾ��
 * @author HHT
 *
 */
@Repository
public class PressDaoImpl extends DaoSupport {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T delete(Class<T> clazz, Serializable entityId) {
		Press press = (Press) find(clazz, entityId);
		press.setDeleted(true);
		update(press);
		return (T) press;
	}

}
