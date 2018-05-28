package com.hxy.core.sms.action;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.sms.entity.MsgSend;
import com.hxy.core.sms.service.MsgSendService;

@Namespace("/msgSend")
@Action(value = "msgSendAction", results = { @Result(name = "viewReport", location = "/page/sms/smsReport.jsp") })
public class MsgSendAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MsgSendAction.class);

	private MsgSendService msgSendService;
	private List<MsgSend> sucList;
	private List<MsgSend> failList;
	private List<MsgSend> noReportList;
	private List<MsgSend> readyList;
	private String msgType1;
	private String msgTemplateContent;
	private String[] msgParam;

	private MsgSend msgSend;

	public void getAddrBookNode() {
	}

	public void addMsgSend() {
		Message j = new Message();
		try {
			if (msgType1.equals("模板短信")) {
				if (msgParam != null) {
					for (int i = 0; i < msgParam.length; i++) {
						msgTemplateContent = msgTemplateContent.replace("${" + i + "}", msgParam[i]);
					}
				}
				msgSend.setContent(msgTemplateContent);
			}
			msgSend.setStaffId(getUser().getUserId());
			msgSendService.insertMsgSend(msgSend);
			msgSendService.sendSMSForNotificationMessage(msgSend.getTelphone(), msgSend.getContent());
			j.setSuccess(true);
			j.setMsg("发送成功");
		} catch (Exception e) {
			j.setMsg("发送失败");
			j.setSuccess(false);
			logger.error(e, e);
		}

		super.writeJson(j);
	}
	
	public void addMsgSend1() {
		Message j = new Message();
		try {
			if (msgType1.equals("模板短信")) {
				if (msgParam != null) {
					for (int i = 0; i < msgParam.length; i++) {
						msgTemplateContent = msgTemplateContent.replace("${" + i + "}", msgParam[i]);
					}
				}
				msgSend.setContent(msgTemplateContent);
			}
			msgSend.setStaffId(getUser().getUserId());
			msgSendService.insertMsgSend(msgSend);
			j.setSuccess(true);
			j.setMsg("发送成功");
		} catch (Exception e) {
			j.setMsg("发送失败");
			j.setSuccess(false);
			logger.error(e, e);
		}

		super.writeJson(j);
	}

	public void getSmsOutBox() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		if (msgSend != null) {
			map.put("telphone", msgSend.getTelphone());
		}
		if(getUser().getRole().getSystemAdmin()!=1){
			map.put("staffId", getUser().getUserId());
		}
		super.writeJson(msgSendService.dataGridOutBox(map));
	}

	public String viewReport() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String messagegroup = request.getParameter("messagegroup");
		Map<String, List<MsgSend>> map = msgSendService.viewReport(messagegroup);
		sucList = map.get("sucList");
		failList = map.get("failList");
		noReportList = map.get("noReportList");
		readyList = map.get("readyList");
		return "viewReport";
	}

	public MsgSendService getMsgSendService() {
		return msgSendService;
	}

	@Autowired
	public void setMsgSendService(MsgSendService msgSendService) {
		this.msgSendService = msgSendService;
	}

	public List<MsgSend> getSucList() {
		return sucList;
	}

	public void setSucList(List<MsgSend> sucList) {
		this.sucList = sucList;
	}

	public List<MsgSend> getFailList() {
		return failList;
	}

	public void setFailList(List<MsgSend> failList) {
		this.failList = failList;
	}

	public List<MsgSend> getNoReportList() {
		return noReportList;
	}

	public void setNoReportList(List<MsgSend> noReportList) {
		this.noReportList = noReportList;
	}

	public List<MsgSend> getReadyList() {
		return readyList;
	}

	public void setReadyList(List<MsgSend> readyList) {
		this.readyList = readyList;
	}

	public String getMsgType1() {
		return msgType1;
	}

	public void setMsgType1(String msgType1) {
		this.msgType1 = msgType1;
	}

	public String getMsgTemplateContent() {
		return msgTemplateContent;
	}

	public void setMsgTemplateContent(String msgTemplateContent) {
		this.msgTemplateContent = msgTemplateContent;
	}

	public String[] getMsgParam() {
		return msgParam;
	}

	public void setMsgParam(String[] msgParam) {
		this.msgParam = msgParam;
	}

	public MsgSend getMsgSend() {
		return msgSend;
	}

	public void setMsgSend(MsgSend msgSend) {
		this.msgSend = msgSend;
	}

}
