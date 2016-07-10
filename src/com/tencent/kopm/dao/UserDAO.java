package com.tencent.kopm.dao;

import java.util.List;

import com.tencent.kopm.model.User;

public interface UserDAO {

	// property constants
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String SCORE = "score";

	public abstract void save(User transientInstance);

	public abstract void delete(User persistentInstance);

	public abstract User findById(java.lang.Integer id);

	public abstract List findByExample(User instance);

	public abstract List findByProperty(String propertyName, Object value);

	public abstract List findByUsername(Object username);

	public abstract List findByPassword(Object password);

	public abstract List findByScore(Object score);

	public abstract List findAll();

	public abstract User merge(User detachedInstance);

	public abstract void attachDirty(User instance);

	public abstract void attachClean(User instance);

}