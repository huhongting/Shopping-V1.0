package cn.edu.jnu.web.util;

import javax.servlet.http.HttpServletRequest;

import cn.edu.jnu.web.view.dao.TypeBookList;

/**
 * ��ҳ������
 * @author HHT
 *
 */
public class PagingUtil {
	/**
	 * ���ݲ�ѯ������ɷ�ҳ����<br>
	 * total ��ҳ��<br>
	 * limit ÿҳ��ʾ��<br>
	 * start ��ʼ����λ��<br>
	 * totalPage ��ҳ��<br>
	 * id ����id<br>
	 * @param req ҳ��request����
	 * @param attr ��ѯ�������������
	 * @param param �����������
	 */
	public static void createPagingData(
			HttpServletRequest req, String attr, String param) {
		TypeBookList tbl = (TypeBookList) req.getAttribute(attr);
		int total = tbl.getBooks().getTotal();// ��������
		int limit = Integer.valueOf(req.getParameter("limit"));// ÿҳ��ʾ��
		int totalPage = total % limit == 0 ? total/limit : (total/limit + 1);// ��ҳ��
		int cur = Integer.valueOf(req.getParameter("start")) / limit + 1;// ��ǰҳ
		cur = cur > totalPage ? totalPage : cur;
		int pre = (cur - 2) * limit;// ��ʼ����λ��
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
	 * ���ݲ�ѯ������ɷ�ҳ����<br>
	 * total ��ҳ��<br>
	 * limit ÿҳ��ʾ��<br>
	 * start ��ʼ����λ��<br>
	 * totalPage ��ҳ��<br>
	 * id ����id<br>
	 * @param req ҳ��request����
	 * @param attr ��ѯ�������������
	 */
	public static void getPagingData(
			HttpServletRequest req, String attr) {
		QueryResult<?> res = (QueryResult<?>) req.getAttribute(attr);
		int total = res.getTotal();// ��������
		int limit = Integer.valueOf(req.getParameter("limit"));// ÿҳ��ʾ��
		int totalPage = total % limit == 0 ? total/limit : (total/limit + 1);// ��ҳ��
		int cur = Integer.valueOf(req.getParameter("start")) / limit + 1;// ��ǰҳ
		cur = cur > totalPage ? totalPage : cur;
		int pre = (cur - 2) * limit;// ��ʼ����λ��
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
