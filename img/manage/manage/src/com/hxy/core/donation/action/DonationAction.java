package com.hxy.core.donation.action;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.core.donation.entity.Donation;
import com.hxy.core.donation.service.DonationService;
import com.hxy.system.WebUtil;

@Namespace("/donation")
@Action(value = "donationAction", results = {
		@Result(name = "donateSave", location = "/donation/donationAction!doNotNeedSessionAndSecurity_donationConfirm.action", type = "redirect", params = {
				"id", "${id}" }), @Result(name = "donateConfirm", location = "/mobile/donate/donatePay.jsp"),
				@Result(name="donateHistoryDetail",location="/mobile/donate/donateHistoryDetail.jsp")})
public class DonationAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DonationAction.class);

	@Autowired
	private DonationService donationService;
	private Donation donation;
	private Date startTime;
	private Date endTime;
	private double startMoney;
	private double endMoney;
	private String departName;
	private String gradeName;
	private String className;
	private String schoolName;

	private int countMethod;

	private String accountNum;

	public Donation getDonation() {
		return donation;
	}

	public void setDonation(Donation donation) {
		this.donation = donation;
	}

	/** --捐赠管理列表数据查询-- **/
	public void dataGrid() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startMoney", startMoney);
		map.put("endMoney", endMoney);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		if (!WebUtil.isEmpty(schoolName)) {
			map.put("deptId", schoolName);
		}
		if (!WebUtil.isEmpty(departName)) {
			map.put("deptId", departName);
		}
		if (!WebUtil.isEmpty(gradeName)) {
			map.put("deptId", gradeName);
		}
		if (!WebUtil.isEmpty(className)) {
			map.put("deptId", className);
		}
		if (donation != null) {
			if (donation.getUserInfo() != null) {
				map.put("userName", donation.getUserInfo().getUserName());
				map.put("studentType", donation.getUserInfo().getStudentType());
			}
			map.put("projectId", donation.getProjectId());
			map.put("confirmStatus", donation.getConfirmStatus());
			map.put("payStatus", donation.getPayStatus());
			map.put("majorId", donation.getMajorId());
		}
		map.put("page", page);
		map.put("rows", rows);
		super.writeJson(donationService.dataGrid(map));
	}

	public String doNotNeedSessionAndSecurity_donationSave() {
		donationService.saveFromMobile(donation);
		id = donation.getDonationId();
		return "donateSave";
	}

	public String doNotNeedSessionAndSecurity_donationConfirm() {
		donation = donationService.selectById(id);
		return "donateConfirm";
	}

	public String doNotNeedSessionAndSecurity_getById() {
		donation = donationService.selectById(id);
		return "donateHistoryDetail";
	}

	public void doNotNeedSessionAndSecurity_listAll() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		map.put("accountNum", accountNum);
		super.writeJson(donationService.listAll(map));
	}
	
	public void doNotNeedSessionAndSecurity_listNew() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("rows", rows);
		map.put("accountNum", accountNum);
		super.writeJson(donationService.listNew(map));
	}

	/** --捐赠统计列表数据-- **/
	public void dataGridForCount() {
		Map<String, Object> map = new HashMap<String, Object>();

		if (!WebUtil.isEmpty(schoolName)) {
			map.put("deptId", schoolName);
		}
		if (!WebUtil.isEmpty(departName)) {
			map.put("deptId", departName);
		}
		if (!WebUtil.isEmpty(gradeName)) {
			map.put("deptId", gradeName);
		}
		if (!WebUtil.isEmpty(className)) {
			map.put("deptId", className);
		}

		if (donation != null) {
			if (donation.getUserInfo() != null) {
				map.put("studentType", donation.getUserInfo().getStudentType());
				map.put("sex", donation.getUserInfo().getSex());
				map.put("entranceTime", donation.getUserInfo().getEntranceTime());
			}
			map.put("projectId", donation.getProjectId());
			map.put("majorId", donation.getMajorId());
		}
		if (countMethod == 0) {
			// 默认按照学校统计
			countMethod = 4;
		}
		map.put("countMethod", countMethod);
		map.put("page", page);
		map.put("rows", rows);
		super.writeJson(donationService.dataGridForCount(map));
	}

	public void save() {
		Message message = new Message();
		try {
			if (donation.getConfirmStatus() == 1) {
				donation.setConfirmId(getUser().getUserId());
			}
			donationService.save(donation);
			message.setMsg("新增成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("新增失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void update() {
		Message message = new Message();
		try {
			donation.setConfirmId(getUser().getUserId());
			donation.setConfirmTime(new Date());
			donationService.update(donation);
			message.setMsg("修改成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("修改失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void delete() {
		Message message = new Message();
		try {
			donationService.delete(ids);
			message.setMsg("删除成功");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("删除失败");
			message.setSuccess(false);
		}
		super.writeJson(message);
	}

	public void getById() {
		super.writeJson(donationService.selectById(id));
	}

	public void doNotNeedSecurity_getById() {
		super.writeJson(donationService.selectById(id));
	}

	public DonationService getDonationService() {
		return donationService;
	}

	public void setDonationService(DonationService donationService) {
		this.donationService = donationService;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public double getStartMoney() {
		return startMoney;
	}

	public void setStartMoney(double startMoney) {
		this.startMoney = startMoney;
	}

	public double getEndMoney() {
		return endMoney;
	}

	public void setEndMoney(double endMoney) {
		this.endMoney = endMoney;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getCountMethod() {
		return countMethod;
	}

	public void setCountMethod(int countMethod) {
		this.countMethod = countMethod;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

}
