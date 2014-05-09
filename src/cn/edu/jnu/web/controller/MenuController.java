package cn.edu.jnu.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.edu.jnu.web.entity.menu.Node;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.NodeService;
import cn.edu.jnu.web.service.UserService;

/**
 * ���ܲ˵�������
 * @author HHT
 *
 */
@Controller
public class MenuController {
	private NodeService nodeService;
	private ObjectMapper mapper = new ObjectMapper();
	
	private UserService userService;
	
	/**
	 * ��ѯ���ù��ܲ˵�
	 * @param node
	 * @return
	 */
	@RequestMapping(value="/control/admin/view.do", params="method=menu")
	public @ResponseBody List<ObjectNode> handelMenu(@RequestParam String node) {
		if(node.equals("root")) return list2Json(nodeService.findNodes(null).getResults());
		else return list2Json(nodeService.findNodes(node).getResults());
	}

	/**
	 * ����ѯ���ת����JSON��ʽ�����ͻ����������ι��ܲ˵���
	 * @param nodes
	 * @return
	 */
	private List<ObjectNode> list2Json(List<Node> nodes) {
		List<ObjectNode> json = new ArrayList<ObjectNode>();
		for(Node n : nodes) {
			ObjectNode onode = mapper.createObjectNode();
			onode.put("id", n.getId());
			onode.put("text", n.getText());
			onode.put("leaf", n.getLeaf());
			json.add(onode);
		}
		return json;
	}
	
	/**
	 * ��ѯ��ǰ�����û�
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/view.do", params="method=user", method=RequestMethod.POST)
	public @ResponseBody ObjectNode getUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("employee");
		User emp = userService.findById(user.getId(), false);
		ObjectNode json = mapper.createObjectNode();
		if(emp == null) {// û�е����û�����ʧ�ܡ�
			json.put("success", false);
			return json;
		}
		request.getSession().setAttribute("employee", emp);
		json.put("success", true);
		json.put("name", emp.getName());
		json.put("info", emp.getContactInfo() == null ? false : true);
		
		return json;
	}

	public NodeService getNodeService() {
		return nodeService;
	}

	@Resource(name="nodeServiceImpl")
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public UserService getUserService() {
		return userService;
	}
	@Resource(name="userServiceImpl")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
