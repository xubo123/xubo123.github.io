package com.hxy.core.region.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.core.region.dao.CityMapper;
import com.hxy.core.region.entity.City;

@Service("cityService")
public class CityServiceImpl implements CityService {

	@Autowired
	private CityMapper cityMapper;

	@Override
	public List<City> selectByProvinceId(int provinceId) {
		return cityMapper.selectByProvinceId(provinceId);
	}

}
