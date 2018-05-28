<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
    <title>证书补办</title>
    <meta name="Description" content="证书补办" />
    <meta name="Keywords" content="窗友,证书补办" />
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
        <div style="background-image:url(../wkd_images/zsbb_top.jpg)"></div>
    </div>
    <h1>证书补办</h1>
    <ul class="ui-list ui-border-tb">
        <li class="ui-border-t" data-href="show_1.html">
            <div class="ui-list-img">
                <span style="background-image:url(../wkd_images/zsbb_1.jpg)"></span>
            </div>
            <div class="ui-list-info">
                <h4 class="ui-nowrap">证书补办流程</h4>
                <p class="ui-nowrap-multi">为方便广大校友补办毕业证、学位证，现将补办流程绘制如下，请校友按图</p>
            </div>
        </li>
        <li class="ui-border-t" data-href="show_2.html">
            <div class="ui-list-img">
                <span style="background-image:url(../wkd_images/zsbb_2.jpg)"></span>
            </div>
            <div class="ui-list-info">
                <h4 class="ui-nowrap">证书补办一次性告知书</h4>
                <p class="ui-nowrap-multi">证书补办您需要准备如下资料，如有资料不全，请到学校档案馆查询</p>
            </div>
        </li>
        <li class="ui-border-t" data-href="show_3.html">
            <div class="ui-list-img">
                <span style="background-image:url(../wkd_images/zsbb_3.jpg)"></span>
            </div>
            <div class="ui-list-info">
                <h4 class="ui-nowrap">联系我们</h4>
                <p class="ui-nowrap-multi">如果您还有什么不清楚的，请您电话联系我们，或者留言给我们，我</p>
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