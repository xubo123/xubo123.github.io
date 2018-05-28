package com.hxy.core.dept.entity;

import java.io.Serializable;

public class Dept_New implements Serializable{
long deptId;	

String deptName;
String parentName;
String grandparentName;
String department;//院系，年级和班级需要
public String getDepartment() {
	return department;
}
public void setDepartment(String department) {
	this.department = department;
}
public String getGrandparentName() {
	return grandparentName;
}
public void setGrandparentName(String grandparentName) {
	this.grandparentName = grandparentName;
}
public String getDeptName() {
	return deptName;
}
public void setDeptName(String deptName) {
	this.deptName = deptName;
}
public String getParentName() {
	return parentName;
}
public void setParentName(String parentName) {
	this.parentName = parentName;
}
public long getDeptId() {
	return deptId;
}
public void setDeptId(long deptId) {
	this.deptId = deptId;
}
}
