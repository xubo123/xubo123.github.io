package com.hxy.core.community.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 投诉
 */
public class Complaint implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;  //自增长id
	private long topic_context_id;
	private String reason;
	public long created_by;
	public Date created_date;
	
	private String appUserName;		//  --> created_by

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTopic_context_id() {
		return topic_context_id;
	}

	public void setTopic_context_id(long topic_context_id) {
		this.topic_context_id = topic_context_id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getCreated_by() {
		return created_by;
	}

	public void setCreated_by(long created_by) {
		this.created_by = created_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public String getAppUserName() {
		return appUserName;
	}

	public void setAppUserName(String appUserName) {
		this.appUserName = appUserName;
	}
	
	
	
	
}
