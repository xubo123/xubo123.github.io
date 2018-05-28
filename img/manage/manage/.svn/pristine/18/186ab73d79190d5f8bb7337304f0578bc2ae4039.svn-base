package com.hxy.core.mobevent.entity;

import java.io.Serializable;
import java.util.Date;

public class CyEventBoardComplaint implements Serializable{
	
	/**
	 * 花絮举报
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;//举报编号
	private long boardId;//花絮编号
	private String userInfoId;//举报人
	private String userName;//前台用户名称
	private String userAvatar;//前台用户头像
	private String reason;//举报理由
	private Date createTime;//举报时间
	private String createTimeStr;//举报时间(格式化)
	
	public long getId() {
		return id;
	}
	public long getBoardId() {
		return boardId;
	}
	public String getUserInfoId() {
		return userInfoId;
	}
	public String getReason() {
		return reason;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setBoardId(long boardId) {
		this.boardId = boardId;
	}
	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
	public String getUserName() {
		return userName;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CyEventBoardComplaint [id=<");
		builder.append(id);
		builder.append(">, boardId=<");
		builder.append(boardId);
		builder.append(">, userInfoId=<");
		builder.append(userInfoId);
		builder.append(">, userName=<");
		builder.append(userName);
		builder.append(">, userAvatar=<");
		builder.append(userAvatar);
		builder.append(">, reason=<");
		builder.append(reason);
		builder.append(">, createTime=<");
		builder.append(createTime);
		builder.append(">, createTimeStr=<");
		builder.append(createTimeStr);
		builder.append("]");
		return builder.toString();
	}
	
}
