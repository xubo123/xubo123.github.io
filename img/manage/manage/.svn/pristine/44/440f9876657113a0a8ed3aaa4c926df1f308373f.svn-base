<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
    <title>稿件投送</title>
    <meta name="Description" content="稿件投送" />
    <meta name="Keywords" content="窗友,稿件投送" />
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
        <div style="background-image:url(../wkd_images/gjts_top.jpg)"></div>
    </div>
    <h1>稿件投送</h1>
    <ul class="ui-list ui-border-tb">
        <li class="ui-border-t" data-href="show_1.html">
            <div class="ui-list-img">
                <span style="background-image:url(../wkd_images/gjts_1.jpg)"></span>
            </div>
            <div class="ui-list-info">
                <h4 class="ui-nowrap">学报投稿</h4>
                <p class="ui-nowrap-multi">《武汉科技大学学报》是由武汉科技大学主办的综合性科技期刊，</p>
            </div>
        </li>
        <li class="ui-border-t" data-href="show_2.html">
            <div class="ui-list-img">
                <span style="background-image:url(../wkd_images/gjts_2.jpg)"></span>
            </div>
            <div class="ui-list-info">
                <h4 class="ui-nowrap">校友APP投稿</h4>
                <p class="ui-nowrap-multi">校友APP平台是发布武汉科技大学校友会发布校友信息的新平台，欢迎广</p>
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