package com.hxy.core.news.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.news.dao.MobNewsTypeMapper;
import com.hxy.core.news.entity.News;
import com.hxy.core.news.entity.NewsChannel;
import com.hxy.core.news.entity.NewsType;

@Service("mobNewsTypeService")
public class MobNewsTypeServiceImpl implements MobNewsTypeService
{
	@Autowired
	private MobNewsTypeMapper mobNewsTypeMapper;


	public DataGrid<NewsChannel> dataGrid(Map<String, Object> map){
		DataGrid<NewsChannel> dataGrid = new DataGrid<NewsChannel>();
		int total = mobNewsTypeMapper.count2(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<NewsChannel> list = mobNewsTypeMapper.query2(map);
		long parent_id = (Long) map.get("parent_id");
		if(parent_id>0){
			NewsChannel webNewsType = this.getById2(parent_id+"");
			for(NewsChannel channel:list){
				channel.setChannel_pid(webNewsType.getChannel_pid());
			}
		}
		dataGrid.setRows(list);
		return dataGrid;
	}
	

	public NewsType getById(String id){
		return this.mobNewsTypeMapper.getById(id);
	}
	
	public NewsChannel getById2(String id){
		return this.mobNewsTypeMapper.getById2(id);
	}


	public String save(NewsType type){
		long parent_id = type.getParent_id();
		if(parent_id>0){
			//添加子栏目
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("parent_id", parent_id);
			int count  = mobNewsTypeMapper.count(map);
			if(count > 5){
				return "2";
			}
		}
		this.mobNewsTypeMapper.save(type);
		return "1";
	}
	
	public String save2(NewsChannel newsChannel){
	/*	long parent_id = newsChannel.getChannel_pid();
		if(parent_id>0){
			//添加子栏目
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("parent_id", parent_id);
			int count  = mobNewsTypeMapper.count2(map);
			if(count > 5){
				return "2";
			}
		}*/
		this.mobNewsTypeMapper.save2(newsChannel);
		return "1";
	}

	public void delete(List<Long> list,long parent_id){
		if(parent_id==0){								//该栏目为父栏目
			//父栏目删除
			this.mobNewsTypeMapper.deleteById(list);
			//对应子栏目删除
			this.mobNewsTypeMapper.deleteByPid(list);
		}else if(parent_id > 0){
			//子栏目删除									//该栏目为子栏目
			this.mobNewsTypeMapper.deleteById(list);
		}
	}
	

	public void update(NewsType type){
		this.mobNewsTypeMapper.update(type);
	}

	@Override
	public void update2(NewsChannel newsChannel) {
		// TODO Auto-generated method stub
		this.mobNewsTypeMapper.update2(newsChannel);
	}

	public void saveNews(News news) {
		this.mobNewsTypeMapper.saveNews(news);
	}

	public long getNewId() {
		return this.mobNewsTypeMapper.getNewId();
	}

	public void updateNews(News news) {
		this.mobNewsTypeMapper.updateNews(news);
	}
	
	public void deleteNews(String typeId) {
		this.mobNewsTypeMapper.deleteNews(typeId);
	}


	@Override
	public NewsType getByName(String name) {
		return mobNewsTypeMapper.getByName(name);
	}


	
}
