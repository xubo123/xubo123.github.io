package com.hxy.core.region.dao;

import java.util.List;

import com.hxy.core.region.entity.Country;

public interface CountryMapper {
	/**
	 * 查询所有国家
	 * 
	 * @return
	 */
	List<Country> selectAll();
}
