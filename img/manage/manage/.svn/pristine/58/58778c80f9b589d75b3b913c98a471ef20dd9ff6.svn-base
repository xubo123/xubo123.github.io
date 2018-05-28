package com.hxy.core.resource.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hxy.base.entity.Tree;
import com.hxy.core.resource.entity.Resource;

public interface ResourceService
{
	void parseTree(List<Tree> tree,List<Tree> allList);
	
	void save(Resource resource);
	
	Resource getById(long id);
	
	void update(Resource resource);
	
	void delete(long Id);
	
	List<Resource> selectAll();
	
	Resource selectByNameOrUrl(Map<String, Object> map);
	
	String export() throws IOException;
	
	void importData(String url);
}
