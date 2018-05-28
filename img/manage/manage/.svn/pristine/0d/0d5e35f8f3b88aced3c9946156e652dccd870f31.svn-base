<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<jsp:include page="../../../inc.jsp"></jsp:include>
</head>
  
  <body>
<form method="post">
	<table class="ta001">
		<input name="newsTag.tagId" type="hidden" id="tagId"
			value="${newsTag.tagId}">
		<tr>
			<th>
				频道名称
			</th>
			<td colspan="3">
				<input name="newsTag.tagName" class="easyui-validatebox"
					disabled="disabled"
					value="${newsTag.tagName}" />
			</td>
		</tr>
		<tr>
			<th>
				频道简介
			</th>
			<td colspan="3">
				<textarea name="newsTag.tagRemark" rows="6" cols="95" disabled="disabled">${newsTag.tagRemark}</textarea>
			</td>
		</tr>
		<tr>
			<th>
				所属院系/组织
			</th>
			<td colspan="3">
				${newsTag.departmentName}
			</td>
		</tr>
		<tr>
			<th>
				频道图标
			</th>
			<td colspan="3">
				<div id="pic">
					<c:if test="${newsTag.tagIcon!=null and newsTag.tagIcon!=''}">
						<div style="float:left;width:180px;"><img src="${newsTag.tagIconUrl}" width="150px" height="150px"/><div class="bb001"></div><input type="hidden" name="newsTag.tagIcon" value="${newsTag.tagIcon}"/></div>
					</c:if>
				</div>
			</td>
		</tr>
	</table>
</form>
  </body>
</html>