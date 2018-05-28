package com.hxy.core.event.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.appuser.entity.AppUser;
import com.hxy.core.appuser.service.AppUserService;
import com.hxy.core.channel.entity.NewsTag;
import com.hxy.core.event.dao.EventMapper;
import com.hxy.core.event.entity.Event;
import com.hxy.core.event.entity.PushedEvent;
import com.hxy.core.event.entity.SignUserProfile;
import com.hxy.core.news.service.NewsService;
import com.hxy.system.Global;
import com.hxy.util.jms.SingleNewsMessage;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;

@Service("eventService")
public class EventServiceImpl implements EventService {
	@Autowired
	private EventMapper eventMapper;
	@Autowired
	private NewsService newsService;
	@Autowired
	private AppUserService appUserService;
	public DataGrid<Event> dataGrid(Map<String, Object> map) {
		DataGrid<Event> dataGrid = new DataGrid<Event>();
		long total = eventMapper.count(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<Event> list = eventMapper.query(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public int pushEvent(Event eventtmp) {
		try {
		Event event = this.getById(eventtmp.getId());
		PushedEvent pushedEvent = new PushedEvent();
		//获取推送频道
		NewsTag newstag = eventMapper.getTagById(event.getId());
		String tag = newstag.getTag();
		List<String> taglist = new ArrayList<String>();
		taglist.add(tag);
		//设置推送活动内容
		pushedEvent.setGroupName(newstag.getTagName());
		pushedEvent.setTitle(event.getTitle());
		String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(event.getCreateTime());
		pushedEvent.setCreateTime(createTime);
		pushedEvent.setGroupId(event.getGroupId());
		pushedEvent.setId(event.getId());
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("eventId", event.getId());
		pushedEvent.setJoinedPeople(eventMapper.countSignUser(map));
		pushedEvent.setMaxPeople(event.getMaxPeople());
		pushedEvent.setPic(event.getPic());
		pushedEvent.setPlace(event.getPlace());
		Date date = event.getStartTime();
		String startTime = new SimpleDateFormat("MM-dd E").format(event.getStartTime());
		pushedEvent.setStartTimeStr(startTime);
		pushedEvent.setType("1");
		AppUser user = appUserService.getById(event.getUserInfoId());
		pushedEvent.setUserAvatar(user.getUser_avatar());
		pushedEvent.setUserName(user.getUser_name());
		List<PushedEvent> pushedEvents = new ArrayList<PushedEvent>();
		pushedEvents.add(pushedEvent);
		newsService.sendAndroidMessage(taglist, null,pushedEvents);
		newsService.sendIosMessage(taglist, null,pushedEvents);
		} catch (NullPointerException e) {
			// TODO: handle exception
			System.out.println("活动数据有误！");
		}
		return 1;
	}

	public Event getById(long id) {
		Event event = eventMapper.getById(id);
		event.setPicUrl(Global.URL_DOMAIN + event.getPic());
		return event;
	}

	public void save(Event event) {
		if (event == null)
			throw new IllegalArgumentException("event cannot be null!");

		eventMapper.add(event);
	}

	public void update(Event event) {
		if (event == null)
			throw new IllegalArgumentException("event cannot be null!");

		eventMapper.update(event);
		eventMapper.resetNotification(event);
	}

	public void delete(String ids) {
		String[] array = ids.split(",");
		List<Long> list = new ArrayList<Long>();
		for (String id : array) {
			list.add(Long.parseLong(id));
		}
		eventMapper.delete(list);
	}

	public void audit(Event event) {
		if (event == null)
			throw new IllegalArgumentException("event cannot be null!");

		eventMapper.audit(event);

	}

	public DataGrid<SignUserProfile> dataGridForSignUser(Map<String, Object> map) {
		DataGrid<SignUserProfile> dataGrid = new DataGrid<SignUserProfile>();
		long total = eventMapper.countSignUser(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<SignUserProfile> list = eventMapper.querySignUser(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public void undoDelete(long id) {
		eventMapper.undoDelete(id);
	}

}
