package com.hxy.core.donation.service;

import java.util.List;
import java.util.Map;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.donation.entity.Donation;
import com.hxy.core.donation.entity.MyDonation;
import com.hxy.core.donation.entity.NewDonate;

public interface DonationService {
	void save(Donation donation);
	
	void saveFromMobile(Donation donation);

	void update(Donation donation);

	void delete(String ids);

	Donation selectById(long id);

	DataGrid<Donation> dataGrid(Map<String, Object> map);
	
	DataGrid<Donation> dataGridForCount(Map<String, Object> map);
	
	void updateFromShouXin(Donation donation);
	
	List<Donation> selectRandom50();
	
	List<Donation> selectByNameAndPhone(Donation donation);
	
	MyDonation listAll(Map<String, Object> map);
	
	NewDonate listNew(Map<String, Object> map);
	
	Donation selectByOrderNo(String orderNo);
}
