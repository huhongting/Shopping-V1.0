package cn.edu.jnu.web.controller;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.service.BookService;
import cn.edu.jnu.web.service.BookTypeService;
import cn.edu.jnu.web.service.PressService;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.UploadUtil;
import cn.edu.jnu.web.util.UserUtil;

/**
 * �鼮������
 * @author HHT
 *
 */
@Controller
public class BookController {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private BookService bookService;
	private BookTypeService bookTypeService;
	private PressService pressService;
	
	/**
	 * ������鼮
	 * @param bookName
	 * @param bookType
	 * @param press
	 * @param auther
	 * @param pubTime
	 * @param price
	 * @param normalPrice
	 * @param saleInfo
	 * @param pic
	 * @param remark
	 * @param note
	 * @param number
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/control/admin/book.do", params="method=add")
	public @ResponseBody ObjectNode handlerAdd(@RequestParam String bookName, @RequestParam String bookType,
			@RequestParam String press, @RequestParam String auther, @RequestParam String pubTime,
			@RequestParam double price, @RequestParam double normalPrice, @RequestParam String saleInfo,
			@RequestParam CommonsMultipartFile pic, @RequestParam String remark, @RequestParam String note,
			@RequestParam int number, HttpServletRequest request) {
		try {
			// ���ͼƬ��ʽjpg/jpeg/png/gif/bmp/ico
			String[] urls = UploadUtil.uploadPicture(request, pic, 
					"/WEB-INF/resources/upload/books/cover", 
					"/resources/upload/books/cover/", "ncover_");
			
			if(urls == null) {
				return mapper.createObjectNode()
							 .put("success", false)
							 .put("msg", "����ͼƬ��ʽ����ȷ��");
			}
			// ����鼮���ƻ�������Ϊ�գ�����ʧ�ܡ�
			if(!checkInput(bookName, auther)) {
				return mapper.createObjectNode()
							 .put("success", false)
							 .put("msg", "�鼮���ʧ�ܣ�");
			}
			Book b = bookService.findBook(bookName.trim());
			if(b == null) {
				Book book = new Book();
				book.setBookName(bookName.trim());
				book.setAuther(auther.trim());
				book.setBookType(bookTypeService.findBookType(bookType.trim()));
				book.setPress(pressService.findPress(Integer.valueOf(press.trim())));
				book.setPubTime(pubTime);
				book.setPrice(price);
				book.setNormalPrice(normalPrice);
				book.setNumber(number);
				book.setSaleInfo(saleInfo.trim().replace("����Ϊ��", ""));
				book.setNote(note.trim().replace("����Ϊ��", ""));
				book.setRemark(remark.trim().replace("����Ϊ��", ""));
				book.setPicUrl(urls[0]);
				bookService.saveBook(book);
				return mapper.createObjectNode().put("success", true);
			} else {
				return mapper.createObjectNode().put("success", false);
			}
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			return mapper.createObjectNode()
					 	 .put("success", false)
					 	 .put("msg", "�鼮���ʧ�ܣ�");
		}
		
	}
	// ���ͻ�������
	private boolean checkInput(String bookName, String auther) {
		if(bookName.trim().equals("") || auther.trim().equals("")) return false;
		return true;
	}

	/**
	 * ����ͼ���Ƿ��Ƽ�
	 * @param ids
	 * @param recom
	 * @return
	 */
	@RequestMapping(value="/control/admin/book.do", params="method=recom")
	public @ResponseBody ObjectNode handlerRecommend(@RequestParam int[] ids, boolean recom) {
		for(int i=0; i<ids.length; i++) {
			Book book = bookService.findBook(ids[i]);
			book.setRecom(recom);
			bookService.updateBook(book);
		}
		return mapper.createObjectNode().put("success", true);
	}
	/**
	 * ����ͼ���ϼܺ��¼�
	 * @param ids
	 * @param down
	 * @return
	 */
	@RequestMapping(value="/control/admin/book.do", params="method=updown")
	public @ResponseBody ObjectNode handlerDownTime(@RequestParam int[] ids, 
			@RequestParam boolean down) {
		if(down == true) {
			for(int i=0; i<ids.length; i++) {
				Book book = bookService.findBook(ids[i]);
				book.setDownTime(new Date());
				bookService.updateBook(book);
			}
		} else {
			for(int i=0; i<ids.length; i++) {
				Book book = bookService.findBook(ids[i]);
				book.setDownTime(null);
				book.setUpTime(new Date());
				bookService.updateBook(book);
			}
		}
		return mapper.createObjectNode().put("success", true);
	}

