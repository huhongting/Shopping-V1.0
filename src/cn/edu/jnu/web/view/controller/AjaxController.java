package cn.edu.jnu.web.view.controller;

import java.util.LinkedHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.service.BookService;
import cn.edu.jnu.web.service.BookTypeService;
import cn.edu.jnu.web.util.UserUtil;
import cn.edu.jnu.web.view.dao.TypeBookList;

@Controller
public class AjaxController {
	private BookTypeService bookTypeService;
	private BookService bookService;
	
	@RequestMapping(value="/view/ajax/type/list")
	public String handlerListType(@RequestParam int type, 
			@RequestParam int start, @RequestParam int limit, 
			HttpServletRequest request) {
		String sort = UserUtil.getQueryString(request, "sort");
		LinkedHashMap<String, String> sorts = createSorts(sort);
		// 获取图书类型
		BookType bt = bookTypeService.findBookType(type);
		TypeBookList tbl = new TypeBookList();
		tbl.setType(bt);
		//tbl.setBooks(bookService.listTypeBooks(start, limit, bt));
		tbl.setBooks(bookService.listBooks(null, sorts, start, limit, bt));
		request.setAttribute("typeBooks", tbl);
		
		return "/pages/product/products";
	}
	
	@RequestMapping(value="/view/ajax/tejia/type/list")
	public String handlerTeJiaList(@RequestParam int type, HttpServletRequest request,
			@RequestParam int start, @RequestParam int limit) {
		TypeBookList tjBooks = new TypeBookList();
		BookType bt = bookTypeService.findBookType(type);// 获取图书类型
		tjBooks.setType(bt);
		String[] ands = new String[]{
				"delflag=false", 
				"downtime=null", 
				"normalprice > 1.5*price"
			};
		String sort = UserUtil.getQueryString(request, "sort");
		LinkedHashMap<String, String> sorts = createSorts(sort);
		tjBooks.setBooks(bookService.listTeJia(start, limit, bt, ands, sorts));
		request.setAttribute("typeBooks", tjBooks);
		
		return "/pages/product/tjproducts";
	}

	private LinkedHashMap<String, String> createSorts(String sort) {
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		if(sort != null && !sort.equals("")) {
			if(sort.equals("pri_up")) {
				sorts.put("price", "asc");
			} else if(sort.equals("pri_down")) {
				sorts.put("price", "desc");
			} else if(sort.equals("pub_up")) {
				sorts.put("pubtime", "asc");
			} else if(sort.equals("pub_down")) {
				sorts.put("pubtime", "desc");
			} else if(sort.equals("upt_up")) {
				sorts.put("uptime", "asc");
			} else if(sort.equals("upt_down")) {
				sorts.put("uptime", "desc");
			} else if(sort.equals("hot_up")) {
				sorts.put("salecount", "asc");
			} else if(sort.equals("hot_down")) {
				sorts.put("salecount", "desc");
			}
			return sorts;
		}
		return null;
	}

	public BookTypeService getBookTypeService() {
		return bookTypeService;
	}
	@Resource(name="bookTypeServiceImpl")
	public void setBookTypeService(BookTypeService bookTypeService) {
		this.bookTypeService = bookTypeService;
	}

	public BookService getBookService() {
		return bookService;
	}
	@Resource(name="bookServiceImpl")
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}
}
