package com.hxy.core.authCount.service;

import com.hxy.core.authCount.entity.AuthCount;

public interface AuthCountService {
	AuthCount selectByAccountNum(String accountNum);

	void save(AuthCount authCount);

	void update(AuthCount authCount);

	void delete(String ids);
}
