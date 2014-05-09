package cn.edu.jnu.web.controller;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.jnu.web.entity.BookType;
import cn.edu.jnu.web.service.BookTypeService;
import cn.edu.jnu.web.util.QueryResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * �鼮���Ϳ�����
 * @author HHT
 *
 */
@Controller
public class BookTypeController {
	
	private BookTypeService bookTypeService;
	private ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * ������ͼ������
	 * @param typename
	 * @param note
	 * @param parent
	 * @return
	 */
	@RequestMapping(value="/control/admin/booktype.do", params="method=add")
	public @ResponseBody ObjectNode handlerAdd(@RequestParam String typename, 
			@RequestParam String note, @RequestParam String parent) {
		BookType type = bookTypeService.findBookType(typename);
		if(type == null) {
			BookType bt = new BookType();
			bt.setNote(note);
			bt.setTypeName(typename);
			
			if(parent == null || parent.equals("")) bt.setParent(null);
			else {
				BookType b = bookTypeService.findBookType(parent);
				if(b == null || b.getDeleted() == true) {
					return mapper.createObjectNode().put("success", false);
				} else {
					bt.setParent(b);
					b.setLeaf(false);
					bookTypeService.updateBookType(b);
				}
			}
			bookTypeService.saveBookType(bt);
			return mapper.createObjectNode().put("success", true);
		} else {// ����Ѿ�������ͬ�鼮���ͣ������ʧ�ܡ�
			return mapper.createObjectNode().put("success", false);
		}
	}
	
