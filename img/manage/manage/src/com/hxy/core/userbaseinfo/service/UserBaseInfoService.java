package com.hxy.core.userbaseinfo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.user.entity.User;
import com.hxy.core.userbaseinfo.entity.UserBaseInfo;

/**
 * @author 张东跃
 *
 */
public interface UserBaseInfoService {
	
	DataGrid<UserBaseInfo> dataGrid(Map<String, Object> map);
	
	UserBaseInfo getById(long user_id);
	
	void save(UserBaseInfo userBaseInfo);
	
	void update(UserBaseInfo userBaseInfo);
	
	void delete(String ids);
	
	/**
	 * 按搜索条件删除
	 */
	void deleteQuery(Map<String, Object> map);


	/**
	 * 获取某班级的学生
	 */
	List<UserBaseInfo> getByClass(long class_id);
	
	/**
	 * 校友总汇
	 */
	DataGrid<UserBaseInfo> dataGridSum(Map<String, Object> map);
	/**
	 * 校友会名单总汇
	 */
	DataGrid<UserBaseInfo> dataGridSumForAlumni(Map<String, Object> map);
	/**
	 * 查看校友总汇里的校友
	 * @param user_ids  多个user_id以逗号隔开
	 * @return
	 */
	List<UserBaseInfo> getSumByIds(String user_ids);
	/**
	 * 查看校友会里的校友
	 * @param user_ids  多个user_id以逗号隔开
	 * @return
	 */
	List<UserBaseInfo> getAlumniInfoByIds(String user_ids);
	/**
	 * 根据条件获取校友会里的校友
	 * @return
	 */
	List<UserBaseInfo> getAlumniInfo(Map<String, Object> map);
	
	/**
	 * excel导入学生
	 */
	String importData(String url, User user);
	
	/**
	 * 按搜索条件导出
	 */
	String export(Map<String, Object> map) throws IOException;
	

	
	
	
	
	
	
	DataGrid<UserBaseInfo> selectByDeptIdForImport(Map<String, Object> map);



	List<UserBaseInfo> selectAllUserList();

	List<UserBaseInfo> selectUserByClassIdAndName(String userName, String classId);

	void updateUserAccountNum(UserBaseInfo UserBaseInfo);

	UserBaseInfo selectAllProByUserId(String userId);

	List<UserBaseInfo> selectUserByClassId(String classId);

	UserBaseInfo selectUserBaseInfoByGmidAndName(Map<String, Object> map);

	//int updateUserTelId(Map<String, Object> map, UserBaseInfo UserBaseInfo);

	boolean selectUserInClass(String classId, List<String> list);

	List<UserBaseInfo> selectByUserName(String userName, String deptId);

	List<UserBaseInfo> selectCard(List<String> list);

	DataGrid<UserBaseInfo> dataGridFor(Map<String, Object> map);



	void modifyClass(long userId, long classId);//群组关系表更新

	List<String> selectEmailbyId(List<String> ids);

	List<String> selectTelbyId(List<String> list);

}