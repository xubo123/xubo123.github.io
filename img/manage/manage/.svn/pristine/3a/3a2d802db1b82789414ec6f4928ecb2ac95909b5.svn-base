package com.hxy.core.userbaseinfo.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.userbaseinfo.entity.UserBaseInfo;

public interface UserBaseInfoMapper {
	
	List<UserBaseInfo> query(Map<String, Object> map);
	
	long count(Map<String, Object> map);
	
	void save(UserBaseInfo userBaseInfo);
	
	void update(UserBaseInfo userBaseInfo);
	
	void delete(List<String> list);
	
	void deleteQuery(Map<String, Object> map);
	
	UserBaseInfo getById(long user_id);
	
	List<UserBaseInfo> getByClass(long class_id);
	
	List<UserBaseInfo> querySum(Map<String, Object> map);
	
	List<UserBaseInfo> querySumForAlumni(Map<String, Object> map);
	
	long countSum(Map<String, Object> map);
	
	List<UserBaseInfo> getSumByIds(List<String> list);
	
	List<UserBaseInfo> getAlumniInfoByIds(List<String> list);
	
	
	
	
	
	long countByDeptIdForImport(Map<String, Object> map);

	List<UserBaseInfo> selectByDeptIdForImport(Map<String, Object> map);
	
	List<UserBaseInfo> selectByDeptIdForImportAll(Map<String, Object> map);

	

	

	
	
	void updateOthers(UserBaseInfo userInfo);

	List<UserBaseInfo> selectAllByDeptId(String deptId);



	List<UserBaseInfo> selectUserToGetTel();

	List<UserBaseInfo> selectUserByClassIdAndName(Map<String, Object> map);

	void updateUserAccountNum(Map<String, Object> map);

	UserBaseInfo selectAllProByUserId(Map<String, Object> map);

	List<UserBaseInfo> selectAllUserByClassId(Map<String, Object> map);

	List<UserBaseInfo> selectCard(Map<String, Object> map);

	List<UserBaseInfo> selectByUserName(Map<String, Object> map);

	long selectUserInClass(Map<String, Object> map);

	UserBaseInfo selectUserInfo(Map<String, Object> map);

	//int updateTelId(Map<String, Object> map);

	List<UserBaseInfo> selectAll();

	List<UserBaseInfo> selectListFor(Map<String, Object> map);

	long countFor(Map<String, Object> map);
	
	List<UserBaseInfo> selectByUserIds(List<String> list);
	
	void updateImport(UserBaseInfo userBaseInfo);
	
	List<UserBaseInfo> selectByDeptIdAll(Map<String, Object> map);
	

	
	List<UserBaseInfo> selectByDeptIdForExport(Map<String, Object> map);
	
	List<UserBaseInfo> selectByMobile();
	
	//List<UserBaseInfo> searchDeptId(Map<String,Object> map);
	
	void updateMobile(UserBaseInfo userBaseInfo);

	void updateRelationship(Map<String,Object> map);

	List<String> selectEmailbyId(List<String> ids);

	List<String> selectTelbyId(List<String> list);

	long countSumForAlumni(Map<String, Object> map);

	List<UserBaseInfo> getPhoneOrEmailList(Map<String, Object> map);
}
