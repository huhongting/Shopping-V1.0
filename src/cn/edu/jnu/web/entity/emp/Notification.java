package cn.edu.jnu.web.entity.emp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import cn.edu.jnu.web.entity.user.User;

/**
 * ϵͳ֪ͨʵ����
 * @author HHT
 *
 */
@Entity
public class Notification implements Serializable {
	private static final long serialVersionUID = 7612889354046888405L;
	
	public static String NOTIFICATIONTYPE_IMPORTANT = "IMPORTANT";
	public static String NOTIFICATIONTYPE_NORMAL = "NORMAL";
	
	private int id;
	private User creater;// ������
	private Date createDate = new Date();// ����ʱ��
	private String title;// ֪ͨ����
	private String content;// ֪ͨ����
	private String status = NOTIFICATIONTYPE_NORMAL;// ֪ͨ״̬��Ĭ����ͨ
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="creater")
	public User getCreater() {
		return creater;
	}
	public void setCreater(User creater) {
		this.creater = creater;
	}
	@Column(name="createdate")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	@Column(length=20)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
