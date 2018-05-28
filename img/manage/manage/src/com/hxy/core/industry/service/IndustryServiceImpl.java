package com.hxy.core.industry.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.core.industry.dao.IndustryMapper;
import com.hxy.core.industry.entity.Industry;

@Service("industryService")
public class IndustryServiceImpl implements IndustryService{

	@Autowired
	private IndustryMapper industryMapper;
	@Override
	public List<Industry> selectByParentCode(String parentCode) {
		return industryMapper.selectByParentCode(parentCode);
	}

}
