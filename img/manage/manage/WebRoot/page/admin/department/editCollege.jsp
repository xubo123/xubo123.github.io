<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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
		$(function() {
			if ($('#departmentId').val() > 0) {
				$.ajax({
					url : '${pageContext.request.contextPath}/department/departmentAction!getByCollegeId.action',
					data : $('form').serialize(),
					dataType : 'json',
					success : function(result) {
						if (result.department_id != undefined) {
							$('form').form('load', {
								'department.departmentName' : result.departmentName
							});
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
		});
		var submitForm = function($dialog, $grid, $pjq) {
			if ($('form').form('validate')) {
				var url;
				if ($('#departmentId').val() > 0) {
					url = '${pageContext.request.contextPath}/department/departmentAction!updateCollege.action';
				} else {
					url = '${pageContext.request.contextPath}/department/departmentAction!insertCollege.action';
				}
				$.ajax({
					url : url,
					data : $('form').serialize(),
					dataType : 'json',
					success : function(result) {
						if (result.success) {
							$grid.datagrid('reload');
							$dialog.dialog('destroy');
							$pjq.messager.alert('提示', result.msg, 'info');
						} else {
							$pjq.messager.alert('提示', result.msg, 'error');
						}
					},
					beforeSend:function(){
						parent.$.messager.progress({
							text : '数据提交中....'
						});
					},
					complete:function(){
						parent.$.messager.progress('close');
					}
				});
			}
		};
		</script>
	</head>

	<body>
		<form method="post" class="form">
		<input name="department.department_id" type="hidden" id="departmentId" value="${param.id}">
		<input name="department.type" type="hidden" id="type" value="1">
			<fieldset>
				<legend>
					当前院系基本信息
				</legend>
				<table class="ta001">
					<tr>
						<th>
							院系名称
						</th>
						<td>
							<input name="department.departmentName" class="easyui-validatebox"
								data-options="required:true,validType:'customRequired'" />
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</body>
</html>
