package com.tencent.kopm.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import com.tencent.kopm.model.Record;
import com.tencent.kopm.service.RankListService;
import com.tencent.kopm.service.UserService;
import com.tencent.kopm.util.JsonConvert;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class RankListAction extends ActionSupport {
	
	static final String domainStr = "tencentkopm";

	UserService userService;
	RankListService rankListService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setRankListService(RankListService rankListService) {
		this.rankListService = rankListService;
	}

	public RankListService getRankListService() {
		return rankListService;
	}
	
	/**
	 * 说明：（用户在登录状态下）获取游戏列表成绩
	 * 参数：无
	 * 返回：用户未登录，返回-1；用户已登录，返回前10成绩（JSON格式），降序排列
	 * Author：chmwang
	 */
	public void ajaxGetRankList() {
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
				List<Record> rankList = getRankListService().rankList(getUserService().findByUsername(userName));
				JSONObject jsonList = JsonConvert.generate(rankList);
				String jsonListStr = jsonList.toString();
				out.write(jsonListStr);
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