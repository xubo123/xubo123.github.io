package com.hxy.core.role.service;

import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.role.entity.Role;

public interface RoleService
{
	DataGrid<Role> dataGrid(Map<String, Object> map);

	Role getById(long roleId);

	void save(Role role);

	void update(Role role);

	void delete(long id);
	
	Role selectResource(long roleId);
	
	void updateGrant(String ids,long id);
	
	List<Role> selectAll();
	
	List<Role> selectAllNoAdmin();
	
	List<Role> getMenu(long roleId);
	
	List<Role> selectxAllNoAdmin();
	
}
