package cn.edu.jnu.web.service;

import cn.edu.jnu.web.entity.order.Comment;
import cn.edu.jnu.web.util.QueryResult;

public interface CommentService {
	void saveComment(Comment comm);
	void updateComment(Comment comm);
	void deleteComment(int id);
	void deleteComments(int[] ids);
	Comment findComment(int id);
	QueryResult<Comment> listComments(int start, int limit, String[] ands);
}
