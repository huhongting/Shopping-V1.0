package cn.edu.jnu.web.entity.menu;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 功能菜单节点实体类
 * @author HHT
 *
 */
@Entity
@Table(name="node")
public class Node implements Serializable {
	private static final long serialVersionUID = 5316337959449896299L;
	
	private int id;// 编号
	private String text;// 名称
	private boolean leaf;// 是否叶子节点
	private Node parent;// 父节点
	private Set<Node> children = new HashSet<Node>();// 子节点
	
	public Node() {}
	/**
	 * @param id
	 * @param text
	 * @param leaf
	 * @param parent
	 * @param children
	 */
	public Node(String text, boolean leaf, Node parent,
			Set<Node> children) {
		super();
		this.text = text;
		this.leaf = leaf;
		this.parent = parent;
		this.children = children;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="text", length=20, nullable=false)
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Column(name="leaf", nullable=false)
	public boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="parentid")
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	@OneToMany(cascade={CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy="parent")
	public Set<Node> getChildren() {
		return children;
	}
	public void setChildren(Set<Node> children) {
		this.children = children;
	}
}
