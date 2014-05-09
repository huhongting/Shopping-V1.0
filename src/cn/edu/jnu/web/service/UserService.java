package cn.edu.jnu.web.service;

import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.util.QueryResult;

public interface UserService {
	/**
	 * 根据用户id查找用户
	 * @param id
	 * @param deleted
	 * @return
	 */
	User findById(int id, boolean deleted);
	/**
	 * 根据用户姓名查找用户
	 * @param name
	 * @return
	 */
	User findByName(String name);
	/**
	 * 根据用户邮箱查找用户
	 * @param email
	 * @return
	 */
	User findByEmail(String email);
	/**
	 * 保存用户信息
	 * @param user
	 */
	void saveUser(User user);
	/**
	 * 删除用户
	 * @param id
	 */
	void deleteUser(int id);
	/**
	 * 更新用户信息
	 * @param user
	 */
	void updateUser(User user);
	/**
	 * 恢复用户为可用用户
	 * @param id
	 * @return
	 */
	boolean reset(int id);
	/**
	 * 分页条件查询用户
	 * @param start
	 * @param limit
	 * @param ands
	 * @return
	 */
	QueryResult<User> listUsers(int start, int limit, String[] ands);
	/**
	 * 分页条件查询用户
	 * @param start
	 * @param limit
	 * @param ands
	 * @param ors
	 * @return
	 */
	QueryResult<User> listUsers(int start, int limit, String[] ands, String[] ors);
}
