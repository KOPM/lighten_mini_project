package com.tencent.kopm.service;

import com.tencent.kopm.model.Record;
import com.tencent.kopm.model.User;

import java.util.List;

public interface RankListService {

	/**
	 * ˵������ȡ��Ϸ�б�ɼ�
	 * ������User����
	 * ���أ�����ǰ10�ɼ��б�Record�б�
	 * Author��chmwang
	 */
	public List<Record> rankList(User user);
}