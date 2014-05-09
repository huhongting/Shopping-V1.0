package cn.edu.jnu.web.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.CommentDaoImpl;
import cn.edu.jnu.web.entity.order.Comment;
import cn.edu.jnu.web.service.CommentService;
import cn.edu.jnu.web.util.QueryResult;

@Service
public class CommentServiceImpl implements CommentService {
	
	private CommentDaoImpl commentDao;

	@Override
	public void saveComment(Comment comm) {
		commentDao.save(comm);
	}

	@Override
	public void deleteComment(int id) {
		commentDao.delete(Comment.class, id);
	}

	@Override
	public void deleteComments(int[] ids) {
		for(int id : ids) {
			deleteComment(id);
		}
	}

	@Override
	public Comment findComment(int id) {
		Comment comm = commentDao.find(Comment.class, id);
		return comm;
	}

	@Override
	public QueryResult<Comment> listComments(int start, int limit,
			String[] ands) {
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		sorts.put("date", "desc");
		List<Comment> comms = commentDao.pagingQuery(Comment.class, start, limit, ands, sorts);
		List<Comment> total = commentDao.pagingQuery(Comment.class, -1, -1, ands, null);
		QueryResult<Comment> res = new QueryResult<Comment>();
		res.setResults(comms);
		res.setTotal(total.size());
		return res;
	}
	

	@Override
	public void updateComment(Comment comm) {
		commentDao.update(comm);
	}

	public CommentDaoImpl getCommentDao() {
		return commentDao;
	}
	@Resource(name="commentDaoImpl")
	public void setCommentDao(CommentDaoImpl commentDao) {
		this.commentDao = commentDao;
	}

}
