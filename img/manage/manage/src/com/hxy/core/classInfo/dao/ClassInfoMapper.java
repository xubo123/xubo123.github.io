package com.hxy.core.classInfo.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.classInfo.entity.ClassInfo;
import com.hxy.core.classInfo.entity.Linker;

public interface ClassInfoMapper {
	
	List<ClassInfo> query(Map<String, Object> map);
	
	long count(Map<String, Object> map);

	ClassInfo getById(long class_id);
	
	ClassInfo getByName(ClassInfo classInfo);
	
	long countStudent(long class_id);

    void insert(ClassInfo classInfo);

    void update(ClassInfo classInfo);
    
    void delete(long class_id);
    
    List<ClassInfo> queryMajor(Map<String, Object> map);
	
	long countMajor(Map<String, Object> map);
	
	/**
	 * key: schoolName <br>
	 * key: college <br>
	 * key: major <br>
	 * key: department_id <br>
	 */
	void updateAffiliation(Map<String, Object> map);
	/**
	 * key: schoolName <br>
	 * key: college <br>
	 * key: major <br>
	 * key: department_id <br>
	 */
	void insertAppuserGroupByMajor(Map<String, Object> map);
	/**
	 * key: schoolName <br>
	 * key: college <br>
	 * key: major <br>
	 * key: department_id <br>
	 */
	void deleteAppuserGroupByMajor(Map<String, Object> map);
	
	
	List<Linker> getSchools();
	/**
	 * key: schoolName <br>
	 */
	List<Linker> getColleges(Map<String, Object> map);
	/**
	 * key: schoolName <br>
	 * key: college <br>
	 */
	List<Linker> getMajors(Map<String, Object> map);
	/**
	 * key: schoolName <br>
	 * key: college <br>
	 * key: major <br>
	 */
	List<Linker> getGrades(Map<String, Object> map);
	/**
	 * key: schoolName <br>
	 * key: college <br>
	 * key: major <br>
	 * key: grade <br>
	 */
	List<Linker> getClasses(Map<String, Object> map);
	
	List<ClassInfo> getAllClass();
}
