package cn.edu.jnu.web.service;

import java.util.LinkedHashMap;
import java.util.List;

import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.util.QueryResult;

/**
 * 图书类型的服务类接口，定义图书类型需要的特定服务方法
 * @author HHT
 *
 */
public interface BookTypeService {
	/**
	 * 保存图书类型
	 * @param bookType
	 */
	void saveBookType(BookType bookType);
	/**
	 * 删除图书类型
	 * @param bookTypeId
	 * @return
	 */
	BookType deleteBookType(int bookTypeId);
	/**
	 * 批量删除图书类型
	 * @param bookTypeIds
	 */
	void deleteBookType(int[] bookTypeIds);
	/**
	 * 更新图书类型
	 * @param bookType
	 */
	void updateBookType(BookType bookType);
	/**
	 * 查询图书类型
	 * @param id
	 * @return
	 */
	BookType findBookType(int id);
	/**
	 * 根据图书名称查询图书类型
	 * @param name
	 * @return
	 */
	BookType findBookType(String name);
	/**
	 * 查询所有图书类型
	 * @return
	 */
	QueryResult<BookType> list();
	/**
	 * 分页查询根图书类型
	 * @param start
	 * @param limit
	 * @return
	 */
	QueryResult<BookType> listRoots(String start, String limit, boolean deleted);
	/**
	 * 分页条件查询图书类型
	 * @param start
	 * @param limit
	 * @param wheres
	 * @return
	 */
	QueryResult<BookType> list(String start, String limit, String[] ands);
	/**
	 * 分页条件查询图书类别，并按要求排序
	 * @param start
	 * @param limit
	 * @param ands
	 * @param ors
	 * @param sorts
	 * @return
	 */
	QueryResult<BookType> list(String start, String limit, String[] ands, 
			String[] ors, LinkedHashMap<String, String> sorts);
	/**
	 * 分页条件排序查询
	 * @param start
	 * @param limit
	 * @param wheres
	 * @param sorts
	 * @return
	 */
	QueryResult<BookType> list(String start, String limit, String[] ands, LinkedHashMap<String, String> sorts);
	/**
	 * 查询指定父节点下的子类型
	 * @param parentId
	 * @return
	 */
	QueryResult<BookType> list(int parentId);
	
	public static int EXACT = 1;
	public static int BLUR = 2;
	/**
	 * 根据图书类型名称查询，可以模糊查询也可以精确查询。
	 * @param name
	 * @param method EXACT: 精确查询 (typename = '_typename')<br>BLUR: 模糊查询(typename like '%_typename%')
	 * @return
	 */
	List<BookType> queryByTypeName(String name, int method);
	
}
