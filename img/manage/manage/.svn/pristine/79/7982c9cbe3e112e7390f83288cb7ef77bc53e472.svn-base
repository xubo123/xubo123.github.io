package com.hxy.core.department.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.department.dao.DepartmentMapper;
import com.hxy.core.department.entity.Department;
import com.hxy.core.department.entity.School;

@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper;

	@Override
	public School getCurrentSchool(String school_id) {
		return departmentMapper.getCurrentSchool(school_id);
	}

	@Override
	public DataGrid<Department> dataGrid(Map<String, Object> map) {
		DataGrid<Department> dataGrid = new DataGrid<Department>();
        long total = departmentMapper.count(map);
        dataGrid.setTotal(total);
        int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
        map.put("start", start);
        List<Department> list = departmentMapper.query(map);
        dataGrid.setRows(list);
        return dataGrid;
	}

	@Override
	public Department getById(long department_id) {
		return departmentMapper.getById(department_id);
	}

	@Override
	public void insert(Department department) {
		departmentMapper.insert(department);
	}

	@Override
	public void update(Department department) {
		departmentMapper.update(department);
	}

	@Override
	public void delete(long department_id) {
		departmentMapper.delete(department_id);
	}

	@Override
	public Department getByName(String name) {
		return departmentMapper.getByName(name);
	}

	@Override
	public boolean existClass(long department_id) {
		long count = departmentMapper.countClass(department_id);
		return count>0?true:false;
	}

	@Override
	public List<Department> getCollegeDepartment() {
		return departmentMapper.getCollegeDepartment();
	}

	@Override
	public List<Department> getAlumniDepartment() {
		return departmentMapper.getAlumniDepartment();
	}

	
	


}
