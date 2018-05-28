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
		<jsp:include page="../../../inc.jsp"></jsp:include>
		<script type="text/javascript">
		var projectGrid;
		$(function() {
			projectGrid = $('#projectGrid').datagrid({
				url : '${pageContext.request.contextPath}/project/projectAction!dataGrid.action',
				fit : true,
				border : false,
				striped : true,
				rownumbers : true,
				pagination : true,
				idField : 'projectId',
				columns : [ [ {
						field : 'donationId',
						checkbox : true
					},{
						width : '200',
						title : '项目名称',
						field : 'projectName',
						align : 'center'
					},
					{
						width : '100',
						title : '金额',
						field : 'donationMoney',
						align : 'center'
					},
					{
						width : '100',
						title : '排序',
						field : 'seq',
						align : 'center'
					},
					{
						width : '100',
						title : '创建人',
						field : 'createId',
						align : 'center',
						formatter : function(value, row) {
							if(row.user!=undefined){
								return row.user.userName;
							}else{
								return "";
							}
						}
					},
					{
						width : '150',
						title : '创建时间',
						field : 'createTime',
						align : 'center'
					},
					{
						title : '操作',
						field : 'action',
						width : '150',
						formatter : function(value, row) {
							var str = '';
							<authority:authority authorizationCode="查看捐赠项目" role="${sessionScope.user.role}">
								str += '<a href="javascript:void(0)" onclick="showFun(' + row.projectId + ');"><img class="iconImg ext-icon-note"/>查看</a>&nbsp;';
							</authority:authority>
							<authority:authority authorizationCode="编辑捐赠项目" role="${sessionScope.user.role}">
								str += '<a href="javascript:void(0)" onclick="editFun(' + row.projectId + ');"><img class="iconImg ext-icon-note_edit"/>编辑</a>&nbsp;';
							</authority:authority>
							return str;
						}
					} ] ],
					toolbar : '#toolbar',
					onBeforeLoad : function(param) {
						parent.$.messager.progress({
							text : '数据加载中....'
						});
					},
					onLoadSuccess : function(data) {
						$('.iconImg').attr('src', pixel_0);
						parent.$.messager.progress('close');
					}
				});
			});

	 function addFun() {
		var dialog = parent.modalDialog({
			title : '新增捐赠项目',
			iconCls : 'ext-icon-note_add',
			url : '${pageContext.request.contextPath}/page/admin/project/addProject.jsp',
			buttons : [ {
				text : '保存',
				iconCls : 'ext-icon-save',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, projectGrid, parent.$);
				}
			} ]
		});
	};

	function showFun(id) {
		var dialog = parent.modalDialog({
			title : '查看捐赠项目',
			iconCls : 'ext-icon-note',
			url : '${pageContext.request.contextPath}/page/admin/project/viewProject.jsp?id=' + id
		});
	}
	
	function editFun(id) {
		var dialog = parent.modalDialog({
			title : '编辑捐赠项目',
			iconCls : 'ext-icon-note_edit',
			url : '${pageContext.request.contextPath}/page/admin/project/editProject.jsp?id=' + id,
			buttons : [ {
				text : '保存',
				iconCls : 'ext-icon-save',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, projectGrid, parent.$);
				}
			} ]
		});
	}
	
	function removeFun()
	{
		var rows = $('#projectGrid').datagrid('getChecked');
		var ids = [];
		if (rows.length > 0)
		{
			parent.$.messager.confirm('确认', '确定删除吗？', function(r)
			{
				if (r)
				{
					for ( var i = 0; i < rows.length; i++)
					{
						ids.push(rows[i].projectId);
					}
					$.ajax({
						url : '${pageContext.request.contextPath}/project/projectAction!delete.action',
						data : {
							ids : ids.join(',')
						},
						dataType : 'json',
						success : function(data)
						{
							if (data.success)
							{
								$("#projectGrid").datagrid('reload');
								$("#projectGrid").datagrid('unselectAll');
								parent.$.messager.alert('提示', data.msg, 'info');
							} else
							{
								parent.$.messager.alert('错误', data.msg, 'error');
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
			});
		} else
		{
			parent.$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}
	</script>
	</head>

	<body class="easyui-layout" data-options="fit:true,border:false">
		<div id="toolbar" style="display: none;">
			<table>
				<tr>
					<td>
						<form id="searchForm">
							<table>
								<tr>
									<th>
										项目名称
									</th>
									<td>
									<div class="datagrid-btn-separator"></div>
									</td>
									<td>
										<input id="projectName" style="width: 150px;" />
									</td>
									<td>
										<a href="javascript:void(0);" class="easyui-linkbutton"
											data-options="iconCls:'icon-search',plain:true"
											onclick="projectGrid.datagrid('load',{projectName:$('#projectName').val()});">查询</a>
									</td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<td>
									<authority:authority authorizationCode="新增捐赠项目" role="${sessionScope.user.role}">
										<a href="javascript:void(0);" class="easyui-linkbutton"
											data-options="iconCls:'ext-icon-note_add',plain:true"
											onclick="addFun();">新增</a>
									</authority:authority>
								</td>
								<td>
									<authority:authority authorizationCode="删除捐赠项目" role="${sessionScope.user.role}">
										<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-note_delete',plain:true" onclick="removeFun();">删除</a>
									</authority:authority>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',fit:true,border:false">
			<table id="projectGrid"></table>
		</div>
	</body>
</html>
