package com.tencent.kopm.service;

import com.tencent.kopm.model.User;

public interface UserService {
	
	/**
	 * 说明：判断用户名是否存在
	 * 参数：userName
	 * 返回：用户名不存在，返回true；用户名存在，返回false
	 * Author：firminli
	 */
	public boolean userNameValid(String userName);

	/**
	 * 说明：用户注册，同时保存成绩
	 * 参数：userName，passWord，score
	 * 返回：用户注册成功，返回true；用户注册失败，返回false
	 * Author：firminli
	 */
	public boolean userSignIn(String userName, String passWord, Integer score);

	/**
	 * 说明：用户登录，若score为该用户最好成绩，更新保存
	 * 参数：userName
	 * 返回：用户登录成功，返回1；用户名错误，返回-1；密码错误，返回0
	 * Author：firminli
	 */
	public int userLogin(String userName, String passWord, Integer score);
	
	/**
	 * 说明：若score为该用户最好成绩，更新保存
	 * 参数：userName，score
	 * 返回：保存成功，返回true；保存失败，返回false
	 * Author：davionli
	 */
	public boolean saveScore(String username, int score);

	/**
	 * 说明：根据用户名返回用户信息
	 * 参数：userName
	 * 返回：用户存在，返回User对象；用户不存在，返回null
	 * Author：firminli
	 */
	public User findByUsername(Object username);
}