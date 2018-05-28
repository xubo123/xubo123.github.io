package com.hxy.core.mobevent.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.core.dict.entity.Dict;
import com.hxy.core.mobevent.dao.MobEventMapper;
import com.hxy.core.mobevent.entity.CyEvent;
import com.hxy.core.mobevent.entity.CyEventBoard;
import com.hxy.core.mobevent.entity.CyEventBoardComment;
import com.hxy.core.mobevent.entity.CyEventBoardComplaint;
import com.hxy.core.mobevent.entity.CyEventBoardPic;
import com.hxy.core.mobevent.entity.CyEventBoardPraise;
import com.hxy.core.mobevent.entity.CyEventSign;
import com.hxy.util.WebUtil;


@Service("mobEventService")
public class MobEventServiceImpl implements MobEventService {

	@Autowired
	private MobEventMapper mobEventMapper;

	
	/**
	 * 通过活动ID和用户ID得到活动详情
	 * 
	 * @param long id
	 * @param String userId
	 * @return Map<String, Object>
	 * 
	 */
	@Override
	public Map<String, Object> getEventDetail(long id, String userId) {
		// TODO Auto-generated method stub
		
		CyEvent cyEvent = getEventById(id);
		
		List<CyEventBoardPic> eventBoardPicList = null; 
		if(cyEvent == null)
		{
			return null;
		}
		else
		{
			cyEvent.setStartShortTime(WebUtil.formatDateByPattern(cyEvent.getStartTime(), WebUtil.MDHM));
			cyEvent.setEndShortTime(WebUtil.formatDateByPattern(cyEvent.getEndTime(), WebUtil.MDHM));
			cyEvent.setSignupStartShortTime(WebUtil.formatDateByPattern(cyEvent.getSignupStartTime(), WebUtil.MDHM));
			cyEvent.setSignupEndShortTime(WebUtil.formatDateByPattern(cyEvent.getSignupEndTime(), WebUtil.MDHM));
			
			CyEventBoard eventBoard = new CyEventBoard();eventBoard.setEventId(cyEvent.getId());
			eventBoardPicList = getEventBoardPicListForEventDetail(eventBoard);
		}
		
		CyEventSign eventSign = new CyEventSign();
		eventSign.setUserInfoId(userId);
		eventSign.setEventId(id);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", "5");
		//map.put("userInfoId", userId);
		map.put("eventId", id);
		
		Map<String, Object> eventDetail = new HashMap<String, Object>();
		String userPic = cyEvent.getUserAvatar();
		
		if(WebUtil.isNumeric(userPic))
		{
			cyEvent.setUserAvatar(WebUtil.getIcon(Integer.parseInt(userPic)));
		}
		
		
		eventDetail.put("cyEvent", cyEvent);
		eventDetail.put("eventBoardPicList", eventBoardPicList);
		eventDetail.put("eventSign", getEventSign(eventSign));eventSign.setUserInfoId(null);
		eventDetail.put("countEventSign", countEventSign(eventSign));
		eventDetail.put("eventSignList", getEventSignList(map));
		
		return eventDetail;
	}
	
	
	/**
	 * 通过活动ID得到活动对象
	 * 
	 * @param long id
	 * @return CyEvent
	 * 
	 */
	@Override
	public CyEvent getEventById(long id) {
		// TODO Auto-generated method stub
		CyEvent cyEvent = mobEventMapper.getEventById(id);
		
		if(cyEvent != null)
		{
			String userPic = cyEvent.getUserAvatar();
			
			if(WebUtil.isNumeric(userPic))
			{
				cyEvent.setUserAvatar(WebUtil.getIcon(Integer.parseInt(userPic)));
			}
		}
		
		
		
		return cyEvent;
	}

	/**
	 * 通过指定活动签到报名对象中的参数得到活动签到报名对象
	 * 
	 * @param CyEventSign eventSign
	 * @return CyEventSign
	 * 
	 */
	@Override
	public CyEventSign getEventSign(CyEventSign eventSign) {
		// TODO Auto-generated method stub
		CyEventSign cyEventSign = mobEventMapper.getEventSign(eventSign);
		
		if(cyEventSign != null)
		{
			String userPic = cyEventSign.getUserAvatar();
			
			if(WebUtil.isNumeric(userPic))
			{
				cyEventSign.setUserAvatar(WebUtil.getIcon(Integer.parseInt(userPic)));
			}
		}
		
		
		
		return cyEventSign;
	}
	
