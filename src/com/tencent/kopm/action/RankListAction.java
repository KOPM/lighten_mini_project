package com.tencent.kopm.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;

import com.tencent.kopm.model.User;
import com.tencent.kopm.service.RankListService;
import com.tencent.kopm.service.UserService;
import com.tencent.kopm.util.JsonConvert;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class RankListAction extends ActionSupport {

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
	
	public void ajaxGetRankList() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		String username = (String) request.getParameter("userName");
		List<User> rankList = getRankListService().rankList(getUserService().findByUsername(username));
		
		JSONObject jsonList = JsonConvert.generate(rankList);
		String jsonListStr = jsonList.toString();
		
		response.setContentType("text/html;charset=UTF-8");
		try {
			PrintWriter out = response.getWriter();
			out.write(jsonListStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}