package com.hxy.core.donation.entity;

import java.io.Serializable;
import java.util.Date;

import com.hxy.core.project.entity.Project;
import com.hxy.core.user.entity.User;
import com.hxy.core.userinfo.entity.UserInfo;

public class Donation implements Serializable {
	private static final long serialVersionUID = 1L;
	private long donationId;
	private String userId;
	private String orderNo;
	private long projectId;
	private double money;
	private Date donationTime;
	private double payMoney;
	private Date payTime;
	private int payStatus;
	private int confirmStatus;
	private Date confirmTime;
	private long confirmId;
	private String remark;
	private String message;
	private String payMode;
	private String payDetail;
	private int flag;

	private String x_name;
	private String x_phone;
	private String x_email;
	private String x_address;
	private String x_school;
	private String x_depart;
	private String x_grade;
	private String x_clazz;
	private String x_sex;
	private String x_workunit;
	private String x_position;
	private String x_major;

	private String v_ymd;
	private String v_md5info;

	private long majorId;

	private int totalMoney;
	private int totalPeople;
	private int totalDonationMoney;

	private String alipayNumber;
	private Long accountNum;
	private String payMethod;

	private User user;
	private UserInfo userInfo;
	private Project project;

	private short anonymous;

	public long getDonationId() {
		return donationId;
	}

	public void setDonationId(long donationId) {
		this.donationId = donationId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Date getDonationTime() {
		return donationTime;
	}

	public void setDonationTime(Date donationTime) {
		this.donationTime = donationTime;
	}

	public double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public int getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(int confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public long getConfirmId() {
		return confirmId;
	}

	public void setConfirmId(long confirmId) {
		this.confirmId = confirmId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public int getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}

	public int getTotalPeople() {
		return totalPeople;
	}

	public void setTotalPeople(int totalPeople) {
		this.totalPeople = totalPeople;
	}

	public long getMajorId() {
		return majorId;
	}

	public void setMajorId(long majorId) {
		this.majorId = majorId;
	}

	public String getX_name() {
		return x_name;
	}

	public void setX_name(String x_name) {
		this.x_name = x_name;
	}

	public String getX_phone() {
		return x_phone;
	}

	public void setX_phone(String x_phone) {
		this.x_phone = x_phone;
	}

	public String getX_email() {
		return x_email;
	}

	public void setX_email(String x_email) {
		this.x_email = x_email;
	}

	public String getX_address() {
		return x_address;
	}

	public void setX_address(String x_address) {
		this.x_address = x_address;
	}

	public String getX_school() {
		return x_school;
	}

	public void setX_school(String x_school) {
		this.x_school = x_school;
	}

	public String getX_depart() {
		return x_depart;
	}

	public void setX_depart(String x_depart) {
		this.x_depart = x_depart;
	}

	public String getX_grade() {
		return x_grade;
	}

	public void setX_grade(String x_grade) {
		this.x_grade = x_grade;
	}

	public String getX_clazz() {
		return x_clazz;
	}

	public void setX_clazz(String x_clazz) {
		this.x_clazz = x_clazz;
	}

	public String getX_sex() {
		return x_sex;
	}

	public void setX_sex(String x_sex) {
		this.x_sex = x_sex;
	}

	public String getX_workunit() {
		return x_workunit;
	}

	public void setX_workunit(String x_workunit) {
		this.x_workunit = x_workunit;
	}

	public String getX_position() {
		return x_position;
	}

	public void setX_position(String x_position) {
		this.x_position = x_position;
	}

	public String getX_major() {
		return x_major;
	}

	public void setX_major(String x_major) {
		this.x_major = x_major;
	}

	public String getV_ymd() {
		return v_ymd;
	}

	public void setV_ymd(String v_ymd) {
		this.v_ymd = v_ymd;
	}

	public String getV_md5info() {
		return v_md5info;
	}

	public void setV_md5info(String v_md5info) {
		this.v_md5info = v_md5info;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getPayDetail() {
		return payDetail;
	}

	public void setPayDetail(String payDetail) {
		this.payDetail = payDetail;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getTotalDonationMoney() {
		return totalDonationMoney;
	}

	public void setTotalDonationMoney(int totalDonationMoney) {
		this.totalDonationMoney = totalDonationMoney;
	}

	public String getAlipayNumber() {
		return alipayNumber;
	}

	public void setAlipayNumber(String alipayNumber) {
		this.alipayNumber = alipayNumber;
	}

	public Long getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(Long accountNum) {
		this.accountNum = accountNum;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public short getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(short anonymous) {
		this.anonymous = anonymous;
	}

}
