package com.hxy.core.region.service;

import java.util.List;

import com.hxy.core.region.entity.City;

public interface CityService {
	/**
	 * 根据省份ID查询所有城市
	 * 
	 * @return
	 */
	List<City> selectByProvinceId(int provinceId);
}
