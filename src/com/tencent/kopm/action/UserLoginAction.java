package com.tencent.kopm.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.xwork2.ActionSupport;
import com.tencent.kopm.service.UserLoginService;

import org.apache.struts2.ServletActionContext;

public class UserLoginAction extends ActionSupport {
	UserLoginService userLoginService;
	public UserLoginService getUserLoginService() {
		return userLoginService;
	}

	public void setUserLoginService(UserLoginService userLoginService) {
		this.userLoginService = userLoginService;
	}
	
	public void ajaxUsernameValid() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String userName = (String) request.getParameter("userName");
		boolean valid =  getUserLoginService().userNameValid(userName);
		
		response.setContentType("text/html;charset=UTF-8");
		try {
			PrintWriter out = response.getWriter();
			if(valid) {
				System.out.println("��KOPM��--ajaxUsernameValid--�û�����"+ userName + " �����ڣ�����ע��");
				out.print(1);
			} else {
				System.out.println("��KOPM��--ajaxUsernameValid--�û�����"+ userName + " �Ѵ��ڣ�����ע��");
				out.print(0);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void ajaxSignin() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String userName = (String) request.getParameter("userName");
		String passWord = (String) request.getParameter("passWord");
		
		boolean valid = getUserLoginService().userSignIn(userName, passWord);
		
		response.setContentType("text/html;charset=UTF-8");// ע������Response��ContentType
		try {
			PrintWriter out = response.getWriter();
			if(valid) {
				System.out.println("��KOPM��--ajaxSignin--�û���"+ userName + " ע��ɹ�");
				out.print(1);
			} else {
				System.out.println("��KOPM��--ajaxSignin--�û���"+ userName + " ע��ʧ��");
				out.print(0);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void ajaxLogin() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String userName = (String) request.getParameter("userName");
		String passWord = (String) request.getParameter("passWord");
		
		boolean valid = getUserLoginService().userLogin(userName, passWord);
		
		response.setContentType("text/html;charset=UTF-8");
		try {
			PrintWriter out = response.getWriter();
			if(valid) {
				System.out.println("��KOPM��--ajaxLogin--�û���"+ userName + " ��¼�ɹ�");
				out.print(1);
			} else {
				System.out.println("��KOPM��--ajaxLogin--�û���"+ userName + " ��¼ʧ��");
				out.print(0);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
