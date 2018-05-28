package com.hxy.core.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.project.dao.ProjectMapper;
import com.hxy.core.project.entity.Item;
import com.hxy.core.project.entity.Project;
import com.hxy.core.project.entity.ProjectItem;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private ProjectMapper projectMapper;

	public void save(Project project) {
		projectMapper.save(project);
	}

	public void update(Project project) {
		projectMapper.update(project);
	}

	public void delete(String ids) {
		List<Long> list = new ArrayList<Long>();
		String[] idArray = ids.split(",");
		if (idArray != null) {
			for (String id : idArray) {
				list.add(Long.parseLong(id));
			}
		}
		projectMapper.delete(list);
	}

	public DataGrid<Project> dataGrid(Map<String, Object> map) {
		DataGrid<Project> dataGrid = new DataGrid<Project>();
		long total = projectMapper.countProject(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<Project> list = projectMapper.selectList(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public Project selectById(long id) {
		return projectMapper.selectById(id);
	}

	public Project selectByProjectName(String projectName) {
		return projectMapper.selectByProjectName(projectName);
	}

	public Project selectByProjectNameAndProjectId(Project project) {
		return projectMapper.selectByProjectNameAndProjectId(project);
	}

	public List<Project> selectAll() {
		return projectMapper.selectAll();
	}

	@Override
	public List<Project> selectTop6() {
		return projectMapper.selectTop6();
	}

	@Override
	public List<Project> selectMore(int page, int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		int start = (page - 1) * rows;
		map.put("start", start);
		return projectMapper.selectMore(map);
	}

	@Override
	public long selectTotalCount() {
		return projectMapper.selectTotalCount();
	}

	@Override
	public ProjectItem listAll(int page, int rows, String accountNum) {
		long totalCount = projectMapper.selectTotalCount();
		ProjectItem projectItem = new ProjectItem();
		projectItem.setCountDonateItem(totalCount);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		int start = (page - 1) * rows;
		map.put("start", start);
		List<Project> list = projectMapper.selectMore(map);
		List<Item> items = new ArrayList<Item>();
		for (Project project : list) {
			Item item = new Item();
			String proPic = project.getProjectPic().substring(0, project.getProjectPic().lastIndexOf(".")) + "_MIN"
					+ project.getProjectPic().substring(project.getProjectPic().lastIndexOf("."));
			item.setProjectPic(proPic);
			item.setProjectName(project.getProjectName());
			item.setIntroduction(project.getIntroduction());
			item.setDonateItemUrl("../../project/projectAction!doNotNeedSessionAndSecurity_getById.action?id=" + project.getProjectId() + "&accountNum="
					+ accountNum);
			items.add(item);
		}
		projectItem.setDonateItemList(items);
		return projectItem;
	}
}
