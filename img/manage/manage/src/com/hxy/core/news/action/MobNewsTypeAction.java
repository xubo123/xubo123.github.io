package com.hxy.core.news.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.news.entity.News;
import com.hxy.core.news.entity.NewsChannel;
import com.hxy.core.news.entity.NewsType;
import com.hxy.core.news.service.MobNewsTypeService;
import com.hxy.util.WebUtil;

@Namespace("/mobNewsType")
@Action(value = "mobNewsTypeAction")
public class MobNewsTypeAction extends AdminBaseAction {

	@Autowired
	private MobNewsTypeService mobNewsTypeService;

	private NewsType type;
	
	private NewsChannel channel;//手机新闻栏目

//	private String country;
	private String province;
	private String city;
	
	
	/**--新闻类型管理--**/
	public void dataGridNewsType(){
		if(channel==null){
			channel = new NewsChannel();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		map.put("page", page);
		map.put("parent_id", channel.getChannel_pid());
		map.put("name", channel.getChannel_name());
		map.put("type", channel.getChannel_type());
		super.writeJson(mobNewsTypeService.dataGrid(map));
	}
	
	/**--新闻类型添加--**/
	public void saveNewsType(){
		Message message = new Message();
		if((province==null && city==null) || province.isEmpty()) {
			type.setCityName("");
		} else {
			type.setCityName(province + " " + city);
		}
		String result = mobNewsTypeService.save(type);
		if("1".equals(result)){
			//如果是“单页面”栏目添加对应新闻
			if(type.getType()==3) {
				long newTypeId = mobNewsTypeService.getNewId();
				News news = new News();
				news.setTitle(type.getNewsTitle());
				news.setContent(type.getNewsContent());
				news.setChannelName("");
				news.setCategory(newTypeId);
				mobNewsTypeService.saveNews(news);
			}
			
			message.setMsg("添加成功");
			message.setSuccess(true);
		}else if("2".equals(result)){
			message.setMsg("子栏目不能超过6个");
			message.setSuccess(false);
		}else{
			message.setMsg("添加失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}
	/**--新闻类型添加--**/
	public void saveNewsChannel(){
		Message message = new Message();
		channel.setCreated_by(getUser().getUserId());
		String result = mobNewsTypeService.save2(channel);
		if("1".equals(result)){
			message.setMsg("添加成功");
			message.setSuccess(true);
		}else{
			message.setMsg("添加失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}
	
	/**--新闻栏目删除--**/
	public void deleteNewsType(){
		Message message = new Message();
		long parent_id = WebUtil.toLong(this.getRequest().getParameter("parent_id"));
		if(!WebUtil.isEmpty(ids)){
			String[] array = ids.split(",");
			List<Long> list = new ArrayList<Long>();
			for (String id : array){
				mobNewsTypeService.deleteNews(id);
				list.add(Long.parseLong(id));
			}
			mobNewsTypeService.delete(list, parent_id);
			message.setSuccess(true);
			message.setMsg("删除成功");
			super.writeJson(message);
		}
	}
	
	/**--查看新闻栏目--**/
	public void getNewsType(){
		NewsChannel newsChannel = this.mobNewsTypeService.getById2(Long.toString(channel.getId()));
		super.writeJson(newsChannel);
	}
	
	
	/**--编辑新闻栏目--**/
	public void updateNewsType(){
		Message message = new Message();
		this.mobNewsTypeService.update2(channel);
//		if(type.getType()==3) {
//			News news = new News();
//			news.setTitle(type.getNewsTitle());
//			news.setContent(type.getNewsContent());
//			news.setCategory(type.getId());
//			mobNewsTypeService.updateNews(news);
//		}
		message.setSuccess(true);
		message.setMsg("更新成功");
		super.writeJson(message);
	}


	public NewsType getType() {
		return type;
	}

	public void setType(NewsType type) {
		this.type = type;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public NewsChannel getChannel() {
		return channel;
	}

	public void setChannel(NewsChannel channel) {
		this.channel = channel;
	}


}
