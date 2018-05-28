package com.hxy.core.region.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.core.region.service.AreaService;

@Namespace("/area")
@Action(value = "areaAction")
public class AreaAction extends AdminBaseAction {

	private int cityId;

	@Autowired
	private AreaService areaService;

	public void doNotNeedSecurity_getArea2ComboBox() {
		super.writeJson(areaService.selectByCityId(cityId));
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

}
