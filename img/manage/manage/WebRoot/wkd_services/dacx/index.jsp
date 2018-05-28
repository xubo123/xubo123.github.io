<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
    <title>档案查询</title>
    <meta name="Description" content="档案查询" />
    <meta name="Keywords" content="窗友,档案查询" />
    <meta name="author" content="Rainly" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="../../mobile/css/cy_core.css">
    <link rel="stylesheet" href="../../mobile/css/font-awesome.min.css">
    <link rel="stylesheet" href="../../mobile/css/wkd_services.css">
</head>
<body>
<section class="ui-container">
    <div class="ui-banner">
        <div style="background-image:url(../wkd_images/dacx_top.jpg)"></div>
    </div>
    <h1>档案查询</h1>
    <ul class="ui-list ui-border-tb">
        <li class="ui-border-t" data-href="show_1.html">
            <div class="ui-list-img">
                <span style="background-image:url(../wkd_images/dacx_1.jpg)"></span>
            </div>
            <div class="ui-list-info">
                <h4 class="ui-nowrap">档案馆服务简介</h4>
                <p class="ui-nowrap-multi">档案馆秉承着“为学校的明天，保存好学校的昨天，记载下学校的今</p>
            </div>
        </li>
        <li class="ui-border-t" data-href="show_2.html">
            <div class="ui-list-img">
                <span style="background-image:url(../wkd_images/dacx_2.jpg)"></span>
            </div>
            <div class="ui-list-info">
                <h4 class="ui-nowrap">档案服务申请</h4>
                <p class="ui-nowrap-multi"> 欢迎校友使用学校电子图书资源！请各位校友按照如下流程办理：</p>
            </div>
        </li>
        <li class="ui-border-t" data-href="show_3.html">
            <div class="ui-list-img">
                <span style="background-image:url(../wkd_images/dacx_3.jpg)"></span>
            </div>
            <div class="ui-list-info">
                <h4 class="ui-nowrap">校史馆参观预约</h4>
                <p class="ui-nowrap-multi">为让校友和在校学生读到更多的好书，我们开展好书推荐活动，欢迎</p>
            </div>
        </li>
    </ul>
</section>
<script src="../../mobile/js/zepto.js"></script>
<script src="../../mobile/js/cy_core.js"></script>
<script>
    Zepto(function($){
        $(document).on('tap','.ui-list li',function(){
            if($(this).data('href')){
                location.href= $(this).data('href');
            }
        });
    });
</script>
</body>
</html>