package com.tencent.kopm.service;

public interface UserLoginService {
	//�ж��û����Ƿ񲻴��ڣ����ڷ���false��������Ϊtrue
	public boolean userNameValid(String userName);

	//�û�ע��
	public boolean userSignIn(String userName, String passWord);

	//�û���¼
	public boolean userLogin(String userName, String passWord);

}
