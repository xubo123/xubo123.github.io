package com.hxy.core.sms.entity;

import java.io.Serializable;
import java.util.Date;

public class MsgSend implements Serializable {
	private static final long serialVersionUID = 1L;
	private int msgId;
	private String deptId;
	private long staffId;
	private String telphone;
	private String content;
	private int statues;
	private Date sendtime;
	private int msgType;
	private int countNumber;
	private long messagegroup;
	private Date receivetime;
	private String errorCode;

	private String userAccount;

	private long smsID;

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatues() {
		return statues;
	}

	public void setStatues(int statues) {
		this.statues = statues;
	}

	public Date getSendtime() {
		return sendtime;
	}

	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getCountNumber() {
		return countNumber;
	}

	public void setCountNumber(int countNumber) {
		this.countNumber = countNumber;
	}

	public long getMessagegroup() {
		return messagegroup;
	}

	public void setMessagegroup(long messagegroup) {
		this.messagegroup = messagegroup;
	}

	public Date getReceivetime() {
		return receivetime;
	}

	public void setReceivetime(Date receivetime) {
		this.receivetime = receivetime;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public long getSmsID() {
		return smsID;
	}

	public void setSmsID(long smsID) {
		this.smsID = smsID;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

}
