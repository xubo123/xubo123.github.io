package com.hxy.core.major.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.major.entity.Major;
import com.hxy.core.major.entity.MajorDept;

public interface MajorMapper {
	
	List<Major> query(Map<String, Object> map);

    long count(Map<String, Object> map);

    Major getById(long id);
    
    long countByName(String majorName);
    
    long countByIdName(Major major);
    
    Major getByName(String majorName);

    void addMajor(Major major);
    
    void addMajorDept(Map<String, Object> map);

    void updateMajor(Major major);

    void deleteMajor(List<Long> list);
    
    void deleteMajorDept(List<Long> list);
    
    void deleteMajorByDeptId(List<String> list);
    
    List<Major> getMajor(String deptId);
    
    List<Major> selectAll();
    
    List<MajorDept> getMajorAndDept(Map<String, Object> map);
    
    List<MajorDept> selectMajorAndDeptAll();
}
