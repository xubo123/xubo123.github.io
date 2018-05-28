package com.hxy.core.clientrelease.service;

import java.util.List;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.clientrelease.entity.Client;
import com.hxy.core.clientrelease.entity.ClientModel;

public interface ClientService {
	/**
	 * @param clientModel
	 * @return
	 */
	DataGrid<ClientModel> dataGridClient(ClientModel clientModel);
	
	/**
	 * @param clientModel
	 * @return
	 */
	int addClient(ClientModel clientModel);
	
	/**
	 * @param id
	 * @return
	 */
	ClientModel selectById(int id);
	
	/**
	 * @param clientModel
	 * @return
	 */
	int updateClient(ClientModel clientModel);
	
	/**
	 * @param clientModel
	 * @return
	 */
	List<Client> selectClient(ClientModel clientModel);
	
	Client selectNewOne();
}
