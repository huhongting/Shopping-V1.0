package cn.edu.jnu.web.util;

import javax.servlet.http.HttpServletRequest;

import cn.edu.jnu.web.view.dao.TypeBookList;

/**
 * 分页工具类
 * @author HHT
 *
 */
public class PagingUtil {
	/**
	 * 根据查询结果生成分页数据<br>
	 * total 总页数<br>
	 * limit 每页显示数<br>
	 * start 起始数据位置<br>
	 * totalPage 总页数<br>
	 * id 分类id<br>
	 * @param req 页面request对象
	 * @param attr 查询结果集变量名称
	 * @param param 分类参数名称
	 */
	public static void createPagingData(
			HttpServletRequest req, String attr, String param) {
		TypeBookList tbl = (TypeBookList) req.getAttribute(attr);
		int total = tbl.getBooks().getTotal();// 总数据数
		int limit = Integer.valueOf(req.getParameter("limit"));// 每页显示数
		int totalPage = total % limit == 0 ? total/limit : (total/limit + 1);// 总页数
		int cur = Integer.valueOf(req.getParameter("start")) / limit + 1;// 当前页
		cur = cur > totalPage ? totalPage : cur;
		int pre = (cur - 2) * limit;// 起始数据位置
		pre = pre < 0 ? 0 : pre;
		int next = cur * limit;
		
		String id = req.getParameter(param);
		
		req.setAttribute("total", total);
		req.setAttribute("pre", pre);
		req.setAttribute("next", next);
		req.setAttribute("limit", limit);
		req.setAttribute("cur", cur);
		req.setAttribute("totalPage", totalPage);
		req.setAttribute("id", id);
	}
	
	/**
	 * 根据查询结果生成分页数据<br>
	 * total 总页数<br>
	 * limit 每页显示数<br>
	 * start 起始数据位置<br>
	 * totalPage 总页数<br>
	 * id 分类id<br>
	 * @param req 页面request对象
	 * @param attr 查询结果集变量名称
	 */
	public static void getPagingData(
			HttpServletRequest req, String attr) {
		QueryResult<?> res = (QueryResult<?>) req.getAttribute(attr);
		int total = res.getTotal();// 总数据数
		int limit = Integer.valueOf(req.getParameter("limit"));// 每页显示数
		int totalPage = total % limit == 0 ? total/limit : (total/limit + 1);// 总页数
		int cur = Integer.valueOf(req.getParameter("start")) / limit + 1;// 当前页
		cur = cur > totalPage ? totalPage : cur;
		int pre = (cur - 2) * limit;// 起始数据位置
		pre = pre < 0 ? 0 : pre;
		int next = cur * limit;
		
		req.setAttribute("total", total);
		req.setAttribute("pre", pre);
		req.setAttribute("next", next);
		req.setAttribute("limit", limit);
		req.setAttribute("cur", cur);
		req.setAttribute("totalPage", totalPage);
	}
}
