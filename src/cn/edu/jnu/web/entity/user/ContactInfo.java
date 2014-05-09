package cn.edu.jnu.web.entity.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * ��ϵ��Ϣʵ����
 * @author HHT
 *
 */
@Entity
public class ContactInfo implements Serializable{
	private static final long serialVersionUID = -4336182674133849896L;
	private int id;
	/** ��ַ **/
	private String address;
	/** �ʱ� **/
	private String postalcode;
	/** ���� **/
	private String phone;
	/** �ֻ� **/
	private String mobile;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=100,nullable=false)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(length=6)
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postcode) {
		this.postalcode = postcode;
	}
	@Column(length=20)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column(length=11)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
