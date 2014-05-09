package cn.edu.jnu.web.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.user.User;

/**
 * 用户实体类的DAO，继承DaoSupport。<br>
 * 为用户提供基本的增删改查操作。<br>
 * 重写delete方法，将删除操作重写为逻辑删除
 * @author HHT
 *
 */
@Repository
public class UserDaoImpl extends DaoSupport {

	/**
	 * 重写用户删除操作，将用户删除定义为逻辑删除。
	 */
	@Override
	public <T> T delete(Class<T> clazz, Serializable entityId) {
		User user = (User) find(clazz, entityId);
		user.setDelete(true);
		update(user);
		
		return null;
	}
	
	/**
	 * 根据用户名查找用户。
	 * @param name
	 * @return
	 */
	public User findByName(String name) {
		String[] wheres = new String[] {"name='"+name+"'"};
		List<User> users = list(User.class, wheres);
		if(users != null && !users.isEmpty()) {
			return users.get(0);
		}
		return null;
	}
	
}
