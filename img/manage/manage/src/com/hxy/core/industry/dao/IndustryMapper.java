package com.hxy.core.industry.dao;

import java.util.List;

import com.hxy.core.industry.entity.Industry;

public interface IndustryMapper {
	/**
	 * 查询行业节点下所有子行业节点
	 * 
	 * @param parentCode
	 * @return
	 */
	List<Industry> selectByParentCode(String parentCode);
}
