package com.hxy.core.appuser.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.appuser.dao.AppUserMapper;
import com.hxy.core.appuser.entity.AppUser;

@Service("appUserService")
public class AppUserServiceImpl implements AppUserService {

	@Autowired
	private AppUserMapper appUserMapper;

	public DataGrid<AppUser> dataGrid(Map<String, Object> map) {
		DataGrid<AppUser> dataGrid = new DataGrid<AppUser>();
		long count = appUserMapper.count(map);
		dataGrid.setTotal(count);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<AppUser> list = appUserMapper.query(map);
		dataGrid.setRows(list);
		return dataGrid;
	}


	public void delete(String ids) {
		List<Long> list = new ArrayList<Long>();

		String[] idArray = ids.split(",");
		for (int j = 0; j < idArray.length; ++j) {
			list.add(Long.parseLong(idArray[j]));
		}
		appUserMapper.delete(list);
		
	}


	@Override
	public AppUser getById(long id) {
		return appUserMapper.getById(id);
	}

	
	
}
