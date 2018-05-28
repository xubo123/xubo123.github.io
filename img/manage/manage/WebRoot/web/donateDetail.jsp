<%@page import="com.hxy.web.news.entity.WebNewsType"%>
<%@page import="com.hxy.system.SpringManager"%>
<%@page import="com.hxy.web.news.service.WebNewsService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/web/";
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
		<jsp:param name="webNewsType.origin" value="1" />
		<jsp:param name="name" value="捐赠" />
	</jsp:include>
<!-- header -->

<div class="container">
    <div class="sixteen columns page-title">
        <div class="eight columns alpha">
            <h3><span>${project.projectName}</span></h3>
        </div>
        <div class="eight columns omega">
            <nav class="breadcrumbs">
                <ul>
                    <li>当前位置:</li>
                    <li>
                        <a href="../web">首页</a>
                    </li>
                    <li>
                        <a href="./_projectAction!donationIndex.action">捐赠</a>
                    </li>
                    <li>${project.projectName}</li>
                </ul>
            </nav>
        </div>
        <div class="clearfix"></div>
    </div>
</div>

<div class="container">
    <div class="twelve columns">
       	${project.content}
        <p class="centered"><a href="./_projectAction!donationStep1.action?id=${project.projectId}" class="btn big colored"><i class="fa fa-arrow-circle-right"></i>开始捐赠</a></p>

    </div>
    <div class="four columns sidebar">
        <h3>捐赠实时播报</h3>
        <div class="donate-history">
        	<c:forEach items="${donations}" var="donation">
        		 <dl>
	                <dt>
	                <c:choose>
	                	<c:when test="${donation.anonymous==1}">匿名</c:when>
	                	<c:otherwise>${donation.x_name}</c:otherwise>
	                </c:choose>
	                </dt>
	                <dd>捐赠 <strong>${donation.project.projectName}</strong> <span>${donation.money}元</span></dd>
	            </dl>
        	</c:forEach>
        </div>

    </div>
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