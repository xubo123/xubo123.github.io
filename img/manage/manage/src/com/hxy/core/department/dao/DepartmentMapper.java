package com.hxy.core.department.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.department.entity.Department;
import com.hxy.core.department.entity.School;

public interface DepartmentMapper {
	
	School getCurrentSchool(String school_id);
	
	List<Department> getCollegeDepartment();
	
	List<Department> getAlumniDepartment();
	
	List<Department> query(Map<String, Object> map);
	
	long count(Map<String, Object> map);

	Department getById(long department_id);
	
	Department getByName(String name);
	
	long countClass(long department_id);

    void insert(Department department);

    void update(Department department);
   
    void delete(long department_id);
}
