package com.hxy.core.news.service;

import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.news.entity.News;
import com.hxy.core.news.entity.NewsChannel;
import com.hxy.core.news.entity.NewsType;

public interface MobNewsTypeService {
	
	public DataGrid<NewsChannel> dataGrid(Map<String, Object> map);

	public NewsType getById(String id);
	
	public NewsChannel getById2(String id);

	public String save(NewsType type);
	
	public String save2(NewsChannel newsChannel);

	long getNewId();
	
	public void delete(List<Long> list, long parent_id);

	public void update(NewsType type);
	
	public void update2(NewsChannel newsChannel);
	
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
	
	/**
	 * 根据名称获取新闻栏目
	 * 
	 * @param name
	 * @return
	 */
	NewsType getByName(String name);
	

}
