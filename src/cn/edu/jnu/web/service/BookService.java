package cn.edu.jnu.web.service;

import java.util.LinkedHashMap;
import java.util.List;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.util.QueryResult;

/**
 * ͼ��ķ�����ӿڣ�����ͼ����Ҫ���ض����񷽷�
 * @author HHT
 *
 */
public interface BookService {
	/**
	 * �����鼮
	 * @param book
	 */
	void saveBook(Book book);
	/**
	 * ɾ���鼮
	 * @param bookId
	 * @return
	 */
	Book deleteBook(int bookId);
	/**
	 * ����ɾ���鼮
	 * @param bookIds
	 */
	void deleteBook(int[] bookIds);
	/**
	 * �����鼮
	 * @param book
	 */
	void updateBook(Book book);
	/**
	 * ��ѯ�鼮
	 * @param id
	 * @return
	 */
	Book findBook(int id);
	/**
	 * ��ѯ�鼮
	 * @param id
	 * @return
	 */
	Book showBook(int id);
	/**
	 * �����鼮���Ʋ�ѯ
	 * @param name
	 * @return
	 */
	Book findBook(String name);
	/**
	 * ��ҳ������ѯ
	 * @param start
	 * @param limit
	 * @param wheres
	 * @return
	 */
	QueryResult<Book> list(String start, String limit, String[] ands);
	/**
	 * ��ҳ������ѯ
	 * @param start
	 * @param limit
	 * @param ands
	 * @param ors
	 * @return
	 */
	QueryResult<Book> list(String start, String limit, String[] ands, String[] ors);
	/**
	 * ��ҳ��ѯ
	 * @param start
	 * @param limit
	 * @return
	 */
	QueryResult<Book> list(String start, String limit);
	/**
	 * ��ȡ�����Ƽ�
	 * @return
	 */
	List<Book> listNewRecommends(int start, int limit);
	
	/**
	 * ��ҳ�����ѯĳ�����������µ�����ͼ��
	 * @param ands
	 * @param sorts
	 * @param start
	 * @param limit
	 * @param type
	 * @return
	 */
	QueryResult<Book> listBooks(String[] ands, LinkedHashMap<String, String> sorts, 
			int start, int limit, BookType type);
	/**
	 * ģ����ѯ�ض�����µ�ͼ��
	 * @param start
	 * @param limit
	 * @param ands
	 * @param ors
	 * @param types
	 * @return
	 */
	QueryResult<Book> listTypeBooks(int start, int limit, BookType type);
	/**
	 * ��ҳ��ѯ����ͼ��
	 * @param start
	 * @param limit
	 * @return
	 */
	QueryResult<Book> listCuXiao(int start, int limit);
	/**
	 * ��ҳ��ѯĳ���ͼ����������µĴ���ͼ��
	 * @param start
	 * @param limit
	 * @param type
	 * @return
	 */
	QueryResult<Book> listCuXiao(int start, int limit, BookType type);
	/**
	 * ��ҳ������ѯ�ؼ�ͼ��
	 * @param start
	 * @param limit
	 * @param ands
	 * @return
	 */
	QueryResult<Book> listTeJia(int start, int limit, String[] ands);
	/**
	 * ��ҳ���������ѯ�ض��б�������µ��ؼ�ͼ��
	 * @param start
	 * @param limit
	 * @param type
	 * @param ands
	 * @param sorts
	 * @return
	 */
	QueryResult<Book> listTeJia(int start, int limit, BookType type, 
			String[] ands, LinkedHashMap<String, String> sorts);
}
