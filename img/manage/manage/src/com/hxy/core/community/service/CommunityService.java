package com.hxy.core.community.service;

import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.appuser.entity.AppUser;
import com.hxy.core.community.entity.Complaint;
import com.hxy.core.community.entity.GroupBoard;
import com.hxy.core.community.entity.GroupTopic;
import com.hxy.core.community.entity.TopicContext;

public interface CommunityService {

	/**
	 * 获取所有的群组板块
	 */
	List<GroupBoard> getAllGroupBoard();
	
	/**
	 * 获取群组板块列表
	 */
	DataGrid<GroupBoard> dataGridGroupBoard(Map<String, Object> map);
	
	/**
	 * 获取群组板块成员列表<br>
	 * key:	 boardId=社区板块<br>
	 * key:	 parentId=花絮的父主题，相当于查询该父主题的花絮 <br>
	 */
	DataGrid<AppUser> dataGridGroupBoardMember(Map<String, Object> map);
	
	/**
	 * 新建群组板块
	 */
	void insertGroupBoard(GroupBoard groupBoard);
	
	/**
	 * 更新群组板块
	 */
	void updateGroupBoard(GroupBoard groupBoard);
	
	
	/**
	 * 获取某机构对应的社群板块
	 */
	GroupBoard getGroupBoardByDept(long department_id);
	
	/**
	 * 删除某机构对应的社群板块
	 */
	void deleteGroupBoardByDept(long department_id);
	
	
	/**
	 * 新建主题
	 */
	void insertTopic(GroupTopic groupTopic);
	
	/**
	 * 更新主题
	 */
	void updateTopic(GroupTopic groupTopic);
	
	/**
     * 获取主题
     */
    GroupTopic getTopicById(long id);
	
	/**
	 * 新建主题内容
	 */
	void insertTopicContext(TopicContext topicContext);
	
	/**
	 * 更新主题内容
	 */
	void updateTopicContext(TopicContext topicContext);
	
	
	/**
	 * 获取主题列表<br>
	 * 花絮也是主题，只是其拥有父主题<br>
	 * key:  topicType<br>
	 * key:	 boardId=社区板块<br>
	 * key:	 parentId=花絮的父主题，相当于查询该父主题的花絮 <br>
	 * key:  status=状态，就是topic.first_post.status<br>
	 */
	DataGrid<GroupTopic> dataGridTopic(Map<String, Object> map);
	
	/**
	 * 获取某主题/花絮的内容列表<br>
	 * 花絮也是主题<br>
	 * key:  topicId
	 */
	DataGrid<TopicContext> dataGridTopicContext(Map<String, Object> map);
	
	
	/**
	 * 获取某主题内容的图片
	 */
	List<String> getTopicContextPics(long topic_context_id);
	
	/**
	 * 获取某主题内容的投诉列表
	 * key:  contextId
	 */
	DataGrid<Complaint> dataGridComplaint(Map<String, Object> map);
	
	/**
	 * 获取有投诉的主题内容列表<br>
	 * key:  status<br>
	 * key:  topicType=[0 社区主题  ,1 活动主题]
	 */
	DataGrid<TopicContext> dataGridContextComplaint(Map<String, Object> map);
	
	/**
	 * 设置某主题内容的状态<br>
	 * key:  handleStatus<br>
	 * 		 contextId
	 */
	void handleContextStatus(Map<String, Object> map);
}
