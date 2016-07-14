package com.tencent.kopm.service.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
	
	@Override
	public boolean userNameValid(String userName) {
		try {
			List<User> userList = (List<User>)getUserDao().findByUsername(userName);
			if(userList.isEmpty()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	@Override
	public boolean userSignIn(String userName, String passWord, Integer score) {
		try {
			if(userNameValid(userName) && score>=0 && score<=200){ // 0 <= score <= 200
				User user = new User(userName,passWord,score);
				getUserDao().save(user);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int userLogin(String userName, String passWord, Integer score) {
		try {
			if(userNameValid(userName)){
				return -1;//username invalid
			}else{
				User user = (User) getUserDao().findByUsername(userName).get(0);
				if(user.getPassword().equals(passWord)){
				    saveScore(userName, score);
					return 1;//login successful
				}else{
					return 0;//password invalid¯¯
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}	
	}
	
	@Override
	public boolean saveScore(String username, int score) {
		try {
			if(score>=0 && score <= 200) { // 0 <= score <= 200
				List<User> userList = (List<User>)getUserDao().findByUsername(username);
				int size = userList.size();
				if(size > 0) {
					if(userList.get(0).getScore() < score) {
						Session session = sessionFactory.getCurrentSession();
						Transaction tx = session.beginTransaction();
						String hqlUpdate = "update User set score = :newScore where username = :username";
						int updateEntity = session.createQuery(hqlUpdate)
								.setInteger("newScore", score)
								.setString("username", username)
								.executeUpdate();
						tx.commit();
						session.close();
					}
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public User findByUsername(Object username) {
		try {
			List<User> userList = (List<User>)getUserDao().findByUsername(username);
			int size = userList.size();
			if(size > 0) {
				return userList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}