package com.hxy.core.dept.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Dept implements Serializable {
	private static final long serialVersionUID = 1L;
	private String deptId;
	private String deptName;
	private String parentId;
	private Date createTime;
	private String fullName;
	private int level;
	private String aliasName;

	private String schoolId;
	private String departId;
	
	private String resourceName;
	private List<Dept> depts;
	

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getDepartId() {
		return departId;
	}

	public void setDepartId(String departId) {
		this.departId = departId;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getResourceName() {
    	return resourceName;
    }

	public void setResourceName(String resourceName) {
    	this.resourceName = resourceName;
    }

	public List<Dept> getDepts() {
    	return depts;
    }

	public void setDepts(List<Dept> depts) {
    	this.depts = depts;
    }
	
}
