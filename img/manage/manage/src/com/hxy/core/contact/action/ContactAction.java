package com.hxy.core.contact.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.contact.entity.Contact;
import com.hxy.core.contact.service.ContactService;

@Namespace("/contact")
@Action(value = "contactAction")
public class ContactAction extends AdminBaseAction {

	private static final Logger logger = Logger.getLogger(ContactAction.class);

    private Contact contact;

    @Autowired
    private ContactService contactService;

    public void getList() {
        Map<String, Object> map = new HashMap<String, Object>();
        String category = getRequest().getParameter("category");
        map.put("category", category);
        map.put("page", page);
        map.put("rows", rows);
        super.writeJson(contactService.dataGrid(map));
    }

    public void getById() {
        super.writeJson(contactService.getById(contact.getId()));
    }

    public void reply() {
        Message message = new Message();
        try {
        	com.hxy.core.user.entity.User user = (com.hxy.core.user.entity.User)getSession().get("user");
        	contact.setReplyUserId(user.getUserId());
        	contactService.reply(contact);
            message.setMsg("保存成功");
            message.setSuccess(true);
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
        	contactService.delete(ids);
            message.setMsg("删除成功");
            message.setSuccess(true);
        } catch (Exception e) {
            logger.error(e, e);
            message.setMsg("删除失败");
            message.setSuccess(false);
        }
        super.writeJson(message);
    }

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

    
}
