package com.hxy.core.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.user.dao.UserDeptMapper;
import com.hxy.core.user.dao.UserMapper;
import com.hxy.core.user.entity.User;
import com.hxy.core.user.entity.UserDept;
import com.hxy.system.SecretUtil;

@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserDeptMapper userDeptMapper;

	public DataGrid<User> dataGrid(Map<String, Object> map) {
		DataGrid<User> dataGrid = new DataGrid<User>();
		long count = userMapper.countUser(map);
		dataGrid.setTotal(count);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<User> list = userMapper.selectUserList(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public void save(User user, String ids) {
		try {
			user.setUserPassword(SecretUtil.encryptToSHA(user.getUserPassword()));
			userMapper.save(user);
	
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				UserDept userDept = new UserDept();
				userDept.setDeptId(id);
				userDept.setUserId(user.getUserId());
				userDeptMapper.save(userDept);
			}
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public long countByUserAccount(Map<String, Object> map) {
		return userMapper.countByUserAccount(map);
	}

	public User selectByUserId(long userId) {
		return userMapper.selectByUserId(userId);
	}

	public void update(User user, String ids) {
		try {
			userMapper.update(user);
			
			String[] idArray = ids.split(",");
			userDeptMapper.deleteByUserId(user.getUserId());
			for (String id : idArray) {
				UserDept userDept = new UserDept();
				userDept.setDeptId(id);
				userDept.setUserId(user.getUserId());
				userDeptMapper.save(userDept);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void delete(long userId) {
		try {
			userDeptMapper.deleteByUserId(userId);
			userMapper.delete(userId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public User selectRole(long userId) {
		return userMapper.selectRole(userId);
	}

	public void updateGrant(String ids, long id) {
		try {
			String[] idAttr = ids.split(",");
			for (String idStr : idAttr) {
				if (idStr != null && !"".equals(idStr)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", id);
					map.put("roleId", Long.parseLong(idStr));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public User selectByUserAccount(String userAccount) {
		return userMapper.selectByUserAccount(userAccount);
	}

	public void updatePassword(User user) {
		userMapper.updatePassword(user);
	}

	@Override
	public User selectByUserAccountx(String userAccount) {
		return userMapper.selectByUserAccountx(userAccount);
	}

	@Override
	public User selectByUserAccountx_new(String userAccount) {
		// TODO Auto-generated method stub
		return userMapper.selectByUserAccountx_new(userAccount);
	}

}
