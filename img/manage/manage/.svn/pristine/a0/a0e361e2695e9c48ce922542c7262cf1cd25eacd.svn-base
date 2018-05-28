<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<jsp:include page="../../../inc.jsp"></jsp:include>


<script type="text/javascript">

	//编辑器里面的内容图片上传
	KindEditor.ready(function(K) {
		K.create('#content',{
			 items : [
			          'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
			          'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
			          'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
			          'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
			          'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
			          'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image',
			          'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
			          'anchor', 'link', 'unlink', '|', 'about'
			  ],
	    	 uploadJson : '${pageContext.request.contextPath}/fileUpload/fileUploadAction!doNotNeedSecurity_fileUploadK.action',
	         afterChange:function(){
		        	this.sync();
		        }
	    });
	});

	//封面图上传
	$(function() {
		var button = $("#news_upload_button"), interval;
		new AjaxUpload(button, {
			action : '${pageContext.request.contextPath}/fileUpload/fileUploadAction!doNotNeedSecurity_fileUploadNews.action',
			name : 'upload',
			onSubmit : function(file, ext) {
				if (!(ext && /^(jpg|jpeg|png|gif|bmp)$/.test(ext))) {
					$.messager.alert('提示', '您上传的图片格式不对，请重新选择！', 'error');
					return false;
				}
				$.messager.progress({
					text : '图片正在上传,请稍后....'
				});
			},
			onComplete : function(file, response) {
				$.messager.progress('close');
				var msg = $.parseJSON(response);
				if (msg.error == 0) {
					$('#newsPic').append('<div style="float:left;width:180px;"><img src="'+msg.url+'" width="150px" height="150px"/><div class="bb001" onclick="removeNewsPic(this)"></div><input type="hidden" name="news2.pic" value="'+msg.no_domain_url+'"/></div>');
					$("#news_upload_button").prop('disabled', 'disabled');
				} else {
					$.messager.alert('提示', msg.message, 'error');
				}
			}
		});
		
	});

	function removeNewsPic(newsPic) {
		$(newsPic).parent().remove();
		$("#news_upload_button").prop('disabled', false);
	}

	
	function submitForm($dialog, $grid, $pjq){

		if($('#introduction').val()==''){
			parent.$.messager.alert('提示', '请输入新闻简介', 'error');
			return false;
		}
		if($('#newsUrl').val()==''){
			parent.$.messager.alert('提示', '请输入新闻网页链接', 'error');
			return false;
		}
		if($('input[name="news2.pic"]').val()==undefined){
			parent.$.messager.alert('提示', '请上传活动封面图片', 'error');
			return false;
		}
		
		//手机1级栏目
		var type1 = $("#newsType1").val();
/* 		//手机2级栏目
		var type2 = $("#newsType2").val(); */
		if(type1==0){
			parent.$.messager.alert('提示', '请选择手机或网页新闻栏目', 'error');
			return false;
		}	
/* 		if((type1 > 0) && (type2==0) && $("#newsType2 option").length>1  ){
			parent.$.messager.alert('提示', '请选择手机新闻二级栏目', 'error');
			return false;
		} */
						
						
		if ($('form').form('validate'))
		{
			var convert = "${convert}";
			var URL = "";
			if(convert==""){
				URL = "${pageContext.request.contextPath}/mobile/news/newsAction!update.action";
			}else if(convert=="1"){
				//新闻添加,将地方新闻COPY一份转为总会新闻
				$("#newsId").val("0");
				URL = "${pageContext.request.contextPath}/mobile/news/newsAction!save.action";
			}
			
			$.ajax({
				url : URL,
				data : $('form').serialize(),
				dataType : 'json',
				success : function(result)
				{
					if (result.success)
					{
						$grid.datagrid('reload');
						$dialog.dialog('destroy');
						$pjq.messager.alert('提示', result.msg, 'info');
					} else
					{
						$pjq.messager.alert('提示', result.msg, 'error');
					}
				},
				beforeSend : function()
				{
					parent.$.messager.progress({
						text : '数据提交中....'
					});
				},
				complete : function()
				{
					parent.$.messager.progress('close');
				}
			});
		}
	};
	
	
 	/**--新闻一级栏目类别修改事件--**/
