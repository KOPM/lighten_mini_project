package com.tencent.kopm.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.tencent.kopm.dao.UserDAO;
import com.tencent.kopm.model.User;
import com.tencent.kopm.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDAO userDao;

	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}

	private UserDAO getUserDao() {
		return userDao;
	}
	
	// HibernateTemplate SessionFactory
	private HibernateTemplate ht = null;
	private SessionFactory sessionFactory;
	
	public HibernateTemplate getHt() {
		if(ht == null) {
			ht = new HibernateTemplate(sessionFactory);
		}
		return ht;
	}

	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public User findByUsername(Object username) {
		List<User> userList = (List<User>)getUserDao().findByUsername(username);
		return userList.size()>0 ? userList.get(0) : null;
	}
}