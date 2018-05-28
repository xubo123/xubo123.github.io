package com.hxy.core.dept.entity;

import java.io.Serializable;

public class School implements Serializable{
String school_id;
String schoolName;
public String getSchool_id() {
	return school_id;
}
public void setSchool_id(String school_id) {
	this.school_id = school_id;
}
public String getSchoolName() {
	return schoolName;
}
public void setSchoolName(String schooolName) {
	this.schoolName = schooolName;
}
}
