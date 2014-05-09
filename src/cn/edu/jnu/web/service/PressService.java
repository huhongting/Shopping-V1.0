package cn.edu.jnu.web.service;

import cn.edu.jnu.web.entity.Press;
import cn.edu.jnu.web.util.QueryResult;

/**
 * ������ķ���ӿڣ����������ʵ��Ĳ���������
 * @author HHT
 *
 */
public interface PressService {
	/**
	 * ���������
	 * @param press
	 */
	void savePress(Press press);
	/**
	 * ɾ��������
	 * @param id
	 * @return
	 */
	Press deletePress(int id);
	/**
	 * ����ɾ��������
	 * @param ids
	 */
	void deletePress(int[] ids);
	/**
	 * ���³�������Ϣ
	 * @param press
	 */
	void updatePress(Press press);
	/**
	 * ��ѯ��������Ϣ
	 * @param id
	 * @return
	 */
	Press findPress(int id);
	/**
	 * ���ݳ��������Ʋ�ѯ
	 * @param name
	 * @return
	 */
	Press findPress(String name);
	/**
	 * ��ҳ��ѯ
	 * @param start
	 * @param limit
	 * @return
	 */
	QueryResult<Press> list(String start, String limit);
	/**
	 * ��ҳ������ѯ
	 * @param start
	 * @param limit
	 * @param wheres
	 * @return
	 */
	QueryResult<Press> list(String start, String limit, String[] ands);
	/**
	 * ��ҳ������ѯ
	 * @param start
	 * @param limit
	 * @param ands
	 * @param ors
	 * @return
	 */
	QueryResult<Press> list(String start, String limit, String[] ands, String[] ors);
}
