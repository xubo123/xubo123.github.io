<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="authority" uri="/authority"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新闻栏目管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<jsp:include page="../../../inc.jsp"></jsp:include>
<script type="text/javascript">
var grid;
$(function(){
	grid=$('#newsGrid').datagrid({
		url : '${pageContext.request.contextPath}/mobNewsType/mobNewsTypeAction!dataGridNewsType.action',
		fit : true,
		border : false,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		pagination : true,
		idField : 'id',
		 columns:[[  
				{field:'id',checkbox : true}, 
			    {field:'channel_name',title:'名称',width:150,align:'center'}, 
			  /*   {field:'channel_pid',title:'级别',width:60,align:'center',
			    	formatter: function(value,row,index){
						if(value==0){
							return "一级";
						}else{
							return "二级";
						}
					}	
			    }, */
			    {field:'channel_type',title:'类型',width:100,align:'center',
			    	formatter: function(value,row,index){
						if(value==1){
							return "新闻类别";
						}else if(value==2){
							return "链接";
						}
					}
			    },
			    {field:'channel_ordernum',title:'排序',width:100,align:'center'}, 
			    /*
			    {field:'origin',title:'来源',width:100,align:'center',
			    	formatter: function(value,row,index){
						if(value==1){
							return "总会";
						}else if(value==2){
							return "地方";
						}
					}
			    },
			    {field:'cityName',title:'地域',width:100,align:'center',
			    	formatter: function(value,row,index){
						if(row.origin==1){
							return "---";
						}else if(row.origin==2){
							return value;
						}
					}
			    },
			    {field:'isNavigation',title:'导航显示',width:50,align:'center',
			    	formatter: function(value,row,index){
						if(row.isNavigation==0){
							return "×";
						}else if(row.isNavigation==1){
							return "√";
						}
					}
			    },
			    {field:'orderNum',title:'排序编号',width:50,align:'center'}, */
			    {field:'operator',title:'操作',width:150,align:'center',
			    		formatter: function(value,row,index){
							var content="";
							<authority:authority authorizationCode="查看手机新闻栏目" role="${sessionScope.user.role}">
			    			content+='<a href="javascript:void(0)" onclick="viewType1('+row.id+')"><img class="iconImg ext-icon-note"/>查看</a>&nbsp;';
			    			</authority:authority>
			    			<authority:authority authorizationCode="编辑手机新闻栏目" role="${sessionScope.user.role}">
			    			content+='<a href="javascript:void(0)" onclick="editType('+row.id+')"><img class="iconImg ext-icon-note_edit"/>编辑</a>&nbsp;';
			    			</authority:authority>
			    		/* 	<authority:authority authorizationCode="查询手机新闻栏目" role="${sessionScope.user.role}">
			    			content+='<a href="javascript:void(0)" onclick="viewType2('+row.id+','+row.origin+')"><img class="iconImg ext-icon-note"/>子栏目</a>&nbsp;';
			    			</authority:authority> */
			    			return content;
					}}
			    ]],
				toolbar : '#newsToolbar',
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

	function searchNews(){
		  if ($('#searchNewsForm').form('validate')) {
			  $('#newsGrid').datagrid('load',serializeObject($('#searchNewsForm')));
		  }
	}
	
	/**--重置--**/
	function resetT(){	
		$("#cityName").attr("disabled",true);	
		$('#searchNewsForm')[0].reset();
		$('#type').combobox('clear');
		$('#origin').combobox('clear');		
	}

	/**--新增类型--**/
	function addType() {
		var dialog = parent.modalDialog({
			title : '新增一级栏目',
			iconCls:'ext-icon-note_add',
			url : '${pageContext.request.contextPath}/page/admin/newsType/add.jsp',
			buttons : [ {
				text : '保存',
				iconCls : 'ext-icon-save',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	}
	
	/**--编辑类型--**/
	function editType(id) {
		var dialog = parent.modalDialog({
			title : '编辑',
			iconCls:'ext-icon-note_add',
			url : '${pageContext.request.contextPath}/page/admin/newsType/edit.jsp?id='+id,
			buttons : [ {
				text : '保存',
				iconCls : 'ext-icon-save',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	}




	/**--查看类型详情--**/
	function viewType1(id) {
		var dialog = parent.modalDialog({
			title : '查看',
			iconCls:'ext-icon-note_add',
			url : '${pageContext.request.contextPath}/page/admin/newsType/view.jsp?id=' + id
		});
	}
	
	/**--查看子类型--**/
	function viewType2(id,origin) {
		var dialog = parent.modalDialog({
			title : '子栏目',
			iconCls:'ext-icon-note_add',
			url : '${pageContext.request.contextPath}/page/admin/newsType/list2.jsp?parent_id=' + id+'&origin='+origin,
		});
	}


/**--删除--**/
function removeType(){
	
	var rows = $("#newsGrid").datagrid('getChecked');
	var ids = [];
	if (rows.length > 0) {
		$.messager.confirm('确认', '确定删除吗？', function(r) {
			if (r) {
				for ( var i = 0; i < rows.length; i++) {
					ids.push(rows[i].id);
				}
				$.ajax({
					url : '${pageContext.request.contextPath}/mobNewsType/mobNewsTypeAction!deleteNewsType.action',
					data : {
						ids : ids.join(',')
					},
					dataType : 'json',
					success : function(data) {
						if(data.success){
							$("#newsGrid").datagrid('reload');
							$("#newsGrid").datagrid('unselectAll');
							$.messager.alert('提示',data.msg,'info');
						}
						else{
							$.messager.alert('错误', data.msg, 'error');
						}
					},
					beforeSend:function(){
						$.messager.progress({
							text : '数据提交中....'
						});
					},
					complete:function(){
						$.messager.progress('close');
					}
				});
			}
		});
	} else {
		 $.messager.alert('提示', '请选择要删除的记录！', 'error');
	}
}


	function selectOrigin(){
		var value = $("#origin").val();
		if(value=="2"){
			$("#cityName").attr("disabled",false);
		}else{
			$("#cityName").attr("disabled",true);
			$("#cityName").val("");
		}
	}
</script>
</head>
  
  <body>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div id="newsToolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<form id="searchNewsForm">
						<table>
							<tr>
								<th>
									名称
								</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
								<td>
									<input name="channel.channel_name" style="width: 150px;" />
								</td>
								<th>
									类型
								</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
								<td>
									<select id="type" name="channel.channel_type" style="width: 150px;">
										<option value="">&nbsp;</option>
										<option value="1">新闻类别</option>
										<option value="2">链接</option>
									</select>
								</td>
							
								<!-- 
								<th>
									来源
								</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
								<td>
									<select id="origin" name="type.origin"  style="width: 150px;" onchange="selectOrigin();">
										<option value="">&nbsp;</option>
										<option value="1">总会</option>
										<option value="2">地方</option>
									</select>
								</td>
								
								<th>
									地域
								</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
								<td>
									<input id="cityName" name="type.cityName" style="width: 150px;" disabled="disabled" />
								</td>
								 -->
								<td>
									<a href="javascript:void(0);" class="easyui-linkbutton"
										data-options="iconCls:'icon-search',plain:true"
										onclick="searchNews();">查询</a>
									<a href="javascript:void(0);" class="easyui-linkbutton"
	                                   data-options="iconCls:'ext-icon-huifu',plain:true"
	                                   onclick="resetT();">重置</a>
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
							<authority:authority authorizationCode="新增手机新闻栏目" role="${sessionScope.user.role}">
								<a href="javascript:void(0);" class="easyui-linkbutton"
									data-options="iconCls:'ext-icon-note_add',plain:true"
									onclick="addType();">新增</a>
							</authority:authority>
							</td>
							<td>
							<authority:authority authorizationCode="删除手机新闻栏目" role="${sessionScope.user.role}">
								<a href="javascript:void(0);" class="easyui-linkbutton"
									data-options="iconCls:'ext-icon-note_delete',plain:true"
									onclick="removeType();">删除</a>
							</authority:authority>
							</td>
							
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="newsGrid"></table>
	</div>
</div>
  </body>
</html>