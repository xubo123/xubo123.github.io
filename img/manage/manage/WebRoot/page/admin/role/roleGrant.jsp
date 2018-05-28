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
			if(${param.flag==0}){
				$('#tree').tree({
					url : '${pageContext.request.contextPath}/resource/resourceAction!doNotNeedSecurity_getGrantTree.action',
					parentField : 'pid',
					checkbox : true,
					onLoadSuccess : function(node, data) {
						$.ajax({
							url : '${pageContext.request.contextPath}/role/roleAction!doNotNeedSecurity_getHasTree.action',
							data : {
								id : $('#roleId').val()
							},
							dataType : 'json',
							success : function(result) {
								if (result) {
									for (var i = 0; i < result.length; i++) {
										var node = $('#tree').tree('find', result[i].id);
										if (node) {
											var isLeaf = $('#tree').tree('isLeaf', node.target);
											if (isLeaf) {
												$('#tree').tree('check', node.target);
											}
										}
									}
								}
							},
							complete:function(){
								parent.$.messager.progress('close');
							}
						});
					},onBeforeLoad:function(node, param){
						parent.$.messager.progress({
							text : '数据加载中....'
						});
					}
				});
			}else{
				$('#tree').tree({
					url : '${pageContext.request.contextPath}/resource/resourceAction!doNotNeedSecurity_getxGrantTree.action',
					parentField : 'pid',
					checkbox : true,
					onLoadSuccess : function(node, data) {
						$.ajax({
							url : '${pageContext.request.contextPath}/role/roleAction!doNotNeedSecurity_getHasTree.action',
							data : {
								id : $('#roleId').val()
							},
							dataType : 'json',
							success : function(result) {
								if (result) {
									for (var i = 0; i < result.length; i++) {
										var node = $('#tree').tree('find', result[i].id);
										if (node) {
											var isLeaf = $('#tree').tree('isLeaf', node.target);
											if (isLeaf) {
												$('#tree').tree('check', node.target);
											}
										}
									}
								}
							},
							complete:function(){
								parent.$.messager.progress('close');
							}
						});
					},onBeforeLoad:function(node, param){
						parent.$.messager.progress({
							text : '数据加载中....'
						});
					}
				});
			}
		});
		var submitForm = function($dialog, $grid, $pjq) {
			var nodes = $('#tree').tree('getChecked', [ 'checked', 'indeterminate' ]);
			var ids = [];
			for (var i = 0; i < nodes.length; i++) {
				ids.push(nodes[i].id);
			}
			$.ajax({
				url : '${pageContext.request.contextPath}/role/roleAction!grant.action',
				data : {
					id : $('#roleId').val(),
					ids : ids.join(',')
				},
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
					$pjq.messager.progress({
						text : '数据提交中....'
					});
				},
				complete:function(){
					$pjq.messager.progress('close');
				}
			});
		};
		</script>
	</head>

	<body>
		<input name="role.roleId" value="${param.id}" type="hidden" id="roleId">
		<fieldset>
			<legend>
				角色授权
			</legend>
			<ul id="tree"></ul>
		</fieldset>
	</body>
</html>
