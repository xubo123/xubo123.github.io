package com.hxy.core.news.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class News implements Serializable{

	private static final long serialVersionUID = 1L;

	private long newsId;
	private String title;
	private String content;
	private String pic;
	private String introduction;
	private Date createTime;
	private String type;
	private String channelName;
	private String newsUrl;//新闻网址
	private String [] types;
	private String typeStr;
	private long category;
	private long pCategory;
	private String categoryName;
	private String pCategoryName;//父栏目名
	private String topnews;
	private long currentRow = 0;//当前行数
	private int incremental = 10;//每次拉取数据的增量
	private String fDateTime;
	
	private long categoryWeb;
	private long pCategoryWeb;
	private String categoryWebName;
	private String pCategoryWebName;//父栏目名
	private String topnewsWeb;
	
	private String cityName;	//新增所在地
	
	/**--1.总会新闻，2.地方新闻--**/
	private int origin;
	private int originP;
	private int originWeb;
	private int originWebP;
	
	private String json_news_url;
	
	private String dept_id;		//所属院系
	private String dept_name;	//所属院系名称

	@Override
	public String toString() {
		return "News [newsId=" + newsId + ", title=" + title + ", content="
				+ content + ", pic=" + pic + ", introduction=" + introduction
				+ ", createTime=" + createTime + ", type=" + type
				+ ", channelName=" + channelName + ", newsUrl=" + newsUrl
				+ ", types=" + Arrays.toString(types) + ", typeStr=" + typeStr
				+ ", category=" + category + ", categoryName=" + categoryName
				+ ", topnews=" + topnews + ", currentRow=" + currentRow
				+ ", pCategory=" + pCategory 
				+ ", incremental=" + incremental + "]";
	}

	
	
	
	public String getfDateTime() {
		return fDateTime;
	}

	public void setfDateTime(String fDateTime) {
		this.fDateTime = fDateTime;
	}

	public int getIncremental() {
		return incremental;
	}

	public void setIncremental(int incremental) {
		this.incremental = incremental;
	}

	public long getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(long currentRow) {
		this.currentRow = currentRow;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTopnews() {
		return topnews;
	}

	public void setTopnews(String topnews) {
		this.topnews = topnews;
	}

	public long getCategory() {
		return category;
	}

	public void setCategory(long category) {
		this.category = category;
	}

	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public long getNewsId()
	{
		return newsId;
	}

	public void setNewsId(long newsId)
	{
		this.newsId = newsId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getPic()
	{
		return pic;
	}

	public void setPic(String pic)
	{
		this.pic = pic;
	}

	public String getIntroduction()
	{
		return introduction;
	}

	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
	public String[] getTypes() {
		return types;
	}
	public void setTypes(String[] types) {
		this.types = types;
	}
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getPCategoryName() {
		return pCategoryName;
	}
	public void setPCategoryName(String categoryName) {
		pCategoryName = categoryName;
	}
	public long getPCategory() {
		return pCategory;
	}
	public void setPCategory(long category) {
		pCategory = category;
	}
	public String getFDateTime() {
		return fDateTime;
	}
	public void setFDateTime(String dateTime) {
		fDateTime = dateTime;
	}
	public int getOrigin() {
		return origin;
	}
	public void setOrigin(int origin) {
		this.origin = origin;
	}


	public void setCategoryWeb(long categoryWeb) {
		this.categoryWeb = categoryWeb;
	}

	public long getCategoryWeb() {
		return categoryWeb;
	}

	public void setPCategoryWeb(long pCategoryWeb) {
		this.pCategoryWeb = pCategoryWeb;
	}

	public long getPCategoryWeb() {
		return pCategoryWeb;
	}

	public void setCategoryWebName(String categoryWebName) {
		this.categoryWebName = categoryWebName;
	}

	public String getCategoryWebName() {
		return categoryWebName;
	}

	public void setPCategoryWebName(String pCategoryWebName) {
		this.pCategoryWebName = pCategoryWebName;
	}

	public String getPCategoryWebName() {
		return pCategoryWebName;
	}

	public void setTopnewsWeb(String topnewsWeb) {
		this.topnewsWeb = topnewsWeb;
	}

	public String getTopnewsWeb() {
		return topnewsWeb;
	}

	public void setOriginWeb(int originWeb) {
		this.originWeb = originWeb;
	}

	public int getOriginWeb() {
		return originWeb;
	}

	public void setOriginP(int originP) {
		this.originP = originP;
	}
	
	public int getOriginP() {
		return originP;
	}

	public void setOriginWebP(int originWebP) {
		this.originWebP = originWebP;
	}

	public int getOriginWebP() {
		return originWebP;
	}

	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}
	public String getDept_id() {
		return dept_id;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getDept_name() {
		return dept_name;
	}

}
