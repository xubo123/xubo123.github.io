package com.hxy.core.region.dao;

import java.util.List;

import com.hxy.core.region.entity.City;

public interface CityMapper {
	/**
	 * 根据省份ID查询所有城市
	 * 
	 * @return
	 */
	List<City> selectByProvinceId(int provinceId);
}
