package com.hxy.core.project.service;

import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.project.entity.Project;
import com.hxy.core.project.entity.ProjectItem;

public interface ProjectService {
	void save(Project project);
	
	void update(Project project);
	
	void delete(String ids);
	
	DataGrid<Project> dataGrid(Map<String, Object> map);
	
	Project selectById(long id);
	
	List<Project> selectAll();
	
	List<Project> selectTop6();
	
	List<Project> selectMore(int page,int rows);
	
	long selectTotalCount();
	
	Project selectByProjectName(String projectName);
	
	Project selectByProjectNameAndProjectId(Project project);
	
	ProjectItem listAll(int page,int rows,String accountNum);
}
