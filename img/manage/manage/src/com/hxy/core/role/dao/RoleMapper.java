package com.hxy.core.role.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.role.entity.Role;

public interface RoleMapper {
	List<Role> selectRole(Map<String, Object> map);

	long countRole(Map<String, Object> map);

	Role getById(long roleId);

	void add(Role role);

	void update(Role role);

	void delete(long id);

	void deleteRoleAndResource(long id);

	Role selectResource(long roleId);

	void insertRoleAndResource(Map<String, Object> map);

	List<Role> selectAll();

	List<Role> getMenu(long roleId);

	Role selectSystemAdmin();
	
	List<Role> selectAllNoAdmin();
	
	List<Role> selectxAllNoAdmin();
}
