<!DOCTYPE html>
<!--[if lt IE 7 ]>
<html class="ie ie6"> <![endif]-->
<!--[if IE 7 ]>
<html class="ie ie7"> <![endif]-->
<!--[if IE 8 ]>
<html class="ie ie8"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->
<html> <!--<![endif]-->

<%@ page language="java" import="java.util.*,com.hxy.util.WebUtil" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>

<!-- head -->
<jsp:include page="webHead.jsp" flush="true"/>
<!-- head -->

</head>
<body>

<!-- header -->

<jsp:include page="webNavigation.jsp" flush="true">
	<jsp:param name="webNewsType.origin" value="1" />
	<jsp:param name="categoryWeb" value="-1" />
</jsp:include>


<!-- header -->

<div class="container">
    <div class="sixteen columns page-title">
        <div class="eight columns alpha">
            <h3>地方校友会</h3>
        </div>
        <div class="eight columns omega">
            <nav class="breadcrumbs">
                <ul>
                    <li>当前位置:</li>
                    <li>
                        <a href="index.jsp">首页</a>
                    </li>
                    <li>地方校友会</li>
                </ul>
            </nav>
        </div>
        <div class="clearfix"></div>
    </div>
</div>
<form action="webNewsAction!getWebSubAlumniPage.action" name="cityForm" id="cityFormId" method="post">
<div class="container">

		<c:if test="${realListNewsType!=null}">
		  
			<c:forEach var="type" items="${realListNewsType}">
			  <div class="sixteen columns">
				<h4 class="headline">${type.cityName }</h4> 
				    <c:if test="${type.webNewsType!=null and type.webNewsType.size()>0}">
						<ul class="city-list clearfix">
							<c:forEach var="type2" items="${type.webNewsType}">
								<li><a href="#" onclick="javascript:subForm('${type2.dispCityName }');">${type2.cityName }</a></li>
							</c:forEach>
						</ul>
					</c:if>
			  </div>
			</c:forEach>
		  
		</c:if>

</div>
<input type="hidden" name="cityName" value="" />
</form>

<script type="text/javascript">

function subForm(dispCityName){
	
	document.cityForm.cityName.value = dispCityName;
	
	document.cityForm.submit();
}

</script>

<!-- footer -->

<jsp:include page="webBottom.jsp" flush="true"/>


<!-- footer -->


<script type="text/javascript">

</script>

</body>
</html>