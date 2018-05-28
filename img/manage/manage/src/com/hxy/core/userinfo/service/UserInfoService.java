package com.hxy.core.userinfo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.user.entity.User;
import com.hxy.core.userinfo.entity.UserInfo;

public interface UserInfoService {
	DataGrid<UserInfo> selectByDeptId(Map<String, Object> map);

	DataGrid<UserInfo> selectByDeptIdForAlumni(Map<String, Object> map);
	
	List<UserInfo> selectUserInfoByName(String name);

	boolean delete(String ids);

	void save(UserInfo userInfo, User user,int isInput);

	void saveFromBase(Map<String, Object> map);

	void update(UserInfo userInfo, User user,int isInput);
	
	void update(UserInfo info);

	void updateIdea(UserInfo userInfo);

	UserInfo selectByUserId(String userId);

	List<UserInfo> getUserInfoByUserIdForAlumni(String userId);

	String importData(String url, User user);

	List<UserInfo> selectAllUserList();
	
	/**--查询所有有手机号的用户并且分页--**/
	public List<UserInfo> selectUserToGetTelPage(Map<String, Object> map);

	List<UserInfo> selectUserByClassIdAndName(String userName, String classId);

	void updateUserAccountNum(UserInfo userInfo);

	UserInfo selectAllProByUserId(String userId);

	List<UserInfo> selectUserByClassId(String classId);

	UserInfo selectUserInfoByGmidAndName(Map<String, Object> map);

	int updateUserTelId(Map<String, Object> map, UserInfo userinfo);

	boolean selectUserInClass(String classId, List<String> list);

	List<UserInfo> selectByUserName(String userName);

	List<UserInfo> selectCard(List<String> list);

	DataGrid<UserInfo> dataGridFor(Map<String, Object> map);

	boolean deleteAll(Map<String, Object> map);

	String export(Map<String, Object> map) throws IOException;

	List<UserInfo> selectByAccountNum(Map<String, Object> map);

	/**
	 * 轮询，更新用户手机归属地
	 */
	void updateMobileLocal();
	
	void sendBirthdaySms();
	
	List<UserInfo> selectByAccountNum2FullName(String accountNum);
	
	void updateFromUserProfile();
	
	void updateTwoWay();
}
