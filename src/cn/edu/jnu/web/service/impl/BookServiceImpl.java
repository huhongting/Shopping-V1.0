package cn.edu.jnu.web.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.BookDaoImpl;
import cn.edu.jnu.web.entity.Book;
import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.service.BookService;
import cn.edu.jnu.web.util.QueryResult;

/**
 * 图书服务的实现类，为图书实体提供基本操作服务
 * @author HHT
 *
 */
@Service
public class BookServiceImpl implements BookService {
	private BookDaoImpl bookDao;

	public BookDaoImpl getBookDao() {
		return bookDao;
	}

	@Resource(name="bookDaoImpl")
	public void setBookDao(BookDaoImpl bookDao) {
		this.bookDao = bookDao;
	}

	@Override
	public void saveBook(Book Book) {
		bookDao.save(Book);
	}

	@Override
	public void updateBook(Book book) {
		bookDao.update(book);
	}

	@Override
	public QueryResult<Book> list(String start, String limit) {
		int s, m;
		try {
			s = Integer.valueOf(start);
			m = Integer.valueOf(limit);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			s = -1;
			m = -1;
		}
		
		QueryResult<Book> res = new QueryResult<Book>();
		
		res.setResults(bookDao.pagingQuery(Book.class, s, m));
		res.setTotal(bookDao.pagingQuery(Book.class, -1, -1).size());
		return res;
	}
	
	@Override
	public Book deleteBook(int BookId) {
		return bookDao.delete(Book.class, BookId);
	}
	
	@Override
	public void deleteBook(int[] BookIds) {
		for(int id : BookIds) {
			deleteBook(id);
		}
	}

	@Override
	public Book findBook(int id) {
		return bookDao.find(Book.class, id);
		/*List<Book> books = bookDao.list(Book.class, new String[]{
			"id=" + id,
			"downtime is null",
			"delflag=false"
		});
		if(books == null || books.size() == 0) return null;
		else return books.get(0);*/
	}
	
	@Override
	public Book showBook(int id) {
		List<Book> books = bookDao.list(Book.class, new String[]{
			"id=" + id,
			"downtime is null",
			"delflag=false"
		});
		if(books == null || books.size() == 0) return null;
		else return books.get(0);
	}

	@Override
	public Book findBook(String name) {
		List<Book> list = bookDao.list(Book.class, new String[] {"bookname = '" + name + "'"});
		if(list.size() == 0) return null;
		return list.get(0);
	}

	@Override
	public QueryResult<Book> list(String start, String limit, String[] ands) {
		return list(start, limit, ands, null);
	}

	@Override
	public List<Book> listNewRecommends(int start, int limit) {
		String[] wheres = new String[]{"delflag=false", "downtime is null", "recom=true"};
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		sorts.put("uptime", "desc");
		return bookDao.pagingQuery(Book.class, start, limit, wheres, sorts);
	}

	@Override
	public QueryResult<Book> list(String start, String limit, String[] ands,
			String[] ors) {
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		//sorts.put("recom", "desc");
		sorts.put("uptime", "desc");// 图书查询默认按推荐排序，然后按上架时间排序
		int s, l;
		try {
			s = Integer.valueOf(start);
			l = Integer.valueOf(limit);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			s = -1;
			l = -1;
		}
		
		QueryResult<Book> res = new QueryResult<Book>();
		List<Book> list = bookDao.pagingQuery(Book.class, s, l, ands, ors, sorts);
		res.setResults(list);
		res.setTotal(bookDao.pagingQuery(Book.class, -1, -1, ands, ors, null).size());
		return res;
	}
	
	@Override
	public QueryResult<Book> listBooks(String[] _ands,
			LinkedHashMap<String, String> _sorts, int start, int limit,
			BookType type) {
		List<String> ors = new ArrayList<String>();
		if(type != null) {
			Set<BookType> children = new TreeSet<BookType>();
			getChildTypes(children, type);
			BookType[] bts = children.toArray(new BookType[0]);
			
			for(BookType t : bts) {
				ors.add("bookType.typeId=" + t.getTypeId());
			}
		}		
		List<String> ands = new ArrayList<String>();
		ands.add("delflag=false");
		ands.add("downtime is null");
		if(_ands != null) 
			for(String s : _ands) 
				ands.add(s);
		
		
		List<Book> list = bookDao.pagingQuery(Book.class, start, limit, 
				ands.toArray(new String[]{}), 
				ors.toArray(new String[]{}),
				_sorts);
		QueryResult<Book> res = new QueryResult<Book>();
		res.setResults(list);
		int total = bookDao.pagingQuery(Book.class, -1, -1,
						ands.toArray(new String[]{}), 
						ors.toArray(new String[]{}),
						null).size();
		res.setTotal(total);
		
		return res;
	}

	@Override
	public QueryResult<Book> listTypeBooks(int start, int limit, BookType type) {
		Set<BookType> children = new TreeSet<BookType>();
		getChildTypes(children, type);
		BookType[] bts = children.toArray(new BookType[0]);
		String[] ors = new String[bts.length];
		String[] ands = new String[]{"delflag=false", "downtime is null"};
		int i = 0;
		for(BookType t : bts) {
			ors[i] = "bookType.typeId=" + t.getTypeId();
			i++;
		}
		QueryResult<Book> res = list(String.valueOf(start), 
									String.valueOf(limit), 
									ands, ors);
		return res;
	}

	private void getChildTypes(Set<BookType> children, BookType type) {
		children.add(type);
		for(BookType bt : type.getChildTypes()) {
			children.add(bt);
			if(!bt.getLeaf()) {
				getChildTypes(children, bt);
			}
		}
	}

	@Override
	public QueryResult<Book> listCuXiao(int start, int limit, BookType type) {
		String[] ors = null;
		if(type != null) {
			Set<BookType> children = new TreeSet<BookType>();
			getChildTypes(children, type);
			BookType[] bts = children.toArray(new BookType[0]);
			ors = new String[bts.length];
			int i = 0;
			for(BookType t : bts) {
				ors[i] = "bookType.typeId=" + t.getTypeId();
				i++;
			}
		}
		String[] ands = new String[]{
				"delflag=false", 
				"downtime is null", 
				"saleinfo is not null", 
				"saleinfo != ''"
			};
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		sorts.put("uptime", "desc");
		QueryResult<Book> res = new QueryResult<Book>();
		res.setResults(bookDao.pagingQuery(Book.class, start, limit, 
				ands, ors, sorts));
		return res;
	}
	
	@Override
	public QueryResult<Book> listCuXiao(int start, int limit) {
		return listCuXiao(start, limit, null);
	}
	
	@Override
	public QueryResult<Book> listTeJia(int start, int limit, String[] ands) {
		return listTeJia(start, limit, null, ands, null);
	}

	@Override
	public QueryResult<Book> listTeJia(int start, int limit, BookType type,
			String[] ands, LinkedHashMap<String, String> sorts) {
		String[] ors = null;
		if(type != null) {
			Set<BookType> children = new TreeSet<BookType>();
			getChildTypes(children, type);
			BookType[] bts = children.toArray(new BookType[0]);
			ors = new String[bts.length];
			int i = 0;
			for(BookType t : bts) {
				ors[i] = "bookType.typeId=" + t.getTypeId();
				i++;
			}
		}
		QueryResult<Book> res = new QueryResult<Book>();
		res.setResults(bookDao.pagingQuery(Book.class, start, limit, 
				ands, ors, sorts));
		res.setTotal(bookDao.pagingQuery(Book.class, -1, -1, ands, ors, null).size());
		return res;
	}
	
}
