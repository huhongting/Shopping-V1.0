package cn.edu.jnu.web.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SiteSessionListener implements HttpSessionListener {

	private static Map<String, HttpSession> SESSIONS = new HashMap<String, HttpSession>();
	
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		HttpSession session = sessionEvent.getSession();
		SESSIONS.put(session.getId(), session);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		SESSIONS.remove(sessionEvent.getSession().getId());
	}
	
	public static HttpSession getSession(String sessionId) {
		if(sessionId == null) return null;
		return SESSIONS.get(sessionId);
	}

}
