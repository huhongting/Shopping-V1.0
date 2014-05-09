package cn.edu.jnu.web.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.jnu.web.dao.base.DaoSupport;
import cn.edu.jnu.web.entity.user.User;

/**
 * �û�ʵ�����DAO���̳�DaoSupport��<br>
 * Ϊ�û��ṩ��������ɾ�Ĳ������<br>
 * ��дdelete��������ɾ��������дΪ�߼�ɾ��
 * @author HHT
 *
 */
@Repository
public class UserDaoImpl extends DaoSupport {

	/**
	 * ��д�û�ɾ�����������û�ɾ������Ϊ�߼�ɾ����
	 */
	@Override
	public <T> T delete(Class<T> clazz, Serializable entityId) {
		User user = (User) find(clazz, entityId);
		user.setDelete(true);
		update(user);
		
		return null;
	}
	
	/**
	 * �����û��������û���
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
