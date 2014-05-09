package cn.edu.jnu.web.view.dao;

import java.io.Serializable;

import cn.edu.jnu.web.entity.Book;

/**
 * 购物车图书类<br>
 * 包含图书和数量
 * @author HHT
 *
 */
public class CarBook implements Serializable {
	private static final long serialVersionUID = 1599153067727198134L;
	private Book book;
	private int num;
	
	public CarBook() {}
	
	public CarBook(Book book, int num) {
		this.book = book;
		this.num = num;
	}
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Override
	public int hashCode() {
		return (book.getId() + book.getBookName()).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		else if(obj instanceof CarBook) {
			CarBook cb = (CarBook) obj;
			if(cb.getBook().getId() == this.getBook().getId()) return true;
			return false;
		}
		return false;
	}
}