<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
        <h2 class="centered error-404"><i class="fa fa-check-circle" style="color: #104d0b"></i></h2>

        <p class="centered p-20" style="margin-bottom: 25px;font-size: 18px">捐赠成功，感谢您的热心捐赠。</p>

        <p class="centered"><a href="../_project/_projectAction!donationIndex.action" class="btn big colored"><i class="fa fa-arrow-circle-left"></i>返回捐赠首页</a><a href="./donateQuery.jsp" class="btn big green"><i class="fa fa-search"></i>查询我的捐赠记录</a></p>
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