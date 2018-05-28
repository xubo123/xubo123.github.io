package com.hxy.util.jms;

import java.io.Serializable;
import java.util.Date;

public class SingleNewsMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private int nid;// 新闻id
	private boolean isBreaking;// 是否放在头条
	private String title;// 新闻标题
	private String summary;// 新闻摘要
	private Date time;
	private String newsUrl;// 新闻网址
	private String icon;
	private String tagName;// 所属频道
	private long tagId;// 所属频道id
	private String type;// 用于推送时，区分新闻和活动

	public long getChannelId() {
		return tagId;
	}

	public void setChannelId(long tagId) {
		this.tagId = tagId;
	}

	private String PMId;// 所属pushedmessage的id

	public String getPMId() {
		return PMId;
	}

	public void setPMId(String pMId) {
		PMId = pMId;
	}

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public boolean isBreaking() {
		return isBreaking;
	}

	public void setBreaking(boolean isBreaking) {
		this.isBreaking = isBreaking;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
