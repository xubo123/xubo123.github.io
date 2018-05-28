<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
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
            <h3><span>校友捐赠</span></h3>
        </div>
        <div class="eight columns omega">
            <nav class="breadcrumbs">
                <ul>
                    <li>当前位置:</li>
                    <li>
                        <a href="../web">首页</a>
                    </li>
                    <li>
                        <a href="../_project/_projectAction!donationIndex.action">捐赠</a>
                    </li>
                    <li>校友捐赠</li>
                </ul>
            </nav>
        </div>
        <div class="clearfix"></div>
    </div>
</div>

<div class="container">
    <div class="sixteen columns">
        <h4 class="headline">捐赠查询</h4>
        <p>以下是您的捐赠记录，感谢您对母校的热心支持。</p>
        <div class="donate-list">
            <table width="100%">
                <tr>
                    <th>捐赠时间</th>
                    <th>捐赠项目</th>
                    <th>捐赠金额</th>
                    <th>捐赠状态</th>
                    <th>操作</th>
                </tr>
               <c:if test="${donations!=null}">
               		<c:forEach var="donation" items="${donations}">
               			<tr>
		                    <td><fmt:formatDate value="${donation.donationTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		                    <td>${donation.project.projectName}</td>
		                    <td>${donation.money}元</td>
		                    <td>
		                    <c:choose>
		                    	<c:when test="${donation.payStatus==1}"><font color="green">已支付</font></c:when>
		                    	<c:otherwise><font color="red">待支付</font></c:otherwise>
		                    </c:choose>
		                    </td>
		                    <td>
		                    	<c:if test="${donation.payStatus!=1}">
			                    	<a href="<%=path%>/_donation/_donationAction!doNotNeedSessionAndSecurity_donationConfirm.action?id=${donation.donationId}">支付</a>
		                    	</c:if>
		                    </td>
		                </tr>
               		</c:forEach>
               </c:if>
            </table>
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