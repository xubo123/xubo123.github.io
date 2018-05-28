package com.hxy.core.event.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.community.entity.GroupBoard;
import com.hxy.core.community.entity.GroupTopic;
import com.hxy.core.community.entity.TopicContext;
import com.hxy.core.community.service.CommunityService;
import com.hxy.core.event.entity.Event;
import com.hxy.core.event.service.EventService;
import com.hxy.core.news.service.NewsService;
import com.hxy.util.WebUtil;

@Namespace("/event")
@Action(value = "eventAction")
public class EventAction extends AdminBaseAction {

	private static final Logger logger = Logger.getLogger(EventAction.class);

	private Event event;

	@Autowired
	private EventService eventService;
	
	@Autowired
	private CommunityService communityService;

	public void save() {
		save0();
	}

	public void savex() {
		save0();
	}

	private void save0() {
		Message message = new Message();
		try {

			long board_id = 0;
			// 获取活动所属机构的社群板块
			long department_id = 0;
			if (event.getType() == 0) { // 官方活动
				department_id = event.getDepartment_id(); // 用户选择的院系机构
			} else if (event.getType() == 5) { // 校友会活动
				department_id = getUser().getDepts().get(0).getDepartment_id(); // 用户所属的校友会机构
			}
			if (department_id != 0) {
				GroupBoard groupBoard = communityService
						.getGroupBoardByDept(department_id);
				board_id = groupBoard.getId();
			}

			String user_ip = WebUtil.getIpAddr(getRequest());
			GroupTopic groupTopic = new GroupTopic();
			groupTopic.setBoard_id(board_id);
			groupTopic.setTopic_title(event.getTitle());
			groupTopic.setTopic_type(1);
			groupTopic.setUser_ip(user_ip);
			groupTopic.setToplevel(1);
			communityService.insertTopic(groupTopic);

			TopicContext topicContext = new TopicContext();
			topicContext.setTopic_id(groupTopic.getId());
			topicContext.setContext(event.getContent());
			topicContext.setUser_ip(user_ip);
			communityService.insertTopicContext(topicContext);

			GroupTopic groupTopic2 = new GroupTopic();
			groupTopic2.setId(groupTopic.getId());
			groupTopic2.setFirst_post_id(topicContext.getId());
			communityService.updateTopic(groupTopic2);

			event.setGroupId(groupTopic.getId());

			event.setCreated_by(getUser().getUserId());
			if (event.getType() == 0) { // 官方活动
				event.setAuditStatus(1);
			} else if (event.getType() == 5) { // 校友会活动
				event.setAuditStatus(0);
			}
			// 生成签到码
			if (event.isNeedSignIn()) {
				event.setSignInCode(generateSignInCode());
			}

			eventService.save(event);
			message.setMsg("保存成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("保存失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void getListOfficial() {
		getList("0", "");
	}

	public void getListAlumni() {
		getList("5", "");
	}

	public void getListPersonal() {
		getList("9", "");
	}

	public void getListx() {
		getList("5", Long.toString(getUser().getUserId()));
	}

	private void getList(String type, String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String title = getRequest().getParameter("title");
		String category = getRequest().getParameter("category");
		String place = getRequest().getParameter("place");
		String organizer = getRequest().getParameter("organizer");
		String startFrom = getRequest().getParameter("startFrom");
		String startTo = getRequest().getParameter("startTo");
		String endFrom = getRequest().getParameter("endFrom");
		String endTo = getRequest().getParameter("endTo");
		String userInfoId = getRequest().getParameter("userInfoId");
		String auditStatus = getRequest().getParameter("auditStatus");
		String status = getRequest().getParameter("status");
		String alumniId = getRequest().getParameter("alumniId");
		String region = getRequest().getParameter("region");
		map.put("page", page);
		map.put("rows", rows);
		map.put("title", title);
		map.put("category", category);
		map.put("place", place);
		map.put("organizer", organizer);
		map.put("startFrom", startFrom);
		map.put("startTo", startTo);
		map.put("endFrom", endFrom);
		map.put("endTo", endTo);
		map.put("userInfoId", userInfoId);
		map.put("auditStatus", auditStatus);
		map.put("status", status);
		map.put("alumniId", alumniId);
		map.put("region", region);

		map.put("type", type);
		map.put("userId", userId);

		if (getUser().getRole().getSystemAdmin() != 1 && type.equals("0")) {
			map.put("userDeptList", getUser().getDepts());
		}
		super.writeJson(eventService.dataGrid(map));
	}

	public void getSignupPeople() {
		getSignupPeople0();
	}

	public void getSignupPeoplex() {
		getSignupPeople0();
	}

	private void getSignupPeople0() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		map.put("eventId", id);
		super.writeJson(eventService.dataGridForSignUser(map));
	}

	public void getEventBoard() {
		getEventBoard0();
	}

	public void getEventBoardx() {
		getEventBoard0();
	}

	private void getEventBoard0() {
		String parentId = getRequest().getParameter("parentId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		map.put("parentId", parentId);
		map.put("topicType", "2");
		super.writeJson(communityService.dataGridTopic(map));
	}

	public void getEventBoardComment() {
		getEventBoardComment0();
	}

	public void getEventBoardCommentx() {
		getEventBoardComment0();
	}

	private void getEventBoardComment0() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		map.put("topicId", id);
		super.writeJson(communityService.dataGridTopicContext(map));
	}

	public void getEventBoardComplaint() {
		getEventBoardComplaint0();
	}

	public void getEventBoardComplaintx() {
		getEventBoardComplaint0();
	}

	private void getEventBoardComplaint0() {
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
		if (status == null) {
			status = "0";
		}
		map.put("page", page);
		map.put("rows", rows);
		map.put("status", status);
		map.put("topicType", "1");
		// if(isAlumni) {
		// com.hxy.core.user.entity.User user =
		// (com.hxy.core.user.entity.User)getSession().get("user");
		// map.put("userId", user.getUserId());
		// }
		super.writeJson(communityService.dataGridContextComplaint(map));
	}

	public void getByIdOfficial() {
		getById();
	}

	public void getByIdAlumni() {
		getById();
	}

	public void getByIdPersonal() {
		getById();
	}

	public void getByIdx() {
		getById();
	}

	private void getById() {
		super.writeJson(eventService.getById(event.getId()));
	}

	public void update() {
		update0();
	}

	public void updatex() {
		update0();
	}

	private void update0() {
		Message message = new Message();
		try {

			if (event.getType() == 0) { // 官方活动的活动所属院系可能修改
				long board_id = 0;
				long department_id = event.getDepartment_id(); // 用户选择的院系机构
				if (department_id != 0) {
					GroupBoard groupBoard = communityService
							.getGroupBoardByDept(department_id);
					board_id = groupBoard.getId();
				}
				String user_ip = WebUtil.getIpAddr(getRequest());
				GroupTopic groupTopic = new GroupTopic();
				groupTopic.setId(event.getGroupId());
				groupTopic.setBoard_id(board_id);
				groupTopic.setTopic_title(event.getTitle());
				groupTopic.setUser_ip(user_ip);
				groupTopic.setModified_date(new Date());
				communityService.updateTopic(groupTopic);

				GroupTopic t = communityService
						.getTopicById(event.getGroupId());
				TopicContext topicContext = new TopicContext();
				topicContext.setId(t.getFirst_post_id());
				topicContext.setContext(event.getContent());
				topicContext.setUser_ip(user_ip);
				topicContext.setModified_date(new Date());
				communityService.updateTopicContext(topicContext);

			}

			// 原先无签到码，现在需要，则生成
			if (event.isNeedSignIn() && event.getSignInCode().isEmpty()) {
				event.setSignInCode(generateSignInCode());
			}
			// 原先有签到码，现在不需要，则去掉
			if (!event.isNeedSignIn() && !event.getSignInCode().isEmpty()) {
				event.setSignInCode("");
			}

			eventService.update(event);
			message.setMsg("保存成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("保存失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void audit() {
		audit0();
	}

	public void auditAlumni() {
		audit0();
	}

	public void auditx() {
		audit0();
	}

	public void audit0() {
		Message message = new Message();
		try {
			com.hxy.core.user.entity.User user = (com.hxy.core.user.entity.User) getSession()
					.get("user");
			event.setAuditUserId(user.getUserId());

			eventService.audit(event);
			eventService.pushEvent(event);
			message.setMsg("审核成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("审核失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}


	public void delete() {
		delete0();
	}

	public void deletex() {
		delete0();
	}

	private void delete0() {
		Message message = new Message();
		try {
			eventService.delete(ids);
			message.setMsg("删除成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void undoDelete() {
		undoDelete0();
	}

	public void undoDeletex() {
		undoDelete0();
	}

	private void undoDelete0() {
		Message message = new Message();
		try {
			eventService.undoDelete(id);
			message.setMsg("恢复成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("恢复失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void handleEventBoard() {
		handleEventBoard0();
	}

	public void handleEventBoardx() {
		handleEventBoard0();
	}

	private void handleEventBoard0() {
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

	public void viewEventBoardPic() {
		viewEventBoardPic0();
	}

	public void viewEventBoardPicx() {
		viewEventBoardPic0();
	}

	private void viewEventBoardPic0() {
		List<Map<String, String>> boardPicList = new ArrayList<Map<String, String>>();
		List<String> pics = communityService.getTopicContextPics(id);
		for (String pic : pics) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("n", pic); // normal 原图地址
			map.put("s", WebUtil.getPictureByType(pic, "MIN")); // small 小缩略图地址
			map.put("b", WebUtil.getPictureByType(pic, "MAX")); // big 大缩略图地址
			boardPicList.add(map);
		}
		super.writeJson(boardPicList);
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	private String generateSignInCode() {
		Random r = new Random();
		int x = r.nextInt(9999);
		String code = String.format("%04d", x);
		return code;
	}

}
