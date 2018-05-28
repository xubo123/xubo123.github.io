<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/web/";
%>

<!DOCTYPE html>
<!--[if lt IE 7 ]>
<html class="ie ie6"> <![endif]-->
<!--[if IE 7 ]>
<html class="ie ie7"> <![endif]-->
<!--[if IE 8 ]>
<html class="ie ie8"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->
<html>
<!--<![endif]-->
<head>
<!-- head -->
<jsp:include page="webHead.jsp" flush="true" />
<!-- head -->

</head>
<body>
	<!-- header -->
	<jsp:include page="webNavigation.jsp" flush="true">
		<jsp:param name="webNewsType.origin" value="1" />
		<jsp:param name="name" value="捐赠" />
	</jsp:include>
	<!-- header -->

	<div class="container">
		<div class="sixteen columns page-title">
			<div class="eight columns alpha">
				<h3>
					<span>捐赠</span>
				</h3>
			</div>
			<div class="eight columns omega">
				<nav class="breadcrumbs">
					<ul>
						<li>当前位置:</li>
						<li><a href="../web">首页</a>
						</li>
						<li>捐赠</li>
					</ul>
				</nav>
			</div>
			<div class="clearfix"></div>
		</div>
	</div>

	<div class="container">
		<div class="sixteen columns">
			<div class="carousel">
				<div class="carousel-content">
					<img class="carousel-item"
						src="<%=basePath%>images/services/slide4.jpg" alt=""> <img
						class="carousel-item"
						src="<%=basePath%>images/services/slide5.jpg" alt=""> <img
						class="carousel-item"
						src="<%=basePath%>images/services/slide6.jpg" alt="">
				</div>
			</div>
		</div>

		<div class="separator"></div>
		<div class="sixteen columns news-list">
			<h4 class="headline">捐赠项目</h4>
			<div class="more">
				<a href="${pageContext.request.contextPath}/_project/_projectAction!donationMore.action?page=1&rows=12">更多</a>
			</div>
		</div>
		<div class="donate-list">
			<c:if test="${projects!=null}">
				<c:forEach items="${projects}" var="project">
					<div class="one-third column">
						<a
							href="${pageContext.request.contextPath}/_project/_projectAction!donationDetail.action?id=${project.projectId}"
							class="donate-pic"
							style="background-image: url(${project.projectPic})"></a>
						<h4 class="ui-nowrap">
							<a
								href="${pageContext.request.contextPath}/_project/_projectAction!donationDetail.action?id=${project.projectId}">${project.projectName}</a>
						</h4>
						<p class="ui-nowrap-multi">${project.introduction}</p>
					</div>
				</c:forEach>
			</c:if>
		</div>
		<div class="clearfix"></div>

		<div class="separator"></div>
		<div class="sixteen columns">
			<div class="promo-box clearfix">
				<div class="text">
					<h3>捐赠查询</h3>
					<p>点击可查询您的捐赠记录，感谢您对学校的支持！</p>
				</div>
				<a class="btn big colored" href="../web/donateQuery.jsp"><i class="fa fa-search"></i>捐赠查询</a>
			</div>
		</div>
		<div class="clearfix"></div>

		<div class="separator"></div>
		<%--<div class="eight columns news-list">
			<h4 class="headline">相关报道</h4>
			<ul class="list">
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
			</ul>
			<div class="more">
				<a href="">更多</a>
			</div>
		</div>

		<div class="eight columns news-list">
			<h4 class="headline">校友感言</h4>
			<ul class="list">
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
				<li><span class="date">2015-08-08</span><a href="">新闻标题新闻标题新闻标题</a>
				</li>
			</ul>
			<div class="more">
				<a href="">更多</a>
			</div>
		</div>
		--%><div class="clearfix"></div>

		<div class="separator"></div>
	</div>

	<!-- footer -->
	<jsp:include page="webBottom.jsp" flush="true">
		<jsp:param name="webNewsType.origin" value="1" />
	</jsp:include>
	<!-- footer -->


	<script type="text/javascript">
		
	</script>
</body>
</html>