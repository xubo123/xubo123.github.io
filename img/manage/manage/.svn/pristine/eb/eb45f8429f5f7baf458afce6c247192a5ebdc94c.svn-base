package com.hxy.core.userinfo.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hxy.core.dept.entity.NewDeptInfo;
import com.hxy.core.userbaseinfo.entity.UserBaseInfo;
import com.hxy.core.userinfo.entity.UserInfo;

public interface UserInfoMapper {
	long countByDeptId(Map<String, Object> map);

	List<UserInfo> selectByDeptId(Map<String, Object> map);

	long countByDeptIdForAlumni(Map<String, Object> map);

	List<UserInfo> selectByDeptIdForAlumni(Map<String, Object> map);
	
	List<UserInfo> selectUserInfoByName(String name);

	void delete(List<String> list);

	void save(UserInfo userInfo);

	void update(UserInfo userInfo);

	void updateOthers(UserInfo userInfo);

	void updateIdea(UserInfo userInfo);

	/** --根据accountNum更新-- **/
	void updateByAccountNum(UserInfo userInfo);

	List<UserInfo> selectAllByDeptId(String deptId);

	UserInfo selectByUserId(String userId);

	UserInfo selectByUserIdLess(String userId);

	List<UserInfo> selectUserToGetTel();

	/** --查询所有有手机号的用户集合,分页-- **/
	List<UserInfo> selectUserToGetTelPage(Map<String, Object> map);

	/** --查询所有有手机号的用户数量-- **/
	long selectUserToGetTelCount(Map<String, Object> map);

	List<UserInfo> selectUserByClassIdAndName(Map<String, Object> map);

	List<UserInfo> selectUserByClassId(String classId);

	void updateUserAccountNum(Map<String, Object> map);

	UserInfo selectAllProByUserId(Map<String, Object> map);

	List<UserInfo> selectAllUserByClassId(Map<String, Object> map);

	List<UserInfo> selectCard(Map<String, Object> map);

	List<UserInfo> selectByUserName(Map<String, Object> map);

	long selectUserInClass(Map<String, Object> map);

	UserInfo selectUserInfo(Map<String, Object> map);

	int updateTelId(Map<String, Object> map);

	List<UserInfo> selectAll();

	List<UserInfo> selectListFor(Map<String, Object> map);

	long countFor(Map<String, Object> map);

	void deleteByDeptIdAll(Map<String, Object> map);

	List<UserInfo> selectByDeptIdForExport(Map<String, Object> map);

	List<UserInfo> getUserInfoByUserIdForAlumni(List<String> list);

	List<UserInfo> selectByAccountNum(Map<String, Object> map);

	List<UserInfo> selectByAccountNums(List<String> list);

	List<UserInfo> selectByAccountNum2FullName(String accountNum);

	/**
	 * 查询手机号码不为空and归属地为空的学生记录
	 * 
	 * @return
	 */
	List<UserInfo> selectMobileLocalIsNull();

	/**
	 * 更新手机号码归属地
	 * 
	 * @param userInfo
	 */
	void updateMobileLocal(UserInfo userInfo);

	/**
	 * 
	 * @param userInfo
	 */
	void updateBase(UserInfo userInfo);

	/**
	 * 通过多个用户ID查询多个用户
	 * 
	 * @param list
	 * @return
	 */
	List<UserInfo> selectByIds(List<String> list);

	List<UserInfo> selectByDeptIdAll(Map<String, Object> map);

	/**
	 * 查询今天过生日的同学
	 * 
	 * @return
	 */
	List<UserInfo> selectBirthday();

	List<UserInfo> selectByName(String name);

	/**
	 * 查询校友
	 * 
	 * @param map
	 * @return
	 */
	List<UserInfo> selectAlumni(Map<String, Object> map);

	/**
	 * 更新个人头像
	 * 
	 * @param map
	 */
	void updatePhoto(Map<String, Object> map);

	/**
	 * 认证通过后信息同步
	 * 
	 */
	void updateAuthen2User(UserInfo userInfo);

	void updateAccountNum(UserInfo userInfo);

	void updateFromUserProfile(UserInfo userInfo);

	List<UserInfo> selectUserByClassIdLess(String classId);
	
	List<UserInfo> selectByMobile();
	
	void updateMobile(UserInfo userInfo);
	
}
