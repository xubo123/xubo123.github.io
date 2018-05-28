package com.hxy.core.msgTemplate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.msgTemplate.dao.MsgTemplateMapper;
import com.hxy.core.msgTemplate.entity.MsgTemplate;

@Service("msgTemplateService")
public class MsgTemplateServiceImpl implements MsgTemplateService {

	@Autowired
	private MsgTemplateMapper msgTemplateMapper;

	public void save(MsgTemplate msgTemplate) {
		msgTemplateMapper.save(msgTemplate);
	}

	public void update(MsgTemplate msgTemplate) {
		msgTemplateMapper.update(msgTemplate);

	}

	public void delete(String ids) {
		if (ids != null && !"".equals(ids)) {
			String[] idArray = ids.split(",");
			List<Long> list = new ArrayList<Long>();
			for (String id : idArray) {
				list.add(Long.parseLong(id));
			}
			msgTemplateMapper.delete(list);
		}
	}

	public MsgTemplate selectById(long msgTemplateId) {
		return msgTemplateMapper.selectById(msgTemplateId);
	}

	public List<MsgTemplate> selectAll() {
		return msgTemplateMapper.selectAll();
	}

	public DataGrid<MsgTemplate> dataGrid(Map<String, Object> map) {
		DataGrid<MsgTemplate> dataGrid = new DataGrid<MsgTemplate>();
		long count = msgTemplateMapper.count(map);
		dataGrid.setTotal(count);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<MsgTemplate> list = msgTemplateMapper.selectList(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public MsgTemplate selectByTitle(MsgTemplate msgTemplate) {
		return msgTemplateMapper.selectByTitle(msgTemplate);
	}

}
