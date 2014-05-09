package cn.edu.jnu.web.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.BookTypeDaoImpl;
import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.service.BookTypeService;
import cn.edu.jnu.web.util.QueryResult;

/**
 * 图书类型服务的实现类，为图书类型实体提供基本操作服务
 * @author HHT
 *
 */
@Service
public class BookTypeServiceImpl implements BookTypeService {
	private BookTypeDaoImpl bookTypeDao;

	public BookTypeDaoImpl getBookTypeDao() {
		return bookTypeDao;
	}

	@Resource(name="bookTypeDaoImpl")
	public void setBookTypeDao(BookTypeDaoImpl bookTypeDao) {
		this.bookTypeDao = bookTypeDao;
	}

	@Override
	public void saveBookType(BookType bookType) {
		bookTypeDao.save(bookType);
	}

	@Override
	public void updateBookType(BookType bookType) {
		bookTypeDao.update(bookType);
	}

	@Override
	public QueryResult<BookType> list() {
		QueryResult<BookType> res = new QueryResult<BookType>();
		res.setResults(bookTypeDao.list(BookType.class, null));
		return res;
	}

	@Override
	public QueryResult<BookType> list(int parentId) {
		String[] wheres = new String[] {"parent="+parentId};
		QueryResult<BookType> res = new QueryResult<BookType>();
		res.setResults(bookTypeDao.list(BookType.class, wheres));
		return res;
	}

	@Override
	public QueryResult<BookType> listRoots(String start, String limit, boolean deleted) {
		List<String> ands = new ArrayList<String>();
		ands.add("parent is null");
		if(!deleted) ands.add("deleted=false");
		int s, m;
		try {
			s = Integer.valueOf(start);
			m = Integer.valueOf(limit);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			s = -1;
			m = -1;
		}
		LinkedHashMap<String, String> sorts = new LinkedHashMap<String, String>();
		sorts.put("sortid", "asc");
		QueryResult<BookType> res = new QueryResult<BookType>();
		
		res.setResults(bookTypeDao.pagingQuery(BookType.class, s, m, ands.toArray(new String[]{}), sorts));
		res.setTotal(bookTypeDao.pagingQuery(BookType.class, -1, -1, ands.toArray(new String[]{}), null).size());
		return res;
	}
	
	@Override
	public BookType deleteBookType(int bookTypeId) {
		BookType bt = bookTypeDao.find(BookType.class, bookTypeId);
		if(bt == null) return null;
		for(BookType b : bt.getChildTypes())// 同时删除该书籍类型的子类型。
			b.setDeleted(true);
		bt.setDeleted(true);
		bookTypeDao.update(bt);
		
		if(bt.getParent() != null) {// 如果该书籍类型有父类型，则判断该父类型是否还有未删除的子类型，如果没有，则该父类型更新为叶子节点。
			BookType parent = bookTypeDao.find(BookType.class, bt.getParent().getTypeId());
			boolean flag = true;
			for(BookType b : parent.getChildTypes()) {
				if(b.getDeleted() == false) {
					flag = false;
					break;
				}
			}
			parent.setLeaf(flag);
			bookTypeDao.update(parent);
		}
		
		return bt;
	}
	
	@Override
	public void deleteBookType(int[] bookTypeIds) {
		for(int id : bookTypeIds) {
			deleteBookType(id);
		}
	}

	@Override
	public BookType findBookType(int id) {
		return bookTypeDao.find(BookType.class, id);
	}

	@Override
	public QueryResult<BookType> list(String start, String limit, String[] ands) {
		//return list(start, limit, ands, null);
		return list(start, limit, ands, null, null);
	}

	@Override
	public BookType findBookType(String name) {
		List<BookType> list = bookTypeDao.list(BookType.class, 
								new String[] {"typename = '" + name + "'"});
		if(list.size() == 0) return null;
		return list.get(0);
	}

	@Override
	public QueryResult<BookType> list(String start, String limit,
			String[] ands, LinkedHashMap<String, String> sorts) {
		/*int s = -1, l = -1;
		try {
			s = Integer.valueOf(start);
			l = Integer.valueOf(limit);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
		}
		QueryResult<BookType> res = new QueryResult<BookType>();
		res.setResults(bookTypeDao.pagingQuery(BookType.class, s, l, ands, sorts));
		res.setTotal(bookTypeDao.pagingQuery(BookType.class, -1, -1, ands, sorts).size());
		return res;*/
		return list(start, limit, ands, null, sorts);
	}

	@Override
	public List<BookType> queryByTypeName(String name, int method) {
		switch(method) {
		case BLUR:
			return bookTypeDao.pagingQuery(BookType.class, 0, -1, 
					new String[]{"deleted=false", "typename like '%"+name+"%'"}, null, null);
		case EXACT:
			return bookTypeDao.pagingQuery(BookType.class, 0, -1, 
					new String[]{"deleted=false", "typename='"+name+"'"}, null, null);
		}
		return null;
	}

	@Override
	public QueryResult<BookType> list(String start, String limit,
			String[] ands, String[] ors, LinkedHashMap<String, String> sorts) {
		int s = -1, l = -1;
		try {
			s = Integer.valueOf(start);
			l = Integer.valueOf(limit);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
		}
		QueryResult<BookType> res = new QueryResult<BookType>();
		res.setResults(bookTypeDao.pagingQuery(BookType.class, s, l, ands, ors, sorts));
		res.setTotal(bookTypeDao.pagingQuery(BookType.class, -1, -1, ands, ors, null).size());
		return res;
	}
}
