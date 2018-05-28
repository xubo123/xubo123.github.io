package com.hxy.core.dicttype.entity;

import java.io.Serializable;
import java.util.List;

import com.hxy.core.dict.entity.Dict;

public class DictType implements Serializable {
	private static final long serialVersionUID = 1L;
	private long dictTypeId;
	private String dictTypeName;
	private int dictTypeValue;
	private List<Dict> list;

	public long getDictTypeId() {
		return dictTypeId;
	}

	public void setDictTypeId(long dictTypeId) {
		this.dictTypeId = dictTypeId;
	}

	public void setDictTypeId(int dictTypeId) {
		this.dictTypeId = dictTypeId;
	}

	public String getDictTypeName() {
		return dictTypeName;
	}

	public void setDictTypeName(String dictTypeName) {
		this.dictTypeName = dictTypeName;
	}

	public int getDictTypeValue() {
		return dictTypeValue;
	}

	public void setDictTypeValue(int dictTypeValue) {
		this.dictTypeValue = dictTypeValue;
	}

	public List<Dict> getList() {
		return list;
	}

	public void setList(List<Dict> list) {
		this.list = list;
	}

}
