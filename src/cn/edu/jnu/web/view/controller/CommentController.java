package cn.edu.jnu.web.view.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.edu.jnu.web.entity.order.Comment;
import cn.edu.jnu.web.service.CommentService;
import cn.edu.jnu.web.util.ArrayNode;
import cn.edu.jnu.web.util.ObjectNode;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.XMLUtil;

@Controller
public class CommentController {
	private CommentService commentService;
	
	@RequestMapping(value="/view/comment/list")
	public String listComments(HttpServletRequest request, @RequestParam int start,
			@RequestParam int limit, @RequestParam int bid) {
		String[] ands = new String[]{
			"book.id=" + bid,
			"delflag=false"
		};
		QueryResult<Comment> comments = commentService.listComments(start, limit, ands);
		request.setAttribute("comments", comments);
		
		return "/pages/product/comments";
	}
	
	@RequestMapping(value="/view/comment/update")
	public void handlerUsefull(HttpServletResponse response, @RequestParam int cid,
			@RequestParam boolean usefull) throws IOException {
		Comment comm = commentService.findComment(cid);
		ArrayNode root = XMLUtil.createArrayNode();
		ObjectNode node1 = XMLUtil.createObjectNode();
		ObjectNode node2 = XMLUtil.createObjectNode();
		if(comm != null) {
			if(usefull == true) {
				comm.setUsefull(comm.getUsefull() + 1);
				node2.put("usefull", comm.getUsefull());
			} else {
				comm.setUseless(comm.getUseless() + 1);
				node2.put("usefull", comm.getUseless());
			}
			commentService.updateComment(comm);
			node1.put("success", true);
			root.put(node2);
		} else node1.put("success", false);
		
		root.put(node1);
		PrintWriter out = response.getWriter();
		out.println(root);
		out.flush();
		out.close();
	}

	public CommentService getCommentService() {
		return commentService;
	}
	@Resource(name="commentServiceImpl")
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}
}
