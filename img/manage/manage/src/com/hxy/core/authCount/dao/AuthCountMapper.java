package com.hxy.core.authCount.dao;

import com.hxy.core.authCount.entity.AuthCount;

public interface AuthCountMapper {
	AuthCount selectByAccountNum(String accountNum);

	void save(AuthCount authCount);

	void update(AuthCount authCount);

	void delete(String accountNum);

}
