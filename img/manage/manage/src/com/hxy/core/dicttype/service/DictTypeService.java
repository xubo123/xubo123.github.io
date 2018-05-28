package com.hxy.core.dicttype.service;

import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.dicttype.entity.DictType;


public interface DictTypeService {
	DataGrid<DictType> dataGridDictType(Map<String, Object> map);
	
	void addDictType(DictType dictType);
	
	void deleteDictType(long id);
	
	DictType selectById(String id);
	
	int updateDictType(DictType dictType);
}
