package cn.edu.jnu.web.view.dao;

import java.io.Serializable;
import java.util.List;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.util.QueryResult;

/**
 * �洢ָ�����ͼ����������µ�����ͼ�鸨����
 * @author HHT
 *
 */
public class TypeBookList implements Serializable {
	private static final long serialVersionUID = -6374895271123478230L;
	
	private BookType type;// ͼ������
	private QueryResult<Book> books;// �����ͼ��������µ�ͼ��
	private List<BookType> childTypes;// ������
	
	/**
	 * ��ȡͼ������
	 * @return
	 */
	public BookType getType() {
		return type;
	}
	public void setType(BookType type) {
		this.type = type;
	}
	/**
	 * ��ȡͼ���б�
	 * @return
	 */
	public QueryResult<Book> getBooks() {
		return books;
	}
	public void setBooks(QueryResult<Book> books) {
		this.books = books;
	}
	/**
	 * ��ȡ�����͵�����������
	 * @return
	 */
	public List<BookType> getChildTypes() {
		return childTypes;
	}
	public void setChildTypes(List<BookType> childTypes) {
		this.childTypes = childTypes;
	}
}
