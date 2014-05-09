package cn.edu.jnu.web.controller;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.edu.jnu.web.entity.order.Comment;
import cn.edu.jnu.web.service.CommentService;
import cn.edu.jnu.web.util.QueryResult;

/**
 * 用户评论控制器
 * @author HHT
 *
 */
@Controller
public class CommentsController {
	private ObjectMapper mapper = new ObjectMapper();
	private CommentService commentService;

	/**
	 * 显示用户评论
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/control/admin/comment.do", params="method=list")
	public @ResponseBody ObjectNode handlerCommentList(@RequestParam int start,
			@RequestParam int limit) {
		QueryResult<Comment> comms = commentService.listComments(start, limit, null);
		return encoe2JSON(comms);
	}
	
	/**
	 * 删除用户评论，删除后只有评论者能看到，
	 * 其他用户无法看到
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/control/admin/comment.do", params="method=del")
	public @ResponseBody ObjectNode handlerDelComment(@RequestParam int[] id) {
		if(id == null || id.length == 0) return mapper.createObjectNode().put("success", false);
		commentService.deleteComments(id);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * 设置前台显示用户评论
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/control/admin/comment.do", params="method=show")
	public @ResponseBody ObjectNode handlerShowComment(@RequestParam int[] id) {
		if(id == null || id.length == 0) return mapper.createObjectNode().put("success", false);
		for(int i : id) {
			Comment c = commentService.findComment(i);
			if(c != null) {
				c.setDelflag(false);
				commentService.updateComment(c);
			}
		}
		return mapper.createObjectNode().put("success", true);
	}
	
	private ObjectNode encoe2JSON(QueryResult<Comment> comms) {
		ObjectNode json = mapper.createObjectNode();
		json.put("count", comms.getTotal());
		ArrayNode records = mapper.createArrayNode();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for(Comment comm : comms.getResults()) {
			ObjectNode node = mapper.createObjectNode();
			node.put("cid", comm.getId());
			node.put("bookname", comm.getBook().getBookName());
			node.put("username", comm.getUser().getName());
			node.put("date", sdf.format(comm.getDate()));
			node.put("title", comm.getTitle());
			node.put("content", comm.getContent());
			node.put("usefull", comm.getUsefull());
			node.put("useless", comm.getUseless());
			node.put("delflag", comm.getDelflag());
			records.add(node);
		}
		
		json.put("records", records);
		return json;
	}

	public CommentService getCommentService() {
		return commentService;
	}
	@Resource(name="commentServiceImpl")
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}
}
