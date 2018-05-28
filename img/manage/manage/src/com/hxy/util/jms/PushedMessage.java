package com.hxy.util.jms;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PushedMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private String PMId;// 推送消息id
	private String icon;
	private String tagName;// 频道名称
	private String newsSummary;// 头条新闻的摘要
	private Date time;
	private List<SingleNewsMessage> newsList;// 存放新闻的list
	private long tagId;

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getNewsSummary() {
		return newsSummary;
	}

	public void setNewsSummary(String newsSummary) {
		this.newsSummary = newsSummary;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public List<SingleNewsMessage> getNewsList() {
		return newsList;
	}

	public void setNewsList(List<SingleNewsMessage> newsList) {
		this.newsList = newsList;
	}

	public String getPMId() {
		return PMId;
	}

	public void setPMId(String pMId) {
		PMId = pMId;
	}

}
