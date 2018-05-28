package com.hxy.core.region.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.core.region.dao.AreaMapper;
import com.hxy.core.region.entity.Area;

@Service("areaService")
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaMapper areaMapper;

	@Override
	public List<Area> selectByCityId(int cityId) {
		return areaMapper.selectByCityId(cityId);
	}

}
