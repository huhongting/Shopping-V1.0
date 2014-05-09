package cn.edu.jnu.web.service.impl;

import java.util.LinkedHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.AccountRecordDaoImpl;
import cn.edu.jnu.web.entity.user.AccountRecord;
import cn.edu.jnu.web.service.AccountRecordService;
import cn.edu.jnu.web.util.QueryResult;

@Service
public class AccountRecordServiceImpl implements AccountRecordService {
	
	private AccountRecordDaoImpl accountRecordDao;

	@Override
	public QueryResult<AccountRecord> listRecords(int start, int limit,
			String[] ands, LinkedHashMap<String, String> sorts) {
		QueryResult<AccountRecord> res = new QueryResult<AccountRecord>();
		res.setResults(accountRecordDao.pagingQuery(AccountRecord.class, start, limit, ands, sorts));
		res.setTotal(accountRecordDao.pagingQuery(AccountRecord.class, -1, -1, ands, null).size());
		return res;
	}

	public AccountRecordDaoImpl getAccountRecordDao() {
		return accountRecordDao;
	}
	@Resource(name="accountRecordDaoImpl")
	public void setAccountRecordDao(AccountRecordDaoImpl accountRecordDao) {
		this.accountRecordDao = accountRecordDao;
	}

}
