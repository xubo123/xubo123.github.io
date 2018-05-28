package com.hxy.util;


import java.io.IOException;
import java.net.URLDecoder;

import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class GroupRegister {

	/**
	 * @param args 环信后台配置参数
	 */
	static String url = "https://a1.easemob.com/xbblfz/xbblfz/chatgroups";// 该url可配置，与环信后台注册的应用相关
	static String tokenurl = "https://a1.easemob.com/xbblfz/xbblfz/token";
	static String client_secret = "YXA6ufDQtvBvi7TmzSxw6jzoSrK-sjE";//可配置，环信后台中的client_secret
	static String client_id = "YXA6waPAEDRJEeaK4d8Z92sWDw";//可配置，环信后台中的client_id

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
	    ChatGroup chatGroup = new ChatGroup();
	    chatGroup.setGroupname("测试群组");
	    chatGroup.setDesc("创建群组");
	    chatGroup.setMaxusers(100);
	    //String[] members = { "xubo","xubo4" };//群主成员
	    //chatGroup.setMembers(members);
	    chatGroup.setOwner("system");
	    chatGroup.setPublic(false);
	    chatGroup.setApproval(true);
	    String groupId = createChatGroup(chatGroup);
		System.out.print(groupId);
	    
	    
	}
	

	/**
	 * @param chatgroup群组参数类
	 * @return 创建成功返回聊天室ID，创建失败返回"error"
	 */
	public static String createChatGroup(ChatGroup chatGroup) {
		JSONObject json = new JSONObject();
		//获取认证token
		String token = getToken(client_id,client_secret);
		//填写创建聊天室参数
		json.put("groupname", chatGroup.getGroupname());
		json.put("maxusers", chatGroup.getMaxusers());
		json.put("members", chatGroup.getMembers());
		json.put("desc", chatGroup.getDesc());
		json.put("owner", chatGroup.getOwner());
		json.put("approval", chatGroup.isApproval());
		json.put("public", chatGroup.isPublic());
		JSONObject result = httpPost(url, json, false, token);
		if (result.get("error") != null) {
			System.out.println("创建失败! 原因:" + result.get("error"));
			return "error";
		} else {
			System.out.println("创建成功！"+"返回内容:"+result);
			return (String) result.getJSONObject("data").get("groupid");
		}
	}

	/**
	 * @return 返回认证token
	 */
	public static String getToken(String client_id,String client_secret) {
		JSONObject json = new JSONObject();
		json.put("grant_type", "client_credentials");
		json.put("client_id",client_id );
		json.put("client_secret", client_secret);
		JSONObject result = httpPost(tokenurl, json, false, null);
		if (result.get("error") != null) {
			System.out.println("获取token失败! 原因:" + result.get("error"));
			return "error";
		} else {
			String token = result.getString("access_token");
			System.out.println("获取token成功！token为"+token);
			return token;
		}
	}

	/**
	 * Http访问工具方法
	 */
	public static JSONObject httpPost(String url, JSONObject jsonParam,
			boolean noNeedResponse, String token) {
		// post请求返回结果
		DefaultHttpClient httpClient = new DefaultHttpClient();
		JSONObject jsonResult = null;
		HttpPost method = new HttpPost(url);
		if (token != null) {
			method.addHeader("Authorization", "Bearer " + token);
		}
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam.toString(),
						"utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			HttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			/** 请求发送成功，并得到响应 **/
			if (result.getStatusLine().getStatusCode() == 200) {
				String str = "";
				try {
					/** 读取服务器返回过来的json字符串数据 **/
					str = EntityUtils.toString(result.getEntity());
					if (noNeedResponse) {
						return null;
					}
					/** 把json字符串转换成json对象 **/
					jsonResult = JSONObject.fromObject(str);
				} catch (Exception e) {
					System.out.println("post请求提交失败:" + url + e);
				}
			} else {
				String str2 = "";
				str2 = EntityUtils.toString(result.getEntity());
				jsonResult = JSONObject.fromObject(str2);
			}
		} catch (IOException e) {
			System.out.println("post请求提交失败:" + url + e);
		}
		return jsonResult;
	}
}


