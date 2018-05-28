package com.hxy.core.event.entity;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable
{

	private static final long serialVersionUID = 8995887982157489814L;
	
	private long id;
	private String title;  		//活动标题
	private long groupId;		//对应的主题(group_topic)编号
	private int type;			//活动性质： 0=官方， 5=校友会， 9=个人
	private String category;	//活动类别（聚餐、旅游、讲座...）
	private String place;		//活动地点
	private String content;		//活动介绍
	private String pic;     	//活动图片(保存在数据库里的相对地址)
	private String picUrl;     	//活动图片包含完整域名的绝对地址
	private String organizer;  	//主办方（官方活动）
	private Date startTime;		//开始时间
	private Date endTime;		//结束时间
	private Date signupStartTime;	//报名开始时间
	private Date signupEndTime;		//报名截止时间
	private int minPeople;		//人数下限（最少报名人数）
	private int maxPeople;		//人数上限（最多报名人数， 0=无上限）
	private boolean needSignIn;	//是否需要签到（0=不需要， 1=需要）
	private String signInCode;	//签到码
	private Date createTime;	//创建时间
	private long created_by;	//后台创建官方、校友会活动的管理员
	private long userInfoId;	//前台创建个人活动的用户
	
	private boolean needNotification;	//是否有通知（0=没有， 1=有）
	private String notification;		//通知文本
	
	private int auditStatus;	//审核状态（0=未审核，1=通过，2=不通过）
	private String auditOpinion;//审核意见
	private long auditUserId;	//审核人
	private Date auditTime;		//审核时间
	
	private int status;			//活动状态（0=正常， 1=取消， 2=删除）
	private String region;		//地域
	
	private long board_id;			//所属社群板块(groupId --> group_topic.board_id)
	private long department_id;		//所属机构（官方活动的院系，校友会活动的校友会） (group_topic.board_id --> group_board.oid)
	private String departmentName;	//所属机构名称
	
	private String userName;		//  --> created_by
	private String appUserName;		//  --> userInfoId
	
	private int signupNum;		//活动的报名人数
	private String nowStatus;   //根据审核、状态、当前时间等各种因素所得的当前活动状态
	private String pushtype;//用于区分活动和新闻的推送
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getOrganizer() {
		return organizer;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getSignupStartTime() {
		return signupStartTime;
	}
	public void setSignupStartTime(Date signupStartTime) {
		this.signupStartTime = signupStartTime;
	}
	public Date getSignupEndTime() {
		return signupEndTime;
	}
	public void setSignupEndTime(Date signupEndTime) {
		this.signupEndTime = signupEndTime;
	}
	public int getMinPeople() {
		return minPeople;
	}
	public void setMinPeople(int minPeople) {
		this.minPeople = minPeople;
	}
	public int getMaxPeople() {
		return maxPeople;
	}
	public void setMaxPeople(int maxPeople) {
		this.maxPeople = maxPeople;
	}
	public boolean isNeedSignIn() {
		return needSignIn;
	}
	public void setNeedSignIn(boolean needSignIn) {
		this.needSignIn = needSignIn;
	}
	public String getSignInCode() {
		return signInCode;
	}
	public void setSignInCode(String signInCode) {
		this.signInCode = signInCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public long getCreated_by() {
		return created_by;
	}
	public void setCreated_by(long created_by) {
		this.created_by = created_by;
	}
	public long getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(long userInfoId) {
		this.userInfoId = userInfoId;
	}
	public boolean isNeedNotification() {
		return needNotification;
	}
	public void setNeedNotification(boolean needNotification) {
		this.needNotification = needNotification;
	}
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	public int getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditOpinion() {
		return auditOpinion;
	}
	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}
	public long getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(long auditUserId) {
		this.auditUserId = auditUserId;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAppUserName() {
		return appUserName;
	}
	public void setAppUserName(String appUserName) {
		this.appUserName = appUserName;
	}
	public int getSignupNum() {
		return signupNum;
	}
	public void setSignupNum(int signupNum) {
		this.signupNum = signupNum;
	}
	public String getNowStatus() {
		return nowStatus;
	}
	public void setNowStatus(String nowStatus) {
		this.nowStatus = nowStatus;
	}
	public long getBoard_id() {
		return board_id;
	}
	public void setBoard_id(long board_id) {
		this.board_id = board_id;
	}
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
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getPushtype() {
		return pushtype;
	}
	public void setPushtype(String pushtype) {
		this.pushtype = pushtype;
	}

}