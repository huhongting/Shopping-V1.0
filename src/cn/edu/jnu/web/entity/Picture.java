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
 * ǰ̨��ҳ����ͼƬʵ����
 * @author HHT
 *
 */
@Entity
@Table
public class Picture implements Serializable {
	private static final long serialVersionUID = 1418116348927708668L;
	private int id;
	private String name;// ͼƬ����
	private String url;// ͼƬӦ�õ�ַ
	private String path;// ͼƬʵ�ʴ洢·��
	private String action = "javascript:void(0);";// ͼƬ��ת����
	private Date uptime;// ͼƬ�ϴ�ʱ��
	private String note;// ��ע
	private boolean isshow = false;// �Ƿ���ʾ
	
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
