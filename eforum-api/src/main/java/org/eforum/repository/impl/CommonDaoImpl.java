package org.eforum.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.eforum.constant.Constants;
import org.eforum.entity.BaseEntity;
import org.eforum.entity.User;
import org.eforum.exception.ServiceException;
import org.eforum.repository.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public class CommonDaoImpl implements CommonDao {
	@Autowired
	private EntityManager entityManager;

	@Override
	public BaseEntity get(Class clazz, Long id) {
		return (BaseEntity) entityManager.find(clazz, id);
	}

	@Override
	public void save(BaseEntity entity) {
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getSession().getAttribute(Constants.CURRENT_USER_IN_SESSION);
		save(entity, user);
	}

	@Override
	public List<? extends Object> findByHql(String hql, String paramKey, Object paramValue) {
		Query query = entityManager.createQuery(hql);
		query.setParameter(paramKey, paramValue);
		return query.getResultList();
	}

	@Override
	public Object findUniqueByHql(String hql, String paramKey, Object paramValue) {
		List<Object> entitys = (List<Object>) findByHql(hql, paramKey, paramValue);
		if (entitys.size() > 1) {
			throw new ServiceException("查询结果不唯一");
		}
		if (entitys.size() == 0) {
			return null;
		}
		return entitys.get(0);
	}

	@Override
	public List pagingQuery(String hql, int pageNumber, int pageSize) {
		Query query = entityManager.createQuery(hql);
		query.setFirstResult(pageNumber - 1);
		query.setMaxResults(pageSize);
		query.getResultList();
		return query.getResultList();
	}

	@Override
	public List pagingQuery(Class clazz, int pageNumber, int pageSize) {
		String hql = "FROM " + clazz.getSimpleName() + " obj WHERE 1=1";
		return pagingQuery(hql, pageNumber, pageSize);
	}

	@Override
	public <T extends BaseEntity> T findUniqueByHql(Class<T> clazz, String whereSub, String paramKey,
			Object paramValue) {
		String hql = "FROM " + clazz.getSimpleName() + " obj WHERE " + whereSub;

		return (T) findUniqueByHql(hql, paramKey, paramValue);
	}

	@Override
	public BaseEntity findUniqueByHql(String hql, Map<String, Object> condition) {
		List<? extends BaseEntity> entitys = findByHql(hql, condition);
		if (entitys.size() > 1) {
			throw new ServiceException("查询结果不唯一");
		}
		if (entitys.size() == 0) {
			return null;
		}
		return entitys.get(0);
	}

	@Override
	public List<? extends BaseEntity> findByHql(String hql, Map<String, Object> condition) {
		Query query = entityManager.createQuery(hql);
		for (Entry<String, Object> entry : condition.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}

	@Override
	public List<? extends BaseEntity> findByHql(Class<?> clazz, String whereSub, Map<String, Object> condition) {
		String hql = "FROM " + clazz.getSimpleName() + " obj WHERE " + whereSub;
		return findByHql(hql, condition);
	}

	@Override
	public <T extends BaseEntity> T findUniqueByHql(Class<T> clazz, String whereSub, Map<String, Object> condition) {
		String hql = "FROM " + clazz.getSimpleName() + " obj WHERE " + whereSub;
		return (T) findUniqueByHql(hql, condition);
	}

	@Override
	public <T extends BaseEntity> void save(T entity, User user) {
		Date date = new Date();
		if (entity.isNew()) {
			entity.setCreateTime(date);
			entity.setCreateUser(user);
		}
		entity.setLastUpdateTime(date);
		entity.setLastUpdateUser(user);
		entityManager.persist(entity);
	}

}
