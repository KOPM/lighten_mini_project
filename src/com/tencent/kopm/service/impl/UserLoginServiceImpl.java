package com.tencent.kopm.service.impl;


import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.tencent.kopm.dao.UserDAO;
import com.tencent.kopm.model.User;
import com.tencent.kopm.service.UserLoginService;



public  class UserLoginServiceImpl implements UserLoginService {
	private UserDAO userDao;
	
	public UserDAO getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
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
	
	@Override
	public boolean userNameValid(String userName) {
		// TODO Auto-generated method stub
		List<User> userList = (List<User>)getUserDao().findByUsername(userName);
		//userName = userList.get(0).toJSON().toString();
		if(userList.isEmpty()) {
			return true;
		} else {
			return false;
		}
		
	}
	
	@Override
	public boolean userSignIn(String userName, String passWord) {
		// TODO Auto-generated method stub
		User user = new User(userName,passWord);
		getUserDao().save(user);
		
		return true;
	}

	@Override
	public boolean userLogin(String userName, String passWord) {
		List<User> userList = (List<User>)getUserDao().findByUsername(userName);
		
		if(userList.isEmpty()){
			return false;
		}else{
			if(userList.get(0).getPassword().equals(passWord)){
				return true;
			}else{
				return false;
			}			
		}	
	}
}
