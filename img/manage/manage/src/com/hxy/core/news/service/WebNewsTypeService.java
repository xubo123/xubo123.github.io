package com.hxy.core.news.service;

import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.news.entity.News;
import com.hxy.core.news.entity.NewsType;

public interface WebNewsTypeService {
	
	public DataGrid<NewsType> dataGrid(Map<String, Object> map);

	public NewsType getById(String id);

	public String save(NewsType type);

	long getNewId();
	
	public void delete(List<Long> list, long parent_id);

	public void update(NewsType type);
	
	/**
	 * 添加“单页面”栏目时添加对应新闻
	 */
	public void saveNews(News news);
	
	/**
	 * 修改“单页面”栏目时修改对应新闻
	 */
	public void updateNews(News news);
	
	/**
	 * 删除“单页面”栏目时删除对应新闻
	 */
	public void deleteNews(String typeId);
	
	NewsType getByName(String name);
	
}
