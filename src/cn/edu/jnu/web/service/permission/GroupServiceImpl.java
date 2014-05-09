package cn.edu.jnu.web.service.permission;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.jnu.web.dao.impl.permission.GroupDaoImpl;
import cn.edu.jnu.web.entity.permission.Group;
import cn.edu.jnu.web.util.QueryResult;

@Service
public class GroupServiceImpl implements GroupService {

	private GroupDaoImpl groupDao;
	
	@Override
	public void saveGroup(Group group) {
		groupDao.save(group);
	}

	@Override
	public void deleteGroup(int id) {
		groupDao.delete(Group.class, id);
	}
	
	@Override
	public Group findGroup(int id) {
		return groupDao.find(Group.class, id);
	}

	@Override
	public void updateGroup(Group group) {
		groupDao.update(group);
	}

	@Override
	public QueryResult<Group> listGroups(int start, int limit, String[] ands) {
		QueryResult<Group> res = new QueryResult<Group>();
		res.setResults(groupDao.pagingQuery(Group.class, start, limit, ands, null));
		res.setTotal(groupDao.pagingQuery(Group.class, -1, -1, ands, null).size());
		return res;
	}

	public GroupDaoImpl getGroupDao() {
		return groupDao;
	}
	@Resource(name="groupDaoImpl")
	public void setGroupDao(GroupDaoImpl groupDao) {
		this.groupDao = groupDao;
	}

}
