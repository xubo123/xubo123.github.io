package com.hxy.core.region.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.core.region.service.CountryService;

@Namespace("/country")
@Action(value = "countryAction")
public class CountryAction extends AdminBaseAction {
	@Autowired
	private CountryService countryService;

	public void doNotNeedSecurity_getCountry2ComboBox() {
		super.writeJson(countryService.selectAll());
	}
}
