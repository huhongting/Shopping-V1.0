package cn.edu.jnu.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.PressDaoImpl;
import cn.edu.jnu.web.entity.Press;
import cn.edu.jnu.web.service.PressService;
import cn.edu.jnu.web.util.QueryResult;

/**
 * 出版社服务实现类
 * @author HHT
 *
 */
@Service
public class PressServiceImpl implements PressService {
	private PressDaoImpl pressDao;

	public PressDaoImpl getPressDao() {
		return pressDao;
	}

	@Resource(name="pressDaoImpl")
	public void setPressDao(PressDaoImpl pressDao) {
		this.pressDao = pressDao;
	}

	@Override
	public void savePress(Press press) {
		pressDao.save(press);
	}

	@Override
	public void updatePress(Press press) {
		pressDao.update(press);
	}

	@Override
	public QueryResult<Press> list(String start, String limit) {
		/*int s, l;
		try {
			s = Integer.valueOf(start);
			l = Integer.valueOf(limit);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			s = -1;
			l = -1;
		}
		QueryResult<Press> res = new QueryResult<Press>();
		res.setResults(pressDao.pagingQuery(Press.class, s, l));
		res.setTotal(pressDao.list(Press.class, null).size());
		return res;*/
		return list(start, limit, null, null);
	}

	@Override
	public Press deletePress(int id) {
		return pressDao.delete(Press.class, id);
	}
	
	@Override
	public void deletePress(int[] ids) {
		for(int id : ids) {
			deletePress(id);
		}
	}

	@Override
	public Press findPress(int id) {
		return pressDao.find(Press.class, id);
	}

	@Override
	public Press findPress(String name) {
		List<Press> list = pressDao.list(Press.class, new String[] {"pressname = '" + name + "'"});
		if(list.size() == 0) return null;
		return list.get(0);
	}

	@Override
	public QueryResult<Press> list(String start, String limit, String[] ands) {
		/*int s, l;
		try {
			s = Integer.valueOf(start);
			l = Integer.valueOf(limit);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			s = -1;
			l = -1;
		}
		QueryResult<Press> res = new QueryResult<Press>();
		res.setResults(pressDao.pagingQuery(Press.class, s, l, ands, null));
		res.setTotal(pressDao.pagingQuery(Press.class, -1, -1, ands, null).size());
		return res;*/
		return list(start, limit, ands, null);
	}

	@Override
	public QueryResult<Press> list(String start, String limit, String[] ands,
			String[] ors) {
		int s, l;
		try {
			s = Integer.valueOf(start);
			l = Integer.valueOf(limit);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			s = -1;
			l = -1;
		}
		List<Press> list = pressDao.pagingQuery(Press.class, s, l, ands, ors, null);
		List<Press> total = pressDao.pagingQuery(Press.class, -1, -1, ands, ors, null);
		QueryResult<Press> res = new QueryResult<Press>();
		res.setResults(list);
		res.setTotal(total.size());
		return res;
	}
}