	/**
	 * ɾ��ͼ������
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/control/admin/booktype.do", params="method=del")
	public @ResponseBody ObjectNode handlerDel(@RequestParam int[] id) {
		if(id == null || id.length == 0) 
			return mapper.createObjectNode().put("success", false);
		bookTypeService.deleteBookType(id);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * �鼮���͸���
	 * @param id
	 * @param typename
	 * @param note
	 * @param delflag
	 * @return
	 */
	@RequestMapping(value="/control/admin/booktype.do", params="method=update")
	public @ResponseBody ObjectNode handlerUpdate(@RequestParam String id, 
			@RequestParam String typename, @RequestParam String note,
			@RequestParam boolean delflag) {
		try {
			BookType bt = bookTypeService.findBookType(Integer.valueOf(id));
			bt.setNote(note);
			bt.setTypeName(typename);
			bt.setDeleted(delflag);
			for(BookType b : bt.getChildTypes())// ͬʱ���¸��鼮���͵������͡�
				b.setDeleted(delflag);
			
			bookTypeService.updateBookType(bt);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return mapper.createObjectNode().put("success", false);
		}
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * ��ʾ�鼮�����ҳ�˵���ʾ˳��
	 * @return
	 */
	@RequestMapping(value="/control/admin/booktype.do", params="method=sort")
	public @ResponseBody ObjectNode handlerSortID() {
		QueryResult<BookType> res = bookTypeService.list("-1", "-1", 
				new String[]{"parentid=null", "deleted=false"});
		ObjectNode json = mapper.createObjectNode();
		ArrayNode records = mapper.createArrayNode();
		json.put("count", res.getTotal());
		for(BookType bt : res.getResults()) {
			ObjectNode node = mapper.createObjectNode();
			node.put("typeid", bt.getTypeId());
			node.put("typename", bt.getTypeName());
			node.put("note", bt.getNote());
			node.put("sortid", bt.getSortId());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}
	
	/**
	 * �����鼮�����ʾ˳��
	 * @param id
	 * @param sid
	 * @return
	 */
	@RequestMapping(value="/control/admin/booktype.do", params="method=updateSort")
	public @ResponseBody ObjectNode updateSortID(@RequestParam int id, @RequestParam int sid) {
		BookType bt = bookTypeService.findBookType(id);
		if(bt == null) {
			return mapper.createObjectNode().put("success", false);
		}
		bt.setSortId(sid);
		bookTypeService.updateBookType(bt);
		return mapper.createObjectNode().put("success", true);
	}
	
	/**
	 * ������ʾͼ�����ͣ���¼��ͼ��ʱѡ��������
	 * @return
	 */
	@RequestMapping(value="/control/admin/booktype.do", params="method=listtree")
	public @ResponseBody ObjectNode handlerList() {
		QueryResult<BookType> roots = bookTypeService.listRoots("-1", "-1", true);
		return encode2TreeJSON(roots);
	}
	
	private ObjectNode encode2TreeJSON(QueryResult<BookType> roots) {
		ObjectNode json = mapper.createObjectNode();
		json.put("text", ".");
		ArrayNode children = mapper.createArrayNode();
		for(BookType bt : roots.getResults()) {
			ObjectNode node = mapper.createObjectNode();
			Set<BookType> ctps = bt.getChildTypes();
			node.put("typeid", bt.getTypeId());
			node.put("typename", bt.getTypeName());
			BookType parent = bt.getParent();
			node.put("parent", parent == null ? "��" : parent.getTypeName());
			node.put("note", bt.getNote());
			node.put("leaf", bt.getLeaf());
			node.put("delflag", bt.getDeleted());
			addChildren(node, ctps);
			children.add(node);
		}
		json.put("children", children);
		return json;
	}

	private void addChildren(ObjectNode node, Set<BookType> set) {
		if(set != null && !set.isEmpty()) {
			ArrayNode children = mapper.createArrayNode();
			for(BookType bt : set) {
				ObjectNode on = mapper.createObjectNode();
				Set<BookType> ctps = bt.getChildTypes();
				on.put("typeid", bt.getTypeId());
				on.put("typename", bt.getTypeName());
				BookType parent = bt.getParent();
				on.put("parent", parent == null ? "��" : parent.getTypeName());
				on.put("note", bt.getNote());
				on.put("leaf", bt.getLeaf());
				on.put("delflag", bt.getDeleted());
				addChildren(on, ctps);
				children.add(on);
			}
			node.put("children", children);
		}
	}

	/**
	 * ��ѯ�鼮���ͣ��������νṹ���ء�
	 * @param node
	 * @return
	 */
	@RequestMapping(value="/control/admin/booktype.do", params="method=tree")
	public @ResponseBody ArrayNode handelTree(@RequestParam String node) {
		if(node.equals("root")) return list2Json(
								bookTypeService.list("-1", "-1", 
										new String[] {"parent is null", "deleted = false"}));
		else return list2Json(bookTypeService.list("-1", "-1", 
										new String[] {"parent = " + node, "deleted = false"}));
	}
	
	/**
	 * ����ѯ���ת����JSON��ʽ�����ͻ��˶�ȡ������������ʾ�ṹ��
	 * @param lists
	 * @return
	 */
	private ArrayNode list2Json(QueryResult<BookType> lists) {
		ArrayNode json = mapper.createArrayNode();
		
		for(BookType bt : lists.getResults()) {
			ObjectNode node = mapper.createObjectNode();
			node.put("id", bt.getTypeId());
			node.put("text", bt.getTypeName());
			if(bt.getNote() != null && 
					!bt.getNote().equals("") && 
					!bt.getNote().equals("null")) {
				node.put("node", " - [" + bt.getNote() + "]");
			}
			node.put("leaf", bt.getLeaf());
			json.add(node);
		}
		return json;
	}

	/**
	 * ����ѯ���ת����JSON��ʽ��
	 * @param list
	 * @return
	 */
	public ObjectNode encodeJSON(QueryResult<BookType> list) {
		ObjectNode json = mapper.createObjectNode();
		ArrayNode records = mapper.createArrayNode();
		json.put("count", list.getTotal());
		
		Iterator<BookType> it = list.getIterator();
		while(it.hasNext()) {
			ObjectNode node = mapper.createObjectNode();
			BookType bt = it.next();
			node.put("typeid", bt.getTypeId());
			node.put("typename", bt.getTypeName());
			if(bt.getParent() == null) node.put("parent", "null");
			else node.put("parent", bt.getParent().getTypeName());
			node.put("node", bt.getNote());
			node.put("delflag", bt.getDeleted());
			records.add(node);
		}
		json.put("records", records);
		return json;
	}

	public BookTypeService getBookTypeService() {
		return bookTypeService;
	}

	@Resource(name="bookTypeServiceImpl")
	public void setBookTypeService(BookTypeService bookTypeService) {
		this.bookTypeService = bookTypeService;
	}
}
