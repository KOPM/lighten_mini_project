package com.tencent.kopm.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.xwork2.ActionSupport;
import com.tencent.kopm.model.Record;
import com.tencent.kopm.model.User;
import com.tencent.kopm.service.RankListService;
import com.tencent.kopm.service.UserService;
import com.tencent.kopm.util.JsonConvert;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

public class UserAction extends ActionSupport {
	
	static final String domainStr = "tencentkopm";
	
	UserService userService;
	RankListService rankListService;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setRankListService(RankListService rankListService) {
		this.rankListService = rankListService;
	}

	public RankListService getRankListService() {
		return rankListService;
	}
	
	/**
	 * ˵������֤�û����Ƿ����
	 * ������userName
	 * ���أ��û��������ڣ�����ע�ᣬ����1���û������ڣ�����ע�ᣬ����-1
	 * Author��firminli
	 */
	public void ajaxUsernameValid() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String userName = (String) request.getParameter("userName");
		
		boolean valid =  getUserService().userNameValid(userName);
		
		response.setContentType("text/html;charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		try {
			PrintWriter out = response.getWriter();
			if(valid) {
				out.print(1);
			} else {
				out.print(-1);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * ˵������֤�û��ĵ�¼״̬
	 * ��������
	 * ���أ��е�¼״̬�������û����ַ������޵�¼״̬������-1
	 * Author��davionli
	 */
	public void ajaxCookieLogin() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String userName = "";
		boolean valid = false;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(domainStr.equals(c.getName())) {
					valid = true;
					userName = c.getValue();
					break;
				}
			}
		}
		
		response.setContentType("text/html;charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		try {
			PrintWriter out = response.getWriter();
			if(valid) {
				out.write(userName);
			} else {
				out.print(-1);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ˵�����û�ע�ᣬͬʱ����score�ɼ�
	 * ������userName��passWord��score
	 * ���أ��û�ע��ɹ�������1���û�ע��ʧ�ܣ�����-1
	 * Author��firminli
	 */
	public void ajaxSignin() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String userName = (String) request.getParameter("userName");
		String passWord = (String) request.getParameter("passWord");
		String score = (String) request.getParameter("score");
		
		boolean valid = getUserService().userSignIn(userName, passWord, Integer.parseInt(score));
		
		response.setContentType("text/html;charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		Cookie myCookie = new Cookie(domainStr, null);
		myCookie.setMaxAge(0); // 0s
		response.addCookie(myCookie);
		try {
			PrintWriter out = response.getWriter();
			if(valid) {
				out.print(1);
			} else {
				out.print(-1);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ˵�����û���¼����scoreΪ���û���óɼ������±���
	 * ������userName��passWord��score
	 * ���أ��û���¼�ɹ�������cookie��600�룩������1���û����������cookie������-1������������cookie������0
	 * Author��firminli
	 */
	public void ajaxLogin() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String userName = (String) request.getParameter("userName");
		String passWord = (String) request.getParameter("passWord");
		String score = (String) request.getParameter("score");
		
		int valid = getUserService().userLogin(userName, passWord, Integer.parseInt(score));
		
		response.setContentType("text/html;charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if(valid == 1) {
			Cookie myCookie = new Cookie(domainStr, userName);
			myCookie.setMaxAge(600); // ?s
			response.addCookie(myCookie);
		} else {
			Cookie myCookie = new Cookie(domainStr, null);
			myCookie.setMaxAge(0); // 0s
			response.addCookie(myCookie);
		}

		try {
			PrintWriter out = response.getWriter();
			out.print(valid);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * ˵�����û��˳������cookie
	 * ��������
	 * ���أ���
	 * Author��firminli
	 */
	public void ajaxLogout() {
		HttpServletResponse response = ServletActionContext.getResponse();
		
		response.setContentType("text/html;charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		Cookie myCookie = new Cookie(domainStr, null);
		myCookie.setMaxAge(0); // 0s
		response.addCookie(myCookie);
	}
	
	/**
	 * ˵�������û��ڵ�¼״̬�£���scoreΪ���û���óɼ������±���
	 * ������score
	 * ���أ��û�δ��¼������-1���û��ѵ�¼������ǰ10�ɼ���JSON��ʽ������������
	 * Author��davionli
	 */
	public void ajaxSaveScore() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String score = (String) request.getParameter("score");
		
		String userName = "";
		boolean valid = false;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(domainStr.equals(c.getName())) {
					valid = true;
					userName = c.getValue();
					break;
				}
			}
		}
		
		response.setContentType("text/html;charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");
		try {
			PrintWriter out = response.getWriter();
			if(valid) {
				boolean success = getUserService().saveScore(userName, Integer.parseInt(score));
				if(success) {
					User user = getUserService().findByUsername(userName);
					List<Record> rankList = getRankListService().rankList(user);
					JSONObject jsonList = JsonConvert.generate(rankList);
					String jsonListStr = jsonList.toString();
					
					out.write(jsonListStr);
				} else {
					out.print(-1);
				}
				out.flush();
				out.close();
			} else {
				out.print(-1);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				PrintWriter out = response.getWriter();
				out.print(-1);
				out.flush();
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
