package com.hxy.core.userProfile.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.userProfile.entity.UserProfile;

public interface UserProfileMapper {
	long countUserProfile(Map<String, Object> map);

	long countUserProfileByBaseInfoId(Map<String, Object> map);

	List<UserProfile> selectUserProfileList(Map<String, Object> map);

	List<UserProfile> selectUserByName(Map<String, Object> map);

	/** --新增模糊查询-- **/
	List<UserProfile> selectUserByQuery(Map<String, Object> map);

	List<UserProfile> selectAll();

	int save(UserProfile userProfile);

	UserProfile selectById(String id);

	void delete(List<Long> list);

	void deleteByAccountNum(Map<String, Object> map);

	void update(UserProfile userProfile);
	
	void updateGps(UserProfile userProfile);

	void updatePhoto(UserProfile userProfile);

	void updateAuthenticated(Map<String, Object> map);

	UserProfile selectByAccountNum(String accountNum);

	List<UserProfile> selectGroupInfoByAccountNumList(List<String> accountList);

	UserProfile selectByBaseInfoId(String baseInfoId);

	UserProfile selectByPhoneNum(String phoneNum);

	long countByPhoneNum(Map<String, Object> map);

	void clearBaseInfoId(Map<String, Object> map);

	void updateBase(UserProfile userProfile);

	List<UserProfile> selectNearPeople(Map<String, Object> map);

	void updatePassword(UserProfile userProfile);

	List<UserProfile> selectByAccountNums(List<Long> list);
	
	void updateGroup(UserProfile userProfile);
	
	List<UserProfile> getUserProfileByGroupId(String groupId);
	
	List<UserProfile> selectAlumni(Map<String, Object> map);

	long[] searchDeptId(Map<String, String> map);
}
