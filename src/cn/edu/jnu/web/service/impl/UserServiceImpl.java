package cn.edu.jnu.web.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.UserDaoImpl;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.util.QueryResult;

@Service
public class UserServiceImpl implements UserService {

	private UserDaoImpl userDao;
	
	@Override
	public User findById(int id, boolean deleted) {
		User user = userDao.find(User.class, id);
		if(deleted) return user;
		else {
			if(user.getDelete() == true) return null;
			return user;
		}
	}

	@Override
	public User findByName(String name) {
		User user = userDao.findByName(name);
		if(user != null && user.getDelete() == true) return null;
		return user;
	}
	
	@Override
	public User findByEmail(String email) {
		List<User> users = userDao.list(User.class, new String[] {"email='" + email + "'"});
		if(users == null || users.isEmpty()) return null;
		User user = users.get(0);
		if(user.getDelete() == true) return null;
		return user;
	}

	@Override
	public void deleteUser(int id) {
		userDao.delete(User.class, id);
	}

	@Override
	public void updateUser(User user) {
		userDao.update(user);
	}

	@Override
	public void saveUser(User user) {
		userDao.save(user);
	}

	@Override
	public QueryResult<User> listUsers(int start, int limit, String[] ands) {
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		sorts.put("regtime", "desc");
		List<User> users = userDao.pagingQuery(User.class, start, limit, ands, sorts);
		QueryResult<User> res = new QueryResult<User>();
		res.setResults(users);
		res.setTotal(userDao.pagingQuery(User.class, -1, -1, ands, null).size());
		return res;
	}
	

	public UserDaoImpl getUserDao() {
		return userDao;
	}

	@Resource(name="userDaoImpl")
	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	@Override
	public boolean reset(int id) {
		List<User> users = userDao.list(User.class, 
				new String[]{"id=" + id, "deleted=true"});
		if(users == null || users.isEmpty()) return false;
		User user = users.get(0);
		user.setDelete(false);
		updateUser(user);
		return true;
	}

	@Override
	public QueryResult<User> listUsers(int start, int limit, String[] ands,
			String[] ors) {
		List<User> users = userDao.pagingQuery(User.class, start, limit, ands, ors, null);
		QueryResult<User> res = new QueryResult<User>();
		res.setResults(users);
		res.setTotal(userDao.pagingQuery(User.class, -1, -1, ands, ors, null).size());
		return res;
	}

}
