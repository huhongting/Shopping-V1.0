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
 * ����ʵ����
 * @author HHT
 *
 */
@Entity
@Table(name="orders")
public class Order implements Serializable {
	private static final long serialVersionUID = 8033668282430418797L;
	/* ������ */
	private String orderid;
	/* �����û� */
	private User buyer;
	/* ��������ʱ�� */
	private Date createDate = new Date();
	/* ����״̬ */
	private OrderState state;
	/* ��Ʒ�ܽ�� */
	private Float productTotalPrice = 0f;
	/* �����ܽ�� */
	private Float totalPrice= 0f;
	/* Ӧ����(ʵ����Ҫ֧���ķ���) */
	private Float epay = 0f;
	/* �˿͸��� */
	private String note = null;
	/* ��Ʊ */
	private String fapiao = null;
	/* ֧����ʽ */
	private PaymentWay paymentWay;
	/* ֧��״̬ */
    private Boolean paymentstate = false;
    /* ����������Ϣ */
	private OrderDeliverInfo orderDeliverInfo;
	/* ���ͷ�ʽ */
	private DeliverWay deliverWay;
	/* ������Ʒ */ 
	private Set<OrderItem> items = new HashSet<OrderItem>();
	/* �����ö�����Ա��,�����ֵ��Ϊnull,������������ */
	private String employee;
	/* Ա������ */
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
	 * ��Ӷ�������
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
	 * ����ͼ�����Ʋ��Ҷ����еĶ�����Ʒ
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
	 * ��Ӷ�����
	 * @param item ������
	 */
	public Order addOrderItem(OrderItem item){
		this.items.add(item);
		item.setOrder(this);
		return this;
	}
	/**
	 * ������<br>
	 * ��ʽ��yyMMddHHmm + 6��0-9�����������16λ
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
