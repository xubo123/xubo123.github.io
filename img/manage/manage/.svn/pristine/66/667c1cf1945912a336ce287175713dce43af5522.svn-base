package com.hxy.core.systemsetting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.core.systemsetting.dao.SystemSettingMapper;
import com.hxy.core.systemsetting.entity.SystemSetting;
import com.hxy.system.GetDictionaryInfo;

@Service("systemSettingService")
public class SystemSettingServiceImpl implements SystemSettingService {

	@Autowired
	private SystemSettingMapper systemSettingMapper;

	@Override
	public List<SystemSetting> selectAll() {
		return systemSettingMapper.selectAll();
	}

	@Override
	public SystemSetting selectById(long systemId) {
		return systemSettingMapper.selectById(systemId);
	}

	@Override
	public void update(SystemSetting systemSetting) {
		systemSettingMapper.update(systemSetting);
		GetDictionaryInfo.getInstance().initSystem();
	}

	@Override
	public void insert(SystemSetting systemSetting) {
		systemSettingMapper.insert(systemSetting);
		GetDictionaryInfo.getInstance().initSystem();
	}

}
