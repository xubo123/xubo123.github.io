<!DOCTYPE html>
<html>

<%@ page language="java" pageEncoding="UTF-8"  import="com.hxy.util.*" %>
<%

String accountNum = request.getParameter("accountNum");

//String region = request.getParameter("region");
//String isAll =  request.getParameter("isAll");
String category = request.getParameter("category");
%>


<head>
  <title></title>
  <meta name="Description" content="联系校友会" />
  <meta name="Keywords" content="窗友,联系校友会" />
  <meta name="author" content="Rainly" />
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="format-detection" content="telephone=no">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <link rel="stylesheet" href="../css/cy_core.css">
  <link rel="stylesheet" href="../css/font-awesome.min.css">
  <link rel="stylesheet" href="../css/contact.css">
</head>
<body>
<footer class="ui-footer ui-footer-stable ui-btn-group ui-border-t">
  <button id="submitid" class="ui-btn-lg ui-btn-primary">发 送</button>
</footer>
<form id="saveForm" method="post">
<section class="ui-container create-post">
  <div class="ui-form-item ui-form-item-pure ui-border-b">
    <input type="text" id="title" name="cyContact.title" placeholder="标题" maxlength="120">
  </div>
  <div class="ui-form-item ui-form-item-pure ui-form-item-textarea ui-border-b">
    <textarea id="content" placeholder="说点什么..." name="cyContact.content"></textarea>
  </div>
  <input type="hidden" name="cyContact.accountNum" value="<%=accountNum %>" />
  <input type="hidden" name="cyContact.category" value="<%=category %>" />
</section>
</form>

<script src="../js/cy_core.js" type="text/javascript"></script>
<script src="../js/zepto.min.js" type="text/javascript"></script>
<script src="../js/global.js" type="text/javascript"></script>
<script src="../js/dropload.min.js" type="text/javascript"></script>
<script src="../js/lrz.mobile.min.js"></script>

<script type="text/javascript">

$("#submitid").tap(function(){
	
    if($('#title').val().trim()==''){
   		  $.dialog({
    	        title:'温馨提示',
    	        content:'请输入标题',
    	        button:["确认"]
    	      });
		  return;
	}
    
    if($('#content').val().trim()==''){
 		  $.dialog({
  	        title:'温馨提示',
  	        content:'请输入相关内容',
  	        button:["确认"]
  	      });
		  return;
	}
    
    var tmpMsg = '';
    
    var dia=$.dialog({
      title:'温馨提示',
      content:'请确认信息填写无误',
      button:["确认","返回"]
    });
    
    dia.on("dialog:action",function(e){
        console.log(e.index);
        if(e.index == 0){
        	
        	$.ajax({
				url : 'mobServAction!doNotNeedSessionAndSecurity_insertContact.action?isWlight=true',
				data : $('form').serialize(),
				dataType : 'json',
				type: "post",
				success : function(result)
				{
					if (result.success)
					{
						var dia2=$.dialog({
			                title:'温馨提示',
			                content:result.msg,
			                button:["确认"]
			              });
			            
			            dia2.on("dialog:action",function(e){
			                console.log(e.index);
			                if(e.index == 0){
			              	  window.location.href = 'servContact.jsp?category=<%=category %>&accountNum=<%=accountNum%>';
			                }
			            });
						
					} else
					{
						var dia2=$.dialog({
			                title:'温馨提示',
			                content:result.msg,
			                button:["确认"]
			              });
					}
				},
				beforeSend : function()
				{
					
				},
				complete : function()
				{	
					
				}
			});
        	
        	
            
        	
        }
      });
      
      
      
      dia.on("dialog:hide",function(e){
        console.log("dialog hide");
      });
      
      dia2.on("dialog:hide",function(e){
          console.log("dialog hide");
      });
});

</script>

</body>
</html>