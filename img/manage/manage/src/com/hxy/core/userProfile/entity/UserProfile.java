package com.hxy.core.userProfile.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 * picture 用户图像地址 accountNum 用户账号 password 用户密码 name 昵称 phoneNum 电话号码 sex 性别
	 * 0 代表男，1代表女 address 通讯地址 sign 签名 intrestType 兴趣类型 channels 选择监听的频道 email
	 * 邮箱 authenticated 0没有认证，1已经认证 baseInfoId 用户在基础信息数据库中的id groupName
	 * 用户所在群信息，逗号隔开
	 */
	private String picture;
	private String accountNum;
	private String password;
	private String name;
	private String phoneNum;
	private String sex;
	private String address;
	private String sign;
	private String intrestType;
	private String channels;
	private String email;
	private String authenticated;
	private String baseInfoId;
	private String newBaseInfoId;

	/** --新增班级字段-- **/
	private String classes;
	/** --新增工作单位字段-- **/
	private String workUtil;
	/** --新增行业字段-- **/
	private String profession;
	/** --兴趣爱好-- **/
	private String hobby;
	/** --职务-- **/
	private String position;
	
	/**--经度--**/
	private double mu_longitud;
	/**--纬度--**/
	private double mu_latitude;
	/**--定位时间--**/
	private Timestamp gps_time;
	/**--地方校友会字段id--**/
	private long alumni_id;
	

	private String groupName;

	public String getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(String authenticated) {
		this.authenticated = authenticated;
	}

	public String getBaseInfoId() {
		return baseInfoId;
	}

	public void setBaseInfoId(String baseInfoId) {
		this.baseInfoId = baseInfoId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIntrestType() {
		return intrestType;
	}

	public void setIntrestType(String intrestType) {
		this.intrestType = intrestType;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getWorkUtil() {
		return workUtil;
	}
	public void setWorkUtil(String workUtil) {
		this.workUtil = workUtil;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getNewBaseInfoId() {
		return newBaseInfoId;
	}
	public void setNewBaseInfoId(String newBaseInfoId) {
		this.newBaseInfoId = newBaseInfoId;
	}
	public double getMu_longitud() {
		return mu_longitud;
	}
	public void setMu_longitud(double mu_longitud) {
		this.mu_longitud = mu_longitud;
	}
	public double getMu_latitude() {
		return mu_latitude;
	}
	public void setMu_latitude(double mu_latitude) {
		this.mu_latitude = mu_latitude;
	}
	public Timestamp getGps_time() {
		return gps_time;
	}
	public void setGps_time(Timestamp gps_time) {
		this.gps_time = gps_time;
	}
	public long getAlumni_id() {
		return alumni_id;
	}
	public void setAlumni_id(long alumni_id) {
		this.alumni_id = alumni_id;
	}
	
}
