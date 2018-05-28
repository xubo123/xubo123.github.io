package com.hxy.core.department.service;

import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.department.entity.Department;
import com.hxy.core.department.entity.School;

public interface DepartmentService {
	
	School getCurrentSchool(String school_id);
	
	/**
	 * 获取所有当前院系
	 * @return
	 */
	List<Department> getCollegeDepartment();
	
	/**
	 * 获取所有非当前院系
	 * @return
	 */
	List<Department> getAlumniDepartment();
	
	/**
	 * 获取机构列表<br>
	 * key:  type<br>
	 * key:  college=[0  非院系机构  ,1  院系机构]
	 */
	DataGrid<Department> dataGrid(Map<String, Object> map);

	Department getById(long department_id);
	
	/**
	 * 用于验证是否重名
	 */
	Department getByName(String name);

	/**
	 * 检查某院系下是否有隶属的班级
	 */
	boolean existClass(long department_id);
	
    void insert(Department department);

    void update(Department department);
   
    void delete(long department_id);
   
}
