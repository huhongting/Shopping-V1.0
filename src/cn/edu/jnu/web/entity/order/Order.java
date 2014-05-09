package cn.edu.jnu.web.entity.order;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cn.edu.jnu.web.entity.user.OrderDeliverInfo;
import cn.edu.jnu.web.entity.user.User;

/**
 * 订单实体类
 * @author HHT
 *
 */
@Entity
@Table(name="orders")
public class Order implements Serializable {
	private static final long serialVersionUID = 8033668282430418797L;
	/* 订单号 */
	private String orderid;
	/* 所属用户 */
	private User buyer;
	/* 订单创建时间 */
	private Date createDate = new Date();
	/* 订单状态 */
	private OrderState state;
	/* 商品总金额 */
	private Float productTotalPrice = 0f;
	/* 订单总金额 */
	private Float totalPrice= 0f;
	/* 应付款(实际需要支付的费用) */
	private Float epay = 0f;
	/* 顾客附言 */
	private String note = null;
	/* 发票 */
	private String fapiao = null;
	/* 支付方式 */
	private PaymentWay paymentWay;
	/* 支付状态 */
    private Boolean paymentstate = false;
    /* 订单配送信息 */
	private OrderDeliverInfo orderDeliverInfo;
	/* 配送方式 */
	private DeliverWay deliverWay;
	/* 订单商品 */ 
	private Set<OrderItem> items = new HashSet<OrderItem>();
	/* 锁定该订单的员工,如果该值不为null,即订单被锁定 */
	private String employee;
	/* 员工留言 */
	private Set<Message> messages = new HashSet<Message>();
	private double alreadyPay = 0;
	private double payable = 0;
	
	public Order(){}
	
	public Order(String orderid) {
		this.orderid = orderid;
	}
	@OneToMany(mappedBy="order", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	public Set<Message> getMessages() {
		return messages;
	}
	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}
	@Enumerated(EnumType.STRING) 
	@Column(name="deliverway", length=23, nullable=false)
	public DeliverWay getDeliverWay() {
		return deliverWay;
	}
	public void setDeliverWay(DeliverWay deliverWay) {
		this.deliverWay = deliverWay;
	}
	@Column(length=20)
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	@Id @Column(length=16)
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	@ManyToOne(cascade=CascadeType.ALL,optional=false)
	@JoinColumn(name="userid")
	public User getBuyer() {
		return buyer;
	}
	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="createdate", nullable=false)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Enumerated(EnumType.STRING) @Column(length=16,nullable=false)
	public OrderState getState() {
		return state;
	}
	public void setState(OrderState state) {
		this.state = state;
	}
	@Column(name="producttotalprice", nullable=false)
	public Float getProductTotalPrice() {
		return productTotalPrice;
	}
	public void setProductTotalPrice(Float productTotalPrice) {
		this.productTotalPrice = productTotalPrice;
	}
	@Column(name="totalprice", nullable=false)
	public Float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}
	@Column(nullable=false)
	public Float getEpay() {
		return epay;
	}
	public void setEpay(Float epay) {
		this.epay = epay;
	}
	@Column(length=100)
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Enumerated(EnumType.STRING) 
	@Column(name="paymentway", length=20, nullable=false)
	public PaymentWay getPaymentWay() {
		return paymentWay;
	}
	public void setPaymentWay(PaymentWay paymentWay) {
		this.paymentWay = paymentWay;
	}
	@Column(nullable=false)
	public Boolean getPaymentstate() {
		return paymentstate;
	}
	public void setPaymentstate(Boolean paymentstate) {
		this.paymentstate = paymentstate;
	}
	@ManyToOne(cascade=CascadeType.ALL,optional=false)
	@JoinColumn(name="deliverid")
	public OrderDeliverInfo getOrderDeliverInfo() {
		return orderDeliverInfo;
	}
	public void setOrderDeliverInfo(OrderDeliverInfo orderDeliverInfo) {
		this.orderDeliverInfo = orderDeliverInfo;
	}
	
	@OneToMany(mappedBy="order",
			cascade={CascadeType.ALL},
			fetch=FetchType.EAGER)
	public Set<OrderItem> getItems() {
		return items;
	}
	public void setItems(Set<OrderItem> items) {
		this.items = items;
	}
	
	public String getFapiao() {
		return fapiao;
	}

	public void setFapiao(String fapiao) {
		this.fapiao = fapiao;
	}
	@Column(name="alreadypay")
	public double getAlreadyPay() {
		return alreadyPay;
	}

	public void setAlreadyPay(double alreadyPay) {
		this.alreadyPay = alreadyPay;
	}

	public double getPayable() {
		return payable;
	}

	public void setPayable(double payable) {
		this.payable = payable;
	}
	
	/**
	 * 添加订单留言
	 * @param content
	 * @param userName
	 */
	public void addOrderMsg(String content, String userName) {
		Message msg = new Message();
		msg.setContent(content);
		msg.setOrder(this);
		msg.setUsername(userName);
		this.messages.add(msg);
	}
	
	/**
	 * 根据图书名称查找订单中的订单商品
	 * @param bookName
	 * @return
	 */
	public OrderItem findOrderItemByBookName(String bookName) {
		Iterator<OrderItem> it = getItems().iterator();
		while(it.hasNext()) {
			OrderItem oi = it.next();
			if(oi.getProductName().equals(bookName)) return oi;
		}
		return null;
	}
	/**
	 * 添加订单项
	 * @param item 订单项
	 */
	public Order addOrderItem(OrderItem item){
		this.items.add(item);
		item.setOrder(this);
		return this;
	}
	/**
	 * 订单号<br>
	 * 格式：yyMMddHHmm + 6个0-9的随机数，共16位
	 * @return
	 */
	public static String createOrderId() {
		Random random = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		StringBuffer id = new StringBuffer();
		id.append(sdf.format(new Date()));
		for(int i=0; i<6; i++) {
			id.append(random.nextInt(10));
		}
		return id.toString();
	}
}
