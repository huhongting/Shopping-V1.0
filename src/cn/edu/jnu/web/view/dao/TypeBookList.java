package cn.edu.jnu.web.view.dao;

import java.io.Serializable;
import java.util.List;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.util.QueryResult;

/**
 * 存储指定类型及其子类型下的所有图书辅助类
 * @author HHT
 *
 */
public class TypeBookList implements Serializable {
	private static final long serialVersionUID = -6374895271123478230L;
	
	private BookType type;// 图书类型
	private QueryResult<Book> books;// 该类型及子类型下的图书
	private List<BookType> childTypes;// 子类型
	
	/**
	 * 获取图书类型
	 * @return
	 */
	public BookType getType() {
		return type;
	}
	public void setType(BookType type) {
		this.type = type;
	}
	/**
	 * 获取图书列表
	 * @return
	 */
	public QueryResult<Book> getBooks() {
		return books;
	}
	public void setBooks(QueryResult<Book> books) {
		this.books = books;
	}
	/**
	 * 获取该类型的所有子类型
	 * @return
	 */
	public List<BookType> getChildTypes() {
		return childTypes;
	}
	public void setChildTypes(List<BookType> childTypes) {
		this.childTypes = childTypes;
	}
}
