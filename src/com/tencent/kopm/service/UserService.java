package com.tencent.kopm.service;

import com.tencent.kopm.model.User;

public interface UserService {

	public User findByUsername(Object username);
}