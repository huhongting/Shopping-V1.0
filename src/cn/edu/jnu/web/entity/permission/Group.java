package cn.edu.jnu.web.entity.permission;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import cn.edu.jnu.web.entity.user.User;

/**
 * 权限组实体类
 * @author HHT
 *
 */
@Entity
@Table(name="groups")
public class Group implements Serializable {
	private static final long serialVersionUID = -7246549253075954325L;
	private int id;
	private String name;// 名称
	private Set<User> emps = new HashSet<User>();// 用户
	private String authority;// 权限字符串
	private Date createDate = new Date();// 创建时间
	private User creater;// 创建人
	private String remark;// 备注
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@OneToMany(mappedBy="group", fetch=FetchType.EAGER)
	public Set<User> getEmps() {
		return emps;
	}
	public void setEmps(Set<User> emps) {
		this.emps = emps;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	@Column(length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Group addEmployee(User emp) {
		if(emp == null) return this;
		emp.setGroup(this);
		this.emps.add(emp);
		return this;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name="createdate")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="createrid")
	public User getCreater() {
		return creater;
	}
	public void setCreater(User creater) {
		this.creater = creater;
	}
	@Override
	public int hashCode() {
		return (this.getName() + this.getId()).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj instanceof Group) {
			Group g = (Group) obj;
			if(g.getId() == this.getId()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	
}
