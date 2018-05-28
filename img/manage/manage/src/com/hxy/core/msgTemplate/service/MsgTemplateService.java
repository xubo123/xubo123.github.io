package com.hxy.core.msgTemplate.service;

import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.msgTemplate.entity.MsgTemplate;

public interface MsgTemplateService {
	void save(MsgTemplate msgTemplate);
	
	void update(MsgTemplate msgTemplate);
	
	void delete(String ids);
	
	MsgTemplate selectById(long msgTemplateId);
	
	List<MsgTemplate> selectAll();
	
	DataGrid<MsgTemplate> dataGrid(Map<String, Object> map);
	
	MsgTemplate selectByTitle(MsgTemplate msgTemplate);
}