	/**
	 * 通过指定活动签到报名对象中的参数得到活动签到报名对象的列表
	 * 
	 * @param Map<String, Object> map
	 * @return CyEventSign
	 * 
	 */
	@Override
	public List<CyEventSign> getEventSignList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<CyEventSign> list = mobEventMapper.getEventSignList(map);
		
		for(CyEventSign cyEventSigns : list)
		{
			if(cyEventSigns != null)
			{
				String userPic = cyEventSigns.getUserAvatar();
				
				if(WebUtil.isNumeric(userPic))
				{
					cyEventSigns.setUserAvatar(WebUtil.getIcon(Integer.parseInt(userPic)));
				}
			}
			
			
		}
		
		return list;
	}
	
	/**
	 * 通过指定活动签到报名对象中的参数得到活动签到报名对象的列表（手机列表专用）
	 * 
	 * @param CyEventSign cyEventSign
	 * @return CyEventSign
	 * 
	 */
	@Override
	public List<CyEventSign> pullEventSignList(CyEventSign cyEventSign) {
		// TODO Auto-generated method stub
		List<CyEventSign> list = mobEventMapper.pullEventSignList(cyEventSign);
		
		for(CyEventSign cyEventSigns : list)
		{
			if(cyEventSigns != null)
			{
				String userPic = cyEventSigns.getUserAvatar();
				
				if(WebUtil.isNumeric(userPic))
				{
					cyEventSigns.setUserAvatar(WebUtil.getIcon(Integer.parseInt(userPic)));
				}
			}
			
			
		}
		
		return list;

	}


	/**
	 * 通过指定活动签到报名对象中的参数得到相应的统计数量
	 * 
	 * @param CyEventSign eventSign
	 * @return long
	 * 
	 */
	@Override
	public long countEventSign(CyEventSign eventSign) {
		// TODO Auto-generated method stub
		return mobEventMapper.countEventSign(eventSign);
	}

	/**
	 * 验证签到码
	 * 
	 * @param CyEvent event
	 * @return CyEvent
	 * 
	 */
	@Override
	public long verifyEventSignInCode(CyEvent event) {
		// TODO Auto-generated method stub
		return mobEventMapper.verifyEventSignInCode(event);
	}

	/**
	 * 更新报名签到表
	 * 
	 * @param CyEventSign eventSign
	 * 
	 */
	@Override
	public void updateEventSign(CyEventSign eventSign) {
		// TODO Auto-generated method stub
		mobEventMapper.updateEventSign(eventSign);
	}

	/**
	 * 插入报名签到表
	 * 
	 * @param CyEventSign eventSign
	 * 
	 */
	@Override
	public void insertEventSign(CyEventSign eventSign) {
		// TODO Auto-generated method stub
		mobEventMapper.insertEventSign(eventSign);
	}

	
	/**
	 * 插入活动表
	 * 
	 * @param CyEvent cyEevent
	 * 
	 */
	@Override
	public void saveMobEvent(CyEvent cyEevent) {
		// TODO Auto-generated method stub
		mobEventMapper.saveMobEvent(cyEevent);
	}

	
	/**
	 * 查询活动列表
	 * 
	 * @param CyEvent cyEevent
	 * 
	 */
	@Override
	public List<CyEvent> listMobEvens(CyEvent cyEevent) {
		// TODO Auto-generated method stub
		return mobEventMapper.listMobEvens(cyEevent);
	}


	/**
	 * 查询活动类型列表
	 * 
	 * @param String dictTypeName
	 * 
	 */
	public List<Dict> getDicts(String dictTypeName){
		return mobEventMapper.getDicts(dictTypeName);
	}
	
	/**
	 * 查询我的所有活动通知数量
	 * 
	 * @param String userInfoId
	 * 
	 */
	public CyEvent getNumOfNotifyForMyEvents(String userInfoId){
		return mobEventMapper.getNumOfNotifyForMyEvents(userInfoId);
	}


	/**
	 * 插入花絮表
	 * 
	 * @param CyEventBoard eventBoard
	 * 
	 */
	@Override
	public void saveMobEventBoard(CyEventBoard eventBoard) {
		mobEventMapper.saveMobEventBoard(eventBoard);
		
	}

	
	/**
	 * 插入花絮图片表
	 * 
	 * @param CyEventBoardPic eventBoardPic
	 * 
	 */
	@Override
	public void saveMobEventBoardPic(CyEventBoardPic eventBoardPic) {
		mobEventMapper.saveMobEventBoardPic(eventBoardPic);
		
	}
	
	

	/**
	 * 通过指定活动花絮对象中的参数得到活动花絮对象的列表
	 * 
	 * @param CyEventBoard eventBoard
	 * @return List<CyEventBoard>
	 * 
	 */
	@Override
	public List<CyEventBoard> getEventBoardList(CyEventBoard eventBoard) {
		// TODO Auto-generated method stub
		
		String userInfoId = eventBoard.getUserInfoId();
		eventBoard.setUserInfoId(null);
		
		List<CyEventBoard> list = mobEventMapper.getEventBoardList(eventBoard);
		
		for(CyEventBoard cyEventBoard : list)
		{
			if(cyEventBoard != null)
			{
				CyEventBoardPraise eventBoardPraise = new CyEventBoardPraise();
				eventBoardPraise.setBoardId(cyEventBoard.getId());
				cyEventBoard.setPraiseNum(mobEventMapper.countEventBoardPraise(eventBoardPraise));
				
				CyEventBoardPraise countEventBoardPraise = new CyEventBoardPraise();
				countEventBoardPraise.setBoardId(cyEventBoard.getId());
				countEventBoardPraise.setUserInfoId(userInfoId);
				long pariseNum = mobEventMapper.countEventBoardPraise(countEventBoardPraise);
				
				if(pariseNum > 0)
				{
					cyEventBoard.setParise(true);
				}
				else
				{
					cyEventBoard.setParise(false);
				}
				
				String userPic = cyEventBoard.getUserAvatar();
				
				long boardId = cyEventBoard.getId();
				CyEventBoardPic cyEventBoardPic = new CyEventBoardPic();cyEventBoardPic.setBoardId(boardId);
				
				cyEventBoard.setCyEventBoardPicList(getEventBoardPicList(cyEventBoardPic));
				cyEventBoard.setCreateTimeStr(WebUtil.pastTime(cyEventBoard.getCreateTime()));
				
				if(WebUtil.isNumeric(userPic))
				{
					cyEventBoard.setUserAvatar(WebUtil.getIcon(Integer.parseInt(userPic)));
				}
			}
			
			
		}
		
		
		return list;
	}
	
	
	/**
	 * 通过指定活动花絮对象中的参数得到活动花絮总数
	 * 
	 * @param CyEventBoard eventBoard
	 * @return long
	 * 
	 */
	@Override
	public long countEventBoard(CyEventBoard eventBoard)
	{
		//String userInfoId = eventBoard.getUserInfoId();
		eventBoard.setUserInfoId(null);
		return mobEventMapper.countEventBoard(eventBoard);
	}

	/**
	 * 通过指定活动花絮对象中的参数得到活动花絮的对象
	 * 
	 * @param CyEventBoard eventBoard
	 * @return CyEventBoard
	 * 
	 */
	@Override
	public CyEventBoard getEventBoard(CyEventBoard eventBoard) {
		// TODO Auto-generated method stub
		CyEventBoard cyEventBoard  = mobEventMapper.getEventBoard(eventBoard);
		if(cyEventBoard != null)
		{
			CyEventBoardPraise eventBoardPraise = new CyEventBoardPraise();
			eventBoardPraise.setBoardId(cyEventBoard.getId());
			cyEventBoard.setPraiseNum(mobEventMapper.countEventBoardPraise(eventBoardPraise));
			
			String userPic = cyEventBoard.getUserAvatar();
			
			cyEventBoard.setCreateTimeStr(WebUtil.pastTime(cyEventBoard.getCreateTime()));
			
			if(WebUtil.isNumeric(userPic))
			{
				cyEventBoard.setUserAvatar(WebUtil.getIcon(Integer.parseInt(userPic)));
			}
		}
		
		
		return cyEventBoard;
	}

	/**
	 * 通过指定活动花絮图片对象中的参数得到活动花絮图片对象的列表
	 * 
	 * @param CyEventBoardPic eventBoardPic
	 * @return List<CyEventBoardPic>
	 * 
	 */
	@Override
	public List<CyEventBoardPic> getEventBoardPicList(
			CyEventBoardPic eventBoardPic) {
		// TODO Auto-generated method stub
		
		List<CyEventBoardPic> eventBoardPicList = mobEventMapper.getEventBoardPicList(eventBoardPic);
		
		for(CyEventBoardPic eventBoardPics : eventBoardPicList)
		{
			String pic = eventBoardPics.getPic();
			
			if(!WebUtil.isEmpty(pic))
			{
				pic = pic.replaceAll("_MIN", "").replaceFirst("_MAX", "");
				
				String thumbnail = pic;
				
				String xemanhdep = pic;
				
				thumbnail = WebUtil.getPictureByType(pic, "MIN");
				xemanhdep = WebUtil.getPictureByType(pic, "MAX");
				
				eventBoardPics.setPic(pic);
				eventBoardPics.setThumbnail(thumbnail);
				eventBoardPics.setXemanhdep(xemanhdep);
			}
			
		}
		
		return eventBoardPicList;
	}
	
	
	/**
	 * 得到每个用户发布的活动花絮中的最新的一张的活动花絮图片对象的列表
	 * 
	 * @param CyEventBoard eventBoard
	 * @return List<CyEventBoardPic>
	 * 
	 */
	@Override
	public List<CyEventBoardPic> getEventBoardPicListForEventDetail(CyEventBoard eventBoard){
		// TODO Auto-generated method stub
		List<CyEventBoardPic> eventBoardPicList = mobEventMapper.getEventBoardPicListForEventDetail(eventBoard);
		
		for(CyEventBoardPic eventBoardPic : eventBoardPicList)
		{
			String pic = eventBoardPic.getPic();
			
			if(!WebUtil.isEmpty(pic))
			{
				pic = pic.replaceAll("_MIN", "").replaceFirst("_MAX", "");
				
				String thumbnail = pic;
				
				String xemanhdep = pic;
				
				thumbnail = WebUtil.getPictureByType(pic, "MIN");
				xemanhdep = WebUtil.getPictureByType(pic, "MAX");
				
				eventBoardPic.setPic(pic);
				eventBoardPic.setThumbnail(thumbnail);
				eventBoardPic.setXemanhdep(xemanhdep);
			}
			
		}
		
		return eventBoardPicList;
	}

	/**
	 * 通过指定活动花絮评论对象中的参数得到活动花絮评论对象的列表
	 * 
	 * @param CyEventBoardComment eventBoardComment
	 * @return List<CyEventBoardComment>
	 * 
	 */
	@Override
	public List<CyEventBoardComment> getEventBoardCommentList(
			CyEventBoardComment eventBoardComment) {
		// TODO Auto-generated method stub
		List<CyEventBoardComment> list = mobEventMapper.getEventBoardCommentList(eventBoardComment);
		
		for(CyEventBoardComment cyEventBoardComment : list)
		{
			if(cyEventBoardComment != null)
			{
				String userPic = cyEventBoardComment.getUserAvatar();
				
				cyEventBoardComment.setCreateTimeStr(WebUtil.pastTime(cyEventBoardComment.getCreateTime()));
				
				if(WebUtil.isNumeric(userPic))
				{
					cyEventBoardComment.setUserAvatar(WebUtil.getIcon(Integer.parseInt(userPic)));
				}
			}
			
			
		}
		
		
		return list;
	}
	
	
	/**
	 * 通过指定活动花絮评论对象中的参数得到活动花絮评论对象的列表(带楼主)
	 * 
	 * @param CyEventBoardComment eventBoardComment
	 * @return List<CyEventBoardComment>
	 * 
	 */
	@Override
	public Map<String, Object> getEventBoardCommentListWithEventBoard(CyEventBoard eventBoard, CyEventBoardComment eventBoardComment) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String userInfoId = eventBoard.getUserInfoId();
		eventBoard.setUserInfoId(null);

		CyEventBoard cyEventBoard = mobEventMapper.getEventBoard(eventBoard);
		
		if(cyEventBoard != null)
		{
			CyEventBoardPraise eventBoardPraise = new CyEventBoardPraise();
			eventBoardPraise.setBoardId(cyEventBoard.getId());
			cyEventBoard.setPraiseNum(mobEventMapper.countEventBoardPraise(eventBoardPraise));
			
			CyEventBoardPraise countEventBoardPraise = new CyEventBoardPraise();
			countEventBoardPraise.setBoardId(cyEventBoard.getId());
			countEventBoardPraise.setUserInfoId(userInfoId);
			long pariseNum = mobEventMapper.countEventBoardPraise(countEventBoardPraise);
			
			if(pariseNum > 0)
			{
				cyEventBoard.setParise(true);
			}
			else
			{
				cyEventBoard.setParise(false);
			}
			
			if(cyEventBoard != null)
			{
				String userPic = cyEventBoard.getUserAvatar();
				
				long boardId = cyEventBoard.getId();
				CyEventBoardPic cyEventBoardPic = new CyEventBoardPic();cyEventBoardPic.setBoardId(boardId);
				
				cyEventBoard.setCyEventBoardPicList(getEventBoardPicList(cyEventBoardPic));
				cyEventBoard.setCreateTimeStr(WebUtil.pastTime(cyEventBoard.getCreateTime()));
				
				if(WebUtil.isNumeric(userPic))
				{
					cyEventBoard.setUserAvatar(WebUtil.getIcon(Integer.parseInt(userPic)));
				}
			}
			
		}
		
		List<CyEventBoardComment> list = mobEventMapper.getEventBoardCommentList(eventBoardComment);
		
		for(CyEventBoardComment cyEventBoardComment : list)
		{
			if(cyEventBoardComment != null)
			{
				String userPic = cyEventBoardComment.getUserAvatar();
				
				cyEventBoardComment.setCreateTimeStr(WebUtil.pastTime(cyEventBoardComment.getCreateTime()));
				
				if(WebUtil.isNumeric(userPic))
				{
					cyEventBoardComment.setUserAvatar(WebUtil.getIcon(Integer.parseInt(userPic)));
				}
			}
		}
		
		map.put("eventBoard", cyEventBoard);
		map.put("commentList", list);
		
		
		return map;
	}

	/**
	 * 通过指定活动花絮举报对象中的参数得到活动花絮举报数
	 * 
	 * @param CyEventBoardComplaint eventBoardComplaint
	 * @return long
	 * 
	 */
	@Override
	public long countEventBoardComplaint(
			CyEventBoardComplaint eventBoardComplaint) {
		// TODO Auto-generated method stub
		return mobEventMapper.countEventBoardComplaint(eventBoardComplaint);
	}
	
	/**
	 * 通过指定活动花絮点赞对象中的参数得到活动花絮点赞数
	 * 
	 * @param CyEventBoardPraise eventBoardPraise
	 * @return long
	 * 
	 */
	@Override
	public long countEventBoardPraise(
			CyEventBoardPraise eventBoardPraise) {
		// TODO Auto-generated method stub
		return mobEventMapper.countEventBoardPraise(eventBoardPraise);
	}
	
	
	/**
	 * 通过指定活动花絮评论对象中的参数得到活动花絮评论数
	 * 
	 * @param CyEventBoardComment eventBoardComment
	 * @return long
	 * 
	 */
	@Override
	public long countEventBoardComment(
			CyEventBoardComment eventBoardComment) {
		// TODO Auto-generated method stub
		return mobEventMapper.countEventBoardComment(eventBoardComment);
	}
	

	/**
	 * 插入活动花絮举报对象中的参数到活动花絮举报表
	 * 
	 * @param CyEventBoardComplaint eventBoardComplaint
	 * 
	 */
	@Override
	public void insertEventBoardComplaint(
			CyEventBoardComplaint eventBoardComplaint) {
		// TODO Auto-generated method stub
		mobEventMapper.insertEventBoardComplaint(eventBoardComplaint);
	}


	/**
	 * 插入活动花絮点赞对象中的参数到活动花絮点赞表
	 * 
	 * @param CyEventBoardPraise eventBoardPraise
	 * 
	 */
	public void insertEventBoardPraise(CyEventBoardPraise eventBoardPraise) {
		// TODO Auto-generated method stub
		mobEventMapper.insertEventBoardPraise(eventBoardPraise);
	}


	/**
	 * 通过活动花絮点赞对象中的参数删除活动花絮点赞
	 * 
	 * @param CyEventBoardPraise eventBoardPraise
	 * 
	 */
	public void deleteEventBoardPraise(CyEventBoardPraise eventBoardPraise) {
		// TODO Auto-generated method stub
		mobEventMapper.deleteEventBoardPraise(eventBoardPraise);
	}


	/**
	 * 通过活动花絮点赞对象中的参数删除活动花絮点赞
	 * 
	 * @param CyEventBoardPraise eventBoardPraise
	 * 
	 */
	@Override
	public void eventBoardPraise(CyEventBoardPraise eventBoardPraise) {
		// TODO Auto-generated method stub
		
		long praiseNum = mobEventMapper.countEventBoardPraise(eventBoardPraise);
		
		if(praiseNum > 0)
		{
			mobEventMapper.deleteEventBoardPraise(eventBoardPraise);
		}
		else
		{
			mobEventMapper.insertEventBoardPraise(eventBoardPraise);
		}
		
	}


	/**
	 * 插入活动花絮评论对象中的参数到活动花絮评论表
	 * 
	 * @param CyEventBoardComment eventBoardComment
	 * 
	 */
	@Override
	public void insertEventBoardComment(CyEventBoardComment eventBoardComment) {
		// TODO Auto-generated method stub
		mobEventMapper.insertEventBoardComment(eventBoardComment);
	}


	/**
	 * 通过活动花絮评论对象中的参数删除活动花絮评论
	 * 
	 * @param CyEventBoardComment eventBoardComment
	 * 
	 */
	@Override
	public void deleteEventBoardComment(CyEventBoardComment eventBoardComment) {
		// TODO Auto-generated method stub
		mobEventMapper.deleteEventBoardComment(eventBoardComment);
	}


	/**
	 * 通过活动花絮对象中的参数删除活动花絮
	 * 
	 * @param CyEventBoard eventBoard
	 * 
	 */
	@Override
	public void deleteEventBoard(CyEventBoard eventBoard) {
		// TODO Auto-generated method stub
		mobEventMapper.deleteEventBoard(eventBoard);
	}


	@Override
	public long listMobEvensNum(CyEvent cyEevent) {
		// TODO Auto-generated method stub
		return mobEventMapper.listMobEvensNum(cyEevent);
	}


	/**
	 * 通过活动对象中的参数删除活动
	 * 
	 * @param CyEvent event
	 * 
	 */
	@Override
	public void deleteEvent(CyEvent event) {
		// TODO Auto-generated method stub
		mobEventMapper.deleteEvent(event);
	}


	@Override
	public String getRegionFromAlumniByAccountNum(CyEvent cyEvent) {
		
		String returnStr = "";
		String pointStr = " ";
    	
		CyEvent tmpEvent = mobEventMapper.getRegionFromAlumniByAccountNum(cyEvent);
		
		if(tmpEvent != null){
			
			if(tmpEvent.getRegion()!=null && !tmpEvent.getRegion().equals("")){
				
				if(tmpEvent.getRegion().contains(pointStr)){
					returnStr = tmpEvent.getRegion().substring(tmpEvent.getRegion().lastIndexOf(pointStr)+1, tmpEvent.getRegion().length());
				}else{
					returnStr = tmpEvent.getRegion();
				}
				 
				
			}else if(tmpEvent.getPlace()!=null && !tmpEvent.getPlace().equals("")){
				
				if(tmpEvent.getPlace().contains(pointStr)){
					returnStr = tmpEvent.getPlace().substring(tmpEvent.getPlace().lastIndexOf(pointStr)+1, tmpEvent.getPlace().length());
				}else{
					returnStr = tmpEvent.getPlace();
				}
				
			}
		}
		
    	return returnStr;
		
	}
	
	

}
