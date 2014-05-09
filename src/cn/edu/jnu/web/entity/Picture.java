package cn.edu.jnu.web.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 前台首页滚动图片实体类
 * @author HHT
 *
 */
@Entity
@Table
public class Picture implements Serializable {
	private static final long serialVersionUID = 1418116348927708668L;
	private int id;
	private String name;// 图片名称
	private String url;// 图片应用地址
	private String path;// 图片实际存储路径
	private String action = "javascript:void(0);";// 图片跳转连接
	private Date uptime;// 图片上传时间
	private String note;// 备注
	private boolean isshow = false;// 是否显示
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(length=100, nullable=false)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getUptime() {
		return uptime;
	}
	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean getIsshow() {
		return isshow;
	}
	public void setIsshow(boolean show) {
		this.isshow = show;
	}
	@Column(nullable=false)
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
