package cn.edu.jnu.web.service.permission;

import cn.edu.jnu.web.entity.permission.Group;
import cn.edu.jnu.web.util.QueryResult;

public interface GroupService {
	Group findGroup(int id);
	void saveGroup(Group group);
	void deleteGroup(int id);
	void updateGroup(Group group);
	QueryResult<Group> listGroups(int start, int limit, String[] ands);
}
