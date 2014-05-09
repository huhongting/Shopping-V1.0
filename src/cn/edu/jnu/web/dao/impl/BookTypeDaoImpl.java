package cn.edu.jnu.web.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.BookType;

/**
 * 图书类型实体类的DAO，继承DaoSupport
 * 为图书类型提供基本的增删改查操作
 * 重写delete方法，将删除操作重写为逻辑删除，即将图书类型标记为不可见
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
