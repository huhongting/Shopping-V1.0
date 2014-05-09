package cn.edu.jnu.web.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.order.Comment;

/**
 * 用户评论体类的DAO，继承DaoSupport
 * 为用户评论提供基本的增删改查操作
 * 重写delete方法，将删除操作重写为逻辑删除，即将评论标记为本用户可见
 * @author HHT
 *
 */
@Repository
public class CommentDaoImpl extends DaoSupport {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T delete(Class<T> clazz, Serializable entityId) {
		Comment comm = (Comment) find(clazz, entityId);
		comm.setDelflag(true);
		update(comm);
		return (T) comm;
	}

}
