package com.hxy.core.donation.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hxy.base.entity.DataGrid;
import com.hxy.core.appuser.dao.AppUserMapper;
import com.hxy.core.appuser.entity.AppUser;
import com.hxy.core.donation.dao.DonationMapper;
import com.hxy.core.donation.entity.DonateHistory;
import com.hxy.core.donation.entity.Donation;
import com.hxy.core.donation.entity.MyDonation;
import com.hxy.core.donation.entity.NewDonate;
import com.hxy.core.donation.entity.NewDonateItem;
import com.hxy.core.userProfile.dao.UserProfileMapper;
import com.hxy.core.userProfile.entity.UserProfile;
import com.hxy.system.SystemUtil;

@Service("donationService")
public class DonationServiceImpl implements DonationService {
	@Autowired
	private DonationMapper donationMapper;

	@Autowired
	private UserProfileMapper userProfileMapper;
	@Autowired
	private AppUserMapper appUserMapper;

	public void save(Donation donation) {
		donation.setDonationTime(new Date());
		// if (donation.getUserId() != null && donation.getUserId().length() >
		// 0) {
		// UserInfo userInfo =
		// userInfoMapper.selectByUserId(donation.getUserId());
		// donation.setX_school(userInfo.getSchoolName());
		// donation.setX_depart(userInfo.getDepartName());
		// donation.setX_grade(userInfo.getGradeName());
		// donation.setX_clazz(userInfo.getClassName());
		// donation.setX_major(userInfo.getMajorName());
		// donation.setX_name(userInfo.getUserName());
		// donation.setX_sex(userInfo.getSex());
		// } else {
		// if (donation.getFlag() == 1) {
		// donation.setX_grade(donation.getX_grade() + "级");
		// }
		// }

		if (donation.getUserId() != null && donation.getUserId().length() > 0) {
			donation.setFlag(1);
		}

		donationMapper.save(donation);
	}

	public void update(Donation donation) {
		donationMapper.update(donation);
	}

	public void delete(String ids) {
		List<Long> list = new ArrayList<Long>();
		String[] idArray = ids.split(",");
		if (idArray != null) {
			for (String id : idArray) {
				list.add(Long.parseLong(id));
			}
		}
		donationMapper.delete(list);
	}

	public Donation selectById(long id) {
		return donationMapper.selectById(id);
	}

