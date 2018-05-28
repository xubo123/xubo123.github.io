package com.hxy.core.dict.action;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.dict.entity.Dict;
import com.hxy.core.dict.service.DictService;

@Namespace("/dict")
@Action(value = "dictAction", results = { @Result(name = "showUpdate", location = "/page/admin/dict/editDict.jsp") })
public class DictAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DictAction.class);

	@Autowired
	private DictService dictService;
	private Dict dictObj;

	public Dict getDictObj() {
		return dictObj;
	}

	public void setDictObj(Dict dictObj) {
		this.dictObj = dictObj;
	}

	public void getDict() {
		super.writeJson(dictService.selectByDictTypeId(id));
	}

	public void addDict() {
		Message j = new Message();
		try {
			dictService.addDict(dictObj);
			j.setSuccess(true);
			j.setMsg("新增成功");
		} catch (Exception e) {
			logger.error(e, e);
			j.setSuccess(false);
			j.setMsg("新增失败");
		}

		super.writeJson(j);
	}

	public void deleteDict() {
		Message j = new Message();
		try {
			dictService.deleteDict(id);
			j.setSuccess(true);
			j.setMsg("删除成功");
		} catch (Exception e) {
			logger.error(e, e);
			j.setSuccess(false);
			j.setMsg("删除失败");
		}
		super.writeJson(j);
	}

	public String doNotNeedSessionAndSecurity_showUpdate() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		dictObj = dictService.selectDictById(id);
		return "showUpdate";
	}

	public void updateDict() {
		Message j = new Message();
		try {
			dictService.updateDict(dictObj);
			j.setSuccess(true);
			j.setMsg("修改成功");
		} catch (Exception e) {
			logger.error(e, e);
			j.setSuccess(false);
			j.setMsg("修改失败");
		}
		super.writeJson(j);
	}

}
