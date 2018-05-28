package com.hxy.core.news.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.news.entity.News;
import com.hxy.core.news.entity.NewsChannel;
import com.hxy.core.news.entity.NewsType;

public interface MobNewsTypeMapper
{

	public List<NewsType> query(Map<String, Object> map);
	
	public List<NewsChannel> query2(Map<String, Object> map);

	public int count(Map<String, Object> map);
	
	public int count2(Map<String, Object> map);

	public NewsType getById(String id);

	public void save(NewsType type);
	
	public void save2(NewsChannel newsChannel);
	
	long getNewId();
	
	/**--新闻栏目删除,根据id--**/
	public void deleteById(List<Long> list);
	/**--新闻栏目删除,根据父id--**/
	public void deleteByPid(List<Long> list);

	public void update(NewsType type);
	
	public void update2(NewsChannel channel);
	
	public void saveNews(News news);
	
	public void updateNews(News news);
	
	public void deleteNews(String typeId);
	
	NewsType getByName(String name);

	public NewsChannel getById2(String id);


	
}
