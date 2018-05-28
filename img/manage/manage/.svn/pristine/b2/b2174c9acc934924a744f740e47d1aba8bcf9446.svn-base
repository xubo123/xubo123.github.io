<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.hxy.core.user.entity.User" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String url = "";
	User loginUser = (User)request.getSession().getAttribute("user");
	if(loginUser.getRole().getSystemAdmin()==1) {
		url = basePath + "page/admin/community/memberList.jsp";
	} else {
		url = basePath + "page/admin/community/viewMember.jsp";
	}
	
	response.sendRedirect(url);
%>

