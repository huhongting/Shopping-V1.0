package cn.edu.jnu.web.entity.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cn.edu.jnu.web.entity.permission.Group;



/**
 * 用户实体类
 * @author HHT
 *
 */
@Entity
@Table(name="user")
public class User implements Serializable{
	private static final long serialVersionUID = 6196671119571186603L;
	
	public static String DEFAULT_SYS_NAME = "admin.ishare.com";
	public static String DEFAULT_SYS_PASS = "ishare.com.admin";
	
	// 用户类型
	public static int CUSTOMER = 0;
	public static int EMPLOYEE = 1;
	public static int ADMINISTRATOR = 2;
	
	private int id;// 编号
	private int userType = CUSTOMER;// 用户类型：2-超级管理员 1-公司员工 0-普通用户。默认为0。
	private String name;// 名称
	private String password;// 密码
	private String email;// Email
	private ContactInfo contactInfo;
	private String note;// 说明
	private Date regTime = new Date();// 注册时间
	private Date lastLogin;// 上次登入时间，给网站注册会员使用
	private Account account;// 网站电子货币账户
	private boolean delete = false;// 是否删除
	private Group group;// 所属权限组，给网站员工使用
	
	public User() { }
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="name",nullable=false, length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="password", nullable=false, length=100)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="email", length=50)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Lob
	@Column(name="note")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	@Column(name="usertype", nullable=false)
	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	@Column(name="regtime", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="contactid")
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="accountid")
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
	}
	@Column(name="lastlogin")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	@Column(name="deleted", nullable=false)
	public boolean getDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	@ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name="groupid")
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
	@Override
	public int hashCode() {
		return (this.getId() + this.getName()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj instanceof User) {
			User u = (User) obj;
			if(u.getId() == this.getId()) return true;
			return false;
		}
		return false;
	}
}
