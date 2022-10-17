package com.dykj.rpg.db.dao;


import com.dykj.rpg.db.consts.DataSourceKey;
import com.dykj.rpg.db.data.BaseModel;
import com.dykj.rpg.db.queue.AbstractDbQueue;
import com.dykj.rpg.db.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author: jyb
 * @Date: 2018/3/2 11:29
 * @Description:
 */
public abstract class AbstractBaseDao<T extends BaseModel> extends AbstractDbQueue<T> implements IBaseDao<T> {

    private Logger logger = LoggerFactory.getLogger("db");
    /**
     * T.class
     */
    protected Class<T> clazz;
    /**
     * insert语句
     */
    private String insertSql;
    /**
     * 查询语句
     */
    private String querySqL;
    /**
     * 更新语句
     */
    private String updateSql;
    /**
     * 删除语句
     */
    private String deleteSql;

    @Resource(name = "jdbcPoolMap")
    public Map<String, JdbcTemplate> jdbcPoolMap;


    protected String dataSourceKey() {
        return DataSourceKey.DEFAULT;
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcPoolMap.get(dataSourceKey());
    }

    public AbstractBaseDao() {
        super();
        initClazz();
        insertSql = SqlUtil.getInsertSql(clazz);
        updateSql = SqlUtil.getUpdateSql(clazz);
        querySqL = SqlUtil.getQuerySql(clazz);
        deleteSql = SqlUtil.getDeleteSql(clazz);
        logger.debug(clazz.getSimpleName() + "===================sql====================" + clazz.getSimpleName());
        logger.debug("insertSql =" + insertSql);
        logger.debug("updateSql =" + updateSql);
        logger.debug("querySqL  =" + querySqL);
        logger.debug("deleteSql =" + deleteSql);
        logger.debug(clazz.getSimpleName() + "===================sql====================" + clazz.getSimpleName());
    }


    private void initClazz() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            this.clazz = (Class) actualTypeArguments[0];
        } else {
            this.clazz = (Class<T>) genericSuperclass;
        }
    }

    public int insert(T data) throws DataAccessException {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        SqlParameterSource sqlParameterSource = beanPropertySqlParameterSource(data);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int num =namedParameterJdbcTemplate.update(insertSql, sqlParameterSource, keyHolder);
        if(keyHolder.getKey()!=null){
            num =keyHolder.getKey().intValue();
        }
        return num;

    }

    public int updateByPrimaykey(T data) throws DataAccessException {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(data);
        return namedParameterJdbcTemplate.update(updateSql, namedParameters);
    }

    public int deleteByPrimaykey(T data) throws DataAccessException {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(data);
        return namedParameterJdbcTemplate.update(deleteSql, namedParameters);
    }

    public T queryByPrimaykey(Object... args) throws DataAccessException {
        return queryForObject(querySqL,args);
    }

    public List<T> queryForList(String sql, Object... args) throws DataAccessException {
        return getJdbcTemplate().query(sql, args, rowMapper());
    }

    public void delete(String sql, Object... args) throws DataAccessException {
        getJdbcTemplate().update(sql, args);
    }

    public T queryForObject(String sql, Object... args) throws DataAccessException {
        List<T> results = getJdbcTemplate().query(sql, args, rowMapper());
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }
        return DataAccessUtils.requiredSingleResult(results);
    }

    public int[] batchInsert(Collection<T> collection) throws DataAccessException {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        SqlParameterSource[] batchArgs = sqlParameterSources(collection);
        return namedParameterJdbcTemplate.batchUpdate(insertSql, batchArgs);
    }

    public int[] batchUpdate(Collection<T> collection) throws DataAccessException {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        SqlParameterSource[] batchArgs = sqlParameterSources(collection);
        return namedParameterJdbcTemplate.batchUpdate(updateSql, batchArgs);
    }

    public int[] batchDelete(Collection<T> collection) throws DataAccessException {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
        SqlParameterSource[] batchArgs = sqlParameterSources(collection);
        return namedParameterJdbcTemplate.batchUpdate(deleteSql, batchArgs);
    }

    private SqlParameterSource[] sqlParameterSources(Collection<T> collection) {
        SqlParameterSource[] batchArgs = new SqlParameterSource[collection.size()];
        int index = 0;
        T data = null;
        for (Iterator<T> iterator = collection.iterator(); iterator.hasNext(); ) {
            data = iterator.next();
            batchArgs[index++] = beanPropertySqlParameterSource(data);
        }
        return batchArgs;
    }

    private BeanPropertySqlParameterSource beanPropertySqlParameterSource(T data) {
        return new BeanPropertySqlParameterSource(data);
    }

    @Override
    protected void flushInsert(Collection<T> list) {
        batchInsert(list);
    }

    @Override
    protected void flushUpdate(Collection<T> list) {
        batchUpdate(list);
    }

    @Override
    protected void flushDelete(Collection<T> list) {
        batchDelete(list);
    }

    @Override
    protected void singleInsert(T data) {
        insert(data);
    }

    @Override
    protected void singleUpdate(T data) {
        updateByPrimaykey(data);
    }

    @Override
    protected void singleDelete(T data) {
        deleteByPrimaykey(data);
    }

    @Override
    public RowMapper<T> rowMapper() {
        return new BeanPropertyRowMapper<>(this.clazz);
    }

}
