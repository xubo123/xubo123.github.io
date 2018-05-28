package com.hxy.core.dict.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.dict.entity.Dict;

public interface DictMapper {
	int deleteDict(long id);

	long countDict(Map<String, Object> map);

	List<Dict> selectDict(Map<String, Object> map);

	int addDict(Dict dict);

	Dict selectDictById(int id);

	int updateDict(Dict dict);

	List<Dict> selectByDictTypeValue(Map<String, Object> map);
	
	void deleteByDictTypeId(long dictTypeId);
	
	List<Dict> selectByDictTypeId(long dictTypeId);

	void deleteDictbyTagId(List<Long> list);
}
