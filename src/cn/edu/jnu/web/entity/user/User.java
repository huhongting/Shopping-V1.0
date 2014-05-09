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
 * �û�ʵ����
 * @author HHT
 *
 */
@Entity
@Table(name="user")
public class User implements Serializable{
	private static final long serialVersionUID = 6196671119571186603L;
	
	public static String DEFAULT_SYS_NAME = "admin.ishare.com";
	public static String DEFAULT_SYS_PASS = "ishare.com.admin";
	
	// �û�����
	public static int CUSTOMER = 0;
	public static int EMPLOYEE = 1;
	public static int ADMINISTRATOR = 2;
	
	private int id;// ���
	private int userType = CUSTOMER;// �û����ͣ�2-��������Ա 1-��˾Ա�� 0-��ͨ�û���Ĭ��Ϊ0��
	private String name;// ����
	private String password;// ����
	private String email;// Email
	private ContactInfo contactInfo;
	private String note;// ˵��
	private Date regTime = new Date();// ע��ʱ��
	private Date lastLogin;// �ϴε���ʱ�䣬����վע���Աʹ��
	private Account account;// ��վ���ӻ����˻�
	private boolean delete = false;// �Ƿ�ɾ��
	private Group group;// ����Ȩ���飬����վԱ��ʹ��
	
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
