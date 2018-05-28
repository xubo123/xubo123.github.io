package com.hxy.core.community.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.appuser.entity.AppUser;
import com.hxy.core.community.entity.Complaint;
import com.hxy.core.community.entity.GroupBoard;
import com.hxy.core.community.entity.GroupTopic;
import com.hxy.core.community.entity.TopicContext;

public interface CommunityMapper {
	
	List<GroupBoard> getAllGroupBoard();
	
	List<GroupBoard> queryGroupBoard(Map<String, Object> map);
    
    long countGroupBoard(Map<String, Object> map);
    
    List<AppUser> queryGroupBoardMember(Map<String, Object> map);
    
    long countGroupBoardMember(Map<String, Object> map);
	
	void insertGroupBoard(GroupBoard groupBoard);
	
	void updateGroupBoard(GroupBoard groupBoard);
	
	GroupBoard getGroupBoardByDept(long department_id);
	
	void deleteGroupBoardByDept(long department_id);
	
	
	void insertTopic(GroupTopic groupTopic);
	
	void updateTopic(GroupTopic groupTopic);
	
	GroupTopic getTopicById(long id);
	
	void insertTopicContext(TopicContext topicContext);
	
	void updateTopicContext(TopicContext topicContext);
	
	List<GroupTopic> queryTopic(Map<String, Object> map);
    
    long countTopic(Map<String, Object> map);
    
    List<TopicContext> queryTopicContext(Map<String, Object> map);
    
    long countTopicContext(Map<String, Object> map);
    
    List<String> getTopicContextPics(long topic_context_id);
    
    List<Complaint> queryComplaint(Map<String, Object> map);
    
    long countComplaint(Map<String, Object> map);
    
    List<TopicContext> queryContextComplaint(Map<String, Object> map);
    
    long countContextComplaint(Map<String, Object> map);
    
    void handleContextStatus(Map<String, Object> map);
}
