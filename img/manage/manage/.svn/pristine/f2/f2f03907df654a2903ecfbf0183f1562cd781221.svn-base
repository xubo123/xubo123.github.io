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
            <h3><span>捐赠项目</span></h3>
        </div>
        <div class="eight columns omega">
            <nav class="breadcrumbs">
                <ul>
                    <li>当前位置:</li>
                    <li>
                        <a href="../web">首页</a>
                    </li>
                    <li><a href="./_projectAction!donationIndex.action">捐赠</a></li>
                    <li>捐赠项目</li>
                </ul>
            </nav>
        </div>
        <div class="clearfix"></div>
    </div>
</div>

<div class="container">
    <div class="donate-list">
       <c:if test="${projects!=null}">
				<c:forEach items="${projects}" var="project">
					<div class="one-third column">
						<a
							href="./_projectAction!donationDetail.action?id=${project.projectId}"
							class="donate-pic"
							style="background-image: url(${project.projectPic})"></a>
						<h4 class="ui-nowrap">
							<a
								href="./_projectAction!donationDetail.action?id=${project.projectId}">${project.projectName}</a>
						</h4>
						<p class="ui-nowrap-multi">${project.introduction}</p>
					</div>
				</c:forEach>
			</c:if>
    </div>
    <div class="clearfix separator"></div>
    <!-- 分页 -->
    <div class="pagination">
        <div>
 			第 ${page} 页，共 ${totalPage} 页
        </div>
        <c:forEach var="pageItem" begin="1" end="${totalPage}">
        	<c:choose>
        		<c:when test="${page==pageItem}">
		        	<a href="./_projectAction!donationMore.action?page=${pageItem}&rows=12" class="active">${pageItem}</a>
        		</c:when>
        		<c:otherwise>
        			<a href="./_projectAction!donationMore.action?page=${pageItem}&rows=12">${pageItem}</a>
        		</c:otherwise>
        	</c:choose>
        </c:forEach>
    </div>
    <!-- End 分页 -->
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
