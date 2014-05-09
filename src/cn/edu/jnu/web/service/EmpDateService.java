package cn.edu.jnu.web.service;

import cn.edu.jnu.web.entity.emp.EmpDate;
import cn.edu.jnu.web.util.QueryResult;

public interface EmpDateService {
	public void AddEmpDate(EmpDate ed);
	public EmpDate findById(int id);
	public void deleteEmpDate(int id);
	public QueryResult<EmpDate> listEmpDates(int start, int limit, String[] ands);
}
