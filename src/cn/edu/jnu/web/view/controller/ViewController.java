package cn.edu.jnu.web.view.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.entity.Picture;
import cn.edu.jnu.web.entity.order.Comment;
import cn.edu.jnu.web.service.BookService;
import cn.edu.jnu.web.service.BookTypeService;
import cn.edu.jnu.web.service.CommentService;
import cn.edu.jnu.web.service.PictureService;
import cn.edu.jnu.web.util.QueryResult;
import cn.edu.jnu.web.util.UserUtil;
import cn.edu.jnu.web.util.WebUtil;
import cn.edu.jnu.web.view.dao.MenuList;
import cn.edu.jnu.web.view.dao.TypeBookList;

/**
 * ǰ̨��ʾ������<br>
 * ���ܣ�<br>
 * 1.��ҳ��ʾ<br>
 * 2.
 * @author HHT
 *
 */
@Controller
public class ViewController {
	private BookTypeService bookTypeService;
	private BookService bookService;
	private PictureService pictureService;
	private CommentService commentService;
	
	/**
	 * ��ȡ�˵�������<br>
	 * ���ModelAttributeע�⣬ִ�����д˿���������֮ǰ��ִ�д˷�����
	 * @param request
	 */
	@ModelAttribute
	protected void queryMenu(HttpServletRequest request) {
		if(request.getSession().getAttribute("menuList") != null) {
			return;
		}
		
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		sorts.put("sortid", "asc");
		sorts.put("typename", "asc");
		String[] ands = new String[]{"parentid=null","deleted=false"};
		QueryResult<BookType> res = bookTypeService.list("-1", "-1", ands, sorts);// ���˵�
		List<MenuList> menu = new LinkedList<MenuList>();
		for(BookType bt : res.getResults()) {
			MenuList ml = new MenuList();
			ml.setRootType(bt);
			ml.setMenu(bt);// ����Ӳ˵�
			menu.add(ml);
		}
		request.getSession().setAttribute("menuList", menu);
	}
	
