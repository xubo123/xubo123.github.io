package com.hxy.core.event.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.channel.entity.NewsTag;
import com.hxy.core.event.entity.Event;
import com.hxy.core.event.entity.SignUserProfile;

public interface EventMapper {

	List<Event> query(Map<String, Object> map);

	long count(Map<String, Object> map);

	Event getById(long id);

	void add(Event event);

	void update(Event event);

	void resetNotification(Event event);

	void delete(List<Long> list);

	void undoDelete(long id);

	void audit(Event event);

	List<SignUserProfile> querySignUser(Map<String, Object> map);

	long countSignUser(Map<String, Object> map);
	
	NewsTag getTagById(long id);

}
