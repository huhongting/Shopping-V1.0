package cn.edu.jnu.web.dao.base;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * ����ʵ�����DAO�ӿڣ��ṩ��������ɾ�Ĳ�����ͷ�ҳ��ѯ
 * @author HHT
 *
 */
public interface Dao {
	void save(Object entity);// ʵ����󱣴淽��
	<T> T delete(Class<T> clazz, Serializable entityId);// ʵ�����ɾ������
	void update(Object entity);// ʵ�������·���
	<T> T find(Class<T> clazz, Serializable entityId);// ʵ�������ҷ��� 
	<T> List<T> list(Class<T> clazz, String[] ands);//ʵ�����������ѯ
	<T> List<T> pagingQuery(Class<T> clazz, int start, int max);// ʵ������ҳ��ѯ
	<T> List<T> pagingQuery(Class<T> clazz, int start, int max, 
						LinkedHashMap<String, String> sorts);// ʵ������ҳ�����ѯ
	<T> List<T> pagingQuery(Class<T> clazz, int start, 
			int max, String[] ands, LinkedHashMap<String, String> sorts);// ʵ������ҳ���������ѯ
	<T> List<T> pagingQuery(Class<T> clazz, int start, 
			int max, String[] ands, String[] ors, LinkedHashMap<String, String> sorts);// ʵ������ҳ���������ѯ
}
