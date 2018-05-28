<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/authority" prefix="authority"%>
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
		var userProfileGrid;
		$(function(){
			userProfileGrid = $('#userProfileGrid').datagrid({
				url : '${pageContext.request.contextPath}/appuser/appUserAction!dataGrid.action',
				fit : true,
				border : false,
				striped : true,
				rownumbers : true,
				pagination : true,
				singleSelect : true,
				idField : 'id',
				columns : [ [
					{
						width : '100',
						title : '帐号',
						field : 'user_id',
						align : 'center'
					},
					{
						width : '80',
						title : '姓名',
						field : 'user_name',
						align : 'center'
					},
					{
						width : '50',
						title : '性别',
						field : 'user_sex',
						align : 'center',
						formatter : function(value, row) {
                        	if(value == 0) {
                        		return "男";
                        	} else if(value == 1) {
                        		return "女";
                        	} else {
                        		return "";
                        	}
						}  
					},
					{
						width : '60',
						title : '所在地',
						field : 'user_city',
						align : 'center'
					},
					{
						width : '60',
						title : '行业',
						field : 'user_profession',
						align : 'center'
					},
					{
						width : '100',
						title : '电话号码',
						field : 'user_mobile',
						align : 'center'
					},
					{
						width : '120',
						title : '电子邮箱',
						field : 'user_email',
						align : 'center'
					},
					{
						width : '300',
						title : '学习经历',
						field : 'fullClassName',
						align : 'center',
						formatter : function(value, row)
						{
							var text='';
							if(value != null) {
								var array = value.split(',');
								for(var i=0;i<array.length;i++){
									if(i==array.length-1){
										text+=array[i];
									}
									else{
										text+=array[i]+ "<br />";
									}
								}
							}
							return text;
						}
					},
					{
						title : '操作',
						field : 'action',
						width : '120',
						formatter : function(value, row) {
							var str = '';
							<authority:authority role="${sessionScope.user.role}" authorizationCode="查看帐号">
								str += '<a href="javascript:void(0)" onclick="viewFun(\'' + row.id + '\');"><img class="iconImg ext-icon-note"/>查看</a>&nbsp;';
							</authority:authority>
							<authority:authority role="${sessionScope.user.role}" authorizationCode="删除帐号">
								str += '<a href="javascript:void(0)" onclick="removeFun(' + row.id + ');"><img class="iconImg ext-icon-note_delete"/>删除</a>';
							</authority:authority>
							return str;
						}
					}
					] ],
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
		function searchUserProfile(){
			  if ($('#searchForm').form('validate')) {
				  $('#userProfileGrid').datagrid('load',serializeObject($('#searchForm')));
			  }
		}
		
		function resetT(){				
			$('#searchForm')[0].reset();
		}
		  function removeFun(id){
					parent.$.messager.confirm('确认', '确定删除吗？', function(r) {
						if (r) {
							$.ajax({
								url : '${pageContext.request.contextPath}/appuser/appUserAction!delete.action',
								data : {
									ids : id
								},
								dataType : 'json',
								success : function(data) {
									if(data.success){
										$("#userProfileGrid").datagrid('reload');
										$("#userProfileGrid").datagrid('unselectAll');
										parent.$.messager.alert('提示',data.msg,'info');
									}
									else{
										parent.$.messager.alert('错误', data.msg, 'error');
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
					});
			}
			
			
	var viewFun = function(id)
	{
		var dialog = parent.parent.WidescreenModalDialog({
			title : '查看帐号',
			iconCls : 'ext-icon-note',
			url : '${pageContext.request.contextPath}/page/admin/appuser/viewAppuser.jsp?id=' + id
		});
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
										帐号
									</th>
									<td>
										<div class="datagrid-btn-separator"></div>
									</td>
									<td>
										<input name="user_id" style="width: 150px;" />
									</td>
									<th>
										姓名
									</th>
									<td>
										<div class="datagrid-btn-separator"></div>
									</td>
									<td>
										<input name="user_name" style="width: 150px;" />
									</td>
									<td>
										<a href="javascript:void(0);" class="easyui-linkbutton"
											data-options="iconCls:'icon-search',plain:true"
											onclick="searchUserProfile();">查询</a>
										<a href="javascript:void(0);" class="easyui-linkbutton"
		                                   data-options="iconCls:'ext-icon-huifu',plain:true"
		                                   onclick="resetT();">重置</a>
									</td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center',fit:true,border:false">
			<table id="userProfileGrid"></table>
		</div>
	</body>
</html>
