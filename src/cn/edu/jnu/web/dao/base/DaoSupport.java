package cn.edu.jnu.web.dao.base;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Entity;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 实现DAO接口 的抽象基类，实现实体类的默认增删改查操作
 * @author HHT
 *
 */
@Transactional
public abstract class DaoSupport implements Dao {

	protected HibernateTemplate hibernateTemplate;

	/**
	 * 实体类默认的持久化操作。
	 */
	@Override
	public void save(Object entity) {
		hibernateTemplate.save(entity);
	}

	/**
	 * 实体类默认的删除操作。
	 * 该操作将彻底删除数据库中的记录。
	 */
	@Override
	public <T> T delete(final Class<T> clazz, final Serializable entityId) {
		return hibernateTemplate.execute(new HibernateCallback<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Object o = session.get(clazz, entityId);
				session.delete(o);
				return (T) o;
			}
		});
	}

	/**
	 * 实体类默认的更新操作，对数据库中的持久化对象进行更新。
	 */
	@Override
	public void update(Object entity) {
		hibernateTemplate.update(entity);
	}

	/**
	 * 实体类默认的查找操作。
	 * 根据实体类id查找相应的实体对象。
	 */
	@Override
	@Transactional(readOnly=true, propagation=Propagation.NOT_SUPPORTED)
	public <T> T find(final Class<T> clazz, final Serializable entityId) {
		return hibernateTemplate.get(clazz, entityId);
	}

	/**
	 * 默认的实体类条件查询操作。
	 * 根据指定的查询进行实体类查找。
	 */
	@Override
	public <T> List<T> list(Class<T> clazz, String[] ands) {
		return pagingQuery(clazz, -1, -1, ands, null);
		/*StringBuffer sql = new StringBuffer();
		sql.append("from ");
		sql.append(getEntityName(clazz));
		sql.append(" o ");
		if(wheres != null && wheres.length != 0) {
			sql.append(createWhereSql(wheres));
		}
		
		List<T> lists = hibernateTemplate.find(sql.toString());
		
		if(lists == null || lists.size() == 0) return null;
		
		QueryResult<T> res = new QueryResult<T>();
		res.setResults(lists);
		res.setTotal(lists.size());
		return res;*/
	}

	/**
	 * 分页查询。
	 */
	@Override
	public <T> List<T> pagingQuery(final Class<T> clazz, final int start, final int max) {
		return pagingQuery(clazz, start, max, null, null);
	}

	/**
	 * 分页查询，并且可以指定结果的排列顺序。
	 */
	@Override
	public <T> List<T> pagingQuery(final Class<T> clazz, final int start, final int max,
			final LinkedHashMap<String, String> sorts) {
		return pagingQuery(clazz, start, max, null, sorts);
		/*List<T> lists = hibernateTemplate.executeFind(new HibernateCallback<List<T>>() {

			@Override
			public List<T> doInHibernate(Session session) throws HibernateException,
					SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("from ");
				sql.append(getEntityName(clazz));
				sql.append(" o ");
				if(sorts != null && !sorts.isEmpty()) {
					sql.append(createSortSql(sorts));
				}
				return session.createQuery(sql.toString())
						.setFirstResult(start)
						.setMaxResults(max)
						.list();
				
			}
		});
		
		if(lists == null || lists.size() == 0) return null;
		
		QueryResult<T> qres = new QueryResult<T>();
		qres.setResults(lists);
		qres.setTotal(lists.size());
		return qres;*/
	}
	
	/**
	 * 分页查询，并对指定条件进行过滤和排序
	 */
	@Override
	public <T> List<T> pagingQuery(final Class<T> clazz, final int start, final int max,
			final String[] ands, final LinkedHashMap<String, String> sorts) {
		
		return pagingQuery(clazz, start, max, ands, null, sorts);

	}
	
	/**
	 * 分页查询，并对指定条件进行过滤和排序
	 */
	@SuppressWarnings({"deprecation", "unchecked"})
	@Override
	public <T> List<T> pagingQuery(final Class<T> clazz, final int start, final int max,
			final String[] wheres, final String[] ors, final LinkedHashMap<String, String> sorts) {
		return hibernateTemplate.executeFind(new HibernateCallback<List<T>>() {

			@Override
			public List<T> doInHibernate(Session session) throws HibernateException,
					SQLException {
				StringBuffer sql = new StringBuffer();
				sql.append("from ");
				sql.append(getEntityName(clazz));
				sql.append(" o ");
				sql.append(createWhereSql(wheres, ors));
				if(sorts != null && !sorts.isEmpty()) {
					sql.append(createSortSql(sorts));
				}
				
				Query query = session.createQuery(sql.toString());
				if(start >= 0) {
					query.setFirstResult(start);
				}
				if(max >= 0) {
					query.setMaxResults(max);
				}
				return query.list();
			}
		});

	}
	
	/**
	 * 根据条件Map拼接SQL语句。
	 * @param wheres
	 * @return
	 */
	private Object createWhereSql(String[] wheres, String[] ors) {
		StringBuffer sql = new StringBuffer();
		if((wheres != null && wheres.length != 0) || (ors != null && ors.length != 0)) {
			sql.append(" where ");
			if(wheres != null)
				for(String s : wheres) {
					sql.append(s);
					sql.append(" and ");
				}
			if(ors == null || ors.length == 0) sql = sql.replace(sql.lastIndexOf(" and "), sql.length(), " ");
			else {
				sql.append("(");
				for(String s : ors) {
					sql.append(s);
					sql.append(" or ");
				}
				sql = sql.replace(sql.lastIndexOf("or"), sql.length(), "");
				sql.append(")");	
			}
			return sql;
		} else {
			return "";
		}
	}

	/**
	 * 根据类名获取类的Entity名称。
	 * @param clazz
	 * @return
	 */
	private <T> String getEntityName(Class<T> clazz) {
		String name = clazz.getSimpleName();
		Entity entity = clazz.getAnnotation(Entity.class);
		if(entity.name() != null && !"".equals(entity.name())) {
			name = entity.name();
		}
		return name;
	}
	
	/**
	 * 根据指定的排序Map拼接排序语句。
	 * @param sorts
	 * @return
	 */
	private String createSortSql(LinkedHashMap<String, String> sorts) {
		StringBuffer sql = new StringBuffer();
		Set<Entry<String, String>> entrySet = sorts.entrySet();
		Iterator<Entry<String, String>> it = entrySet.iterator();
		sql.append(" order by ");
		while(it.hasNext()) {
			Entry<String, String> entry = it.next();
			sql.append(entry.getKey());
			sql.append(" ");
			sql.append(entry.getValue());
			sql.append(",");
		}
		sql = sql.replace(sql.lastIndexOf(","), sql.length(), "");
		return sql.toString();
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	
	@Resource
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
}
