package com.hxy.core.user.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.user.entity.User;
import com.hxy.core.user.service.UserService;

@Namespace("/user")
@Action("userAction")
public class UserAction extends AdminBaseAction
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserAction.class);

	private User user;

	@Autowired
	private UserService userService;

	public void dataGrid()
	{
		String userAccount = getRequest().getParameter("userAccount");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		map.put("userAccount", userAccount);
		super.writeJson(userService.dataGrid(map));
	}
	
	public void save()
	{
		Message message = new Message();
		try
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userAccount", user.getUserAccount());
			map.put("userId", user.getUserId());
			long count = userService.countByUserAccount(map);
			if (count > 0)
			{
				message.setMsg("用户帐号已被占用");
				message.setSuccess(false);
			} else
			{
				userService.save(user,ids);
				message.setMsg("新增成功");
				message.setSuccess(true);
			}
		} catch (Exception e)
		{
			logger.error(e, e);
			message.setMsg("新增失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void update()
	{
		Message message = new Message();
		try
		{
			userService.update(user,ids);
			message.setMsg("修改成功");
			message.setSuccess(true);
		} catch (Exception e)
		{
			logger.error(e, e);
			message.setMsg("修改失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void getUserByUserId()
	{
		user = userService.selectByUserId(user.getUserId());
		super.writeJson(user);
	}

	public void delete()
	{
		Message message = new Message();
		try
		{
			userService.delete(id);
			message.setMsg("删除成功");
			message.setSuccess(true);
		} catch (Exception e)
		{
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void grant(){
		Message message = new Message();
		try
		{
			userService.updateGrant(ids, id);
			message.setMsg("修改角色成功");
			message.setSuccess(true);
		} catch (Exception e)
		{
			logger.error(e, e);
			message.setMsg("修改角色失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}
	
	

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

}
