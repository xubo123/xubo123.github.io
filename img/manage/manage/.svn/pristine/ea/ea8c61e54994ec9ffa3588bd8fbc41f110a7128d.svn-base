package com.hxy.listener;

import java.util.*;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import com.alibaba.druid.filter.config.ConfigTools;
import com.hxy.system.ConfigProperties;
import com.hxy.system.DESPlus;
import com.hxy.system.Global;
import com.hxy.system.InitDB;
import com.hxy.util.WebUtil;

public class SystemListener implements ServletContextListener {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SystemListener.class);

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}

	/** --容器初始化-- **/
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("config");
			String os = System.getProperty("os.name");
			// 无法从配置中心得到参数，读本地
			setProperties2(servletContextEvent, os, bundle);
			// web数据库初始化
			String jdbc_url = bundle.getString("jdbc_url");
			String jdbc_username = bundle.getString("jdbc_username");
			String jdbc_password = ConfigTools.decrypt(bundle.getString("jdbc_password"));
			InitDB.initDB(jdbc_url, jdbc_username, jdbc_password);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/** --设置属性文件的内容,从本地读取-- **/
	public void setProperties2(ServletContextEvent servletContextEvent, String os, ResourceBundle bundles) throws Exception {
		// 以下为属性文件的字段
		ConfigProperties config = new ConfigProperties();
		config.setDisk_path(bundles.getString("disk_path"));
		config.setLinux_url_domain(bundles.getString("linux_url_domain"));
		config.setWin_url_domain(bundles.getString("win_url_domain"));
		config.setSchoolId(bundles.getString("schoolId"));
		config.setSchoolSign(bundles.getString("schoolSign"));
		config.setDeptNo(bundles.getString("deptNo"));
		config.setActivemq_server_ip(bundles.getString("activemq_server_ip"));
		config.setActivemq_server_port(WebUtil.toInt(bundles.getString("activemq_server_port")));
		config.setActivemq_connection_admin_user(bundles.getString("activemq_connection_admin_user"));
		config.setActivemq_connection_admin_password(bundles.getString("activemq_connection_admin_password"));
		config.setActivemq_server_general_channel_name(bundles.getString("activemq_server_general_channel_name"));
		config.setTigase_server_domain(bundles.getString("tigase_server_domain"));
		config.setTigase_db_ip(bundles.getString("tigase_db_ip"));
		config.setTigase_db_port(WebUtil.toInt(bundles.getString("tigase_db_port")));
		config.setTigase_db_user(bundles.getString("tigase_db_user"));
		config.setTigase_db_password(bundles.getString("tigase_db_password"));
		config.setTigase_db_conn_size(WebUtil.toInt(bundles.getString("tigase_db_conn_size")));
		config.setTigase_db_name(bundles.getString("tigase_db_name"));
		config.setClient_release_checkcode(bundles.getString("client_release_checkcode"));
		config.setAuthenticated_secret_key(bundles.getString("authenticated_secret_key"));
		config.setPhone_secret_key(bundles.getString("phone_secret_key"));
		config.setREGULAR_CHANNEL_1(bundles.getString("REGULAR_CHANNEL_1"));
		config.setREGULAR_CHANNEL_2(bundles.getString("REGULAR_CHANNEL_2"));
		config.setREGULAR_CHANNEL_3(bundles.getString("REGULAR_CHANNEL_3"));
		// 密码数据解密
		this.setGlobalValue(servletContextEvent, os, config);
		logger.info("##############################加载本地项目的属性文件成功##############################");
	}

	/** --Global赋值,密码数据解密-- **/
	private void setGlobalValue(ServletContextEvent servletContextEvent, String os, ConfigProperties config) throws Exception {
		// 密码数据解密
		DESPlus dESPlus = DESPlus.getInstance();
		Global.schoolId = config.getSchoolId();
		Global.schoolSign = config.getSchoolSign();
		Global.deptNo = config.getDeptNo();
		Global.activemq_server_ip = config.getActivemq_server_ip();
		Global.activemq_server_port = config.getActivemq_server_port();
		Global.activemq_connection_admin_user = config.getActivemq_connection_admin_user();
		Global.activemq_connection_admin_password = dESPlus.decrypt(config.getActivemq_connection_admin_password());
		Global.activemq_server_general_channel_name = config.getActivemq_server_general_channel_name();
		Global.tigase_server_domain = config.getTigase_server_domain();
		Global.tigase_db_ip = config.getTigase_db_ip();
		Global.tigase_db_port = config.getTigase_db_port();
		Global.tigase_db_user = config.getTigase_db_user();
		Global.tigase_db_password = dESPlus.decrypt(config.getTigase_db_password());
		Global.tigase_db_conn_size = config.getTigase_db_conn_size();
		Global.tigase_db_name = config.getTigase_db_name();
		Global.client_release_checkcode = config.getClient_release_checkcode();
		Global.authenticated_secret_key = config.getAuthenticated_secret_key();
		Global.phone_secret_key = config.getPhone_secret_key();
		Global.REGULAR_CHANNEL_1 = config.getREGULAR_CHANNEL_1();
		Global.REGULAR_CHANNEL_2 = config.getREGULAR_CHANNEL_2();
		Global.REGULAR_CHANNEL_3 = config.getREGULAR_CHANNEL_3();
		if (os.startsWith("win") || os.startsWith("Win")) {
			Global.DISK_PATH = System.getProperty("user.dir").substring(0, System.getProperty("user.dir").lastIndexOf("bin")) + "webapps/";
			Global.URL_DOMAIN = config.getWin_url_domain();
			servletContextEvent.getServletContext().setAttribute("URL_DOMAIN", config.getWin_url_domain());
		}
		if (os.startsWith("linux") || os.startsWith("Linux")) {
			Global.DISK_PATH = config.getDisk_path();
			Global.URL_DOMAIN = config.getLinux_url_domain();
			servletContextEvent.getServletContext().setAttribute("URL_DOMAIN", config.getLinux_url_domain());
		}
		servletContextEvent.getServletContext().setAttribute("schoolSign", config.getSchoolSign());
	}
}
