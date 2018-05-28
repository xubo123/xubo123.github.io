package com.hxy.core.classInfo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.classInfo.dao.ClassInfoMapper;
import com.hxy.core.classInfo.entity.ClassInfo;
import com.hxy.core.classInfo.entity.Linker;

@Service("classInfoService")
public class ClassInfoServiceImpl implements ClassInfoService {

	@Autowired
	private ClassInfoMapper classInfoMapper;

	@Override
	public DataGrid<ClassInfo> dataGrid(Map<String, Object> map) {
		DataGrid<ClassInfo> dataGrid = new DataGrid<ClassInfo>();
        long total = classInfoMapper.count(map);
        dataGrid.setTotal(total);
        int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
        map.put("start", start);
        List<ClassInfo> list = classInfoMapper.query(map);
        dataGrid.setRows(list);
        return dataGrid;
	}

	@Override
	public ClassInfo getById(long class_id) {
		return classInfoMapper.getById(class_id);
	}

	@Override
	public ClassInfo getByName(ClassInfo classInfo) {
		return classInfoMapper.getByName(classInfo);
	}

	@Override
	public void insert(ClassInfo classInfo) {
		classInfoMapper.insert(classInfo);
	}

	@Override
	public void update(ClassInfo classInfo) {
		classInfoMapper.update(classInfo);
	}

	@Override
	public void delete(long class_id) {
		classInfoMapper.delete(class_id);
	}

	@Override
	public boolean existStudent(long class_id) {
		long count = classInfoMapper.countStudent(class_id);
		return count>0?true:false;
	}

	@Override
	public DataGrid<ClassInfo> dataGridMajor(Map<String, Object> map) {
		DataGrid<ClassInfo> dataGrid = new DataGrid<ClassInfo>();
        long total = classInfoMapper.countMajor(map);
        dataGrid.setTotal(total);
        int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
        map.put("start", start);
        List<ClassInfo> list = classInfoMapper.queryMajor(map);
        dataGrid.setRows(list);
        return dataGrid;
	}

	@Override
	public void updateAffiliation(long class_id, long department_id) {
		ClassInfo c = classInfoMapper.getById(class_id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolName", c.getSchoolName());
		map.put("college", c.getCollege());
		map.put("major", c.getMajor());
		map.put("department_id", department_id);
		classInfoMapper.updateAffiliation(map);
	}
	@Override
	public void insertAppuserGroupByMajor(long class_id, long department_id) {
		ClassInfo c = classInfoMapper.getById(class_id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolName", c.getSchoolName());
		map.put("college", c.getCollege());
		map.put("major", c.getMajor());
		map.put("department_id", department_id);
		classInfoMapper.insertAppuserGroupByMajor(map);
	}
	@Override
	public void deleteAppuserGroupByMajor(long class_id, long department_id) {
		ClassInfo c = classInfoMapper.getById(class_id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolName", c.getSchoolName());
		map.put("college", c.getCollege());
		map.put("major", c.getMajor());
		map.put("department_id", department_id);
		classInfoMapper.deleteAppuserGroupByMajor(map);
	}

	@Override
	public List<Linker> getSchools() {
		return classInfoMapper.getSchools();
	}

	@Override
	public List<Linker> getColleges(String schoolName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolName", schoolName);
		return classInfoMapper.getColleges(map);
	}

	@Override
	public List<Linker> getMajors(String schoolName, String college) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolName", schoolName);
		map.put("college", college);
		return classInfoMapper.getMajors(map);
	}

	@Override
	public List<Linker> getGrades(String schoolName, String college, String major) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolName", schoolName);
		map.put("college", college);
		map.put("major", major);
		return classInfoMapper.getGrades(map);
	}

	@Override
	public List<Linker> getClasses(String schoolName, String college, String major, String grade) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolName", schoolName);
		map.put("college", college);
		map.put("major", major);
		map.put("grade", grade);
		return classInfoMapper.getClasses(map);
	}

	@Override
	public List<ClassInfo> getAllClass() {
		return classInfoMapper.getAllClass();
	}

	
	


}
