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
String topnews = request.getParameter("topnews");
String cityName = request.getParameter("cityName");
String origin = request.getParameter("origin");

String numberOfRows = request.getParameter("numberOfRows");

if(WebUtil.isEmpty(categoryWeb))
{
	categoryWeb = "0";
}
		
if(WebUtil.isEmpty(topnews))
{
	topnews = null;
}

if(WebUtil.isEmpty(numberOfRows))
{
	numberOfRows = "6";
}

if(WebUtil.isEmpty(origin))
{
	origin = "1";
}

if(WebUtil.isEmpty(cityName))
{
	cityName = null;
}

WebNews webNews = new WebNews();
webNews.setCategoryWeb(Integer.parseInt(categoryWeb));
webNews.setTopnews(topnews);

webNews.setCurrentRow(0);
webNews.setIncremental(Integer.parseInt(numberOfRows));

WebNewsType webNewsType = new WebNewsType();
webNewsType.setId(Long.parseLong(categoryWeb));

WebNewsService service = SpringManager.getBean("webNewsService", WebNewsService.class);

WebNewsType newsType = service.getWebNewsTypeById(webNewsType);


List<WebNews> list = service.getWebNewsList(webNews);

int originInt = Integer.parseInt(origin);

%>

<% if(WebUtil.isEmpty(topnews) && "-100".equalsIgnoreCase(categoryWeb)){

		
	
	
	if(originInt == 2)
	{
		webNewsType.setCityName(cityName);
	}
	
	webNewsType.setOrigin(originInt);
	List<WebNewsType> typeList = service.getMainWebNewsType(webNewsType);

	for(int i = 0; i < typeList.size(); i++)
	{
		
		if(i % 2 == 0)
		{
			
			%><div class="clearfix separator"></div><%
		}
		
		WebNewsType mainNewsType = typeList.get(i);
		
		webNews.setCategoryWeb(Integer.parseInt(String.valueOf(mainNewsType.getId())));

		list = service.getWebNewsList(webNews);
		
		
		%>
		<div class="eight columns news-list">
		<h4 class="headline ui-nowrap"><%= mainNewsType.getName() %></h4>
        <ul class="list">
        <% for(WebNews news : list){ %>
            <li><span class="date"><%= WebUtil.formatDateByPattern(news.getCreateTime(),WebUtil.YMD) %></span><a href="newsDetail.jsp?newsId=<%= news.getNewsId() %>&origin=<%= origin %>"><%= news.getTitle() %></a></li>
        <% } %>
        </ul>
        <div class="more"><a href="newsList.jsp?categoryWeb=<%= mainNewsType.getId() %>&origin=<%= origin %>">更多</a></div>
		</div>
	<%
	}

} 
else if(!"-100".equalsIgnoreCase(categoryWeb))
{
	if(originInt == 2)
	{
		webNews.setCityName(cityName);
	}
	
	list = service.getWebNewsList(webNews);
	%>
	<% for(WebNews news : list){ %>
	<div class="one-third column">
        <a href=""><img src="<%= news.getPic() %>" alt=""/></a>
        <h4 class="ui-nowrap"><a href="newsDetail.jsp?newsId=<%= news.getNewsId() %>&origin=<%= origin %>"><%= news.getTitle() %></a></h4>
        <p><%= news.getIntroduction() %></p>
    </div>
	<% } %>
	<%
}
%>	