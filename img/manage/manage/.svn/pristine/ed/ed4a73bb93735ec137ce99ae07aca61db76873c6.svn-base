package com.hxy.core.schoolServ.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.schoolServ.dao.SchoolServMapper;
import com.hxy.core.schoolServ.entity.SchoolServ;

@Service("schoolServService")
public class SchoolServServiceImpl implements SchoolServService {

	@Autowired
	private SchoolServMapper schoolServiceMapper;
	
	/**
	 * 存储
	 * 
	 * @param SchoolServ schoolService
	 * @return true，成功；false，失败；
	 * 
	 */
	@Override
	public boolean save(SchoolServ schoolService) {
		// TODO Auto-generated method stub
		return schoolServiceMapper.save(schoolService);
	}

	
	/**
	 * 更新
	 * 
	 * @param SchoolServ schoolService 
	 * @return true，成功；false，失败；
	 * 
	 */
	@Override
	public boolean update(SchoolServ schoolService) {
		// TODO Auto-generated method stub
		return schoolServiceMapper.update(schoolService);
	}

	
	/**
	 * 获取总条数
	 * 
	 * @param Map<String, Object> map
	 * @return long
	 */
	@Override
	public long count(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return schoolServiceMapper.count(map);
	}

	
	/**
	 * 获取列表
	 * 
	 * @param Map<String, Object> map
	 * @return List<SchoolService>
	 */
	@Override
	public List<SchoolServ> list(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return schoolServiceMapper.list(map);
	}

	
	/**
	 * 所有信息列表(带分页)
	 * 
	 * @param Map<String, Object> map
	 * @return DataGrid<SchoolService>
	 */

	@Override
	public DataGrid<SchoolServ> dataGrid(Map<String, Object> map) {
		DataGrid<SchoolServ> dataGrid = new DataGrid<SchoolServ>();
		long total = schoolServiceMapper.count(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<SchoolServ> list = schoolServiceMapper.list(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	
	/**
	 * 删除
	 * 
	 * @param String ids
	 */
	@Override
	public void delete(String ids) {
		String[] array = ids.split(",");
		List<Long> list = new ArrayList<Long>();
		for (String id : array)
		{
			list.add(Long.parseLong(id));
		}
		
		schoolServiceMapper.delete(list);
	}

	
	/**
	 * 获取详情
	 * 
	 * @param map
	 * @return List<SchoolService>
	 */
	@Override
	public SchoolServ selectById(long id) {
		// TODO Auto-generated method stub
		return schoolServiceMapper.selectById(id);
	}


	/**
	 * 获取服务列表
	 * 
	 * @return List<SchoolService>
	 */
	@Override
	public List<SchoolServ> getServiceList() {
		// TODO Auto-generated method stub
		return schoolServiceMapper.getServiceList();
	}

}
