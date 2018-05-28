package com.hxy.core.dept.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.base.entity.TreeString;
import com.hxy.core.dept.dao.DeptMapper;
import com.hxy.core.dept.entity.Department;
import com.hxy.core.dept.entity.Dept;
import com.hxy.core.dept.entity.Dept_New;
import com.hxy.core.dept.entity.NewDeptInfo;
import com.hxy.core.dept.entity.School;
import com.hxy.core.major.dao.MajorMapper;
import com.hxy.core.major.entity.Major;
import com.hxy.core.user.entity.User;
import com.hxy.core.userbaseinfo.entity.UserBaseInfo;
import com.hxy.system.ClassPathResource;
import com.hxy.system.ExcelUtil;
import com.hxy.system.Global;
import com.hxy.system.IdUtil;
import com.hxy.system.TreeStringUtil;

@Service("deptService")
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptMapper deptMapper;

	@Autowired
	private MajorMapper majorMapper;

	public List<Dept> selectAll(List<Dept> list) {
		return deptMapper.selectAll(list);
	}

	public void insert(Dept dept) {
		try {
			if ("0".equals(dept.getParentId())) {
				dept.setCreateTime(new Date());
				dept.setFullName(dept.getDeptName());
				deptMapper.insert(dept);
			}
			if (dept.getParentId().length() == 14) {
				List<Dept> list = deptMapper.selectByDeptId(dept.getParentId());
				Set<String> set = new HashSet<String>();
				for (Dept dept2 : list) {
					if (dept2.getDeptId() != null && dept2.getDeptId().length() == 16) {
						set.add(dept2.getDeptId().substring(14, 16));
					}
				}
				String classId = IdUtil.getClassId(set);
				if (classId.equals("")) {
					throw new RuntimeException("班级数据异常，同年级班级已超过100个");
				}
				String deptId = dept.getParentId() + classId;
				dept.setDeptId(deptId);
				dept.setCreateTime(new Date());
				Dept grade = deptMapper.selectOne(dept.getParentId());
				String fullName = grade.getFullName() + "," + dept.getDeptName();
				dept.setFullName(fullName);
				deptMapper.insert(dept);
			}

			if (dept.getParentId().length() == 6 && dept.getLevel() == 4 && dept.getDepartId() == null || "".equals(dept.getDepartId())) {
				List<Dept> list = deptMapper.selectByDeptId(dept.getParentId());
				Set<String> set = new HashSet<String>();
				for (Dept dept2 : list) {
					if (dept2.getDeptId() != null && dept2.getDeptId().length() == 10) {
						set.add(dept2.getDeptId().substring(6, 9));
					}
				}
				String departId = IdUtil.getDepart(set);
				if (departId.equals("")) {
					throw new RuntimeException("院系数据异常，院系已超过1000个");
				}
				String deptId = dept.getParentId() + departId + "0";
				dept.setDeptId(deptId);
				dept.setCreateTime(new Date());
				Dept school = deptMapper.selectOne(dept.getParentId());
				String fullName = school.getFullName() + "," + dept.getDeptName();
				dept.setFullName(fullName);
				deptMapper.insert(dept);
			}

			if (dept.getParentId().length() == 6 && dept.getLevel() == 4 && (dept.getDepartId() != null && !dept.getDepartId().equals(""))) {
				Set<String> set = new HashSet<String>();
				List<Dept> list = deptMapper.getDepart2(dept.getParentId());
				for (Dept dept2 : list) {
					if (dept2.getDeptId().substring(6, 9).equals(dept.getDepartId().substring(6, 9))) {
						set.add(dept2.getDeptId().substring(9, 10));
					}
				}
				String extend = IdUtil.getExtend(set);
				if (extend.equals("")) {
					throw new RuntimeException("曾用名异常，曾用名数据量已超过35个");
				}
				String deptId = dept.getParentId() + dept.getDepartId().substring(6, 9) + extend;
				dept.setDeptId(deptId);
				dept.setCreateTime(new Date());
				Dept school = deptMapper.selectOne(dept.getParentId());
				String fullName = school.getFullName() + "," + dept.getDeptName();
				dept.setFullName(fullName);
				deptMapper.insert(dept);
			}

			if (dept.getParentId().length() == 10) {
				String deptId = dept.getParentId() + dept.getDeptName();
				dept.setDeptId(deptId);
				dept.setCreateTime(new Date());
				Dept depart = deptMapper.selectOne(dept.getParentId());
				dept.setFullName(depart.getFullName() + "," + dept.getDeptName() + "级");
				dept.setDeptName(dept.getDeptName() + "级");
				deptMapper.insert(dept);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void insertAlias(Dept dept) {
		if (dept.getDeptId().length() == 6) {
			List<Dept> list = deptMapper.getSchool();
			Set<String> set = new HashSet<String>();
			for (Dept dept2 : list) {
				set.add(dept2.getDeptId().substring(5, 6));
			}
			String extend = IdUtil.getExtend(set);
			if (extend.equals("")) {
				throw new RuntimeException("曾用名异常，曾用名数据量已超过35个");
			}
			String deptId = dept.getDeptId().substring(0, 5) + extend;
			dept.setAliasName(dept.getDeptId());
			dept.setDeptId(deptId);
			dept.setCreateTime(new Date());
			dept.setFullName(dept.getDeptName());
			deptMapper.insert(dept);
		}
		if (dept.getDeptId().length() == 10) {
			Dept school = deptMapper.selectOne(dept.getSchoolId());
			List<Dept> list = deptMapper.getDepart2(dept.getSchoolId());
			Set<String> set = new HashSet<String>();
			for (Dept dept2 : list) {
				if (dept2.getDeptId().substring(6, 9).equals(dept.getDeptId().substring(6, 9))) {
					set.add(dept2.getDeptId().substring(9, 10));
				}
			}
			String extend = IdUtil.getExtend(set);
			if (extend.equals("")) {
				throw new RuntimeException("曾用名异常，曾用名数据量已超过35个");
			}
			String deptId = dept.getSchoolId() + dept.getDeptId().substring(6, 9) + extend;
			dept.setDeptId(deptId);
			dept.setCreateTime(new Date());
			dept.setParentId(dept.getSchoolId());
			dept.setFullName(school.getFullName() + "," + dept.getDeptName());
			deptMapper.insert(dept);
		}
	}

	public Dept checkDeptId(String deptId) {
		return deptMapper.checkDeptId(deptId);
	}

	public void delete(String deptId) {
		try {
			List<Dept> list = deptMapper.selectAll2();
			List<TreeString> allList = new ArrayList<TreeString>();
			if (list != null && list.size() > 0) {
				for (Dept dept : list) {
					TreeString node = new TreeString();
					node.setId(dept.getDeptId());
					node.setPid(dept.getParentId());
					node.setText(dept.getDeptName());
					Map<String, Integer> attributes = new HashMap<String, Integer>();
					attributes.put("level", dept.getLevel());
					node.setAttributes(attributes);
					allList.add(node);
				}
			}
			List<String> deptIdList = new ArrayList<String>();
			deptIdList.add(deptId);
			TreeStringUtil.getChildren(deptId, allList, deptIdList);
			deptMapper.delete(deptIdList);
			// 删除与专业的关系表
			majorMapper.deleteMajorByDeptId(deptIdList);
			// 清空归属关系
			for (String aliasName : deptIdList) {
				deptMapper.updateAliasName(aliasName);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public String importDepartment(String url, User user) {
		// TODO Auto-generated method stub
		try{
		// 文件保存目录路径
		String savePath = Global.DISK_PATH;
		// 文件保存目录URL
		String saveUrl = Global.URL_DOMAIN;
		url = savePath + url.replace(saveUrl, "");
		File file = new File(url);
		List<Object[]> list = ExcelUtil.parseExcel(file);
		List<Object[]> errorList = new ArrayList<Object[]>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String schoolName = ((String) list.get(i)[0]).trim();
				String departName = ((String) list.get(i)[1]).trim();
				if (i == 0) {
					// 第一行为excle表头
					Object[] head = new Object[list.get(i).length + 1];
					for (int j = 0; j < head.length; j++) {
						if (j != head.length - 1) {
							head[j] = list.get(i)[j];
						} else {
							head[j] = "失败原因";
						}
					}
					errorList.add(head);
				} else if (schoolName == null || "".equals(schoolName.trim())) {
					Object[] content = new Object[list.get(i).length + 1];
					for (int j = 0; j < content.length; j++) {
						if (j != content.length - 1) {
							content[j] = list.get(i)[j];
						} else {
							content[j] = "学校为空";
						}
					}
					errorList.add(content);
					continue;
				} else if (departName == null || "".equals(departName.trim())) {
					Object[] content = new Object[list.get(i).length + 1];
					for (int j = 0; j < content.length; j++) {
						if (j != content.length - 1) {
							content[j] = list.get(i)[j];
						} else {
							content[j] = "院系为空";
						}
					}
					errorList.add(content);
					continue;
				} else {
					if (user.getDepts() == null || user.getDepts().size() == 0) {
						 Department department = new Department();
						 department.setSchoolName(schoolName);
						 department.setDepartmentName(departName);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("schoolName", department.getSchoolName());
							map.put("departmentName", department.getDepartmentName());
							// 检查是否含有重名的院系
							Department checkDept = checkDepartment(map);
							
						if (checkDept != null){
							// 班级名重复
							Object[] content = new Object[list.get(i).length + 1];
							for (int j = 0; j < content.length; j++) {
								if (j != content.length - 1) {
									content[j] = list.get(i)[j];
								} else {
									content[j] = "院系名重复";
								}
							}
							errorList.add(content);
							continue;
						}
						else{
							deptMapper.insert_department(department);
						}
					}
				}
			}
		}
		return ExcelUtil.exportData(errorList);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	}
	private Department checkDepartment(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return deptMapper.checkDepartment(map);
	}

	/**
	 * @author Xubo
	 * @param url 文件地址  
	 * @param user 登录用户
	 */
	public String importData(String url, User user,HashMap<String, String> affiliation) {
		try {
			// 文件保存目录路径
			String savePath = Global.DISK_PATH;

			// 文件保存目录URL
			String saveUrl = Global.URL_DOMAIN;
			url = savePath + url.replace(saveUrl, "");
			File file = new File(url);
			List<Object[]> list = ExcelUtil.parseExcel(file);
			List<Object[]> errorList = new ArrayList<Object[]>();
			// 数据整理结束
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String schoolName = ((String) list.get(i)[0]).trim();
					String departName = ((String) list.get(i)[1]).trim();
					String majorName = ((String) list.get(i)[2]).trim();
					String gradeName = ((String) list.get(i)[3]).trim();
					String className = ((String) list.get(i)[4]).trim();
					if (i == 0) {
						// 第一行为excle表头
						Object[] head = new Object[list.get(i).length + 1];
						for (int j = 0; j < head.length; j++) {
							if (j != head.length - 1) {
								head[j] = list.get(i)[j];
							} else {
								head[j] = "失败原因";
							}
						}
						errorList.add(head);
					} else if (schoolName == null || "".equals(schoolName.trim())) {
						Object[] content = new Object[list.get(i).length + 1];
						for (int j = 0; j < content.length; j++) {
							if (j != content.length - 1) {
								content[j] = list.get(i)[j];
							} else {
								content[j] = "学校为空";
							}
						}
						errorList.add(content);
						continue;
					} else if (departName == null || "".equals(departName.trim())) {
						Object[] content = new Object[list.get(i).length + 1];
						for (int j = 0; j < content.length; j++) {
							if (j != content.length - 1) {
								content[j] = list.get(i)[j];
							} else {
								content[j] = "院系为空";
							}
						}
						errorList.add(content);
						continue;
					} else if (gradeName == null || "".equals(gradeName.trim())) {
						// 班级为空
						Object[] content = new Object[list.get(i).length + 1];
						for (int j = 0; j < content.length; j++) {
							if (j != content.length - 1) {
								content[j] = list.get(i)[j];
							} else {
								content[j] = "年级为空";
							}
						}
						errorList.add(content);
						continue;
					} else if (className == null || "".equals(className.trim())) {
						// 班级为空
						Object[] content = new Object[list.get(i).length + 1];
						for (int j = 0; j < content.length; j++) {
							if (j != content.length - 1) {
								content[j] = list.get(i)[j];
							} else {
								content[j] = "班级为空";
							}
						}
						errorList.add(content);
						continue;
					} else if (!ClassPathResource.isNumeric(gradeName)) {
						// 班级为空
						Object[] content = new Object[list.get(i).length + 1];
						for (int j = 0; j < content.length; j++) {
							if (j != content.length - 1) {
								content[j] = list.get(i)[j];
							} else {
								content[j] = "年级数据有误";
							}
						}
						errorList.add(content);
						continue;
					} else {
						if (user.getDepts() == null || user.getDepts().size() == 0) {
							 NewDeptInfo class1 = new NewDeptInfo();
								class1.setSchoolName(schoolName);
								class1.setCollege(departName);
								class1.setGrade(gradeName);
								class1.setMajor(majorName);
								class1.setClassName(className);
								class1.setAffiliationName(affiliation.get("departName"));
								SimpleDateFormat df = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");// 设置日期格式
								class1.setModifyTime(df.format(new Date()));
								class1.setAffiliation(searchAffiliation(class1.getAffiliationName()));
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("affiliation", class1.getAffiliation());
								map.put("major", class1.getMajor());
								map.put("grade", class1.getGrade());
								map.put("class", class1.getClassName());
								// 检查是否含有重名的机构
								NewDeptInfo checkDept = selectByNameAndAffiliation(map);
								
							if (checkDept != null){
								// 班级名重复
								Object[] content = new Object[list.get(i).length + 1];
								for (int j = 0; j < content.length; j++) {
									if (j != content.length - 1) {
										content[j] = list.get(i)[j];
									} else {
										content[j] = "班级名重复";
									}
								}
								errorList.add(content);
								continue;
							}
							else{
								deptMapper.insert_new(class1);
							}
						}
					}
				}
			}
			return ExcelUtil.exportData(errorList);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
//	public String importData(String url, User user) {
//		try {
//			// 文件保存目录路径
//			String savePath = Global.DISK_PATH;
//
//			// 文件保存目录URL
//			String saveUrl = Global.URL_DOMAIN;
//			url = savePath + url.replace(saveUrl, "");
//			File file = new File(url);
//			List<Object[]> list = ExcelUtil.parseExcel(file);
//			List<Object[]> errorList = new ArrayList<Object[]>();
//			// 数据整理开始
//			Map<String, Dept> deptNameAndParentIdMap = new ConcurrentHashMap<String, Dept>();
//			Map<String, Set<String>> parentIdMap = new ConcurrentHashMap<String, Set<String>>();//建立从属关系
//			List<Dept> allDeptList = deptMapper.selectAll2();
//			String schoolId = "";
//			if (allDeptList != null && allDeptList.size() > 0) {
//				for (Dept dept2 : allDeptList) {
//					String deptNameAndParentId = dept2.getDeptName() + "," + dept2.getParentId();
//					if (deptNameAndParentIdMap.keySet().contains(deptNameAndParentId)) {
//						throw new RuntimeException("同一机构下，发现重名的子机构");
//					} else {
//						deptNameAndParentIdMap.put(deptNameAndParentId, dept2);//将父机构与子机构建立映射关系
//					}
//					String parentId = dept2.getParentId();
//					Set<String> deptIdSet = null;
//					// 学校
//					if (dept2.getDeptId().length() == 6) {
//						if (parentIdMap.keySet().contains(parentId)) {
//							deptIdSet = parentIdMap.get(parentId);
//							deptIdSet.add(dept2.getDeptId().substring(5, 6));
//						} else {
//							deptIdSet = new HashSet<String>();
//							deptIdSet.add(dept2.getDeptId().substring(5, 6));
//						}
//						parentIdMap.put(parentId, deptIdSet);
//						schoolId = dept2.getDeptId().substring(0, 5);
//					}
//					// 院系
//					if (dept2.getDeptId().length() == 10) {
//						if (parentIdMap.keySet().contains(parentId)) {
//							deptIdSet = parentIdMap.get(parentId);
//							deptIdSet.add(dept2.getDeptId().substring(6, 9));
//						} else {
//							deptIdSet = new HashSet<String>();
//							deptIdSet.add(dept2.getDeptId().substring(6, 9));
//						}
//						parentIdMap.put(parentId, deptIdSet);
//					}
//					// 班级
//					if (dept2.getDeptId().length() == 16) {
//						if (parentIdMap.keySet().contains(parentId)) {
//							deptIdSet = parentIdMap.get(parentId);
//							deptIdSet.add(dept2.getDeptId().substring(14, 16));
//						} else {
//							deptIdSet = new HashSet<String>();
//							deptIdSet.add(dept2.getDeptId().substring(14, 16));
//						}
//						parentIdMap.put(parentId, deptIdSet);
//					}
//				}
//			}
//			// 专业和院系的关系初始化
//			Map<String, Major> majorMap = new ConcurrentHashMap<String, Major>();
//			Set<String> majorSet = new HashSet<String>();
//			List<Major> majorList = majorMapper.selectAll();
//			if (majorList != null && majorList.size() > 0) {
//				for (Major major : majorList) {
//					majorMap.put(major.getMajorName(), major);
//					List<Dept> mDeptList = major.getDeptList();
//					if (mDeptList != null && mDeptList.size() > 0) {
//						for (Dept dept2 : mDeptList) {
//							majorSet.add(dept2.getDeptId() + "," + major.getMajorId());
//						}
//					}
//				}
//			}
//
//			List<Dept> hasDeptList = user.getDepts();
//			Set<String> schoolSet = new HashSet<String>();
//			Set<String> departSet = new HashSet<String>();
//			if (hasDeptList != null && hasDeptList.size() > 0) {
//				for (Dept dept : hasDeptList) {
//					if (dept.getDeptId().length() == 6) {
//						schoolSet.add(dept.getDeptId());
//					} else {
//						departSet.add(dept.getDeptId());
//					}
//				}
//			}
//			// 数据整理结束
//			if (list != null && list.size() > 0) {
//				for (int i = 0; i < list.size(); i++) {
//					String schoolName = ((String) list.get(i)[0]).trim();
//					String departName = ((String) list.get(i)[1]).trim();
//					String majorName = ((String) list.get(i)[2]).trim();
//					String gradeName = ((String) list.get(i)[3]).trim();
//					String className = ((String) list.get(i)[4]).trim();
//					if (i == 0) {
//						// 第一行为excle表头
//						Object[] head = new Object[list.get(i).length + 1];
//						for (int j = 0; j < head.length; j++) {
//							if (j != head.length - 1) {
//								head[j] = list.get(i)[j];
//							} else {
//								head[j] = "失败原因";
//							}
//						}
//						errorList.add(head);
//					} else if (schoolName == null || "".equals(schoolName.trim())) {
//						Object[] content = new Object[list.get(i).length + 1];
//						for (int j = 0; j < content.length; j++) {
//							if (j != content.length - 1) {
//								content[j] = list.get(i)[j];
//							} else {
//								content[j] = "学校为空";
//							}
//						}
//						errorList.add(content);
//						continue;
//					} else if (departName == null || "".equals(departName.trim())) {
//						Object[] content = new Object[list.get(i).length + 1];
//						for (int j = 0; j < content.length; j++) {
//							if (j != content.length - 1) {
//								content[j] = list.get(i)[j];
//							} else {
//								content[j] = "院系为空";
//							}
//						}
//						errorList.add(content);
//						continue;
//					} else if (gradeName == null || "".equals(gradeName.trim())) {
//						// 班级为空
//						Object[] content = new Object[list.get(i).length + 1];
//						for (int j = 0; j < content.length; j++) {
//							if (j != content.length - 1) {
//								content[j] = list.get(i)[j];
//							} else {
//								content[j] = "年级为空";
//							}
//						}
//						errorList.add(content);
//						continue;
//					} else if (className == null || "".equals(className.trim())) {
//						// 班级为空
//						Object[] content = new Object[list.get(i).length + 1];
//						for (int j = 0; j < content.length; j++) {
//							if (j != content.length - 1) {
//								content[j] = list.get(i)[j];
//							} else {
//								content[j] = "班级为空";
//							}
//						}
//						errorList.add(content);
//						continue;
//					} else if (!ClassPathResource.isNumeric(gradeName)) {
//						// 班级为空
//						Object[] content = new Object[list.get(i).length + 1];
//						for (int j = 0; j < content.length; j++) {
//							if (j != content.length - 1) {
//								content[j] = list.get(i)[j];
//							} else {
//								content[j] = "年级数据有误";
//							}
//						}
//						errorList.add(content);
//						continue;
//					} else {
//						if (user.getDepts() == null || user.getDepts().size() == 0) {
//							// 学校
//							Dept school = deptNameAndParentIdMap.get(schoolName + "," + "0");
//							if (school == null) {
//								school = new Dept();
//								Set<String> set = parentIdMap.get("0");
//								if (set == null) {
//									set = new HashSet<String>();
//								}
//								String extend = IdUtil.getExtend(set);
//								if (extend.length() == 0) {
//									throw new RuntimeException("学校曾用名数已超过最大限制(35个)");
//								}
//								String deptId = schoolId + extend;
//								school.setDeptId(deptId);
//								school.setCreateTime(new Date());
//								school.setFullName(schoolName);
//								school.setDeptName(schoolName);
//								school.setLevel(4);
//								school.setParentId("0");
//								deptMapper.insert(school);
//								allDeptList.add(school);
//								deptNameAndParentIdMap.put(schoolName + "," + "0", school);
//								set.add(extend);
//								parentIdMap.put("0", set);
//							}
//							Dept depart = deptNameAndParentIdMap.get(departName + "," + school.getDeptId());
//							if (depart == null) {
//								depart = new Dept();
//								String parentId = school.getDeptId();
//								Set<String> set = parentIdMap.get(parentId);
//								if (set == null) {
//									set = new HashSet<String>();
//								}
//								String departId = IdUtil.getDepart(set);
//								if (departId.equals("")) {
//									throw new RuntimeException("院系数据异常，院系已超过1000个");
//								}
//								String deptId = parentId + departId + "0";
//								depart.setDeptId(deptId);
//								depart.setParentId(parentId);
//								depart.setCreateTime(new Date());
//								depart.setFullName(school.getFullName() + "," + departName);
//								depart.setDeptName(departName);
//								depart.setLevel(school.getLevel());
//								deptMapper.insert(depart);
//								// 院系新增后，更新deptNameAndParentIdMap，allDeptList，parentIdMap
//								allDeptList.add(depart);
//								deptNameAndParentIdMap.put(departName + "," + school.getDeptId(), depart);
//								set.add(departId);
//								parentIdMap.put(parentId, set);
//							}
//
//							// 专业
//							if (majorName != null && !majorName.equals("")) {
//								Major major = majorMap.get(majorName);
//								if (major == null) {
//									major = new Major();
//									major.setMajorName(majorName);
//									majorMapper.addMajor(major);
//									majorMap.put(majorName, major);
//								}
//								if (!majorSet.contains(depart.getDeptId() + "," + major.getMajorId())) {
//									// 增加专业和院系的关系
//									Map<String, Object> map = new HashMap<String, Object>();
//									map.put("majorId", major.getMajorId());
//									map.put("deptId", depart.getDeptId());
//									majorMapper.addMajorDept(map);
//									majorSet.add(depart.getDeptId() + "," + major.getMajorId());
//								}
//							}
//
//							Dept grade = deptNameAndParentIdMap.get(gradeName + "级" + "," + depart.getDeptId());
//							if (grade == null) {
//								grade = new Dept();
//								String parentId = depart.getDeptId();
//								String deptId = "";
//								String fullName = "";
//								deptId = depart.getDeptId() + gradeName;
//								fullName = depart.getFullName() + "," + gradeName + "级";
//								grade.setDeptId(deptId);
//								grade.setDeptName(gradeName + "级");
//								grade.setParentId(parentId);
//								grade.setCreateTime(new Date());
//								grade.setFullName(fullName);
//								grade.setLevel(school.getLevel());
//								deptMapper.insert(grade);
//								// 年级新增后，更新deptNameAndParentIdMap，allDeptList
//								allDeptList.add(grade);
//								deptNameAndParentIdMap.put(gradeName + "级" + "," + depart.getDeptId(), grade);
//							}
//
//							Dept class1 = deptNameAndParentIdMap.get(className + "," + grade.getDeptId());
//							if (class1 == null) {
//								class1 = new Dept();
//								String parentId = grade.getDeptId();
//								Set<String> set = parentIdMap.get(parentId);
//								if (set == null) {
//									set = new HashSet<String>();
//								}
//								String classId = IdUtil.getClassId(set);
//								if (classId.equals("")) {
//									throw new RuntimeException("班级数据异常，同年级班级已超过100个");
//								}
//								String deptId = parentId + classId;
//								class1.setDeptId(deptId);
//								class1.setParentId(parentId);
//								class1.setCreateTime(new Date());
//								class1.setDeptName(className);
//								class1.setFullName(grade.getFullName() + "," + className);
//								class1.setLevel(school.getLevel());
//								deptMapper.insert(class1);
//								// 班级新增后，更新deptNameAndParentIdMap，allDeptList，parentIdMap
//								allDeptList.add(class1);
//								deptNameAndParentIdMap.put(className + "," + grade.getDeptId(), class1);
//								set.add(classId);
//								parentIdMap.put(parentId, set);
//							} else {
//								// 班级名重复
//								Object[] content = new Object[list.get(i).length + 1];
//								for (int j = 0; j < content.length; j++) {
//									if (j != content.length - 1) {
//										content[j] = list.get(i)[j];
//									} else {
//										content[j] = "班级名重复";
//									}
//								}
//								errorList.add(content);
//								continue;
//							}
//						} else {
//							// 学校
//							Dept school = deptNameAndParentIdMap.get(schoolName + "," + "0");
//							if (school == null) {
//								Object[] content = new Object[list.get(i).length + 1];
//								for (int j = 0; j < content.length; j++) {
//									if (j != content.length - 1) {
//										content[j] = list.get(i)[j];
//									} else {
//										content[j] = "当前用户无权限添加学校曾用名";
//									}
//								}
//								errorList.add(content);
//								continue;
//							}
//							Dept depart = null;
//							if (departSet.size() == 0) {
//								if (schoolSet.size() > 0 && !schoolSet.contains(school.getDeptId())) {
//									Object[] content = new Object[list.get(i).length + 1];
//									for (int j = 0; j < content.length; j++) {
//										if (j != content.length - 1) {
//											content[j] = list.get(i)[j];
//										} else {
//											content[j] = "无权限向该学校内导入数据";
//										}
//									}
//									errorList.add(content);
//									continue;
//								}
//								depart = deptNameAndParentIdMap.get(departName + "," + school.getDeptId());
//								if (depart == null) {
//									depart = new Dept();
//									String parentId = school.getDeptId();
//									Set<String> set = parentIdMap.get(parentId);
//									if (set == null) {
//										set = new HashSet<String>();
//									}
//									String departId = IdUtil.getDepart(set);
//									if (departId.equals("")) {
//										throw new RuntimeException("院系数据异常，院系已超过1000个");
//									}
//									String deptId = parentId + departId + "0";
//									depart.setDeptId(deptId);
//									depart.setParentId(parentId);
//									depart.setCreateTime(new Date());
//									depart.setFullName(school.getFullName() + "," + departName);
//									depart.setDeptName(departName);
//									depart.setLevel(school.getLevel());
//									deptMapper.insert(depart);
//									// 院系新增后，更新deptNameAndParentIdMap，allDeptList，parentIdMap
//									allDeptList.add(depart);
//									deptNameAndParentIdMap.put(departName + "," + school.getDeptId(), depart);
//									set.add(departId);
//									parentIdMap.put(parentId, set);
//								}
//							} else {
//								depart = deptNameAndParentIdMap.get(departName + " " + school.getDeptId());
//								if (depart == null) {
//									Object[] content = new Object[list.get(i).length + 1];
//									for (int j = 0; j < content.length; j++) {
//										if (j != content.length - 1) {
//											content[j] = list.get(i)[j];
//										} else {
//											content[j] = "院系名称找不到，当前用户无权限添加院系名称";
//										}
//									}
//									errorList.add(content);
//									continue;
//								}
//								if (departSet.size() > 0 && !departSet.contains(depart.getDeptId())) {
//									Object[] content = new Object[list.get(i).length + 1];
//									for (int j = 0; j < content.length; j++) {
//										if (j != content.length - 1) {
//											content[j] = list.get(i)[j];
//										} else {
//											content[j] = "无权限向该院系内导入数据";
//										}
//									}
//									errorList.add(content);
//									continue;
//								}
//							}
//
//							// 专业
//							if (majorName != null && !majorName.equals("")) {
//								Major major = majorMap.get(majorName);
//								if (major == null) {
//									major = new Major();
//									major.setMajorName(majorName);
//									majorMapper.addMajor(major);
//									majorMap.put(majorName, major);
//								}
//								if (!majorSet.contains(depart.getDeptId() + "," + major.getMajorId())) {
//									// 增加专业和院系的关系
//									Map<String, Object> map = new HashMap<String, Object>();
//									map.put("majorId", major.getMajorId());
//									map.put("deptId", depart.getDeptId());
//									majorMapper.addMajorDept(map);
//									majorSet.add(depart.getDeptId() + "," + major.getMajorId());
//								}
//							}
//
//							Dept grade = deptNameAndParentIdMap.get(gradeName + "级" + "," + depart.getDeptId());
//							if (grade == null) {
//								grade = new Dept();
//								String parentId = depart.getDeptId();
//								String deptId = "";
//								String fullName = "";
//								deptId = depart.getDeptId() + gradeName;
//								fullName = depart.getFullName() + "," + gradeName + "级";
//								grade.setDeptId(deptId);
//								grade.setDeptName(gradeName + "级");
//								grade.setParentId(parentId);
//								grade.setCreateTime(new Date());
//								grade.setFullName(fullName);
//								grade.setLevel(school.getLevel());
//								deptMapper.insert(grade);
//								// 年级新增后，更新deptNameAndParentIdMap，allDeptList
//								allDeptList.add(grade);
//								deptNameAndParentIdMap.put(gradeName + "级" + "," + depart.getDeptId(), grade);
//							}
//
//							Dept class1 = deptNameAndParentIdMap.get(className + "," + grade.getDeptId());
//							if (class1 == null) {
//								class1 = new Dept();
//								String parentId = grade.getDeptId();
//								Set<String> set = parentIdMap.get(parentId);
//								if (set == null) {
//									set = new HashSet<String>();
//								}
//								String classId = IdUtil.getClassId(set);
//								if (classId.equals("")) {
//									throw new RuntimeException("班级数据异常，同年级班级已超过100个");
//								}
//								String deptId = parentId + classId;
//								class1.setDeptId(deptId);
//								class1.setParentId(parentId);
//								class1.setCreateTime(new Date());
//								class1.setDeptName(className);
//								class1.setFullName(grade.getFullName() + "," + className);
//								class1.setLevel(school.getLevel());
//								deptMapper.insert(class1);
//								// 班级新增后，更新deptNameAndParentIdMap，allDeptList，parentIdMap
//								allDeptList.add(class1);
//								deptNameAndParentIdMap.put(className + "," + grade.getDeptId(), class1);
//								set.add(classId);
//								parentIdMap.put(parentId, set);
//							} else {
//								// 班级名重复
//								Object[] content = new Object[list.get(i).length + 1];
//								for (int j = 0; j < content.length; j++) {
//									if (j != content.length - 1) {
//										content[j] = list.get(i)[j];
//									} else {
//										content[j] = "班级名重复";
//									}
//								}
//								errorList.add(content);
//								continue;
//							}
//						}
//					}
//				}
//			}
//			return ExcelUtil.exportData(errorList);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

	public List<Dept> getSchool() {
		return deptMapper.getSchool();
	}

	public List<Dept> selectAllClass(String deptId) {
		return deptMapper.selectAllClass(deptId);
	}

	public DataGrid<Dept> dateGridForUser(Map<String, Object> map) {
		DataGrid<Dept> dataGrid = new DataGrid<Dept>();
		long count = deptMapper.countDept(map);
		dataGrid.setTotal(count);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<Dept> list = deptMapper.selectDeptList(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public Dept selectByNameAndParentId(Map<String, Object> map) {
		return deptMapper.selectByNameAndParentId(map);
	}

	public List<Dept> getDepart() {
		return deptMapper.getDepart();
	}

	public List<Dept> getDepart1() {
		return deptMapper.getDepart1();
	}

	public List<Dept> getByParentId(String deptId) {
		return deptMapper.getByParentId(deptId);
	}

	@Override
	public Department getById_new(long classId) {
		return deptMapper.getById_new(classId);
	}
	
	@Override
	public Dept getById(String classId) {
		return deptMapper.getById(classId);
	}

	@Override
	public void update(Dept dept) {
		try {
			if (!dept.getDeptName().equals(dept.getResourceName())) {
				deptMapper.update(dept);
				deptMapper.updateFullName(dept);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Dept> getBelong(String deptId) {
		if (deptId.length() == 6) {
			return deptMapper.getBelong1(deptId);
		} else {
			return deptMapper.getBelong2(deptId);
		}

	}

	@Override
	public List<Department> getBelong_new(String classId) {
			return deptMapper.getBelong_new(classId);
	}
	
	@Override
	public void updateBelong(long department_id,long classId) {
		Map<String,Long> map =new HashMap<String, Long>();
		map.put("department_id", department_id);
		map.put("classId", classId);
		deptMapper.updateBelong(map);
	}

	@Override
	public Dept getByAliasName(String deptId) {
		return deptMapper.getByAliasName(deptId);
	}

	@Override
	public List<Dept> selectByDeptIds(List<String> list) {
		return deptMapper.selectByDeptIds(list);
	}

	@Override
	public List<Dept> getByParentIdAndDeptIds(Map<String, Object> map) {
		return deptMapper.getByParentIdAndDeptIds(map);
	}

	@Override
	public List<Dept> selectAll1(List<Dept> list) {
		return deptMapper.selectAll1(list);
	}

	@Override
	public List<Dept> selectDepart(List<Dept> list) {
		return deptMapper.selectDepart(list);
	}

	@Override
	public List<Dept_New> selectAlldept(String school,List<Department> list) {
		// TODO Auto-generated method selectAlldeptstub
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("school", school);
		map.put("department", list);
		List<Dept_New> lists = deptMapper.selectAlldept(map);
		return lists;
	}

	@Override
	public School getSchool1() {
		// TODO Auto-generated method stub
		return deptMapper.getSchool1();
	}

	@Override
	public List<Dept_New> selectAllmajor() {
		// TODO Auto-generated method stub
		return deptMapper.selectAllmajor();
	}

	@Override
	public List<Dept_New> selectAllclass() {
		// TODO Auto-generated method stub
		return deptMapper.selectAllclass();
	}

	@Override
	public List<Dept_New> selectAllgrade() {
		// TODO Auto-generated method stub
		return deptMapper.selectAllgrade();
	}

	@Override
	public NewDeptInfo selectByNameAndAffiliation(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return deptMapper.selectByNameAndAffiliation(map);
	}

	@Override
	public void insert_new(NewDeptInfo newDeptInfo) {
		// TODO Auto-generated method stub
		deptMapper.insert_new(newDeptInfo);
	}


	@Override
	public List<Dept_New> selectmajorBydeptName(String deptName) {
		// TODO Auto-generated method stub
		return deptMapper.selectmajorBydeptName(deptName);
	}

	@Override
	public List<Dept_New> selectgradeBydeptName(String deptName) {
		// TODO Auto-generated method stub
		return deptMapper.selectgradeBydeptName(deptName);
	}

	@Override
	public List<Dept_New> selectclassBydeptName(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		return deptMapper.selectclassBydeptName(map);
	}

	@Override
	public long searchAffiliation(String affiliationName) {
		// TODO Auto-generated method stub
		return deptMapper.searchAffiliation(affiliationName);
	}

	@Override
	public String exportDept() throws IOException {
		// TODO Auto-generated method stub
		List<Object[]> objects = new ArrayList<Object[]>();
		Object[] o = new Object[36];
		o[0] = "学校名称";
		o[1] = "院系";
		o[2] = "专业";
		o[3] = "年级";
		o[4] = "班级";
		o[5] = "所属院系";
		objects.add(o);
		List<NewDeptInfo> list = deptMapper.selectAllForExport();
		if (list != null && list.size() > 0) {
			for (NewDeptInfo newDeptInfo : list) {
				Object[] o1 = new Object[36];
				o1[0] = newDeptInfo.getSchoolName();
				o1[1] = newDeptInfo.getCollege();
				o1[2] = newDeptInfo.getMajor();
				o1[3] = newDeptInfo.getGrade();
				o1[4] = newDeptInfo.getClassName();
				o1[5] = newDeptInfo.getAffiliationName();
				objects.add(o1);
			}
		}
		return ExcelUtil.exportData(objects);
	}

	@Override
	public String exportData() throws IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				List<Object[]> objects = new ArrayList<Object[]>();
				Object[] o = new Object[36];
				o[0] = "学校名称";
				o[1] = "当前院系";
				objects.add(o);
				List<Department> list = deptMapper.getCollegeDepartment();
				if (list != null && list.size() > 0) {
					for (Department department : list) {
						Object[] o1 = new Object[36];
						o1[0] = department.getSchoolName();
						o1[1] = department.getDepartmentName();
						objects.add(o1);
					}
				}
				return ExcelUtil.exportData(objects);
	}

	@Override
	public List<Department> getCollegeDepartment() {
		return deptMapper.getCollegeDepartment();
	}

	@Override
	public List<Department> getNotCollegeDepartment() {
		return deptMapper.getNotCollegeDepartment();
	}



}
