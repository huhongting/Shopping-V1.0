package cn.edu.jnu.web.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.Press;

/**
 * 出版社实体类的DAO，继承DaoSupport
 * 为图书类型提供基本的增删改查操作
 * 重写delete方法，将删除操作重写为逻辑删除
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
