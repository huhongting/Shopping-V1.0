package cn.edu.jnu.web.authority;

/**
 * 系统权限
 * @author HHT
 *
 */
public enum AuthorityType {
	CREATE_DELETE_NOTIFICATION("创建/删除通知", 1),
	MODIFY_WEB_ADDS("管理前台广告", 2),
	MODIFY_EMPLOYEE("在职员工管理", 3),
	MODIFY_DELETED_EMPLOYEE("离职员工管理", 4),
	MODIFY_CUSTOMER("用户管理", 5),
	MODIFY_DELCUSTOMER("已禁用用户管理", 6),
	MODIFY_COMMENTS("评论管理", 7),
	MODIFY_BOOKS("书籍管理", 8),
	MODIFY_BOOKTYPE("书籍类别管理", 9),
	MODIFY_PRESS("出版社管理", 10),
	MODIFY_MENU_SORT("菜单排序管理", 11),
	MODIFY_DELETED_BOOKS("已删除书籍管理", 12),
	ADMIN_UNLOCK_ORDER("解锁订单", 13),
	COMFIRM_ORDER("审核订单", 14),
	CHANGE_PAYMENTWAY("修改支付方式", 15),
	LEAVE_MESSAGE("客服留言", 16),
	CHECK_ORDER_MONEY("财务确认账款", 17),
	CANCEL_ORDER("取消订单", 18),
	WAIT_ORDER_PRODUCT("订单配货", 19),
	RESET_NEW_ORDER("生成新订单", 20),
	DELIVER_ORDER("订单发货", 21),
	RECEIVE_ORDER("确认收货", 22),
	RESET_ORDER("恢复订单", 23),
	MODIFY_AUTHORITY("权限组管理", 24);
	
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
	 * 根据索引查找权限
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
	 * 根据权限名查找权限
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
