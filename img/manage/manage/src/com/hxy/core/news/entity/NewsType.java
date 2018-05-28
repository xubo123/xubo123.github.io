package com.hxy.core.news.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 新闻类别表
 * @author Administrator
 *
 */

public class NewsType implements Serializable {


	private static final long serialVersionUID = 1L;
	/**--主键--**/
	private long id;
	/**--父id--**/
	private long parent_id;
	/**--类型名称--**/
	private String name;
	/**--(1.新闻类别,2.链接，3.单页面)--**/
	private int type;
	/**--新闻的来源,1.总会新闻，2校友会新闻--**/
	private int origin;
	/**--url--**/
	private String url;
	/**----**/
	private int orderNum;
	/**--所在地,市--**/
	private String cityName;
	/**--是否上首页--**/
	private int isMain;
	/**--是否上导航--**/
	private int isNavigation;
	/**--父栏目名称--**/
	private String parent_name;
	/**--如果该栏目为父栏目，子栏目集合--**/
	private List<NewsType> leveList;
	/**--该栏目下所有新闻集合--**/
	private List<News> newsList;
	/**--取相应的新闻web用到--**/
	private String json_news_url;
	
	/** 单页面栏目的新闻标题 **/
	private String newsTitle;
	/** 单页面栏目的新闻内容 **/
	private String newsContent;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParent_id() {
		return parent_id;
	}
	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityName() {
		return cityName;
	}
	public String getParent_name() {
		return parent_name;
	}
	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}
	public List<NewsType> getLeveList() {
		return leveList;
	}
	public void setLeveList(List<NewsType> leveList) {
		this.leveList = leveList;
	}
	public List<News> getNewsList() {
		return newsList;
	}
	public void setNewsList(List<News> newsList) {
		this.newsList = newsList;
	}
	public String getJson_news_url() {
		return json_news_url;
	}
	public void setJson_news_url(String json_news_url) {
		this.json_news_url = json_news_url;
	}
	public int getOrigin() {
		return origin;
	}
	public void setOrigin(int origin) {
		this.origin = origin;
	}
	public void setIsMain(int isMain) {
		this.isMain = isMain;
	}
	public int getIsMain() {
		return isMain;
	}
	public void setIsNavigation(int isNavigation) {
		this.isNavigation = isNavigation;
	}
	public int getIsNavigation() {
		return isNavigation;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}
	public String getNewsContent() {
		return newsContent;
	}
	
	
}
