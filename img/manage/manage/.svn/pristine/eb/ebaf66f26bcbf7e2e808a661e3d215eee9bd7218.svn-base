package com.hxy.core.login.action;

import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.hxy.base.action.AdminBaseAction;
import com.hxy.base.entity.Message;
import com.hxy.base.entity.Tree;
import com.hxy.core.resource.entity.Resource;
import com.hxy.core.resource.service.ResourceService;
import com.hxy.core.role.entity.Role;
import com.hxy.core.role.service.RoleService;
import com.hxy.core.user.entity.User;
import com.hxy.core.user.service.UserService;
import com.hxy.system.GetDictionaryInfo;
import com.hxy.system.SecretUtil;

@Namespace("/login")
@Action("loginAction")
public class LoginAction extends AdminBaseAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(LoginAction.class);

	@Autowired
	private Producer captchaProducer;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResourceService resourceService;

	private String userAccount;

	private String userPassword;

	private String validCode;

	/**
	 * 登录
	 */
	public void doNotNeedSessionAndSecurity_login() {
		Message message = new Message();
		String code = (String) getRequest().getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (code == null) {
			message.setMsg("请刷新当前页面");
			message.setSuccess(false);
			super.writeJson(message);
			return;
		}
		if (!code.equalsIgnoreCase(validCode)) {
			message.setMsg("验证码错误");
			message.setSuccess(false);
		} else {
			User selectUser = userService.selectByUserAccount(this.userAccount);
			List<Resource> menus = new ArrayList<Resource>();
			List<Resource> grantTreeList = new ArrayList<Resource>();
			// 校友会角色授权时使用该list
			List<Resource> xgrantTreeList = new ArrayList<Resource>();
			if (selectUser == null) {
				message.setMsg("帐号错误");
				message.setSuccess(false);
			} else {
				if (!selectUser.getUserPassword().equals(SecretUtil.encryptToSHA(this.userPassword))) {
					message.setMsg("密码错误");
					message.setSuccess(false);
				} else {
					// 用户拥有的角色
					Role hasRole = selectUser.getRole();
					if (hasRole != null) {
						if (hasRole.getSystemAdmin() == 1) {
							Map<String, Object> map = GetDictionaryInfo.dictionaryInfoMap;
							@SuppressWarnings("unchecked")
							List<Resource> allList = (List<Resource>) map.get("resources");
							for (Resource resource : allList) {
								// 后台菜单
								if (resource.getType().equals("菜单") && resource.getFlag() == 0) {
									menus.add(resource);
								}
								if (resource.getFlag() == 0) {
									grantTreeList.add(resource);
								} else {
									xgrantTreeList.add(resource);
								}
							}
						} else {
							List<Role> roles2 = roleService.getMenu(hasRole.getRoleId());
							// 拥有的菜单
							for (Role role : roles2) {
								List<Resource> resources = role.getList();
								for (Resource resource : resources) {
									if (resource.getType().equals("菜单")) {
										menus.add(resource);
									}
									grantTreeList.add(resource);
								}
							}
						}
						getSession().put("xgrantTreeList", xgrantTreeList);
						getSession().put("grantTreeList", grantTreeList);
						getSession().put("menus", menus);
						getSession().put("user", selectUser);
						message.setSuccess(true);
					}
				}
			}
		}
		super.writeJson(message);
	}

	public void doNotNeedSessionAndSecurity_loginx() {
		Message message = new Message();
		String code = (String) getRequest().getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		if (code == null) {
			message.setMsg("请刷新当前页面");
			message.setSuccess(false);
			super.writeJson(message);
			return;
		}
		if (!code.equalsIgnoreCase(validCode)) {
			message.setMsg("验证码错误");
			message.setSuccess(false);
		} else {
			User selectUser = userService.selectByUserAccountx(this.userAccount);
			List<Resource> menus = new ArrayList<Resource>();
			List<Resource> grantTreeList = new ArrayList<Resource>();
			if (selectUser == null) {
				message.setMsg("帐号错误");
				message.setSuccess(false);
			} else {
				if (!selectUser.getUserPassword().equals(SecretUtil.encryptToSHA(this.userPassword))) {
					message.setMsg("密码错误");
					message.setSuccess(false);
				} else {
					// 用户拥有的角色
					Role hasRole = selectUser.getRole();
					if (hasRole != null) {
						if (hasRole.getSystemAdmin() == 1) {
							Map<String, Object> map = GetDictionaryInfo.dictionaryInfoMap;
							@SuppressWarnings("unchecked")
							List<Resource> allList = (List<Resource>) map.get("resources");
							for (Resource resource : allList) {
								// 后台菜单
								if (resource.getType().equals("菜单") && resource.getFlag() == 0) {
									menus.add(resource);
								}
								if (resource.getFlag() == 0) {
									grantTreeList.add(resource);
								}
							}
						} else {
							List<Role> roles2 = roleService.getMenu(hasRole.getRoleId());
							// 拥有的菜单
							for (Role role : roles2) {
								List<Resource> resources = role.getList();
								for (Resource resource : resources) {
									if ("菜单".equals(resource.getType())) {
										menus.add(resource);
									}
									grantTreeList.add(resource);
								}
							}
						}
						getSession().put("grantTreeList", grantTreeList);
						getSession().put("menus", menus);
						getSession().put("user", selectUser);
						message.setSuccess(true);
					}
				}
			}
		}
		super.writeJson(message);
	}

	/**
	 * 退出
	 */
	public void doNotNeedSessionAndSecurity_logout() {
		Message message = new Message();
		getSession().clear();
		message.setSuccess(true);
		super.writeJson(message);
	}

	/**
	 * 修改密码
	 */
	public void doNotNeedSecurity_updateCurrentPwd() {
		Message message = new Message();
		try {
			String pwd = getRequest().getParameter("pwd");
			User user = (User) getSession().get("user");
			user.setUserPassword(SecretUtil.encryptToSHA(pwd));
			userService.updatePassword(user);
			message.setMsg("密码修改成功!");
			message.setSuccess(true);
		} catch (Exception e) {
			logger.error(e, e);
			message.setMsg("密码修改失败!");
			message.setSuccess(false);
		}
		super.writeJson(message);

	}

	/**
	 * 验证码
	 */
	public void doNotNeedSessionAndSecurity_captchaImage() {
		try {
			getResponse().setDateHeader("Expires", 0);
			getResponse().setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			getResponse().addHeader("Cache-Control", "post-check=0, pre-check=0");
			getResponse().setHeader("Pragma", "no-cache");
			getResponse().setContentType("image/jpeg");

			String capText = captchaProducer.createText();

			getRequest().getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

			BufferedImage bi = captchaProducer.createImage(capText);
			ServletOutputStream out = getResponse().getOutputStream();
			ImageIO.write(bi, "jpg", out);
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 初始化后台菜单
	 * 
	 * @throws Exception
	 */
	public void doNotNeedSecurity_initMenu() {
		// 获取session中的菜单
		@SuppressWarnings("unchecked")
		List<Resource> menus = (List<Resource>) getSession().get("menus");
		if (menus != null && menus.size() > 0) {
			List<Tree> treeList = new ArrayList<Tree>();
			List<Tree> tree = new ArrayList<Tree>();
			for (Resource resource : menus) {
				if (resource.getType().equals("菜单")) {
					Tree node = new Tree();
					node.setId(resource.getId());
					node.setPid(resource.getPid());
					node.setText(resource.getName());
					node.setIconCls(resource.getIconCls());
					Map<String, String> attributes = new HashMap<String, String>();
					attributes.put("url", resource.getUrl());
					node.setAttributes(attributes);
					treeList.add(node);
				}
			}
			resourceService.parseTree(tree, treeList);
			super.writeJson(tree);
		} else {
			List<Tree> tree = new ArrayList<Tree>();
			super.writeJson(tree);
		}
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

}
