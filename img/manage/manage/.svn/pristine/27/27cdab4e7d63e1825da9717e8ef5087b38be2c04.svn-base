package com.hxy.core.channel.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.channel.entity.NewsTag;

public interface NewsChannelMapper
{
	
	List<NewsTag> selectAll2(Map<String, Object> map);

	void delete(List<Long> list);


	void update(NewsTag newsTag);
	

	List<NewsTag> selectNewsTagList(Map<String, Object> map);

	long countNewsTag(Map<String, Object> map);

	void saveTag(NewsTag newsTag);

	NewsTag selectById2(long tagId);

	String selectTagbytagId(long tagId);
}
