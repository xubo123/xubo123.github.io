package com.hxy.core.dept.entity;

import java.io.Serializable;

public class NewDeptInfo implements Serializable{
long dept_id;
String schoolName;
String college;
String major;
String grade;
String className;
String modifyTime;
String affiliationName;
public String getAffiliationName() {
	return affiliationName;
}
public void setAffiliationName(String affiliationName) {
	this.affiliationName = affiliationName;
}
public String getModifyTime() {
	return modifyTime;
}
public void setModifyTime(String modifyTime) {
	this.modifyTime = modifyTime;
}
long affiliation;
public long getDept_id() {
	return dept_id;
}
public void setDept_id(long dept_id) {
	this.dept_id = dept_id;
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
public long getAffiliation() {
	return affiliation;
}
public void setAffiliation(long affiliation) {
	this.affiliation = affiliation;
}
}
