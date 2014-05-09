package cn.edu.jnu.web.util;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.listener.SiteSessionListener;
import cn.edu.jnu.web.view.dao.BookCart;
import cn.edu.jnu.web.view.dao.CarBook;

/**
 * WEB工具类
 * @author HHT
 *
 */
public class WebUtil {
	/**
	 * 获取当前用户
	 * @param request
	 * @return
	 */
	public static User getCurrentUser(HttpServletRequest request) {
		Object obj =  request.getSession().getAttribute("user");
		if(obj == null) return null;
		else return (User) obj;
	}
	/**
	 * 获取当前用户类型
	 * @param user
	 * @return
	 */
	public static int getUserType(User user) {
		return user.getUserType();
	}
	
	/**
	 * 根据Cookie名称获取Cookie值
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) return null;
		for(Cookie c : cookies) {
			if(c.getName().equalsIgnoreCase(name)) {
				return c.getValue();
			}
		}
		return null;
	}
	/*
	public static Map<Integer, Integer> getCarCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Map<Integer, Integer> books = new HashMap<Integer, Integer>();
		for(Cookie c : cookies) {
			if(c.getName().contains("car.ishare.")) {
				String value = c.getValue();
				String[] vs = value.split("-");
				books.put(Integer.valueOf(vs[0]), Integer.valueOf(vs[1]));
			}
		}
		return books;
	}
	*/
	/**
	 * 设置Cookie，有效时间为30天
	 * @param resp
	 * @param name
	 * @param value
	 */
	public static void setCookie(HttpServletResponse resp, 
			String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(30*24*60*60);
		resp.addCookie(cookie);
	}
	
