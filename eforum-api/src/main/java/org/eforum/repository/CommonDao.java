package org.eforum.repository;

import java.util.List;
import java.util.Map;

import org.eforum.entity.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * 一些公用操作的dao
 * 
 * @author huxiansheng
 *
 */
public interface CommonDao {
	/**
	 * 根据ID查找entity
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T extends BaseEntity> T get(Class<T> clazz, Long id);

	/**
	 * 保存entity
	 * 
	 * @param entity
	 */
	@Transactional
	public <T extends BaseEntity> void save(T entity);

	/**
	 * 根据hql查找
	 * 
	 * @param hql
	 * @param paramKey
	 * @param paramValue
	 */
	public List<? extends Object> findByHql(String hql, String paramKey, Object paramValue);

	/**
	 * 根据hql查找唯一的结果，如果为空或者多余一条结果，则抛出异常
	 * 
	 * @param hql
	 * @param condition
	 */
	public BaseEntity findUniqueByHql(String hql, Map<String, Object> condition);

	/**
	 * 根据hql查找唯一的结果，如果为空或者多余一条结果，则抛出异常
	 * 
	 * @param clazz
	 *            所要查询类的class
	 * @param whereSub
	 *            查询字句。例如 obj.username = :username
	 * @param condition
	 *            参数
	 * @return
	 */
	public <T extends BaseEntity> T findUniqueByHql(Class<T> clazz, String whereSub, Map<String, Object> condition);

	/**
	 * 根据hql查找唯一的结果，如果为空或者多余一条结果，则抛出异常
	 * 
	 * @param hql
	 * @param paramKey
	 * @param paramValue
	 */
	public Object findUniqueByHql(String hql, String paramKey, Object paramValue);

	/**
	 * 根据hql查找唯一的结果，如果为空或者多余一条结果，则抛出异常
	 * 
	 * @param clazz
	 *            所要查询类的class
	 * @param whereSub
	 *            查询字句。例如 obj.username = :username
	 * @param paramKey
	 *            参数key
	 * @param paramValue
	 *            参数值
	 * @return
	 */
	public <T extends BaseEntity> T findUniqueByHql(Class<T> clazz, String whereSub, String paramKey,
			Object paramValue);

	/**
	 * 分页查询
	 * 
	 * @param clazz
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List pagingQuery(Class clazz, int pageNumber, int pageSize);

	/**
	 * 根据条件查询
	 * 
	 * @param hql
	 * @param condition
	 * @return
	 */
	public List<? extends BaseEntity> findByHql(String hql, Map<String, Object> condition);

	/**
	 * @param clazz
	 * @param whereSub
	 * @param condition
	 * @return
	 */
	public List<? extends BaseEntity> findByHql(Class<?> clazz, String whereSub, Map<String, Object> condition);

	/**
	 * 分页查询
	 * 
	 * @param hql
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List pagingQuery(String hql, int pageNumber, int pageSize);
}