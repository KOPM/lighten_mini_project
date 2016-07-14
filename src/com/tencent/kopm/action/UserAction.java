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
	 * 说明：验证用户名是否存在
	 * 参数：userName
	 * 返回：用户名不存在，可以注册，返回1；用户名存在，不能注册，返回-1
	 * Author：firminli
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
	 * 说明：验证用户的登录状态
	 * 参数：无
	 * 返回：有登录状态，返回用户名字符串；无登录状态，返回-1
	 * Author：davionli
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
	 * 说明：用户注册，同时保存score成绩
	 * 参数：userName，passWord，score
	 * 返回：用户注册成功，返回1；用户注册失败，返回-1
	 * Author：firminli
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
	 * 说明：用户登录，若score为该用户最好成绩，更新保存
	 * 参数：userName，passWord，score
	 * 返回：用户登录成功，设置cookie（600秒），返回1；用户名错误，清除cookie，返回-1；密码错误，清除cookie，返回0
	 * Author：firminli
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
	 * 说明：用户退出，清除cookie
	 * 参数：无
	 * 返回：无
	 * Author：firminli
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
	 * 说明：（用户在登录状态下）若score为该用户最好成绩，更新保存
	 * 参数：score
	 * 返回：用户未登录，返回-1；用户已登录，返回前10成绩（JSON格式），降序排列
	 * Author：davionli
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
