package com.hxy.core.email.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.DataGrid;
import com.hxy.base.entity.Message;
import com.hxy.core.email.entity.Email;
import com.hxy.core.email.entity.EmailRecipient;
import com.hxy.core.email.service.EmailService;
import com.hxy.core.sms.entity.MsgSend;
import com.hxy.core.user.entity.User;
import com.hxy.core.userbaseinfo.service.UserBaseInfoService;

@Namespace("/page/admin/email")
@Action(value = "emailAction", results = {
		@Result(name = "view", location = "/page/admin/email/view.jsp"),
		@Result(name = "edit", location = "/page/admin/email/edit.jsp"),
		@Result(name = "add", location = "/page/admin/email/add.jsp") })
public class EmailAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(EmailAction.class);

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserBaseInfoService userbaseinfoService;
	
	private Email email;
	
	private MsgSend msgSend;

	private String emailType1;

	private String[] emailParam;

	private String emailTemplateContent;

	private String fromPassword;

	private String[] fj;

	private String bccAddress;

	private String ccAddress;

	public void dataGrid() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		map.put("page", page);
		if (email != null) {
			map.put("emailSubject", email.getEmailSubject());
		}
		if (getUser().getRole().getSystemAdmin() != 1) {
			map.put("staffId", getUser().getUserId());
		}
		super.writeJson(emailService.dataGrid(map));
	}

	public void save() {
		Message message = new Message();
		try {
			if (emailType1.equals("模板邮件")) {
				if (emailParam != null) {
					for (int i = 0; i < emailParam.length; i++) {
						emailTemplateContent = emailTemplateContent.replace(
								"${" + i + "}", emailParam[i]);
					}
				}
				email.setEmailText(email.getEmailText());
			}
			email.setFj(fj);
			email.setStaffId(getUser().getUserId());
			User user = getUser();
			email.setFromAddress(getUser().getEmail());
			email.setFromPassword("hgvcjmurbdvwbcbh");
			email.setCcAddress(ccAddress);
			email.setBccAddress(bccAddress);
			emailService.save(email);
			message.setMsg("发送成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("发送失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void save1() {
		Message message = new Message();
		try {
			if (emailType1.equals("模板邮件")) {
				if (emailParam != null) {
					for (int i = 0; i < emailParam.length; i++) {
						emailTemplateContent = emailTemplateContent.replace(
								"${" + i + "}", emailParam[i]);
					}
				}
				email.setEmailText(emailTemplateContent);
			}
			email.setFj(fj);
			email.setStaffId(getUser().getUserId());
			email.setFromPassword(fromPassword);
			email.setCcAddress(ccAddress);
			email.setBccAddress(bccAddress);
			emailService.save(email);
			message.setMsg("发送成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("发送失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void delete() {
		Message message = new Message();
		try {
			emailService.delete(ids);
			message.setMsg("删除成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void update() {
		Message message = new Message();
		try {
			emailService.update(email);
			message.setMsg("操作成功");
			message.setSuccess(true);

		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("操作失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void getById() {
		email = emailService.selectById(id);
		super.writeJson(email);
	}

	public String initUpdate() {
		email = emailService.selectById(id);
		return "edit";
	}

	/**
	 * 给选中毕业生发送邮件
	 * 
	 * @return
	 */
	public void sendEmail() {
//		email = emailService.selectById(1);
		String[] array = ids.split(",");
		List<String> list = new ArrayList<String>();
		for (String id : array) {
			list.add(id);
		}
		List<String> toAddresslist = userbaseinfoService.selectEmailbyId(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < toAddresslist.size(); i++) {  
		    sb.append(toAddresslist.get(i)).append(",");  
		}
		String toAddress = sb.toString().substring(0,sb.toString().length()-1);
		email = new Email();
		email.setToAddress(toAddress);
		super.writeJson(email);
	}
	/**
	 * 给选中毕业生发送短信
	 * 
	 * @return
	 */
	public void sendMsg() {
//		email = emailService.selectById(1);
		String[] array = ids.split(",");
		List<String> list = new ArrayList<String>();
		for (String id : array) {
			list.add(id);
		}
		List<String> toAddresslist = userbaseinfoService.selectTelbyId(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < toAddresslist.size(); i++) {  
		    sb.append(toAddresslist.get(i)).append(",");  
		}
		String toAddress = sb.toString().substring(0,sb.toString().length()-1);
		msgSend = new MsgSend();
		msgSend.setTelphone(toAddress);
		super.writeJson(msgSend);
	}
	/**
	 * 得到所有的标签
	 * 
	 */
	public void doNotNeedSecurity_getAllUserList() {
		// List<Map<String, String>> userList =
		// emailService.selectAllUsersList();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", rows);
		map.put("page", page);

		DataGrid<EmailRecipient> dataGrid = emailService
				.selectAllUsersList(map);

		super.writeJson(dataGrid);
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public String getEmailType1() {
		return emailType1;
	}

	public void setEmailType1(String emailType1) {
		this.emailType1 = emailType1;
	}

	public String[] getEmailParam() {
		return emailParam;
	}

	public void setEmailParam(String[] emailParam) {
		this.emailParam = emailParam;
	}

	public String getEmailTemplateContent() {
		return emailTemplateContent;
	}

	public void setEmailTemplateContent(String emailTemplateContent) {
		this.emailTemplateContent = emailTemplateContent;
	}

	public String getFromPassword() {
		return fromPassword;
	}

	public void setFromPassword(String fromPassword) {
		this.fromPassword = fromPassword;
	}

	public String[] getFj() {
		return fj;
	}

	public void setFj(String[] fj) {
		this.fj = fj;
	}

	public String getBccAddress() {
		return bccAddress;
	}

	public void setBccAddress(String bccAddress) {
		this.bccAddress = bccAddress;
	}

	public String getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}

}
