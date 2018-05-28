package com.hxy.core.role.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.role.dao.RoleMapper;
import com.hxy.core.role.entity.Role;
import com.hxy.system.GetDictionaryInfo;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	public DataGrid<Role> dataGrid(Map<String, Object> map) {
		DataGrid<Role> dataGrid = new DataGrid<Role>();
		long total = roleMapper.countRole(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<Role> list = roleMapper.selectRole(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public Role getById(long roleId) {
		return roleMapper.getById(roleId);
	}

	public void save(Role role) {
		role.setSystemAdmin(0);
		roleMapper.add(role);
	}

	public void update(Role role) {
		roleMapper.update(role);
	}

	public void delete(long id) {
		try {
			roleMapper.deleteRoleAndResource(id);
			roleMapper.delete(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Role selectResource(long roleId) {
		return roleMapper.selectResource(roleId);
	}

	public void updateGrant(String ids, long id) {
		try {
			roleMapper.deleteRoleAndResource(id);
			String[] idAttr = ids.split(",");
			for (String idStr : idAttr) {
				if (idStr != null && !"".equals(idStr)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("roleId", id);
					map.put("id", Long.parseLong(idStr));
					roleMapper.insertRoleAndResource(map);
				}
			}
			// 刷新内存
			GetDictionaryInfo.getInstance().reloadDictionaryInfoMap();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Role> selectAll() {
		return roleMapper.selectAll();
	}

	public List<Role> getMenu(long roleId) {
		return roleMapper.getMenu(roleId);
	}

	@Override
	public List<Role> selectAllNoAdmin() {
		return roleMapper.selectAllNoAdmin();
	}
	
	@Override
	public List<Role> selectxAllNoAdmin() {
		return roleMapper.selectxAllNoAdmin();
	}

}
