package com.hxy.core.classInfo.entity;

import java.io.Serializable;

/**
 * 某级别的机构信息<br>
 * 用于实现学校-院系-专业-年级-班级的多级联动
 */
public class Linker implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 本级别机构的单独名称<br>
	 * 例如专业级别就显示该专业名称
	 */
	private String singleName;
	
	/**
	 * 包含各级上级机构的名称，用于查找下一级别的机构<br>
	 * 例如专业级别就显示“学校,院系,专业”名称<br>
	 * <b>对于最末端“班级”则为class_id</b>
	 */
	private String fullName;
	

	public String getSingleName() {
		return singleName;
	}

	public void setSingleName(String singleName) {
		this.singleName = singleName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
}
