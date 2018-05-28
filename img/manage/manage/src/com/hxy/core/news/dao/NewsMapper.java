package com.hxy.core.news.dao;

import java.util.List;
import java.util.Map;
import com.hxy.core.dict.entity.Dict;
import com.hxy.core.news.entity.*;

public interface NewsMapper
{
	long countNews(Map<String, Object> map);
	
	long countNews2(Map<String, Object> map);

	List<News> selectNews(Map<String, Object> map);
	
	List<News2> selectNews2(Map<String, Object> map);

	News selectById(long activityId);
	
	News2 selectById2(long activityId);

	void save(News news);
	
	void save2(News2 news);
	
	Long insert(News2 news);

	void update(News news);
	
	void update2(News2 news);

	void delete(List<Long> list);

	List<News> selectForMicro(Map<String, Object> map);

	List<News> selectByIds(List<Long> list);
	
	List<Dict> getAllCategorys(Map<String, Object> map);
	
	void setMobTypeList(Map<String, Object> map);
	void setWebTypeList(Map<String, Object> map);
	
	List<News> listMobTopNews(Map<String, Object> map);
	
	List<News2> listMobNews(Map<String, Object> map);
	/**--查询新闻的数量--**/
	public int listMobNewsCount(Map<String, Object> map);
	
	/**--查询新闻类别并分页--**/
	public List<NewsType> selectTypeList(Map<String, Object> map);
	
	public List<NewsType> selectWebTypeList(Map<String, Object> map);
	
	News selectWebNewFromWebType(NewsType newsType);

	List<NewsChannel> selectTypeList2(Map<String, Object> map);

	NewsChannel selectChannelbyId(long id);

	long countNewsForAlumni(Map<String, Object> map);

	List<News2> selectNewsForAlumni(Map<String, Object> map);
	
}
