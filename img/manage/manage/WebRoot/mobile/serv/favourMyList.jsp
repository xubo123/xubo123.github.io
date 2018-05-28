<%@ page language="java" pageEncoding="UTF-8"  import="com.hxy.util.*" %>
<%

String id = request.getParameter("id");
String category = request.getParameter("category");
String accountNum = request.getParameter("accountNum");
String region = request.getParameter("region");
String isWhat =  request.getParameter("isWhat");//0或空:本地，1:所有，2:我的收藏，3:我的发帖

if(WebUtil.isEmpty(isWhat))
{
	isWhat = "1";
}

String listExtensionParameters = "&cyServ.isWhat=" + isWhat + "&isWhat=" + isWhat;

//if("2".equals(isWhat) || "3".equals(isWhat))
//{
	listExtensionParameters += "&cyServ.accountNum=" + accountNum ;
//}

if(region == null)
{
	region = "";
}

%>

<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta name="Description" content="校友帮帮忙" />
    <meta name="Keywords" content="窗友,校友帮帮忙" />
    <meta name="author" content="Rainly" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="../css/cy_core.css">
    <link rel="stylesheet" href="../css/font-awesome.min.css">
    <link rel="stylesheet" href="../css/appeal.css">
    <!-- 图片展示 -->
    <link rel="stylesheet" href="../css/photoswipe.css">
    <link rel="stylesheet" href="../css/default-skin/default-skin.css">
</head>
<body>
<footer class="ui-footer ui-footer-btn">
    <ul class="ui-tiled ui-border-t">
    
      <%--
      <li data-href="favourList.jsp?isWhat=0&category=<%= category %>&accountNum=<%= accountNum %>&region=<%= region %>" <% if("0".equals(isWhat) || "".equals(isWhat) || isWhat == null){%>class="current"<%}%>><i class="fa fa-map-marker"></i>本地</li>
      --%>
      <li data-href="favourList.jsp?isWhat=1&category=<%= category %>&accountNum=<%= accountNum %>" <% if("1".equals(isWhat)){%>class="current"<%}%>><i class="fa fa-home"></i>全部</li>
      <li data-href="favourMyList.jsp?isWhat=2&category=<%= category %>&accountNum=<%= accountNum %>" <% if("2".equals(isWhat)){%>class="current"<%}%>><i class="fa fa-user"></i>我的</li>
      <li data-href="servHelpCreate.jsp?category=<%= category %>&accountNum=<%= accountNum %>"><i class="fa fa-pencil-square-o"></i>发帖</li>
    </ul>
</footer>
<section class="ui-container my-post">
    <div class="ui-tab">
        <ul class="ui-tab-nav ui-border-b">
            <li class="current">我的收藏</li>
            <li>我的发帖</li>
        </ul>
        <ul class="ui-tab-content" style="width:200%">
            <li class="current">
                <div class="thread-list" id="myFavorites">
                </div>
                <div class="more"><a href="javascript:;" alt="myFavorites" id="favoritesMore">点击查看更多</a></div>
                <div class="ui-loading-wrap" style="display:none"><i class="ui-loading"></i><p>加载中</p></div>
            </li>
            <li>
                <div class="thread-list" id="myPost">
                </div>
                <div class="more"><a href="javascript:;" alt="myPost" id="postMore">点击查看更多</a></div>
                <div class="ui-loading-wrap" style="display:none"><i class="ui-loading"></i><p>加载中</p></div>
            </li>
        </ul>
    </div>
