package com.tencent.kopm.service;

import com.tencent.kopm.model.User;

public interface UserService {
	
	/**
	 * ˵�����ж��û����Ƿ����
	 * ������userName
	 * ���أ��û��������ڣ�����true���û������ڣ�����false
	 * Author��firminli
	 */
	public boolean userNameValid(String userName);

	/**
	 * ˵�����û�ע�ᣬͬʱ����ɼ�
	 * ������userName��passWord��score
	 * ���أ��û�ע��ɹ�������true���û�ע��ʧ�ܣ�����false
	 * Author��firminli
	 */
	public boolean userSignIn(String userName, String passWord, Integer score);

	/**
	 * ˵�����û���¼����scoreΪ���û���óɼ������±���
	 * ������userName
	 * ���أ��û���¼�ɹ�������1���û������󣬷���-1��������󣬷���0
	 * Author��firminli
	 */
	public int userLogin(String userName, String passWord, Integer score);
	
	/**
	 * ˵������scoreΪ���û���óɼ������±���
	 * ������userName��score
	 * ���أ�����ɹ�������true������ʧ�ܣ�����false
	 * Author��davionli
	 */
	public boolean saveScore(String username, int score);

	/**
	 * ˵���������û��������û���Ϣ
	 * ������userName
	 * ���أ��û����ڣ�����User�����û������ڣ�����null
	 * Author��firminli
	 */
	public User findByUsername(Object username);
}