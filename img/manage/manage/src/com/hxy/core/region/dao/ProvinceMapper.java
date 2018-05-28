package com.hxy.core.region.dao;

import java.util.List;

import com.hxy.core.region.entity.Province;

public interface ProvinceMapper {
	/**
	 * 根据国家ID查询所有省份
	 * 
	 * @return
	 */
	List<Province> selectByCountryId(int countryId);
}
