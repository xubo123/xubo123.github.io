package com.hxy.system;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hxy.alipay.config.AlipayConfig;
import com.hxy.core.dept.dao.DeptMapper;
import com.hxy.core.dept.entity.Dept;
import com.hxy.core.dicttype.dao.DictTypeMapper;
import com.hxy.core.dicttype.entity.DictType;
import com.hxy.core.resource.dao.ResourceMapper;
import com.hxy.core.resource.entity.Resource;
import com.hxy.core.role.dao.RoleMapper;
import com.hxy.core.role.entity.Role;
import com.hxy.core.systemsetting.dao.SystemSettingMapper;
import com.hxy.core.systemsetting.entity.SystemSetting;
import com.hxy.core.user.dao.UserMapper;
import com.hxy.core.user.entity.User;
import com.hxy.util.WebUtil;

@Component("getDictionaryInfo")
public class GetDictionaryInfo {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(GetDictionaryInfo.class);

	@Autowired
	private ResourceMapper resourceMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DictTypeMapper dictTypeMapper;

	@Autowired
	private DeptMapper deptMapper;

	@Autowired
	private SystemSettingMapper systemSettingMapper;

	private static WebApplicationContext springContext;
	public static ServletContext servletContext;// 这个是为了取servletContext
	private static GetDictionaryInfo instance;

	public static Map<String, Object> dictionaryInfoMap = new ConcurrentHashMap<String, Object>();

	public static Map<String, Object> authorityMap = new ConcurrentHashMap<String, Object>();

	/** --功能菜单和URL的映射-- **/
	public static Map<String, Resource> menuUrlMap = new ConcurrentHashMap<String, Resource>();

	public static GetDictionaryInfo getInstance() {
		springContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		if (null == instance)
			instance = (GetDictionaryInfo) springContext
					.getBean("getDictionaryInfo");
		return instance;

	}

	public void getAllInfo() {
		loadDict();
		initDB();
		initSystem();
	}

	public void loadDict() {
		logger.info("##############################	     系统启动加载	##############################");
		logger.info("##############################开始从数据库中取数据##############################");
		List<Resource> resources = resourceMapper.selectAll();
		logger.info("##############################资源数据大小" + resources.size()
				+ "##############################");
		if (resources != null && resources.size() > 0) {
			for (Resource resource : resources) {
				if (!WebUtil.isEmpty(resource.getUrl())) {
					authorityMap.put(resource.getUrl().trim(),
							resource.getRoles());

					authorityMap.put(resource.getName().trim(),
							resource.getRoles());
					menuUrlMap.put(resource.getUrl().trim(), resource);
				}
			}
		}
		logger.info("##############################权限MAP大小"
				+ authorityMap.size() + "##############################");
		logger.info("##############################数据从数据库中全部取出##############################");
		dictionaryInfoMap.put("resources", resources);
		logger.info("##############################数据打包完毕，全部放进了dictionaryInfoMap##############################");
		// 取出所有字典
		List<DictType> dictTypeList = dictTypeMapper.selectAll();
		dictionaryInfoMap.put("dicts", dictTypeList);
	}

	public void initDB() {
		// 超级管理员角色是否存在
		Role role = roleMapper.selectSystemAdmin();
		if (role == null) {
			role = new Role();
			role.setRoleName("超级管理员");
			role.setSystemAdmin(1);
			role.setFlag(0);
			roleMapper.add(role);
		}
		// 超级管理员帐号是否存在
		User user = userMapper.selectAdminUser();
		if (user == null) {
			user = new User();
			user.setUserAccount("system");
			user.setUserName("超级管理员");
			user.setUserPassword(SecretUtil.encryptToSHA("cykjqwer"));
			user.setFlag(0);
			user.setRoleId(role.getRoleId());
			userMapper.save(user);
		}
		// 初始化学校名称
		String deptNo = Global.deptNo;
		String schoolSign = Global.schoolSign;
		String schoolName = "";
		if (!WebUtil.isEmpty(deptNo) && !WebUtil.isEmpty(schoolSign)) {
			schoolName = schoolSign.length() > 4 ? schoolSign.substring(0,
					schoolSign.length() - 4) : schoolSign;
			Dept dept = this.deptMapper.getById(Global.deptNo);
			if (dept == null) {
				dept = new Dept();
				dept.setDeptId(deptNo);
				dept.setDeptName(schoolName);
				dept.setFullName(schoolName);
				dept.setCreateTime(new Date());
				dept.setParentId("0");
				dept.setLevel(4);
				deptMapper.insert(dept);
			}
		}

	}

	public void initSystem() {
		List<SystemSetting> list = systemSettingMapper.selectAll();
		if (list != null && list.size() > 0) {
			SystemSetting systemSetting = list.get(0);
			Global.sign = systemSetting.getDownload_app_url();
			Global.smtpHost = systemSetting.getSmtpHost();
			Global.smtpPort = systemSetting.getSmtpPort();
			Global.email_account = systemSetting.getEmail_account();
			Global.email_password = systemSetting.getEmail_password();
			AlipayConfig.partner = systemSetting.getPartner();
			AlipayConfig.seller_email = systemSetting.getSeller_email();
			AlipayConfig.key = systemSetting.getKey();
			AlipayConfig.notify_url = systemSetting.getNotify_url();
			AlipayConfig.return_url = systemSetting.getReturn_url();
			AlipayConfig.exter_invoke_ip = systemSetting.getExter_invoke_ip();
			com.hxy.wapalipay.config.AlipayConfig.partner = systemSetting
					.getPartner();
			com.hxy.wapalipay.config.AlipayConfig.seller_email = systemSetting
					.getSeller_email();
			com.hxy.wapalipay.config.AlipayConfig.key = systemSetting.getKey();
			com.hxy.wapalipay.config.AlipayConfig.private_key = systemSetting
					.getPrivate_key();
			com.hxy.wapalipay.config.AlipayConfig.ali_public_key = systemSetting
					.getWap_public_key();
			com.hxy.wapalipay.config.AlipayConfig.merchant_url = systemSetting
					.getWap_merchant_url();
			com.hxy.wapalipay.config.AlipayConfig.notify_url = systemSetting
					.getWap_notify_url();
			com.hxy.wapalipay.config.AlipayConfig.return_url = systemSetting
					.getWap_return_url();
			Global.userAccount = systemSetting.getSmsAccount();
			Global.password = systemSetting.getSmsPassword();
			Global.smsUrl = systemSetting.getSmsUrl();
			Global.sendType = systemSetting.getSendType();
			Global.smsBirthdayTemplate = systemSetting.getSmsBirthdayTemplate();
			Global.smsCodeTemplate = systemSetting.getSmsCodeTemplate();
			Global.smsVisitTemplate = systemSetting.getSmsVisitTemplate();
		}
	}

	public void reloadDictionaryInfoMap() {
		logger.info("##############################系统开始重新加载字典表##############################");
		dictionaryInfoMap.clear();
		authorityMap.clear();
		menuUrlMap.clear();
		logger.info("##############################字典表MAP,清空成功##############################");
		loadDict();
		logger.info("##############################重新加载字典表成功##############################");
	}

	public static Map<String, Object> getDictionaryInfoMap() {
		return dictionaryInfoMap;
	}

	public static void setDictionaryInfoMap(
			Map<String, Object> dictionaryInfoMap) {
		GetDictionaryInfo.dictionaryInfoMap = dictionaryInfoMap;
	}

}
