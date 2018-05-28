package com.hxy.core.department.entity;

import java.io.Serializable;

public class School implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String school_id;
	String school_name;
	
	public String getSchool_id() {
		return school_id;
	}
	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
	
}
