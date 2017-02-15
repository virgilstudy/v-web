package com.virgil.common.persistance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virgil.common.utils.Reflections;
import org.hibernate.*;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Virgil on 2017/1/30.
 */
@Component
public class SimpleHibernateDao<T, PK extends Serializable> {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected SessionFactory sessionFactory;
    protected final Class<T> entityClass;
    protected final String entityName;

    /**
     * 用于Dao层子类使用的构造函数.
     * 通过子类的泛型定义取得对象类型Class.
     * eg.
     * public class UserDao extends SimpleHibernateDao<User, Long>
     */
    public SimpleHibernateDao() {
        this.entityClass = Reflections.getClassGenericType(getClass());
        this.entityName = entityClass.getName().replace(entityClass.getPackage().getName(), "").substring(1);
    }

    /**
     * 用于用于省略Dao层, 在Service层直接使用通用SimpleHibernateDao的构造函数.
     * 在构造函数中定义对象类型Class.
     * eg.
     * SimpleHibernateDao<User, Long> userDao = new SimpleHibernateDao<User, Long>(sessionFactory, User.class);
     */
    public SimpleHibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
        this.entityName = entityClass.getName().replace(entityClass.getPackage().getName(), "").substring(1);
    }

    /**
     * 取得当前Session.
     *
     * @return Session
     */
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(final T entity) {
        getSession().save(entity);
    }

    public void delete(final T entity) {
        getSession().delete(entity);
    }

    public void delete(final PK id) {
        delete(find(id));
    }

    public T find(final PK id) {
        return (T) getSession().load(entityClass, id);
    }

    public T findOne(final String condition, final Object... values) {
        Query query = getSession().createQuery("select a from " + entityName + " a where " + condition);
        for (int i = 0; i < values.length; ++i) {
            query.setParameter(i, values[i]);
        }
        query.setFirstResult(0).setMaxResults(1);
        List list = query.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return (T) list.get(0);
        }
    }

    /**
     * 按id列表获取对象列表.
     *
     * @param idList
     * @return 对象集合
     */
    public List<T> find(final Collection<PK> idList) {
        return find(Restrictions.in(getIDName(), idList));
    }

    public List<T> findAll() {
        return find();
    }

    /**
     * 获取全部对象.
     *
     * @param isCache 是否缓存
     * @return 对象集合.
     */
    public List<T> findAll(Boolean isCache) {
        return find(isCache);
    }

    /**
     * 获取全部对象, 支持按属性行序.
     *
     * @param orderByProperty 排序属性name
     * @param isAsc           是否升序排序
     * @return 查询结果集合
     */
    public List<T> findAll(String orderByProperty, boolean isAsc) {
        Criteria criteria = createCriteria();
        if (isAsc) {
            criteria.addOrder(Order.asc(orderByProperty));
        } else {
            criteria.addOrder(Order.desc(orderByProperty));
        }
        return criteria.list();
    }

    /**
     * 按属性查找对象列表, 匹配方式为相等
     *
     * @param propertyName 属性name
     * @param value        属性值
     * @return 结果集合
     */
    public List<T> findBy(final String propertyName, final Object value) {
        Criterion criterion = Restrictions.eq(propertyName, value);
        return find(criterion);
    }

    /**
     * 按属性查找唯一对象, 匹配方式为相等
     *
     * @param propertyName 属性name
     * @param value        属性值
     * @return 结果对象
     */
    public T findUniqueBy(final String propertyName, final Object value) {
        Criterion criterion = Restrictions.eq(propertyName, value);
        return (T) createCriteria(criterion).uniqueResult();
    }

    /**
     * 按HQL查询对象列表.
     *
     * @param hql
     * @param values 数量可变的参数,按顺序绑定.
     * @return 结果集合
     */
    public <X> List<X> find(final String hql, final Object... values) {
        return createQuery(hql, values).list();
    }

    ObjectMapper mapper=new ObjectMapper();

    public List<T> simpleFindAll(String condition, Object... argv) {
        Query query = getSession().createQuery("select a from " + entityName + " a where " + condition);
        for (int i = 0; i < argv.length; ++i) {
            query.setParameter(i, argv[i]);
        }
        List list = query.list();
        return list;
    }

    /**
     * 按HQL查询对象列表.
     *
     * @param hql
     * @param values 命名参数,按名称绑定.
     * @return 对象集合
     */
    public <X> List<X> find(final String hql, final Map<String, ?> values) {
        return createQuery(hql, values).list();
    }


    public <X> X findUnique(final String hql, final Object... values) {
        return (X) createQuery(hql, values).uniqueResult();
    }


    /**
     * 按HQL查询唯一对象.
     *
     * @param hql
     * @param values 命名参数,按名称绑定.
     * @return 对象
     */
    public <X> X findUnique(final String hql, final Map<String, ?> values) {
        return (X) createQuery(hql, values).uniqueResult();
    }

    /**
     * 执行HQL进行批量修改/删除操作.
     *
     * @param hql
     * @param values 数量可变的参数,按顺序绑定.
     * @return 更新记录数.
     */
    public int batchExecute(final String hql, final Object... values) {
        return createQuery(hql, values).executeUpdate();
    }

    public int batchExecute(final String hql, final Map<String, ?> values) {
        return createQuery(hql, values).executeUpdate();
    }

    /**
     * 根据查询HQL与参数列表创建Query对象.
     *
     * @param queryString
     * @param values      数量可变的参数,按顺序绑定.
     * @return Query
     */
    public Query createQuery(final String queryString, final Object... values) {
        Query query = getSession().createQuery(queryString);
        ObjectMapper mapper = new ObjectMapper();

        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                try {
                    System.out.println(mapper.writeValueAsString(values[i]));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    /**
     * 根据查询HQL与参数列表创建Query对象.
     * 与find()函数可进行更加灵活的操作.
     *
     * @param queryString
     * @param values      命名参数,按名称绑定.
     * @return Query
     */
    public Query createQuery(final String queryString, final Map<String, ?> values) {
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            query.setProperties(values);
        }
        return query;
    }

    /**
     * 根据查询SQL与参数列表创建Query对象.
     *
     * @param queryString
     * @param values      数量可变的参数,按顺序绑定.
     * @return SQLQuery
     */
    public SQLQuery createSQLQuery(final String queryString, final Object... values) {
        SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                sqlQuery.setParameter(String.valueOf(i), values[i]);
            }
        }
        return sqlQuery;
    }

    /**
     * 根据查询SQL与参数列表创建Query对象.
     *
     * @param queryString
     * @param values      命名参数,按名称绑定.
     * @return SQLQuery
     */

    public SQLQuery createSQLQuery(final String queryString, final Map<String, ?> values) {
        SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
        if (values != null) {
            sqlQuery.setProperties(values);
        }
        return sqlQuery;
    }

    /**
     * 按Criteria查询对象列表.
     *
     * @param criterions 数量可变的Criterion.
     * @return 结果集合
     */
    public List<T> find(final Criterion... criterions) {
        return createCriteria(criterions).list();
    }

    public List<T> find(Boolean isCache, final Criterion... criterions) {
        return createCriteria(isCache, criterions).list();
    }

    /**
     * 按Criteria查询唯一对象.
     *
     * @param criterions 数量可变的Criterion.
     * @return 对象
     */
    public T findUnique(final Criterion... criterions) {
        return (T) createCriteria(criterions).uniqueResult();
    }

    public Criteria createCriteria(Boolean isCache, final Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        criteria.setCacheable(isCache);
        return criteria;
    }

    /**
     * 根据Criterion条件创建Criteria.
     * 与find()函数可进行更加灵活的操作.
     *
     * @param criterions 数量可变的Criterion.
     * @return Criteria
     */
    public Criteria createCriteria(final Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion criterion : criterions) {
            criteria.add(criterion);
        }
        return criteria;
    }

    /**
     * 初始化对象.
     * 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化.
     * 如果传入entity, 则只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性.
     * 如需初始化关联属性,需执行:
     * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合.
     * Hibernate.initialize(user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
     */
    public void initProxyObject(Object proxy) {
        Hibernate.initialize(proxy);
    }

    /**
     * Flush当前Session.
     */
    public void flush() {
        getSession().flush();
    }

    /**
     * 为Query添加distinct transformer.
     * 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
     *
     * @param query
     * @return Query
     */
    public Query distinct(Query query) {
        query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return query;
    }

    /**
     * 取得对象的主键名.
     *
     * @return 对象的主键名
     */
    public String getIDName() {
        ClassMetadata classMetadata = sessionFactory.getClassMetadata(entityClass);
        return classMetadata.getIdentifierPropertyName();
    }

    /**
     * 判断对象的属性值在数据库内是否唯一.
     * 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
     *
     * @param propertyName 属性name
     * @param newValue     新值
     * @param oldValue     旧值
     * @return 是否唯一
     */
    public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
        if (newValue == null || newValue.equals(oldValue)) {
            return true;
        }
        Object obj = findUniqueBy(propertyName, newValue);
        return (obj == null);
    }

}

