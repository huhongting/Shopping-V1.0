package cn.edu.jnu.web.service;

import java.util.LinkedHashMap;
import java.util.List;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.util.QueryResult;

/**
 * 图书的服务类接口，定义图书需要的特定服务方法
 * @author HHT
 *
 */
public interface BookService {
	/**
	 * 保存书籍
	 * @param book
	 */
	void saveBook(Book book);
	/**
	 * 删除书籍
	 * @param bookId
	 * @return
	 */
	Book deleteBook(int bookId);
	/**
	 * 批量删除书籍
	 * @param bookIds
	 */
	void deleteBook(int[] bookIds);
	/**
	 * 更新书籍
	 * @param book
	 */
	void updateBook(Book book);
	/**
	 * 查询书籍
	 * @param id
	 * @return
	 */
	Book findBook(int id);
	/**
	 * 查询书籍
	 * @param id
	 * @return
	 */
	Book showBook(int id);
	/**
	 * 根据书籍名称查询
	 * @param name
	 * @return
	 */
	Book findBook(String name);
	/**
	 * 分页条件查询
	 * @param start
	 * @param limit
	 * @param wheres
	 * @return
	 */
	QueryResult<Book> list(String start, String limit, String[] ands);
	/**
	 * 分页条件查询
	 * @param start
	 * @param limit
	 * @param ands
	 * @param ors
	 * @return
	 */
	QueryResult<Book> list(String start, String limit, String[] ands, String[] ors);
	/**
	 * 分页查询
	 * @param start
	 * @param limit
	 * @return
	 */
	QueryResult<Book> list(String start, String limit);
	/**
	 * 获取新书推荐
	 * @return
	 */
	List<Book> listNewRecommends(int start, int limit);
	
	/**
	 * 分页排序查询某类别及其子类别下的所有图书
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
	 * 模糊查询特定类别下的图书
	 * @param start
	 * @param limit
	 * @param ands
	 * @param ors
	 * @param types
	 * @return
	 */
	QueryResult<Book> listTypeBooks(int start, int limit, BookType type);
	/**
	 * 分页查询促销图书
	 * @param start
	 * @param limit
	 * @return
	 */
	QueryResult<Book> listCuXiao(int start, int limit);
	/**
	 * 分页查询某类型及其子类型下的促销图书
	 * @param start
	 * @param limit
	 * @param type
	 * @return
	 */
	QueryResult<Book> listCuXiao(int start, int limit, BookType type);
	/**
	 * 分页条件查询特价图书
	 * @param start
	 * @param limit
	 * @param ands
	 * @return
	 */
	QueryResult<Book> listTeJia(int start, int limit, String[] ands);
	/**
	 * 分页条件排序查询特定列表及子类别下单特价图书
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
