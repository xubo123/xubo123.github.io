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
		KindEditor.ready(function(K) {
			var editor=K.create('#content',{
				items : [
					'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
					'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
					'insertunorderedlist', '|', 'emoticons', 'image', 'link','preview','fullscreen'
				 ],
		    	 uploadJson : '${pageContext.request.contextPath}/fileUpload/fileUploadAction!doNotNeedSecurity_fileUploadK.action',
		         afterChange:function(){
			        	this.sync();
			        }
		    });
			editor.readonly();
		});
		$(function() {
			var projectId=$('#projectId').val();
			if (projectId > 0) {
				$.ajax({  
					url:'${pageContext.request.contextPath}/project/projectAction!getById.action',
					data :{'id':projectId},
					dataType:'json',
					success : function(result){  
						if (result.projectId != undefined) {
							$('form').form('load', {
								'project.projectName' : result.projectName,
								'project.seq' : result.seq,
								'project.introduction' : result.introduction,
								'creator' : result.user!=undefined?result.user.userName:'',
								'project.createTime' : result.createTime,
								'project.donationMoney':result.donationMoney
							});
							KindEditor.ready(function(K) {
								K.insertHtml('#content', result.content);
							});
							if(result.projectPic!=undefined){
								$('#projectPic').append('<div style="float:left;width:180px;"><img src="'+result.projectPic+'" width="150px" height="150px"/><input type="hidden" name="project.projectPic" value="'+result.projectPic+'"/></div>');
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
		})
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
							<input name="project.projectName" class="easyui-validatebox" disabled="disabled"/>
						</td>
					</tr>
					<tr>
						<th>
							金额
						</th>
						<td>
							<input name="project.donationMoney" class="easyui-validatebox" disabled="disabled"/>
						</td>
					</tr>
					<tr>
						<th>
							排序
						</th>
						<td>
							<input name="project.seq" class="easyui-validatebox" disabled="disabled"></input>
						</td>
					</tr>
					<tr>
						<th>
							创建人
						</th>
						<td>
							<input name="creator" class="easyui-validatebox" disabled="disabled"></input>
						</td>
					</tr>
					<tr>
						<th>
							创建时间
						</th>
						<td>
							<input name="project.createTime" class="easyui-validatebox" disabled="disabled"></input>
						</td>
					</tr>
					<tr>
						<th>
							项目简介
						</th>
						<td>
							<textarea rows="5" disabled="disabled" cols="100" name="project.introduction"></textarea>
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
