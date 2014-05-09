package cn.edu.jnu.web.service;

import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.util.QueryResult;

public interface UserService {
	/**
	 * �����û�id�����û�
	 * @param id
	 * @param deleted
	 * @return
	 */
	User findById(int id, boolean deleted);
	/**
	 * �����û����������û�
	 * @param name
	 * @return
	 */
	User findByName(String name);
	/**
	 * �����û���������û�
	 * @param email
	 * @return
	 */
	User findByEmail(String email);
	/**
	 * �����û���Ϣ
	 * @param user
	 */
	void saveUser(User user);
	/**
	 * ɾ���û�
	 * @param id
	 */
	void deleteUser(int id);
	/**
	 * �����û���Ϣ
	 * @param user
	 */
	void updateUser(User user);
	/**
	 * �ָ��û�Ϊ�����û�
	 * @param id
	 * @return
	 */
	boolean reset(int id);
	/**
	 * ��ҳ������ѯ�û�
	 * @param start
	 * @param limit
	 * @param ands
	 * @return
	 */
	QueryResult<User> listUsers(int start, int limit, String[] ands);
	/**
	 * ��ҳ������ѯ�û�
	 * @param start
	 * @param limit
	 * @param ands
	 * @param ors
	 * @return
	 */
	QueryResult<User> listUsers(int start, int limit, String[] ands, String[] ors);
}
