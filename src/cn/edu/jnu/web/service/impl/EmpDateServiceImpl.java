package cn.edu.jnu.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.EmpDateDaoImpl;
import cn.edu.jnu.web.entity.emp.EmpDate;
import cn.edu.jnu.web.service.EmpDateService;
import cn.edu.jnu.web.util.QueryResult;

@Service
public class EmpDateServiceImpl implements EmpDateService {

	private EmpDateDaoImpl empDateDao;
	
	@Override
	public EmpDate findById(int id) {
		return empDateDao.find(EmpDate.class, id);
	}

	@Override
	public void deleteEmpDate(int id) {
		empDateDao.delete(EmpDate.class, id);
	}

	@Override
	public QueryResult<EmpDate> listEmpDates(int start, int limit, String[] ands) {
		List<EmpDate> list = empDateDao.pagingQuery(EmpDate.class, start, limit, ands, null);
		QueryResult<EmpDate> res = new QueryResult<EmpDate>();
		res.setResults(list);
		res.setTotal(empDateDao.pagingQuery(EmpDate.class, -1, -1, ands, null).size());
		return res;
	}

	public EmpDateDaoImpl getEmpDateDao() {
		return empDateDao;
	}
	@Resource(name="empDateDaoImpl")
	public void setEmpDateDao(EmpDateDaoImpl empDateDao) {
		this.empDateDao = empDateDao;
	}

	@Override
	public void AddEmpDate(EmpDate ed) {
		empDateDao.save(ed);
	}

}
