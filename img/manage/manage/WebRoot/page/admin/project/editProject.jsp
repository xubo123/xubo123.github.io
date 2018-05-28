<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
		var editor;
		KindEditor.ready(function(K) {
			editor=K.create('#content',{
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
		$(function() {
			var projectId=$('#projectId').val();
			if (projectId > 0) {
				$.ajax({  
					url:'${pageContext.request.contextPath}/project/projectAction!doNotNeedSecurity_getById.action',
					data :{'id':projectId},
					dataType:'json',
					success : function(result){  
						if (result.projectId != undefined) {
							$('form').form('load', {
								'project.projectName' : result.projectName,
								'project.seq' : result.seq,
								'project.introduction' : result.introduction
							});
							KindEditor.ready(function(K) {
								K.insertHtml('#content', result.content);
							});
							if(result.projectPic!=undefined){
								$('#projectPic').append('<div style="float:left;width:180px;"><img src="'+result.projectPic+'" width="150px" height="150px"/><div class="bb001" onclick="removeProjectPic(this)"></div><input type="hidden" name="project.projectPic" value="'+result.projectPic+'"/></div>');
								$("#news_upload_button").prop('disabled', 'disabled');
							}
						}
			          },
					  beforeSend:function(){
						parent.$.messager.progress({
							text : '数据加载中....'
						});
					  },
					  complete:function(){
						parent.$.messager.progress('close');
					  }  
				});
			}
			
			var button = $("#news_upload_button"), interval;
			new AjaxUpload(button, {
				action : '${pageContext.request.contextPath}/fileUpload/fileUploadAction!doNotNeedSecurity_fileUploadNews.action',
				name : 'upload',
				onSubmit : function(file, ext) {
					if (!(ext && /^(jpg|png|gif|bmp)$/.test(ext))) {
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
						$('#projectPic').append('<div style="float:left;width:180px;"><img src="'+msg.url+'" width="150px" height="150px"/><div class="bb001" onclick="removeProjectPic(this)"></div><input type="hidden" name="project.projectPic" value="'+msg.url+'"/></div>');
						$("#news_upload_button").prop('disabled', 'disabled');
					} else {
						$.messager.alert('提示', msg.message, 'error');
					}
				}
			});
		})
		
		function removeProjectPic(newsPic) {
			$(newsPic).parent().remove();
			$("#news_upload_button").prop('disabled', false);
		}
		
		function submitForm($dialog, $grid, $pjq)
		{
			if($('#introduction').val()==''){
				parent.$.messager.alert('提示', '请输入项目简介', 'error');
				return false;
			}
			if (editor.isEmpty()) {
				parent.$.messager.alert('提示', '请输入项目内容', 'error');
				return false;
			}
			if($('input[name="project.projectPic"]').val()==undefined){
				parent.$.messager.alert('提示', '请上传封面图片', 'error');
				return false;
			}
			if ($('form').form('validate'))
			{
				$.ajax({
					url : '${pageContext.request.contextPath}/project/projectAction!update.action',
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
		}
	</script>
	</head>

	<body>
		<form method="post">
			<fieldset>
				<legend>
					基本信息
				</legend>
				<table class="ta001">
					<tr>
						<th>
							捐赠项目
						</th>
						<td>
							<input name="project.projectId" id="projectId" type="hidden" value="${param.id}">
							<input name="project.projectName" class="easyui-validatebox" data-options="required:true,validType:'customRequired'"/>
						</td>
					</tr>
					<tr>
						<th>
							排序
						</th>
						<td>
							<input name="project.seq" class="easyui-validatebox" value="1" data-options="required:true" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></input>
						</td>
					</tr>
					<tr>
						<th>
							项目简介
						</th>
						<td>
							<textarea rows="5" id="introduction" cols="100" name="project.introduction"></textarea>
						</td>
					</tr>
					<tr>
						<th>
							项目内容
						</th>
						<td colspan="3">
							<textarea id="content" name="project.content"
								style="width: 700px; height: 300px;"></textarea>
						</td>
					</tr>
					<tr>
						<th>
							封面上传
						</th>
						<td colspan="3">
							<input type="button" id="news_upload_button" value="上传图片">
						</td>
					</tr>
					<tr>
						<th>
							封面图片
						</th>
						<td colspan="3">
							<div id="projectPic"></div>
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</body>
</html>
