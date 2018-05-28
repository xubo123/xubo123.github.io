/**
 * 学校服务JS
 */



function initSchoolServ()
{
	
	
	$.ajax({
		url : "schoolServAction!doNotNeedSessionAndSecurity_getServiceList.action",
		dataType : 'json',
        type: "POST",
		
        success: function(data) 
        {
        	
        	var servList = data;
        	
        	if(servList != null && servList.length > 0)
        	{
        		var i = 0;
        		for(i = 0; i < servList.length; i++)
        		{
        			buildHTMLForServList("servList",servList[i]);
        		}
        		
        		
        		while(true)
        		{
        			if( i % 4 != 0)
        			{
        				var ul = document.getElementById("servList");
                		ul.innerHTML += '<li class="pure-u-1-4"></li>';
                		i++;
        			}
        			else
        			{
        				break;
        			}
        		}
        		
        		
        		
        	}
        	else
        	{
        		var ul = document.getElementById("servList");
        		ul.innerHTML = '暂无服务！';
        	}
        	
        }
    });	
}


function buildHTMLForServList(divId, schoolServ)
{
	var ul = document.getElementById(divId);
	
	var userId = document.getElementById("userId");
	
	var html = 
		'<li class="pure-u-1-4">' +
		'<a href="'+schoolServ.serviceUrl+(schoolServ.systemService == '1' ? userId.value:'')+'">' +
		'<img src="'+schoolServ.servicePic+'" width="48" height="48" alt="">' +
		'<h4>'+schoolServ.serviceName+'</h4>' +
		'</a>' +
		'</li>';
	
	ul.innerHTML += html;
	
}


function sendMessage(messageType,messageUserId)
{
	var title = document.getElementById('messageTitle');
	var content = document.getElementById('messageContent');
	

	
	if(title.value == '')
	{
		alert('请输入主题');
	}
	else if(content.value == '')
	{
		alert('请输入内容');
	}
	else	
	{
		
		$.ajax({
			url : "../messageBoard/messageBoardAction!messageSend.action",
			data : $('form').serialize(),
			dataType : 'json',
	        type: "POST",
			
	        success: function(data) 
	        {
	        	if(data.status == 0)
	        	{
	        		alert('发送成功，请等待审核回复');
	        		//alert('发布成功');
	        		
	        		$("#wrapper").animate({left:'0'});
	        		$("#posts").animate({left:'100%'}).fadeOut(0);
	        		$(".psfooter").hide();
	        		$(".plfooter").show();
	        		
	        	}
	        	else if(data.status == 1)
	        	{
	        		alert('发送失败');
	        	}
	        	
	        }
	    });
	}

}


var totalRows = 0;

var oldCurrentRow = 0;


function initMessageList()
{
	//alert($('form').serialize());
	document.getElementById('scroller').style.display = 'none';
	document.getElementById('loading').style.display = 'block';
	
	
	$.ajax({
		url : "../messageBoard/messageBoardAction!pullContactList.action",
		data : $('form').serialize(),
		dataType : 'json',
        //type: "POST",
		
        success: function(data) 
        {
        	
        	var messageList = data.list;
        	var currentRow = document.getElementById('currentRow');
        	
        	document.getElementById('scroller').style.display = 'block';
        	document.getElementById('loading').style.display = 'none';
        	
    		if(messageList != null && messageList.length > 0)
    		{
    			totalRows = totalRows + messageList.length;
    			
    			currentRow.value = totalRows;
        		
    			buildHTML(messageList, true);
    			
    			
    			document.getElementById('pullUp').style.display = 'block';
    			
    		}
    		else
    		{
    			if(reply == null || reply.value != '1')
    			{
        			document.getElementById('nodata').style.display = 'block';
    			}
    			
    			
    			document.getElementById('pullUp').style.display = 'none';
    		}
        },
        
        complete: function()
        {
        	myScroll.refresh();
        }
    });

}


function pullDownMessageList()
{
	
	var currentRow = document.getElementById('currentRow');
	var incremental = document.getElementById('incremental');
	oldCurrentRow = currentRow.value;
	incremental.value = totalRows;
	currentRow.value = 0;
	//alert($('form').serialize());
	$.ajax({
		url : "../messageBoard/messageBoardAction!pullContactList.action",
		data : $('form').serialize(),
		dataType : 'json',
        //type: "POST",
		
        success: function(data) 
        {
        	var messageList = data.list;
        	buildHTML(messageList, true);
        },
        
        complete: function()
        {
        	currentRow.value = oldCurrentRow;
        	myScroll.refresh();
        }
    });

}
function pullUpMessageList()
{
	//alert($('form').serialize());
	
	var refresh = document.getElementById('refresh'); if(refresh != null){refresh.value == '1';}	
	$.ajax({
		url : "../messageBoard/messageBoardAction!pullContactList.action",
		data : $('form').serialize(),
		dataType : 'json',
        //type: "POST",
		
        success: function(data) 
        {
        	var messageList = data.list;
        	var currentRow = document.getElementById('currentRow');
        	
    		if(messageList != null && messageList.length > 0)
    		{
    			totalRows = totalRows + messageList.length;
    			
    			currentRow.value = totalRows;
        		
    			
    			buildHTML(messageList, false);
    			
    		}
        },
        
        complete: function()
        {
        	myScroll.refresh();
        }
    });

}




function buildHTML(messageList, isRefresh)
{
	if(messageList != null && messageList.length > 0)
	{
		var el = document.getElementById('thelist');
		
		var messageUserId = document.getElementById('messageUserId');

		if(isRefresh)
		{
			el.innerHTML = '';
		}
		
		

		for(var i = 0; i < messageList.length; i++)
		{
			var messageBoard = messageList[i];
			
			var li = document.createElement('li');
			
			var html = 
				'<div class="post-content">' +
	        	'	<div class="post">' +
	        	'		<h3>' + messageBoard.messageTitle + '</h3>' +
	        	'		<div class="detail">' + messageBoard.messageContent + '</div>' +
	        	'		<div class="post-time">' + messageBoard.pastTime + '</div>' +
	        	'		<div class="user-info">' +
	        	'			<div class="avatar"><img src="' + getIcons(messageBoard.messageUserImageURL) + '" width="64" height="64"></div>' +
	        	'		</div>' +
	        	'	</div>';
	        	
				//alert(messageBoard.replyMessageContent);
	        	
	        	if(messageBoard.replyMessageContent == null || messageBoard.replyMessageContent == '')
	        	{
	        		html += 
	        		'	<div class="reply">' +
		        	'		<div class="no-reply">暂未回复</div>' +
		        	'	</div>';
	        	}
	        	else
	        	{
	        		html += 
	    		        '	<div class="reply">' +
	    		        '		<h3>' + theme + '回复：</h3>' +
	    		        '		<div class="detail">' + messageBoard.replyMessageContent + '</div>' +
	    		        //'		<div class="reply-time">' + replyMessageBoard.pastTime + '</div>' +
	    		        '	</div>';
	        	}
	        	
	        	html += '</div>';
	        	
	        	

	        	li.innerHTML = html;
				
				el.appendChild(li, el.childNodes[0]);
		}
		
	}
}







