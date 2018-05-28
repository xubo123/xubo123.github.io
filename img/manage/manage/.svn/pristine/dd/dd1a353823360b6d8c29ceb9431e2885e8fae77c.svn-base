<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
    <title>值年返校</title>
    <meta name="Description" content="值年返校" />
    <meta name="Keywords" content="窗友,值年返校" />
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
        <div style="background-image:url(../wkd_images/zn_top.jpg)"></div>
    </div>
    <div class="ui-tab">
        <ul class="ui-tab-nav ui-border-b">
            <li class="current">邀请函</li>
            <li>组织方案</li>
            <li>服务提供</li>
            <li>活动</li>
        </ul>
        <ul class="ui-tab-content" style="width:400%">
            <li class="current">
                <ul class="ui-list ui-border-tb">
                    <li class="ui-border-t" data-href="show_1.html">
                        <div class="ui-list-img">
                            <span style="background-image:url(../wkd_images/zn_1.jpg)"></span>
                        </div>
                        <div class="ui-list-info">
                            <h4 class="ui-nowrap">校友值年返校邀请函</h4>
                            <p class="ui-nowrap-multi">亲爱的校友，时光荏苒，岁月如梭。曾经时，扬子江边，黄家湖畔</p>
                        </div>
                    </li>
                </ul>
            </li>
            <li>
                <ul class="ui-list ui-border-tb">
                    <li class="ui-border-t" data-href="show_2.html">
                        <div class="ui-list-img">
                            <span style="background-image:url(../wkd_images/zn_2.jpg)"></span>
                        </div>
                        <div class="ui-list-info">
                            <h4 class="ui-nowrap">校友值年返校活动组织方案</h4>
                            <p class="ui-nowrap-multi">校友返校活动分为四个部分，分别是校内座谈、参观游览、纪念活动</p>
                        </div>
                    </li>
                </ul>
            </li>
            <li>
                <ul class="ui-list ui-border-tb">
                    <li class="ui-border-t" data-href="show_3.html">
                        <div class="ui-list-img">
                            <span style="background-image:url(../wkd_images/zn_3.jpg)"></span>
                        </div>
                        <div class="ui-list-info">
                            <h4 class="ui-nowrap">校友值年返校服务提供</h4>
                            <p class="ui-nowrap-multi">校友会愿意竭诚为校友提供所需的各项帮助和服务</p>
                        </div>
                    </li>
                </ul>
            </li>
            <li>
                <p>暂无数据</p>
            </li>
        </ul>
    </div>
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
        var tab = new fz.Scroll('.ui-tab', {
            role: 'tab',
            autoplay: false
        });
    });
</script>
</body>
</html>