package com.hxy.servlet;

import org.apache.log4j.Logger;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.hxy.system.GetDictionaryInfo;

public class InitDictionaryInfoServlet extends HttpServlet
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(InitDictionaryInfoServlet.class);

	private static final long serialVersionUID = 1L;

	private ServletContext context;

	@Override
	public void init() throws ServletException
	{
		GetDictionaryInfo.servletContext = this.getServletContext();
		context = this.getServletContext();
		Map<String, Object> dictionaryInfoMap = GetDictionaryInfo.dictionaryInfoMap;
		Map<String, Object> authorityMap = GetDictionaryInfo.authorityMap;
		logger.info("##############################字典MAP里的数据长度为" + dictionaryInfoMap.size() + "##############################");
		context.setAttribute("dictionaryInfoMap", dictionaryInfoMap);
		context.setAttribute("authorityMap", authorityMap);
		context.setAttribute("menuUrlMap", GetDictionaryInfo.menuUrlMap);
		logger.info("##############################系统字典表数据加载成功##############################:");
		String userAccount = getInitParameter("userAccount");
		String passWord = getInitParameter("passWord");
		context.setAttribute("userAccount", userAccount);
		context.setAttribute("passWord", passWord);
	}

}