/*	function selectNewsType1(){
		if($("#newsType1").val()=="0"){
			$("#newsType2").empty();
			var html = "<option value='0' >请选择</option>";
			$("#newsType2").append(html);
			return;
		}
		//通过ajax取2级栏目
		var params = "parent_id="+$("#newsType1").val()+"&type=0";
		$.ajax({
             type: "GET",
             url: "${pageContext.request.contextPath}/mobile/news/newsAction!doNotNeedSessionAndSecurity_getNewsTypeByParent.action",
             data: params,
             dataType: "json",
             success: function(data){
             	$("#newsType2").empty(); 
             	var html = "<option value='0' >请选择</option>";
             	for(var i=0;i<data.length;i++){
             		var obj = data[i];
             		html += "<option value=\"" +obj.id+  "\">" + obj.channel_name + "</option>";
             	}
             	$("#newsType2").append(html);
             }
     	});	
	} */
</script>
</head>
  
<body>
<form method="post" id="editNewsForm" class="form">
	<fieldset>
		<legend>
			新闻基本信息
		</legend>
		<table class="ta001">
			<tr>
				<th>
					标题
				</th>
				<td colspan="3">
					<input name="news2.newsId" type="hidden" id="newsId" 
							value="${news2.newsId}">
					<input name="news2.title" class="easyui-validatebox"
						style="width: 700px;"
						data-options="required:true,validType:'customRequired'"
						maxlength="30" value="${news2.title}"/>
				</td>
			</tr>
			<tr>
				<th>
					新闻简介
				</th>
				<td colspan="3">
					<textarea id="introduction" rows="7" cols="100"
						name="news2.introduction">${news2.introduction}</textarea>
				</td>
			</tr>
			<tr>
				<th>
					网页链接
				</th>
				<td colspan="3">
					<textarea id="newsUrl" rows="3" cols="100" name="news2.newsUrl">${news2.newsUrl}</textarea>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<input id="channelId" type = "hidden"
						name="news2.tagId" value="${news2.tagId}"></input>
				</td>
			</tr>
			<tr>
				<th>
					频道
				</th>
				<td colspan="3">
					<input id="channel" name="news2.tagName" class="easyui-combobox" style="width: 150px;" value="${news2.tagName}"
						data-options="editable:false,required:true,
							        valueField: 'tagId',
							        textField: 'tagName',
							        url: '${pageContext.request.contextPath}/newsChannel/newsChannelAction!doNotNeedSecurity_initType.action',
							        onSelect: function(rec){
							        $('#channelId').prop('value',rec.tagId);
							        }
							        " />
				</td>
			</tr>
			
			<tr>
				<th>
					栏目
				</th>
				<td colspan="3">
					<select id="newsType1" name="type1">
						<option value="0">请选择</option>
						<c:forEach var="type" items="${list1}">
							<option value="${type.id }" <c:if test="${pCategory==type.id}">selected="selected"</c:if> >${type.channel_name}</option>
						</c:forEach>
					</select>
				<%-- 	二级栏目
					<select id="newsType2" name="type2">
						<option value="0">请选择</option>
						<c:forEach var="type" items="${list2}">
						<option value="${type.id }" <c:if test="${news2.category==type.id}">selected="selected"</c:if>>${type.channel_name }</option>
						</c:forEach>
					</select> --%>
				</td>
			</tr>	
			<tr>
				<th>
					时间
				</th>
				<td colspan="3">
					<input name="news2.createTime" class="easyui-datetimebox" style="width: 150px;" data-options="editable:false" value="${createTime}" />
				</td>
			</tr>
				
			<tr>
				<th>
					新闻内容
				</th>
				<td colspan="3">
					<textarea id="content" name="news2.content"
						style="width: 700px; height: 300px;">${news2.content}</textarea>
				</td>
			</tr>
			
			<tr>
				<th>
					新闻封面上传
				</th>
				<td colspan="3">
					<c:choose>
						<c:when test="${news2.pic!=null and news2.pic!=''}">
							<input type="button" id="news_upload_button" value="上传图片" disabled="disabled">
						</c:when>
						<c:otherwise>
							<input type="button" id="news_upload_button" value="上传图片">
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th>
					新闻封面图片
				</th>
				<td colspan="3">
					<div id="newsPic">
						<c:if test="${news2.pic!=null and news2.pic!=''}">
							<div style="float:left;width:180px;"><img src="${news2.picUrl}" width="150px" height="150px"/><div class="bb001" onclick="removeNewsPic(this)"></div><input type="hidden" name="news2.pic" value="${news2.pic}"/></div>
						</c:if>
					</div>
				</td>
			</tr>
		</table>
	</fieldset>
</form>
</body>
</html>