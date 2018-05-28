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
					url : '${pageContext.request.contextPath}/department/departmentAction!getByAlumniId.action',
					data : $('form').serialize(),
					dataType : 'json',
					success : function(result) {
						if (result.department_id != undefined) {
							$('form').form('load', {
								'department.departmentName' : result.departmentName,
								'department.type' : result.type
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
					url = '${pageContext.request.contextPath}/department/departmentAction!updateAlumni.action';
				} else {
					url = '${pageContext.request.contextPath}/department/departmentAction!insertAlumni.action';
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
			<fieldset>
				<legend>
					校友组织基本信息
				</legend>
				<table class="ta001">
					<tr>
						<th>
							组织名称
						</th>
						<td>
							<input name="department.departmentName" class="easyui-validatebox"
								data-options="required:true,validType:'customRequired'" />
						</td>
					</tr>
					<tr>
						<th>
							组织类别
						</th>
						<td>
							<select class="easyui-combobox" data-options="editable:false" name="department.type" style="width: 150px;">
								<option value="2">地方校友会</option>
								<option value="3">行业校友会</option>
								<option value="4">企业校友会</option>
								<option value="5">兴趣校友会</option>
								<option value="6">其他</option>
							</select>
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</body>
</html>
