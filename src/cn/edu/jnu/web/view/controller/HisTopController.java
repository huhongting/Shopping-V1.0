package cn.edu.jnu.web.view.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.entity.user.User;
import cn.edu.jnu.web.service.BookService;
import cn.edu.jnu.web.service.BookTypeService;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.WebUtil;

/**
 * �����ʷ�ͳ���ͼ�������
 * @author HHT
 *
 */
@Controller
public class HisTopController {
	
	private BookService bookService;
	private BookTypeService bookTypeService;
	
	/**
	 * ��ȡ���ͼ�飬����Ϊ10<br>
	 * ��ָ�����ͣ��򷵻�ָ�������µ����ͼ�飬���򷵻����������µ����ͼ��
	 * @param request
	 * @param tid
	 * @return
	 */
	@RequestMapping(value="/view/product/changxiao")
	public String handlerChangXiao(HttpServletRequest request, 
			@RequestParam int tid) {
		BookType type = bookTypeService.findBookType(tid);
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		/*
		 * ���ͼ������Ƽ�����������
		 * ������ʾ���Ƽ���������õ�ͼ��
		 */
		sorts.put("recom", "desc");
		sorts.put("salecount", "desc");
		QueryResult<Book> books = bookService.listBooks(null, sorts, 0, 10, type);
		request.setAttribute("books", books.getResults());
		return "/pages/product/tlcx";
	}
	
	/**
	 * ��ȡ�û���Ʒ�������ʷ
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/product/{history}")
	public String handlerHistoryView(HttpServletRequest request,
			@PathVariable String history) {
		User user = (User) request.getSession().getAttribute("user");
		List<Book> hisBooks = null;
		String his = null;

		if(user == null) {
			his = (String) request.getSession().getAttribute("viewhis.ishare");
		} else {
			his = WebUtil.getCookie(request, user.getId() + ".viewhis.ishare");
		}
		if(his != null && !his.isEmpty()) {
			hisBooks = new ArrayList<Book>();
			String[] ids = his.split("-");
			for(int i=ids.length-1; i>=0; i--) {// �û������ʷ������ʾ
				try {
					Book b = bookService.showBook(Integer.valueOf(ids[i]));
					if(b != null) hisBooks.add(b);
				} catch (NumberFormatException e) {
					//e.printStackTrace();
				}
			}
		}
		
		request.setAttribute("hisBooks", hisBooks);
		return "/pages/product/" + history;
	}
	
	/**
	 * ��ȡ�û������ʷ
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/product/history2")
	public String handlerHistoryView2(HttpServletRequest request) {
		String his = WebUtil.getCookie(request, "viewhis.ishare");
		List<Book> hisBooks = null;
		
		if(his != null) {
			hisBooks = new ArrayList<Book>();
			String[] ids = his.split("-");
			for(int i=ids.length-1; i>=0; i--) {
				Book b = bookService.findBook(Integer.valueOf(ids[i]));
				if(b != null) hisBooks.add(b);
			}
		}
		
		request.setAttribute("hisBooks", hisBooks);
		return "/pages/product/carhis2";
	}

	public BookService getBookService() {
		return bookService;
	}
	@Resource(name="bookServiceImpl")
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}

	public BookTypeService getBookTypeService() {
		return bookTypeService;
	}
	@Resource(name="bookTypeServiceImpl")
	public void setBookTypeService(BookTypeService bookTypeService) {
		this.bookTypeService = bookTypeService;
	}
}
