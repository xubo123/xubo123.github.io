package com.hxy.core.userinfo.service;

import org.apache.log4j.Logger;

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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.dept.dao.DeptMapper;
import com.hxy.core.dept.entity.Dept;
import com.hxy.core.dict.entity.Dict;
import com.hxy.core.dicttype.entity.DictType;
import com.hxy.core.major.dao.MajorMapper;
import com.hxy.core.major.entity.Major;
import com.hxy.core.major.entity.MajorDept;
import com.hxy.core.mobileLocal.dao.MobileLocalMapper;
import com.hxy.core.mobileLocal.dao.MobileScratchMapper;
import com.hxy.core.mobileLocal.entity.MobileLocal;
import com.hxy.core.mobileLocal.entity.MobileScratch;
import com.hxy.core.sms.dao.MsgSendMapper;
import com.hxy.core.sms.entity.MsgSend;
import com.hxy.core.user.entity.User;
import com.hxy.core.userProfile.dao.UserProfileMapper;
import com.hxy.core.userProfile.entity.UserProfile;
import com.hxy.core.userbaseinfo.dao.UserBaseInfoMapper;
import com.hxy.core.userbaseinfo.entity.UserBaseInfo;
import com.hxy.core.userinfo.dao.UserInfoMapper;
import com.hxy.core.userinfo.entity.UserInfo;
import com.hxy.system.ClassPathResource;
import com.hxy.system.ExcelUtil;
import com.hxy.system.GetDictionaryInfo;
import com.hxy.system.Global;
import com.hxy.system.IdUtil;
import com.hxy.system.PinYinUtils;
import com.hxy.system.UUID;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserInfoServiceImpl.class);

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private UserBaseInfoMapper userBaseInfoMapper;

	@Autowired
	private MajorMapper majorMapper;

	@Autowired
	private DeptMapper deptMapper;

	@Autowired
	private MobileLocalMapper mobileLocalMapper;

	@Autowired
	private MobileScratchMapper mobileScratchMapper;

	@Autowired
	private MsgSendMapper msgSendMapper;

	@Autowired
	private UserProfileMapper userProfileMapper;

	public DataGrid<UserInfo> selectByDeptId(Map<String, Object> map) {
		DataGrid<UserInfo> dataGrid = new DataGrid<UserInfo>();
		long total = userInfoMapper.countByDeptId(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<UserInfo> list = userInfoMapper.selectByDeptId(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public DataGrid<UserInfo> selectByDeptIdForAlumni(Map<String, Object> map) {
		DataGrid<UserInfo> dataGrid = new DataGrid<UserInfo>();
		long total = userInfoMapper.countByDeptIdForAlumni(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<UserInfo> list = userInfoMapper.selectByDeptIdForAlumni(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public boolean delete(String ids) {
		try {
			boolean canDelete = true;
			String[] array = ids.split(",");
			List<String> list = new ArrayList<String>();
			for (String id : array) {
				list.add(id);
			}
			List<UserInfo> userInfoList = userInfoMapper.selectByIds(list);
			for (UserInfo info : userInfoList) {
				if (info.getCheckFlag() != null && info.getCheckFlag() == 1) {
					canDelete = false;
					break;
				}
			}
			if (canDelete) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (String id : array) {
					UserProfile userProfile = userProfileMapper.selectByBaseInfoId(id);
					if (userProfile != null) {
						String baseInfoId = "";
						String groupName = "";
						if (userProfile.getBaseInfoId() != null && userProfile.getBaseInfoId().length() > 0) {
							String[] idArray = userProfile.getBaseInfoId().split(",");
							for (String baseId : idArray) {
								if (!baseId.equals(id)) {
									baseInfoId += baseId + ",";
								}
							}
							if (baseInfoId.length() > 0) {
								baseInfoId = baseInfoId.substring(0, baseInfoId.length() - 1);
							}
						}
						if (userProfile.getGroupName() != null && userProfile.getGroupName().length() > 0) {
							String[] groupArray = userProfile.getGroupName().split(",");
							for (String group : groupArray) {
								if (!group.equals(id.substring(0, 16))) {
									groupName += group + ",";
								}
							}
							if (groupName.length() > 0) {
								groupName = groupName.substring(0, groupName.length() - 1);
							}
						}
						map.put("baseInfoId", baseInfoId);
						map.put("accountNum", userProfile.getAccountNum());
						map.put("groupName", groupName);
						userProfileMapper.clearBaseInfoId(map);
					}
				}
				userInfoMapper.delete(list);
			}
			return canDelete;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void save(UserInfo userInfo, User user, int isInput) {
		//TO-FIX
		/*
		try {
			if (isInput == 0 && userInfo.getClassId() != null && !userInfo.getClassId().equals("")) {
				List<UserBaseInfo> list = userBaseInfoMapper.selectAllByDeptId(userInfo.getClassId());
				List<UserInfo> ulist = userInfoMapper.selectAllByDeptId(userInfo.getClassId());
				Set<String> set = new HashSet<String>();
				for (UserBaseInfo UserBaseInfo2 : list) {
					if (UserBaseInfo2.getUserId() != null) {
						set.add(UserBaseInfo2.getUserId().substring(16, 19));
					}
				}
				for (UserInfo UserBaseInfo2 : ulist) {
					if (UserBaseInfo2.getUserId() != null) {
						set.add(UserBaseInfo2.getUserId().substring(16, 19));
					}
				}
				String uId = IdUtil.getUserId(set);
				String userId = userInfo.getClassId() + uId;
				userInfo.setUserId(userId);
				userInfo.setNamePinyin(PinYinUtils.getQuanPin(userInfo.getUserName()));
				userInfo.setCreateTime(new Date());
				userInfoMapper.save(userInfo);
			} else if (isInput == 1 && userInfo.getSchoolName() != null && !userInfo.getSchoolName().equals("") && userInfo.getDepartName() != null
					&& !userInfo.getDepartName().equals("") && userInfo.getGradeName() != null && !userInfo.getGradeName().equals("")
					&& userInfo.getClassName() != null && !userInfo.getClassName().equals("")) {
				Map<String, Object> map = new HashMap<String, Object>();
				Dept depart = null;
				if (user.getDepts() == null || user.getDepts().size() == 0) {
					// 查找学校
					map.put("parentId", "0");
					map.put("deptName", userInfo.getSchoolName());

					// 学校
					Dept school = deptMapper.selectByNameAndParentId(map);
					if (school == null) {
						// 取所有学校名
						school = new Dept();
						List<Dept> list = deptMapper.getSchool();
						Set<String> set = new HashSet<String>();
						for (Dept dept2 : list) {
							set.add(dept2.getDeptId().substring(5, 6));
						}
						String extend = IdUtil.getExtend(set);
						if (extend.equals("")) {
							throw new RuntimeException("学校曾用名数据量已超过最大限制(35个)");
						}
						String deptId = list.get(0).getDeptId().substring(0, 5) + extend;
						school.setDeptId(deptId);
						school.setCreateTime(new Date());
						school.setFullName(userInfo.getSchoolName());
						school.setDeptName(userInfo.getSchoolName());
						school.setLevel(4);
						school.setParentId("0");
						deptMapper.insert(school);
					}

					// 院系
					map.put("parentId", school.getDeptId());
					map.put("deptName", userInfo.getDepartName());
					depart = deptMapper.selectByNameAndParentId(map);
					if (depart == null) {
						depart = new Dept();
						List<Dept> list = deptMapper.selectByDeptId(school.getDeptId());
						Set<String> set = new HashSet<String>();
						for (Dept dept2 : list) {
							if (dept2.getDeptId() != null && dept2.getDeptId().length() == 10) {
								set.add(dept2.getDeptId().substring(6, 9));
							}
						}
						String departId = IdUtil.getDepart(set);
						if (departId.equals("")) {
							throw new RuntimeException("院系已超过最大限制(999个)");
						}
						String deptId = school.getDeptId() + departId + "0";
						depart.setDeptId(deptId);
						depart.setCreateTime(new Date());
						depart.setFullName(school.getFullName() + "," + userInfo.getDepartName());
						depart.setDeptName(userInfo.getDepartName());
						depart.setLevel(4);
						depart.setParentId(school.getDeptId());
						deptMapper.insert(depart);
					}
				} else {
					List<Dept> hasDeptList = user.getDepts();
					Set<String> schoolSet = new HashSet<String>();
					Set<String> departSet = new HashSet<String>();
					for (Dept dept : hasDeptList) {
						if (dept.getDeptId().length() == 6) {
							schoolSet.add(dept.getDeptId());
						} else {
							departSet.add(dept.getDeptId());
						}
					}

					map.put("parentId", "0");
					map.put("deptName", userInfo.getSchoolName());

					// 学校
					Dept school = deptMapper.selectByNameAndParentId(map);
					if (school == null) {
						throw new RuntimeException("无权限添加学校曾用名");
					}

					// 院系
					map.put("parentId", school.getDeptId());
					map.put("deptName", userInfo.getDepartName());
					depart = deptMapper.selectByNameAndParentId(map);
					if (departSet.size() == 0) {
						if (schoolSet.size() > 0 && !schoolSet.contains(school.getDeptId())) {
							throw new RuntimeException("当前用户无权限添加学校曾用名");
						}
						// 院系
						map.put("parentId", school.getDeptId());
						map.put("deptName", userInfo.getDepartName());
						depart = deptMapper.selectByNameAndParentId(map);
						if (depart == null) {
							depart = new Dept();
							List<Dept> list = deptMapper.selectByDeptId(school.getDeptId());
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
							String deptId = school.getDeptId() + departId + "0";
							depart.setDeptId(deptId);
							depart.setCreateTime(new Date());
							depart.setFullName(school.getFullName() + "," + userInfo.getDepartName());
							depart.setDeptName(userInfo.getDepartName());
							depart.setLevel(4);
							depart.setParentId(school.getDeptId());
							deptMapper.insert(depart);
						}
					} else {
						map.put("parentId", school.getDeptId());
						map.put("deptName", userInfo.getDepartName());
						depart = deptMapper.selectByNameAndParentId(map);
						if (depart == null) {
							throw new RuntimeException("院系名称找不到，当前用户无权限添加院系名称");
						}
						if (departSet.size() > 0 && !departSet.contains(depart.getDeptId())) {
							throw new RuntimeException("无权限向该院系内添加数据");
						}
					}
				}
				// 专业
				Major major = majorMapper.getByName(userInfo.getMajorName());
				if (major == null) {
					major = new Major();
					major.setMajorName(userInfo.getMajorName());
					majorMapper.addMajor(major);
				}
				// 获取major和dept的关系
				Map<String, Object> majorAndDeptMap = new HashMap<String, Object>();
				majorAndDeptMap.put("deptId", depart.getDeptId());
				majorAndDeptMap.put("majorId", major.getMajorId());
				List<MajorDept> majorDepts = majorMapper.getMajorAndDept(majorAndDeptMap);
				if (majorDepts != null && majorDepts.size() > 0) {
					// 此处代码是为了去掉中间表的冗余数据，之前由于没有考虑去重，导致中间表大量重复数据
					if (majorDepts.size() > 1) {
						List<String> deptList = new ArrayList<String>();
						deptList.add(depart.getDeptId());
						majorMapper.deleteMajorByDeptId(deptList);
						majorMapper.addMajorDept(majorAndDeptMap);
					}
				} else {
					majorMapper.addMajorDept(majorAndDeptMap);
				}

				// 年级
				map.put("parentId", depart.getDeptId());
				map.put("deptName", userInfo.getGradeName() + "级");
				Dept grade = deptMapper.selectByNameAndParentId(map);
				if (grade == null) {
					grade = new Dept();
					String deptId = depart.getDeptId() + userInfo.getGradeName();
					grade.setDeptId(deptId);
					grade.setCreateTime(new Date());
					grade.setFullName(depart.getFullName() + "," + userInfo.getGradeName() + "级");
					grade.setDeptName(userInfo.getGradeName() + "级");
					grade.setLevel(4);
					grade.setParentId(depart.getDeptId());
					deptMapper.insert(grade);
				}

				// 班级
				map.put("parentId", grade.getDeptId());
				map.put("deptName", userInfo.getClassName());
				Dept class1 = deptMapper.selectByNameAndParentId(map);
				if (class1 == null) {
					class1 = new Dept();
					List<Dept> list = deptMapper.selectByDeptId(grade.getDeptId());
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
					String deptId = grade.getDeptId() + classId;
					class1.setDeptId(deptId);
					class1.setCreateTime(new Date());
					String fullName = grade.getFullName() + "," + userInfo.getClassName();
					class1.setFullName(fullName);
					class1.setLevel(4);
					class1.setParentId(grade.getDeptId());
					class1.setDeptName(userInfo.getClassName());
					deptMapper.insert(class1);
				}

				List<UserBaseInfo> list = userBaseInfoMapper.selectAllByDeptId(class1.getDeptId());
				List<UserInfo> ulist = userInfoMapper.selectAllByDeptId(class1.getDeptId());
				Set<String> set = new HashSet<String>();
				for (UserBaseInfo UserBaseInfo2 : list) {
					if (UserBaseInfo2.getUserId() != null) {
						set.add(UserBaseInfo2.getUserId().substring(16, 19));
					}
				}
				for (UserInfo UserBaseInfo2 : ulist) {
					if (UserBaseInfo2.getUserId() != null) {
						set.add(UserBaseInfo2.getUserId().substring(16, 19));
					}
				}
				String uId = IdUtil.getUserId(set);
				if (uId.length() == 0) {
					throw new RuntimeException("同一班级人数超过最大限制(999人)");
				}
				String userId = class1.getDeptId() + uId;
				userInfo.setUserId(userId);
				userInfo.setMajorId(major.getMajorId());
				userInfo.setNamePinyin(PinYinUtils.getQuanPin(userInfo.getUserName()));
				userInfo.setCreateTime(new Date());
				userInfoMapper.save(userInfo);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new RuntimeException(e);
		}
	*/}

	public UserInfo selectByUserId(String userId) {
		return userInfoMapper.selectByUserId(userId);
	}

	@SuppressWarnings("unchecked")
	public String importData(String url, User user) {
		//TO-FIX
		return "";
		/*
		int rownumber = 0;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			// 文件保存目录路径
			String savePath = Global.DISK_PATH;

			// 文件保存目录URL
			String saveUrl = Global.URL_DOMAIN;
			url = savePath + url.replace(saveUrl, "");
			File file = new File(url);
			List<Object[]> list = ExcelUtil.parseExcel(file);
			if (list.size() > 50000) {
				throw new RuntimeException("一次只能导入2万条数据");
			}
			List<Object[]> errorList = new ArrayList<Object[]>();
			// 整理数据开始
			List<Dept> allDeptList = deptMapper.selectAll2();
			Map<String, Dept> deptNameAndParentIdMap = new ConcurrentHashMap<String, Dept>();
			Map<String, Set<String>> ParentIdAndExtendIdMap = new ConcurrentHashMap<String, Set<String>>();
			String schoolId = "";
			if (allDeptList != null && allDeptList.size() > 0) {
				for (Dept dept2 : allDeptList) {
					Set<String> set = ParentIdAndExtendIdMap.get(dept2.getParentId());
					if (set == null) {
						set = new HashSet<String>();
					}
					if (dept2.getDeptId().length() == 6) {
						set.add(dept2.getDeptId().substring(5, 6));
						schoolId = dept2.getDeptId().substring(0, 5);
					}
					if (dept2.getDeptId().length() == 10) {
						set.add(dept2.getDeptId().substring(6, 9));
					}
					if (dept2.getDeptId().length() == 16) {
						set.add(dept2.getDeptId().substring(14, 16));
					}
					ParentIdAndExtendIdMap.put(dept2.getParentId(), set);
					// 班级
					String deptName = dept2.getDeptName();
					String parentId = dept2.getParentId();
					deptNameAndParentIdMap.put(deptName + " " + parentId, dept2);

				}
			}

			Map<String, Major> majorMap = new ConcurrentHashMap<String, Major>();

			List<Major> majorList = majorMapper.selectAll();
			if (majorList != null && majorList.size() > 0) {
				for (Major major : majorList) {
					majorMap.put(major.getMajorName(), major);
				}
			}

			Map<String, MajorDept> majorDeptMap = new ConcurrentHashMap<String, MajorDept>();
			List<MajorDept> majorDeptList = majorMapper.selectMajorAndDeptAll();
			if (majorDeptList != null && majorDeptList.size() > 0) {
				for (MajorDept majorDept : majorDeptList) {
					majorDeptMap.put(majorDept.getMajorId() + " " + majorDept.getDeptId(), majorDept);
				}
			}

			List<UserBaseInfo> userInfoList = userBaseInfoMapper.selectAll();
			List<UserInfo> uList = userInfoMapper.selectAll();
			// 每个班级下的所有学生，key为班级编号，value为学生集合
			Map<String, List<UserInfo>> classStudentNameMap = new ConcurrentHashMap<String, List<UserInfo>>();
			Map<String, List<UserBaseInfo>> classBaseStudentNameMap = new ConcurrentHashMap<String, List<UserBaseInfo>>();
			// 班级编号与学生扩展位的对应关系,key为学生编号，value为一个班中，所有学生的扩展位
			Map<String, Set<String>> classStudentIdMap = new ConcurrentHashMap<String, Set<String>>();
			// 所填数据包含数据字典，与数据字典校验
			List<DictType> dictTypes = (List<DictType>) GetDictionaryInfo.dictionaryInfoMap.get("dicts");
			List<String> studentTypeList = new ArrayList<String>();
			List<String> programLenthList = new ArrayList<String>();
			List<String> cardTypeList = new ArrayList<String>();
			for (DictType dictType : dictTypes) {
				if (dictType.getDictTypeName().equals("学历")) {
					for (Dict dict : dictType.getList()) {
						studentTypeList.add(dict.getDictName());
					}
				}
				if (dictType.getDictTypeName().equals("学制")) {
					for (Dict dict : dictType.getList()) {
						programLenthList.add(dict.getDictName());
					}
				}
				if (dictType.getDictTypeName().equals("证件类型")) {
					for (Dict dict : dictType.getList()) {
						cardTypeList.add(dict.getDictName());
					}
				}
			}
			if (userInfoList != null && userInfoList.size() > 0) {
				for (UserBaseInfo UserBaseInfo : userInfoList) {
					Set<String> set = null;
					List<UserBaseInfo> userList = null;
					String classId = UserBaseInfo.getUserId().substring(0, 16);
					if (classStudentIdMap.containsKey(classId)) {
						set = classStudentIdMap.get(classId);
						set.add(UserBaseInfo.getUserId().substring(16, 19));
					} else {
						set = new HashSet<String>();
						set.add(UserBaseInfo.getUserId().substring(16, 19));
					}
					classStudentIdMap.put(classId, set);

					if (classBaseStudentNameMap.containsKey(classId)) {
						userList = classBaseStudentNameMap.get(classId);
						userList.add(UserBaseInfo);
					} else {
						userList = new ArrayList<UserBaseInfo>();
						userList.add(UserBaseInfo);
					}
					classBaseStudentNameMap.put(classId, userList);
				}
			}
			if (uList != null && uList.size() > 0) {
				for (UserInfo userInfo : uList) {
					Set<String> set = null;
					List<UserInfo> userList = null;
					String classId = userInfo.getUserId().substring(0, 16);
					if (classStudentIdMap.containsKey(classId)) {
						set = classStudentIdMap.get(classId);
						set.add(userInfo.getUserId().substring(16, 19));
					} else {
						set = new HashSet<String>();
						set.add(userInfo.getUserId().substring(16, 19));
					}
					classStudentIdMap.put(classId, set);

					if (classStudentNameMap.containsKey(classId)) {
						userList = classStudentNameMap.get(classId);
						userList.add(userInfo);
					} else {
						userList = new ArrayList<UserInfo>();
						userList.add(userInfo);
					}
					classStudentNameMap.put(classId, userList);
				}
			}

			List<Dept> hasDeptList = user.getDepts();
			Set<String> schoolSet = new HashSet<String>();
			Set<String> departSet = new HashSet<String>();
			if (hasDeptList != null && hasDeptList.size() > 0) {
				for (Dept dept : hasDeptList) {
					if (dept.getDeptId().length() == 6) {
						schoolSet.add(dept.getDeptId());
					} else {
						departSet.add(dept.getDeptId());
					}
				}
			}

			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					rownumber = i;
					String studentNumber = ((String) list.get(i)[0]).trim();
					String userName = ((String) list.get(i)[1]).trim();
					String aliasName = ((String) list.get(i)[2]).trim();
					String schoolName = ((String) list.get(i)[3]).trim();
					String departName = ((String) list.get(i)[4]).trim();
					String gradeName = ((String) list.get(i)[5]).trim();
					String className = ((String) list.get(i)[6]).trim();
					String majorName = ((String) list.get(i)[7]).trim();
					String cardType = ((String) list.get(i)[8]).trim();
					String card = ((String) list.get(i)[9]).trim();
					String birthday = ((String) list.get(i)[10]).trim();
					String nation = ((String) list.get(i)[11]).trim();
					String nationality = ((String) list.get(i)[12]).trim();
					String sex = ((String) list.get(i)[13]).trim();
					String status = ((String) list.get(i)[14]).trim();
					String resourceArea = ((String) list.get(i)[15]).trim();
					String email = ((String) list.get(i)[16]).trim();
					String residentialArea = ((String) list.get(i)[17]).trim();
					String residentialTel = ((String) list.get(i)[18]).trim();
					String telId = ((String) list.get(i)[19]).trim();
					String entranceTime = ((String) list.get(i)[20]).trim();
					String programLength = ((String) list.get(i)[21]).trim();
					String studentType = ((String) list.get(i)[22]).trim();
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
					} else {
						if (userName == null || "".equals(userName.trim())) {
							// 姓名为空
							Object[] content = new Object[list.get(i).length + 1];
							for (int j = 0; j < content.length; j++) {
								if (j != content.length - 1) {
									content[j] = list.get(i)[j];
								} else {
									content[j] = "姓名为空";
								}
							}
							errorList.add(content);
							continue;
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
							Dept depart = null;
							if (user.getDepts() == null || user.getDepts().size() == 0) {
								// 学校
								Dept school = deptNameAndParentIdMap.get(schoolName + " " + "0");
								if (school == null) {
									school = new Dept();
									Set<String> set = ParentIdAndExtendIdMap.get("0");
									if (set == null) {
										set = new HashSet<String>();
									}
									String extend = IdUtil.getExtend(set);
									if (extend.length() == 0) {
										throw new RuntimeException("学校曾用名数已超过最大限制(35个)");
									}
									String deptId = schoolId + extend;
									school.setDeptId(deptId);
									school.setCreateTime(new Date());
									school.setFullName(schoolName);
									school.setDeptName(schoolName);
									school.setLevel(4);
									school.setParentId("0");
									deptMapper.insert(school);
									deptNameAndParentIdMap.put(schoolName + " " + "0", school);
									set.add(extend);
									ParentIdAndExtendIdMap.put("0", set);
								}
								// 院系
								depart = deptNameAndParentIdMap.get(departName + " " + school.getDeptId());
								if (depart == null) {
									depart = new Dept();
									Set<String> set = ParentIdAndExtendIdMap.get(school.getDeptId());
									if (set == null) {
										set = new HashSet<String>();
									}
									String departId = IdUtil.getDepart(set);
									if (departId.equals("")) {
										throw new RuntimeException("院系已超过最大限制(999个)");
									}
									String deptId = school.getDeptId() + departId + "0";
									depart.setDeptId(deptId);
									depart.setCreateTime(new Date());
									depart.setFullName(school.getFullName() + "," + departName);
									depart.setDeptName(departName);
									depart.setLevel(4);
									depart.setParentId(school.getDeptId());
									deptMapper.insert(depart);
									deptNameAndParentIdMap.put(departName + " " + school.getDeptId(), depart);
									set.add(departId);
									ParentIdAndExtendIdMap.put(school.getDeptId(), set);
								}

							} else {

								Dept school = deptNameAndParentIdMap.get(schoolName + " " + "0");
								if (school == null) {
									Object[] content = new Object[list.get(i).length + 1];
									for (int j = 0; j < content.length; j++) {
										if (j != content.length - 1) {
											content[j] = list.get(i)[j];
										} else {
											content[j] = "当前用户无权限添加学校曾用名";
										}
									}
									errorList.add(content);
									continue;
								}
								if (departSet.size() == 0) {
									if (schoolSet.size() > 0 && !schoolSet.contains(school.getDeptId())) {
										Object[] content = new Object[list.get(i).length + 1];
										for (int j = 0; j < content.length; j++) {
											if (j != content.length - 1) {
												content[j] = list.get(i)[j];
											} else {
												content[j] = "无权限向该学校内导入数据";
											}
										}
										errorList.add(content);
										continue;
									}
									depart = deptNameAndParentIdMap.get(departName + " " + school.getDeptId());
									if (depart == null) {
										depart = new Dept();
										Set<String> set = ParentIdAndExtendIdMap.get(school.getDeptId());
										if (set == null) {
											set = new HashSet<String>();
										}
										String departId = IdUtil.getDepart(set);
										if (departId.equals("")) {
											throw new RuntimeException("院系数据异常，院系已超过1000个");
										}
										String deptId = school.getDeptId() + departId + "0";
										depart.setDeptId(deptId);
										depart.setCreateTime(new Date());
										depart.setFullName(school.getFullName() + "," + departName);
										depart.setDeptName(departName);
										depart.setLevel(4);
										depart.setParentId(school.getDeptId());
										deptMapper.insert(depart);
										deptNameAndParentIdMap.put(departName + " " + school.getDeptId(), depart);
										set.add(departId);
										ParentIdAndExtendIdMap.put(school.getDeptId(), set);
									}

								} else {
									depart = deptNameAndParentIdMap.get(departName + " " + school.getDeptId());
									if (depart == null) {
										Object[] content = new Object[list.get(i).length + 1];
										for (int j = 0; j < content.length; j++) {
											if (j != content.length - 1) {
												content[j] = list.get(i)[j];
											} else {
												content[j] = "院系名称找不到，当前用户无权限添加院系名称";
											}
										}
										errorList.add(content);
										continue;
									}
									if (departSet.size() > 0 && !departSet.contains(depart.getDeptId())) {
										Object[] content = new Object[list.get(i).length + 1];
										for (int j = 0; j < content.length; j++) {
											if (j != content.length - 1) {
												content[j] = list.get(i)[j];
											} else {
												content[j] = "无权限向该院系内导入数据";
											}
										}
										errorList.add(content);
										continue;
									}
								}
							}

							Major major = null;
							if (majorName != null && !majorName.equals("")) {
								major = majorMap.get(majorName);
								if (major == null) {
									major = new Major();
									major.setMajorName(majorName);
									majorMapper.addMajor(major);
									majorMap.put(majorName, major);
								}
								// 获取major和dept的关系,此处待优化
								MajorDept majorDept = majorDeptMap.get(major.getMajorId() + " " + depart.getDeptId());
								if (majorDept == null) {
									majorDept = new MajorDept();
									Map<String, Object> majorAndDeptMap = new HashMap<String, Object>();
									majorAndDeptMap.put("majorId", major.getMajorId());
									majorAndDeptMap.put("deptId", depart.getDeptId());
									majorMapper.addMajorDept(majorAndDeptMap);
									majorDept.setDeptId(depart.getDeptId());
									majorDept.setMajorId(major.getMajorId());
									majorDeptMap.put(major.getMajorId() + " " + depart.getDeptId(), majorDept);
								}
							}

							// 年级
							Dept grade = deptNameAndParentIdMap.get(gradeName + "级" + " " + depart.getDeptId());
							if (grade == null) {
								grade = new Dept();
								String deptId = depart.getDeptId() + gradeName;
								grade.setDeptId(deptId);
								grade.setCreateTime(new Date());
								grade.setFullName(depart.getFullName() + "," + gradeName + "级");
								grade.setDeptName(gradeName + "级");
								grade.setLevel(4);
								grade.setParentId(depart.getDeptId());
								deptMapper.insert(grade);
								deptNameAndParentIdMap.put(gradeName + "级" + " " + depart.getDeptId(), grade);
							}

							// 班级
							Dept class1 = deptNameAndParentIdMap.get(className + " " + grade.getDeptId());
							if (class1 == null) {
								class1 = new Dept();
								Set<String> set = ParentIdAndExtendIdMap.get(grade.getDeptId());
								if (set == null) {
									set = new HashSet<String>();
								}
								String classId = IdUtil.getClassId(set);
								if (classId.equals("")) {
									throw new RuntimeException("班级数据异常，同年级班级已超过100个");
								}
								String deptId = grade.getDeptId() + classId;
								class1.setDeptId(deptId);
								class1.setCreateTime(new Date());
								String fullName = grade.getFullName() + "," + className;
								class1.setFullName(fullName);
								class1.setLevel(4);
								class1.setParentId(grade.getDeptId());
								class1.setDeptName(className);
								deptMapper.insert(class1);
								deptNameAndParentIdMap.put(className + " " + grade.getDeptId(), class1);
								set.add(classId);
								ParentIdAndExtendIdMap.put(grade.getDeptId(), set);
							}
							UserInfo userInfo = new UserInfo();
							List<UserInfo> userList = classStudentNameMap.get(class1.getDeptId());
							List<UserBaseInfo> userBaseList = classBaseStudentNameMap.get(class1.getDeptId());
							Set<String> set = classStudentIdMap.get(class1.getDeptId());
							if (userList == null) {
								userList = new ArrayList<UserInfo>();
							}
							if (userBaseList == null) {
								userBaseList = new ArrayList<UserBaseInfo>();
							}
							if (set == null) {
								set = new HashSet<String>();
							}
							boolean hasIn = false;
							boolean hasBaseIn = false;
							for (UserInfo userInfo2 : userList) {
								String birth = "";
								if (userInfo2.getBirthday() != null) {
									birth = dateFormat.format(userInfo2.getBirthday());
								}
								if (userName.equals(userInfo2.getUserName()) && className.equals(userInfo2.getClassName()) && birthday.equals(birth)) {
									hasIn = true;
									break;
								}
							}

							for (UserBaseInfo userBaseInfo : userBaseList) {
								String birth = "";
								if (userBaseInfo.getBirthday() != null) {
									birth = dateFormat.format(userBaseInfo.getBirthday());
								}
								if (userName.equals(userBaseInfo.getUserName()) && className.equals(userBaseInfo.getClassName()) && birthday.equals(birth)) {
									hasBaseIn = true;
									break;
								}
							}

							if (!hasBaseIn && !hasIn) {
								String uId = IdUtil.getUserId(set);
								if (uId.equals("")) {
									throw new RuntimeException("同一班级学生数量超过最大限制(999人)");
								}
								userInfo.setUserId(class1.getDeptId() + uId);
								userInfo.setUserName(userName);
								userInfo.setAliasname(aliasName);
								userInfo.setNamePinyin(PinYinUtils.getQuanPin(userName));
								userInfo.setCreateTime(new Date());
								userInfo.setStudentnumber(studentNumber);
								try {
									SimpleDateFormat fmt1 = new SimpleDateFormat("yyyyMMdd");
									if (entranceTime != null && !"".equals(entranceTime)) {
										Date date = fmt1.parse(entranceTime);
										userInfo.setEntranceTime(date);
									}
								} catch (Exception e) {
									Object[] content = new Object[list.get(i).length + 1];
									for (int j = 0; j < content.length; j++) {
										if (j != content.length - 1) {
											content[j] = list.get(i)[j];
										} else {
											content[j] = "入学年数据无法转换";
										}
									}
									errorList.add(content);
									continue;
								}
								if (major != null) {
									userInfo.setMajorId(major.getMajorId());
								}
								userInfo.setSex(sex);
								userInfo.setNation(nation);
								userInfo.setNationality(nationality);
								userInfo.setStudentType(studentType);
								if (studentTypeList.contains(studentType)) {
									userInfo.setStudentType(studentType);
								} else {
									userInfo.setStudentType("");
								}
								if (programLenthList.contains(programLength)) {
									userInfo.setProgramLength(programLength);
								} else {
									userInfo.setProgramLength("");
								}
								if (cardTypeList.contains(cardType)) {
									userInfo.setCardType(cardType);
								} else {
									userInfo.setCardType("");
								}
								userInfo.setCard(card);
								userInfo.setEmail(email);
								userInfo.setResidentialArea(residentialArea);
								userInfo.setResidentialTel(residentialTel);
								userInfo.setTelId(telId);
								userInfo.setResourceArea(resourceArea);
								userInfo.setStatus(status);
								userInfo.setClassName(className);
								try {
									SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
									if (birthday != null && !"".equals(birthday)) {
										Date date = fmt.parse(birthday);
										userInfo.setBirthday(date);
									}
								} catch (Exception e) {
									Object[] content = new Object[list.get(i).length + 1];
									for (int j = 0; j < content.length; j++) {
										if (j != content.length - 1) {
											content[j] = list.get(i)[j];
										} else {
											content[j] = "生日数据无法转换";
										}
									}
									errorList.add(content);
									continue;
								}
								userInfo.setCheckFlag(0);
								userInfoMapper.save(userInfo);
								// 保存成功后刷新classStudentNameMap，classStudentIdMap
								userList.add(userInfo);
								set.add(uId);
								classStudentNameMap.put(class1.getDeptId(), userList);
								classStudentIdMap.put(class1.getDeptId(), set);
							}
							if (hasIn) {
								Object[] content = new Object[list.get(i).length + 1];
								for (int j = 0; j < content.length; j++) {
									if (j != content.length - 1) {
										content[j] = list.get(i)[j];
									} else {
										content[j] = "校友库中含有相同记录的数据";
									}
								}
								errorList.add(content);
								continue;
							}
							if (hasBaseIn) {
								Object[] content = new Object[list.get(i).length + 1];
								for (int j = 0; j < content.length; j++) {
									if (j != content.length - 1) {
										content[j] = list.get(i)[j];
									} else {
										content[j] = "基础库中含有相同记录的数据";
									}
								}
								errorList.add(content);
								continue;
							}
						}
					}
				}
			}
			System.gc();
			return ExcelUtil.exportData(errorList);
		} catch (Exception e) {
			String str = "";
			if (rownumber > 0) {
				str = "第" + rownumber + "行数据导致数据导入失败";
			} else {
				str = e.getMessage();
			}
			throw new RuntimeException(str, e);
		}
	*/}

	public List<UserInfo> selectAllUserList() {
		return userInfoMapper.selectUserToGetTel();
	}

	/** --查询所有有手机号的用户并且分页-- **/
	public List<UserInfo> selectUserToGetTelPage(Map<String, Object> map) {
		return userInfoMapper.selectUserToGetTelPage(map);
	}

	public List<UserInfo> selectUserByClassIdAndName(String userName, String classId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("classId", classId);
		return userInfoMapper.selectUserByClassIdAndName(map);
	}

	public void updateUserAccountNum(UserInfo userInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userInfo.getUserId());
		map.put("accountNum", userInfo.getAccountNum());
		userInfoMapper.updateUserAccountNum(map);
	}

	public UserInfo selectAllProByUserId(String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		return userInfoMapper.selectAllProByUserId(map);
	}

	public List<UserInfo> selectUserByClassId(String classId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("classId", classId);
		return userInfoMapper.selectAllUserByClassId(map);
	}

	public UserInfo selectUserInfoByGmidAndName(Map<String, Object> map) {
		return userInfoMapper.selectUserInfo(map);
	}

	public int updateUserTelId(Map<String, Object> map, UserInfo userinfo) {
		if (userinfo == null) {
			return -8;
		}

		String submitTelId = map.get("telId").toString();
		String telId = userinfo.getTelId();
		// System.out.println("-------------------------------------------------------");
		// System.out.println("基础数据表中的电话号码是"+telId);
		// System.out.println("提交的电话号码是"+submitTelId);
		// System.out.println("-------------------------------------------------------");
		if (telId == null || "".equals(telId)) {
			return userInfoMapper.updateTelId(map);
		} else if (!submitTelId.equals(telId)) {
			if (userinfo.getUseTime() == null) {
				if (map.get("useTime") != null) {
					return userInfoMapper.updateTelId(map);
				} else {
					// 返回1代表不做更新处理
					return -8;
				}
			} else if (userinfo.getUseTime().before((Date) map.get("useTime"))) {
				return userInfoMapper.updateTelId(map);
			}
		}
		return -8;
	}

	public boolean selectUserInClass(String classId, List<String> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("classId", classId);
		long count = userInfoMapper.selectUserInClass(map);
		if (count == list.size()) {
			return true;
		}
		return false;
	}

	public List<UserInfo> selectByUserName(String userName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		return userInfoMapper.selectByUserName(map);
	}

	public List<UserInfo> selectCard(List<String> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		return userInfoMapper.selectCard(map);
	}

	public void update(UserInfo userInfo, User user, int isInput) {
		//TO-FIX
		/*
		if (userInfo.getCheckFlag() == 1) {
			userInfoMapper.updateOthers(userInfo);
			UserBaseInfo userBaseInfo = new UserBaseInfo();
			BeanUtils.copyProperties(userInfo, userBaseInfo);
			userBaseInfoMapper.updateOthers(userBaseInfo);
		} else {
			if (isInput == 0 && userInfo.getUserId().substring(0, 16).equals(userInfo.getClassId())) {
				userInfoMapper.update(userInfo);
			} else if (isInput == 0 && userInfo.getClassId() != null && !userInfo.getClassId().equals("")) {
				// 机构ID更改
				List<UserBaseInfo> list = userBaseInfoMapper.selectAllByDeptId(userInfo.getClassId());
				List<UserInfo> ulist = userInfoMapper.selectAllByDeptId(userInfo.getClassId());
				Set<String> set = new HashSet<String>();
				for (UserBaseInfo UserBaseInfo2 : list) {
					if (UserBaseInfo2.getUserId() != null) {
						set.add(UserBaseInfo2.getUserId().substring(16, 19));
					}
				}
				for (UserInfo UserBaseInfo2 : ulist) {
					if (UserBaseInfo2.getUserId() != null) {
						set.add(UserBaseInfo2.getUserId().substring(16, 19));
					}
				}
				String uId = IdUtil.getUserId(set);
				if (uId.length() == 0) {
					throw new RuntimeException("班级人数超过最大限制(999人)");
				}
				String userId = userInfo.getClassId() + uId;
				userInfo.setNewUserId(userId);
				userInfoMapper.update(userInfo);
				UserProfile userProfile = userProfileMapper.selectByBaseInfoId(userInfo.getUserId());
				String baseInfoId = "";
				String groupName = "";
				if (userProfile != null) {
					String[] idArray = userProfile.getBaseInfoId().split(",");
					for (String baseId : idArray) {
						if (baseId.equals(userInfo.getUserId())) {
							baseInfoId += userId + ",";
						} else {
							baseInfoId += baseId + ",";
						}
					}
					if (baseInfoId.length() > 0) {
						baseInfoId = baseInfoId.substring(0, baseInfoId.length() - 1);
					}
					String[] groupArray = userProfile.getGroupName().split(",");
					for (String group : groupArray) {
						if (group.equals(userInfo.getUserId().substring(0, 16))) {
							groupName += userId.substring(0, 16) + ",";
						} else {
							groupName += group + ",";
						}
					}
					if (groupName.length() > 0) {
						groupName = groupName.substring(0, groupName.length() - 1);
					}
				}
				UserProfile newUserProfile = new UserProfile();
				newUserProfile.setName(userInfo.getUserName());
				if (userInfo.getSex() != null && userInfo.getSex().equals("男")) {
					newUserProfile.setSex("0");
				} else {
					newUserProfile.setSex("1");
				}
				newUserProfile.setBaseInfoId(userInfo.getUserId());
				newUserProfile.setNewBaseInfoId(baseInfoId);
				newUserProfile.setGroupName(groupName);
				userProfileMapper.updateBase(newUserProfile);
			} else if (isInput == 1 && userInfo.getSchoolName() != null && !userInfo.getSchoolName().equals("") && userInfo.getDepartName() != null
					&& !userInfo.getDepartName().equals("") && userInfo.getGradeName() != null && !userInfo.getGradeName().equals("")
					&& userInfo.getClassName() != null && !userInfo.getClassName().equals("")) {
				Map<String, Object> map = new HashMap<String, Object>();
				Dept depart = null;
				if (user.getDepts() == null || user.getDepts().size() == 0) {
					// 查找学校
					map.put("parentId", "0");
					map.put("deptName", userInfo.getSchoolName());

					// 学校
					Dept school = deptMapper.selectByNameAndParentId(map);
					if (school == null) {
						// 取所有学校名
						school = new Dept();
						List<Dept> list = deptMapper.getSchool();
						Set<String> set = new HashSet<String>();
						for (Dept dept2 : list) {
							set.add(dept2.getDeptId().substring(5, 6));
						}
						String extend = IdUtil.getExtend(set);
						if (extend.equals("")) {
							throw new RuntimeException("学校曾用名数据量已超过最大限制(35个)");
						}
						String deptId = list.get(0).getDeptId().substring(0, 5) + extend;
						school.setDeptId(deptId);
						school.setCreateTime(new Date());
						school.setFullName(userInfo.getSchoolName());
						school.setDeptName(userInfo.getSchoolName());
						school.setLevel(4);
						school.setParentId("0");
						deptMapper.insert(school);
					}

					// 院系
					map.put("parentId", school.getDeptId());
					map.put("deptName", userInfo.getDepartName());
					depart = deptMapper.selectByNameAndParentId(map);
					if (depart == null) {
						depart = new Dept();
						List<Dept> list = deptMapper.selectByDeptId(school.getDeptId());
						Set<String> set = new HashSet<String>();
						for (Dept dept2 : list) {
							if (dept2.getDeptId() != null && dept2.getDeptId().length() == 10) {
								set.add(dept2.getDeptId().substring(6, 9));
							}
						}
						String departId = IdUtil.getDepart(set);
						if (departId.equals("")) {
							throw new RuntimeException("院系已超过最大限制(999个)");
						}
						String deptId = school.getDeptId() + departId + "0";
						depart.setDeptId(deptId);
						depart.setCreateTime(new Date());
						depart.setFullName(school.getFullName() + "," + userInfo.getDepartName());
						depart.setDeptName(userInfo.getDepartName());
						depart.setLevel(4);
						depart.setParentId(school.getDeptId());
						deptMapper.insert(depart);
					}

				} else {
					List<Dept> hasDeptList = user.getDepts();
					Set<String> schoolSet = new HashSet<String>();
					Set<String> departSet = new HashSet<String>();
					for (Dept dept : hasDeptList) {
						if (dept.getDeptId().length() == 6) {
							schoolSet.add(dept.getDeptId());
						} else {
							departSet.add(dept.getDeptId());
						}
					}

					map.put("parentId", "0");
					map.put("deptName", userInfo.getSchoolName());

					// 学校
					Dept school = deptMapper.selectByNameAndParentId(map);
					if (school == null) {
						throw new RuntimeException("无权限添加学校曾用名");
					}

					if (departSet.size() == 0) {
						if (schoolSet.size() > 0 && !schoolSet.contains(school.getDeptId())) {
							throw new RuntimeException("当前用户无权限添加学校曾用名");
						}
						// 院系
						map.put("parentId", school.getDeptId());
						map.put("deptName", userInfo.getDepartName());
						depart = deptMapper.selectByNameAndParentId(map);
						if (depart == null) {
							depart = new Dept();
							List<Dept> list = deptMapper.selectByDeptId(school.getDeptId());
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
							String deptId = school.getDeptId() + departId + "0";
							depart.setDeptId(deptId);
							depart.setCreateTime(new Date());
							depart.setFullName(school.getFullName() + "," + userInfo.getDepartName());
							depart.setDeptName(userInfo.getDepartName());
							depart.setLevel(4);
							depart.setParentId(school.getDeptId());
							deptMapper.insert(depart);
						} else {
							map.put("parentId", school.getDeptId());
							map.put("deptName", userInfo.getDepartName());
							depart = deptMapper.selectByNameAndParentId(map);
							if (depart == null) {
								throw new RuntimeException("院系名称找不到，当前用户无权限添加院系名称");
							}
							if (departSet.size() > 0 && !departSet.contains(depart.getDeptId())) {
								throw new RuntimeException("无权限向该院系内添加数据");
							}
						}
					}
				}

				// 专业
				Major major = majorMapper.getByName(userInfo.getMajorName());
				if (major == null) {
					major = new Major();
					major.setMajorName(userInfo.getMajorName());
					majorMapper.addMajor(major);
				}
				// 获取major和dept的关系
				Map<String, Object> majorAndDeptMap = new HashMap<String, Object>();
				majorAndDeptMap.put("deptId", depart.getDeptId());
				majorAndDeptMap.put("majorId", major.getMajorId());
				List<MajorDept> majorDepts = majorMapper.getMajorAndDept(majorAndDeptMap);
				if (majorDepts != null && majorDepts.size() > 0) {
					// 此处代码是为了去掉中间表的冗余数据，之前由于没有考虑去重，导致中间表大量重复数据
					if (majorDepts.size() > 1) {
						List<String> deptList = new ArrayList<String>();
						deptList.add(depart.getDeptId());
						majorMapper.deleteMajorByDeptId(deptList);
						majorMapper.addMajorDept(majorAndDeptMap);
					}
				} else {
					majorMapper.addMajorDept(majorAndDeptMap);
				}

				// 年级
				map.put("parentId", depart.getDeptId());
				map.put("deptName", userInfo.getGradeName() + "级");
				Dept grade = deptMapper.selectByNameAndParentId(map);
				if (grade == null) {
					grade = new Dept();
					String deptId = depart.getDeptId() + userInfo.getGradeName();
					grade.setDeptId(deptId);
					grade.setCreateTime(new Date());
					grade.setFullName(depart.getFullName() + "," + userInfo.getGradeName() + "级");
					grade.setDeptName(userInfo.getGradeName() + "级");
					grade.setLevel(4);
					grade.setParentId(depart.getDeptId());
					deptMapper.insert(grade);
				}

				// 班级
				map.put("parentId", grade.getDeptId());
				map.put("deptName", userInfo.getClassName());
				Dept class1 = deptMapper.selectByNameAndParentId(map);
				if (class1 == null) {
					class1 = new Dept();
					List<Dept> list = deptMapper.selectByDeptId(grade.getDeptId());
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
					String deptId = grade.getDeptId() + classId;
					class1.setDeptId(deptId);
					class1.setCreateTime(new Date());
					String fullName = grade.getFullName() + "," + userInfo.getClassName();
					class1.setFullName(fullName);
					class1.setLevel(4);
					class1.setParentId(grade.getDeptId());
					class1.setDeptName(userInfo.getClassName());
					deptMapper.insert(class1);
				}

				List<UserBaseInfo> list = userBaseInfoMapper.selectAllByDeptId(class1.getDeptId());
				List<UserInfo> ulist = userInfoMapper.selectAllByDeptId(class1.getDeptId());
				Set<String> set = new HashSet<String>();
				for (UserBaseInfo UserBaseInfo2 : list) {
					if (UserBaseInfo2.getUserId() != null) {
						set.add(UserBaseInfo2.getUserId().substring(16, 19));
					}
				}
				for (UserInfo UserBaseInfo2 : ulist) {
					if (UserBaseInfo2.getUserId() != null) {
						set.add(UserBaseInfo2.getUserId().substring(16, 19));
					}
				}
				String uId = IdUtil.getUserId(set);
				if (uId.length() == 0) {
					throw new RuntimeException("同一班级人数超过最大限制(999人)");
				}
				String userId = class1.getDeptId() + uId;
				userInfo.setNewUserId(userId);
				userInfo.setMajorId(major.getMajorId());
				userInfoMapper.update(userInfo);
				UserProfile userProfile = userProfileMapper.selectByBaseInfoId(userInfo.getUserId());
				String baseInfoId = "";
				String groupName = "";
				if (userProfile != null) {
					String[] idArray = userProfile.getBaseInfoId().split(",");
					for (String baseId : idArray) {
						if (baseId.equals(userInfo.getUserId())) {
							baseInfoId += userId + ",";
						} else {
							baseInfoId += baseId + ",";
						}
					}
					if (baseInfoId.length() > 0) {
						baseInfoId = baseInfoId.substring(0, baseInfoId.length() - 1);
					}
					String[] groupArray = userProfile.getGroupName().split(",");
					for (String group : groupArray) {
						if (group.equals(userInfo.getUserId().substring(0, 16))) {
							groupName += userId.substring(0, 16) + ",";
						} else {
							groupName += group + ",";
						}
					}
					if (groupName.length() > 0) {
						groupName = groupName.substring(0, groupName.length() - 1);
					}
				}
				UserProfile newUserProfile = new UserProfile();
				newUserProfile.setName(userInfo.getUserName());
				if (userInfo.getSex() != null && userInfo.getSex().equals("男")) {
					newUserProfile.setSex("0");
				} else {
					newUserProfile.setSex("1");
				}
				newUserProfile.setBaseInfoId(userInfo.getUserId());
				newUserProfile.setNewBaseInfoId(baseInfoId);
				newUserProfile.setGroupName(groupName);
				userProfileMapper.updateBase(newUserProfile);
			}
		}
	*/}

	public void update(UserInfo userInfo) {
		this.userInfoMapper.update(userInfo);
	}

	public DataGrid<UserInfo> dataGridFor(Map<String, Object> map) {
		DataGrid<UserInfo> dataGrid = new DataGrid<UserInfo>();
		long total = userInfoMapper.countFor(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<UserInfo> list = userInfoMapper.selectListFor(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	@Override
	public void saveFromBase(Map<String, Object> map) {
		try {
			List<UserBaseInfo> list = userBaseInfoMapper.selectByDeptIdForImportAll(map);
			for (UserBaseInfo baseInfo : list) {
				UserInfo userInfo = new UserInfo();
				BeanUtils.copyProperties(baseInfo, userInfo);
				userInfo.setCheckFlag(1);
				userInfoMapper.save(userInfo);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateIdea(UserInfo userInfo) {
		try {
			userInfoMapper.updateIdea(userInfo);
			if (userInfo.getCheckFlag() == 1) {
				UserInfo info = userInfoMapper.selectByUserId(userInfo.getUserId());
				UserBaseInfo baseInfo = new UserBaseInfo();
				BeanUtils.copyProperties(info, baseInfo);
				userBaseInfoMapper.save(baseInfo);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean deleteAll(Map<String, Object> map) {
		boolean canDelete = true;
		List<UserInfo> list = userInfoMapper.selectByDeptIdAll(map);
		for (UserInfo info : list) {
			if (info.getCheckFlag() != null && info.getCheckFlag() == 1) {
				canDelete = false;
				break;
			}
		}
		if (canDelete) {
			if (list != null && list.size() > 0) {
				for (UserInfo userInfo : list) {
					UserProfile userProfile = userProfileMapper.selectByBaseInfoId(userInfo.getUserId());
					if (userProfile != null) {
						String baseInfoId = "";
						String groupName = "";
						if (userProfile.getBaseInfoId() != null && userProfile.getBaseInfoId().length() > 0) {
							String[] idArray = userProfile.getBaseInfoId().split(",");
							for (String baseId : idArray) {
								if (!baseId.equals(userInfo.getUserId())) {
									baseInfoId += baseId + ",";
								}
							}
							if (baseInfoId.length() > 0) {
								baseInfoId = baseInfoId.substring(0, baseInfoId.length() - 1);
							}
						}
						if (userProfile.getGroupName() != null && userProfile.getGroupName().length() > 0) {
							String[] groupArray = userProfile.getGroupName().split(",");
							for (String group : groupArray) {
								if (!group.equals(userInfo.getUserId().substring(0, 16))) {
									groupName += group + ",";
								}
							}
							if (groupName.length() > 0) {
								groupName = groupName.substring(0, groupName.length() - 1);
							}
						}
						map.put("baseInfoId", baseInfoId);
						map.put("accountNum", userProfile.getAccountNum());
						map.put("groupName", groupName);
						userProfileMapper.clearBaseInfoId(map);
					}
				}
			}
			userInfoMapper.deleteByDeptIdAll(map);
		}
		return canDelete;
	}

	/** --校友管理导出excel-- **/
	@Override
	public String export(Map<String, Object> map) throws IOException {
		List<Object[]> objects = new ArrayList<Object[]>();
		Object[] o = new Object[36];
		o[0] = "学号";
		o[1] = "姓名";
		o[2] = "曾用名";
		o[3] = "学校";
		o[4] = "院系";
		o[5] = "年级";
		o[6] = "班级";
		o[7] = "专业";
		o[8] = "证件类型";
		o[9] = "证件号码";
		o[10] = "出生年月";
		o[11] = "民族";
		o[12] = "国籍";
		o[13] = "性别";
		o[14] = "状态";
		o[15] = "籍贯";
		o[16] = "邮箱";
		o[17] = "家庭地址";
		o[18] = "固定电话";
		o[19] = "手机号码";
		o[20] = "入学日期";
		o[21] = "学制";
		o[22] = "学历";
		// 以下为新增
		o[23] = "政治面貌";
		o[24] = "毕业时间";
		o[25] = "QQ";
		o[26] = "微博";
		o[27] = "个人网站";
		o[28] = "通讯地址";
		o[29] = "工作单位";
		o[30] = "职务";
		o[31] = "所在行业";
		o[32] = "企业性质";
		o[33] = "单位电话";
		o[34] = "单位地址";
		o[35] = "备注";
		objects.add(o);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		List<UserInfo> list = userInfoMapper.selectByDeptIdForExport(map);
		if (list != null && list.size() > 0) {
			for (UserInfo userInfo : list) {
				Object[] o1 = new Object[36];
				o1[0] = userInfo.getStudentnumber();
				o1[1] = userInfo.getUserName();
				o1[2] = userInfo.getAliasname();
				o1[3] = userInfo.getSchoolName();
				o1[4] = userInfo.getDepartName();
				o1[5] = userInfo.getGradeName().substring(0, userInfo.getGradeName().length() - 1);
				o1[6] = userInfo.getClassName();
				o1[7] = userInfo.getMajorName();
				o1[8] = "";
				o1[9] = "";
				if (userInfo.getBirthday() != null) {
					o1[10] = dateFormat.format(userInfo.getBirthday());
				} else {
					o1[10] = "";
				}
				o1[11] = userInfo.getNation();
				o1[12] = userInfo.getNationality();
				o1[13] = userInfo.getSex();
				o1[14] = userInfo.getStatus();
				o1[15] = userInfo.getResourceArea();
				o1[16] = userInfo.getEmail();
				o1[17] = userInfo.getResidentialArea();
				o1[18] = userInfo.getResidentialTel();
				o1[19] = userInfo.getTelId();
				if (userInfo.getEntranceTime() != null) {
					o1[20] = dateFormat.format(userInfo.getEntranceTime());
				} else {
					o1[20] = "";
				}
				o1[21] = userInfo.getProgramLength();
				o1[22] = userInfo.getStudentType();

				o1[23] = userInfo.getPolitical();
				o1[24] = userInfo.getGraduationTime();
				o1[25] = userInfo.getQq();
				o1[26] = userInfo.getWeibo();
				o1[27] = userInfo.getPersonalWebsite();
				o1[28] = userInfo.getMailingAddress();
				o1[29] = userInfo.getWorkUnit();
				o1[30] = userInfo.getPosition();
				o1[31] = userInfo.getIndustryType();
				o1[32] = userInfo.getEnterprise();
				o1[33] = userInfo.getWorkTel();
				o1[34] = userInfo.getWorkAddress();
				o1[35] = userInfo.getRemarks();

				objects.add(o1);
			}
		}
		return ExcelUtil.exportData(objects);
	}

	@Override
	public List<UserInfo> getUserInfoByUserIdForAlumni(String userId) {
		String[] s = userId.split(",");
		List<String> list = new ArrayList<String>();
		for (String str : s) {
			list.add(str);
		}
		return userInfoMapper.getUserInfoByUserIdForAlumni(list);
	}

	public List<UserInfo> selectByAccountNum(Map<String, Object> map) {
		return userInfoMapper.selectByAccountNum(map);
	}

	@Override
	public void updateMobileLocal() {
		try {
			List<UserInfo> list = userInfoMapper.selectMobileLocalIsNull();
			if (list != null && list.size() > 0) {
				for (UserInfo userInfo : list) {
					if (userInfo.getTelId().length() == 11) {
						String mobileNumber = userInfo.getTelId().substring(0, 7);
						MobileLocal mobileLocalObj = mobileLocalMapper.selectByMobileNumber(mobileNumber);
						if (mobileLocalObj != null) {
							// 找到归属地,删掉暂存表的数据
							mobileScratchMapper.delete(mobileNumber);
							// 更新用户表
							userInfo.setMobileLocal(mobileLocalObj.getMobileArea());
							userInfoMapper.updateMobileLocal(userInfo);
						} else {
							// 找不到归属地入暂存表
							MobileScratch mobileScratch = mobileScratchMapper.selectByMobileNumber(mobileNumber);
							if (mobileScratch == null) {
								mobileScratch = new MobileScratch();
								mobileScratch.setMobileNumber(mobileNumber);
								mobileScratchMapper.insert(mobileScratch);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendBirthdaySms() {
		try {
			List<UserInfo> list = userInfoMapper.selectBirthday();
			if (list != null && list.size() > 0 && Global.smsBirthdayTemplate != null && Global.smsBirthdayTemplate.length() > 0) {
				for (UserInfo userInfo : list) {
					MsgSend msgSend = new MsgSend();
					String content = Global.smsBirthdayTemplate;
					content = content.replace("${0}", userInfo.getUserName());
					if (Global.sign != null && Global.sign.length() > 0) {
						content = "【" + Global.sign + "】" + content;
					}
					msgSend.setContent(content);
					msgSend.setMessagegroup(UUID.getMsgGroup());
					msgSend.setTelphone(userInfo.getTelId());
					msgSend.setStatues(9);
					msgSend.setSendtime(new Date());
					msgSend.setMsgType(2);
					int countNumber = 0;
					if (content.length() % 67 == 0) {
						countNumber = content.length() / 67;
					} else {
						countNumber = content.length() / 67 + 1;
					}
					msgSend.setCountNumber(countNumber);
					msgSendMapper.insertMsg(msgSend);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<UserInfo> selectByAccountNum2FullName(String accountNum) {
		return userInfoMapper.selectByAccountNum2FullName(accountNum);
	}

	@Override
	public List<UserInfo> selectUserInfoByName(String name) {
		return userInfoMapper.selectUserInfoByName(name);
	}

	@Override
	public void updateFromUserProfile() {
		List<UserProfile> list = userProfileMapper.selectAll();
		for (UserProfile profile : list) {
			UserInfo userInfo = new UserInfo();
			userInfo.setAccountNum(profile.getAccountNum());
			userInfo.setResidentialArea(profile.getAddress());
			userInfo.setHobbies(profile.getHobby());
			userInfo.setEmail(profile.getEmail());
			userInfo.setAlumniId(profile.getAlumni_id());
			userInfo.setPosition(profile.getPosition());
			userInfo.setWorkUnit(profile.getWorkUtil());
			userInfo.setIndustryType(profile.getProfession());
			userInfo.setTelId(profile.getPhoneNum());
			userInfo.setPicUrl(profile.getPicture());
			userInfoMapper.updateFromUserProfile(userInfo);
		}
	}

	@Override
	public void updateTwoWay() {
		// 查询号码不为空的用户
		List<UserInfo> list = userInfoMapper.selectByMobile();
		for (UserInfo userInfo : list) {
			UserBaseInfo userBaseInfo = new UserBaseInfo();
			BeanUtils.copyProperties(userInfo, userBaseInfo);
			userBaseInfoMapper.updateMobile(userBaseInfo);
		}

		List<UserBaseInfo> list1 = userBaseInfoMapper.selectByMobile();
		for (UserBaseInfo userBaseInfo : list1) {
			UserInfo userInfo = new UserInfo();
			BeanUtils.copyProperties(userBaseInfo, userInfo);
			userInfoMapper.updateMobile(userInfo);
		}
	}
}