	/**
	 * �鼮ɾ��
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/control/admin/book.do", params="method=del")
	public @ResponseBody ObjectNode handlerDel(@RequestParam int[] id) {
		bookService.deleteBook(id);
		return mapper.createObjectNode().put("success", true);
	}
	@RequestMapping(value="/control/admin/book.do", params="method=reset")
	public @ResponseBody ObjectNode handlerReset(@RequestParam int[] id) {
		if(id == null || id.length == 0) 
			return mapper.createObjectNode().put("success", false);
		for(int i : id) {
			Book b = bookService.findBook(i);
			b.setDelFlag(false);
			bookService.updateBook(b);
		}
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * �鼮����
	 * @param bookName
	 * @param bookType
	 * @param press
	 * @param auther
	 * @param pubTime
	 * @param price
	 * @param normalPrice
	 * @param saleInfo
	 * @param delFlag
	 * @param remark
	 * @param note
	 * @param number
	 * @param bookId
	 * @return
	 */
	@RequestMapping(value="/control/admin/book.do", params="method=update")
	public @ResponseBody ObjectNode handlerUpdate(@RequestParam String bookName, @RequestParam String bookType,
			@RequestParam String press, @RequestParam String auther, @RequestParam String pubTime,
			@RequestParam double price, @RequestParam double normalPrice, @RequestParam String saleInfo,
			@RequestParam boolean delFlag, @RequestParam String remark, @RequestParam String note,
			@RequestParam int number, @RequestParam int bookId) {
		if(!checkInput(bookName, auther)) {
			return mapper.createObjectNode().put("success", false);
		}
		Book b = bookService.findBook(bookId);
		if(b == null) {// ������ݿ��в����ڸ��飬�����ʧ�ܡ�
			return mapper.createObjectNode().put("success", false); 
		}
		b.setBookName(bookName.trim());
		b.setBookType(bookTypeService.findBookType(bookType.trim()));
		b.setPress(pressService.findPress(press.trim()));
		b.setAuther(auther.trim());
		b.setPubTime(pubTime.trim());
		b.setPrice(price);
		b.setNormalPrice(normalPrice);
		b.setSaleInfo(saleInfo.replace("����Ϊ��", ""));
		b.setNote(note.replace("����Ϊ��", ""));
		b.setRemark(remark.replace("����Ϊ��", ""));
		b.setNumber(number);
		b.setDelFlag(delFlag);
		bookService.updateBook(b);
		
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * ��ʾ�鼮�б�
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/control/admin/book.do", params="method=list")
	public @ResponseBody ObjectNode handlerQuery(@RequestParam String start, @RequestParam String limit,
			HttpServletRequest request) {
		String query = UserUtil.getQueryString(request, "query");
		String[] ands = new String[] {"delflag=false"};
		if(query == null) {
			return encodeJSON(bookService.list(start, limit, ands));
		} else {
			List<String> keys = UserUtil.getQueryKeys(query);
			String[] ors = UserUtil.createSearchQuerySQL(keys, new String[]{"bookname", "auther"});
			return encodeJSON(bookService.list(start, limit, ands, ors));
		}
	}
	
	@RequestMapping(value="/control/admin/book.do", params="method=listdel")
	public @ResponseBody ObjectNode handlerQueryDel(@RequestParam String start, 
			@RequestParam String limit, HttpServletRequest request) {
		String query = UserUtil.getQueryString(request, "query");
		String[] ands = new String[] {"delflag=true"};
		if(query == null) {
			return encodeJSON(bookService.list(start, limit, ands));
		} else {
			List<String> keys = UserUtil.getQueryKeys(query);
			String[] ors = UserUtil.createSearchQuerySQL(keys, new String[]{"bookname", "auther"});
			return encodeJSON(bookService.list(start, limit, ands, ors));
		}
	}
	
	/**
	 * ���鼮��ѯ���ת����JSON��ʽ���Է��ؿͻ��ˡ�
	 * @param list
	 * @return
	 */
	public ObjectNode encodeJSON(QueryResult<Book> list) {
		ObjectNode json = mapper.createObjectNode();
		ArrayNode records = mapper.createArrayNode();
		json.put("count", list.getTotal());
		Iterator<Book> it = list.getIterator();
		while(it.hasNext()) {
			ObjectNode node = mapper.createObjectNode();
			Book book = it.next();
			node.put("bookid", book.getId());
			node.put("bookname", book.getBookName());
			node.put("saleinfo", book.getSaleInfo());
			node.put("auther", book.getAuther());
			node.put("pubtime", book.getPubTime());
			node.put("note", book.getNote());
			node.put("price", book.getPrice());
			node.put("normalprice", book.getNormalPrice());
			node.put("salecount", book.getSaleCount());
			node.put("remark", book.getRemark());
			node.put("press", book.getPress().getName());
			node.put("booktype", book.getBookType().getTypeName());
			node.put("delflag", book.getDelFlag());
			node.put("number", book.getNumber());
			node.put("recom", book.getRecom());
			node.put("uptime", book.getUpTime().toString());
			if(book.getDownTime() == null) node.put("downtime", "null");
			else node.put("downtime", book.getDownTime().toString());
			
			records.add(node);
		}
		json.put("records", records);
		return json;
	}

	public BookService getBookService() {
		return bookService;
	}

	@Resource(name="bookServiceImpl")
	public void setBookService(BookService BookService) {
		this.bookService = BookService;
	}

	public BookTypeService getBookTypeService() {
		return bookTypeService;
	}
	
	@Resource(name="bookTypeServiceImpl")
	public void setBookTypeService(BookTypeService bookTypeService) {
		this.bookTypeService = bookTypeService;
	}

	public PressService getPressService() {
		return pressService;
	}

	@Resource(name="pressServiceImpl")
	public void setPressService(PressService pressService) {
		this.pressService = pressService;
	}
}
