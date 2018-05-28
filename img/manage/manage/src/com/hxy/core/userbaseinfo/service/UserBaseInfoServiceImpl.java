package com.hxy.core.userbaseinfo.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.classInfo.dao.ClassInfoMapper;
import com.hxy.core.classInfo.entity.ClassInfo;
import com.hxy.core.user.entity.User;
import com.hxy.core.userbaseinfo.dao.UserBaseInfoMapper;
import com.hxy.core.userbaseinfo.entity.UserBaseInfo;
import com.hxy.system.ExcelUtil;
import com.hxy.system.Global;
import com.hxy.system.PinYinUtils;
import com.hxy.util.ChatGroup;
import com.hxy.util.GroupRegister;

@Service("userBaseInfoService")
public class UserBaseInfoServiceImpl implements UserBaseInfoService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserBaseInfoServiceImpl.class);

	@Autowired
	private UserBaseInfoMapper userBaseInfoMapper;

	@Autowired
	private ClassInfoMapper classInfoMapper;


	public DataGrid<UserBaseInfo> dataGrid(Map<String, Object> map) {
		DataGrid<UserBaseInfo> dataGrid = new DataGrid<UserBaseInfo>();
		long total = userBaseInfoMapper.count(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<UserBaseInfo> list = userBaseInfoMapper.query(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public void save(UserBaseInfo userBaseInfo) {
		userBaseInfoMapper.save(userBaseInfo);
	}
	
	public void update(UserBaseInfo userBaseInfo){
		userBaseInfoMapper.update(userBaseInfo);
	}

	public void delete(String ids) {
		String[] array = ids.split(",");
		List<String> list = new ArrayList<String>();
		for (String id : array) {
			list.add(id);
		}
		userBaseInfoMapper.delete(list);
	}
	
	
	public void deleteQuery(Map<String, Object> map) {
		String regflag = (String)map.get("regflag");
		if(regflag == null || regflag.isEmpty()) {
			//检查是否有注册的用户
			map.put("regflag", 1);
			long c = userBaseInfoMapper.count(map);
			if(c > 0){
				throw new RuntimeException("无法删除已注册的用户。");
			}
		} else if(regflag.equals("1")) {
			throw new RuntimeException("不能删除已注册的用户。");
		}
		map.put("regflag", "");
			
		userBaseInfoMapper.deleteQuery(map);		
	}


	public UserBaseInfo getById(long user_id) {
		return userBaseInfoMapper.getById(user_id);
	}
	

	public List<UserBaseInfo> getByClass(long class_id) {
		return userBaseInfoMapper.getByClass(class_id);
	}

	public DataGrid<UserBaseInfo> dataGridSum(Map<String, Object> map) {
		DataGrid<UserBaseInfo> dataGrid = new DataGrid<UserBaseInfo>();
		long total = userBaseInfoMapper.countSum(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<UserBaseInfo> list = userBaseInfoMapper.querySum(map);
		dataGrid.setRows(list);
		return dataGrid;
	}
	public DataGrid<UserBaseInfo> dataGridSumForAlumni(Map<String, Object> map) {
		DataGrid<UserBaseInfo> dataGrid = new DataGrid<UserBaseInfo>();
		long total = userBaseInfoMapper.countSumForAlumni(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<UserBaseInfo> list = userBaseInfoMapper.querySumForAlumni(map);
		dataGrid.setRows(list);
		return dataGrid;
	}
	public List<UserBaseInfo> getAlumniInfo(Map<String, Object> map){
		List<UserBaseInfo> list = userBaseInfoMapper.getPhoneOrEmailList(map);
		return userBaseInfoMapper.getPhoneOrEmailList(map);
	}
	public List<UserBaseInfo> getSumByIds(String user_ids) {
		String[] array = user_ids.split(",");
		List<String> list = new ArrayList<String>();
		for (String id : array) {
			list.add(id);
		}
		return userBaseInfoMapper.getSumByIds(list);
	}
	public List<UserBaseInfo> getAlumniInfoByIds(String user_ids) {
		String[] array = user_ids.split(",");
		List<String> list = new ArrayList<String>();
		for (String id : array) {
			list.add(id);
		}
		return userBaseInfoMapper.getAlumniInfoByIds(list);
	}

	@Override
	public String export(Map<String, Object> map) throws IOException {
		List<Object[]> objects = new ArrayList<Object[]>();
		Object[] o = new Object[36];
		o[0] = "学号";
		o[1] = "姓名";	
		o[2] = "曾用名";
		o[3] = "性别";
		o[4] = "学校";
		o[5] = "院系";
		o[6] = "专业";
		o[7] = "年级";
		o[8] = "班级";
		o[9] = "籍贯";
		o[10] = "入学时间";
		o[11] = "毕业时间";	
		o[12] = "学历";
		o[13] = "学制";
		o[14] = "生日";
		o[15] = "国籍";
		o[16] = "民族";
		o[17] = "证件类型";
		o[18] = "证件号码";
		o[19] = "电话号码";
		o[20] = "邮箱";
		o[21] = "QQ";
		o[22] = "微博";
		o[23] = "家庭地址";
		o[24] = "家庭电话";
		o[25] = "工作单位";
		o[26] = "所属行业";
		o[27] = "政治面貌";	
		o[28] = "兴趣爱好";
		o[29] = "状态";
		o[30] = "备注";

		objects.add(o);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		List<UserBaseInfo> list = userBaseInfoMapper.query(map);
		if (list != null && list.size() > 0) {
			for (UserBaseInfo userBaseInfo : list) {
				Object[] o1 = new Object[31];
				o1[0] = userBaseInfo.getStudentNumber();
				o1[1] = userBaseInfo.getUser_name();
				o1[2] = userBaseInfo.getAliasname();
				o1[3] = userBaseInfo.getSex();
				o1[4] = userBaseInfo.getSchoolName();
				o1[5] = userBaseInfo.getCollege();
				o1[6] = userBaseInfo.getMajor();
				o1[7] = userBaseInfo.getGrade().substring(0, userBaseInfo.getGrade().length() - 1);
				o1[8] = userBaseInfo.getClassName();
				o1[9] = userBaseInfo.getResourceArea();
				if (userBaseInfo.getEntranceTime() != null) {
					o1[10] = dateFormat.format(userBaseInfo.getEntranceTime());
				} else {
					o1[10] = "";
				}
				if (userBaseInfo.getGraduationTime() != null) {
					o1[11] = dateFormat.format(userBaseInfo.getGraduationTime());
				} else {
					o1[11] = "";
				}
				o1[12] = userBaseInfo.getStudentType();
				o1[13] = userBaseInfo.getProgramLength();
				if (userBaseInfo.getBirthday() != null) {
					o1[14] = dateFormat.format(userBaseInfo.getBirthday());
				} else {
					o1[14] = "";
				}
				o1[15] = userBaseInfo.getNation();
				o1[16] = userBaseInfo.getNationality();
				o1[17] = userBaseInfo.getCardType();
				o1[18] = userBaseInfo.getCardID();
				o1[19] = userBaseInfo.getTel_id();
				o1[20] = userBaseInfo.getEmail();
				o1[21] = userBaseInfo.getQq();
				o1[22] = userBaseInfo.getWeibo();
				o1[23] = userBaseInfo.getResidentialArea();
				o1[24] = userBaseInfo.getResidentialTel();
				o1[25] = userBaseInfo.getWorkUnit();
				o1[26] = userBaseInfo.getProfession();
				o1[27] = userBaseInfo.getPolitical();
				o1[28] = userBaseInfo.getAvocation();
				o1[29] = userBaseInfo.getStatus();
				o1[30] = userBaseInfo.getRemarks();
				objects.add(o1);
			}
		}
		return ExcelUtil.exportData(objects);
		
	}


	
	
	
	
	public List<UserBaseInfo> selectAllUserList() {
		return userBaseInfoMapper.selectUserToGetTel();
	}

	public List<UserBaseInfo> selectUserByClassIdAndName(String userName, String classId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("classId", classId);
		return userBaseInfoMapper.selectUserByClassIdAndName(map);
	}

	public void updateUserAccountNum(UserBaseInfo UserBaseInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", UserBaseInfo.getUser_id());
		map.put("accountNum", UserBaseInfo.getAppuser_id());
		userBaseInfoMapper.updateUserAccountNum(map);
	}

	public UserBaseInfo selectAllProByUserId(String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		return userBaseInfoMapper.selectAllProByUserId(map);
	}

	public List<UserBaseInfo> selectUserByClassId(String classId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("classId", classId);
		return userBaseInfoMapper.selectAllUserByClassId(map);
	}

	public UserBaseInfo selectUserBaseInfoByGmidAndName(Map<String, Object> map) {
		return userBaseInfoMapper.selectUserInfo(map);
	}

	/*
	public int updateUserTelId(Map<String, Object> map, UserBaseInfo UserBaseInfo) {
		if (UserBaseInfo == null) {
			return -8;
		}

		String submitTelId = map.get("telId").toString();
		String telId = UserBaseInfo.getTelId();
		// System.out.println("-------------------------------------------------------");
		// System.out.println("基础数据表中的电话号码是"+telId);
		// System.out.println("提交的电话号码是"+submitTelId);
		// System.out.println("-------------------------------------------------------");
		if (telId == null || "".equals(telId)) {
			return UserBaseInfoMapper.updateTelId(map);
		} else if (!submitTelId.equals(telId)) {
			if (UserBaseInfo.getUseTime() == null) {
				if (map.get("useTime") != null) {
					return UserBaseInfoMapper.updateTelId(map);
				} else {
					// 返回1代表不做更新处理
					return -8;
				}
			} else if (UserBaseInfo.getUseTime().before((Date) map.get("useTime"))) {
				return UserBaseInfoMapper.updateTelId(map);
			}
		}
		return -8;
	}
	*/

	public boolean selectUserInClass(String classId, List<String> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("classId", classId);
		long count = userBaseInfoMapper.selectUserInClass(map);
		if (count == list.size()) {
			return true;
		}
		return false;
	}

	public List<UserBaseInfo> selectByUserName(String userName, String deptId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		return userBaseInfoMapper.selectByUserName(map);
	}

	public List<UserBaseInfo> selectCard(List<String> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		return userBaseInfoMapper.selectCard(map);
	}
	

	public DataGrid<UserBaseInfo> dataGridFor(Map<String, Object> map) {
		DataGrid<UserBaseInfo> dataGrid = new DataGrid<UserBaseInfo>();
		long total = userBaseInfoMapper.countFor(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<UserBaseInfo> list = userBaseInfoMapper.selectListFor(map);
		dataGrid.setRows(list);
		return dataGrid;
	}
	public DataGrid<UserBaseInfo> selectByDeptIdForImport(Map<String, Object> map) {
		DataGrid<UserBaseInfo> dataGrid = new DataGrid<UserBaseInfo>();
		long total = userBaseInfoMapper.countByDeptIdForImport(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<UserBaseInfo> list = userBaseInfoMapper.selectByDeptIdForImport(map);
		dataGrid.setRows(list);
		return dataGrid;
	}
	

	
	/**
	 * 导入学生数据
	 */
	public String importData(String url, User user) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			// 文件保存目录路径
			String savePath = Global.DISK_PATH;

			// 文件保存目录URL
			String saveUrl = Global.URL_DOMAIN;
			url = savePath + url.replace(saveUrl, "");
			File file = new File(url);
			List<Object[]> list = ExcelUtil.parseExcel(file);
			List<Object[]> errorList = new ArrayList<Object[]>();
			
			
			//班级信息，key为班级fullName，value为班级class_id --> 用于快速验证班级和获取class_id
			Map<String, Long> classMap = new HashMap<String, Long>();
			//每个班级下的所有学生，key为class_id，value为学生集合 --> 用于比对信息防止学生信息重复
			Map<Long, List<UserBaseInfo>> classStudentMap = new ConcurrentHashMap<Long, List<UserBaseInfo>>();
			
			
			List<ClassInfo> allClass = classInfoMapper.getAllClass();
			for(ClassInfo c : allClass) {
				classMap.put(c.getFullName(), c.getClass_id());
			}

			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Object[] line = list.get(i);
					
					// 第一行为excel表头
					if (i == 0) {
						addLineError(line, "失败原因", errorList);
						continue;
					}
					
					String studentNumber = ((String) line[0]).trim();
					String userName = ((String) line[1]).trim();
					String aliasName = ((String) line[2]).trim();
					String sex = ((String) line[3]).trim();
					String schoolName = ((String) line[4]).trim();
					String collegeName = ((String) line[5]).trim();
					String majorName = ((String) line[6]).trim();
					String gradeName = ((String) line[7]).trim();
					String className = ((String) line[8]).trim();
					String resourceArea = ((String) line[9]).trim();
					String entranceTimeStr = ((String) line[10]).trim();
					String graduationTimeStr = ((String) line[11]).trim();
					String studentType = ((String) line[12]).trim();
					String programLength = ((String) line[13]).trim();
					String birthdayStr = ((String) line[14]).trim();
					String nation = ((String) line[15]).trim();
					String nationality = ((String) line[16]).trim();
					String cardType = ((String) line[17]).trim();
					String cardID = ((String) line[18]).trim();
					String telId = ((String) line[19]).trim();
					String email = ((String) line[20]).trim();
					String qq = ((String) line[21]).trim();
					String weibo = ((String) line[22]).trim();
					String residentialArea = ((String) line[23]).trim();
					String residentialTel = ((String) line[24]).trim();
					String workUnit = ((String) line[25]).trim();
					String profession = ((String) line[26]).trim();
					String political = ((String) line[27]).trim();
					String avocation = ((String) line[28]).trim();
					String status = ((String) line[29]).trim();
					String remarks = ((String) line[30]).trim();
					
					
					
					
					
					
					Date birthday = new Date();
					Date entranceTime = new Date();
					Date graduationTime = new Date();

					//数据简单验证
					if (userName == null || "".equals(userName.trim())) {
						addLineError(line, "姓名为空", errorList);
						continue;
					}
					if (schoolName == null || "".equals(schoolName.trim())) {
						addLineError(line, "学校为空", errorList);
						continue;
					}
					if (collegeName == null || "".equals(collegeName.trim())) {
						addLineError(line, "院系为空", errorList);
						continue;
					}
					if (majorName == null || "".equals(majorName.trim())) {
						addLineError(line, "专业为空", errorList);
						continue;
					}
					if (gradeName == null || "".equals(gradeName.trim())) {
						addLineError(line, "年级为空", errorList);
						continue;
					}
					if (className == null || "".equals(className.trim())) {
						addLineError(line, "班级为空", errorList);
						continue;
					}
	
					try {
						if (birthdayStr != null && !"".equals(birthdayStr)) {
							birthday = dateFormat.parse(birthdayStr);
						}
					} catch (Exception e) {
						addLineError(line, "生日日期数据无法转换", errorList);
						continue;
					}
					try {
						if (entranceTimeStr != null && !"".equals(entranceTimeStr)) {
							entranceTime = dateFormat.parse(entranceTimeStr);
						}
					} catch (Exception e) {
						addLineError(line, "入学时间数据无法转换", errorList);
						continue;
					}
					try {
						if (graduationTimeStr != null && !"".equals(graduationTimeStr)) {
							graduationTime = dateFormat.parse(graduationTimeStr);
						}
					} catch (Exception e) {
						addLineError(line, "毕业时间数据无法转换", errorList);
						continue;
					}
					
					
					UserBaseInfo userBaseInfo = new UserBaseInfo();
					userBaseInfo.setUser_name(userName);
					userBaseInfo.setAliasname(aliasName);
					userBaseInfo.setName_pinyin(PinYinUtils.getQuanPin(userName));
					userBaseInfo.setStudentNumber(studentNumber);
					userBaseInfo.setSex(sex);
					userBaseInfo.setResourceArea(resourceArea);
					userBaseInfo.setNation(nation);
					userBaseInfo.setNationality(nationality);
					userBaseInfo.setStudentType(studentType);
					userBaseInfo.setProgramLength(programLength);
					userBaseInfo.setCardType(cardType);
					userBaseInfo.setCardID(cardID);
					userBaseInfo.setTel_id(telId);
					userBaseInfo.setEmail(email);
					userBaseInfo.setQq(qq);
					userBaseInfo.setWeibo(weibo);
					userBaseInfo.setResidentialArea(residentialArea);
					userBaseInfo.setResidentialTel(residentialTel);
					userBaseInfo.setBirthday(birthday);
					userBaseInfo.setEntranceTime(entranceTime);
					userBaseInfo.setGraduationTime(graduationTime);
					userBaseInfo.setWorkUnit(workUnit);
					userBaseInfo.setProfession(profession);
					userBaseInfo.setPolitical(political);
					userBaseInfo.setAvocation(avocation);
					userBaseInfo.setStatus(status);
					userBaseInfo.setRemarks(remarks);
					
					//获取班级信息
					gradeName = gradeName +"级";
					String fullName = schoolName+","+collegeName+","+majorName+","+gradeName+","+className;
					Long class_id = classMap.get(fullName);
					if(class_id == null) {
						//该班级不存在，创建这个班级
						ClassInfo classInfo = new ClassInfo();
						classInfo.setSchoolName(schoolName);
			        	classInfo.setCollege(collegeName);
			        	classInfo.setMajor(majorName);
			        	classInfo.setGrade(gradeName);
			        	classInfo.setClassName(className);
			        	classInfo.setFullName(fullName);
			        		//获取同专业的院系挂接情况，有挂接的话要更新到新的班级
		        		Map<String, Object> map = new HashMap<String, Object>();
		                map.put("start", 0);
		                map.put("rows", 10);
		                map.put("schoolName", schoolName);
		                map.put("college", collegeName);
		                map.put("major", majorName);
		                List<ClassInfo> cList = classInfoMapper.query(map);
		                if(cList.size()>0) {
		                	ClassInfo sameMajor = cList.get(0);
		                	classInfo.setAffiliation(sameMajor.getAffiliation());
		                }
		                
		                //创建环信群组
		        		ChatGroup chatGroup = new ChatGroup();
		        	    chatGroup.setGroupname(classInfo.getClassName());
		        	    chatGroup.setDesc(classInfo.getFullName());
		        	    chatGroup.setMaxusers(200);
		        	    chatGroup.setOwner("system");
		        	    chatGroup.setPublic(false);
		        	    chatGroup.setApproval(true);
		        	    String groupId = GroupRegister.createChatGroup(chatGroup);
		        	    if(!groupId.equals("error")) {
		        	    	classInfo.setEasemobGroup(groupId);
		        	    }
		        	    
		                classInfoMapper.insert(classInfo);
		                
		                //创建成功更新相关变量
		                class_id = classInfo.getClass_id();
		                
		                userBaseInfo.setClass_id(class_id);
		                
		                classMap.put(fullName, class_id);
		                
		                List<UserBaseInfo> studentList = new ArrayList<UserBaseInfo>();
		                studentList.add(userBaseInfo);
		                classStudentMap.put(class_id, studentList);
					} else {
						//该班级已存在，如果学生为空则加载学生用于数据重复检查
						List<UserBaseInfo> studentList = classStudentMap.get(class_id);
						if(studentList == null || studentList.size() == 0) {
							studentList = userBaseInfoMapper.getByClass(class_id);
						}
						
						//数据重复检查
						if(studentList != null && studentList.size() > 0) {
							boolean hasBaseIn = false;
							for (UserBaseInfo userInfo : studentList) {
								if (studentNumber.equals(userInfo.getStudentNumber()) 
									&& userName.equals(userInfo.getUser_name())
									&& sex.equals(userInfo.getSex()) 
									&& cardType.equals(userInfo.getCardType()) 
									&& cardID.equals(userInfo.getCardID()) 
									&& programLength.equals(userInfo.getProgramLength())	
									&& studentType.equals(userInfo.getStudentType())) {
									hasBaseIn = true;
									break;
								}
							}
							
							if (hasBaseIn) {
								addLineError(line, "该数据在基础库中已经存在", errorList);
								continue;
							}
						}
						
						//检查通过更新相关变量
		                userBaseInfo.setClass_id(class_id);
		                studentList.add(userBaseInfo);
		                classStudentMap.put(class_id, studentList);
					}
					
					//添加学生数据
					userBaseInfoMapper.save(userBaseInfo);
				}
			}
			System.gc();
			return ExcelUtil.exportData(errorList);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private void addLineError(Object[] line, String error, List<Object[]> errorList) {
		Object[] newLine = new Object[line.length + 1];
		System.arraycopy(line, 0, newLine, 0, line.length);
		newLine[line.length] = error;
		errorList.add(newLine);
	}
		
		
	@Override
	public void modifyClass(long userId, long classId) {
		// TODO Auto-generated method stub
		modifyRelationship(userId, classId, 1);
	}
	/**
	 * 更新群组关系表
	 * @param userId
	 * @param groupId
	 * @param type 更新关系数据类型 0:校友会,group_id=department.department_id; 1：班级,group_id=class_info.class_id
	 */
	public void modifyRelationship(long userId, long groupId,int type) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("groupId", groupId);
		map.put("type", type);
		userBaseInfoMapper.updateRelationship(map);
	}

	@Override
	public List<String> selectEmailbyId(List<String> ids) {
		// TODO Auto-generated method stub
		return userBaseInfoMapper.selectEmailbyId(ids);
	}

	@Override
	public List<String> selectTelbyId(List<String> list) {
		// TODO Auto-generated method stub
		return userBaseInfoMapper.selectTelbyId(list);
	}






}
