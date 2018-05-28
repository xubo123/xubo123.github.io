package com.hxy.core.userinfo.action;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.DataGrid;
import com.hxy.base.entity.Message;
import com.hxy.core.userinfo.entity.UserInfo;
import com.hxy.core.userinfo.service.UserInfoService;

@Namespace("/userInfo")
@Action(value = "userInfoAction")
public class UserInfoAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserInfoAction.class);

	@Autowired
	private UserInfoService userInfoService;

	private UserInfo userInfo;

	private String deptId;

	private String schoolId;
	private String departId;
	private String gradeId;
	private String classId;

	private String region;

	private String url;

	private int isInput;

	private String country;
	private String province;
	private String city;
	private String area;

	private String birthday;
	private String entranceTime;
	private String regflag;

	public void dataGrid() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		if (sort.equals("userName")) {
			sort = "name_pinyin";
		}
		map.put("sort", sort);
		map.put("order", order);
		map.put("deptList", getUser().getDepts());
		if (classId != null && !classId.equals("")) {
			map.put("deptId1", classId);
		} else if (gradeId != null && !gradeId.equals("")) {
			map.put("deptId1", gradeId);
		} else if (departId != null && !departId.equals("")) {
			map.put("deptId1", departId);
		} else if (schoolId != null && !schoolId.equals("")) {
			map.put("deptId1", schoolId);
		}
		map.put("regflag", regflag);

		if (userInfo != null) {
			map.put("userName", userInfo.getUserName());
			map.put("studentType", userInfo.getStudentType());
			map.put("studentnumber", userInfo.getStudentnumber());
			// map.put("residentialArea", userInfo.getResidentialArea());
			map.put("workUnit", userInfo.getWorkUnit());
			map.put("mailingAddress", userInfo.getMailingAddress());
			map.put("majorId", userInfo.getMajorId());
			map.put("industryType", userInfo.getIndustryType());
		}

		String residentialArea = "";
		// if (country != null && country.length() > 0) {
		// residentialArea += country;
		// }
		if (province != null && province.length() > 0) {
			residentialArea += province;
		}
		if (city != null && city.length() > 0) {
			residentialArea += " " + city;
		}
		if (area != null && area.length() > 0) {
			residentialArea += " " + area;
		}
		if (residentialArea.length() > 0) {
			map.put("residentialArea", residentialArea);
		}
		super.writeJson(userInfoService.selectByDeptId(map));
	}

	/**
	 * 后台校友总汇
	 */
	public void dataGridForAlumni() {
		alumni();
	}

	private void alumni() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);

		if (classId != null && !classId.equals("")) {
			map.put("deptId1", classId);
		} else if (gradeId != null && !gradeId.equals("")) {
			map.put("deptId1", gradeId);
		} else if (departId != null && !departId.equals("")) {
			map.put("deptId1", departId);
		} else if (schoolId != null && !schoolId.equals("")) {
			map.put("deptId1", schoolId);
		}
		
		map.put("deptList", getUser().getDepts());

		if (userInfo != null) {
			map.put("userName", userInfo.getUserName());
			map.put("studentType", userInfo.getStudentType());
			map.put("majorId", userInfo.getMajorId());
			map.put("birthday", userInfo.getBirthday());
			map.put("sex", userInfo.getSex());
			map.put("industryType", userInfo.getIndustryType());
			map.put("entranceTime", userInfo.getEntranceTime());
			map.put("alumniId", userInfo.getAlumniId());
		}

		if (entranceTime != null && entranceTime.length() > 0) {
			map.put("entranceTime", entranceTime);
		}

		if (birthday != null && birthday.length() > 0) {
			map.put("birthday", birthday);
		}

		//TO-FIX
		/*if (getUser().getFlag() == 1) {
			map.put("alumniId", getUser().getDeptId());
		}*/

		super.writeJson(userInfoService.selectByDeptIdForAlumni(map));
	}

	/**
	 * 校友会列表
	 */
	public void dataGridForAlumnix() {
		alumni();
	}

	public void delete() {
		Message message = new Message();
		try {
			boolean canDelete = userInfoService.delete(ids);
			if (canDelete) {
				message.setMsg("删除成功");
				message.setSuccess(true);
			} else {
				message.setMsg("删除失败,删除的记录中存在正式校友");
				message.setSuccess(false);
			}

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void deleteAll() {
		Message message = new Message();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptList", getUser().getDepts());
			if (classId != null && !classId.equals("")) {
				map.put("deptId1", classId);
			} else if (gradeId != null && !gradeId.equals("")) {
				map.put("deptId1", gradeId);
			} else if (departId != null && !departId.equals("")) {
				map.put("deptId1", departId);
			} else if (schoolId != null && !schoolId.equals("")) {
				map.put("deptId1", schoolId);
			}

			if (userInfo != null) {
				map.put("userName", userInfo.getUserName());
				map.put("studentType", userInfo.getStudentType());
				map.put("studentnumber", userInfo.getStudentnumber());
				// map.put("residentialArea", userInfo.getResidentialArea());
				map.put("workUnit", userInfo.getWorkUnit());
				map.put("mailingAddress", userInfo.getMailingAddress());
				map.put("majorId", userInfo.getMajorId());
				map.put("industryType", userInfo.getIndustryType());
			}

			String residentialArea = "";
			// if (country != null && country.length() > 0) {
			// residentialArea += country;
			// }
			if (province != null && province.length() > 0) {
				residentialArea += province;
			}
			if (city != null && city.length() > 0) {
				residentialArea += " " + city;
			}
			if (area != null && area.length() > 0) {
				residentialArea += " " + area;
			}
			if (residentialArea.length() > 0) {
				map.put("residentialArea", residentialArea);
			}
			boolean canDelete = userInfoService.deleteAll(map);
			if (canDelete) {
				message.setMsg("删除成功");
				message.setSuccess(true);
			} else {
				message.setMsg("删除失败,删除的记录中存在正式校友");
				message.setSuccess(false);
			}

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void save() {
		Message message = new Message();
		try {
			userInfoService.save(userInfo, getUser(), isInput);
			message.setMsg("新增成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("新增失败" + e.getMessage());
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void saveFromBase() {
		Message message = new Message();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptList", getUser().getDepts());
			if (classId != null && !classId.equals("")) {
				map.put("deptId1", classId);
			} else if (gradeId != null && !gradeId.equals("")) {
				map.put("deptId1", gradeId);
			} else if (departId != null && !departId.equals("")) {
				map.put("deptId1", departId);
			} else if (schoolId != null && !schoolId.equals("")) {
				map.put("deptId1", schoolId);
			}
			if (userInfo != null) {
				map.put("telId", userInfo.getTelId());
				map.put("userName", userInfo.getUserName());
				map.put("studentType", userInfo.getStudentType());
				map.put("studentnumber", userInfo.getStudentnumber());
				map.put("residentialArea", userInfo.getResidentialArea());
				map.put("workUnit", userInfo.getWorkUnit());
				map.put("mailingAddress", userInfo.getMailingAddress());
				map.put("majorId", userInfo.getMajorId());
			}
			userInfoService.saveFromBase(map);
			message.setMsg("导入成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("导入失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void update() {
		Message message = new Message();
		try {
			userInfoService.update(userInfo, getUser(), isInput);
			message.setMsg("修改成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("修改失败" + e.getMessage().replace("java.lang.RuntimeException: ", ""));
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void updateIdea() {
		Message message = new Message();
		try {
			userInfoService.updateIdea(userInfo);
			message.setMsg("操作成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("操作失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void doNotNeedSecurity_getUserInfoByUserId() {
		super.writeJson(userInfoService.selectByUserId(userInfo.getUserId()));
	}

	public void getUserInfoByUserId() {
		super.writeJson(userInfoService.selectByUserId(userInfo.getUserId()));
	}

	public void getUserInfoByUserIdForAlumni() {
		super.writeJson(userInfoService.getUserInfoByUserIdForAlumni(userInfo.getUserId()));
	}

	public void getUserInfoByUserIdForAlumnix() {
		super.writeJson(userInfoService.getUserInfoByUserIdForAlumni(userInfo.getUserId()));
	}

	public void doNotNeedSecurity_getAllUserList() {
		super.writeJson(userInfoService.selectAllUserList());
	}

	/** --通过ajax查询所有手机号不为空的用户，并且分页-- **/
	public void doNotNeedSecurity_getAllUserList_dataGrid() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phoneNotNull", true);
		map.put("emailNotNull", false);

		map.put("deptList", getUser().getDepts());
		
		//TO-FIX
		/*
		if(getUser().getAlumni()!=null&&getUser().getAlumni().getAlumniId()!=0){
			map.put("alumniId", getUser().getAlumni().getAlumniId());
		}
		*/
		if (classId != null && !classId.equals("")) {
			map.put("deptId1", classId);
		} else if (gradeId != null && !gradeId.equals("")) {
			map.put("deptId1", gradeId);
		} else if (departId != null && !departId.equals("")) {
			map.put("deptId1", departId);
		} else if (schoolId != null && !schoolId.equals("")) {
			map.put("deptId1", schoolId);
		}

		if (userInfo != null) {
			map.put("telId", userInfo.getTelId());
			map.put("userName", userInfo.getUserName());
			map.put("studentType", userInfo.getStudentType());
			map.put("studentnumber", userInfo.getStudentnumber());
			// map.put("residentialArea", userInfo.getResidentialArea());
			map.put("workUnit", userInfo.getWorkUnit());
			map.put("mailingAddress", userInfo.getMailingAddress());
			map.put("majorId", userInfo.getMajorId());
			map.put("industryType", userInfo.getIndustryType());
		}

		String residentialArea = "";
		// if (country != null && country.length() > 0) {
		// residentialArea += country;
		// }
		if (province != null && province.length() > 0) {
			residentialArea += province;
		}
		if (city != null && city.length() > 0) {
			residentialArea += " " + city;
		}
		if (area != null && area.length() > 0) {
			residentialArea += " " + area;
		}
		if (residentialArea.length() > 0) {
			map.put("residentialArea", residentialArea);
		}
		super.writeJson(userInfoService.selectUserToGetTelPage(map));
	}

	/** --通过ajax查询所有邮箱号不为空的用户，并且分页-- **/
	public void doNotNeedSecurity_getAllUserEmailList_dataGrid() {
		Map<String, Object> map = new HashMap<String, Object>();
		// phoneNotNull==true
		map.put("phoneNotNull", false);
		map.put("emailNotNull", true);

		map.put("deptList", getUser().getDepts());
		//TO-FIX
		/*
		if(getUser().getAlumni()!=null&&getUser().getAlumni().getAlumniId()!=0){
			map.put("alumniId", getUser().getAlumni().getAlumniId());
		}
		*/
		if (classId != null && !classId.equals("")) {
			map.put("deptId1", classId);
		} else if (gradeId != null && !gradeId.equals("")) {
			map.put("deptId1", gradeId);
		} else if (departId != null && !departId.equals("")) {
			map.put("deptId1", departId);
		} else if (schoolId != null && !schoolId.equals("")) {
			map.put("deptId1", schoolId);
		}
		
		map.put("regflag", regflag);

		if (userInfo != null) {
			map.put("telId", userInfo.getTelId());
			map.put("userName", userInfo.getUserName());
			map.put("studentType", userInfo.getStudentType());
			map.put("studentnumber", userInfo.getStudentnumber());
			// map.put("residentialArea", userInfo.getResidentialArea());
			map.put("workUnit", userInfo.getWorkUnit());
			map.put("mailingAddress", userInfo.getMailingAddress());
			map.put("majorId", userInfo.getMajorId());
			map.put("industryType", userInfo.getIndustryType());
		}

		String residentialArea = "";
		// if (country != null && country.length() > 0) {
		// residentialArea += country;
		// }
		if (province != null && province.length() > 0) {
			residentialArea += province;
		}
		if (city != null && city.length() > 0) {
			residentialArea += " " + city;
		}
		if (area != null && area.length() > 0) {
			residentialArea += " " + area;
		}
		if (residentialArea.length() > 0) {
			map.put("residentialArea", residentialArea);
		}
		super.writeJson(userInfoService.selectUserToGetTelPage(map));
	}

	public void doNotNeedSecurity_dataGridFor() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		if (userInfo != null) {
			map.put("telId", userInfo.getTelId());
			map.put("userName", userInfo.getUserName());
		}
		super.writeJson(userInfoService.dataGridFor(map));
	}

	public void doNotNeedSessionAndSecurity_isShowInfoData() {

	}

	/** --外部查询用户信息-- **/
	public void doNotNeedSessionAndSecurity_dataGridFor() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		if (userInfo != null) {
			map.put("telId", userInfo.getTelId());
			map.put("userName", userInfo.getUserName());
		}

		DataGrid<UserInfo> data = userInfoService.dataGridFor(map);
		super.writeJson(data);
	}

	public void importData() {
		Message message = new Message();
		try {
			String result = userInfoService.importData(url, getUser());
			message.setMsg(result);
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("导入失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	/** --导出校友-- **/
	public void exportData() {
		Message message = new Message();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptList", getUser().getDepts());
			if (classId != null && !classId.equals("")) {
				map.put("deptId1", classId);
			} else if (gradeId != null && !gradeId.equals("")) {
				map.put("deptId1", gradeId);
			} else if (departId != null && !departId.equals("")) {
				map.put("deptId1", departId);
			} else if (schoolId != null && !schoolId.equals("")) {
				map.put("deptId1", schoolId);
			}
			if (userInfo != null) {
				map.put("telId", userInfo.getTelId());
				map.put("userName", userInfo.getUserName());
				map.put("studentType", userInfo.getStudentType());
				map.put("studentnumber", userInfo.getStudentnumber());
				// map.put("residentialArea", userInfo.getResidentialArea());
				map.put("workUnit", userInfo.getWorkUnit());
				map.put("mailingAddress", userInfo.getMailingAddress());
				map.put("majorId", userInfo.getMajorId());
				map.put("accountNum", userInfo.getAccountNum());
			}

			String residentialArea = "";
			// if (country != null && country.length() > 0) {
			// residentialArea += country;
			// }
			if (province != null && province.length() > 0) {
				residentialArea += province;
			}
			if (city != null && city.length() > 0) {
				residentialArea += " " + city;
			}
			if (area != null && area.length() > 0) {
				residentialArea += " " + area;
			}
			if (residentialArea.length() > 0) {
				map.put("residentialArea", residentialArea);
			}
			String result = userInfoService.export(map);
			message.setMsg(result);
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("导出失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public int getIsInput() {
		return isInput;
	}

	public void setIsInput(int isInput) {
		this.isInput = isInput;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEntranceTime() {
		return entranceTime;
	}

	public void setEntranceTime(String entranceTime) {
		this.entranceTime = entranceTime;
	}

	public String getRegflag() {
		return regflag;
	}

	public void setRegflag(String regflag) {
		this.regflag = regflag;
	}

}
