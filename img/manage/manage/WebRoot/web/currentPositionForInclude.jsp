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
List<WebNewsType> newsTypeList = service.getWebParentsByTypeId(webNewsType);


%>

<div class="container">
    <div class="sixteen columns page-title">
        <div class="eight columns alpha">
            <h3><%= newsType.getName() %></h3>
        </div>
        <div class="eight columns omega">
            <nav class="breadcrumbs">
                <ul>
                    <li>当前位置:</li>
                    <li><a href="<%= origin.equals("1")?"index.jsp":"indexForRegion.jsp" %>">首页</a></li>
                    
                    <%
                    for(WebNewsType newsTypes : newsTypeList)
                    {
                    	%><%-- <li><a href="newsList.jsp?origin=<%= origin %>&categoryWeb=<%= newsTypes.getId() %>"><%= newsTypes.getName() %></a></li> --%><%
                    	%><li><%= newsTypes.getName() %></li><%
                    }
                    %>
                    
                    
                    <li><%= newsType.getName() %></li>
                </ul>
            </nav>
        </div>
        <div class="clearfix"></div>
    </div>
</div>