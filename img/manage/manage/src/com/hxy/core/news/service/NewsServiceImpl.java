package com.hxy.core.news.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hxy.base.entity.DataGrid;
import com.hxy.core.dict.entity.Dict;
import com.hxy.core.event.entity.Event;
import com.hxy.core.event.entity.PushedEvent;
import com.hxy.core.news.dao.NewsMapper;
import com.hxy.core.news.entity.*;
import com.hxy.system.Global;
import com.hxy.util.WebUtil;
import com.hxy.util.jms.SingleNewsMessage;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;

@Service("newsService")
public class NewsServiceImpl implements NewsService {
	@Autowired
	private NewsMapper newsMapper;

	/** --后台管理新闻列表-- **/
	public DataGrid<News> dataGrid(Map<String, Object> map) {
		DataGrid<News> dataGrid = new DataGrid<News>();
		long total = newsMapper.countNews2(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<News> list = newsMapper.selectNews(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	/** --后台管理新闻列表-- **/
	public DataGrid<News2> dataGrid2(Map<String, Object> map) {
		DataGrid<News2> dataGrid = new DataGrid<News2>();
		long total = newsMapper.countNews2(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<News2> list = newsMapper.selectNews2(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public NewsChannel selectChannelbyId(long id) {
		return newsMapper.selectChannelbyId(id);
	}

	public News selectById(long activityId) {
		News news = newsMapper.selectById(activityId);

		String separator = ",";

		if (news.getType() != null && news.getType().length() > 0) {
			if (news.getType().indexOf(separator) != -1) {
				news.setTypes(news.getType().split(separator));
			} else {
				String types[] = new String[1];
				types[0] = news.getType();
				news.setTypes(types);
			}
		}

		String types = JSON.toJSONString(news.getTypes());
		if (types != null && types.indexOf("\"") != -1) {
			types = types.replaceAll("\"", "'").replaceAll(" ", "");
		}

		news.setTypeStr(types);

		return news;
	}

	public News2 selectById2(long activityId) {
		News2 news = newsMapper.selectById2(activityId);

		String separator = ",";

		if (news.getType() != null && news.getType().length() > 0) {
			if (news.getType().indexOf(separator) != -1) {
				news.setTypes(news.getType().split(separator));
			} else {
				String types[] = new String[1];
				types[0] = news.getType();
				news.setTypes(types);
			}
		}

		String types = JSON.toJSONString(news.getTypes());
		if (types != null && types.indexOf("\"") != -1) {
			types = types.replaceAll("\"", "'").replaceAll(" ", "");
		}

		news.setTypeStr(types);
		news.setPicUrl(Global.URL_DOMAIN + news.getPic());
		// news.setPicUrl(Global.URL_DOMAIN+news.getPic());
		return news;
	}

	public void save(News news) {
		newsMapper.save(news);
	}

	public void save2(News2 news) {
		newsMapper.save2(news);
	}

	/** --新闻添加，同时生成URL-- **/
	public void insertNewsAndsetUrl(News2 news, String url) {
		newsMapper.insert(news);
		String newsId = news.getNewsId() + "";
		news.setNewsUrl(url + newsId);
		newsMapper.update2(news);
	}

	public void update(News news) {
		newsMapper.update(news);
	}

	public void update2(News2 news) {
		newsMapper.update2(news);
	}

	public void delete(String ids) {
		String[] array = ids.split(",");
		List<Long> list = new ArrayList<Long>();
		for (String id : array) {
			list.add(Long.parseLong(id));
		}
		newsMapper.delete(list);
	}

	public List<News> selectNews(Map<String, Object> map) {
		List<News> list = newsMapper.selectNews(map);
		return this.selectNewsImgeURL(list);
		// return newsMapper.selectNews(map);
	}

	public List<Dict> getAllCategorys(Map<String, Object> map) {

		return newsMapper.getAllCategorys(map);
	}

	public void setMobTypeList(String ids, String controlStr) {
		Map<String, Object> map = new HashMap<String, Object>();

		String[] array = ids.split(",");
		List<Long> list = new ArrayList<Long>();
		for (String id : array) {
			list.add(Long.parseLong(id));
		}

		map.put("list", list);
		map.put("controlStr", controlStr);

		newsMapper.setMobTypeList(map);
	}

	public void setWebTypeList(String ids, String controlStr) {
		Map<String, Object> map = new HashMap<String, Object>();

		String[] array = ids.split(",");
		List<Long> list = new ArrayList<Long>();
		for (String id : array) {
			list.add(Long.parseLong(id));
		}

		map.put("list", list);
		map.put("controlStr", controlStr);

		newsMapper.setWebTypeList(map);
	}

	public List<News> listMobTopNews(Map<String, Object> map) {
		List<News> list = newsMapper.listMobTopNews(map);
		return this.selectNewsImgeURL(list);
		// return newsMapper.listMobTopNews(map);
	}

	public List<News2> listMobNews(Map<String, Object> map) {
		List<News2> list = newsMapper.listMobNews(map);
		for (News2 news : list) {
			news.setPic(Global.URL_DOMAIN + news.getPic());
		}
		return this.selectNewsImgeURL2(list);
	}

	/** --查询新闻的数量-- **/
	public int listMobNewsCount(Map<String, Object> map) {
		return this.newsMapper.listMobNewsCount(map);
	}

	/** --整理新闻图片的链接-- **/
	private List<News2> selectNewsImgeURL2(List<News2> list) {
		for (int i = 0; i < list.size(); i++) {
			News2 news = list.get(i);
			// 图片URL
			String pic = news.getPic();
			if (!WebUtil.isEmpty(pic)) {
				// 图片后缀
				String fileExt = pic.substring(pic.lastIndexOf("."));
				String temp = pic.substring(0, pic.lastIndexOf("."));
				if ("100".equals(news.getTopnews())) {
					// 该新闻为滚动新闻，需要调用大图
					news.setPic(temp + "_MAX" + fileExt);
				} else if (temp.indexOf("_MIN") == -1) {
					news.setPic(temp + "_MIN" + fileExt);
				}
			}
		}
		return list;
	}

	/** --整理新闻图片的链接-- **/
	private List<News> selectNewsImgeURL(List<News> list) {
		for (int i = 0; i < list.size(); i++) {
			News news = list.get(i);
			// 图片URL
			String pic = news.getPic();
			if (!WebUtil.isEmpty(pic)) {
				// 图片后缀
				String fileExt = pic.substring(pic.lastIndexOf("."));
				String temp = pic.substring(0, pic.lastIndexOf("."));
				if ("100".equals(news.getTopnews())) {
					// 该新闻为滚动新闻，需要调用大图
					news.setPic(temp + "_MAX" + fileExt);
				} else if (temp.indexOf("_MIN") == -1) {
					news.setPic(temp + "_MIN" + fileExt);
				}
			}
		}
		return list;
	}

	/** --查询列表-- **/
	public List<NewsType> selectTypeList(Map<String, Object> map) {
		return newsMapper.selectTypeList(map);
	}

	@Override
	public List<NewsChannel> selectTypeList2(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return newsMapper.selectTypeList2(map);
	}

	public List<NewsType> selectWebTypeList(Map<String, Object> map) {
		return newsMapper.selectWebTypeList(map);
	}

	/** --查询返回所有的新闻1级栏目,提供给外部web页面-- **/
	public List<NewsType> selectMobileNewsType(long category) {
		// 查询所有
		Map<String, Object> map = new HashMap<String, Object>();
		List<NewsType> list = newsMapper.selectTypeList(map);
		List<NewsType> list1 = new ArrayList<NewsType>(); // 1级栏目
		List<NewsType> list2 = new ArrayList<NewsType>(); // 2级栏目
		List<NewsType> returnList = new ArrayList<NewsType>(); // 返回的数据
		if (list != null && list.size() > 0) {
			for (NewsType type : list) {
				if (type.getParent_id() == 0) {
					list1.add(type);
				} else if (type.getParent_id() > 0) {
					list2.add(type);
				}
			}
			// 便利1级栏目
			for (NewsType type1 : list1) {
				// 便利2级栏目
				List<NewsType> leveList = new ArrayList<NewsType>(); // 1级栏目的子栏目
				for (NewsType type2 : list2) {
					if (type2.getParent_id() == type1.getId()) {
						leveList.add(type2);
					}
				}
				type1.setLeveList(leveList);
			}
		}

		if (category == 0) {
			returnList = list1;
		} else {
			List<NewsType> datalist = new ArrayList<NewsType>();
			for (NewsType type : list1) {
				if (type.getId() == category) {
					datalist.add(type);
					returnList = datalist;
					break;
				}
			}
		}
		return returnList;
	}

	/** --查询2级栏目集合-- **/
	public List<NewsType> selectLeveList(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parent_id", id);
		return this.newsMapper.selectTypeList(map);
	}

	public static void main(String[] args) {
		String s = "http://122.205.9.115:8088/image/20150417/20150417131824_639.jpg";
		String fileExt = s.substring(s.lastIndexOf("."));
		System.out.println(fileExt);
		System.out.println(s.substring(0, s.lastIndexOf(".")));
	}

	@Override
	public News selectWebNewFromWebType(NewsType newsType) {

		return this.newsMapper.selectWebNewFromWebType(newsType);
	}

	/**
	 * Ios 推送
	 * 
	 * @param tagList
	 *            标签
	 * @param xinge
	 *            信鸽实例化对象
	 * @param newsList
	 *            推送内容
	 * @param type
	 *            区分推送活动和新闻
	 */
	public void sendIosMessage(List<String> tagList,
			List<SingleNewsMessage> newsList, List<PushedEvent> pushedEvent) {
		XingeApp xinge = new XingeApp(Long.parseLong("2200204847"),
				"394435a4b3b865a7eb7efa6e82c83fcd");
		// IOS推送
		MessageIOS messageIos = new MessageIOS();
		messageIos.setExpireTime(86400);
		messageIos.setAlert("IOS Push Test");
		messageIos.setBadge(1);
		messageIos.setSound("beep.wav");
		if (newsList != null) {
			messageIos.setM_content(newsList);
		} else {
			messageIos.setEvent_content(pushedEvent);
		}
//		messageIos.setMessageType(type);// 活动和新闻
		if (tagList != null && tagList.size() != 0) {
			System.out.println(xinge.pushTags(0, tagList, "OR", messageIos,
					XingeApp.IOSENV_DEV));
		} else {
			System.out.println(xinge.pushAllDevice(0, messageIos,
					XingeApp.IOSENV_DEV));
		}
	}

	/**
	 * Android 推送
	 * 
	 * @param tagList
	 *            标签
	 * @param xinge
	 *            信鸽实例化对象
	 * @param newsList
	 *            推送内容
	 * @param type
	 *            区分推送活动和新闻
	 */
	public void sendAndroidMessage(List<String> tagList,
			List<SingleNewsMessage> newsList, List<PushedEvent> pushedEvent) {
		// Android推送
		XingeApp xinge = new XingeApp(Long.parseLong("2100209524"),
				"26c57e9c66b80b51d29e6c4e6bd3994f");
		Style style = new Style(1);
		style = new Style(3, 1, 0, 1, 0);
		ClickAction action = new ClickAction();
		action.setActionType(ClickAction.TYPE_ACTIVITY);
		Map<String, Object> custom = new HashMap<String, Object>();
		custom.put("key1", "value1");
		custom.put("key2", 2);
		com.tencent.xinge.Message messageAndroid = new com.tencent.xinge.Message();
		messageAndroid.setType(com.tencent.xinge.Message.TYPE_NOTIFICATION);
		messageAndroid.setTitle("title");
		// message1.setContent(entry.getValue().getNewsList());
		if (newsList != null) {
			messageAndroid.setContent(newsList);
			action.setActivity("com.example.app.em.MainActivity");
		} else {
			messageAndroid.setEvent_content(pushedEvent);
			action.setActivity("com.example.app.xg.PushMessageActivity");
		}
		messageAndroid.setStyle(style);
		messageAndroid.setAction(action);
		messageAndroid.setCustom(custom);
		if (tagList != null && tagList.size() != 0) {
			System.out
					.println(xinge.pushTags(0, tagList, "OR", messageAndroid));
		} else {
			System.out.println(xinge.pushAllDevice(0, messageAndroid));
		}
	}

	@Override
	public DataGrid<News2> dataGridForAlumni(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataGrid<News2> dataGrid = new DataGrid<News2>();
		long total = newsMapper.countNewsForAlumni(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<News2> list = newsMapper.selectNewsForAlumni(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

}
