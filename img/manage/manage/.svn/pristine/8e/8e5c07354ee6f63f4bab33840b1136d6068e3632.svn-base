package com.hxy.core.msgTemplate.action;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.msgTemplate.entity.MsgTemplate;
import com.hxy.core.msgTemplate.service.MsgTemplateService;

@Namespace("/msgTemplate")
@Action(value = "msgTemplateAction", results = {
		@Result(name = "initMsgTemplateUpdate", location = "/page/admin/msgTemplate/editMsgTemplate.jsp"),
		@Result(name = "viewMsgTemplate", location = "/page/admin/msgTemplate/viewMsgTemplate.jsp") })
public class MsgTemplateAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(MsgTemplateAction.class);

	@Autowired
	private MsgTemplateService msgTemplateService;

	private MsgTemplate msgTemplate;

	public MsgTemplate getMsgTemplate() {
		return msgTemplate;
	}

	public void setMsgTemplate(MsgTemplate msgTemplate) {
		this.msgTemplate = msgTemplate;
	}

	public void dataGrid() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		if (msgTemplate != null) {
			map.put("msgTemplateTitle", msgTemplate.getMsgTemplateTitle());
		}
		super.writeJson(msgTemplateService.dataGrid(map));
	}

	public void save() {
		Message message = new Message();
		try {
			MsgTemplate check = msgTemplateService.selectByTitle(msgTemplate);
			if (check == null) {
				Pattern pattern = Pattern.compile("\\$\\{\\d+\\}");
				Matcher matcher = pattern.matcher(msgTemplate
						.getMsgTemplateContent());
				int number = 0;
				while (matcher.find()) {
					number++;
				}
				msgTemplate.setMsgTemplateParamNumber(number);
				msgTemplateService.save(msgTemplate);
				message.setMsg("保存成功");
				message.setSuccess(true);
			} else {
				message.setMsg("保存失败,短信模板名称重复");
				message.setSuccess(false);
			}
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
			MsgTemplate check = msgTemplateService.selectByTitle(msgTemplate);
			if (check == null) {
				Pattern pattern = Pattern.compile("\\$\\{\\d+\\}");
				Matcher matcher = pattern.matcher(msgTemplate
						.getMsgTemplateContent());
				int number = 0;
				while (matcher.find()) {
					number++;
				}
				msgTemplate.setMsgTemplateParamNumber(number);
				msgTemplateService.update(msgTemplate);
				message.setMsg("保存成功");
				message.setSuccess(true);
			} else {
				message.setMsg("保存失败,短信模板名称重复");
				message.setSuccess(false);
			}
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("保存失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void delete() {
		Message message = new Message();
		try {
			msgTemplateService.delete(ids);
			message.setMsg("删除成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public String doNotNeedSessionAndSecurity_initMsgTemplateUpdate() {
		msgTemplate = msgTemplateService.selectById(id);
		return "initMsgTemplateUpdate";
	}

	public void doNotNeedSessionAndSecurity_getAll() {
		super.writeJson(msgTemplateService.selectAll());
	}

	public String getById() {
		msgTemplate = msgTemplateService.selectById(id);
		return "viewMsgTemplate";
	}

}
