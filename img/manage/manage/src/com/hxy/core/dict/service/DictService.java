package com.hxy.core.dict.service;

import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.dict.entity.Dict;

public interface DictService {
	DataGrid<Dict> dataGridDict(Map<String, Object> map);

	void addDict(Dict Dict);

	void deleteDict(long id);

	Dict selectDictById(String id);

	int updateDict(Dict Dict);

	List<Dict> selectByDictTypeValue(int dictTypeValue);
	
	List<Dict> selectByDictTypeId(long dictTypeId);
}
