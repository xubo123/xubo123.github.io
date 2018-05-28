package com.hxy.core.mobevent.entity;

import java.io.Serializable;
import java.util.Date;

public class CyEventBoardPraise implements Serializable{

	/**
	 * 花絮点赞
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;//花絮图片编号
	
	private long boardId;//花絮编号
	
	private String userInfoId;  //创建花絮的用户编号
	
	private String userName;//前台用户名称
	
	private String userAvatar;//前台用户头像
	
	private Date createTime; //花絮创建时间
	
	private String createTimeStr;//格式化创建时间

	public long getId() {
		return id;
	}

	public long getBoardId() {
		return boardId;
	}

	public String getUserInfoId() {
		return userInfoId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserAvatar() {
		return userAvatar;
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

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CyEventBoardPraise [id=<");
		builder.append(id);
		builder.append(">, boardId=<");
		builder.append(boardId);
		builder.append(">, userInfoId=<");
		builder.append(userInfoId);
		builder.append(">, userName=<");
		builder.append(userName);
		builder.append(">, userAvatar=<");
		builder.append(userAvatar);
		builder.append(">, createTime=<");
		builder.append(createTime);
		builder.append(">, createTimeStr=<");
		builder.append(createTimeStr);
		builder.append(">]");
		return builder.toString();
	}

}
