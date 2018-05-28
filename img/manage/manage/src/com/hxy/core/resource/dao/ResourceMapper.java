package com.hxy.core.resource.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.resource.entity.Resource;

public interface ResourceMapper
{
	void save(Resource resource);
	
	void save2Id(Resource resource);

	Resource getById(long id);

	void update(Resource resource);

	void delete(List<Long> list);
	
	void deleteRoleAndResource(List<Long> list);
	
	List<Resource> selectAll();
	
	Resource selectByNameOrUrl(Map<String, Object> map);
	
	List<Resource> selectAllOrderById();
}
