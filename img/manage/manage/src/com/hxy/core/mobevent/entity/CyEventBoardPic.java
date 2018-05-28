package com.hxy.core.mobevent.entity;

import java.io.Serializable;

public class CyEventBoardPic implements Serializable{

	/**
	 * 花絮图片
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;//花絮图片编号
	
	private long boardId;//花絮编号
	
	private String pic;//花絮图片地址
	
	private String thumbnail;//花絮缩略图地址
	
	private String xemanhdep;//花絮高清图地址
	
	private String picStr;//花絮图片说明
	
	

	public String getPicStr() {
		return picStr;
	}

	public void setPicStr(String picStr) {
		this.picStr = picStr;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBoardId() {
		return boardId;
	}

	public void setBoardId(long boardId) {
		this.boardId = boardId;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}

	public String getXemanhdep() {
		return xemanhdep;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void setXemanhdep(String xemanhdep) {
		this.xemanhdep = xemanhdep;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CyEventBoardPic [id=");
		builder.append(id);
		builder.append(", boardId=");
		builder.append(boardId);
		builder.append(", pic=");
		builder.append(pic);
		builder.append(", picStr=");
		builder.append(picStr);
		builder.append("]");
		return builder.toString();
	}

	

	
	
}
