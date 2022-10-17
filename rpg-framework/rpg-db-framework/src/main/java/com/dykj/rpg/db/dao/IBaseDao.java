package com.dykj.rpg.db.dao;

import com.dykj.rpg.db.data.BaseModel;
import com.dykj.rpg.db.queue.IDbQueue;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import java.util.Collection;
import java.util.List;

/**
 * @Author: jianbo.Gong
 * @Date: 2018/3/2 11:21
 * @Description:
 */

public interface IBaseDao<T extends BaseModel> extends IDbQueue<T> {
	/**
	 * model映射
	 *
	 * @return
	 */
	RowMapper<T> rowMapper();

	/**
	 * 新增数据 NamedParameterJdbcTemplate
	 * 
	 * @param data
	 *            数据对象实例
	 * @return effect row number
	 * @throws Exception
	 */
	int insert(T data) throws DataAccessException;

	/**
	 * 更新数据 NamedParameterJdbcTemplate
	 * 
	 * @param data
	 *            数据对象实例
	 * @return effect row number
	 * @throws Exception
	 */
	int updateByPrimaykey(T data) throws DataAccessException;

	/**
	 * 通过玩家id查询结果 NamedParameterJdbcTemplate
	 * 
	 * @return 结果�?
	 * @throws Exception
	 * @args 参数 注意跟sql语句参数的顺序一�?
	 */
	T queryByPrimaykey(Object... args) throws DataAccessException;

	/**
	 * 通过实例id删除数据记录 NamedParameterJdbcTemplate
	 * 
	 * @param data
	 *            数据对象实例
	 * @return effect row number
	 * @throws Exception
	 */
	int deleteByPrimaykey(T data) throws DataAccessException;

	/**
	 * 批量插入数据 NamedParameterJdbcTemplate
	 * 
	 * @param collection
	 *            数据集合
	 * @return effect row number
	 * @throws Exception
	 */
	int[] batchInsert(Collection<T> collection) throws DataAccessException;

	/**
	 * 批量更新 NamedParameterJdbcTemplate
	 *
	 * @param collection
	 *            数据集合
	 * @return effect row number
	 * @throws Exception
	 */
	int[] batchUpdate(Collection<T> collection) throws DataAccessException;

	/**
	 * 批量删除 NamedParameterJdbcTemplate
	 *
	 * @param collection
	 *            数据集合
	 * @return effect row number
	 * @throws Exception
	 */
	int[] batchDelete(Collection<T> collection) throws DataAccessException;

	/**
	 * 删除某些记录 JdbcTemplate
	 *
	 * @param sql
	 * @param args
	 * @throws Exception
	 */
	void delete(String sql, Object... args) throws DataAccessException;

	/**
	 * 查询某条记录 JdbcTemplate
	 *
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	T queryForObject(String sql, Object... args) throws DataAccessException;

	/**
	 * 查询集合 JdbcTemplate
	 * 
	 * @param args
	 *            查询条件
	 * @return 结果�?
	 * @throws Exception
	 */
	List<T> queryForList(String sql, Object... args) throws DataAccessException;

}
