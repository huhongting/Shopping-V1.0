package cn.edu.jnu.web.view.dao;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import cn.edu.jnu.web.entity.BookType;

/**
 * 菜单栏图书类型保存类<br>
 * rootType保存根图书类型<br>
 * childTypes保存根类型下所有子类型
 * @author HHT
 *
 */
public class MenuList implements Serializable {
	private static final long serialVersionUID = -5018249662803626554L;
	
	private BookType rootType;// 根类型
	private Set<BookType> childTypes = new TreeSet<BookType>();// 所有子类型
	
	public BookType getRootType() {
		return rootType;
	}
	public void setRootType(BookType rootType) {
		this.rootType = rootType;
	}
	public Set<BookType> getChildTypes() {
		return childTypes;
	}
	public void setMenu(BookType root) {
		this.rootType = root;// 设置根类型
		addMenu(root.getChildTypes());// 添加子类型
		
	}
	private void addMenu(Set<BookType> children) {
		for(BookType bt : children) {
			if(!bt.getDeleted()) {
				this.childTypes.add(bt);
				if(!bt.getLeaf()) {
					addMenu(bt.getChildTypes());
				}
			}
		}
	}
}
