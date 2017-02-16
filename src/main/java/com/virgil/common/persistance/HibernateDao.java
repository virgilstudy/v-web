package com.virgil.common.persistance;

import com.virgil.common.utils.Reflections;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Virgil on 2017/1/30.
 */
public class HibernateDao<T, PK extends Serializable> extends SimpleHibernateDao<T, PK> {
    /**
     * 用于Dao层子类的构造函数.
     * 通过子类的泛型定义取得对象类型Class.
     * eg.
     * public class UserDao extends HibernateDao<User, Long>{
     * }
     */
    public HibernateDao() {
        super();
    }

    /**
     * 用于省略Dao层, Service层直接使用通用HibernateDao的构造函数.
     * 在构造函数中定义对象类型Class.
     * eg.
     * HibernateDao<User, Long> userDao = new HibernateDao<User, Long>(sessionFactory, User.class);
     */
    public HibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
        super(sessionFactory, entityClass);
    }

    /**
     * 分页获取全部对象.
     */
    public Page<T> getAll(final Page<T> page) {
        return findPage(page);
    }

    /**
     * 按HQL分页查询.
     *
     * @param values 数量可变的查询参数,按顺序绑定.
     * @return 分页查询结果, 附带结果列表及所有查询输入参数.
     */
    public Page<T> findPage(final Page<T> page, String hql, Object... values) {
        Query query = createQuery(hql, values);
        if (page.isAutoCount()) {
            long count = countHalResult(hql, values);
            page.setTotalCount(count);
        }
        setPageParameterToQuery(query, page);
        List result = query.list();
        page.setList(result);
        return page;
    }

    /**
     * 按Criteria分页查询.
     *
     * @param page       分页参数.
     * @param criterions 数量可变的Criterion.   @return 分页查询结果.附带结果列表及所有查询输入参数.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Page<T> findPage(final Page<T> page, final Criterion... criterions) {
        Assert.notNull(page, "page不能为空");

        Criteria c = createCriteria(criterions);

        if (page.isAutoCount()) {
            long totalCount = countCriteriaResult(c);
            page.setTotalCount(totalCount);
        }

        setPageParameterToCriteria(c, page);

        List result = c.list();
        page.setList(result);
        return page;
    }

    protected long countCriteriaResult(final Criteria c) {
        CriteriaImpl impl = (CriteriaImpl) c;
        // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();
        List<CriteriaImpl.OrderEntry> orderEntries = null;
        try {
            Reflections.getFieldValue(impl, "orderEntries");
            Reflections.setFieldValue(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
        // 执行Count查询
        Long totalCountObject = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
        long totalCount = (totalCountObject != null) ? totalCountObject : 0;
        // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
        c.setProjection(projection);
        if (projection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        try {
            Reflections.setFieldValue(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
        return totalCount;
    }

    /**
     * 设置分页参数到Criteria对象,辅助函数.
     */
    protected Criteria setPageParameterToCriteria(final Criteria c, final Page<T> page) {

        Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

        //hibernate的firstResult的序号从0开始
        c.setFirstResult(page.getFirst() - 1);
        c.setMaxResults(page.getPageSize());

        if (page.isOrderBySetted()) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');

            Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

            for (int i = 0; i < orderByArray.length; i++) {
                if (Page.ASC.equals(orderArray[i])) {
                    c.addOrder(Order.asc(orderByArray[i]));
                } else {
                    c.addOrder(Order.desc(orderByArray[i]));
                }
            }
        }
        return c;
    }

    /**
     * 设置分页参数到Query对象,辅助函数.
     */
    protected Query setPageParameterToQuery(final Query q, final Page<T> page) {
        Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");
        //hibernate的firstResult的序号从0开始
        q.setFirstResult(page.getFirst() - 1);
        q.setMaxResults(page.getPageSize());
        return q;
    }


    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数.
     * <p>
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
     */
    protected long countHalResult(String hql, final Object... values) {
        String countHql = prepareCountHql(hql);
        try {
            Long count = findUnique(countHql, values);
            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
        }
    }

    private String prepareCountHql(String orgHql) {
        String fromHql = orgHql;
        //select子句与order by子句会影响count查询,进行简单的排除.
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");
        String countHql = "select count(*) " + fromHql;
        return countHql;
    }

    /**
     * 按属性查找对象列表,支持多种匹配方式.
     *
     * @param matchType 匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
     */
    public List<T> findBy(final String propertyName, final Object value, final PropertyFilter.MatchType matchType) {
        Criterion criterion = buildCriterion(propertyName, value, matchType);
        return find(criterion);
    }

    /**
     * 按属性过滤条件列表查找对象列表.
     */
    public List<T> find(List<PropertyFilter> filters) {
        Criterion[] criterions = buildCriterionByPropertyFilter(filters);
        return find(criterions);
    }

    /**
     * 按属性条件参数创建Criterion,辅助函数.
     */
    protected Criterion buildCriterion(final String propertyName, final Object propertyValue, final PropertyFilter.MatchType
            matchType) {
        Criterion criterion = null;
        switch (matchType) {
            case EQ:
                criterion = Restrictions.eq(propertyName, propertyValue);
                break;
            case LIKE:
                criterion = Restrictions.like(propertyName, propertyValue);
                break;
            case LE:
                criterion = Restrictions.le(propertyName, propertyValue);
                break;
            case LT:
                criterion = Restrictions.lt(propertyName, propertyValue);
                break;
            case GE:
                criterion = Restrictions.ge(propertyName, propertyValue);
                break;
            case GT:
                criterion = Restrictions.gt(propertyName, propertyValue);
                break;
        }
        return criterion;
    }

    /**
     * 按属性条件列表创建Criterion数组,辅助函数.
     */
    public Criterion[] buildCriterionByPropertyFilter(final List<PropertyFilter> filters) {
        List<Criterion> criterionList = new ArrayList<>();
        for (PropertyFilter filter : filters) {
            if (!filter.hasMultiProperties()) {
                Criterion criterion = buildCriterion(filter.getPropertyName(), filter.getMatchValue(), filter.getMatchType());
                criterionList.add(criterion);
            } else {
                Disjunction disjunction = Restrictions.disjunction();
                for (String param : filter.getPropertyNames()) {
                    Criterion criterion = buildCriterion(param, filter.getMatchValue(), filter.getMatchType());
                    disjunction.add(criterion);
                }
                criterionList.add(disjunction);
            }
        }
        return criterionList.toArray(new Criterion[criterionList.size()]);
    }
}