	/**
	 * ��ҳ���ݳ�ʼ��
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/index")
	public String handlerIndexView(HttpServletRequest request) {
		queryPicture(request);// ��ѯ���ͼƬ
		queryNewRecommend(request);// �����Ƽ�
		queryCuXiao(request);// ����ͼ��
		queryTeJia(request);// �ؼ�ͼ��
		queryFirstBooks(request);// ���һͼ��
		querySecondBooks(request);// ����ͼ��
		return "/pages/product/default";
	}

	private void queryPicture(HttpServletRequest request) {
		QueryResult<Picture> pics = pictureService.listPictures(0, 6, new String[]{"isshow=true"});
		request.setAttribute("showpics", pics);
	}

	/**
	 * ��ѯ���������ϼ�ͼ������
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/new/list")
	public String handlerNewList(HttpServletRequest request) {
		// ��ȡͼ�����͵ĸ�����
		QueryResult<BookType> types = bookTypeService.listRoots("-1", "-1", false);
		
		// ��ȡÿ�������͵���ͼ��
		List<TypeBookList> list = new LinkedList<TypeBookList>();
		for(BookType type : types.getResults()) {
			TypeBookList tbl = new TypeBookList();
			tbl.setType(type);
			tbl.setBooks(bookService.listTypeBooks(0, 12, type));
			list.add(tbl);
		}
		request.setAttribute("newBooks", list);
		
		return "/pages/product/newlist";
	}
	
	/**
	 * ��ѯ�������ض�����ͼ������
	 * @param type
	 * @param start
	 * @param limit
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/type/list")
	public String handlerTypeList(HttpServletRequest request, @RequestParam int type) {
		// ��ȡͼ������
		BookType bt = bookTypeService.findBookType(type);
		QueryResult<BookType> res = bookTypeService.list(bt.getTypeId());
		
		// ��ȡ����ͼ��
		TypeBookList tbl = new TypeBookList();
		tbl.setType(bt);
		tbl.setChildTypes(res.getResults());
		request.setAttribute("typeBooks", tbl);
		return "/pages/product/typelist";
	}
	
	/**
	 * ��ѯ�������ؼ�ͼ������
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/tejia/list")
	public String handlerTeJiaList(HttpServletRequest request) {
		// ��ȡͼ�����͵ĸ�����
		QueryResult<BookType> types = bookTypeService.listRoots("-1", "-1", false);
		
		// ��ȡÿ�������͵���ͼ��
		List<TypeBookList> list = new LinkedList<TypeBookList>();
		for(BookType type : types.getResults()) {
			TypeBookList tjBooks = new TypeBookList();
			tjBooks.setType(type);
			QueryResult<BookType> bts = bookTypeService.list(type.getTypeId());
			bts.getResults().add(type);
			String[] ands = new String[]{
					"delflag=false", 
					"downtime=null", 
					"normalprice > 1.5*price"
				};
			LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
			sorts.put("salecount", "desc");
			sorts.put("uptime", "desc");
			tjBooks.setBooks(bookService.listTeJia(0, 5, type, ands, sorts));
			list.add(tjBooks);
		}
		request.setAttribute("tjBooks", list);
		
		return "/pages/product/tjlist";
	}
	
	@RequestMapping(value="/view/tejia/list/s")
	public String handlerTeJiaList(HttpServletRequest request, @RequestParam int start,
			@RequestParam int limit, @RequestParam int type) {
		
		request.setAttribute("type", type);
		
		TypeBookList tjBooks = new TypeBookList();
		BookType bt = null;
		if(type != 0) {
			bt = bookTypeService.findBookType(type);// ��ȡͼ������
		}
		tjBooks.setType(bt);
		QueryResult<BookType> childTypes = null;// ��ȡͼ�����͵�����������
		if(bt == null) {
			childTypes = bookTypeService.listRoots("-1", "-1", false);
		} else {
			childTypes = bookTypeService.list(bt.getTypeId());
		}
		tjBooks.setChildTypes(childTypes.getResults());
		
		String[] ands = createQuerySQL(request);
		LinkedHashMap<String, String> sorts = createSorts(request);
		
		QueryResult<Book> list = bookService.listTeJia(start, limit, bt, ands, sorts);
		tjBooks.setBooks(list);
		request.setAttribute("typeBooks", tjBooks);
		
		return "/pages/product/typetjlist";
	}
	
	private LinkedHashMap<String, String> createSorts(HttpServletRequest request) {
		String sort = UserUtil.getQueryString(request, "sort");
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
	
	private String[] createQuerySQL(HttpServletRequest request) {
		List<String> ands = new ArrayList<String>();
		String jia1 = UserUtil.getQueryString(request, "jia1");
		String jia2 = UserUtil.getQueryString(request, "jia2");
		String key = UserUtil.getQueryString(request, "key");
		String cbs = UserUtil.getQueryString(request, "cbs");

		request.setAttribute("jia1", jia1);
		request.setAttribute("jia2", jia2);
		request.setAttribute("key", key);
		request.setAttribute("cbs", cbs);
		
		ands.add("o.delFlag=false"); 
		ands.add("downtime is null");
		ands.add("normalprice > 1.5*price");
		if(jia1 != null && !jia1.equals("")) {
			ands.add("price >= " + jia1);
		}
		if(jia2 != null && !jia2.equals("")) {
			ands.add("price <= " + jia2);
		}
		if(key != null && !key.equals("")) {
			List<String> keys = UserUtil.getQueryKeys(key);
			StringBuffer sql = new StringBuffer();
			sql.append("(");
			for(String k : keys) {
				sql.append("bookname like '%" + k + "%'");
				sql.append(" or ");
				sql.append("auther like '%" + k + "%'");
				sql.append(" or ");
				sql.append("bookType.typeName like '%" + k + "%'");
				sql.append(" or ");
			}
			sql.replace(sql.lastIndexOf(" or "), sql.length(), " ");
			sql.append(")");
			ands.add(sql.toString());
		}
		if(cbs != null && !cbs.equals("")) {
			ands.add("press.name like '%" + cbs + "%'");
		}
		
		return ands.toArray(new String[]{});
	}

	/**
	 * ��ѯ������ָ�����͵��ؼ�ͼ��
	 * @param start
	 * @param limit
	 * @param type
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/view/tejia/type/list")
	public String handlerTeJiaList(@RequestParam int type, HttpServletRequest request,
			@RequestParam int start, @RequestParam int limit) {
		TypeBookList tjBooks = new TypeBookList();
		BookType bt = bookTypeService.findBookType(type);// ��ȡͼ������
		tjBooks.setType(bt);
		QueryResult<BookType> childTypes = bookTypeService.list(bt.getTypeId());// ��ȡͼ�����͵�����������
		tjBooks.setChildTypes(childTypes.getResults());
		String[] ands = createQuerySQL(request);
		LinkedHashMap<String, String> sorts = createSorts(request);
		tjBooks.setBooks(bookService.listTeJia(start, limit, bt, ands, sorts));
		request.setAttribute("typeBooks", tjBooks);
		
		return "/pages/product/typetjlist";
	}
	
	/**
	 * ��ѯ����ͼ����������
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/view/book/show")
	public String handlerBookView(HttpServletRequest request, 
			HttpServletResponse response, @RequestParam int id) {
		Book book = bookService.showBook(id);
		request.setAttribute("bbook", book);
		QueryResult<Comment> total = commentService.listComments(-1, -1, 
				new String[]{"book.id=" + id, "delflag=false"});
		request.setAttribute("total", total.getTotal());
		
		WebUtil.addViewHistory(request, response, id);
		
		return "/pages/product/book";
	}
	
	protected static String[] SORTS = new String[] {
		"price desc", "price asc",// ���ݼ۸����� 1,2
		"uptime desc", "uptime asc",// �����ϼ�ʱ������ 3,4
		"pubtime desc", "pubtime asc",// ���ݳ���ʱ������ 5,6
		"salecount desc", "salecount asc"// ������������ 7,8
	};
	
	/**
	 * ����ͼ��
	 * @param request
	 * @param key �ո�Ͷ�����Ϊ�ؼ��ַָ���
	 * @param sorts
	 * @param type
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value="/view/search")
	public String handlerSearchView(HttpServletRequest request, 
			@RequestParam String key,  @RequestParam int sorts, 
			@RequestParam int type, @RequestParam int start, 
			@RequestParam int limit) {
		try {
			key = URLDecoder.decode(key, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		TypeBookList tbl = new TypeBookList();
		String[] keys = createAnds(key);
		QueryResult<BookType> res = null;
		
		if(type <= 0) {
			// ��ȡͼ�����ͼ�����������
			res = bookTypeService.listRoots("-1", "-1", false);
			
			// ��ȡ����ͼ��
			tbl.setBooks(bookService.listBooks(keys, createSort(sorts), start, limit, null));
			tbl.setChildTypes(res.getResults());
		} else {
			// ��ȡͼ�����ͼ�����������
			BookType bt = bookTypeService.findBookType(type);
			res = bookTypeService.list(bt.getTypeId());
			// ��ȡ����ͼ��
			tbl.setType(bt);
			tbl.setBooks(bookService.listBooks(keys, createSort(sorts), start, limit, bt));
			tbl.setChildTypes(res.getResults());
			request.setAttribute("type", bt);
		}
		
		request.setAttribute("sbooks", tbl);
		request.setAttribute("keys", key);
		
		return "/pages/product/search";
	}
	/**
	 * �����������
	 * @param _sort
	 * @return
	 */
	private LinkedHashMap<String, String> createSort(int _sort) {
		if(_sort < 1 || _sort > 8) return null;
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		String[] str = SORTS[_sort-1].split(" ");
		sorts.put(str[0], str[1]);
		return sorts;
	}
	/**
	 * ���ɹؼ��ֲ�ѯ���
	 * @param key
	 * @return
	 */
	private String[] createAnds(String key) {
		List<String> ands = new ArrayList<String>();
		List<String> keys = UserUtil.getQueryKeys(key);
		StringBuffer sql = new StringBuffer();
		sql.append("(");
		for(String s : keys) {
			sql.append("bookname like '%");
			sql.append(s);
			sql.append("%'");
			sql.append(" or ");
			sql.append("auther like '%");
			sql.append(s);
			sql.append("%'");
			sql.append(" or ");
			sql.append("press.name like '%");
			sql.append(s);
			sql.append("%'");
			sql.append(" or ");
			sql.append("bookType.typeName like '%");
			sql.append(s);
			sql.append("%'");
			sql.append(" or ");
		}
		sql = sql.replace(sql.lastIndexOf(" or "), sql.length(), "");
		sql.append(")");
		ands.add(sql.toString());
		
		return ands.toArray(new String[]{});
	}

