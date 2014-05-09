package cn.edu.jnu.web.entity;

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
import javax.persistence.Table;

/**
 * 书籍实体类
 * @author HHT
 *
 */
@Entity
@Table(name="book")
public class Book implements Serializable {
	private static final long serialVersionUID = 1534134089273554558L;
	
	private int id;// 编号
	private String bookName;// 书籍名称
	private String saleInfo;// 促销信息
	private String auther;// 作者
	private String pubTime;// 出版时间
	private String picUrl;// 封面地址
	private String note;// 说明信息
	private double price;// 售价
	private double normalPrice;// 市场价
	private String remark;// 备注
	private Press press;// 出版社
	private BookType bookType;// 书籍类型
	private boolean delFlag = false;// 是否删除的标记，默认false
	private int number = 0;// 库存量
	private boolean hot = false;// 经典
	private boolean recom = false; // 推荐
	private Date upTime = new Date();
	private Date downTime;
	private int saleCount = 0;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="bookid")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name="bookname", nullable=false, length=100)
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	@Column(name="auther", nullable=false, length=100)
	public String getAuther() {
		return auther;
	}
	public void setAuther(String auther) {
		this.auther = auther;
	}
	@Column(name="pubtime", nullable=false, length=15)
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	@Column(name="picurl", nullable=false, length=100)
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	@Lob
	@Column(name="note")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Column(name="price", nullable=false)
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	@Column(name="normalprice")
	public double getNormalPrice() {
		return normalPrice;
	}
	public void setNormalPrice(double normalPrice) {
		this.normalPrice = normalPrice;
	}
	@Lob
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Lob
	@Column(name="saleinfo")
	public String getSaleInfo() {
		return saleInfo;
	}
	public void setSaleInfo(String saleInfo) {
		this.saleInfo = saleInfo;
	}
	@ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="pressid")
	public Press getPress() {
		return press;
	}
	public void setPress(Press press) {
		this.press = press;
	}
	@ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="typeid")
	public BookType getBookType() {
		return bookType;
	}
	public void setBookType(BookType bookType) {
		this.bookType = bookType;
	}
	@Column(name="delflag", nullable=false)
	public boolean getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(boolean delFlag) {
		this.delFlag = delFlag;
	}
	
	@Column(name="number", nullable=false)
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	@Column(name="hot", nullable=false)
	public boolean getHot() {
		return hot;
	}
	public void setHot(boolean hot) {
		this.hot = hot;
	}
	@Column(name="recom", nullable=false)
	public boolean getRecom() {
		return recom;
	}
	public void setRecom(boolean recom) {
		this.recom = recom;
	}
	@Column(name="uptime")
	public Date getUpTime() {
		return upTime;
	}
	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
	@Column(name="downtime")
	public Date getDownTime() {
		return downTime;
	}
	public void setDownTime(Date downTime) {
		this.downTime = downTime;
	}
	@Column(name="salecount")
	public int getSaleCount() {
		return saleCount;
	}
	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}
}
