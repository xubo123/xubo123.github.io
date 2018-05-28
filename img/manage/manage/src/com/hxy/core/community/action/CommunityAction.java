package com.hxy.core.community.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.community.entity.GroupBoard;
import com.hxy.core.community.service.CommunityService;
import com.hxy.core.department.entity.Department;
import com.hxy.util.WebUtil;

@Namespace("/community")
@Action(value = "communityAction")
public class CommunityAction extends AdminBaseAction {

	private static final Logger logger = Logger.getLogger(CommunityAction.class);

    @Autowired
    private CommunityService communityService;
  
    public void doNotNeedSecurity_getAllGroupBoard() {
    	super.writeJson(communityService.getAllGroupBoard());
    }
    
    
    public void getGroupBoardList() {
    	getGroupBoardList0();
    }
    public void getGroupBoardListx() {
    	getGroupBoardList0();
    }
    private void getGroupBoardList0() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("rows", rows);
        super.writeJson(communityService.dataGridGroupBoard(map));
    }
    
    public void getMemberList() {
    	getMemberList0();
    }
    public void getMemberListx() {
    	getMemberList0();
    }
    private void getMemberList0() {
    	String boardId = getRequest().getParameter("boardId");
    	if(boardId == null || boardId.isEmpty()) {
    		if(getUser().getRole().getSystemAdmin()!=1) {
    			Department userDept = getUser().getDepts().get(0);
    			GroupBoard board = communityService.getGroupBoardByDept(userDept.getDepartment_id());
    			boardId = Long.toString(board.getId());
    		}
    	}
    	
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("rows", rows);
        map.put("boardId", boardId);
        super.writeJson(communityService.dataGridGroupBoardMember(map));
    }
    
    public void getTopicList() {
    	getTopicList0();
    }
    public void getTopicListx() {
    	getTopicList0();
    }
    private void getTopicList0() {
    	String status = getRequest().getParameter("status");
    	String boardId = getRequest().getParameter("boardId");
    	if(getUser().getRole().getSystemAdmin()!=1) {
			Department userDept = getUser().getDepts().get(0);
			GroupBoard board = communityService.getGroupBoardByDept(userDept.getDepartment_id());
			boardId = Long.toString(board.getId());
		}
    	
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("rows", rows);
        map.put("topicType", "0");
        map.put("status", status);
        map.put("boardId", boardId);
        super.writeJson(communityService.dataGridTopic(map));
    }
    
    public void getTopicComment() {
    	getTopicComment0();
    }
    public void getTopicCommentx() {
    	getTopicComment0();
    }
    private void getTopicComment0() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("rows", rows);
        map.put("topicId", id);
        super.writeJson(communityService.dataGridTopicContext(map));
    }
    
    public void getContextComplaint() {
    	getContextComplaint0();
    }
    public void getContextComplaintx() {
    	getContextComplaint0();
    }
    private void getContextComplaint0() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("rows", rows);
        map.put("contextId", id);
        super.writeJson(communityService.dataGridComplaint(map));
    }
    
    public void getComplaintList() {
    	getComplaintList0(false);
    }
    public void getComplaintListx() {
    	getComplaintList0(true);
    }
    private void getComplaintList0(boolean isAlumni) {
        Map<String, Object> map = new HashMap<String, Object>();
        String status = getRequest().getParameter("status");
        if(status == null) {
        	status = "0";
        }
        map.put("page", page);
        map.put("rows", rows);
        map.put("status", status);
        map.put("topicType", "0");

        super.writeJson(communityService.dataGridContextComplaint(map));
    }

   
    public void handleTopic() {
    	handleTopic0();
    }
    public void handleTopicx() {
    	handleTopic0();
    }
    private void handleTopic0() {
    	String handleStatus = getRequest().getParameter("handleStatus");
    	Message message = new Message();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contextId", id);
        map.put("handleStatus", handleStatus);
        try {
        	communityService.handleContextStatus(map);
            message.setMsg("处理成功");
            message.setSuccess(true);
        } catch (Exception e) {
            logger.error(e, e);
            message.setMsg("处理失败");
            message.setSuccess(false);
        }
        super.writeJson(message);
    }
    
    public void handleComplaint() {
    	handleComplaint0();
    }
    public void handleComplaintx() {
    	handleComplaint0();
    }
    private void handleComplaint0() {
    	String handleStatus = getRequest().getParameter("handleStatus");
    	Message message = new Message();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contextId", id);
        map.put("handleStatus", handleStatus);

        try {
        	communityService.handleContextStatus(map);
            message.setMsg("处理成功");
            message.setSuccess(true);
        } catch (Exception e) {
            logger.error(e, e);
            message.setMsg("处理失败");
            message.setSuccess(false);
        }
        super.writeJson(message);
    }
    
    public void viewContextPic() {
    	viewContextPic0();
    }
    public void viewContextPicx() {
    	viewContextPic0();
    }
    private void viewContextPic0() {
    	List<Map<String, String>> contextPicList = new ArrayList<Map<String,String>>();
		List<String> pics = communityService.getTopicContextPics(id);
		for(String pic : pics) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("n", pic);  								 //normal 原图地址
			map.put("s", WebUtil.getPictureByType(pic, "MIN"));  //small  小缩略图地址
			map.put("b", WebUtil.getPictureByType(pic, "MAX"));  //big    大缩略图地址
			contextPicList.add(map);
		}
		super.writeJson(contextPicList);
	}

	
}
