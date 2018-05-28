package com.hxy.core.region.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.core.region.dao.ProvinceMapper;
import com.hxy.core.region.entity.Province;

@Service("provinceService")
public class ProvinceServiceImpl implements ProvinceService {

	@Autowired
	private ProvinceMapper provinceMapper;

	@Override
	public List<Province> selectByCountryId(int countryId) {
		return provinceMapper.selectByCountryId(countryId);
	}

}
