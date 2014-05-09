package cn.edu.jnu.web.entity.order;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.user.User;

/**
 * 用户评论实体类
 * @author HHT
 *
 */
@Entity
public class Comment {
	private int id;
	private String title;// 评论标题
	private String content;// 评论内容
	private Date date = new Date();// 评论时间
	private Book book;// 所属图书
	private Order order;// 所属订单
	private User user;// 评论人
	private int usefull = 0;// 认为有用
	private int useless = 0;// 认为没用
	private boolean delflag = false;// 是否删除标记,删除后只有本人能在控制中心看到,不显示到评论列表
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Lob
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@ManyToOne
	@JoinColumn(name="bookid")
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	@ManyToOne
	@JoinColumn(name="orderid")
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	@ManyToOne
	@JoinColumn(name="userid")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getUsefull() {
		return usefull;
	}
	public void setUsefull(int usefull) {
		this.usefull = usefull;
	}
	public int getUseless() {
		return useless;
	}
	public void setUseless(int useless) {
		this.useless = useless;
	}
	public boolean getDelflag() {
		return delflag;
	}
	public void setDelflag(boolean delflag) {
		this.delflag = delflag;
	}
}
