package com.hxy.core.channel.entity;

import java.io.Serializable;

public class NewsTag implements Serializable
{
	private static final long serialVersionUID = 1L;

	private int tagId;
	private String tagName;// 频道名称
	private String tagRemark;// 信道介绍
	private String tagIcon;
	private String tagIconUrl;
	private long department_id;
	private String tag;

	private String departmentName;

	public String getTagRemark()
	{
		return tagRemark;
	}

	public void setTagRemark(String tagRemark)
	{
		this.tagRemark = tagRemark;
	}

	public int getTagId()
	{
		return tagId;
	}

	public void setTagId(int tagId)
	{
		this.tagId = tagId;
	}

	public String getTagName()
	{
		return tagName;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public String getTagIcon()
	{
		return tagIcon;
	}

	public void setTagIcon(String tagIcon)
	{
		this.tagIcon = tagIcon;
	}

	public String getTagIconUrl() {
		return tagIconUrl;
	}

	public void setTagIconUrl(String tagIconUrl) {
		this.tagIconUrl = tagIconUrl;
	}

	public long getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(long department_id) {
		this.department_id = department_id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

}
