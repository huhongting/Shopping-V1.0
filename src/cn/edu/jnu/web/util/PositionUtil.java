package cn.edu.jnu.web.util;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.BookType;

/**
 * 
 * @author HHT
 *
 */
public class PositionUtil {
	/**
	 * ���ݵ�ǰ�ڵ��ȡ���и��ڵ㣬������λ�õ����˵�
	 * @param bt ��ǰ�ڵ�
	 * @return
	 */
	public static String getPosition(BookType bt, String url) {
		StringBuffer buff = new StringBuffer();
		if(bt.getParent() != null) 
			addPos(buff, bt.getParent(), url);
		buff.append(bt.getTypeName());
		return buff.toString();
	}
	
	/**
	 * ���ݵ�ǰ�鼮����λ�õ����˵�
	 * @param bt ��ǰ�ڵ�
	 * @return
	 */
	public static String getPosition(Book book, String url) {
		StringBuffer buff = new StringBuffer();
		addPos(buff, book.getBookType(), url);
		buff.append(book.getBookName());
		return buff.toString();
	}

	private static void addPos(StringBuffer buff, BookType parent,
			String url) {
		if(parent.getParent() != null) {
			addPos(buff, parent.getParent(), url);
		}
		buff.append("<a href='");
		buff.append(url);
		buff.append("?type=");
		buff.append(parent.getTypeId());
		buff.append("&start=0&limit=15");
		buff.append("'>");
		buff.append(parent.getTypeName());
		buff.append("</a>");
		buff.append(" &gt; ");
	}
}
