package com.hxy.core.classInfo.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
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
import com.hxy.util.ChatGroup;
import com.hxy.util.GroupRegister;

@Namespace("/classInfo")
@Action(value = "classInfoAction")
public class ClassInfoAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ClassInfoAction.class);

	@Autowired
	private ClassInfoService classInfoService;

	private ClassInfo classInfo;

	public ClassInfo getClassInfo() {
		return classInfo;
	}

	public void setClassInfo(ClassInfo classInfo) {
		this.classInfo = classInfo;
	}

	public void getList() {
		String schoolName = getRequest().getParameter("schoolName");
		String college = getRequest().getParameter("college");
		String major = getRequest().getParameter("major");
		String grade = getRequest().getParameter("grade");
		String className = getRequest().getParameter("className");
		String affiliation = getRequest().getParameter("affiliation");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		map.put("schoolName", schoolName);
		map.put("college", college);
		map.put("major", major);
		map.put("grade", grade);
		map.put("className", className);
		map.put("affiliation", affiliation);
		super.writeJson(classInfoService.dataGrid(map));
	}

	public void getById() {
		super.writeJson(classInfoService.getById(classInfo.getClass_id()));
	}

	public void viewAffiliation() {
		getById();
	}

	public void insert() {
		Message message = new Message();
		try {
			// 检查是否含有重名的班级
			classInfo.setSchoolName(classInfo.getSchoolName().trim());
			classInfo.setCollege(classInfo.getCollege().trim());
			classInfo.setMajor(classInfo.getMajor().trim());
			classInfo.setGrade(classInfo.getGrade().trim() + "级");
			classInfo.setClassName(classInfo.getClassName().trim());

			ClassInfo tmp = classInfoService.getByName(classInfo);

			if (tmp == null) {
				// 获取同专业的院系挂接情况，有挂接的话要更新到新的班级
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("page", 1);
				map.put("rows", 10);
				map.put("schoolName", classInfo.getSchoolName());
				map.put("college", classInfo.getCollege());
				map.put("major", classInfo.getMajor());
				DataGrid<ClassInfo> cGrid = classInfoService.dataGrid(map);
				if (cGrid.getTotal() > 0) {
					ClassInfo sameMajor = cGrid.getRows().get(0);
					classInfo.setAffiliation(sameMajor.getAffiliation());
				}

				classInfo.setFullName(classInfo.getSchoolName() + ","
						+ classInfo.getCollege() + "," + classInfo.getMajor()
						+ "," + classInfo.getGrade() + ","
						+ classInfo.getClassName());

				// 创建环信群组
				ChatGroup chatGroup = new ChatGroup();
				chatGroup.setGroupname(classInfo.getClassName());
				chatGroup.setDesc(classInfo.getFullName());
				chatGroup.setMaxusers(200);
				chatGroup.setOwner("system");
				chatGroup.setPublic(false);
				chatGroup.setApproval(true);
				String groupId = GroupRegister.createChatGroup(chatGroup);
				if (!groupId.equals("error")) {
					classInfo.setEasemobGroup(groupId);
				}

				classInfoService.insert(classInfo);

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

	public void update() {
		Message message = new Message();
		try {
			// 检查是否含有重名的机构
			classInfo.setSchoolName(classInfo.getSchoolName().trim());
			classInfo.setCollege(classInfo.getCollege().trim());
			classInfo.setMajor(classInfo.getMajor().trim());
			classInfo.setGrade(classInfo.getGrade().trim() + "级");
			classInfo.setClassName(classInfo.getClassName().trim());

			ClassInfo tmp = classInfoService.getByName(classInfo);

			if (tmp == null) {
				// 专业变更的话更新院系挂接
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("page", 1);
				map.put("rows", 10);
				map.put("schoolName", classInfo.getSchoolName());
				map.put("college", classInfo.getCollege());
				map.put("major", classInfo.getMajor());
				DataGrid<ClassInfo> cGrid = classInfoService.dataGrid(map);
				if (cGrid.getTotal() > 0) {
					ClassInfo sameMajor = cGrid.getRows().get(0);
					classInfo.setAffiliation(sameMajor.getAffiliation());
				} else {
					// 新的专业，无挂接
					classInfo.setAffiliation(0);
				}

				classInfo.setFullName(classInfo.getSchoolName() + ","
						+ classInfo.getCollege() + "," + classInfo.getMajor()
						+ "," + classInfo.getGrade() + ","
						+ classInfo.getClassName());
				classInfoService.update(classInfo);

				message.setMsg("修改成功");
				message.setSuccess(true);
			} else if (classInfo.getClass_id() == tmp.getClass_id()) {
				// 相当于什么也没改
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

	public void delete() {
		Message message = new Message();
		try {
			// 检查该班级下是否有隶属的学生
			if (!classInfoService.existStudent(id)) {
				classInfoService.delete(id);
				message.setMsg("删除成功");
				message.setSuccess(true);
			} else {
				message.setMsg("删除失败,该班级已有学生");
				message.setSuccess(false);
			}
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void getAffiliationList() {
		String affiliated = getRequest().getParameter("affiliated");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		map.put("affiliated", affiliated);
		super.writeJson(classInfoService.dataGridMajor(map));
	}

	public void updateAffiliation() {
		Message message = new Message();
		try {
			long oldAffiliation = classInfoService.getById(
					classInfo.getClass_id()).getAffiliation();
			long newAffiiiation = classInfo.getAffiliation();
			if (oldAffiliation != newAffiiiation) {
				classInfoService.updateAffiliation(classInfo.getClass_id(),
						newAffiiiation);

				// 如果隶属了新的当前院系，则该专业下所有注册用户自动关联新的社群
				// 但与旧社群的关联仍然保留，由注册用户自己决定是否解除关联
				if (newAffiiiation != 0) {
					// 也许注册用户自己关联过这个新社群，这里先删除，以免下面添加关联后造成重复
					classInfoService.deleteAppuserGroupByMajor(
							classInfo.getClass_id(), newAffiiiation);
					classInfoService.insertAppuserGroupByMajor(
							classInfo.getClass_id(), newAffiiiation);
				}
			}
			message.setMsg("修改成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("修改失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	/** 学校-院系-专业-年级-班级的多级联动 **/
	public void doNotNeedSecurity_getDeptByParent() {
		try {
			String parentName;
			parentName = getRequest().getParameter("pn");
			if (parentName == null || parentName.isEmpty()) {
				super.writeJson(classInfoService.getSchools());
			} else {
				parentName = URLDecoder.decode(parentName, "UTF-8");
				String[] arr = parentName.split(",");
				if (arr.length == 1) {
					super.writeJson(classInfoService.getColleges(arr[0]));
				} else if (arr.length == 2) {
					super.writeJson(classInfoService.getMajors(arr[0], arr[1]));
				} else if (arr.length == 3) {
					super.writeJson(classInfoService.getGrades(arr[0], arr[1],
							arr[2]));
				} else if (arr.length == 4) {
					super.writeJson(classInfoService.getClasses(arr[0], arr[1],
							arr[2], arr[3]));
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
