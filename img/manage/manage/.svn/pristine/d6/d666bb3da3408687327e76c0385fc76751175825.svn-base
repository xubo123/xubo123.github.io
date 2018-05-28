<%@ page language="java" import="java.util.*,com.hxy.util.WebUtil" pageEncoding="UTF-8"%>
<%@ page import="com.hxy.web.news.service.WebNewsService"%>
<%@ page import="com.hxy.system.SpringManager"%>
<%@ page import="com.hxy.web.news.entity.WebNews"%>
<%@ page import="com.hxy.web.news.entity.WebNewsType"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%

String categoryWeb = request.getParameter("categoryWeb");
String origin = request.getParameter("origin");
String pagination = request.getParameter("pagination");
String cityName = request.getParameter("cityName");

if(WebUtil.isEmpty(categoryWeb))
{
	categoryWeb = "0";
}

if(WebUtil.isEmpty(origin))
{
	origin = "1";
}

if(WebUtil.isEmpty(pagination))
{
	pagination = "1";
}

WebNewsService service = SpringManager.getBean("webNewsService", WebNewsService.class);
WebNews webNews = new WebNews();
webNews.setCategoryWeb(Integer.parseInt(categoryWeb));
webNews.setTopnews("100");
webNews.setCurrentRow(0);
webNews.setIncremental(10);

List<WebNews> list = service.getWebNewsList(webNews);

webNews = new WebNews();
webNews.setOrigin(Integer.parseInt(origin));
webNews.setCategoryWeb(Integer.parseInt(categoryWeb));
webNews.setActionUrl("newsList.jsp");
webNews.setPagination(Integer.parseInt(pagination));
webNews.setIncremental(20);


Map<String, Object> map = service.getWebNewsListWithPaging(webNews);

List<WebNews> newsList = (List<WebNews>)map.get("list");
String paging = (String)map.get("paging");

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


<script type="text/javascript">

function doGO(totalPage, actionUrl)
{
	var txtPageNum = document.getElementById('txtPageNum');
	
	if(txtPageNum.value == '' || isNaN(txtPageNum.value) || Number(txtPageNum.value) < 1)
	{
		alert('请输入正确的跳转页数');
		return;
	}
	else if(Number(txtPageNum.value) >= totalPage)
	{
		alert('已经是最后一页了');
		return;
	}
	else
	{
		var pagination = Number(txtPageNum.value);
		
		location.href = actionUrl + "&pagination=" + pagination;
	}
}

</script>
</head>
<body>


<!-- header -->
<jsp:include page="webNavigation.jsp" flush="true">
	<jsp:param name="webNewsType.origin" value="<%= origin %>" />
	<jsp:param name="categoryWeb" value="<%= categoryWeb  %>" />
	<jsp:param name="webNewsType.cityName" value="<%=cityName %>" />
</jsp:include>
<!-- header -->


<jsp:include page="currentPositionForInclude.jsp" flush="true">
	<jsp:param name="categoryWeb" value="<%= categoryWeb %>" />
	<jsp:param name="origin" value="<%= origin %>" />
</jsp:include>

<div class="container">
    <div class="four columns sidebar">
        
        <jsp:include page="topicListForInclude.jsp" flush="true">
			<jsp:param name="categoryWeb" value="<%= categoryWeb %>" />
			<jsp:param name="origin" value="<%= origin %>" />
		</jsp:include>
        
        <%if(!WebUtil.isEmpty(list)){ %>
        <h3>热点</h3>
        <% for(WebNews news : list){ %>
		<!-- <div class="one-third column"> -->
	        <li class="ui-nowrap"><a href="newsDetail.jsp?newsId=<%= news.getNewsId() %>&origin=<%= origin %>"><%= news.getTitle() %></a></li>
	    <!-- </div> -->
		<% }} %>
    </div>
    <div class="twelve columns">
    <% for(WebNews news : newsList){ %>
    
    
        <div class="post-preview preview-medium">
            <h2><a href="newsDetail.jsp?newsId=<%= news.getNewsId() %>&origin=<%= origin %>" class="dark-link"><%= news.getTitle() %></a></h2>
            <div class="post-meta">
                <span class="meta-date"><%= WebUtil.formatDateByPattern(news.getCreateTime(),WebUtil.YMD) %></span>
            </div>
            <div class="four columns alpha post-image-wrap">
                <a href="newsDetail.jsp?newsId=<%= news.getNewsId() %>&origin=<%= origin %>" class="post-image">
                    <img src="<%= news.getPic() %>" alt="">
                    <div class="link-overlay fa fa-chevron-right"></div>
                </a>
            </div>
            <div class="eight columns omega">
                <p><%= news.getIntroduction() %></p>
                <a class="btn colored" href="newsDetail.jsp?newsId=<%= news.getNewsId() %>&origin=<%= origin %>">查看全文<i class="fa fa-chevron-circle-right" style="margin: 0 0 0 7px;"></i></a>
            </div>
            <div class="clearfix"></div>
        </div>
      <%} %> 
        
       
        <!-- 分页 -->
        <div class="pagination">
            <%= paging %>
        </div>
        <!-- End 分页 -->
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