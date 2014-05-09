package cn.edu.jnu.web.service;

import cn.edu.jnu.web.entity.Press;
import cn.edu.jnu.web.util.QueryResult;

/**
 * 出版社的服务接口，定义出版社实体的操作方法。
 * @author HHT
 *
 */
public interface PressService {
	/**
	 * 保存出版社
	 * @param press
	 */
	void savePress(Press press);
	/**
	 * 删除出版社
	 * @param id
	 * @return
	 */
	Press deletePress(int id);
	/**
	 * 批量删除出版社
	 * @param ids
	 */
	void deletePress(int[] ids);
	/**
	 * 更新出版社信息
	 * @param press
	 */
	void updatePress(Press press);
	/**
	 * 查询出版社信息
	 * @param id
	 * @return
	 */
	Press findPress(int id);
	/**
	 * 根据出版社名称查询
	 * @param name
	 * @return
	 */
	Press findPress(String name);
	/**
	 * 分页查询
	 * @param start
	 * @param limit
	 * @return
	 */
	QueryResult<Press> list(String start, String limit);
	/**
	 * 分页条件查询
	 * @param start
	 * @param limit
	 * @param wheres
	 * @return
	 */
	QueryResult<Press> list(String start, String limit, String[] ands);
	/**
	 * 分页条件查询
	 * @param start
	 * @param limit
	 * @param ands
	 * @param ors
	 * @return
	 */
	QueryResult<Press> list(String start, String limit, String[] ands, String[] ors);
}
