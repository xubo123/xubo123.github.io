package com.hxy.core.community.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 主题内容
 */
public class TopicContext implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;  //自增长id
	private long topic_id;//帖子ID
	private String context;//内容（楼主或回复的内容）
	private String user_ip;//用户IP地址
	
	public Date created_date;
	public Date modified_date;
	public long created_by;
	private int status;				//状态（0=正常，1=投诉处理-正常，2=投诉处理-违规，3=用户自己删除，4=管理员删除）
	
	private String appUserName;		//  --> created_by
	private int complaintNum;		//投诉数量
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(long topic_id) {
		this.topic_id = topic_id;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public Date getModified_date() {
		return modified_date;
	}
	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}
	public long getCreated_by() {
		return created_by;
	}
	public void setCreated_by(long created_by) {
		this.created_by = created_by;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getComplaintNum() {
		return complaintNum;
	}
	public void setComplaintNum(int complaintNum) {
		this.complaintNum = complaintNum;
	}
	public String getAppUserName() {
		return appUserName;
	}
	public void setAppUserName(String appUserName) {
		this.appUserName = appUserName;
	}
	
	
}
