package com.hxy.tag;

import org.apache.log4j.Logger;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.hxy.core.role.entity.Role;
import com.hxy.system.GetDictionaryInfo;

public class AuthorityTag extends TagSupport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AuthorityTag.class);

	private static final long serialVersionUID = 1L;

	private Role role;
	private String authorizationCode;

	@Override
	public int doStartTag() throws JspException {
		boolean flag = false;
		if (role.getSystemAdmin() == 1) {
			flag = true;
		} else {
			@SuppressWarnings("unchecked")
			List<Role> list = (List<Role>) GetDictionaryInfo.authorityMap.get(authorizationCode);
			if (list != null && list.size() > 0) {
				logger.info("授权码拥有的角色" + list.toString());
				logger.info("当前角色" + role.toString());
				for (Role role : list) {
					if (role.getRoleName().equals(this.role.getRoleName())) {
						flag = true;
						break;
					}
				}
			}
		}
		if (flag) {
			return EVAL_BODY_INCLUDE;
		} else {
			return super.doStartTag();
		}
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

}
