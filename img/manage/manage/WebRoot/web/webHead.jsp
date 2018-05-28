<%@ page language="java" import="java.util.*,com.hxy.util.WebUtil" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/web/";
String title = request.getParameter("title");

if(WebUtil.isEmpty(title))
{
	title = "欢迎进入校友会网站";
}

%>


	<meta charset="utf-8">
    <title><%= title %></title>
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- CSS
  ================================================== -->
    <link rel="stylesheet" href="<%=basePath%>css/style.min.css">

    <!-- JS
  ================================================== -->
    <script src="<%=basePath%>js/jquery-1.8.2.min.js" type="text/javascript"></script>
    <!-- jQuery -->
    <script src="<%=basePath%>js/jquery.easing.1.3.js" type="text/javascript"></script>
    <!-- jQuery easing -->
    <script src="<%=basePath%>js/modernizr.custom.js" type="text/javascript"></script>
    <!-- Modernizr -->
    <script src="<%=basePath%>js/jquery-ui.min.js" type="text/javascript"></script>
    <!-- tabs, toggles, accordion -->
    <script src="<%=basePath%>js/custom.min.js" type="text/javascript"></script>
    <!-- jQuery initialization -->

    <!-- Responsive Menu -->
    <script src="<%=basePath%>js/jquery.meanmenu.min.js"></script>
    <script src="<%=basePath%>js/Validform_v5.3.2_min.js"></script>
    <script>
        jQuery(document).ready(function () {
            jQuery('header nav').meanmenu();
        });
    </script>

    <!-- Favicons
    ================================================== -->
    <link rel="shortcut icon" href="<%=basePath%>images/favicon.ico">
    <link rel="apple-touch-icon" href="<%=basePath%>images/apple-touch-icon.png">
    <link rel="apple-touch-icon" sizes="72x72" href="<%=basePath%>images/apple-touch-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="114x114" href="<%=basePath%>images/apple-touch-icon-114x114.png">
    <link rel="stylesheet" href="<%=basePath%>css/jquery-ui.min.css" type="text/css">
    <link rel="stylesheet" href="<%=basePath%>css/vstyle.css" type="text/css">
