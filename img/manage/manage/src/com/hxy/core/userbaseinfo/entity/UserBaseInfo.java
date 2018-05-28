package com.hxy.core.userbaseinfo.entity;

import java.util.Date;

public class UserBaseInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private long user_id;
	private String user_name;
	private String aliasname;
	private String name_pinyin;
	private String sex;
	private String nation;
	private String political;
	private String nationality;
	private Date birthday;
	private String cardType;
	private String cardID;
	private Date entranceTime;
	private Date graduationTime;
	private String studentNumber;
	private String tel_id;
	private String email;
	private String qq;
	private String weibo;
	private String residentialArea;
	private String residentialTel;
	private String pic_url;
	private String workUnit;
	private String profession;
	private String avocation;
	private Date create_time;
	private long appuser_id;
	
	private String status;
	private String resourceArea;
	private String studentType;
	private String programLength;
	private long class_id;
	private String remarks;
	
	private String schoolName;  //class_id 所对应的班级信息
	private String college;		//class_id 所对应的班级信息
	private String major;		//class_id 所对应的班级信息
	private String grade;		//class_id 所对应的班级信息
	private String className;	//class_id 所对应的班级信息
	private String fullName;	//class_id 所对应班级全称
		
	private long department_id;	//class_id 所对应的班级的隶属当前院系信息
	private String departmentName; 	//class_id 所对应的班级的隶属当前院系信息
	
	private String allUser_id;		//同一app用户下的所有user_id,以逗号隔开
	private String allClassName;	//同一app用户下的所有班级名称,以逗号隔开
	
	
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getAliasname() {
		return aliasname;
	}
	public void setAliasname(String aliasname) {
		this.aliasname = aliasname;
	}
	public String getName_pinyin() {
		return name_pinyin;
	}
	public void setName_pinyin(String name_pinyin) {
		this.name_pinyin = name_pinyin;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getPolitical() {
		return political;
	}
	public void setPolitical(String political) {
		this.political = political;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardID() {
		return cardID;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	public Date getEntranceTime() {
		return entranceTime;
	}
	public void setEntranceTime(Date entranceTime) {
		this.entranceTime = entranceTime;
	}
	public Date getGraduationTime() {
		return graduationTime;
	}
	public void setGraduationTime(Date graduationTime) {
		this.graduationTime = graduationTime;
	}
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getTel_id() {
		return tel_id;
	}
	public void setTel_id(String tel_id) {
		this.tel_id = tel_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeibo() {
		return weibo;
	}
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	public String getResidentialArea() {
		return residentialArea;
	}
	public void setResidentialArea(String residentialArea) {
		this.residentialArea = residentialArea;
	}
	public String getResidentialTel() {
		return residentialTel;
	}
	public void setResidentialTel(String residentialTel) {
		this.residentialTel = residentialTel;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getWorkUnit() {
		return workUnit;
	}
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getAvocation() {
		return avocation;
	}
	public void setAvocation(String avocation) {
		this.avocation = avocation;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public long getAppuser_id() {
		return appuser_id;
	}
	public void setAppuser_id(long appuser_id) {
		this.appuser_id = appuser_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResourceArea() {
		return resourceArea;
	}
	public void setResourceArea(String resourceArea) {
		this.resourceArea = resourceArea;
	}
	public String getStudentType() {
		return studentType;
	}
	public void setStudentType(String studentType) {
		this.studentType = studentType;
	}
	public String getProgramLength() {
		return programLength;
	}
	public void setProgramLength(String programLength) {
		this.programLength = programLength;
	}
	public long getClass_id() {
		return class_id;
	}
	public void setClass_id(long class_id) {
		this.class_id = class_id;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public long getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(long department_id) {
		this.department_id = department_id;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getAllClassName() {
		return allClassName;
	}
	public void setAllClassName(String allClassName) {
		this.allClassName = allClassName;
	}
	public String getAllUser_id() {
		return allUser_id;
	}
	public void setAllUser_id(String allUser_id) {
		this.allUser_id = allUser_id;
	}
	
	
	

	
	
}