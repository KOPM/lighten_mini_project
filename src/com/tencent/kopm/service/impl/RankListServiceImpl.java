package com.tencent.kopm.service.impl;

import com.tencent.kopm.dao.UserDAO;
import com.tencent.kopm.model.User;
import com.tencent.kopm.service.RankListService;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class RankListServiceImpl implements RankListService {

	private static final int rankListMaxSize = 10;
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
	

	private class ComparatorUserScore implements Comparator<Object> {
		public int compare(Object arg0, Object arg1) {
			User user0 = (User)arg0;
  			User user1 = (User)arg1;
  			return user1.getScore().compareTo(user0.getScore());
		}
	}

	public List<User> rankList(User user) {
		List<User> userList = (List<User>)getUserDao().findAll();
		
		ComparatorUserScore comparator = new ComparatorUserScore();
		Collections.sort(userList, comparator);

		List<User> rankList = new ArrayList<User>();
		Iterator<User> iter = userList.iterator();
		while (iter.hasNext() && rankList.size()<rankListMaxSize) {
			rankList.add(iter.next());
		}


		if (rankList.contains(user) == false) {
			int index = userList.indexOf(user);
			if (rankList.size() > 0) {
				rankList.remove(rankList.size()-1);
			}
			
			if (index == userList.size()-1) {
				rankList.add(userList.get(index));
				return rankList;
			}
			
			if (rankList.size() > 0) {
				rankList.remove(rankList.size()-1);
			}
			rankList.add(userList.get(index));
			rankList.add(userList.get(index+1));
		}

		return rankList;
	}
}