package com.hxy.core.dict.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.dict.dao.DictMapper;
import com.hxy.core.dict.entity.Dict;
import com.hxy.system.GetDictionaryInfo;

@Service("dictService")
public class DictServiceImpl implements DictService {
	private DictMapper dictMapper;

	public DictMapper getDictMapper() {
		return dictMapper;
	}

	@Autowired
	public void setDictMapper(DictMapper dictMapper) {
		this.dictMapper = dictMapper;
	}

	public DataGrid<Dict> dataGridDict(Map<String, Object> map) {
		long count = dictMapper.countDict(map);
		DataGrid<Dict> dataGrid = new DataGrid<Dict>();
		dataGrid.setTotal(count);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<Dict> list = dictMapper.selectDict(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public void addDict(Dict dictModel) {
		dictMapper.addDict(dictModel);
		GetDictionaryInfo.getInstance().reloadDictionaryInfoMap();
	}

	public void deleteDict(long id) {
		dictMapper.deleteDict(id);
		GetDictionaryInfo.getInstance().reloadDictionaryInfoMap();
	}

	public Dict selectDictById(String id) {
		return dictMapper.selectDictById(Integer.parseInt(id));
	}

	public int updateDict(Dict dictModel) {
		return dictMapper.updateDict(dictModel);
	}

	public List<Dict> selectByDictTypeValue(int dictTypeValue) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dictTypeValue", dictTypeValue);
		return dictMapper.selectByDictTypeValue(map);
	}

	@Override
	public List<Dict> selectByDictTypeId(long dictTypeId) {
		return dictMapper.selectByDictTypeId(dictTypeId);
	}

}
