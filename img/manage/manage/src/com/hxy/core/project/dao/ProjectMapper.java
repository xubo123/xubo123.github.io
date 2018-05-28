package com.hxy.core.project.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.project.entity.Project;

public interface ProjectMapper {
	void save(Project project);
	
	List<Project> selectList(Map<String, Object> map);
	
	long countProject(Map<String, Object> map);
	
	Project selectByProjectName(String projectName);
	
	Project selectById(long id);
	
	void delete(List<Long> list);
	
	Project selectByProjectNameAndProjectId(Project project);
	
	void update(Project project);
	
	List<Project> selectAll();
	
	void updateDonationMoney(Project project);
	
	List<Project> selectTop6();
	
	List<Project> selectMore(Map<String, Object> map);
	
	long selectTotalCount();
	
}
