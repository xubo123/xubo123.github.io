package com.hxy.core.community.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.appuser.entity.AppUser;
import com.hxy.core.community.dao.CommunityMapper;
import com.hxy.core.community.entity.Complaint;
import com.hxy.core.community.entity.GroupBoard;
import com.hxy.core.community.entity.GroupTopic;
import com.hxy.core.community.entity.TopicContext;
import com.hxy.system.Global;

@Service("communityService")
public class CommunityServiceImpl implements CommunityService {

	@Autowired
	private CommunityMapper communityMapper;

	@Override
	public void insertTopic(GroupTopic groupTopic) {
		communityMapper.insertTopic(groupTopic);
	}
	
	@Override
	public void updateTopic(GroupTopic groupTopic) {
		communityMapper.updateTopic(groupTopic);
	}
	
	@Override
	public void insertTopicContext(TopicContext topicContext) {
		communityMapper.insertTopicContext(topicContext);
		
	}
	
	@Override
	public void updateTopicContext(TopicContext topicContext) {
		communityMapper.updateTopicContext(topicContext);	
	}

	@Override
	public GroupBoard getGroupBoardByDept(long department_id) {
		return communityMapper.getGroupBoardByDept(department_id);
	}

	@Override
	public GroupTopic getTopicById(long id) {
		return communityMapper.getTopicById(id);
	}

	@Override
	public DataGrid<GroupTopic> dataGridTopic(Map<String, Object> map) {
		DataGrid<GroupTopic> dataGrid = new DataGrid<GroupTopic>();
        long total = communityMapper.countTopic(map);
        dataGrid.setTotal(total);
        int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
        map.put("start", start);
        List<GroupTopic> list = communityMapper.queryTopic(map);
        dataGrid.setRows(list);
        return dataGrid;
	}

	@Override
	public DataGrid<TopicContext> dataGridTopicContext(Map<String, Object> map) {
		DataGrid<TopicContext> dataGrid = new DataGrid<TopicContext>();
        long total = communityMapper.countTopicContext(map);
        dataGrid.setTotal(total);
        int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
        map.put("start", start);
        List<TopicContext> list = communityMapper.queryTopicContext(map);
        dataGrid.setRows(list);
        return dataGrid;
	}

	@Override
	public List<String> getTopicContextPics(long topic_context_id) {
		List<String> pics = communityMapper.getTopicContextPics(topic_context_id);
		List<String> picList = new ArrayList<String>();
		for(String pic : pics) {
			picList.add(Global.URL_DOMAIN + pic);
		}
		return picList;
	}

	@Override
	public DataGrid<Complaint> dataGridComplaint(Map<String, Object> map) {
		DataGrid<Complaint> dataGrid = new DataGrid<Complaint>();
        long total = communityMapper.countComplaint(map);
        dataGrid.setTotal(total);
        int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
        map.put("start", start);
        List<Complaint> list = communityMapper.queryComplaint(map);
        dataGrid.setRows(list);
        return dataGrid;
	}

	@Override
	public DataGrid<TopicContext> dataGridContextComplaint(Map<String, Object> map) {
		DataGrid<TopicContext> dataGrid = new DataGrid<TopicContext>();
        long total = communityMapper.countContextComplaint(map);
        dataGrid.setTotal(total);
        int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
        map.put("start", start);
        List<TopicContext> list = communityMapper.queryContextComplaint(map);
        dataGrid.setRows(list);
        return dataGrid;
	}

	@Override
	public void handleContextStatus(Map<String, Object> map) {
		communityMapper.handleContextStatus(map);
	}

	@Override
	public List<GroupBoard> getAllGroupBoard() {
		return communityMapper.getAllGroupBoard();
	}

	@Override
	public void insertGroupBoard(GroupBoard groupBoard) {	
		communityMapper.insertGroupBoard(groupBoard);
	}

	@Override
	public void updateGroupBoard(GroupBoard groupBoard) {
		communityMapper.updateGroupBoard(groupBoard);
	}

	@Override
	public void deleteGroupBoardByDept(long department_id) {
		communityMapper.deleteGroupBoardByDept(department_id);
	}

	@Override
	public DataGrid<GroupBoard> dataGridGroupBoard(Map<String, Object> map) {
		DataGrid<GroupBoard> dataGrid = new DataGrid<GroupBoard>();
        long total = communityMapper.countGroupBoard(map);
        dataGrid.setTotal(total);
        int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
        map.put("start", start);
        List<GroupBoard> list = communityMapper.queryGroupBoard(map);
        dataGrid.setRows(list);
        return dataGrid;
	}

	@Override
	public DataGrid<AppUser> dataGridGroupBoardMember(Map<String, Object> map) {
		DataGrid<AppUser> dataGrid = new DataGrid<AppUser>();
        long total = communityMapper.countGroupBoardMember(map);
        dataGrid.setTotal(total);
        int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
        map.put("start", start);
        List<AppUser> list = communityMapper.queryGroupBoardMember(map);
        dataGrid.setRows(list);
        return dataGrid;
	}




	
	
	
}
