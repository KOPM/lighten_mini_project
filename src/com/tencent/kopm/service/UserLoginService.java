package com.tencent.kopm.service;

public interface UserLoginService {
	//判断用户名是否不存在，存在返回false，不存在为true
	public boolean userNameValid(String userName);

	//用户注册
	public boolean userSignIn(String userName, String passWord);

	//用户登录
	public boolean userLogin(String userName, String passWord);

}
