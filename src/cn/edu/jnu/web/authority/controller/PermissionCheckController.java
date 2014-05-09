package cn.edu.jnu.web.authority.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.authority.AuthorityType;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.util.AuthorityUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 权限验证控制器
 * @author HHT
 *
 */
@Controller
public class PermissionCheckController {
	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 验证系统通知的发布和删除权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/notify.do")
	public @ResponseBody ObjectNode checkNotify(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.CREATE_DELETE_NOTIFICATION);
		return mapper.createObjectNode().put("notify", permission);
	}
	
	/**
	 * 验证订单解锁权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/adminunlock.do")
	public @ResponseBody ObjectNode checkUnlock(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.ADMIN_UNLOCK_ORDER);
		return mapper.createObjectNode().put("unlock", permission);
	}
	
	/**
	 * 验证订单审核权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/conformorder.do")
	public @ResponseBody ObjectNode checkConformOrder(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean conforder = AuthorityUtil.hasAuthority(emp, AuthorityType.COMFIRM_ORDER);
		boolean paymentway = AuthorityUtil.hasAuthority(emp, AuthorityType.CHANGE_PAYMENTWAY);
		boolean leafmsg = AuthorityUtil.hasAuthority(emp, AuthorityType.LEAVE_MESSAGE);
		return mapper.createObjectNode()
					 .put("conforder", conforder)
					 .put("paymentway", paymentway)
					 .put("leafmsg", leafmsg);
	}
	
	/**
	 * 验证订单收款权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/payorder.do")
	public @ResponseBody ObjectNode checkPayOrder(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean checkmoney = AuthorityUtil.hasAuthority(emp, AuthorityType.CHECK_ORDER_MONEY);
		boolean cancel = AuthorityUtil.hasAuthority(emp, AuthorityType.CANCEL_ORDER);
		boolean leafmsg = AuthorityUtil.hasAuthority(emp, AuthorityType.LEAVE_MESSAGE);
		return mapper.createObjectNode()
					 .put("chkmoney", checkmoney)
					 .put("cancel", cancel)
					 .put("leafmsg", leafmsg);
	}
	
	/**
	 * 验证配货权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/product.do")
	public @ResponseBody ObjectNode checkProductOrder(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean odrpro = AuthorityUtil.hasAuthority(emp, AuthorityType.WAIT_ORDER_PRODUCT);
		boolean resetorder = AuthorityUtil.hasAuthority(emp, AuthorityType.RESET_NEW_ORDER);
		boolean cancel = AuthorityUtil.hasAuthority(emp, AuthorityType.CANCEL_ORDER);
		boolean leafmsg = AuthorityUtil.hasAuthority(emp, AuthorityType.LEAVE_MESSAGE);
		return mapper.createObjectNode()
					 .put("odrpro", odrpro)
					 .put("resetorder", resetorder)
					 .put("cancel", cancel)
					 .put("leafmsg", leafmsg);
	}
	
	/**
	 * 验证送货权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/deliver.do")
	public @ResponseBody ObjectNode checkDeliverOrder(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean deliver = AuthorityUtil.hasAuthority(emp, AuthorityType.DELIVER_ORDER);
		boolean leafmsg = AuthorityUtil.hasAuthority(emp, AuthorityType.LEAVE_MESSAGE);
		return mapper.createObjectNode()
					 .put("deliver", deliver)
					 .put("leafmsg", leafmsg);
	}
	
	/**
	 * 验证确认收货权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/receive.do")
	public @ResponseBody ObjectNode checkReceiveOrder(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean receive = AuthorityUtil.hasAuthority(emp, AuthorityType.RECEIVE_ORDER);
		return mapper.createObjectNode()
					 .put("receive", receive);
	}
	
	/**
	 * 验证删除订单恢复权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/reorder.do")
	public @ResponseBody ObjectNode checkReOrder(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean reorder = AuthorityUtil.hasAuthority(emp, AuthorityType.RESET_ORDER);
		boolean leafmsg = AuthorityUtil.hasAuthority(emp, AuthorityType.LEAVE_MESSAGE);
		return mapper.createObjectNode()
					 .put("reorder", reorder)
					 .put("leafmsg", leafmsg);
	}
	
	/**
	 * 验证修改支付方式权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/changepaymentway.do")
	public @ResponseBody ObjectNode checkChangePaymentway(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.CHANGE_PAYMENTWAY);
		return mapper.createObjectNode().put("conforder", permission);
	}
	
	/**
	 * 验证前台广告发布权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/webpicshow.do")
	public @ResponseBody ObjectNode checkWebAdd(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_WEB_ADDS);
		return mapper.createObjectNode().put("webpicshow", permission);
	}
	
	/**
	 * 验证书籍管理权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/book.do")
	public @ResponseBody ObjectNode checkBook(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_BOOKS);
		return mapper.createObjectNode().put("book", permission);
	}
	
	/**
	 * 验证删除书籍管理权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/delbook.do")
	public @ResponseBody ObjectNode checkDelBook(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_DELETED_BOOKS);
		return mapper.createObjectNode().put("delbook", permission);
	}
	
	/**
	 * 验证出版社管理权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/press.do")
	public @ResponseBody ObjectNode checkPress(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_PRESS);
		return mapper.createObjectNode().put("press", permission);
	}
	
	/**
	 * 验证书籍类型管理权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/booktype.do")
	public @ResponseBody ObjectNode checkBookType(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_BOOKTYPE);
		return mapper.createObjectNode().put("booktype", permission);
	}
	
	/**
	 * 验证菜单排序权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/menusort.do")
	public @ResponseBody ObjectNode checkMenuSort(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_MENU_SORT);
		return mapper.createObjectNode().put("menusort", permission);
	}
	
	/**
	 * 验证用户管理权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/user.do")
	public @ResponseBody ObjectNode checkCustomer(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_CUSTOMER);
		return mapper.createObjectNode().put("user", permission);
	}
	
	/**
	 * 验证已删除用户管理权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/deluser.do")
	public @ResponseBody ObjectNode checkDelCustomer(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_DELCUSTOMER);
		return mapper.createObjectNode().put("deluser", permission);
	}
	
	/**
	 * 验证用户评论权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/comment.do")
	public @ResponseBody ObjectNode checkComment(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_COMMENTS);
		return mapper.createObjectNode().put("comment", permission);
	}
	
	/**
	 * 验证职工管理权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/employee.do")
	public @ResponseBody ObjectNode checkEmployee(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_EMPLOYEE);
		return mapper.createObjectNode().put("emp", permission);
	}
	
	/**
	 * 验证已删除职工管理权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/delemployee.do")
	public @ResponseBody ObjectNode checkDelEmployee(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		boolean permission = AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_DELETED_EMPLOYEE);
		return mapper.createObjectNode().put("delemp", permission);
	}

	/**
	 * 验证权限组管理权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/check/permission.do")
	public @ResponseBody ObjectNode checkPermission(HttpServletRequest request) {
		User emp = (User) request.getSession().getAttribute("employee");
		//if(emp.getUserType() == User.ADMINISTRATOR) return mapper.createObjectNode().put("permission", true);
		if(AuthorityUtil.hasAuthority(emp, AuthorityType.MODIFY_AUTHORITY)) {
			return mapper.createObjectNode().put("permission", true);
		}
		return mapper.createObjectNode().put("permission", false);
	}
}
