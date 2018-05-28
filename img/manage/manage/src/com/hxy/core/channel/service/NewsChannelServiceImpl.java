package com.hxy.core.channel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.channel.dao.NewsChannelMapper;
import com.hxy.core.channel.entity.NewsTag;
import com.hxy.system.Global;

@Service("newsChannelService")
public class NewsChannelServiceImpl implements NewsChannelService
{

	@Autowired
	private NewsChannelMapper newsChannelMapper;


	public DataGrid<NewsTag> dataGrid(Map<String, Object> map)
	{
		DataGrid<NewsTag> dataGrid = new DataGrid<NewsTag>();
		long count = newsChannelMapper.countNewsTag(map);
		dataGrid.setTotal(count);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<NewsTag> list = newsChannelMapper.selectNewsTagList(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	

	public void delete(String ids)
	{
		String[] array = ids.split(",");
		List<Long> list = new ArrayList<Long>();
		for (String id : array)
		{
			list.add(Long.parseLong(id));
		}

		newsChannelMapper.delete(list);
	}

	
	public NewsTag selectById2(long tagId)
	{
		NewsTag newsTag = newsChannelMapper.selectById2(tagId);

		newsTag.setTagIconUrl(Global.URL_DOMAIN+newsTag.getTagIcon());

		return newsTag;
	}

	public void update(NewsTag newsTag)
	{
		newsChannelMapper.update(newsTag);
	}


	
	public List<NewsTag> selectAll2(Map<String, Object> map)
	{
		return newsChannelMapper.selectAll2(map);
	}



	

	@Override
	public long countNewsTag(Map<String, Object> map) {

		return newsChannelMapper.countNewsTag(map);
	}

	@Override
	public void save(NewsTag newsTag) {

		newsChannelMapper.saveTag(newsTag);

	}

	@Override
	public List<NewsTag> selectNewsTagList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return newsChannelMapper.selectNewsTagList(map);
	}



	@Override
	public String selectTagbytagId(long tagId) {
		// TODO Auto-generated method stub
		return newsChannelMapper.selectTagbytagId(tagId);
	}	
}
