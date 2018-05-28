package com.hxy.system;

/**
 * 系统属性文件配置实体类
 * @author Administrator
 *
 */
public class ConfigProperties {
	
	
	private String disk_path;
	private String linux_url_domain;
	private String win_url_domain;
	private String sign;
	private String schoolId;
	private String schoolSign;
	private String deptNo;
	private String tigase_admin_account;
	private String tigase_admin_passwd;
	private String tigase_server_ip;
	private int tigase_server_port;
	private String tigase_db_ip;
	private int tigase_db_port;
	private String tigase_db_user;
	private String tigase_db_password;
	private int tigase_db_conn_size;
	private String tigase_db_name;
	private String activemq_server_ip;
	private int activemq_server_port;
	private String activemq_connection_admin_user;
	private String activemq_connection_admin_password;
	private String activemq_server_general_channel_name;
	private String tigase_server_domain;
	private String client_release_checkcode;
	private String phone_secret_key;
	private String authenticated_secret_key;
	private String FACE_FILE_DIR;
	private String REGULAR_CHANNEL_1;
	private String REGULAR_CHANNEL_2;
	private String REGULAR_CHANNEL_3;
	private String DOWNLOAD_APP_URL;
	private String smtpHost;
	private String smtpPort;
	private String email_account;
	private String email_password;
	
	
	public String getDisk_path() {
		return disk_path;
	}
	public void setDisk_path(String disk_path) {
		this.disk_path = disk_path;
	}
	public String getLinux_url_domain() {
		return linux_url_domain;
	}
	public void setLinux_url_domain(String linux_url_domain) {
		this.linux_url_domain = linux_url_domain;
	}
	public String getWin_url_domain() {
		return win_url_domain;
	}
	public void setWin_url_domain(String win_url_domain) {
		this.win_url_domain = win_url_domain;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolSign() {
		return schoolSign;
	}
	public void setSchoolSign(String schoolSign) {
		this.schoolSign = schoolSign;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getTigase_admin_account() {
		return tigase_admin_account;
	}
	public void setTigase_admin_account(String tigase_admin_account) {
		this.tigase_admin_account = tigase_admin_account;
	}
	public String getTigase_admin_passwd() {
		return tigase_admin_passwd;
	}
	public void setTigase_admin_passwd(String tigase_admin_passwd) {
		this.tigase_admin_passwd = tigase_admin_passwd;
	}
	public String getTigase_server_ip() {
		return tigase_server_ip;
	}
	public void setTigase_server_ip(String tigase_server_ip) {
		this.tigase_server_ip = tigase_server_ip;
	}
	public int getTigase_server_port() {
		return tigase_server_port;
	}
	public void setTigase_server_port(int tigase_server_port) {
		this.tigase_server_port = tigase_server_port;
	}
	public String getTigase_db_ip() {
		return tigase_db_ip;
	}
	public void setTigase_db_ip(String tigase_db_ip) {
		this.tigase_db_ip = tigase_db_ip;
	}
	public int getTigase_db_port() {
		return tigase_db_port;
	}
	public void setTigase_db_port(int tigase_db_port) {
		this.tigase_db_port = tigase_db_port;
	}
	public String getTigase_db_user() {
		return tigase_db_user;
	}
	public void setTigase_db_user(String tigase_db_user) {
		this.tigase_db_user = tigase_db_user;
	}
	public String getTigase_db_password() {
		return tigase_db_password;
	}
	public void setTigase_db_password(String tigase_db_password) {
		this.tigase_db_password = tigase_db_password;
	}
	public int getTigase_db_conn_size() {
		return tigase_db_conn_size;
	}
	public void setTigase_db_conn_size(int tigase_db_conn_size) {
		this.tigase_db_conn_size = tigase_db_conn_size;
	}
	public String getTigase_db_name() {
		return tigase_db_name;
	}
	public void setTigase_db_name(String tigase_db_name) {
		this.tigase_db_name = tigase_db_name;
	}
	public String getActivemq_server_ip() {
		return activemq_server_ip;
	}
	public void setActivemq_server_ip(String activemq_server_ip) {
		this.activemq_server_ip = activemq_server_ip;
	}
	public int getActivemq_server_port() {
		return activemq_server_port;
	}
	public void setActivemq_server_port(int activemq_server_port) {
		this.activemq_server_port = activemq_server_port;
	}
	public String getActivemq_connection_admin_user() {
		return activemq_connection_admin_user;
	}
	public void setActivemq_connection_admin_user(
			String activemq_connection_admin_user) {
		this.activemq_connection_admin_user = activemq_connection_admin_user;
	}
	public String getActivemq_connection_admin_password() {
		return activemq_connection_admin_password;
	}
	public void setActivemq_connection_admin_password(
			String activemq_connection_admin_password) {
		this.activemq_connection_admin_password = activemq_connection_admin_password;
	}
	public String getActivemq_server_general_channel_name() {
		return activemq_server_general_channel_name;
	}
	public void setActivemq_server_general_channel_name(
			String activemq_server_general_channel_name) {
		this.activemq_server_general_channel_name = activemq_server_general_channel_name;
	}
	public String getTigase_server_domain() {
		return tigase_server_domain;
	}
	public void setTigase_server_domain(String tigase_server_domain) {
		this.tigase_server_domain = tigase_server_domain;
	}
	public String getClient_release_checkcode() {
		return client_release_checkcode;
	}
	public void setClient_release_checkcode(String client_release_checkcode) {
		this.client_release_checkcode = client_release_checkcode;
	}
	public String getPhone_secret_key() {
		return phone_secret_key;
	}
	public void setPhone_secret_key(String phone_secret_key) {
		this.phone_secret_key = phone_secret_key;
	}
	public String getAuthenticated_secret_key() {
		return authenticated_secret_key;
	}
	public void setAuthenticated_secret_key(String authenticated_secret_key) {
		this.authenticated_secret_key = authenticated_secret_key;
	}
	public String getFACE_FILE_DIR() {
		return FACE_FILE_DIR;
	}
	public void setFACE_FILE_DIR(String face_file_dir) {
		FACE_FILE_DIR = face_file_dir;
	}
	public String getREGULAR_CHANNEL_1() {
		return REGULAR_CHANNEL_1;
	}
	public void setREGULAR_CHANNEL_1(String regular_channel_1) {
		REGULAR_CHANNEL_1 = regular_channel_1;
	}
	public String getREGULAR_CHANNEL_2() {
		return REGULAR_CHANNEL_2;
	}
	public void setREGULAR_CHANNEL_2(String regular_channel_2) {
		REGULAR_CHANNEL_2 = regular_channel_2;
	}
	public String getREGULAR_CHANNEL_3() {
		return REGULAR_CHANNEL_3;
	}
	public void setREGULAR_CHANNEL_3(String regular_channel_3) {
		REGULAR_CHANNEL_3 = regular_channel_3;
	}
	public String getDOWNLOAD_APP_URL() {
		return DOWNLOAD_APP_URL;
	}
	public void setDOWNLOAD_APP_URL(String download_app_url) {
		DOWNLOAD_APP_URL = download_app_url;
	}
	
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public String getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String getEmail_account() {
		return email_account;
	}
	public void setEmail_account(String email_account) {
		this.email_account = email_account;
	}
	public String getEmail_password() {
		return email_password;
	}
	public void setEmail_password(String email_password) {
		this.email_password = email_password;
	}
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\""+"disk_path"+"\""+":"+"\""+disk_path+"\",");
		buffer.append("\""+"linux_url_domain"+"\""+":"+"\""+linux_url_domain+"\",");
		buffer.append("\""+"win_url_domain"+"\""+":"+"\""+win_url_domain+"\",");
		buffer.append("\""+"sign"+"\""+":"+"\""+sign+"\",");
		buffer.append("\""+"schoolId"+"\""+":"+"\""+schoolId+"\",");
		buffer.append("\""+"schoolSign"+"\""+":"+"\""+schoolSign +"\",");
		buffer.append("\""+"deptNo"+"\""+":"+"\""+deptNo+"\",");
		buffer.append("\""+"tigase_admin_account"+"\""+":"+"\""+tigase_admin_account+"\",");
		buffer.append("\""+"tigase_admin_passwd"+"\""+":"+"\""+tigase_admin_passwd+"\",");
		buffer.append("\""+"tigase_server_ip"+"\""+":"+"\""+tigase_server_ip+"\",");
		buffer.append("\""+"tigase_server_port"+"\""+":"+"\""+tigase_server_port+"\",");
		buffer.append("\""+"tigase_server_domain"+"\""+":"+"\""+tigase_server_domain+"\",");
		buffer.append("\""+"tigase_db_ip"+"\""+":"+"\""+tigase_db_ip+"\",");
		buffer.append("\""+"tigase_db_port"+"\""+":"+"\""+tigase_db_port+"\",");
		buffer.append("\""+"tigase_db_user"+"\""+":"+"\""+tigase_db_user+"\",");
		buffer.append("\""+"tigase_db_password"+"\""+":"+"\""+tigase_db_password+"\",");
		buffer.append("\""+"tigase_db_conn_size"+"\""+":"+"\""+tigase_db_conn_size+"\",");
		buffer.append("\""+"tigase_db_name"+"\""+":"+"\""+tigase_db_name+"\",");
		buffer.append("\""+"activemq_server_ip"+"\""+":"+"\""+activemq_server_ip+"\",");
		buffer.append("\""+"activemq_server_port"+"\""+":"+"\""+activemq_server_port+"\",");
		buffer.append("\""+"activemq_connection_admin_user"+"\""+":"+"\""+activemq_connection_admin_user+"\",");
		buffer.append("\""+"activemq_connection_admin_password"+"\""+":"+"\""+activemq_connection_admin_password+"\",");
		buffer.append("\""+"activemq_server_general_channel_name"+"\""+":"+"\""+activemq_server_general_channel_name+"\",");
		buffer.append("\""+"client_release_checkcode"+"\""+":"+"\""+client_release_checkcode+"\",");
		buffer.append("\""+"phone_secret_key"+"\""+":"+"\""+phone_secret_key+"\",");
		buffer.append("\""+"authenticated_secret_key"+"\""+":"+"\""+authenticated_secret_key+"\",");
		buffer.append("\""+"FACE_FILE_DIR"+"\""+":"+"\""+FACE_FILE_DIR+"\",");
		buffer.append("\""+"REGULAR_CHANNEL_1"+"\""+":"+"\""+REGULAR_CHANNEL_1+"\",");
		buffer.append("\""+"REGULAR_CHANNEL_2"+"\""+":"+"\""+REGULAR_CHANNEL_2+"\",");
		buffer.append("\""+"REGULAR_CHANNEL_3"+"\""+":"+"\""+REGULAR_CHANNEL_3+"\",");
		buffer.append("\""+"DOWNLOAD_APP_URL"+"\""+":"+"\""+DOWNLOAD_APP_URL+"\",");
		buffer.append("\""+"smtpHost"+"\""+":"+"\""+smtpHost+"\",");
		buffer.append("\""+"smtpPort"+"\""+":"+"\""+smtpPort+"\",");
		buffer.append("\""+"email_account"+"\""+":"+"\""+email_account+"\",");
		buffer.append("\""+"email_password"+"\""+":"+"\""+email_password+"\"");
		buffer.append("}");
		return buffer.toString();
	}

}
