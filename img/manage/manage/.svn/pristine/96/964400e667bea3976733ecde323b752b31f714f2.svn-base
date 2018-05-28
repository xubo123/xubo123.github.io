package com.hxy.core.department.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.base.entity.TreeString;
import com.hxy.core.channel.entity.NewsTag;
import com.hxy.core.channel.service.NewsChannelService;
import com.hxy.core.community.entity.GroupBoard;
import com.hxy.core.community.service.CommunityService;
import com.hxy.core.department.entity.Department;
import com.hxy.core.department.entity.School;
import com.hxy.core.department.service.DepartmentService;
import com.hxy.system.Global;

@Namespace("/department")
@Action(value = "departmentAction")
public class DepartmentAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DepartmentAction.class);

	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private CommunityService communityService;
	
	@Autowired
	private NewsChannelService newsChannelService;

	private Department department;

	public void getCollegeList() {
		String name = getRequest().getParameter("name");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("rows", rows);
        map.put("name", name);
        map.put("college", "1");
        super.writeJson(departmentService.dataGrid(map));
    }
	
	public void getAlumniList() {
		String name = getRequest().getParameter("name");
		String type = getRequest().getParameter("type");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("rows", rows);
        map.put("college", "0");
        map.put("name", name);
        map.put("type", type);
        super.writeJson(departmentService.dataGrid(map));
    }
	
	public void getByCollegeId() {
		getById();
	}
	
	public void getByAlumniId() {
		getById();
	}
	
	private void getById() {
    	super.writeJson(departmentService.getById(department.getDepartment_id()));
    }
	
	public void insertCollege() {
		insert();
	}
	
	public void insertAlumni() {
		insert();
	}
	
	private void insert() {
        Message message = new Message();
        try {
        	//检查是否含有重名的机构
        	department.setDepartmentName(department.getDepartmentName().trim());
        	Department tmp = departmentService.getByName(department.getDepartmentName());
        	
        	if (tmp == null) {
        		School school = departmentService.getCurrentSchool(Global.schoolId);
            	department.setSchoolName(school.getSchool_name());
            	department.setCreateAccount(getUser().getUserId());
            	departmentService.insert(department);
            	
            	//同时新增对应的社区板块
            	GroupBoard groupBoard = new GroupBoard();
            	groupBoard.setBoard_name(department.getDepartmentName());
            	groupBoard.setBoard_type(0);
            	groupBoard.setOid(department.getDepartment_id());
            	communityService.insertGroupBoard(groupBoard);
            	
            	//同时新增对应的新闻频道
            	NewsTag newsTag = new NewsTag();
            	newsTag.setTagName(department.getDepartmentName());
            	newsTag.setTagRemark(department.getDepartmentName());
            	newsTag.setDepartment_id(department.getDepartment_id());
            	newsTag.setTag(java.util.UUID.randomUUID().toString());
            	newsChannelService.save(newsTag);
            	
				message.setMsg("新增成功");
				message.setSuccess(true);
			} else {
				message.setMsg("新增失败,名称重复");
				message.setSuccess(false);
			}
        } catch (Exception e) {
            logger.error(e, e);
            message.setMsg("新增失败");
            message.setSuccess(false);
        }
        super.writeJson(message);
    }
	
	
	public void updateCollege() {
		update();
	}
	
	public void updateAlumni() {
		update();
	}
	
	private void update() {
        Message message = new Message();
        try {
        	//检查是否含有重名的机构
        	department.setDepartmentName(department.getDepartmentName().trim());
        	Department tmp = departmentService.getByName(department.getDepartmentName());
        	
        	if (tmp == null || department.getDepartment_id() == tmp.getDepartment_id()) {
            	departmentService.update(department);
				message.setMsg("修改成功");
				message.setSuccess(true);
			} else {
				message.setMsg("修改失败,名称重复");
				message.setSuccess(false);
			}
        } catch (Exception e) {
            logger.error(e, e);
            message.setMsg("修改失败");
            message.setSuccess(false);
        }
        super.writeJson(message);
    }
	
	
	public void deleteCollege() {
		delete();
	}
	
	public void deleteAlumni() {
		delete();
	}
	
	private void delete() {
        Message message = new Message();
        try {
        	//检查该机构下是否有隶属的班级
        	if (!departmentService.existClass(id)) {
            	departmentService.delete(id);
				message.setMsg("删除成功");
				message.setSuccess(true);
			} else {
				message.setMsg("删除失败,该院系下存在隶属的班级");
				message.setSuccess(false);
			}
        } catch (Exception e) {
            logger.error(e, e);
            message.setMsg("删除失败");
            message.setSuccess(false);
        }
        super.writeJson(message);
    }
	
	/**
	 * 获取所有当前院系，用于树<br>
	 */
	public void doNotNeedSecurity_getCollege2Tree() {
		List<Department> list = departmentService.getCollegeDepartment();
		List<TreeString> rootTrees = new ArrayList<TreeString>();

		if (list != null && list.size() > 0) {
			for (Department dept : list) {
				TreeString node = new TreeString();
				node.setId(Long.toString(dept.getDepartment_id()));
				node.setState("open");
				node.setPid("0");
				node.setText(dept.getDepartmentName());
				rootTrees.add(node);
			}
		}
		super.writeJson(rootTrees);
	}

	/**
	 * 获取所有当前院系的机构，用于下拉框
	 */
	public void doNotNeedSecurity_getCollege2ComboBox() {
		super.writeJson(departmentService.getCollegeDepartment());
	}
	/**
	 * 获取所有非当前院系的机构，用于下拉框
	 */
	public void doNotNeedSecurity_getAlumni2ComboBox() {
		super.writeJson(departmentService.getAlumniDepartment());
	}
	
	/**
	 * 获取用户所管的当前院系，用于下拉框
	 */
	public void doNotNeedSecurity_getUserDepts2ComboBox() {
		if(getUser().getRole().getSystemAdmin()==1) {
			super.writeJson(departmentService.getCollegeDepartment());
		} else {
			super.writeJson(getUser().getDepts());
		}
	}
	
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

}
