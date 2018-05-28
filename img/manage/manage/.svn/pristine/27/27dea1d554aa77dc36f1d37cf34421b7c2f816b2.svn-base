package com.hxy.core.schoolServ.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.schoolServ.entity.SchoolServ;

public interface SchoolServMapper {
	
	
	/**
	 * 存储
	 * 
	 * @param SchoolServ schoolService
	 * @return true，成功；false，失败；
	 * 
	 */
	boolean save(SchoolServ schoolService);
	
	
	/**
	 * 更新
	 * 
	 * @param SchoolServ schoolService 
	 * @return true，成功；false，失败；
	 * 
	 */
	boolean update(SchoolServ schoolService);
	
	/**
	 * 获取总条数
	 * 
	 * @param Map<String, Object> map
	 * @return long
	 */
	long count(Map<String, Object> map);
	
	/**
	 * 获取列表
	 * 
	 * @param Map<String, Object> map
	 * @return List<SchoolService>
	 */
	List<SchoolServ> list(Map<String, Object> map);
	
	/**
	 * 删除
	 * 
	 * @param List<Long> list
	 */
	void delete(List<Long> list);
	
	/**
	 * 获取详情
	 * 
	 * @param map
	 * @return List<SchoolService>
	 */
	SchoolServ selectById(long id);
	
	/**
	 * 获取服务列表
	 * 
	 * @return List<SchoolService>
	 */
	List<SchoolServ> getServiceList();
}
