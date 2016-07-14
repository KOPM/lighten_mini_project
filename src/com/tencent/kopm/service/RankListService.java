package com.tencent.kopm.service;

import com.tencent.kopm.model.Record;
import com.tencent.kopm.model.User;

import java.util.List;

public interface RankListService {

	/**
	 * 说明：获取游戏列表成绩
	 * 参数：User对象
	 * 返回：返回前10成绩列表（Record列表）
	 * Author：chmwang
	 */
	public List<Record> rankList(User user);
}