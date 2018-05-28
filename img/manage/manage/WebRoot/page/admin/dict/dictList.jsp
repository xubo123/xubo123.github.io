<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/authority" prefix="authority"%>
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
			var dictGrid;
			var dictKey;
			$(function() {
				dictKey = parent.$('#dictTree').tree('getSelected').id;
				$('#dictTypeId').attr('value',dictKey);
				dictGrid = $('#dictGrid')
						.datagrid(
								{
									url : '${pageContext.request.contextPath}/dict/dictAction!getDict.action',
									fit : true,
									queryParams:{'id':dictKey},
									method:'post',
									border : false,
									striped : true,
									rownumbers : true,
									pagination : false,
									singleSelect : true,
									columns : [ [
											{
												width : '300',
												title : '值',
												field : 'dictName'
											},
											{
												title : '操作',
												field : 'action',
												width : '80',
												formatter : function(value, row) {
													var str = '';
													<authority:authority authorizationCode="删除属性值" role="${sessionScope.user.role}">
														str += '<a href="javascript:void(0)" onclick="removeFun(\''
																+ row.dictId
																+ '\');"><img class="iconImg ext-icon-note_delete"/>删除</a>';
													</authority:authority>
													return str;
												}
											} ] ],
									toolbar : '#toolbar',
									onBeforeLoad : function(param) {
										parent.parent.$.messager.progress({
											text : '数据加载中....'
										});
									},
									onLoadSuccess : function(data) {
										$('.iconImg').attr('src', pixel_0);
										parent.parent.$.messager.progress('close');
									}
								});
			});
			
			function addDictValue(){
				if ($('#dictForm').form('validate')) {
					$.ajax({
						url:'${pageContext.request.contextPath}/dict/dictAction!addDict.action',
						data :$('#dictForm').serialize(),
						dataType:'json',
						success : function(result){  
							if(result.success){
								$('#dictForm')[0].reset();
							 	$('#dictGrid').datagrid('reload');
							 	parent.parent.$.messager.alert('提示', result.msg, 'info');
							}else{
								parent.parent.$.messager.alert('提示', result.msg, 'error');
							}
				          },
							beforeSend:function(){
								parent.parent.$.messager.progress({
									text : '数据提交中....'
								});
							},
							complete:function(){
								parent.parent.$.messager.progress('close');
							}
					});
				}
			}
			
			function removeFun(dictId){
				parent.parent.$.messager.confirm('确认', '您确定要删除此记录？', function(r) {
					if (r) {
						$.ajax({
							url:'${pageContext.request.contextPath}/dict/dictAction!deleteDict.action',
							data :{'id':dictId},
							dataType:'json',
							success : function(result){  
								if(result.success){
								 	$('#dictGrid').datagrid('reload');
								 	parent.parent.$.messager.alert('提示', result.msg, 'info');
								}else{
									parent.parent.$.messager.alert('提示', result.msg, 'error');
								}
					          },
								beforeSend:function(){
									parent.parent.$.messager.progress({
										text : '数据提交中....'
									});
								},
								complete:function(){
									parent.parent.$.messager.progress('close');
								}
						});
					}
				});
			}
		</script>
	</head>

	<body class="easyui-layout" data-options="fit:true,border:false">
		<div id="toolbar" style="display: none;">
			<form id="dictForm">
				<table>
					<tr>
						<th>
							值：
						</th>
						<td>
							<input name="dictObj.dictTypeId" id="dictTypeId" style="width: 150px;" type="hidden"/>
							<input name="dictObj.dictName" id="dictName" style="width: 150px;" class="easyui-validatebox"
								data-options="required:true,validType:'customRequired'"/>
						</td>
						<td>
							<authority:authority authorizationCode="新增属性值" role="${sessionScope.user.role}">
								<a href="javascript:void(0);" class="easyui-linkbutton"
									data-options="iconCls:'ext-icon-note_add',plain:true"
									onclick="addDictValue();">新增属性值</a>
							</authority:authority>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',fit:true,border:false">
			<table id="dictGrid"></table>
		</div>
	</body>
</html>
