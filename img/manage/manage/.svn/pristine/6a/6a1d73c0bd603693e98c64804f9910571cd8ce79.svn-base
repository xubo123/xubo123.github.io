package com.hxy.core.dicttype.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.dict.dao.DictMapper;
import com.hxy.core.dicttype.dao.DictTypeMapper;
import com.hxy.core.dicttype.entity.DictType;
import com.hxy.system.GetDictionaryInfo;

@Service("dictTypeService")
public class DictTypeServiceImpl implements DictTypeService {
	@Autowired
	private DictTypeMapper dictTypeMapper;
	@Autowired
	private DictMapper dictMapper;

	public DataGrid<DictType> dataGridDictType(Map<String, Object> map) {
		long total = dictTypeMapper.countDcitType(map);
		DataGrid<DictType> dataGrid = new DataGrid<DictType>();
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<DictType> list = dictTypeMapper.selectDictType(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public void addDictType(DictType dictType) {
		dictTypeMapper.addDictType(dictType);
		GetDictionaryInfo.getInstance().reloadDictionaryInfoMap();
	}

	public void deleteDictType(long id) {
		try {
			dictMapper.deleteByDictTypeId(id);
			dictTypeMapper.deleteDictType(id);
			GetDictionaryInfo.getInstance().reloadDictionaryInfoMap();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public DictType selectById(String id) {
		return dictTypeMapper.selectById(Integer.parseInt(id));
	}

	public int updateDictType(DictType dictType) {
		return dictTypeMapper.updateDictType(dictType);
	}

}
