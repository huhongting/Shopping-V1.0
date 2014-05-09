package cn.edu.jnu.web.view.dao;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品评论数据交换类
 * @author HHT
 *
 */
public class CommentProduct implements Serializable {
	private static final long serialVersionUID = -646130332519453404L;
	
	private String orderId;// 订单编号
	private int bookId;// 图书编号
	private String bookName;// 图书名称
	private Date orderCreateDate;// 订单创建时间
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public Date getOrderCreateDate() {
		return orderCreateDate;
	}
	public void setOrderCreateDate(Date orderCreateDate) {
		this.orderCreateDate = orderCreateDate;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	@Override
	public int hashCode() {
		return (orderId + bookId).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj instanceof CommentProduct) {
			CommentProduct cp = (CommentProduct) obj;
			if((cp.getBookId() == this.getBookId())
					&& cp.getBookName().equals(this.getBookName())
					&& cp.getOrderId().equals(this.getOrderId())) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}
}
