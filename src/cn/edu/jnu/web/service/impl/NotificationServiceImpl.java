package cn.edu.jnu.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.NotificationDaoImpl;
import cn.edu.jnu.web.entity.emp.Notification;
import cn.edu.jnu.web.service.NotificationService;
import cn.edu.jnu.web.util.QueryResult;

@Service
public class NotificationServiceImpl implements NotificationService {

	private NotificationDaoImpl notifyDao;
	
	@Override
	public void AddNotification(Notification no) {
		notifyDao.save(no);
	}

	@Override
	public Notification findById(int id) {
		return notifyDao.find(Notification.class, id);
	}

	@Override
	public void deleteNotification(int id) {
		notifyDao.delete(Notification.class, id);
	}

	@Override
	public QueryResult<Notification> listNotifications(int start, int limit,
			String[] ands) {
		QueryResult<Notification> res = new QueryResult<Notification>();
		res.setResults(notifyDao.pagingQuery(Notification.class, start, limit, ands, null));
		res.setTotal(notifyDao.pagingQuery(Notification.class, -1, -1, ands, null).size());
		return res;
	}

	public NotificationDaoImpl getNotifyDao() {
		return notifyDao;
	}
	@Resource(name="notificationDaoImpl")
	public void setNotifyDao(NotificationDaoImpl notifyDao) {
		this.notifyDao = notifyDao;
	}


}
