package cn.edu.jnu.web.service;

import java.util.LinkedHashMap;

import cn.edu.jnu.web.entity.user.AccountRecord;
import cn.edu.jnu.web.util.QueryResult;

public interface AccountRecordService {
	QueryResult<AccountRecord> listRecords(int start, int limit, 
			String[] ands, LinkedHashMap<String, String> sorts);
}
