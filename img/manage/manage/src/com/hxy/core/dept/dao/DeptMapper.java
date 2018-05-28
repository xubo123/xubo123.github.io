package com.hxy.core.dept.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hxy.core.dept.entity.Department;
import com.hxy.core.dept.entity.Dept;
import com.hxy.core.dept.entity.Dept_New;
import com.hxy.core.dept.entity.NewDeptInfo;
import com.hxy.core.dept.entity.School;

public interface DeptMapper {
	List<Dept> selectAll(List<Dept> list);
	
	List<Dept> selectDepart(List<Dept> list);
	
	List<Dept> selectAll1(List<Dept> list);
	
	List<Dept> selectAll2();

	void insert(Dept dept);

	Dept checkDeptId(String deptId);

	Dept selectOne(String deptId);

	List<Dept> selectByDeptId(String deptId);

	void delete(List<String> list);

	List<Dept> selectByName(String deptName);

	Dept selectByNameAndParentId(Map<String, Object> map);

	List<Dept> getSchool();

	List<Dept> selectAllClass(String deptId);

	long countDept(Map<String, Object> map);

	List<Dept> selectDeptList(Map<String, Object> map);

	List<Dept> getDepart();

	List<Dept> getDepart1();

	List<Dept> getDepart2(String parentId);
	
	List<Dept> getByParentId(String deptId);

	Dept getById(String classId);
	
	Department getById_new(long classId);

	void update(Dept dept);
	
	void updateBelong(Map<String,Long> map);
	
	void updateAliasName(String aliasName);

	void updateFullName(Dept dept);
	
	List<Dept> getBelong1(String deptId);
	
	List<Dept> getBelong2(String deptId);
	
	List<Department> getBelong_new(String classId);
	
	Dept getByAliasName(String deptId);
	
	List<Dept> selectByDeptIds(List<String> list);
	
	List<Dept> getByParentIdAndDeptIds(Map<String, Object> map);

	List<Dept_New> selectAlldept(Map<String,Object> map);

	List<Dept_New> selectAllmajor();

	List<Dept_New> selectAllclass();
	
	School getSchool1();

	List<Dept_New> selectAllgrade();

	NewDeptInfo selectByNameAndAffiliation(Map<String, Object> map);

	void insert_new(NewDeptInfo newDeptInfo);

	List<NewDeptInfo> getAllDeptInfo();

	List<Dept_New> selectmajorBydeptName(String deptName);

	List<Dept_New> selectgradeBydeptName(String deptName);

	List<Dept_New> selectclassBydeptName(HashMap<String, String> map);

	long searchAffiliation(String affiliationName);

	Department checkDepartment(Map<String, Object> map);

	void insert_department(Department department);

	NewDeptInfo selectDeptByDeptinfo(HashMap<String, String> map);

	List<NewDeptInfo> selectAllForExport();

	List<Department> getCollegeDepartment();//获取所有当前院系
	
	List<Department> getNotCollegeDepartment();//获取所有非当前院系
}
