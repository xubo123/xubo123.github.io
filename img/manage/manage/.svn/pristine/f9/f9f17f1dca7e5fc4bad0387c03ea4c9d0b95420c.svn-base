package com.hxy.core.region.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.core.region.service.CityService;

@Namespace("/city")
@Action(value = "cityAction")
public class CityAction extends AdminBaseAction {

	private int provinceId;

	@Autowired
	private CityService cityService;

	public void doNotNeedSecurity_getCity2ComboBox() {
		super.writeJson(cityService.selectByProvinceId(provinceId));
	}
	
	public void doNotNeedSessionAndSecurity_getCity2ComboBox() {
		super.writeJson(cityService.selectByProvinceId(provinceId));
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

}
