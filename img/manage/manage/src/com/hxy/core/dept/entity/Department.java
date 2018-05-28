package com.hxy.core.dept.entity;

import java.io.Serializable;

/**
 * 当前院系类
 * @author Xubo
 *
 */
public class Department implements Serializable{
long department_id;
String departmentName;
String schoolName;
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
public String getSchoolName() {
	return schoolName;
}
public void setSchoolName(String schoolName) {
	this.schoolName = schoolName;
}
}
