package com.hxy.core.msgTemplate.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.msgTemplate.entity.MsgTemplate;


public interface MsgTemplateMapper {
	void save(MsgTemplate msgTemplate);

	void update(MsgTemplate msgTemplate);

	void delete(List<Long> list);

	MsgTemplate selectById(long msgTemplateId);

	List<MsgTemplate> selectAll();

	List<MsgTemplate> selectList(Map<String, Object> map);

	long count(Map<String, Object> map);
	
	MsgTemplate selectByTitle(MsgTemplate msgTemplate);
}