	/**
	 * ��ѯ���������ͼ������
	 * @param request
	 */
	private void queryCuXiao(HttpServletRequest request) {
		QueryResult<Book> res = bookService.listCuXiao(0, 5);
		request.setAttribute("cxBooks", res.getResults());
	}

	/**
	 * ��ѯ�������ؼ�ͼ������
	 * @param request
	 */
	private void queryTeJia(HttpServletRequest request) {
		String[] ands = new String[]{
				"delflag=false", 
				"downtime=null", 
				"normalprice > 1.5*price"
			};
		QueryResult<Book> res = bookService.listTeJia(0, 5, ands);
		request.setAttribute("tjBooks", res.getResults());
	}

	/**
	 * ��ѯ�������������
	 * @param request
	 */
	private void querySecondBooks(HttpServletRequest request) {
		QueryResult<BookType> bts = bookTypeService.list("0", "1", new String[]{"sortId=2"});
		QueryResult<Book> books = null;
		if(bts == null || bts.getResults().isEmpty()) {
			request.setAttribute("slhBooks", null);
		} else {
			books = bookService.listTypeBooks(0, 12, bts.get(0));
			request.setAttribute("slhBooks", books.getResults());
			request.setAttribute("sbt", bts.get(0));
		}
	}

	/**
	 * ��ѯ��������һ����
	 * @param request
	 */
	private void queryFirstBooks(HttpServletRequest request) {
		QueryResult<BookType> bts = bookTypeService.list("0", "1", new String[]{"sortId=1"});
		QueryResult<Book> books = null;
		if(bts == null || bts.getResults().isEmpty()) {
			request.setAttribute("itBooks", null);
		} else {
			books = bookService.listTypeBooks(0, 12, bts.get(0));
			request.setAttribute("itBooks", books.getResults());
			request.setAttribute("fbt", bts.get(0));
		}
	}

	/**
	 * ��ѯ�����������Ƽ��������
	 * @param request
	 */
	private void queryNewRecommend(HttpServletRequest request) {
		List<Book> recoms = bookService.listNewRecommends(0, 5);
		request.setAttribute("newBooks", recoms);
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

	public PictureService getPictureService() {
		return pictureService;
	}
	@Resource(name="pictureServiceImpl")
	public void setPictureService(PictureService pictureService) {
		this.pictureService = pictureService;
	}

	public CommentService getCommentService() {
		return commentService;
	}
	@Resource(name="commentServiceImpl")
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}
}
