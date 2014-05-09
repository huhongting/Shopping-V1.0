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
import javax.persistence.Table;

import cn.edu.jnu.web.entity.user.User;

/**
 * Ա������¼ʵ����
 * @author HHT
 *
 */
@Entity
@Table(name="empdate")
public class EmpDate implements Serializable {
	private static final long serialVersionUID = 3450196465782404046L;
	
	private int id;
	private User createEmp;// ����Ա��
	private Date createTime = new Date();// ����ʱ��
	private String activeTime;// �ʱ��
	private String title;// ����
	private String content;// ����
	private String remark;// ����˵��
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne
	@JoinColumn(name="createemp")
	public User getCreateEmp() {
		return createEmp;
	}
	public void setCreateEmp(User createEmp) {
		this.createEmp = createEmp;
	}
	@Column(name="createtime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name="activetime", length=50)
	public String getActiveTime() {
		return activeTime;
	}
	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
