package com.hxy.core.appuser.action;


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.appuser.service.AppUserService;


@Namespace("/appuser")
@Action(value = "appUserAction")
public class AppUserAction extends AdminBaseAction {
	
	private static final Logger logger = Logger
			.getLogger(AppUserAction.class);
	
	@Autowired
	private AppUserService appUserService;



	public void dataGrid() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		
		String user_id = getRequest().getParameter("user_id");
		String user_name = getRequest().getParameter("user_name");
		map.put("user_id", user_id);
		map.put("user_name", user_name);
		
		super.writeJson(appUserService.dataGrid(map));
	}

	/** --查看用户帐号--* */
	public void getById() {
		String id = getRequest().getParameter("id");
		super.writeJson(appUserService.getById(Long.parseLong(id)));
	}

	/*
	public void save() {
		Message message = new Message();
		try {
			appUserService.save(appuser);
			message.setMsg("保存成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("保存失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}
	
	

	public void update() {
		Message message = new Message();
		try {
			appUserService.update(appuser);
			message.setMsg("修改成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("修改失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	*/

	/**
	 * 传入app_user表的主键id列表删除用户
	 */
	public void delete() {
		Message message = new Message();
		try {
			if (ids != null) {
				// 删除注册信息
				appUserService.delete(ids);
				message.setMsg("删除成功");
				message.setSuccess(true);
			} else {
				message.setMsg("没有选择用户");
				message.setSuccess(false);
			}
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	

	
}
