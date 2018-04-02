package dao;

import common.Pager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Repository
public abstract class BaseDao<T> {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    private Class<T> entityClass;

    public BaseDao() {
        Type genType = getClass().getGenericSuperclass();
        if (genType instanceof ParameterizedType) {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            entityClass = (Class) params[0];
        }
    }

    public int getCount(String hql, Object... params) {
        String countSQL = "select count(*) " + hql ;
        int count = Integer.parseInt( getHibernateTemplate().find(countSQL,params).get(0).toString() );
        return count;
    }

    public List getList(int startPosition, int endPosition, String hql, Object... params) {
        List list = getHibernateTemplate().execute((Session session) -> {
            Query query = session.createQuery(hql);
            if( params != null) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i, params[i]);
                }
            }
            query.setFirstResult(startPosition).setMaxResults(endPosition);
            return query.list();
        });
        return list;
    }

    public Pager pagedQuery(int pageIndex, int pageSize, String hql, Object... params ) {
        int count = getCount(removeOrders( hql ), params);
        List list = getList((pageIndex  - 1 )* pageSize, pageSize, hql, params);
        Pager pager = new Pager( pageSize,pageIndex,list );
        pager.setDataCount(count);
        return pager;
    }

    private String removeOrders( String hql ) {
        String result = hql.replaceAll("order\\s+by[\\s\\S]+$","");
        return result;
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hbtpl) {
        this.hibernateTemplate = hbtpl;
    }

    public T get(Serializable id) {
        return (T) getHibernateTemplate().get(entityClass, id);
    }

    public T load(Serializable id) {
        return (T) getHibernateTemplate().load(entityClass, id);
    }

    public List<T> loadAll() {
        return getHibernateTemplate().loadAll(entityClass);
    }

    public void save(T entity) {
        getHibernateTemplate().save(entity);
    }

    public void remove(T entity) {
        getHibernateTemplate().delete(entity);
    }

    public void update(T entity) {
        getHibernateTemplate().update(entity);
    }

    public List find(String hql) {
        return getHibernateTemplate().find(hql);
    }

    public List find(String hql, Object... params) {
        return getHibernateTemplate().find(hql, params);
    }

    public List<Map> findBySQL(String sql, Object... params) {
        return getHibernateTemplate().execute((Session session) -> {
            Query query = session.createSQLQuery(sql);
            if( params != null) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i, params[i]);
                }
            }
            query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
            return query.list();
        });
    }

    public int executeBySQL( String sql, Object... params ) {
        return getHibernateTemplate().execute((Session session) -> {
            Query query = session.createSQLQuery(sql);
            if( params != null) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i, params[i]);
                }
            }
            return query.executeUpdate();
        });
    }

    public int getSum(String sql, Object... params) {
        List<Map> result = findBySQL(sql, params);
        if (result != null && result.size() > 0) {
            Map map = result.get(0);

            for (Object key : map.keySet()) {
                if (map.get(key) != null) {
                    return Integer.parseInt(String.valueOf(map.get(key)));
                }
                return 0;
            }
        }
        return 0;
    }

    public Object getSingle(String sql, Object... params) {
        List<Map> result = findBySQL(sql, params);
        return getSingle(result);
    }

    private Object getSingle(List<Map> result) {
        if (result != null && result.size() > 0) {
            Map map = result.get(0);
            for (Object key :
                    map.keySet()) {
                return map.get(key);
            }
        }
        return null;
    }
}
