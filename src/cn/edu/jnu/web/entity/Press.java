package cn.edu.jnu.web.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 出版社实体类
 * @author HHT
 *
 */
@Entity
@Table(name="press")
public class Press implements Serializable {
	private static final long serialVersionUID = 3651870346371566659L;
	
	private int id;// 编号
	private String name;// 名称
	private boolean deleted = false;// 是否删除标记，默认false
	private String remark;// 备注
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pressid")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="pressname", nullable=false, length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Lob
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name="deleted", nullable=false)
	public boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(boolean delFlag) {
		this.deleted = delFlag;
	}
}
