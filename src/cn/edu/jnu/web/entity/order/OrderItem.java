package cn.edu.jnu.web.entity.order;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * ������Ʒʵ����
 * @author HHT
 *
 */
@Entity
public class OrderItem implements Serializable {
	private static final long serialVersionUID = 8763843749122672024L;
	private Integer itemid;
	/* ��Ʒ���� */
	private String productName;
	/* ��Ʒid */
	private Integer productid;
	/* ��Ʒ���ۼ� */
	private Double productPrice;
	/* �������� */
	private Integer amount=1;
	/* �������� */
	private Order order;
	/* �Ƿ����� */
	private boolean iscomment = false;
	
	public OrderItem(){}
	
	/**
	 * @param productName
	 * @param productid
	 * @param productPrice
	 * @param amount
	 * @param order
	 */
	public OrderItem(String productName, Integer productid,
			Double productPrice, Integer amount, Order order) {
		this.productName = productName;
		this.productid = productid;
		this.productPrice = productPrice;
		this.amount = amount;
		this.order = order;
	}



	@Id @GeneratedValue
	public Integer getItemid() {
		return itemid;
	}
	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}
	@Column(length=50,nullable=false)
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@Column(nullable=false)
	public Integer getProductid() {
		return productid;
	}
	public void setProductid(Integer productid) {
		this.productid = productid;
	}
	@Column(nullable=false)
	public Double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}
	@Column(nullable=false)
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	@ManyToOne(cascade={CascadeType.REFRESH,CascadeType.MERGE})
	@JoinColumn(name="orderid")
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public boolean getIscomment() {
		return iscomment;
	}

	public void setIscomment(boolean commented) {
		this.iscomment = commented;
	}
}
