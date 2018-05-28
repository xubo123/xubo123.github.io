package com.hxy.core.region.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.core.region.service.ProvinceService;

@Namespace("/province")
@Action(value = "provinceAction")
public class ProvinceAction extends AdminBaseAction {

	private int countryId;

	@Autowired
	private ProvinceService provinceService;

	public void doNotNeedSecurity_getProvince2ComboBox() {
		super.writeJson(provinceService.selectByCountryId(countryId));
	}
	
	public void doNotNeedSessionAndSecurity_getProvince2ComboBox() {
		super.writeJson(provinceService.selectByCountryId(countryId));
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

}
