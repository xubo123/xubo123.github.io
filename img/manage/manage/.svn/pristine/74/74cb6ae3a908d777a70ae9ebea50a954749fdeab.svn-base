<%@ page language="java" import="java.util.*,com.hxy.util.WebUtil" pageEncoding="UTF-8"%>
<%@ page import="com.hxy.web.news.service.WebNewsService"%>
<%@ page import="com.hxy.system.SpringManager"%>
<%@ page import="com.hxy.web.news.entity.WebNews"%>
<%@ page import="com.hxy.web.news.entity.WebNewsType"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%

String newsId = request.getParameter("newsId");
String origin = request.getParameter("origin");
String cityName = request.getParameter("cityName");

if(WebUtil.isEmpty(newsId))
{
	newsId = "0";
}

if(WebUtil.isEmpty(origin))
{
	origin = "1";
}

WebNews webNews = new WebNews();
webNews.setNewsId(Long.parseLong(newsId));

WebNewsService service = SpringManager.getBean("webNewsService", WebNewsService.class);
WebNews news = service.getWebNews(webNews);

webNews = new WebNews();
webNews.setCategoryWeb(news.getCategoryWeb());
webNews.setTopnews("100");
webNews.setCurrentRow(0);
webNews.setIncremental(10);

List<WebNews> list = service.getWebNewsList(webNews);

%>

<!DOCTYPE html>
<!--[if lt IE 7 ]>
<html class="ie ie6"> <![endif]-->
<!--[if IE 7 ]>
<html class="ie ie7"> <![endif]-->
<!--[if IE 8 ]>
<html class="ie ie8"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->
<html> <!--<![endif]-->
<head>
    <!-- head -->
	<jsp:include page="webHead.jsp" flush="true" />
	<!-- head -->  
</head>
<body>

<!-- header -->
<jsp:include page="webNavigation.jsp" flush="true">
	<jsp:param name="webNewsType.origin" value="<%= origin %>" />
	<jsp:param name="categoryWeb" value="<%= news.getCategoryWeb() %>" />
	<jsp:param name="webNewsType.cityName" value="<%=cityName %>" />

</jsp:include>
<!-- header -->

<jsp:include page="currentPositionForInclude.jsp" flush="true">
	<jsp:param name="categoryWeb" value="<%= news.getCategoryWeb() %>" />
	<jsp:param name="origin" value="<%= origin %>" />
</jsp:include>

<div class="container">
    <div class="four columns sidebar">
        <jsp:include page="topicListForInclude.jsp" flush="true">
			<jsp:param name="categoryWeb" value="<%= news.getCategoryWeb() %>" />
			<jsp:param name="origin" value="<%= origin %>" />
		</jsp:include>
		<%if(!WebUtil.isEmpty(list)){ %>
        <h3>热点</h3>
        <ul class="recent-post">
        <% for(WebNews newss : list){ %>
		<!-- <div class="one-third column"> -->
	        <li class="ui-nowrap"><a href="newsDetail.jsp?newsId=<%= newss.getNewsId() %>"><%= newss.getTitle() %></a></li>
	    <!-- </div> -->
		<% } %>
        </ul>
        <% } %>
    </div>
    <div class="twelve columns">
        <div class="post">
            <h2><%= news.getTitle() %></h2>
            <div class="post-meta">
                <span class="meta-date"><%= WebUtil.formatDateByPattern(news.getCreateTime(),WebUtil.YMD) %></span>
            </div>

            <div class="post-content">
                <div class="blockquote"><%= WebUtil.trim(news.getIntroduction()) %></div>
                <p><%= WebUtil.trim(news.getContent()) %></p>
            </div>

        </div>
    </div>



    <div class="clearfix"></div>

</div>

<!-- footer -->
<jsp:include page="webBottom.jsp" flush="true">
	<jsp:param name="webNewsType.origin" value="<%= origin %>" />
</jsp:include>
<!-- footer -->

</body>
</html>