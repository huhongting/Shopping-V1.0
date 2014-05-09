package cn.edu.jnu.web.service;

import java.util.LinkedHashMap;
import java.util.List;

import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.util.QueryResult;

/**
 * ͼ�����͵ķ�����ӿڣ�����ͼ��������Ҫ���ض����񷽷�
 * @author HHT
 *
 */
public interface BookTypeService {
	/**
	 * ����ͼ������
	 * @param bookType
	 */
	void saveBookType(BookType bookType);
	/**
	 * ɾ��ͼ������
	 * @param bookTypeId
	 * @return
	 */
	BookType deleteBookType(int bookTypeId);
	/**
	 * ����ɾ��ͼ������
	 * @param bookTypeIds
	 */
	void deleteBookType(int[] bookTypeIds);
	/**
	 * ����ͼ������
	 * @param bookType
	 */
	void updateBookType(BookType bookType);
	/**
	 * ��ѯͼ������
	 * @param id
	 * @return
	 */
	BookType findBookType(int id);
	/**
	 * ����ͼ�����Ʋ�ѯͼ������
	 * @param name
	 * @return
	 */
	BookType findBookType(String name);
	/**
	 * ��ѯ����ͼ������
	 * @return
	 */
	QueryResult<BookType> list();
	/**
	 * ��ҳ��ѯ��ͼ������
	 * @param start
	 * @param limit
	 * @return
	 */
	QueryResult<BookType> listRoots(String start, String limit, boolean deleted);
	/**
	 * ��ҳ������ѯͼ������
	 * @param start
	 * @param limit
	 * @param wheres
	 * @return
	 */
	QueryResult<BookType> list(String start, String limit, String[] ands);
	/**
	 * ��ҳ������ѯͼ����𣬲���Ҫ������
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
	 * ��ҳ���������ѯ
	 * @param start
	 * @param limit
	 * @param wheres
	 * @param sorts
	 * @return
	 */
	QueryResult<BookType> list(String start, String limit, String[] ands, LinkedHashMap<String, String> sorts);
	/**
	 * ��ѯָ�����ڵ��µ�������
	 * @param parentId
	 * @return
	 */
	QueryResult<BookType> list(int parentId);
	
	public static int EXACT = 1;
	public static int BLUR = 2;
	/**
	 * ����ͼ���������Ʋ�ѯ������ģ����ѯҲ���Ծ�ȷ��ѯ��
	 * @param name
	 * @param method EXACT: ��ȷ��ѯ (typename = '_typename')<br>BLUR: ģ����ѯ(typename like '%_typename%')
	 * @return
	 */
	List<BookType> queryByTypeName(String name, int method);
	
}
