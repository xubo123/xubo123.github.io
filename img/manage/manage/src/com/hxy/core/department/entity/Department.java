package com.hxy.core.department.entity;

import java.io.Serializable;
import java.util.Date;


public class Department implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long department_id;
	String schoolName;
	String departmentName;
	int type;
	Date modifyTime;
	long createAccount;
	
	
	public long getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(long department_id) {
		this.department_id = department_id;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public long getCreateAccount() {
		return createAccount;
	}
	public void setCreateAccount(long createAccount) {
		this.createAccount = createAccount;
	}
	
	
}
