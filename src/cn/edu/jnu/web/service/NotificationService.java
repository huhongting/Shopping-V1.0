package cn.edu.jnu.web.service;

import cn.edu.jnu.web.entity.emp.Notification;
import cn.edu.jnu.web.util.QueryResult;

public interface NotificationService {
	public void AddNotification(Notification no);
	public Notification findById(int id);
	public void deleteNotification(int id);
	public QueryResult<Notification> listNotifications(int start, int limit, String[] ands);
}
