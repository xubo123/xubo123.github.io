package com.hxy.core.dept.action;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.base.entity.TreeString;
import com.hxy.core.dept.entity.Department;
import com.hxy.core.dept.entity.Dept;
import com.hxy.core.dept.entity.Dept_New;
import com.hxy.core.dept.entity.NewDeptInfo;
import com.hxy.core.dept.entity.School;
import com.hxy.core.dept.service.DeptService;
import com.hxy.core.userbaseinfo.entity.UserBaseInfo;
import com.hxy.core.userbaseinfo.service.UserBaseInfoService;
import com.hxy.core.userinfo.entity.UserInfo;
import com.hxy.core.userinfo.service.UserInfoService;
import com.hxy.system.TreeStringUtil;

@Namespace("/dept")
@Action(value = "deptAction")
public class DeptAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DeptAction.class);

	@Autowired
	private DeptService deptService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private UserBaseInfoService userBaseInfoService;

	private Dept dept;

	private String deptId;

	private long classId;

	private String url;

	private NewDeptInfo newDeptInfo;

	private Department department;

	// ------------------------xubo---------------------------
	private String type;
	private String deptName;
	private String majorName;
	private String departName;
	private String schoolName;

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public NewDeptInfo getNewDeptInfo() {
		return newDeptInfo;
	}

	public void setNewDeptInfo(NewDeptInfo newDeptInfo) {
		this.newDeptInfo = newDeptInfo;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void getNewDeptTree() {
		// TO-FIX
/*
		// -------------------------xubo------------------------------------
		School school = deptService.getSchool1();// 获取根节点学校

		List<TreeString> treeList = new ArrayList<TreeString>();
		List<TreeString> rootTrees = new ArrayList<TreeString>();
		// 设置根节点学校
		TreeString node_root = new TreeString();
		node_root.setId(school.getSchoolName());
		node_root.setPid("0");
		node_root.setText(school.getSchoolName());
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("type", "school");
		node_root.setAttributes(attributes);
		rootTrees.add(node_root);
		treeList.add(node_root);
		List<Department> depts = getUser().getDepts();
		List<Dept_New> departmentlist = deptService.selectAlldept(
				school.getSchoolName(), depts);// 获取当前院系列表
		for (Dept_New department : departmentlist) {
			TreeString node_first = new TreeString();
			node_first.setId(department.getDeptName());
			node_first.setPid(department.getParentName());
			node_first.setText(department.getDeptName());
			Map<String, Object> attribute = new HashMap<String, Object>();
			attributes.put("type", "department");
			node_first.setAttributes(attribute);
			treeList.add(node_first);
		}
		List<Dept_New> majorlist = deptService.selectAllmajor();// 获取专业列表
		for (Dept_New major : majorlist) {
			TreeString node_seccond = new TreeString();
			node_seccond.setId(major.getDeptName());
			node_seccond.setPid(major.getParentName());
			node_seccond.setText(major.getDeptName());
			node_seccond.setState("closed");
			Map<String, Object> attribute = new HashMap<String, Object>();
			attribute.put("type", "major");
			node_seccond.setAttributes(attribute);
			treeList.add(node_seccond);
		}
		List<Dept_New> gradelist = deptService.selectAllgrade();// 获取年级列表
		for (Dept_New grade : gradelist) {
			TreeString node_third = new TreeString();
			node_third.setId(grade.getDeptName() + "/" + grade.getParentName());
			node_third.setPid(grade.getParentName());
			node_third.setText(grade.getDeptName());
			node_third.setState("closed");
			Map<String, Object> attribute = new HashMap<String, Object>();
			attribute.put("type", "grade");
			node_third.setAttributes(attribute);
			treeList.add(node_third);
		}
		List<Dept_New> classlist = deptService.selectAllclass();// 获取班级列表
		for (Dept_New class_dept : classlist) {
			TreeString node_fourth = new TreeString();
			node_fourth.setId(String.valueOf(class_dept.getDeptId()));
			node_fourth.setPid(class_dept.getParentName() + "/"
					+ class_dept.getGrandparentName());
			node_fourth.setText(class_dept.getDeptName());
			node_fourth.setState("closed");
			Map<String, Object> attribute = new HashMap<String, Object>();
			attribute.put("type", "class");
			node_fourth.setAttributes(attribute);
			treeList.add(node_fourth);
		}
		TreeStringUtil.parseTreeString(rootTrees, treeList);
		super.writeJson(rootTrees);
*/
	}

	public void getDeptTree() {
		// TO-FIX
		/*
		 * List<Dept> list = deptService.selectAll1(getUser().getDepts());
		 * List<TreeString> treeList = new ArrayList<TreeString>();
		 * List<TreeString> rootTrees = new ArrayList<TreeString>(); if
		 * (getUser().getDepts() != null && getUser().getDepts().size() > 0) {
		 * for (Dept dept : list) { for (Dept dept2 : getUser().getDepts()) { if
		 * (dept.getDeptId().equals(dept2.getDeptId())) { TreeString node = new
		 * TreeString(); node.setId(dept.getDeptId()); if
		 * (dept.getParentId().equals("0")) { node.setState("open"); } else {
		 * node.setState("closed"); } node.setPid(dept.getParentId());
		 * node.setText(dept.getDeptName()); Map<String, Object> attributes =
		 * new HashMap<String, Object>(); attributes.put("level",
		 * dept.getLevel()); attributes.put("fullName", dept.getFullName());
		 * node.setAttributes(attributes); rootTrees.add(node); } } } } else {
		 * 
		 * // 寻找根节点 for (Dept dept : list) { if (dept.getParentId().equals("0"))
		 * { TreeString node = new TreeString(); node.setId(dept.getDeptId());
		 * node.setState("open"); node.setPid(dept.getParentId());
		 * node.setText(dept.getDeptName()); Map<String, Object> attributes =
		 * new HashMap<String, Object>(); attributes.put("level",
		 * dept.getLevel()); attributes.put("fullName", dept.getFullName());
		 * node.setAttributes(attributes); rootTrees.add(node); } } }
		 * 
		 * if (list != null && list.size() > 0) { for (Dept dept : list) {
		 * TreeString node = new TreeString(); node.setId(dept.getDeptId());
		 * node.setPid(dept.getParentId()); node.setText(dept.getDeptName()); if
		 * (dept.getParentId().equals("0")) { node.setState("open"); } else {
		 * node.setState("closed"); } Map<String, Object> attributes = new
		 * HashMap<String, Object>(); attributes.put("level", dept.getLevel());
		 * attributes.put("aliasName", dept.getAliasName());
		 * node.setAttributes(attributes); treeList.add(node); } }
		 * TreeStringUtil.parseTreeString(rootTrees, treeList);
		 * super.writeJson(rootTrees);
		 */}

	public void doNotNeedSecurity_getDeptTreeForUser() {
		// TO-FIX
		/*
		 * List<Dept> list = deptService.selectAll(getUser().getDepts());
		 * List<TreeString> allList = new ArrayList<TreeString>();
		 * List<TreeString> rootTrees = new ArrayList<TreeString>(); if
		 * (getUser().getDepts() != null && getUser().getDepts().size() > 0) {
		 * for (Dept dept : list) { for (Dept dept2 : getUser().getDepts()) { if
		 * (dept.getParentId().equals(dept2.getDeptId())) { TreeString node =
		 * new TreeString(); node.setId(dept.getDeptId());
		 * node.setState("open"); node.setPid(dept.getParentId());
		 * node.setText(dept.getDeptName()); Map<String, Object> attributes =
		 * new HashMap<String, Object>(); attributes.put("level",
		 * dept.getLevel()); attributes.put("fullName", dept.getFullName());
		 * node.setAttributes(attributes); rootTrees.add(node); } } } } else {
		 * for (Dept dept : list) { if (dept.getParentId().equals("0")) {
		 * TreeString node = new TreeString(); node.setId(dept.getDeptId());
		 * node.setState("open"); node.setPid(dept.getParentId());
		 * node.setText(dept.getDeptName()); Map<String, Object> attributes =
		 * new HashMap<String, Object>(); attributes.put("level",
		 * dept.getLevel()); attributes.put("fullName", dept.getFullName());
		 * node.setAttributes(attributes); rootTrees.add(node); } } } if (list
		 * != null && list.size() > 0) { for (Dept dept : list) { TreeString
		 * node = new TreeString(); node.setId(dept.getDeptId());
		 * node.setState("open"); node.setPid(dept.getParentId());
		 * node.setText(dept.getDeptName()); Map<String, Object> attributes =
		 * new HashMap<String, Object>(); attributes.put("level",
		 * dept.getLevel()); attributes.put("fullName", dept.getFullName());
		 * node.setAttributes(attributes); allList.add(node); } }
		 * TreeStringUtil.parseTreeString(rootTrees, allList);
		 * super.writeJson(rootTrees);
		 */}

	public void doNotNeedSecurity_dataGrid() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		map.put("page", page);
		if (dept != null) {
			map.put("fullName", dept.getFullName());
		}
		super.writeJson(deptService.dateGridForUser(map));
	}

	// --------------------------xubo------------------------------------------
	/**
	 * 新增机构信息，徐波修改
	 */
	public void insert_new() {
		Message message = new Message();
		try {
			// 检查同一机构下的子机构是否存在同名的情况
			newDeptInfo.setAffiliation(deptService
					.searchAffiliation(newDeptInfo.getAffiliationName()));
			if (newDeptInfo.getAffiliation() != 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("affiliation", newDeptInfo.getAffiliation());
				map.put("major", newDeptInfo.getMajor());
				map.put("grade", newDeptInfo.getGrade());
				map.put("class", newDeptInfo.getClassName());
				// 检查是否含有重名的机构
				NewDeptInfo checkDept = deptService
						.selectByNameAndAffiliation(map);
				if (checkDept == null) {
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");// 设置日期格式
					newDeptInfo.setModifyTime(df.format(new Date()));
					deptService.insert_new(newDeptInfo);
					message.setMsg("新增成功");
					message.setSuccess(true);
				} else {
					message.setMsg("新增失败,名称重复");
					message.setSuccess(false);
				}
			}
		} catch (Exception e) {
			message.setMsg("新增失败");
			message.setSuccess(false);
			logger.error(e, e);
		}
		super.writeJson(message);
	}

	public void insert() {
		Message message = new Message();
		try {
			// 检查同一机构下的子机构是否存在同名的情况
			if (dept.getParentId() != null && !"".equals(dept.getParentId())) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("parentId", dept.getParentId());
				map.put("deptName", dept.getDeptName());
				// 检查是否含有重名的机构
				Dept checkDept = deptService.selectByNameAndParentId(map);
				if (checkDept == null) {
					deptService.insert(dept);
					message.setMsg("新增成功");
					message.setSuccess(true);
				} else {
					message.setMsg("新增失败,名称重复");
					message.setSuccess(false);
				}
			}
		} catch (Exception e) {
			message.setMsg("新增失败");
			message.setSuccess(false);
			logger.error(e, e);
		}
		super.writeJson(message);
	}

	public void update() {
		Message message = new Message();
		try {
			// 检查同一机构下的子机构是否存在同名的情况
			if (dept.getParentId() != null && !"".equals(dept.getParentId())) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("parentId", dept.getParentId());
				map.put("deptName", dept.getDeptName());
				map.put("deptId", dept.getDeptId());
				// 检查是否含有重名的机构
				Dept checkDept = deptService.selectByNameAndParentId(map);
				if (checkDept == null) {
					deptService.update(dept);
					message.setMsg("修改成功");
					message.setSuccess(true);
				} else {
					message.setMsg("修改失败,名称重复");
					message.setSuccess(false);
				}
			}
		} catch (Exception e) {
			message.setMsg("修改失败");
			message.setSuccess(false);
			logger.error(e, e);
		}
		super.writeJson(message);
	}

	public void updateBelong() {
		Message message = new Message();
		try {
			deptService.updateBelong(department.getDepartment_id(), classId);
			message.setMsg("修改成功");
			message.setSuccess(true);
		} catch (Exception e) {
			message.setMsg("修改失败");
			message.setSuccess(false);
			logger.error(e, e);
		}
		super.writeJson(message);
	}

	public void insertAlias() {
		Message message = new Message();
		try {
			// 检查同一机构下的子机构是否存在同名的情况
			if (dept.getParentId() != null && !"".equals(dept.getParentId())) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("parentId", dept.getParentId());
				map.put("deptName", dept.getDeptName());
				// 检查是否含有重名的机构
				Dept checkDept = deptService.selectByNameAndParentId(map);
				if (checkDept == null) {
					deptService.insertAlias(dept);
					message.setMsg("新增成功");
					message.setSuccess(true);
				} else {
					message.setMsg("新增失败,名称重复");
					message.setSuccess(false);
				}
			}
		} catch (Exception e) {
			message.setMsg("新增失败");
			message.setSuccess(false);
			logger.error(e, e);
		}
		super.writeJson(message);
	}

	public void doNotNeedSecurity_getDeptAlias() {
		super.writeJson(deptService.getSchool());
	}

	public void doNotNeedSecurity_getDeptAlias1() {
		List<Dept> list = deptService.getDepart1();
		List<Dept> dept2list = new ArrayList<Dept>();
		for (Dept dept : list) {
			if (dept.getDeptId().substring(9, 10).equals("0")) {
				dept2list.add(dept);
			}
		}
		super.writeJson(dept2list);
	}

	public void delete() {
		Message message = new Message();
		try {
			// 检查当前结构下是否存在学生,如果存在不允许删除
			List<UserInfo> list = userInfoService.selectUserByClassId(deptId);
			List<UserBaseInfo> baseList = userBaseInfoService
					.selectUserByClassId(deptId);
			if (list != null && list.size() > 0 && baseList != null
					&& baseList.size() > 0) {
				message.setMsg("删除失败,该机构下存在学生,请先删除该机构下的学生，再删除此机构");
				message.setSuccess(false);
			} else {
				deptService.delete(deptId);
				message.setMsg("删除成功");
				message.setSuccess(true);
			}
		} catch (Exception e) {
			message.setMsg("删除失败");
			message.setSuccess(false);
			logger.error(e, e);
		}
		super.writeJson(message);
	}

	public void importData() {
		Message message = new Message();
		try {
			HashMap<String, String> affiliation = new HashMap<String, String>();
			affiliation.put("departName", departName);
			affiliation.put("schoolName", schoolName);
			String result = deptService.importData(url, getUser(), affiliation);
			message.setMsg(result);
			message.setSuccess(true);
		} catch (Exception e) {
			message.setMsg("导入失败");
			message.setSuccess(false);
			logger.error(e, e);
		}
		super.writeJson(message);
	}

	/**
	 * @author Xubo 导出院系数据
	 */
	public void exportData() {
		Message message = new Message();
		try {
			String result = deptService.exportData();
			message.setMsg(result);
			message.setSuccess(true);
		} catch (Exception e) {
			message.setMsg("导出失败");
			message.setSuccess(false);
			logger.error(e, e);
		}
		super.writeJson(message);
	}

	/**
	 * @author Xubo 导出机构数据
	 */
	public void exportDept() {
		Message message = new Message();
		try {
			String result = deptService.exportDept();
			message.setMsg(result);
			message.setSuccess(true);
		} catch (Exception e) {
			message.setMsg("导出失败");
			message.setSuccess(false);
			logger.error(e, e);
		}
		super.writeJson(message);
	}

	/**
	 * 导入当前院系
	 */
	public void importDepartment() {
		Message message = new Message();
		try {
			String result = deptService.importDepartment(url, getUser());
			message.setMsg(result);
			message.setSuccess(true);
		} catch (Exception e) {
			message.setMsg("导入失败");
			message.setSuccess(false);
			logger.error(e, e);
		}
		super.writeJson(message);
	}

	public void doNotNeedSecurity_getSchool() {
		super.writeJson(deptService.getSchool());
	}

	/** --查询学校信息-- **/
	public void doNotNeedSecurity_getDepart() {
		// TO-FIX
		/*
		 * if (getUser().getRole().getSystemAdmin() == 1) {
		 * super.writeJson(deptService.getSchool()); } else { List<Dept> depts =
		 * getUser().getDepts(); List<Dept> deptList = new ArrayList<Dept>();
		 * List<String> deptIds = new ArrayList<String>(); if (depts != null &&
		 * depts.size() > 0) { for (Dept dept : depts) { if
		 * (dept.getDeptId().length() == 6) { deptList.add(dept); } else { if
		 * (!deptIds.contains(dept.getDeptId().substring(0, 6))) {
		 * deptIds.add(dept.getDeptId().substring(0, 6)); } } } if
		 * (deptIds.size() > 0) { deptList =
		 * deptService.selectByDeptIds(deptIds); } } super.writeJson(deptList);
		 * }
		 */}

	/**
	 * --查询学校信息-------------徐波----------------
	 * 由doNotNeedSecurity_getDepart_new修改过来的
	 * **/
	public void doNotNeedSecurity_getSchoolForCombox() {
		// TO-FIX
		List<School> list = new ArrayList<School>();
		if (getUser().getRole().getSystemAdmin() == 1) {
			School school = deptService.getSchool1();
			list.add(school);
		}
		super.writeJson(list);

	}

	/** --查询学校信息(权限和登录都不拦截,提供给外部通用)-- **/
	public void doNotNeedSessionAndSecurity_getDepart() {
		super.writeJson(deptService.getSchool());
	}

	/** --联动下拉框(学院或年级或班级)-- **/
	public void doNotNeedSecurity_getByParentId() {
		// TO-FIX
		/*
		 * if (getUser().getRole().getSystemAdmin() == 1) {
		 * super.writeJson(deptService.getByParentId(deptId)); } else { if
		 * (deptId.length() == 6) { List<Dept> depts = getUser().getDepts();
		 * List<String> list = new ArrayList<String>(); boolean f = false; if
		 * (depts != null && depts.size() > 0) { for (Dept dept : depts) { if
		 * (dept.getDeptId().length() == 6) { f = true; break; } else {
		 * list.add(dept.getDeptId()); } } } else { list.add(" "); } if (f) {
		 * super.writeJson(deptService.getByParentId(deptId)); } else {
		 * Map<String, Object> map = new HashMap<String, Object>();
		 * map.put("deptId", deptId); map.put("list", list);
		 * super.writeJson(deptService.getByParentIdAndDeptIds(map)); } } else {
		 * super.writeJson(deptService.getByParentId(deptId)); } }
		 */}

	/** --联动下拉框(学院或年级或班级)----------------xubo--------------------- **/
	public void doNotNeedSecurity_getByParentId_new() {
		// TO-FIX

		try {
			deptName = java.net.URLDecoder.decode(deptName, "UTF-8");
			if (majorName != null) {
				majorName = java.net.URLDecoder.decode(majorName, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (getUser().getRole().getSystemAdmin() == 1) {
			if (type.equals("department")) {
				List<Dept_New> list = deptService.selectAlldept(deptName, null);
				super.writeJson(list);
			} else if (type.equals("major")) {
				super.writeJson(deptService.selectmajorBydeptName(deptName));
			} else if (type.equals("grade")) {
				super.writeJson(deptService.selectgradeBydeptName(deptName));
			} else if (type.equals("class")) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("deptName", deptName);
				map.put("majorName", majorName);
				List<Dept_New> list = deptService.selectclassBydeptName(map);
				super.writeJson(list);
			}
		}
			 else {
				super.writeJson(deptService.getByParentId(deptId));
			}
		}
	

	/** --联动下拉框(权限和登录都不拦截,提供给外部通用)-- **/
	public void doNotNeedSessionAndSecurity_getByParentId() {
		super.writeJson(deptService.getByParentId(deptId));
	}

	public void doNotNeedSecurity_getById() {
		Department depart = deptService.getById_new(classId);
		super.writeJson(depart);
	}

	public void getByAliasName() {
		super.writeJson(deptService.getByAliasName(dept.getDeptId()));
	}

	public void doNotNeedSecurity_getBelong() {
		List<Department> department = deptService.getBelong_new(deptId);
		super.writeJson(department);
	}

	public void doNotNeedSecurity_getDeptTreeForView() {
		List<Dept> list = deptService.getByAliasName(deptId).getDepts();
		List<TreeString> allList = new ArrayList<TreeString>();
		if (list != null && list.size() > 0) {
			for (Dept dept : list) {
				if (dept.getDeptId().length() == deptId.length()
						&& !dept.getDeptId().equals(deptId)) {
					TreeString node = new TreeString();
					node.setId(dept.getDeptId());
					node.setState("open");
					node.setPid(dept.getParentId());
					node.setText(dept.getDeptName());
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put("level", dept.getLevel());
					attributes.put("fullName", dept.getFullName());
					node.setAttributes(attributes);
					allList.add(node);
				}
			}
		}
		super.writeJson(allList);
	}

	public void doNotNeedSecurity_getUserDepts() {
		super.writeJson(getUser().getDepts());
	}

	/**
	 * 获取所有当前院系，用于树<br>
	 * 取代doNotNeedSecurity_getDeptTreeForUser
	 */
	public void doNotNeedSecurity_getCollege2TreeForUser() {
		List<Department> list = deptService.getCollegeDepartment();
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
	 * 获取所有非当前院系的机构，用于下拉框
	 */
	public void doNotNeedSecurity_getNotCollege2ComboBox() {
		super.writeJson(deptService.getNotCollegeDepartment());
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

}
