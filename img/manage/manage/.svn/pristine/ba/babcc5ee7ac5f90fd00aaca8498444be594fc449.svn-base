package com.hxy.core.clientrelease.action;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.hxy.base.action.BaseAction;
import com.hxy.core.clientrelease.entity.ClientModel;
import com.hxy.core.clientrelease.entity.Json;
import com.hxy.core.clientrelease.service.ClientService;

@Namespace("/client")
@Action(value = "clientAction",results={@Result(name="showupdate",location="/page/admin/clientrelease/editClient.jsp")})
public class ClientAction extends BaseAction implements ModelDriven<ClientModel> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ClientAction.class);

	private ClientModel clientModel = new ClientModel();
	private ClientService clientService;

	public ClientModel getModel() {
		return clientModel;
	}

	public ClientModel getClientModel() {
		return clientModel;
	}

	public void setClientModel(ClientModel clientModel) {
		this.clientModel = clientModel;
	}

	public ClientService getClientService() {
		return clientService;
	}

	@Autowired
	public void setClientService(ClientService clientService) {
		this.clientService = clientService;
	}

	public void getClient(){
		super.writeJson(clientService.dataGridClient(clientModel));
	}
	
	public void addClient(){
		Json j = new Json();
		try{
			if(clientService.addClient(clientModel)>0){
				j.setMsg("新增成功");
				j.setSuccess(true);
			}else{
				j.setMsg("新增失败");
				j.setSuccess(false);
			}
		}catch (Exception e) {
			logger.error(e, e);
			j.setMsg("新增失败");
			j.setSuccess(false);
		}
		super.writeJson(j);
	}
	
	public String showUpdate(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		clientModel=clientService.selectById(Integer.parseInt(id));
		return "showupdate";
	}
	
	public void updateClient(){
		Json j = new Json();
		try{
			if(clientService.updateClient(clientModel)>0){
				j.setMsg("修改成功");
				j.setObj(clientModel);
				j.setSuccess(true);
			}else{
				j.setMsg("修改失败");
				j.setSuccess(false);
			}
		}catch (Exception e) {
			logger.error(e, e);
			j.setMsg("修改失败");
			j.setSuccess(false);
		}
		super.writeJson(j);
	}}
