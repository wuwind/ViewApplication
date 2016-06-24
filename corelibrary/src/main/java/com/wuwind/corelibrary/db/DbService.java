package com.wuwind.corelibrary.db;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DBService<T> {

    private static DBService instance;
    private AbstractDaoSession mDaoSession;
    private AbstractDao<T, Long> abstractDao;

    private DBService() {
    }

    /**
     * 获取实例化
     *
     * @param bean
     * @return
     */
    public static DBService getInstance(Class bean) {
        if (instance == null) {
            instance = new DBService();
        }
        instance.mDaoSession = DBHelper.getDaoSession();
        instance.abstractDao = instance.mDaoSession.getDao(bean);
        return instance;
    }

    private QueryBuilder<T> getQueryBuilder() {
        return instance.abstractDao.queryBuilder();
    }

    /**
     * 添加
     *
     * @param t
     */
    public void insert(final T t) {
        if (t == null) {
            return;
        }
        abstractDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                abstractDao.insertOrReplace(t);
            }
        });
    }

    /**
     * 添加
     *
     * @param list
     */
    public void insertList(final List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        abstractDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    T t = list.get(i);
                    abstractDao.insertOrReplace(t);
                }
            }
        });

    }

    /**
     * 删除
     *
     * @param mId
     */
    public void deleteById(Object mId) {
        delete(abstractDao.getProperties()[0], mId);
    }

    /**
     * 删除
     *
     * @param property
     * @param value
     */
    public void delete(Property property, Object value) {
        QueryBuilder<T> builder = getQueryBuilder();
        DeleteQuery<T> bd = builder.where(property.eq(value)).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
    }

    /**
     * 删除
     *
     * @param property 字段
     * @param values   字段的值
     */
    public void delete(Property property, Object... values) {
        QueryBuilder<T> builder = getQueryBuilder();
        WhereCondition in = property.in(values);
        DeleteQuery<T> bd = builder.where(in).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
    }

    /**
     * 更新
     *
     * @param t
     * @return
     */
    public T update(T t) {
        abstractDao.update(t);
        return t;
    }

    /**
     * 更新  id必须存在
     *
     * @param list
     */
    public void update(List<T> list) {
        abstractDao.updateInTx(list);
    }

    /**
     * 加载全部
     *
     * @return
     */
    public List<T> findAll() {
        return abstractDao.loadAll();
    }

    /**
     * 根据id查询
     *
     * @param mId
     * @return
     */
    public T findById(Object mId) {
        return findBy(abstractDao.getProperties()[0], mId);
    }

    /**
     * 根据属性查询
     *
     * @param property 属性
     * @param value    值
     * @return
     */
    public T findBy(Property property, Object value) {
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        WhereCondition eq = property.eq(value);
        queryBuilder.where(eq);
        List<T> list = queryBuilder.list();
        if (list != null && !list.isEmpty())
            return list.get(0);
        return null;
    }

    /**
     * 查询列表
     *
     * @param property 属性
     * @param value    值
     * @return
     */
    public List<T> findListBy(Property property, Object... value) {
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        WhereCondition in = property.in(value);
        queryBuilder.where(in);
        return queryBuilder.list();
    }

    /**
     * 根据条件升序查询
     *
     * @param property
     * @param conditions
     * @return
     */
    public List<T> findListAsc(Property property, WhereCondition... conditions) {
        QueryBuilder<T> builder = getQueryBuilder();
        if (null != conditions)
            for (WhereCondition where : conditions) {
                builder = builder.where(where);
            }
        builder.orderAsc(property);
        return builder.list();
    }

    /**
     * 根据属性升序查询
     *
     * @param property
     * @return
     */
    public List<T> findListAsc(Property property) {
        QueryBuilder<T> builder = getQueryBuilder();
        return builder.orderAsc(property).list();
    }

    /**
     * 根据条件降序查询
     *
     * @param property
     * @return
     */
    public List<T> findListDesc(Property property, WhereCondition... conditions) {
        QueryBuilder<T> builder = getQueryBuilder();
        if (null != conditions)
            for (WhereCondition where : conditions) {
                builder = builder.where(where);
            }
        builder.orderDesc(property);
        return builder.list();
    }

    /**
     * 根据属性降序查询
     *
     * @param property
     * @return
     */
    public List<T> findListDesc(Property property) {
        QueryBuilder<T> builder = getQueryBuilder();
        return builder.orderDesc(property).list();
    }

    /**
     * 多条件查询
     *
     * @param conditions
     * @return 返回找到的第一条结果
     */
    public T find(WhereCondition... conditions) {
        List<T> tList = findList(conditions);
        if (tList != null)
            return tList.get(0);
        return null;
    }

    /**
     * 模糊查询
     * @param property
     * @param value
     * @return
     */
    public List<T> findLikeList(Property property, Object value) {
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        return queryBuilder.where(property.like("%"+value.toString()+"%")).list();
    }

    /**
     * 多条件组合查询
     * 属性集合和值集合一一对应
     *
     * @param properties 属性集合
     * @param values     值集合
     * @return 返回找到的第一条结果
     */
    public T find(List<Property> properties, List<Object> values) {
        List<T> tList = findList(properties, values);
        if (tList != null && !tList.isEmpty())
            return tList.get(0);
        return null;
    }

    /**
     * 多条件查询
     *
     * @param conditions
     * @return
     */
    public List<T> findList(WhereCondition... conditions) {
        if (conditions == null) return null;
        QueryBuilder<T> builder = getQueryBuilder();
        for (WhereCondition where : conditions) {
            builder = builder.where(where);
        }
        return builder.list();
    }

    /**
     * 多条件组合查询
     * 属性集合和值集合一一对应
     *
     * @param properties 属性集合
     * @param values     值集合
     * @return
     */
    public List<T> findList(List<Property> properties, List<Object> values) {
        if (properties.size() != values.size())
            return null;
        QueryBuilder<T> builder = getQueryBuilder();
        for (int i = 0; i < properties.size(); i++) {
            WhereCondition condition = properties.get(i).eq(values.get(i));
            builder = builder.where(condition);
        }
        return builder.list();
    }

    /**
     * 多条件组合查询
     * 属性集合和值集合一一对应
     *
     * @param properties 属性集合
     * @param values     值集合
     * @return
     */
    public List<T> findListOr(List<Property> properties, List<Object> values) {
        if (properties.size() != values.size())
            return null;
        if (properties.size() < 2)
            return null;
        QueryBuilder<T> builder = getQueryBuilder();
        List<WhereCondition> conditions = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++) {
            WhereCondition condition = properties.get(i).eq(values.get(i));
            conditions.add(condition);
        }
        WhereCondition[] whereConditions = new WhereCondition[conditions.size() - 2];
        for (int i = 2; i < conditions.size(); i++) {
            whereConditions[i - 2] = conditions.get(i);
        }
        builder = builder.whereOr(conditions.get(0), conditions.get(1), whereConditions);
        return builder.list();
    }

    /**
     * 查询在start，end之间,包括首尾
     *
     * @param property
     * @param start
     * @param end
     * @return
     */
    public List<T> findBetween(Property property, Object start, Object end) {
        WhereCondition between = property.between(start, end);
        return findList(between);
    }

    /**
     * 查询below之下，不包括below
     *
     * @param property
     * @param below
     * @return
     */
    public List<T> findBelow(Property property, Object below) {
//        WhereCondition endCondition = new WhereCondition.PropertyCondition(property, " <" + below);
        WhereCondition lt = property.lt(below);
        return findList(lt);
    }

    /**
     * 查询above之上,不包括above
     *
     * @param property
     * @param above
     * @return
     */
    public List<T> findAbove(Property property, Object above) {
        WhereCondition gt = property.gt(above);
        return findList(gt);
    }

}