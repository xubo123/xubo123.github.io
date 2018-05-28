package com.hxy.core.clientrelease.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.clientrelease.dao.ClientMapper;
import com.hxy.core.clientrelease.entity.Client;
import com.hxy.core.clientrelease.entity.ClientModel;
import com.hxy.core.clientrelease.service.ClientService;

@Service("clientService")
public class ClientServiceImpl implements ClientService {

	private ClientMapper clientMapper;

	public ClientMapper getClientMapper() {
		return clientMapper;
	}

	@Autowired
	public void setClientMapper(ClientMapper clientMapper) {
		this.clientMapper = clientMapper;
	}

	public DataGrid<ClientModel> dataGridClient(ClientModel clientModel) {
		DataGrid<ClientModel> dataGrid = new DataGrid<ClientModel>();
		long total = clientMapper.countClient(clientModel);
		dataGrid.setTotal(total);
		int start = (clientModel.getPage() - 1) * clientModel.getRows();
		int end = clientModel.getRows();
		clientModel.setStart(start);
		clientModel.setEnd(end);
		List<Client> list = clientMapper.selectClient(clientModel);
		List<ClientModel> modelList = new ArrayList<ClientModel>();
		for (Client client : list) {
			ClientModel clientModel2 = new ClientModel();
			BeanUtils.copyProperties(client, clientModel2);
			modelList.add(clientModel2);
		}
		dataGrid.setRows(modelList);
		return dataGrid;
	}

	public int addClient(ClientModel clientModel) {
		clientModel.setCreateTime(new Date());
		return clientMapper.addClient(clientModel);
	}

	public ClientModel selectById(int id) {
		Client client = clientMapper.selectById(id);
		ClientModel clientModel = new ClientModel();
		BeanUtils.copyProperties(client, clientModel);
		return clientModel;
	}

	public int updateClient(ClientModel clientModel) {
		return clientMapper.updateClient(clientModel);
	}

	public List<Client> selectClient(ClientModel clientModel) {
		return clientMapper.selectClient(clientModel);
	}

	@Override
	public Client selectNewOne() {
		return clientMapper.selectNewOne();
	}
}
