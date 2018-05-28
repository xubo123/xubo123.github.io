package com.hxy.core.community.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 群组主题
 */
public class GroupTopic implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;  			//自增长id
	private long board_id;		//版块ID/主题ID... 根据topic_type代表不同的含义
	private long topic_type;	//主题类别
								//0：版块主题， board_id = group_borad.id ;  
								//1：活动主题， board_id = 0 / group_borad.id －>  公共活动／社群活动
								//2：主题花絮， board_id = group_topic.id（对应的主题）
	private long first_post_id;	//主题内容ID（楼主） -->  topic_context.id
	private String topic_title;	//主题标题
	private String user_ip;		//用户IP地址
	private String last_reply_date;//最后回复时间
	private int toplevel;		//置顶级别分为3级；为数字 1，2，3

	public Date created_date;
	public Date modified_date;
	public long created_by;
	
	private TopicContext first_post; 	//主题内容 
	private String appUserName;			//  --> created_by
	
	private int commentNum;			//评论数量
	private int praiseNum;			//点赞数量
	private String board_name;		//版块名

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getBoard_id() {
		return board_id;
	}
	public void setBoard_id(long board_id) {
		this.board_id = board_id;
	}
	public long getTopic_type() {
		return topic_type;
	}
	public void setTopic_type(long topic_type) {
		this.topic_type = topic_type;
	}
	public long getFirst_post_id() {
		return first_post_id;
	}
	public void setFirst_post_id(long first_post_id) {
		this.first_post_id = first_post_id;
	}
	public String getTopic_title() {
		return topic_title;
	}
	public void setTopic_title(String topic_title) {
		this.topic_title = topic_title;
	}
	public String getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}
	public String getLast_reply_date() {
		return last_reply_date;
	}
	public void setLast_reply_date(String last_reply_date) {
		this.last_reply_date = last_reply_date;
	}
	public int getToplevel() {
		return toplevel;
	}
	public void setToplevel(int toplevel) {
		this.toplevel = toplevel;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public Date getModified_date() {
		return modified_date;
	}
	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}
	public long getCreated_by() {
		return created_by;
	}
	public void setCreated_by(long created_by) {
		this.created_by = created_by;
	}
	

	public TopicContext getFirst_post() {
		return first_post;
	}
	public void setFirst_post(TopicContext first_post) {
		this.first_post = first_post;
	}
	public String getAppUserName() {
		return appUserName;
	}
	public void setAppUserName(String appUserName) {
		this.appUserName = appUserName;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public int getPraiseNum() {
		return praiseNum;
	}
	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}
	public String getBoard_name() {
		return board_name;
	}
	public void setBoard_name(String board_name) {
		this.board_name = board_name;
	}

}