	public DataGrid<Donation> dataGrid(Map<String, Object> map) {
		DataGrid<Donation> dataGrid = new DataGrid<Donation>();
		long total = donationMapper.countDonation(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<Donation> list = donationMapper.selectDonationList(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	public DataGrid<Donation> dataGridForCount(Map<String, Object> map) {
		DataGrid<Donation> dataGrid = new DataGrid<Donation>();
		long total = donationMapper.countDonationForCount(map);
		dataGrid.setTotal(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<Donation> list = donationMapper.selectDonationForCountList(map);
		dataGrid.setRows(list);
		return dataGrid;
	}

	@Override
	public void updateFromShouXin(Donation donation) {
		donationMapper.updateFromShouXin(donation);
	}

	@Override
	public List<Donation> selectRandom50() {
		return donationMapper.selectRandom50();
	}

	@Override
	public List<Donation> selectByNameAndPhone(Donation donation) {
		return donationMapper.selectByNameAndPhone(donation);
	}

	@Override
	public void saveFromMobile(Donation donation) {
		// 根据accountNum获取用户信息
//		UserProfile userProfile = userProfileMapper.selectById(String.valueOf(donation.getAccountNum()));
//	
//		if (userProfile != null) {
//			donation.setDonationTime(new Date());
//			donation.setFlag(1);
//			donation.setOrderNo(SystemUtil.getOrderNo());
//			donation.setPayStatus(0);
//			donation.setX_email(userProfile.getEmail());
//			donation.setX_address(userProfile.getAddress());
//			donation.setX_position(userProfile.getPosition());
//			if (userProfile.getSex() != null && userProfile.getSex().equals("1")) {
//				donation.setX_sex("女");
//			}
//			if (userProfile.getSex() != null && userProfile.getSex().equals("0")) {
//				donation.setX_sex("男");
//			}
//			donation.setX_name(userProfile.getName());
//			donation.setX_phone(userProfile.getPhoneNum());
//			donation.setX_workunit(userProfile.getWorkUtil());
//			donation.setUserId(userProfile.getBaseInfoId());
//			donationMapper.save(donation);
//		}
		
		// 根据accountNum获取用户信息
		AppUser appuser = appUserMapper.getByUserId(Long.toString(donation.getAccountNum()));
		if (appuser != null) {
			donation.setDonationTime(new Date());
			donation.setFlag(1);
			donation.setOrderNo(SystemUtil.getOrderNo());
			donation.setPayStatus(0);
			donation.setX_email(appuser.getUser_email());
//			donation.setX_address(appuser.getAddress());
//			donation.setX_position(appuser.getPosition());
//			if (appuser.getUser_sex() != null && appuser.getUser_sex().equals("1")) {
			if (appuser.getUser_sex() == 0 ) {
				donation.setX_sex("女");
			}
			if (appuser.getUser_sex() == 1 ) {
				donation.setX_sex("男");
			}
			donation.setX_name(appuser.getUser_name());
			donation.setX_phone(appuser.getUser_mobile());
			donation.setX_workunit(appuser.getUser_work_unit());
			donation.setUserId(appuser.getUser_id());
			donationMapper.save(donation);
		}
	}

	@Override
	public MyDonation listAll(Map<String, Object> map) {
		MyDonation myDonation = new MyDonation();
		long total = donationMapper.countDonationForMobile(map);
		myDonation.setCountDonateHistory(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<DonateHistory> donateHistories = new ArrayList<DonateHistory>();
		List<Donation> list = donationMapper.selectDonationForCountMobile(map);
		if (list != null) {
			for (Donation donation : list) {
				DonateHistory donateHistory = new DonateHistory();
				donateHistory.setConfirmTime(donation.getDonationTime());
				donateHistory.setMoney(donation.getMoney());
				donateHistory.setPayStatus(donation.getPayStatus());
				if (donation.getProject() != null) {
					donateHistory.setProjectName(donation.getProject().getProjectName());
				}
				donateHistory.setDonateUrl("../../donation/donationAction!doNotNeedSessionAndSecurity_getById.action?id=" + donation.getDonationId()
						+ "&accountNum=" + donation.getAccountNum());
				donateHistories.add(donateHistory);
			}
		}
		myDonation.setDonateHistoryList(donateHistories);
		return myDonation;
	}

	@Override
	public NewDonate listNew(Map<String, Object> map) {
		NewDonate newDonate = new NewDonate();
		long total = donationMapper.countDonationForMobileNew(map);
		newDonate.setCountDonateList(total);
		int start = ((Integer) map.get("page") - 1) * (Integer) map.get("rows");
		map.put("start", start);
		List<NewDonateItem> newDonateItems = new ArrayList<NewDonateItem>();
		List<Donation> list = donationMapper.selectDonationForCountMobileNew(map);
		if (list != null) {
			for (Donation donation : list) {
				NewDonateItem newDonateItem = new NewDonateItem();
				if (donation.getAnonymous() == 1) {
					newDonateItem.setX_name("匿名");
				} else {
					newDonateItem.setX_name(donation.getX_name());
				}
				newDonateItem.setMoney(donation.getMoney());
				if (donation.getProject() != null) {
					newDonateItem.setProjectName(donation.getProject().getProjectName());
				}
				newDonateItem.setDonateItemUrl("../../project/projectAction!doNotNeedSessionAndSecurity_getById.action?id=" + donation.getProjectId()
						+ "&accountNum=" + map.get("accountNum"));
				newDonateItems.add(newDonateItem);
			}
		}
		newDonate.setNewDonateList(newDonateItems);
		return newDonate;
	}

	@Override
	public Donation selectByOrderNo(String orderNo) {
		return donationMapper.selectByOrderNo(orderNo);
	}
}
