package com.hxy.core.news.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.DataGrid;
import com.hxy.base.entity.Message;
import com.hxy.core.channel.entity.NewsTag;
import com.hxy.core.channel.service.NewsChannelService;
import com.hxy.core.dict.entity.Dict;
import com.hxy.core.news.entity.News;
import com.hxy.core.news.entity.News2;
import com.hxy.core.news.entity.NewsType;
import com.hxy.core.news.service.NewsService;
import com.hxy.util.WebUtil;
import com.hxy.util.jms.PushedMessage;
import com.hxy.util.jms.SingleNewsMessage;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;

import net.sf.json.JSONObject;

@Namespace("/mobile/news")
@Action(value = "newsAction", results = {
		@Result(name = "viewNews", location = "/page/admin/news/viewNews.jsp"),
		@Result(name = "viewNewsx", location = "/page/alumni/news/view.jsp"),
		@Result(name = "initNewsUpdate", location = "/page/admin/news/editNews.jsp"),
		@Result(name = "initNewsUpdatex", location = "/page/alumni/news/edit.jsp"),
		@Result(name = "listMobNews", location = "/mobile/news/list.jsp"),
		@Result(name = "getMobNew", location = "/mobile/news/show.jsp") })
public class NewsAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(NewsAction.class);

	@Autowired
	private NewsService newsService;

	@Autowired
	private NewsChannelService newsChannelService;

	private News news;

	private News2 news2;

	// @Autowired
	// private MicroService microService;

	private String isRmv;
	private int pageType;

	private String category;

	/** --新闻所在地-- **/
	private String cityName;

	// private String country;
	private String province;
	private String city;
	private String area;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIsRmv() {
		return isRmv;
	}

	public void setIsRmv(String isRmv) {
		this.isRmv = isRmv;
	}

	public void setPageType(int pageType) {
		this.pageType = pageType;
	}

	public int getPageType() {
		return pageType;
	}

	public void dataGrid() {
		dataGridNews();
	}

	public void dataGridx() {
		dataGridNewsx();
	}

	private void dataGridNews() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		map.put("page", page);
		if (news != null) {
			map.put("title", news.getTitle());
		}
		if (getUser().getRole().getSystemAdmin() != 1) {
			map.put("deptList", getUser().getDepts());
		}
		DataGrid<News2> data = newsService.dataGrid2(map);
		super.writeJson(data);
	}
	/**
	 * 校友会新闻展示
	 */
	private void dataGridNewsx() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		map.put("page", page);
		map.put("page", page);
		if (news != null) {
			map.put("title", news.getTitle());
		}
		if (getUser().getRole().getSystemAdmin() != 1) {
			map.put("deptList", getUser().getDepts());
		}
		DataGrid<News2> data = newsService.dataGridForAlumni(map);
		super.writeJson(data);
	}

	public void savex() {
		saveNews();
	}

	public void save() {
		saveNews();
	}

	private void saveNews() {
		// 手机新闻1级栏目类型
		long type1 = WebUtil.toLong(this.getRequest().getParameter("type1"));
		// 手机新闻2级栏目类型
		long type2 = WebUtil.toLong(this.getRequest().getParameter("type2"));
		if (type1 > 0 && type2 == 0) {
			news2.setCategory(type1);
		} else if (type1 > 0 && type2 > 0) {
			news2.setCategory(type2);
		}

		Message message = new Message();
		try {
			// if (getUser().getFlag() == 1) {
			// news.setCityName(getUser().getAlumni().getRegion());
			// }
			// news.setCreateTime(new Date());
			// String newsUrl = news2.getNewsUrl();
			news2.setNewsType(0);
			news2.setModifyTime(new Date());
			news2.setCreator(getUser().getUserId());
			if (WebUtil.isEmpty(news2.getNewsUrl())) {
				// 自动生成URL
				HttpServletRequest request = this.getRequest();
				String path = request.getContextPath();
				String basePath = request.getScheme() + "://"
						+ request.getServerName() + ":"
						+ request.getServerPort() + path + "/";
				String url = basePath
						+ "mobile/news/newsAction!doNotNeedSessionAndSecurity_getMobNew.action?id=";
				newsService.insertNewsAndsetUrl(news2, url);
			} else {
				newsService.save2(news2);
			}
			message.setMsg("新增成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("新增失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void update() {
		updateNews();
	}

	public void updatex() {
		updateNews();
	}

	private void updateNews() {

		// 手机新闻1级栏目类型
		long type1 = WebUtil.toLong(this.getRequest().getParameter("type1"));
		/*
		 * // 手机新闻2级栏目类型 long type2 =
		 * WebUtil.toLong(this.getRequest().getParameter("type2"));
		 */

		/*
		 * if (type1 > 0 && type2 == 0) { news2.setCategory(type1); } else if
		 * (type1 > 0 && type2 > 0) { news2.setCategory(type2); }
		 */
		news2.setCategory(type1);
		news2.setModifyTime(new Date());
		Message message = new Message();
		try {
			newsService.update2(news2);
			message.setMsg("修改成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("修改失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void deletex() {
		deleteNews();
	}

	public void delete() {
		deleteNews();
	}

	private void deleteNews() {
		Message message = new Message();
		try {
			newsService.delete(ids);
			message.setMsg("删除成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	/*
	 * 批量发送
	 */
	public void sendList() {
		sendNews();
	}

	public void sendListx() {
		sendNews();
	}

	private void sendNews() {
		Message message = new Message();
		try {
			String accessToken = "";
			List<Long> list = new ArrayList<Long>();
			if (ids != null && !"".equals(ids)) {
				String[] array = ids.split(",");
				for (String id : array) {
					list.add(Long.parseLong(id));
				}
			}
			int result = 0;
			result = sendList(list, accessToken);
			if (result > 0) {
				message.setMsg("批量发送成功");
				message.setSuccess(true);
			} else if (result == -1) {
				message.setMsg("批量发送失败,批量发送超过每月限制，每月只能群发4批，每批不超过10条消息!");
				message.setSuccess(false);
			} else {
				message.setMsg("批量发送失败");
				message.setSuccess(false);
			}
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("批量发送失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	/** --新闻推送-- **/
	public int sendList(List<Long> list, String accessToken) {
		Map<String, PushedMessage> map = new HashMap<String, PushedMessage>();
		Iterator<Long> iterator = list.iterator();
		while (iterator.hasNext()) {
			Long id = iterator.next();
			News2 news = newsService.selectById2(id);

			// 新闻推送这里需要大图
			// 图片URL
			String pic = news.getPic();
			if (!WebUtil.isEmpty(pic)) {
				// 图片后缀
				String fileExt = pic.substring(pic.lastIndexOf("."));
				String temp = pic.substring(0, pic.lastIndexOf("."));
//				if ("100".equals(news.getTopnews())) {
					// 该新闻为滚动新闻，需要调用大图
					news.setPic(temp + "_MAX" + fileExt);
//				} else if (temp.indexOf("_MIN") == -1) {
//					news.setPic(temp + "_MIN" + fileExt);
//				}
			}
			addToPushedMessageMap2(map, news);
		}
		for (Map.Entry<String, PushedMessage> entry : map.entrySet()) {
			sendPushedMessage(entry.getValue());
		}

		return 1;
	}

	public void sendPushedMessage(PushedMessage pushedMessage) {
		List<String> tagList = new ArrayList<String>();
		if (pushedMessage.getTagName() != null) {
			String tag = newsChannelService.selectTagbytagId(pushedMessage
					.getTagId());
			tagList.add(tag);
		}
		newsService.sendIosMessage(tagList, pushedMessage.getNewsList(),null);
		newsService.sendAndroidMessage(tagList, pushedMessage.getNewsList(),null);
	}


	/**
	 * 推送使用--徐波6.26修改---
	 * 
	 * @param map
	 * @param news
	 */
	public void addToPushedMessageMap2(Map<String, PushedMessage> map,
			News2 news) {
		SingleNewsMessage smsg = new SingleNewsMessage();
		String tagName = news.getTagName();
		long tagId = news.getTagId();

		smsg.setTagName(tagName);
		smsg.setChannelId(tagId);
		// 设置图片url
		String URL3 = news.getPic() == null ? "" : news.getPic();
		String strURL3 = "";
		try {
			strURL3 = URLEncoder.encode(URL3, "utf-8").replace("*", "*")
					.replace("~", "~").replace("+", " ");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		smsg.setIcon(strURL3);

		// 设置newsUrl
		String URL4 = news.getNewsUrl();
		String strURL4 = "";
		try {
			strURL4 = URLEncoder.encode(URL4, "utf-8").replace("*", "*")
					.replace("~", "~").replace("+", " ");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		smsg.setNewsUrl(strURL4);
		smsg.setType("0");//用于区分新闻和活动的推送
		smsg.setNid((int) news.getNewsId());
		smsg.setTime(news.getCreateTime());
		smsg.setTitle(news.getTitle());
		smsg.setSummary(news.getIntroduction());
		// 复用，新闻类型

		if (map.containsKey(tagName)) {
			map.get(tagName).getNewsList().add(smsg);
		} else {
			PushedMessage pushedMessage = new PushedMessage();
			pushedMessage.setTagName(tagName);
			pushedMessage.setTagId(tagId);

			Map<String, Object> tagMap = new HashMap<String, Object>();
			tagMap.put("channelName", tagName);
			List<NewsTag> newsTagList = newsChannelService
					.selectNewsTagList(tagMap);
			String iconURL4 = null;
			if (newsTagList != null && newsTagList.size() != 0) {
				iconURL4 = newsTagList.get(0).getTagIcon();
			} else {
				logger.error("invalid TAG name");
				return;
			}
			String strIconURL4 = "";
			if (iconURL4 != null) {
				try {
					strIconURL4 = URLEncoder.encode(iconURL4, "utf-8")
							.replace("*", "*").replace("~", "~")
							.replace("+", " ");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
			pushedMessage.setIcon(strIconURL4);

			pushedMessage.setNewsSummary(smsg.getSummary());
			pushedMessage.setTime(new Date());
			List<SingleNewsMessage> listSingleNews = new ArrayList<SingleNewsMessage>();
			listSingleNews.add(smsg);
			pushedMessage.setNewsList(listSingleNews);
			map.put(tagName, pushedMessage);
		}
	}

	/*
	 * public void addToPushedMessageMap(Map<String, PushedMessage> map, News
	 * news) { SingleNewsMessage smsg = new SingleNewsMessage(); String
	 * channelName = news.getChannelName();
	 * 
	 * smsg.setChannelName(channelName); smsg.setChannelId(channelName); //
	 * 设置图片url String URL3 = news.getPic() == null ? "" : news.getPic(); String
	 * strURL3 = ""; try { strURL3 = URLEncoder.encode(URL3,
	 * "utf-8").replace("*", "*") .replace("~", "~").replace("+", " "); } catch
	 * (UnsupportedEncodingException e1) { e1.printStackTrace(); }
	 * smsg.setIcon(strURL3);
	 * 
	 * // 设置newsUrl String URL4 = news.getNewsUrl(); String strURL4 = ""; try {
	 * strURL4 = URLEncoder.encode(URL4, "utf-8").replace("*", "*")
	 * .replace("~", "~").replace("+", " "); } catch
	 * (UnsupportedEncodingException e1) { e1.printStackTrace(); }
	 * smsg.setNewsUrl(strURL4); smsg.setNid((int) news.getNewsId());
	 * smsg.setPMId(channelName); smsg.setTime(news.getCreateTime());
	 * smsg.setTitle(news.getTitle()); smsg.setSummary(news.getIntroduction());
	 * // 复用，新闻类型 smsg.setContent(news.getType());
	 * 
	 * if (map.containsKey(channelName)) {
	 * map.get(channelName).getNewsList().add(smsg); } else { PushedMessage
	 * pushedMessage = new PushedMessage();
	 * pushedMessage.setChannelName(channelName);
	 * pushedMessage.setChannelId(channelName);
	 * 
	 * Map<String, Object> channelMap = new HashMap<String, Object>();
	 * channelMap.put("channelName", channelName); List<NewsChannel>
	 * newsChannelList = newsChannelService .selectNewsChannelList(channelMap);
	 * String iconURL4 = null; if (newsChannelList != null &&
	 * newsChannelList.size() != 0) { iconURL4 =
	 * newsChannelList.get(0).getChannelIcon(); } else {
	 * logger.error("invalid channnel name"); return; } String strIconURL4 = "";
	 * try { strIconURL4 = URLEncoder.encode(iconURL4, "utf-8") .replace("*",
	 * "*").replace("~", "~").replace("+", " "); } catch
	 * (UnsupportedEncodingException e1) { e1.printStackTrace(); }
	 * pushedMessage.setIcon(strIconURL4);
	 * 
	 * pushedMessage.setNewsSummary(smsg.getSummary());
	 * pushedMessage.setPMId(channelName); pushedMessage.setTime(new Date());
	 * List<SingleNewsMessage> listSingleNews = new
	 * ArrayList<SingleNewsMessage>(); listSingleNews.add(smsg);
	 * pushedMessage.setNewsList(listSingleNews); map.put(channelName,
	 * pushedMessage); } }
	 */

	/**
	 * 访问地址http://127.0.0.1/cy_v1/mobile/news/newsAction!
	 * doNotNeedSessionAndSecurity_getRegularNews.action
	 * 
	 * demo通过固定通道获取示例新闻
	 */
	/*
	 * public void doNotNeedSessionAndSecurity_getRegularNews() { try {
	 * Map<String, Object> map = new HashMap<String, Object>(); map.put("start",
	 * 0); map.put("rows", 5); List<String> channelNameList = new
	 * ArrayList<String>(); if (Global.REGULAR_CHANNEL_1 != null &&
	 * !Global.REGULAR_CHANNEL_1.equals("")) {
	 * channelNameList.add(Global.REGULAR_CHANNEL_1); } if
	 * (Global.REGULAR_CHANNEL_2 != null &&
	 * !Global.REGULAR_CHANNEL_2.equals("")) {
	 * channelNameList.add(Global.REGULAR_CHANNEL_2); } if
	 * (Global.REGULAR_CHANNEL_3 != null &&
	 * !Global.REGULAR_CHANNEL_3.equals("")) {
	 * channelNameList.add(Global.REGULAR_CHANNEL_3); }
	 * 
	 * if (channelNameList.size() != 0) { map.put("channelNameList",
	 * channelNameList); List<News> list = newsService.selectNews(map);
	 * 
	 * if (list.size() != 0) { Map<String, PushedMessage> pushedMeaageMap = new
	 * HashMap<String, PushedMessage>(); Iterator<News> iterator =
	 * list.iterator(); while (iterator.hasNext()) { News news =
	 * iterator.next(); addToPushedMessageMap(pushedMeaageMap, news); }
	 * 
	 * List<PushedMessage> pushedMessageList = new ArrayList<PushedMessage>();
	 * for (PushedMessage v : pushedMeaageMap.values()) { //
	 * List<SingleNewsMessage> newsList = v.getNewsList(); //
	 * this.setNewsPic(newsList); pushedMessageList.add(v); } //
	 * 设置javabean中日期转换时的格式 JsonConfig jsonConfig = new JsonConfig();
	 * jsonConfig.registerJsonValueProcessor(Date.class, new
	 * JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
	 * 
	 * JSONArray jsonobj = JSONArray.fromObject(pushedMessageList, jsonConfig);
	 * super.writeJson(jsonobj); } else { Message message = new Message();
	 * message.setMsg("频道不存在或无新闻纪录!"); message.setSuccess(false);
	 * super.writeJson(message); } }
	 * 
	 * } catch (Exception e) { Message message = new Message(); logger.error(e,
	 * e); message.setMsg("查询出错!"); message.setSuccess(false);
	 * super.writeJson(message); } }
	 */

	/** --设置新闻的封面图是大图还是小图-- **/
	private void setNewsPic(List<SingleNewsMessage> newsList) {
		boolean isBreaking = false;
		for (int i = 0; i < newsList.size(); i++) {
			SingleNewsMessage news = newsList.get(i);
			// 如果isBreaking为true则调用大图
			isBreaking = news.isBreaking();
			if (isBreaking) {
				// 调用大图
				String pic = news.getIcon();
				if (!WebUtil.isEmpty(pic)) {
					// 图片后缀
					String fileExt = pic.substring(pic.lastIndexOf("."));
					String temp = pic.substring(0, pic.lastIndexOf("."));
					if (temp.indexOf("_MAX") != -1
							|| temp.indexOf("_MIN") != -1) {
						temp = pic.substring(0, pic.lastIndexOf(".") - 4);
					}
					news.setIcon(temp + "_MAX" + fileExt);
					break;
				}
			}
		}

		// 如果isBreaking仍然为false;则将第一个图设置为大图
		if (isBreaking == false) {
			SingleNewsMessage news = newsList.get(0);
			String pic = news.getIcon();
			if (!WebUtil.isEmpty(pic)) {
				// 图片后缀
				String fileExt = pic.substring(pic.lastIndexOf("."));
				String temp = pic.substring(0, pic.lastIndexOf("."));
				if (temp.indexOf("_MAX") != -1 || temp.indexOf("_MIN") != -1) {
					temp = pic.substring(0, pic.lastIndexOf(".") - 4);
				}
				news.setIcon(temp + "_MAX" + fileExt);
			}
		}
	}

	public String doNotNeedSessionAndSecurity_showContent() {
		try {
			news = newsService.selectById(id);
		} catch (Exception e) {
			logger.error(e, e);
		}
		return "showContent";
	}

	// public void doNotNeedSecurity_initType()
	// {
	// @SuppressWarnings("unchecked")
	// Map<String, Object> map = (Map<String, Object>)
	// ServletActionContext.getServletContext().getAttribute("dictionaryInfoMap");
	// @SuppressWarnings("unchecked")
	// List<Dict> allList = (List<Dict>) map.get("dicts");
	// List<DictValue> list = new ArrayList<DictValue>();
	// for (Dict dict : allList)
	// {
	// if (dict.getDictKey().equals("信息类别") && dict.getDictValue() != null &&
	// !"".equals(dict.getDictValue()))
	// {
	// list = JSON.parseArray(dict.getDictValue(), DictValue.class);
	// break;
	// }
	// }
	// super.writeJson(list);
	// }

	public void doNotNeedSecurity_getAllCategorys() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> cateNameList = new ArrayList<String>();
		cateNameList.add("手机新闻类别");
		map.put("cateNameList", cateNameList);
		List<Dict> list = newsService.getAllCategorys(map);
		super.writeJson(list);

	}

	public void setMobTypeList() {
		setMobTypeListNews();
	}

	public void setMobTypeListx() {
		setMobTypeListNews();
	}

	private void setMobTypeListNews() {
		Message message = new Message();
		String controlStr = "";
		if (isRmv.equals("true")) {
			// 100 is for topnews of mobile
			controlStr = "100";
		} else {
			controlStr = null;
		}
		try {
			if (pageType == 1) {
				newsService.setMobTypeList(ids, controlStr);
			} else if (pageType == 2) {
				newsService.setWebTypeList(ids, controlStr);
			}

			message.setMsg("设置成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("设置失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	// public void setWebTypeList() {
	// setWebTypeListNews();
	// }
	//
	// public void setWebTypeListx() {
	// setWebTypeListNews();
	// }
	//
	// private void setWebTypeListNews() {
	// Message message = new Message();
	// String controlStr = "";
	// if (isRmv.equals("true")) {
	// // 100 is for topnews of mobile
	// controlStr = "100";
	// } else {
	// controlStr = null;
	// }
	// try {
	// newsService.setWebTypeList(ids, controlStr);
	// message.setMsg("设置成功");
	// message.setSuccess(true);
	//
	// } catch (Exception e) {
	// logger.error(e, e);
	// message.setMsg("设置失败");
	// message.setSuccess(false);
	// }
	// super.writeJson(message);
	// }

	public void doNotNeedSessionAndSecurity_listMobNews() {
		// News news = new News();
		JSONObject json = new JSONObject();
		if (news == null) {
			news = new News();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("category", news.getCategory());

		// 100 is for topnews of mobile
		map.put("topnews", "100");
		map.put("start", news.getCurrentRow());
		map.put("rows", news.getIncremental());
		map.put("cityName", cityName);
		json.put("list", newsService.listMobNews(map));

		super.writeJson(json);
	}

	public String doNotNeedSessionAndSecurity_initMobNews() {
		News news = new News();
		long locate = 0;
		try {
			locate = Long.parseLong(category);
		} catch (Exception e) {
			locate = 0;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("category", locate);
		news.setCategory(locate);
		// 100 is for topnews of mobile
		map.put("topnews", "100");
		map.put("start", news.getCurrentRow());
		map.put("rows", news.getIncremental());
		map.put("cityName", cityName);
		List<News> topnewslist = newsService.listMobTopNews(map);
		super.getRequest().setAttribute("news", news);
		super.getRequest().setAttribute("topnewslist", topnewslist);
		return "listMobNews";
	}

	public String doNotNeedSessionAndSecurity_getMobNew() {

		news2 = newsService.selectById2(id);

		if (news2.getContent() != null) {

			String tmpOneStr = "";

			StringBuffer bufConStr = new StringBuffer(news2.getContent());

			int findImg = bufConStr.indexOf("<img");

			while (findImg != -1) {

				tmpOneStr = bufConStr.substring(findImg,
						bufConStr.indexOf(">", findImg) + 1);

				bufConStr.delete(findImg, bufConStr.indexOf(">", findImg) + 1);

				tmpOneStr = tmpOneStr.replaceAll(
						"(?i)style[\\s]*=[\\s]*[\'\"][\\S]*[\\s]*[\'\"]", "");

				tmpOneStr = tmpOneStr.replaceAll(
						"(?i)width[\\s]*=[\\s]*[\'\"][\\S]*[\\s]*[\'\"]", "");

				tmpOneStr = tmpOneStr.replaceAll(
						"(?i)height[\\s]*=[\\s]*[\'\"][\\S]*[\\s]*[\'\"]", "");

				bufConStr.insert(findImg, tmpOneStr);

				findImg = bufConStr.indexOf("<img", findImg + 1);

			}

			news2.setContent(bufConStr.toString());
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("category", news2.getCategory());
		map.put("start", 0);
		map.put("rows", 3);
		map.put("butNew", news2.getNewsId());
		List<News2> newslist = newsService.listMobNews(map);
		super.getRequest().setAttribute("newslist", newslist);
		return "getMobNew";
	}

	public void doNotNeedSessionAndSecurity_getMobNewJson() {

		news = newsService.selectById(id);
		super.writeJson(news);
	}

	public void doNotNeedSessionAndSecurity_getMobNewRelatedJson() {

		news = newsService.selectById(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("category", news.getCategory());
		map.put("start", 0);
		map.put("rows", 3);
		map.put("butNew", news.getNewsId());
		List<News2> newslist = newsService.listMobNews(map);
		super.writeJson(newslist);
	}

	public void doNotNeedSessionAndSecurity_initMobNewsJson() {
		News news = new News();
		long locate = 0;
		try {
			locate = Long.parseLong(category);
		} catch (Exception e) {
			locate = 0;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("category", locate);
		news.setCategory(locate);
		// 100 is for topnews of mobile
		map.put("topnews", "100");
		map.put("start", news.getCurrentRow());
		map.put("rows", news.getIncremental());
		List<News> topnewslist = newsService.listMobTopNews(map);
		super.writeJson(topnewslist);
	}

	public void doNotNeedSessionAndSecurity_getNewsTypeByParent() {
		long parent_id = WebUtil.toLong(this.getRequest().getParameter(
				"parent_id"));
		int type = WebUtil.toInt(this.getRequest().getParameter("type"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parent_id", parent_id);
		map.put("type", type);
		// if(origin == 2) { //地方校友会只取本地的栏目
		// com.hxy.core.user.entity.User user =
		// (com.hxy.core.user.entity.User)getSession().get("user");
		// map.put("alumniId", user.getDeptId());
		// }
		List<com.hxy.core.news.entity.NewsChannel> list = this.newsService
				.selectTypeList2(map);// 选择栏目
		super.writeJson(list);
	}

	public void doNotNeedSessionAndSecurity_getWebNewsTypeByParent() {
		long parent_id = WebUtil.toLong(this.getRequest().getParameter(
				"parent_id"));
		int origin = WebUtil.toInt(this.getRequest().getParameter("origin"));
		int type = WebUtil.toInt(this.getRequest().getParameter("type"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parent_id", parent_id);
		map.put("origin", origin);
		map.put("type", type);
		// TO-FIX
		/*
		 * if(origin == 2) { //地方校友会只取本地的栏目 com.hxy.core.user.entity.User user =
		 * (com.hxy.core.user.entity.User)getSession().get("user");
		 * map.put("alumniId", user.getDeptId()); }
		 */
		List<NewsType> list = this.newsService.selectWebTypeList(map);
		super.writeJson(list);
	}

	public String doNotNeedSecurity_initNewsUpdate() {
		String convert = this.getRequest().getParameter("convert");

		news2 = newsService.selectById2(id);
		// 查询手机新闻类型的1级栏目
		Map<String, Object> map = new HashMap<String, Object>();
		// 这里只允许查询总会新闻类别
		// map.put("origin", 1);
		map.put("parent_id", 0);
		map.put("type", 1);
		List<com.hxy.core.news.entity.NewsChannel> list1 = this.newsService
				.selectTypeList2(map);
		// 子栏目集合
		map = new HashMap<String, Object>();
		long parent_id = -1;
		com.hxy.core.news.entity.NewsChannel news2channel = newsService
				.selectChannelbyId(news2.getCategory());
		// if (news2channel.getChannel_pid() == 0) {
		// if (news2.getPCategory() != 0) {
		// parent_id = news2.getPCategory();
		// }
		// } else {
		// parent_id = news2channel.getChannel_pid();
		// }

		map.put("parent_id", 0);
		List<com.hxy.core.news.entity.NewsChannel> list2 = new ArrayList<com.hxy.core.news.entity.NewsChannel>();
		if (WebUtil.isNull(convert)) {
			list2 = this.newsService.selectTypeList2(map);
		}
		String createTime = WebUtil.formatDateByPattern(news2.getCreateTime(),
				WebUtil.YMDHMS);

		this.getRequest().setAttribute("news2", news2);
		this.getRequest().setAttribute(
				"pCategory",
				news2.getPCategory() == 0 ? news2.getCategory() : news2
						.getPCategory());
		this.getRequest().setAttribute("list1", list1);
		this.getRequest().setAttribute("list2", list2);
		this.getRequest().setAttribute("convert",
				WebUtil.isNull(convert) ? "" : convert);
		this.getRequest().setAttribute("createTime", createTime);
		return "initNewsUpdate";
	}

	/** --查询所有一级栏目返还给手机端-- **/
	public void doNotNeedSessionAndSecurity_getAllLinkOfCategorys() {
		String basePath = getRequest().getScheme() + "://"
				+ getRequest().getServerName() + ":"
				+ getRequest().getServerPort() + getRequest().getContextPath();
		String URL = basePath + "/mobile/news/newsList.jsp?category=";
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("parent_id", 0);
		// 是否上导航（0：不上导航， 1：上导航）
		queryMap.put("isNavigation", 1);
		List<NewsType> list = this.newsService.selectTypeList(queryMap);
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		// 组织数据
		if (!WebUtil.isEmpty(list)) {
			for (NewsType type : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("dictId", type.getId());
				map.put("dictTypeId", "0");
				map.put("dictName", type.getName());
				if (type.getType() == 1) {
					map.put("dictUrl", URL + type.getId());
				} else if (type.getType() == 2) {
					map.put("dictUrl", type.getUrl());
				} else if (type.getType() == 3) {

					map.put("dictUrl",
							basePath + "/mobile/news/newsShow.jsp?category="
									+ type.getId());
				} else {
					map.put("dictUrl", "#");
				}

				map.put("dictValue", "0");
				listMap.add(map);
			}
		}
		super.writeJson(listMap);
	}

	/** ---根据mobile类别查询新闻列表-- **/
	public void doNotNeedSessionAndSecurity_getNewsListByMobileType() {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest request = this.getRequest();
		long category = WebUtil.toLong(request.getParameter("category"));
		// 判断栏目
		// NewsType type = this.newsService.selectTypeById(category+"");
		List<NewsType> leveList = this.newsService
				.selectLeveList(category + "");
		String topnews = request.getParameter("topnews");

		// 判断是否查询幻灯片新闻
		if (WebUtil.isNull(topnews) && !"100".equals(topnews)) {
			map.put("category", category);
		} else if ("100".equals(topnews)) {
			// 仅仅查询topnews
			map.put("topnews", topnews);
			if (WebUtil.isEmpty(leveList)) {
				map.put("topnews", topnews);
				map.put("category", category);
			} else {
				map.put("topnews", topnews);
				map.put("parent_id", category);
			}
		}

		int start = WebUtil.toInt(request.getParameter("start"));
		int rows = WebUtil.toInt(request.getParameter("rows"));
		JSONObject json = new JSONObject();
		if (start >= 0 && rows > 0) {
			map.put("start", start);
			map.put("rows", rows);
			json.put("start", start);
			json.put("rows", rows);
		}
		List<News2> list = newsService.listMobNews(map);
		int count = newsService.listMobNewsCount(map);
		json.put("newsList", list);
		json.put("countNews", count);
		super.writeJson(json);
	}

	/** --查询手机新闻类别,返回json数据，提供给前端页面-- **/
	public void doNotNeedSessionAndSecurity_getMobileNewsType() {
		HttpServletRequest request = this.getRequest();
		long category = WebUtil.toLong(request.getParameter("category"));
		List<NewsType> list = this.newsService.selectMobileNewsType(category);
		if (category == 0) {
			super.writeJson(list);
		} else {
			NewsType type = list.get(0);
			for (NewsType t : type.getLeveList()) {
				// 自动生成URL
				String path = request.getContextPath();
				String basePath = request.getScheme() + "://"
						+ request.getServerName() + ":"
						+ request.getServerPort() + path + "/";
				String json_news_url = basePath
						+ "mobile/news/newsAction!doNotNeedSessionAndSecurity_getNewsListByMobileType.action?category="
						+ t.getId();
				t.setJson_news_url(json_news_url);
			}
			super.writeJson(type);
		}
	}

	/** --查询手机新闻详细，返回新闻详细,提供给前端页面-- **/
	public void doNotNeedSessionAndSecurity_getMobileNews() {
		HttpServletRequest request = this.getRequest();
		long newsId = WebUtil.toLong(request.getParameter("newsId"));
		News news = this.newsService.selectById(newsId);
		super.writeJson(news);
	}

	/** --地方新闻的初始化更新-- **/
	public String doNotNeedSecurity_initNewsUpdatex() {
		String convert = this.getRequest().getParameter("convert");

		news2 = newsService.selectById2(id);
		// 查询手机新闻类型的1级栏目
		Map<String, Object> map = new HashMap<String, Object>();
		// 这里只允许查询总会新闻类别
		// map.put("origin", 1);
		map.put("parent_id", 0);
		map.put("type", 1);
		List<com.hxy.core.news.entity.NewsChannel> list1 = this.newsService
				.selectTypeList2(map);
		// 子栏目集合
		map = new HashMap<String, Object>();
		long parent_id = -1;
		com.hxy.core.news.entity.NewsChannel news2channel = newsService
				.selectChannelbyId(news2.getCategory());
		// if (news2channel.getChannel_pid() == 0) {
		// if (news2.getPCategory() != 0) {
		// parent_id = news2.getPCategory();
		// }
		// } else {
		// parent_id = news2channel.getChannel_pid();
		// }

		map.put("parent_id", 0);
		List<com.hxy.core.news.entity.NewsChannel> list2 = new ArrayList<com.hxy.core.news.entity.NewsChannel>();
		if (WebUtil.isNull(convert)) {
			list2 = this.newsService.selectTypeList2(map);
		}
		String createTime = WebUtil.formatDateByPattern(news2.getCreateTime(),
				WebUtil.YMDHMS);

		this.getRequest().setAttribute("news2", news2);
		this.getRequest().setAttribute(
				"pCategory",
				news2.getPCategory() == 0 ? news2.getCategory() : news2
						.getPCategory());
		this.getRequest().setAttribute("list1", list1);
		this.getRequest().setAttribute("list2", list2);
		this.getRequest().setAttribute("convert",
				WebUtil.isNull(convert) ? "" : convert);
		this.getRequest().setAttribute("createTime", createTime);
		return "initNewsUpdatex";
	}

	public String getById() {
		news2 = newsService.selectById2(id);
		String createTime = WebUtil.formatDateByPattern(news2.getCreateTime(),
				WebUtil.YMDHMS);
		this.getRequest().setAttribute("createTime", createTime);
		return "viewNews";
	}

	public String getByIdx() {
		news2 = newsService.selectById2(id);
		String createTime = WebUtil.formatDateByPattern(news2.getCreateTime(),
				WebUtil.YMDHMS);
		this.getRequest().setAttribute("createTime", createTime);
		return "viewNewsx";
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public static void main(String[] args) {
		String pic = "http%3A%2F%2F122.205.9.115%3A8088%2Fimage%2F20150619%2F20150619120402_841_MIN.jpg";
		String fileExt = pic.substring(pic.lastIndexOf("."));
		String temp = pic.substring(0, pic.lastIndexOf(".") - 4);
		System.out.println(fileExt);
		System.out.println(temp);
	}

	public News2 getNews2() {
		return news2;
	}

	public void setNews2(News2 news2) {
		this.news2 = news2;
	}

}
