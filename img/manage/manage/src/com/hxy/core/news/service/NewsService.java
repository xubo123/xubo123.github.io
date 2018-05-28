package com.hxy.core.news.service;

import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.dict.entity.Dict;
import com.hxy.core.event.entity.Event;
import com.hxy.core.event.entity.PushedEvent;
import com.hxy.core.news.entity.*;
import com.hxy.util.jms.SingleNewsMessage;

public interface NewsService
{
	DataGrid<News> dataGrid(Map<String, Object> map);
	/**--用于新闻展示--**/
	DataGrid<News2> dataGrid2(Map<String, Object> map);
	
	List<News> selectNews(Map<String, Object> map);

	News selectById(long activityId);
	
	News2 selectById2(long activityId);

	void save(News news);
	
	void save2(News2 news);
	/**--新闻添加，同时生成URL--**/
	void insertNewsAndsetUrl(News2 news,String url);

	void update(News news);
	
	void update2(News2 news);

	void delete(String ids);
	
	NewsChannel selectChannelbyId(long id);
	
	List<Dict> getAllCategorys(Map<String, Object> map);
	
	void setMobTypeList(String ids, String controlStr);
	void setWebTypeList(String ids, String controlStr);
	
	List<News> listMobTopNews(Map<String, Object> map);
	
	List<News2> listMobNews(Map<String, Object> map);
	
	/**--查询新闻的数量--**/
	public int listMobNewsCount(Map<String, Object> map);
	
	
	public List<NewsType> selectTypeList(Map<String, Object> map);
	public List<NewsType> selectWebTypeList(Map<String, Object> map);
	
	/**--查询2级栏目集合--**/
	public List<NewsType> selectLeveList(String id);
	
	/**--查询返回所有的新闻1级栏目,提供给外部web页面--**/
	public List<NewsType> selectMobileNewsType(long category);
	
	
	News selectWebNewFromWebType(NewsType newsType);
	
	List<NewsChannel> selectTypeList2(Map<String, Object> map);


	public void sendIosMessage(List<String> tagList,
			List<SingleNewsMessage> newsList,List<PushedEvent> pushedEvent);

	public void sendAndroidMessage(List<String> tagList,
			List<SingleNewsMessage> newsList,List<PushedEvent> pushedEvent);
	DataGrid<News2> dataGridForAlumni(Map<String, Object> map);
	
}