</section>
<script src="../js/zepto.js"></script>
<script src="../js/cy_core.js"></script>
<script src="../js/dropload.min.js"></script>
<script src="../js/photoswipe.min.js"></script>
<script src="../js/photoswipe-ui-default.min.js"></script>
<script src="../js/photoswipe-appeal-min.js"></script>
<script src="../js/template_appeal.js?201509141146"></script>
<script src="../js/custom_appeal.js"></script>
<script>
	var category = '<%= category %>';
	var accountNum = '<%= accountNum %>';
	var region = '<%= region %>';
	var isWhat =  '<%= isWhat %>';
	var listExtensionParameters = '<%= listExtensionParameters %>';
	var totalRows = 0;
	
	var me = null;
	
	var isMyList = true;

    //页面加载
    getPageData("mobServAction!doNotNeedSessionAndSecurity_getServList.action?cyServ.category=<%=category%>&cyServ.viewType=favorite<%= listExtensionParameters %>&cyServ.region=<%=region%>","#myFavorites","onload",appealTpl,null,"loadPhotoSwipe");

    //查看更多
    $(document).on('tap','.more',function(){
    
    	
        var target = $(this);
        var altType = target.children('a').attr('alt');
        
        var temUrl = "";
        console.log(totalRows);
        if(altType == 'myFavorites')
        {
        	if($("#myFavorites > li").length >= totalRows)
        	{
        		$("#favoritesMore").text("没有更多了");
        		return;
        	}
        	temUrl = "mobServAction!doNotNeedSessionAndSecurity_getServList.action?cyServ.category=<%=category%>&cyServ.viewType=favorite<%= listExtensionParameters %>&cyServ.region=<%=region%>&cyServ.currentRow=" + $("#myFavorites > li").length;
        }
        else if(altType == 'myPost')
        {
        	if($("#myPost > li").length >= totalRows)
        	{
        		$("#postMore").text("没有更多了");
        		return;
        	}
        	temUrl = "mobServAction!doNotNeedSessionAndSecurity_getServList.action?cyServ.category=<%=category%><%= listExtensionParameters %>&cyServ.region=<%=region%>&cyServ.currentRow=" + $("#myPost > li").length;
        }
        
       
        
        //console.log(temUrl);
        $.ajax({
            type: 'GET',
            url: temUrl,
            dataType: 'json',
            beforeSend: function(xhr, settings) {
                target.hide();
                target.next().show();
                //console.log(target.next())
            },
            success: function(data){
                var result=$.tpl(appealTpl,data);
                $(target.prev()).append(result);
            },
            error: function(xhr, type){
                alert('Ajax error!');
            },
            complete: function(xhr, type){
                target.show();
                target.next().hide();
                initPhotoSwipeFromDOM('.ui-list-pic');
            }
        });
    });

    (function (){
        var tab = new fz.Scroll('.ui-tab', {
            role: 'tab',
            autoplay: false
        });
        /* 滑动开始前 */
        tab.on('scrollEnd', function(curPage) {
            if($('#myPost').children().length == 0) {
                getPageData("mobServAction!doNotNeedSessionAndSecurity_getServList.action?cyServ.category=<%=category%><%= listExtensionParameters %>&cyServ.region=<%=region%>","#myPost","",appealTpl,null,"loadPhotoSwipe");
            }
            //console.log(fromIndex,toIndex);// from 为当前页，to 为下一页
        });
    })();
    
    
    
    $(document).on('tap','.show-bot-menu',function(){
  	
	  	var accountNumStr = $(this).attr("id");
	  	var idStr = $(this).attr("rel");
	  	$('.ui-actionsheet .ui-actionsheet-complaint').attr("rel",idStr);
	  	$('.ui-actionsheet .ui-actionsheet-del').attr("rel",idStr);
	  	
	  	if(accountNumStr == accountNum)//如果是自己
	  	{
	  		$('.ui-actionsheet .ui-actionsheet-complaint').hide();
	  		$('.ui-actionsheet .ui-actionsheet-del').show();
	  	}
	  	else
	  	{
	  		$('.ui-actionsheet .ui-actionsheet-complaint').show();
	  		$('.ui-actionsheet .ui-actionsheet-del').hide();
	  	}
	  
	    $('.ui-actionsheet').addClass('show');
	    
  	});
</script>
</body>
</html>