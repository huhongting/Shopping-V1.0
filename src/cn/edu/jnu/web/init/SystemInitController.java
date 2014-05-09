package cn.edu.jnu.web.init;

import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.entity.emp.Notification;
import cn.edu.jnu.web.entity.menu.Node;
import cn.edu.jnu.web.entity.permission.Group;
import cn.edu.jnu.web.entity.user.Account;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.NodeService;
import cn.edu.jnu.web.service.NotificationService;
import cn.edu.jnu.web.service.OrderService;
import cn.edu.jnu.web.service.UserService;
import cn.edu.jnu.web.service.permission.GroupService;
import cn.edu.jnu.web.util.PasswordUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * ��ʼ��ϵͳ
 * @author HHT
 *
 */
@Controller
public class SystemInitController {
	
	private UserService userService;
	private GroupService groupService;
	private NodeService nodeService;
	private NotificationService notificationService;
	private OrderService orderService;
	
	@RequestMapping(value="/system/init")
	public String showInitView() {
		return "/pages/init/init";
	}
	
	@RequestMapping(value="/control/system/init.do")
	public @ResponseBody ObjectNode executeInit() {
		
		ObjectMapper mapper = new ObjectMapper();

		try {
			// ��ʼ��ϵͳ�˻�&Ȩ����
			initEmployeeAndGroup();
			
			// ��ʼ����̨�˵�
			initMenu();

			// ��ʼ��ϵͳ֪ͨ
			initNotes();
		} catch (Exception e) {
			e.printStackTrace();
			return mapper.createObjectNode().put("success", false);
		}
		
		return mapper.createObjectNode().put("success", true);
	}
	
	private void initNotes() throws Exception {
		Notification note = new Notification();
		note.setContent("��ӭ����IShare.com<br>ף��ҹ�����죡");
		note.setTitle("��ӭ����IShare.com");
		note.setStatus(Notification.NOTIFICATIONTYPE_IMPORTANT);
		note.setCreater(userService.findByName("admin.ishare.com"));
		notificationService.AddNotification(note);
	}

	private void initEmployeeAndGroup() throws NoSuchAlgorithmException {
		Group group = new Group();
		group.setAuthority("111111111111111111111111");
		group.setCreater(null);
		group.setName("��������Ա");
		group.setRemark("ϵͳ��������Ա��ӵ��ϵͳ���в���Ȩ��");
		groupService.saveGroup(group);
		
		User admin = new User();
		admin.setAccount(new Account());
		admin.setEmail("sh.huht@gmail.com");
		admin.setName("admin.ishare.com");
		admin.setNote("Ĭ��ϵͳ����Ա");
		admin.setPassword(PasswordUtil.getEncodeString("ishare.com.admin"));
		admin.setUserType(User.ADMINISTRATOR);
		admin.setGroup(group);
		userService.saveUser(admin);
		
		User test = new User();
		test.setAccount(new Account());
		test.setEmail("2318231678@qq.com");
		test.setName("test");
		test.setNote("Ĭ�������ʺ�");
		test.setPassword(PasswordUtil.getEncodeString("test00"));
		test.setUserType(User.EMPLOYEE);
		test.setGroup(null);
		userService.saveUser(test);
		
		group.setCreater(admin);
		groupService.updateGroup(group);
	}

	private void initMenu() throws Exception {
		Node node = new Node("��������", false, null, null);
		nodeService.saveNode(node);
		Node cnode = new Node("������ѯ", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("�ȴ���˶���", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("�ȴ������", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("�ȴ��������", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("�ȴ���������", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("�ѷ�������", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("���ջ�����", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("��ȡ������", true, node, null);
		nodeService.saveNode(cnode);
		
		node = new Node("�鼮����", false, null, null);
		nodeService.saveNode(node);
		cnode = new Node("�鼮����", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("�鼮������", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("�˵�����", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("���������", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("��ɾ���鼮����", true, node, null);
		nodeService.saveNode(cnode);
		
		node = new Node("ǰ̨����", false, null, null);
		nodeService.saveNode(node);
		cnode = new Node("������", true, node, null);
		nodeService.saveNode(cnode);
		
		node = new Node("�û�����", false, null, null);
		nodeService.saveNode(node);
		cnode = new Node("�û�����", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("�ѽ����û�����", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("���۹���", true, node, null);
		nodeService.saveNode(cnode);
		
		node = new Node("Ա������", false, null, null);
		nodeService.saveNode(node);
		cnode = new Node("Ա����ѯ", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("��ְԱ������", true, node, null);
		nodeService.saveNode(cnode);
		cnode = new Node("��ְԱ������", true, node, null);
		nodeService.saveNode(cnode);
		
		node = new Node("Ȩ�޹���", false, null, null);
		nodeService.saveNode(node);
		cnode = new Node("Ȩ�������", true, node, null);
		nodeService.saveNode(cnode);
	}

	public UserService getUserService() {
		return userService;
	}
	@Resource(name="userServiceImpl")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public GroupService getGroupService() {
		return groupService;
	}
	@Resource(name="groupServiceImpl")
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public NodeService getNodeService() {
		return nodeService;
	}
	@Resource(name="nodeServiceImpl")
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public NotificationService getNotificationService() {
		return notificationService;
	}
	@Resource(name="notificationServiceImpl")
	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	public OrderService getOrderService() {
		return orderService;
	}
	@Resource(name="orderServiceImpl")
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
}