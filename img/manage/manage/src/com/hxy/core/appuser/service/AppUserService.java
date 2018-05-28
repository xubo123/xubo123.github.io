package com.hxy.core.appuser.service;

import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.appuser.entity.AppUser;

public interface AppUserService
{
		
	DataGrid<AppUser> dataGrid(Map<String, Object> map);
	
	AppUser getById(long id);
	
	void delete(String ids);

}
