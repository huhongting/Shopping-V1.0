package cn.edu.jnu.web.authority;

/**
 * ϵͳȨ��
 * @author HHT
 *
 */
public enum AuthorityType {
	CREATE_DELETE_NOTIFICATION("����/ɾ��֪ͨ", 1),
	MODIFY_WEB_ADDS("����ǰ̨���", 2),
	MODIFY_EMPLOYEE("��ְԱ������", 3),
	MODIFY_DELETED_EMPLOYEE("��ְԱ������", 4),
	MODIFY_CUSTOMER("�û�����", 5),
	MODIFY_DELCUSTOMER("�ѽ����û�����", 6),
	MODIFY_COMMENTS("���۹���", 7),
	MODIFY_BOOKS("�鼮����", 8),
	MODIFY_BOOKTYPE("�鼮������", 9),
	MODIFY_PRESS("���������", 10),
	MODIFY_MENU_SORT("�˵��������", 11),
	MODIFY_DELETED_BOOKS("��ɾ���鼮����", 12),
	ADMIN_UNLOCK_ORDER("��������", 13),
	COMFIRM_ORDER("��˶���", 14),
	CHANGE_PAYMENTWAY("�޸�֧����ʽ", 15),
	LEAVE_MESSAGE("�ͷ�����", 16),
	CHECK_ORDER_MONEY("����ȷ���˿�", 17),
	CANCEL_ORDER("ȡ������", 18),
	WAIT_ORDER_PRODUCT("�������", 19),
	RESET_NEW_ORDER("�����¶���", 20),
	DELIVER_ORDER("��������", 21),
	RECEIVE_ORDER("ȷ���ջ�", 22),
	RESET_ORDER("�ָ�����", 23),
	MODIFY_AUTHORITY("Ȩ�������", 24);
	
	private String name;
	private int index;
	private AuthorityType(String name, int index) {
		this.setName(name);
		this.setIndex(index);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * ������������Ȩ��
	 * @param index
	 * @return
	 */
	public static AuthorityType getAuthorityType(int index) {
		for(AuthorityType at : AuthorityType.values()) {
			if(at.getIndex() == index) return at;
		}
		return null;
	}
	
	/**
	 * ����Ȩ��������Ȩ��
	 * @param name
	 * @return
	 */
	public static AuthorityType getAuthorityType(String name) {
		for(AuthorityType at : AuthorityType.values()) {
			if(name.equals(at.getName())) return at;
		}
		return null;
	}

}
