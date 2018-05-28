<%@ page language="java" import="java.util.*,com.hxy.util.WebUtil" pageEncoding="UTF-8"%>
<%@ page import="com.hxy.web.news.service.WebNewsService"%>
<%@ page import="com.hxy.system.SpringManager"%>
<%@ page import="com.hxy.web.news.entity.WebNews"%>
<%@ page import="com.hxy.web.news.entity.WebNewsType"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String categoryWeb = request.getParameter("categoryWeb");
String origin = request.getParameter("origin");

if(WebUtil.isEmpty(categoryWeb))
{
	categoryWeb = "0";
}

if(WebUtil.isEmpty(origin))
{
	origin = "1";
}



WebNewsType webNewsType = new WebNewsType();
webNewsType.setId(Long.parseLong(categoryWeb));

WebNewsService service = SpringManager.getBean("webNewsService", WebNewsService.class);

WebNewsType newsType = service.getWebNewsTypeById(webNewsType);

webNewsType = new WebNewsType();
webNewsType.setId(Long.parseLong(categoryWeb));
List<WebNewsType> newsTypeParentsList = service.getWebParentsByTypeId(webNewsType);

long parentId = 0;
String currentTypeName = "";
if(!WebUtil.isEmpty(newsTypeParentsList))
{
	if(newsTypeParentsList.size() != 0)
	{
		WebNewsType type = newsTypeParentsList.get(newsTypeParentsList.size() - 1);
	
		parentId = type.getId();
		
		currentTypeName = type.getName();
	}
	
}

webNewsType = new WebNewsType();
webNewsType.setParentId(parentId);
webNewsType.setOrigin(Integer.parseInt(origin));
List<WebNewsType> newsTypeList = service.getWebTypeByParentId(webNewsType);


%>
<%if(!WebUtil.isEmpty(newsTypeList)){ %>
<h3><%= currentTypeName %></h3>
<ul class="categories">
<%
	for(WebNewsType newsTypes : newsTypeList)
    {
    	%><li><a href="newsList.jsp?origin=<%= origin %>&categoryWeb=<%= newsTypes.getId() %>"><%= newsTypes.getName() %></a></li><%
    }
%>
</ul>
<% } %>