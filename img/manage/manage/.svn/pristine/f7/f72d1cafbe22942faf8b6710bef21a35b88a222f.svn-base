<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
<jsp:include page="webHead.jsp" flush="true"/>
<!-- head -->    
</head>
<body>
<!-- header -->
<jsp:include page="webNavigation.jsp" flush="true">
	<jsp:param name="webNewsType.origin" value="1" />
</jsp:include>
<!-- header -->
<div class="fullwidthbanner-container">
    <div class="fullwidthbanner">
        <div class="single-banner" style="background-image: url(images/slider/Slider-1.jpg);"></div>
    </div>
</div>

<div class="container">
    <jsp:include page="newsListForInclude.jsp" flush="true">
		<jsp:param name="topnews" value="100" />
		<jsp:param name="numberOfRows" value="3" />
	</jsp:include>

    <jsp:include page="newsListForInclude.jsp" flush="true">
		<jsp:param name="categoryWeb" value="-100" />
	</jsp:include>
</div>

<!-- footer -->
<jsp:include page="webBottom.jsp" flush="true">
	<jsp:param name="webNewsType.origin" value="1" />
</jsp:include>
<!-- footer -->

</body>
</html>