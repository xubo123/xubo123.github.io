package com.hxy.core.user.service;

import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.user.entity.User;

/**
 * @author Administrator
 *
 */
public interface UserService
{
	DataGrid<User> dataGrid(Map<String, Object> map);
	
	void save(User user,String ids);
	
	long countByUserAccount(Map<String, Object> map);
	
	User selectByUserId(long userId);
	
	void update(User user,String ids);
	
	void delete(long userId);
	
	User selectRole(long userId);
	
	void updateGrant(String ids,long id);
	
	User selectByUserAccount(String userAccount);
	
	User selectByUserAccountx(String userAccount);
	
	User selectByUserAccountx_new(String userAccount);
	
	void updatePassword(User user);
	
}
