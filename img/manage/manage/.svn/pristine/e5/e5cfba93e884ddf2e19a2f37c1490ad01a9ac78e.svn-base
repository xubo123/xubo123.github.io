package com.hxy.core.project.action;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.project.entity.Project;
import com.hxy.core.project.service.ProjectService;

@Namespace("/project")
@Action(value = "projectAction", results = { @Result(name = "donateDetail", location = "/mobile/donate/donateDetail.jsp"),
		@Result(name = "donateForm", location = "/mobile/donate/donateForm.jsp") })
public class ProjectAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ProjectAction.class);

	@Autowired
	private ProjectService projectService;

	private Project project;

	private String projectName;

	private String accountNum;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void dataGrid() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		map.put("page", page);
		map.put("projectName", projectName);
		super.writeJson(projectService.dataGrid(map));
	}

	public void save() {
		Message message = new Message();
		try {
			// 检查捐赠项目是否重复
			Project checkProject = projectService.selectByProjectName(project.getProjectName());
			if (checkProject == null) {
				project.setCreateTime(new Date());
				if (getUser().getUserId() != 0) {
					project.setCreateId(getUser().getUserId());
				}
				projectService.save(project);
				message.setMsg("新增成功");
				message.setSuccess(true);
			} else {
				message.setMsg("新增失败,捐赠项目重复");
				message.setSuccess(false);
			}
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("新增失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void getById() {
		super.writeJson(projectService.selectById(id));
	}

	public void doNotNeedSecurity_getById() {
		super.writeJson(projectService.selectById(id));
	}

	public void delete() {
		Message message = new Message();
		try {
			projectService.delete(ids);
			message.setMsg("删除成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void update() {
		Message message = new Message();
		try {
			// 检查捐赠项目是否重复
			Project checkProject = projectService.selectByProjectNameAndProjectId(project);
			if (checkProject == null) {
				projectService.update(project);
				message.setMsg("修改成功");
				message.setSuccess(true);
			} else {
				message.setMsg("修改失败,捐赠项目重复");
				message.setSuccess(false);
			}
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("修改失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void doNotNeedSecurity_getAll() {
		super.writeJson(projectService.selectAll());
	}

	public void doNotNeedSessionAndSecurity_getAll() {
		super.writeJson(projectService.selectAll());
	}

	public String doNotNeedSessionAndSecurity_getById() {
		project = projectService.selectById(id);
		return "donateDetail";
	}

	public String doNotNeedSessionAndSecurity_getByIdForm() {
		project = projectService.selectById(id);
		return "donateForm";
	}

	public void doNotNeedSessionAndSecurity_listAll() {
		super.writeJson(projectService.listAll(page, rows,accountNum));
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

}