	/**
	 * 设置Cookie，自定义有效时间
	 * @param resp
	 * @param name
	 * @param value
	 * @param sec
	 */
	public static void setCookie(HttpServletResponse resp, 
			String name, String value, int sec) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(sec);
		resp.addCookie(cookie);
	}
	
	/**
	 * 删除Cookie
	 * @param req
	 * @param resp
	 * @param name
	 */
	public static void delCookie(HttpServletRequest req, 
			HttpServletResponse resp, String name) {
		Cookie[] cookies = req.getCookies();
		for(Cookie c : cookies) {
			if(c.getName().equalsIgnoreCase(name)) {
				c.setMaxAge(0);
				c.setPath("/");
				resp.addCookie(c);
			}
		}
	}

	public static void addSessionHistory2Cookie(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String value = (String) session.getAttribute("viewhis.ishare");
		LinkedList<String> list = createLinkedList(value);
		value = WebUtil.getCookie(request, user.getId() + ".viewhis.ishare");
		LinkedList<String> cookieList = createLinkedList(value);
		for(String s : list) {
			if(cookieList.size() < 10) {
				cookieList.add(s);
			} else {
				cookieList.removeFirst();
				cookieList.addLast(s);
			}
		}
		WebUtil.setCookie(response, user.getId() + ".viewhis.ishare", getValue(cookieList));
	}
	
	/**
	 * 添加浏览过的商品，最大数量为10
	 * @param request
	 * @param response
	 * @param id
	 */
	public static void addViewHistory(HttpServletRequest request,
			HttpServletResponse response, int id) {
		Object obj = request.getSession().getAttribute("user");
		if(obj == null) {// 未登入
			HttpSession session = request.getSession();
			String value = (String) session.getAttribute("viewhis.ishare");
			LinkedList<String> list = createLinkedList(value);
			
			if(list.contains(String.valueOf(id))) return;
			
			if(list.size() < 10) {
				list.add(String.valueOf(id));
			} else {
				list.removeFirst();
				list.addLast(String.valueOf(id));
			}
			session.setAttribute("viewhis.ishare", getValue(list));
		} else {
			User user = (User) obj;
			// userid + viewhis.ishare
			String value = WebUtil.getCookie(request, user.getId() + ".viewhis.ishare");
			LinkedList<String> list = createLinkedList(value);
			
			if(list.contains(String.valueOf(id))) return;
			
			if(list.size() < 10) {
				list.add(String.valueOf(id));
			} else {
				list.removeFirst();
				list.addLast(String.valueOf(id));
			}
			WebUtil.setCookie(response, user.getId() + ".viewhis.ishare", getValue(list));
		}
	}
	/**
	 * 根据List数据生成字符串数据，输出格式为xx-xx-xx
	 * @param list
	 * @return
	 */
	private static String getValue(LinkedList<String> list) {
		StringBuffer value = new StringBuffer();
		for(String s : list) {
			value.append(s);
			value.append("-");
		}
		if(list != null && !list.isEmpty()) 
			value = value.replace(value.lastIndexOf("-"), value.length(), "");
		return value.toString();
	}
	/**
	 * 根据字符串类型生成LinkedList数据，输入格式为xx-xx-xx
	 * @param value
	 * @return
	 */
	private static LinkedList<String> createLinkedList(String value) {
		LinkedList<String> list = new LinkedList<String>();
		if(value == null) return list;
		String[] ids = value.split("-");
		for(String id : ids) {
			list.add(id);
		}
		return list;
	}
	
	/**
     * 去除html代码
     * @param inputString
     * @return
     */
    public static String Html2Text(String inputString) {
        String htmlStr = inputString; //含html标签的字符串
        String textStr ="";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;          
        java.util.regex.Pattern p_ba;
        java.util.regex.Matcher m_ba;
        
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
            String patternStr = "\\s+";
            
            p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签

            p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签
         
            p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签
            
            p_ba = Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE);
            m_ba = p_ba.matcher(htmlStr);
            htmlStr = m_ba.replaceAll(""); //过滤空格
         
         textStr = htmlStr;
         
        }catch(Exception e) {
        	//System.err.println("Html2Text: " + e.getMessage());
        }
        textStr = textStr.replace(" ", "&nbsp;");
        return textStr;//返回文本字符串
     }
	
    public static BookCart getSessionCart(HttpServletRequest request, HttpServletResponse response) {
		BookCart cart = (BookCart) request.getSession().getAttribute("cart.ishare.com");
		if(cart == null) {
			String sid = WebUtil.getCookie(request, "cart.ishare.com");
			if(sid != null) {
				HttpSession session = SiteSessionListener.getSession(sid);
				if(session != null) {
					cart = (BookCart) session.getAttribute("cart.ishare.com");
				}
			}
		}
		if(cart == null) {
			cart = new BookCart();
			request.getSession().setAttribute("cart.ishare.com", cart);
			WebUtil.setCookie(response, "cart.ishare.com", request.getSession().getId(), -1);
		}
		//request.setAttribute("cart", cart);
		return cart;
	}
    
    /**
     * 根据session购物车创建cookie
     * @param response
     * @param name
     * @param session
     */
    public static void addSessionCart2Cookie(HttpServletResponse response, String name,
			BookCart cart) {
		if(cart == null || cart.getCartNumber() == 0) setCookie(response, name, "");
		List<CarBook> list = cart.getCarBooks();
		StringBuffer sb = new StringBuffer();
		for(CarBook cb : list) {
			sb.append(cb.getBook().getId());
			sb.append("-");
			sb.append(cb.getNum());
			sb.append("_");
		}
		if(sb.length() > 0) sb = sb.replace(sb.lastIndexOf("_"), sb.length(), "");
		setCookie(response, name, sb.toString());
	}
    
    public static int[][] getCartFromCookie(HttpServletRequest request, 
    		HttpServletResponse response, String name) {
    	String value = getCookie(request, name);
    	if(value == null) return null;
    	String[] values = value.split("_");
    	
    	int[][] res = null;
    	if(values != null && values.length > 0) {
    		res = new int[values.length][2];
    		int i = 0;
    		for(String v : values) {
    			try {
					String[] s = v.split("-");
					res[i][0] = Integer.valueOf(s[0]);
					res[i][1] = Integer.valueOf(s[1]);
					i++;
				} catch (NumberFormatException e) {
					//e.printStackTrace();
				}
    		}
    	}
    	return res;
	}
    
    public static void deleteCartCookie(HttpServletRequest request, 
    		HttpServletResponse response, String name) {
    	delCookie(request, response, name);
    }
}
