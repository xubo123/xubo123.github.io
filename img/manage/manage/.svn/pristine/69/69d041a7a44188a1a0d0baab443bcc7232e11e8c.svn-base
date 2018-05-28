<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	function submitForm($dialog, $grid, $pjq)
	{
		if ($('form').form('validate'))
		{
			$.ajax({
				url : '${pageContext.request.contextPath}/dicttype/dictTypeAction!updateDictType.action',
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
</script>
</head>
	<body>
	<form method="post" class="form">
			<fieldset>
				<legend>
					字典类别
				</legend>
				<table class="ta001">
					<tr>
						<th>
							字典类别名称：
						</th>
						<td colspan="3">
							<input name="dictTypeObj.dictTypeId"  type="hidden" value="${dictTypeObj.dictTypeId}">
							<input name="dictTypeObj.dictTypeName" id="dictTypeName" value="${dictTypeObj.dictTypeName}" class="easyui-validatebox" data-options="required:'true'" />
						</td>
					</tr>
					<tr>
						<th>
							值：
						</th>
						<td colspan="3">
							<input name="dictTypeObj.dictTypeValue" id="dictTypeValue" value="${dictTypeObj.dictTypeValue}" class="easyui-validatebox" data-options="validType:'numbers',required:'true'" />
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</body>
</html>
