package cn.edu.jnu.web.dao.impl;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.Book;

/**
 * 图书实体类的DAO，继承DaoSupport
 * 为图书提供基本的增删改查操作
 * 重写delete方法，将删除操作重写为逻辑删除，即将图书标记为不可见
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
