package cn.edu.jnu.web.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 分页查询结果保存类
 * @author HHT
 *
 * @param <T>
 */
public class QueryResult<T> {
	private int total; // 总页数
	private List<T> results = new ArrayList<T>();// 查询结果
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getResults() {
		return results;
	}
	public void setResults(List<T> results) {
		this.results = results;
		this.total = results.size();
	}
	
	public T get(int index) {
		return results.get(index);
	}
	
	public Iterator<T> getIterator() {
		return results.iterator();
	}
}
