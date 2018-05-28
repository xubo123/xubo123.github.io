package com.hxy.core.user.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.user.entity.User;

public interface UserMapper {
	long countUser(Map<String, Object> map);

	List<User> selectUserList(Map<String, Object> map);

	void save(User user);

	long countByUserAccount(Map<String, Object> map);

	User selectByUserId(long userId);

	void update(User user);

	void delete(long userId);

	void updatePassword(User user);

	User selectRole(long userId);

	User selectByUserAccount(String userAccount);
	
	User selectByUserAccountx(String userAccount);

	User selectAdminUser();

	User selectByUserAccountx_new(String userAccount);
}
