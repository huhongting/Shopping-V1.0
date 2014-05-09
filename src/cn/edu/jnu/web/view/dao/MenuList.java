package cn.edu.jnu.web.view.dao;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import cn.edu.jnu.web.entity.BookType;

/**
 * �˵���ͼ�����ͱ�����<br>
 * rootType�����ͼ������<br>
 * childTypes���������������������
 * @author HHT
 *
 */
public class MenuList implements Serializable {
	private static final long serialVersionUID = -5018249662803626554L;
	
	private BookType rootType;// ������
	private Set<BookType> childTypes = new TreeSet<BookType>();// ����������
	
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
		this.rootType = root;// ���ø�����
		addMenu(root.getChildTypes());// ���������
		
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
