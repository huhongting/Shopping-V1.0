package cn.edu.jnu.web.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * ͼ������ʵ����
 * @author HHT
 */
@Entity
@Table(name="booktype")
public class BookType implements Serializable, Comparable<BookType> {
	private static final long serialVersionUID = 4790578720750980494L;

	private int typeId;// ͼ�����id
	private String typeName;// ͼ���������
	private String note;// ͼ����������
	private boolean deleted = false;// ͼ������Ƿ�ɾ���ı�־
	private BookType parent = null;// ͼ�����ĸ����
	private boolean isLeaf = true;// �Ƿ�Ҷ�ӽڵ㣬���Ƿ����������
	private Set<BookType> childTypes = new HashSet<BookType>();
	private int sortId = 11;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="typeid")
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	@Column(name="typename", length=100, nullable=false)
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Lob
	@Column(name="note")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(name="deleted", nullable=false)
	public boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(boolean delFlag) {
		this.deleted = delFlag;
	}
	
	@ManyToOne(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="parentid")
	public BookType getParent() {
		return parent;
	}
	public void setParent(BookType parent) {
		this.parent = parent;
	}
	
	@OneToMany(cascade={CascadeType.ALL}, mappedBy="parent", fetch=FetchType.EAGER)
	public Set<BookType> getChildTypes() {
		return childTypes;
	}
	public void setChildTypes(Set<BookType> childTypes) {
		this.childTypes = childTypes;
	}
	@Column(name="isleaf", nullable=false)
	public boolean getLeaf() {
		return isLeaf;
	}
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	@Column(name="sortid", nullable=false)
	public int getSortId() {
		return sortId;
	}
	public void setSortId(int sortId) {
		this.sortId = sortId;
	}
	@Override
	public int compareTo(BookType bt) {
		return this.getTypeName().compareTo(bt.getTypeName());
	}
}
