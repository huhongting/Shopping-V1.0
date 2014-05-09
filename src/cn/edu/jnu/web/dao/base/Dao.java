package cn.edu.jnu.web.dao.base;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 所有实体类的DAO接口，提供基本的增删改查操作和分页查询
 * @author HHT
 *
 */
public interface Dao {
	void save(Object entity);// 实体对象保存方法
	<T> T delete(Class<T> clazz, Serializable entityId);// 实体对象删除方法
	void update(Object entity);// 实体对象更新方法
	<T> T find(Class<T> clazz, Serializable entityId);// 实体对象查找方法 
	<T> List<T> list(Class<T> clazz, String[] ands);//实体对象条件查询
	<T> List<T> pagingQuery(Class<T> clazz, int start, int max);// 实体对象分页查询
	<T> List<T> pagingQuery(Class<T> clazz, int start, int max, 
						LinkedHashMap<String, String> sorts);// 实体对象分页排序查询
	<T> List<T> pagingQuery(Class<T> clazz, int start, 
			int max, String[] ands, LinkedHashMap<String, String> sorts);// 实体对象分页条件排序查询
	<T> List<T> pagingQuery(Class<T> clazz, int start, 
			int max, String[] ands, String[] ors, LinkedHashMap<String, String> sorts);// 实体对象分页条件排序查询
}
