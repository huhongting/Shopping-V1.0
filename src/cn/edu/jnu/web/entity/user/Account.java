package cn.edu.jnu.web.entity.user;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 网站电子货币账户
 * @author HHT
 *
 */
@Entity
@Table(name="account")
public class Account implements Serializable {
	private static final long serialVersionUID = 3593544518154076865L;
	
	public static String CMD_CUNRU = "存入";
	public static String CMD_ZHIFU = "支付";
	
	private int id;
	private double money = 0;// 账户余额
	private Set<AccountRecord> records = new HashSet<AccountRecord>();// 账户历史操作记录
	private Date ctime = new Date();// 创建时间
	private String note;// 其他说明
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@OneToMany(mappedBy="account", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	public Set<AccountRecord> getRecords() {
		return records;
	}
	public void setRecords(Set<AccountRecord> records) {
		this.records = records;
	}
	
	public Account addRecord(AccountRecord record) {
		record.setAccount(this);
		this.records.add(record);
		return this;
	}
	
	public Account saveMoney(double money, String remark) {
		AccountRecord ar = new AccountRecord();
		this.money = this.money + money;
		ar.setCmd(CMD_CUNRU);
		ar.setMoney(money);
		ar.setRemark(remark);
		return addRecord(ar);
	}
	public Account payMoney(double money, String remark) {
		AccountRecord ar = new AccountRecord();
		this.money = this.money - money;
		ar.setCmd(CMD_ZHIFU);
		ar.setMoney(-money);
		ar.setRemark(remark);
		return addRecord(ar);
	}
}
