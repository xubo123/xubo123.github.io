package com.hxy.core.donation.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.donation.entity.Donation;

public interface DonationMapper {
	List<Donation> selectDonationList(Map<String, Object> map);

	long countDonation(Map<String, Object> map);
	
	List<Donation> selectDonationForCountList(Map<String, Object> map);

	long countDonationForCount(Map<String, Object> map);

	void save(Donation donation);

	void update(Donation donation);

	void delete(List<Long> list);
	
	Donation selectById(long id);
	
	void updateFromShouXin(Donation donation);
	
	List<Donation> selectRandom50();
	
	List<Donation> selectByNameAndPhone(Donation donation);
	
	long countDonationForMobile(Map<String, Object> map);
	
	long countDonationForMobileNew(Map<String, Object> map);
	
	List<Donation> selectDonationForCountMobile(Map<String, Object> map);
	
	List<Donation> selectDonationForCountMobileNew(Map<String, Object> map);
	
	Donation selectByOrderNo(String orderNo);

}
