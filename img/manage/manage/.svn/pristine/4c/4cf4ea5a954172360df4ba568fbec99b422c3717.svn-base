package com.hxy.core.industry.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.core.industry.service.IndustryService;

@Namespace("/industry")
@Action(value = "industryAction")
public class IndustryAction extends AdminBaseAction {

	private String parentCode;

	@Autowired
	private IndustryService industryService;

	public void doNotNeedSecurity_getIndustry2ComboBox() {
		super.writeJson(industryService.selectByParentCode(parentCode));
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

}
