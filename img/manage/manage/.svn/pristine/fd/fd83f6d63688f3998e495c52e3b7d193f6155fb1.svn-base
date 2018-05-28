package com.hxy.core.dept.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.dept.entity.Department;
import com.hxy.core.dept.entity.Dept;
import com.hxy.core.dept.entity.Dept_New;
import com.hxy.core.dept.entity.NewDeptInfo;
import com.hxy.core.dept.entity.School;
import com.hxy.core.user.entity.User;

public interface DeptService {
	List<Dept> selectAll(List<Dept> list);
	
	List<Dept> selectDepart(List<Dept> list);
	
	List<Dept> selectAll1(List<Dept> list);
	
	void insert(Dept dept);
	
	void insertAlias(Dept dept);
	
	Dept checkDeptId(String deptId);
	
	void delete(String deptId);
	
	String importData(String url,User user,HashMap<String, String> affiliation);
	
	List<Dept> getSchool();
	
	List<Dept> getDepart();
	
	List<Dept> getDepart1();
	/**
	 * 获取所有当前院系
	 * @return
	 */
	List<Department> getCollegeDepartment();
	/**
	 * 获取所有非当前院系
	 * @return
	 */
	
	List<Department> getNotCollegeDepartment();
	
	List<Dept> selectAllClass(String deptId);
	
	DataGrid<Dept> dateGridForUser(Map<String, Object> map);
	
	Dept selectByNameAndParentId(Map<String, Object> map);
	
	List<Dept> getByParentId(String deptId);
	
	List<Dept> getByParentIdAndDeptIds(Map<String, Object> map);
	
	Dept getById(String classId);
	
	Department getById_new(long classId);
	
	void update(Dept dept);
	
	void updateBelong(long department_id,long classId);
	
	List<Dept> getBelong(String deptId);
	
	List<Department> getBelong_new(String classId);
	
	Dept getByAliasName(String deptId);
	
	List<Dept> selectByDeptIds(List<String> list);

	List<Dept_New> selectAlldept(String school,List<Department> departments);

	School getSchool1();

	List<Dept_New> selectAllmajor();

	List<Dept_New> selectAllclass();

	List<Dept_New> selectAllgrade();

	NewDeptInfo selectByNameAndAffiliation(Map<String, Object> map);

	void insert_new(NewDeptInfo newDeptInfo);

	List<Dept_New> selectmajorBydeptName(String deptName);

	List<Dept_New> selectgradeBydeptName(String deptName);

	List<Dept_New> selectclassBydeptName(HashMap<String, String> map);

	long searchAffiliation(String string);

	String importDepartment(String url, User user);

	String exportData() throws IOException;

	String exportDept() throws IOException;
	
}
