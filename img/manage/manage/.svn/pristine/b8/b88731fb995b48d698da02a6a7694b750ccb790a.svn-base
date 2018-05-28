package com.hxy.core.schoolServ.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.schoolServ.entity.SchoolServ;
import com.hxy.core.schoolServ.service.SchoolServService;


@Namespace("/mobile/schoolServ")
@Action(value = "schoolServAction", results = { 
		@Result(name = "view", location = "/page/admin/schoolServ/view.jsp"),
		@Result(name = "edit", location = "/page/admin/schoolServ/edit.jsp")
		})
public class SchoolServAction extends AdminBaseAction
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SchoolServAction.class);

	@Autowired
	private SchoolServService service;

	private SchoolServ formData;



	public void dataGrid()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		map.put("page", page);
		if (formData != null)
		{
			if(formData.getServiceName() != null && formData.getServiceName().length() > 0)
			{
				map.put("serviceName", formData.getServiceName());
			}
		}
		super.writeJson(service.dataGrid(map));
	}
	
	public void save()
	{
		Message message = new Message();
		try
		{
			com.hxy.core.user.entity.User user = (com.hxy.core.user.entity.User)getSession().get("user");
			formData.setCreateBy(user.getUserName());
			service.save(formData);
			message.setMsg("操作成功");
			message.setSuccess(true);

		} 
		catch (Exception e)
		{
			logger.error(e, e);
			message.setMsg("操作失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void delete()
	{
		Message message = new Message();
		try
		{
			service.delete(ids);
			message.setMsg("删除成功");
			message.setSuccess(true);

		} 
		catch (Exception e)
		{
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void update()
	{
		Message message = new Message();
		try
		{
			com.hxy.core.user.entity.User user = (com.hxy.core.user.entity.User)getSession().get("user");
			formData.setCreateBy(user.getUserName());
			service.update(formData);
			message.setMsg("操作成功");
			message.setSuccess(true);

		} 
		catch (Exception e)
		{
			logger.error(e, e);
			message.setMsg("操作失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public String getById()
	{
		formData = service.selectById(id);
		return "view";
	}
	
	public String initUpdate()
	{
		formData = service.selectById(id);
		return "edit";
	}
	

	public void doNotNeedSessionAndSecurity_getServiceList()
	{
		List<SchoolServ> schoolServList = service.getServiceList();

		super.writeJson(schoolServList);
	}
	

	public SchoolServ getFormData() {
		return formData;
	}

	public void setFormData(SchoolServ formData) {
		this.formData = formData;
	}
	
	
	

}
