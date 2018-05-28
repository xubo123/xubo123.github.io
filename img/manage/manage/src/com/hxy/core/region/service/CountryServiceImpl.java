package com.hxy.core.region.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.core.region.dao.CountryMapper;
import com.hxy.core.region.entity.Country;

@Service("countryService")
public class CountryServiceImpl implements CountryService {
	@Autowired
	private CountryMapper countryMapper;

	@Override
	public List<Country> selectAll() {
		return countryMapper.selectAll();
	}
}
