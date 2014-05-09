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
 * ʵ��DAO�ӿ� �ĳ�����࣬ʵ��ʵ�����Ĭ����ɾ�Ĳ����
 * @author HHT
 *
 */
@Transactional
public abstract class DaoSupport implements Dao {

	protected HibernateTemplate hibernateTemplate;

	/**
	 * ʵ����Ĭ�ϵĳ־û�������
	 */
	@Override
	public void save(Object entity) {
		hibernateTemplate.save(entity);
	}

	/**
	 * ʵ����Ĭ�ϵ�ɾ��������
	 * �ò���������ɾ�����ݿ��еļ�¼��
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
	 * ʵ����Ĭ�ϵĸ��²����������ݿ��еĳ־û�������и��¡�
	 */
	@Override
	public void update(Object entity) {
		hibernateTemplate.update(entity);
	}

	/**
	 * ʵ����Ĭ�ϵĲ��Ҳ�����
	 * ����ʵ����id������Ӧ��ʵ�����
	 */
	@Override
	@Transactional(readOnly=true, propagation=Propagation.NOT_SUPPORTED)
	public <T> T find(final Class<T> clazz, final Serializable entityId) {
		return hibernateTemplate.get(clazz, entityId);
	}

	/**
	 * Ĭ�ϵ�ʵ����������ѯ������
	 * ����ָ���Ĳ�ѯ����ʵ������ҡ�
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
	 * ��ҳ��ѯ��
	 */
	@Override
	public <T> List<T> pagingQuery(final Class<T> clazz, final int start, final int max) {
		return pagingQuery(clazz, start, max, null, null);
	}

	/**
	 * ��ҳ��ѯ�����ҿ���ָ�����������˳��
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
	 * ��ҳ��ѯ������ָ���������й��˺�����
	 */
	@Override
	public <T> List<T> pagingQuery(final Class<T> clazz, final int start, final int max,
			final String[] ands, final LinkedHashMap<String, String> sorts) {
		
		return pagingQuery(clazz, start, max, ands, null, sorts);

	}
	
	/**
	 * ��ҳ��ѯ������ָ���������й��˺�����
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
	 * ��������Mapƴ��SQL��䡣
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
	 * ����������ȡ���Entity���ơ�
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
	 * ����ָ��������Mapƴ��������䡣
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
