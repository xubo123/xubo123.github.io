package com.hxy.core.event.entity;

import java.io.Serializable;

public class SignUserProfile implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	private String appUserId;  
	
	/** 用户名称 */
	private String user_name;
	
	/** 用户性别[0:femail|1:male] */
	private int user_sex;
	
	/** 所在城市 */
	private String user_city;
	
	/** 手机号 */
	private String user_mobile;
	
	/** 邮箱 */
	private String user_email;
    
    private String allClassName;  //所有的学习经历
    
    

	public String getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public int getUser_sex() {
		return user_sex;
	}

	public void setUser_sex(int user_sex) {
		this.user_sex = user_sex;
	}

	public String getUser_city() {
		return user_city;
	}

	public void setUser_city(String user_city) {
		this.user_city = user_city;
	}

	public String getUser_mobile() {
		return user_mobile;
	}

	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getAllClassName() {
		return allClassName;
	}

	public void setAllClassName(String allClassName) {
		this.allClassName = allClassName;
	}
    


}