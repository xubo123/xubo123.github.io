package com.hxy.core.userbaseinfo.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.DataGrid;
import com.hxy.base.entity.Message;
import com.hxy.core.classInfo.entity.ClassInfo;
import com.hxy.core.classInfo.service.ClassInfoService;
import com.hxy.core.department.entity.Department;
import com.hxy.core.userbaseinfo.entity.UserBaseInfo;
import com.hxy.core.userbaseinfo.service.UserBaseInfoService;
import com.hxy.system.PinYinUtils;

@Namespace("/userBaseInfo")
@Action(value = "userBaseInfoAction")
public class UserBaseInfoAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(UserBaseInfoAction.class);

	@Autowired
	private UserBaseInfoService userbaseInfoService;

	@Autowired
	private ClassInfoService classInfoService;

	private UserBaseInfo userbaseInfo;

	private String url;

	private int isInput;

	public void dataGrid() {

		String schoolName = getRequest().getParameter("schoolName");
		String college = getRequest().getParameter("college");
		String major = getRequest().getParameter("major");
		String grade = getRequest().getParameter("grade");
		String className = getRequest().getParameter("className");
		String user_name = getRequest().getParameter("user_name");
		String studentNumber = getRequest().getParameter("studentNumber");
		String studentType = getRequest().getParameter("studentType");
		String regflag = getRequest().getParameter("regflag");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);

		if (sort.equals("user_name")) {
			sort = "name_pinyin";
		} else if (sort.equals("schoolName")) {
			sort = "fullname";
		}
		map.put("sort", sort);
		map.put("order", order);
		map.put("deptList", getUser().getDepts()); // 后台用户所管理的院系

		map.put("schoolName", schoolName);
		map.put("college", college);
		map.put("major", major);
		map.put("grade", grade);
		map.put("className", className);

		map.put("user_name", user_name);
		map.put("studentNumber", studentNumber);
		map.put("studentType", studentType);
		map.put("regflag", regflag);

		super.writeJson(userbaseInfoService.dataGrid(map));
	}

	public void getById() {
		super.writeJson(userbaseInfoService.getById(userbaseInfo.getUser_id()));
	}

	public void save() {
		Message message = new Message();
		try {
			if (isInput == 0 && userbaseInfo.getClass_id() != 0) {
				if (getUser().getRole().getSystemAdmin() != 1) {
					ClassInfo sel_class = classInfoService.getById(userbaseInfo
							.getClass_id());
					List<Department> userDepts = getUser().getDepts();
					if (!userDepts.contains(sel_class.getAffiliation())) {
						throw new RuntimeException("该班级不隶属于您管理的当前院系。");
					}
				}
			} else if (isInput == 1) {
				userbaseInfo.setSchoolName(userbaseInfo.getSchoolName().trim());
				userbaseInfo.setCollege(userbaseInfo.getCollege().trim());
				userbaseInfo.setMajor(userbaseInfo.getMajor().trim());
				userbaseInfo.setGrade(userbaseInfo.getGrade().trim() + "级");
				userbaseInfo.setClassName(userbaseInfo.getClassName().trim());

				ClassInfo sel_class = new ClassInfo();
				sel_class.setSchoolName(userbaseInfo.getSchoolName());
				sel_class.setCollege(userbaseInfo.getCollege());
				sel_class.setMajor(userbaseInfo.getMajor());
				sel_class.setGrade(userbaseInfo.getGrade());
				sel_class.setClassName(userbaseInfo.getClassName());
				ClassInfo tmp = classInfoService.getByName(sel_class);
				if (tmp != null) {
					throw new RuntimeException("所填班级已存在,请切换至自动选择模式后选择。");
				}

				// 获取同专业的院系挂接情况，有挂接的话要更新到新的班级
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("page", 1);
				map.put("rows", 10);
				map.put("schoolName", sel_class.getSchoolName());
				map.put("college", sel_class.getCollege());
				map.put("major", sel_class.getMajor());
				DataGrid<ClassInfo> cGrid = classInfoService.dataGrid(map);
				if (cGrid.getTotal() > 0) {
					ClassInfo sameMajor = cGrid.getRows().get(0);
					sel_class.setAffiliation(sameMajor.getAffiliation());
				}
				// sel_class.setAffiliation(userbaseInfo.getDepartment_id());
				sel_class.setFullName(userbaseInfo.getSchoolName() + ","
						+ userbaseInfo.getCollege() + ","
						+ userbaseInfo.getMajor() + ","
						+ userbaseInfo.getGrade() + ","
						+ userbaseInfo.getClassName());
				classInfoService.insert(sel_class);

				userbaseInfo.setClass_id(sel_class.getClass_id());
			}

			userbaseInfo.setName_pinyin(PinYinUtils.getQuanPin(userbaseInfo
					.getUser_name()));
			userbaseInfoService.save(userbaseInfo);
			message.setMsg("新增成功");
			message.setSuccess(true);

		} catch (Exception e) {
			// logger.error(e, e);
			message.setMsg("新增失败!" + e.getMessage());
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void update() {
		Message message = new Message();
		try {
			if (isInput == 0 && userbaseInfo.getClass_id() != 0) {
				UserBaseInfo oldUser = userbaseInfoService.getById(userbaseInfo
						.getUser_id());
				if (oldUser.getClass_id() != userbaseInfo.getClass_id()
						&& oldUser.getAppuser_id() > 0) {
					throw new RuntimeException("此人为注册用户，无法修改其班级信息");
				}

				if (getUser().getRole().getSystemAdmin() != 1) {
					ClassInfo sel_class = classInfoService.getById(userbaseInfo
							.getClass_id());
					List<Department> userDepts = getUser().getDepts();
					if (!userDepts.contains(sel_class.getAffiliation())) {
						throw new RuntimeException("该班级不隶属于您管理的当前院系。");
					}
				}
			} else if (isInput == 1) {
				userbaseInfo.setSchoolName(userbaseInfo.getSchoolName().trim());
				userbaseInfo.setCollege(userbaseInfo.getCollege().trim());
				userbaseInfo.setMajor(userbaseInfo.getMajor().trim());
				userbaseInfo.setGrade(userbaseInfo.getGrade().trim() + "级");
				userbaseInfo.setClassName(userbaseInfo.getClassName().trim());

				ClassInfo sel_class = new ClassInfo();
				sel_class.setSchoolName(userbaseInfo.getSchoolName());
				sel_class.setCollege(userbaseInfo.getCollege());
				sel_class.setMajor(userbaseInfo.getMajor());
				sel_class.setGrade(userbaseInfo.getGrade());
				sel_class.setClassName(userbaseInfo.getClassName());
				ClassInfo tmp = classInfoService.getByName(sel_class);
				if (tmp != null) {
					throw new RuntimeException("所填班级已存在,请切换至自动选择模式后选择。");
				}
				UserBaseInfo oldUser = userbaseInfoService.getById(userbaseInfo
						.getUser_id());
				if (oldUser.getAppuser_id() > 0) {
					throw new RuntimeException("此人为注册用户，无法修改其班级信息");
				}

				// 获取同专业的院系挂接情况，有挂接的话要更新到新的班级
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("page", 1);
				map.put("rows", 10);
				map.put("schoolName", sel_class.getSchoolName());
				map.put("college", sel_class.getCollege());
				map.put("major", sel_class.getMajor());
				DataGrid<ClassInfo> cGrid = classInfoService.dataGrid(map);
				if (cGrid.getTotal() > 0) {
					ClassInfo sameMajor = cGrid.getRows().get(0);
					sel_class.setAffiliation(sameMajor.getAffiliation());
				}

				sel_class.setFullName(userbaseInfo.getSchoolName() + ","
						+ userbaseInfo.getCollege() + ","
						+ userbaseInfo.getMajor() + ","
						+ userbaseInfo.getGrade() + ","
						+ userbaseInfo.getClassName());
				classInfoService.insert(sel_class);

				userbaseInfo.setClass_id(sel_class.getClass_id());
			}

			userbaseInfo.setName_pinyin(PinYinUtils.getQuanPin(userbaseInfo
					.getUser_name()));
			userbaseInfoService.update(userbaseInfo);

			// 注册用户不允许更改班级信息，所以不存在注册用户和群组的关系更新问题

			message.setMsg("修改成功");
			message.setSuccess(true);

		} catch (Exception e) {
			// logger.error(e, e);
			message.setMsg("修改失败!" + e.getMessage());
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void delete() {
		Message message = new Message();
		try {
			String[] array = ids.split(",");
			for (String id : array) {
				UserBaseInfo oldUser = userbaseInfoService.getById(Long
						.parseLong(id));
				if (oldUser.getAppuser_id() > 0) {
					throw new RuntimeException("无法删除已注册的用户。");
				}
			}

			userbaseInfoService.delete(ids);
			message.setMsg("删除成功");
			message.setSuccess(true);

		} catch (Exception e) {
			// logger.error(e, e);
			message.setMsg("删除失败!" + e.getMessage());
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void deleteQuery() {
		Message message = new Message();
		try {
			String schoolName = getRequest().getParameter("schoolName");
			String college = getRequest().getParameter("college");
			String major = getRequest().getParameter("major");
			String grade = getRequest().getParameter("grade");
			String className = getRequest().getParameter("className");
			String user_name = getRequest().getParameter("user_name");
			String studentNumber = getRequest().getParameter("studentNumber");
			String studentType = getRequest().getParameter("studentType");
			String regflag = getRequest().getParameter("regflag");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptList", getUser().getDepts()); // 后台用户所管理的院系

			map.put("schoolName", schoolName);
			map.put("college", college);
			map.put("major", major);
			map.put("grade", grade);
			map.put("className", className);

			map.put("user_name", user_name);
			map.put("studentNumber", studentNumber);
			map.put("studentType", studentType);
			map.put("regflag", regflag);

			userbaseInfoService.deleteQuery(map);
			message.setMsg("删除成功");
			message.setSuccess(true);

		} catch (Exception e) {
			// logger.error(e, e);
			message.setMsg("删除失败!" + e.getMessage());
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	/**
	 * 校友总汇
	 */
	public void dataGridSum() {
		String schoolName = getRequest().getParameter("schoolName");
		String college = getRequest().getParameter("college");
		String major = getRequest().getParameter("major");
		String grade = getRequest().getParameter("grade");
		String className = getRequest().getParameter("className");
		String user_name = getRequest().getParameter("user_name");
		String sex = getRequest().getParameter("sex");
		String studentType = getRequest().getParameter("studentType");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);

		if (sort.equals("user_name")) {
			sort = "name_pinyin";
		}
		map.put("sort", sort);
		map.put("order", order);
		map.put("deptList", getUser().getDepts()); // 后台用户所管理的院系

		map.put("schoolName", schoolName);
		map.put("college", college);
		map.put("major", major);
		map.put("grade", grade);
		map.put("className", className);

		map.put("user_name", user_name);
		map.put("studentType", studentType);
		map.put("sex", sex);
		map.put("alumni", 1);
		super.writeJson(userbaseInfoService.dataGridSum(map));
	}

	/** --通过ajax查询所有手机号不为空的用户，并且分页-- **/
	public void doNotNeedSecurity_getAllUserList_dataGrid() {
		String user_id = getRequest().getParameter("messageid");
		Map<String, Object> map = new HashMap<String, Object>();
			map.put("phoneNotNull", true);
			map.put("emailNotNull", false);
			map.put("user_id", user_id);
			map.put("deptList", getUser().getDepts());
			super.writeJson(userbaseInfoService.getAlumniInfo(map));
	}

	/**
	 * 校友会名单
	 */
	public void dataGridForAlumni() {
		String schoolName = getRequest().getParameter("schoolName");
		String college = getRequest().getParameter("college");
		String major = getRequest().getParameter("major");
		String grade = getRequest().getParameter("grade");
		String className = getRequest().getParameter("className");
		String user_name = getRequest().getParameter("user_name");
		String sex = getRequest().getParameter("sex");
		String studentType = getRequest().getParameter("studentType");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		map.put("phoneNotNull", false);
		map.put("emailNotNull", false);
		if (sort.equals("user_name")) {
			sort = "name_pinyin";
		}
		map.put("sort", sort);
		map.put("order", order);
		List<Department> department = getUser().getDepts();
		map.put("deptList", department); // 后台用户所管理的院系

		map.put("schoolName", schoolName);
		map.put("college", college);
		map.put("major", major);
		map.put("grade", grade);
		map.put("className", className);

		map.put("user_name", user_name);
		map.put("studentType", studentType);
		map.put("sex", sex);
		super.writeJson(userbaseInfoService.dataGridSumForAlumni(map));
	}

	/**
	 * 校友总汇--查看个人
	 */
	public void getSumByIds() {
		super.writeJson(userbaseInfoService.getSumByIds(userbaseInfo
				.getAllUser_id()));
	}

	/**
	 * 校友会名单-查看个人
	 */
	public void getAlumniInfoByIds() {
		super.writeJson(userbaseInfoService.getAlumniInfoByIds(userbaseInfo
				.getAllUser_id()));
	}

	public void importData() {
		Message message = new Message();
		try {
			String result = userbaseInfoService.importData(url, getUser());
			message.setMsg(result);
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("导入失败," + e.getMessage());
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void exportData() {
		Message message = new Message();
		try {
			String schoolName = getRequest().getParameter("schoolName");
			String college = getRequest().getParameter("college");
			String major = getRequest().getParameter("major");
			String grade = getRequest().getParameter("grade");
			String className = getRequest().getParameter("className");
			String user_name = getRequest().getParameter("user_name");
			String studentNumber = getRequest().getParameter("studentNumber");
			String studentType = getRequest().getParameter("studentType");
			String regflag = getRequest().getParameter("regflag");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sort", "name_pinyin");
			map.put("order", order);

			map.put("deptList", getUser().getDepts()); // 后台用户所管理的院系

			map.put("schoolName", schoolName);
			map.put("college", college);
			map.put("major", major);
			map.put("grade", grade);
			map.put("className", className);

			map.put("user_name", user_name);
			map.put("studentNumber", studentNumber);
			map.put("studentType", studentType);
			map.put("regflag", regflag);

			map.put("export", "1");
			String result = userbaseInfoService.export(map);
			message.setMsg(result);
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("导出失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	/*
	 * public void doNotNeedSecurity_getUserInfoByUserId() {
	 * super.writeJson(userInfoService.selectByUserId(userInfo.getUser_id())); }
	 * 
	 * 
	 * 
	 * public void doNotNeedSecurity_getAllUserList() {
	 * super.writeJson(userInfoService.selectAllUserList()); }
	 * 
	 * public void doNotNeedSecurity_dataGridFor() { Map<String, Object> map =
	 * new HashMap<String, Object>(); map.put("page", page); map.put("rows",
	 * rows); if (userInfo != null) { map.put("telId", userInfo.getTelId());
	 * map.put("userName", userInfo.getUserName()); }
	 * super.writeJson(userInfoService.dataGridFor(map)); }
	 */

	public UserBaseInfo getUserbaseInfo() {
		return userbaseInfo;
	}

	public void setUserbaseInfo(UserBaseInfo userbaseInfo) {
		this.userbaseInfo = userbaseInfo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIsInput() {
		return isInput;
	}

	public void setIsInput(int isInput) {
		this.isInput = isInput;
	}

}
