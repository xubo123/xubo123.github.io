package com.hxy.core.classInfo.service;

import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.classInfo.entity.ClassInfo;
import com.hxy.core.classInfo.entity.Linker;

public interface ClassInfoService {
	
	/**
	 * 获取班级列表<br>
	 * key:  schoolName<br>
	 * key:  college<br>
	 * key:  major<br>
	 * key:  grade<br>
	 * key:  affiliation
	 */
	DataGrid<ClassInfo> dataGrid(Map<String, Object> map);

	ClassInfo getById(long class_id);
	
	/**
	 * 用于验证是否重名<br>
	 * 参数里学校，院系，专业，年级，班级全部要附值
	 */
	ClassInfo getByName(ClassInfo classInfo);
	
	/**
	 * 检查某班级下是否有隶属的学生
	 */
	boolean existStudent(long class_id);

    void insert(ClassInfo classInfo);

    void update(ClassInfo classInfo);
   
    void delete(long class_id);
    
    
    /**
	 * 获取专业列表及其隶属当前院系<br>
	 * 以专业为单位进行院系挂接<br>
	 * key : affiliated [-1 未挂接,1 已挂接]
	 */
	DataGrid<ClassInfo> dataGridMajor(Map<String, Object> map);
	
	
	/**
	 * 更新隶属当前院系<br>
	 * <b>将更新该班级所属专业下的所有班级</b>
	 */
	void updateAffiliation(long class_id, long department_id);
	
	/**
	 * 新增注册用户与群组板块关联<br>
	 * <b>将更新该班级所属专业下的所有学生</b>
	 */
	void insertAppuserGroupByMajor(long class_id, long department_id);
	/**
	 * 删除注册用户与群组板块关联<br>
	 * <b>将更新该班级所属专业下的所有学生</b>
	 */
	void deleteAppuserGroupByMajor(long class_id, long department_id);
	
	
	List<Linker> getSchools();
	List<Linker> getColleges(String schoolName);
	List<Linker> getMajors(String schoolName, String college);
	List<Linker> getGrades(String schoolName, String college, String major);
	List<Linker> getClasses(String schoolName, String college, String major, String grade);
	
	
	List<ClassInfo> getAllClass();
}
