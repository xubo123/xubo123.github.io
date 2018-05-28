<%@ page language="java" pageEncoding="UTF-8"  import="com.hxy.util.*" %>
<%

String id = request.getParameter("id");
String category = request.getParameter("category");
String accountNum = request.getParameter("accountNum");
String region = request.getParameter("region");
String isWhat =  request.getParameter("isWhat");//0或空:全部，1:所有，2:我的

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
<footer class="ui-footer ui-footer-btn ui-mini-footer">
  <div id="reply">
    <div class="reply ui-border-radius">
      <input id="commentContent" type="text" maxlength="255" placeholder="我也说一句...">
    </div>
    <button class="ui-btn ui-btn-primary" onclick="doComment();">回复</button>
  </div>
</footer>
<section class="ui-container">
  <div class="wrapper">
    <div class="inner">
      <div class="comment-list">
      </div>
    </div>
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
	
	var id = '<%= id %>';
	var category = '<%= category %>';
	var accountNum = '<%= accountNum %>';
	var region = '<%= region %>';
	var isWhat =  '<%= isWhat %>';	
	
	var isMyList = false;
	
  var totalRows = 0;

  Zepto(function($){
  
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
  	
  	//页面加载
    getPageData("mobServAction!doNotNeedSessionAndSecurity_getServCommentListWithServ.action?cyServ.id=<%= id %>",".comment-list","onload",commentTpl,null,"loadPhotoSwipe");
  
    //下拉刷新
    var dropload = $('.inner').dropload({
      domUp : {
        domClass   : 'dropload-up',
        domRefresh : '<div class="dropload-refresh"><i class="fa fa-long-arrow-down"></i>下拉刷新</div>',
        domUpdate  : '<div class="dropload-update"><i class="fa fa-long-arrow-up"></i> 释放更新</div>',
        domLoad    : '<div class="dropload-load"><div class="ui-loading-wrap"><i class="ui-loading"></i><p>加载中</p></div></div>'
      },
      domDown : {
        domClass   : 'dropload-down',
        domRefresh : '<div class="dropload-refresh"><i class="fa fa-long-arrow-up"></i> 上拉加载更多</div>',
        domUpdate  : '<div class="dropload-update"><i class="fa fa-long-arrow-down"></i> 释放加载</div>',
        domLoad    : '<div class="dropload-load"><div class="ui-loading-wrap"><i class="ui-loading"></i><p>加载中</p></div></div>'
      },
      loadUpFn : function(me){

        getPageData("mobServAction!doNotNeedSessionAndSecurity_getServCommentListWithServ.action?cyServ.id=<%= id %>&cyServ.currentRow=0&cyServ.incremental=" + $(".post-comment .ui-list > li").length,".comment-list","update",commentTpl,me);
      },
      loadDownFn : function(me){

      	
      	if($(".post-comment .ui-list > li").length >= totalRows)
  		{
  			if (me != null) {
  				
				me.resetload();
			}
  			$(".inner").append('<div class="dropload-down" style="-webkit-transition: all 300ms; transition: all 300ms; height: 40px;"><div class="dropload-load"><div class="ui-loading-wrap">没有更多了</div></div></div>');
  			
  			
  		}
  		else
  		{
      		getPageData("mobServAction!doNotNeedSessionAndSecurity_getServCommentListWithServ.action?cyServ.id=<%= id %>&cyServ.currentRow=" + $(".post-comment .ui-list > li").length,".post-comment .ui-list","more",commentMoreTpl,me);
      	}

      }
    });

    
  });
  
  
function doComment()
{
	
	var commentContent = document.getElementById('commentContent');
	
	if(commentContent.value == '')
	{
		dia=$.dialog({
			 title:'温馨提示',
			 content:'请输入您想要说的话！',
			 button:["确认"]
			});
			
		dia.on("dialog:hide",function(e){
				console.log("dialog hide");
			});
	}
	else
	{
		$.ajax({
			url : "mobServAction!doNotNeedSessionAndSecurity_comment.action?" + 
				  "cyServComment.serviceId=" + id +
				  "&cyServComment.content=" + commentContent.value +
				  "&cyServComment.accountNum=" + accountNum,
			
			dataType : 'json',
	        type: "POST",
			
	        success: function(data) 
	        {
	        	
	        	if(data.status == true)
	        	{
	        		
				    commentContent.value = '';
				    getPageData("mobServAction!doNotNeedSessionAndSecurity_getServCommentListWithServ.action?cyServ.id=<%= id %>",".comment-list","onload",commentTpl,null,"loadPhotoSwipe");
	        		
	        	}
	        	else
	        	{
	        		dia=$.dialog({
				        title:'温馨提示',
				        content:'回复失败，请重试！',
				        button:["确认"]
				      });
	        	}
	        	
	        },
	        complete: function()
	        {
	        	
	        	dia.on("dialog:hide",function(e){
				        console.log("dialog hide");
				});
	        }
	    });
	
	}
	
	
	

}
  
  
</script>
</body>
</html>