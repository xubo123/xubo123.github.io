<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/authority" prefix="authority"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	request.setCharacterEncoding("UTF-8");
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">

<title></title>
<meta http-equiv=Content-Type content=text/html;charset=UTF-8>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<jsp:include page="../../../inc.jsp"></jsp:include>
<script type="text/javascript">
	var userInfoGrid;
	$(function() {
		userInfoGrid = $('#userInfoGrid').datagrid({
			url : '${pageContext.request.contextPath}/userBaseInfo/userBaseInfoAction!dataGrid.action',
			nowrap : false,
			fit : true,
			border : false,
			striped : true,
			pagination : true,
			sortName : 'user_id',
			sortOrder : 'desc',
			columns : [ [
					{
						field : 'user_id',
						checkbox : true
					},
					{
						width : '100',
						title : '姓名',
						field : 'user_name',
						align : 'center',
						sortable : true
					},
					{
						width : '150',
						title : '学校',
						field : 'schoolName',
						align : 'center',
						sortable : true
					},
					{
						width : '150',
						title : '院系',
						field : 'college',
						align : 'center'
					},
					{
						width : '150',
						title : '专业',
						field : 'major',
						align : 'center'
					},
					{
						width : '80',
						title : '年级',
						field : 'grade',
						align : 'center'
					},
					{
						width : '150',
						title : '班级',
						field : 'className',
						align : 'center'
					},
					{
						width : '80',
						title : '是否注册',
						field : 'appuser_id',
						align : 'center',
						formatter : function(value, row) {
							if (value > 0) {
								return '已注册';
							} else {
								return '未注册';
							}
						}
					},
					{
						title : '操作',
						field : 'action',
						width : '100',
						formatter : function(value, row) {
							var str = '';
							<authority:authority authorizationCode="查看毕业生" role="${sessionScope.user.role}">
							str += '<a href="javascript:void(0)" onclick="viewFun(\''
									+ row.user_id
									+ '\');"><img class="iconImg ext-icon-note"/>查看</a>&nbsp;';
							</authority:authority>
							<authority:authority authorizationCode="编辑毕业生" role="${sessionScope.user.role}">
							str += '<a href="javascript:void(0)" onclick="editFun(\''
									+ row.user_id
									+ '\');"><img class="iconImg ext-icon-note_edit"/>编辑</a>';
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
		
		$('#regflag').combobox('clear');
	});

	function searchUserInfo() {
		if ($('#searchForm').form('validate')) {
			$('#userInfoGrid').datagrid('load',
					serializeObject($('#searchForm')));
		}
	}
	function resetT() {
		$('#searchForm')[0].reset();
		$('#school').combobox('clear');
		$('#college').combobox('clear');
		$('#major').combobox('clear');
		$('#grade').combobox('clear');
		$('#classes').combobox('clear');
		$('#college').combobox('loadData', []);
		$('#major').combobox('loadData', []);
		$('#grade').combobox('loadData', []);
		$('#classes').combobox('loadData', []);
		$('#schoolName').prop('value', '');
		$('#collegeName').prop('value', '');
		$('#majorName').prop('value', '');
		$('#gradeName').prop('value', '');
		$('#className').prop('value', '');

		$('#studentType').combobox('clear');
		$('#regflag').combobox('setValue', '');
	}

	function removeFun() {
		var rows = $("#userInfoGrid").datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			parent.parent.$.messager.confirm('确认','确定删除吗？',
			function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						ids.push(rows[i].user_id);
					}
					$.ajax({
						url : '${pageContext.request.contextPath}/userBaseInfo/userBaseInfoAction!delete.action',
						data : {
							ids : ids.join(',')
						},
						dataType : 'json',
						success : function(data) {
							if (data.success) {
								$("#userInfoGrid").datagrid('reload');
								$("#userInfoGrid").datagrid('unselectAll');
								parent.parent.$.messager.alert('提示',data.msg,'info');
							} else {
								parent.parent.$.messager.alert('错误',data.msg,'error');
							}
						},
						beforeSend : function() {
							parent.parent.$.messager.progress({
								text : '数据提交中....'
							});
						},
						complete : function() {
							parent.parent.$.messager.progress('close');
						}
					});
				}
			});
		} else {
			parent.parent.$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}
	
	function sendEmail() {

		var rows = $("#userInfoGrid").datagrid('getChecked');
		var ids =new Array();
		if (rows.length > 0) {
			parent.parent.$.messager.confirm('确认','确定发送邮件吗？',
			function(r) {
				if (r) {
				    ids = rows[0].user_id;
					for ( var i = 1; i < rows.length; i++) {
						ids=ids+','+rows[i].user_id;
					}
					var dialog = parent.modalDialog({
						width : 1000,
						height : 600,
						title : '发送邮件',
						iconCls : 'ext-icon-note_add',
						url : '${pageContext.request.contextPath}/page/admin/email/add.jsp?ids='+ids,
						buttons : [ {
							text : '保存',
							iconCls : 'ext-icon-save',
							handler : function() {
								dialog.find('iframe').get(0).contentWindow.submitForm(dialog,grid,parent.$);
							}
						} ]
					});
				}
			});
		} else {
			parent.parent.$.messager.alert('提示', '请选择要发送邮件的毕业生！', 'error');
		}
	}
	
	function sendMsg() {

		var rows = $("#userInfoGrid").datagrid('getChecked');
		var ids ='';
		if (rows.length > 0) {
			parent.parent.$.messager.confirm('确认','确定发送短信吗？',
			function(r) {
				if (r) {
				    ids = rows[0].user_id;
					for ( var i = 1; i < rows.length; i++) {
						ids=ids+','+rows[i].user_id;
					}
					var dialog = parent.modalDialog({
						width : 1000,
						height : 600,
						title : '发送短信',
						iconCls : 'ext-icon-note_add',
						url : '${pageContext.request.contextPath}/page/admin/sms/sms.jsp?ids='+ids,
						buttons : [ {
							text : '保存',
							iconCls : 'ext-icon-save',
							handler : function() {
								dialog.find('iframe').get(0).contentWindow.submitForm(dialog,grid,parent.$);
							}
						} ]
					});
				}
			});
		} else {
			parent.parent.$.messager.alert('提示', '请选择要发送短信的毕业生！', 'error');
		}
	}
	function addFun() {
		var dialog = parent.parent
				.WidescreenModalDialog({
					title : '新增毕业生基础信息',
					iconCls : 'ext-icon-note_add',
					url : '${pageContext.request.contextPath}/page/admin/userbaseinfo/addUserInfo.jsp',
					buttons : [ {
						text : '保存',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, userInfoGrid,
											parent.parent.$);
						}
					} ]
				});
	}
	var editFun = function(id) {
		var dialog = parent.parent
				.WidescreenModalDialog({
					title : '编辑毕业生基础信息',
					iconCls : 'ext-icon-note_edit',
					url : '${pageContext.request.contextPath}/page/admin/userbaseinfo/editUserInfo.jsp?id='
							+ id,
					buttons : [ {
						text : '保存',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, userInfoGrid,
											parent.parent.$);
						}
					} ]
				});
	}

	var viewFun = function(id) {
		var dialog = parent.parent
				.WidescreenModalDialog({
					title : '查看毕业生基础信息',
					iconCls : 'ext-icon-note',
					url : '${pageContext.request.contextPath}/page/admin/userbaseinfo/viewUserInfo.jsp?id='
							+ id
				});
	}

	function importFun() {
		var dialog = parent.parent
				.modalDialog({
					title : '导入毕业生基础信息',
					iconCls : 'ext-icon-import_customer',
					url : '${pageContext.request.contextPath}/page/admin/userbaseinfo/importUserInfo.jsp',
					buttons : [ {
						text : '确定',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, parent.parent.$);
						}
					} ]
				});
	}

	function clearFun() {
		parent.parent.$.messager.confirm('确认','确定按搜索条件清空基础库吗？',function(r) {
			if (r) {
				$.ajax({
					url : '${pageContext.request.contextPath}/userBaseInfo/userBaseInfoAction!deleteQuery.action',
					dataType : 'json',
					data : $('#searchForm').serialize(),
					success : function(data) {
						if (data.success) {
							$("#userInfoGrid").datagrid('reload');
							$("#userInfoGrid").datagrid('unselectAll');
							parent.parent.$.messager.alert('提示',data.msg,'info');
						} else {
							parent.parent.$.messager.alert('错误',data.msg,'error');
						}
					},
					beforeSend : function() {
						parent.parent.$.messager.progress({
							text : '数据提交中....'
						});
					},
					complete : function() {
						parent.parent.$.messager.progress('close');
					}
				});
			}
		});
	}

	function exportFun() {
		$.ajax({
			url : '${pageContext.request.contextPath}/userBaseInfo/userBaseInfoAction!exportData.action',
			data : $('#searchForm').serialize(),
			
			dataType : 'json',
			success : function(result) {
				if (result.success) {
					if (result.msg != "") {
						$('#exportResult').html("<a id='mf' href='"+result.msg+"'>导出结果下载</a>")
						parent.parent.$.messager.alert('提示',"导出成功,请在导出结果处下载导出结果", 'info');
					} else {
						parent.parent.$.messager.alert('提示', "无数据导出",'info');
					}
				} else {
					parent.parent.$.messager.alert('提示', result.msg, 'error');
				}
			},
			beforeSend : function() {
				$('#mf').remove();
				parent.parent.$.messager.progress({
					text : '数据导出中....'
				});
			},
			complete : function() {
				parent.parent.$.messager.progress('close');
			}
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
								<th align="right" width="30px;">学校</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><input name="schoolName" id="schoolName" type="hidden">
									<input name="college" id="collegeName" type="hidden"> <input
									name="major" id="majorName" type="hidden"> <input
									name="grade" id="gradeName" type="hidden"> <input
									name="className" id="className" type="hidden"> <input
									id="school" class="easyui-combobox" style="width: 150px;"
									data-options="    
										valueField: 'fullName',  
										textField: 'singleName',		
										editable:false,
										prompt:'--请选择--',
										    icons:[{
							                iconCls:'icon-clear',
							                handler: function(e){
							                $('#school').combobox('clear');
							                $('#college').combobox('clear');
							                $('#major').combobox('clear');
											$('#grade').combobox('clear');
											$('#classes').combobox('clear');
											$('#college').combobox('loadData',[]); 
											$('#major').combobox('loadData',[]);
											$('#grade').combobox('loadData',[]);
											$('#classes').combobox('loadData',[]);
											$('#schoolName').prop('value','');
											$('#collegeName').prop('value','');
											$('#majorName').prop('value','');
											$('#gradeName').prop('value','');
											$('#className').prop('value','');
							                }
							            }],
										url: '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action',  
										onSelect: function(rec){
											var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(encodeURI(rec.fullName)); 
											$('#college').combobox('clear');
							                $('#major').combobox('clear');
											$('#grade').combobox('clear');
											$('#classes').combobox('clear');
											$('#college').combobox('reload',url); 
											$('#major').combobox('loadData',[]);
											$('#grade').combobox('loadData',[]);
											$('#classes').combobox('loadData',[]);
											$('#schoolName').prop('value',rec.singleName);
											$('#collegeName').prop('value','');
											$('#majorName').prop('value','');
											$('#gradeName').prop('value','');
											$('#className').prop('value','');
										}" />
								</td>

								<th align="right" width="30px;">院系</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><input id="college" class="easyui-combobox"
									style="width: 150px;"
									data-options="    
							            valueField: 'fullName',  
										textField: 'singleName',	
										editable:false,
										prompt:'--请选择--',
					                    icons:[{
							                iconCls:'icon-clear',
							                handler: function(e){
							                $('#college').combobox('clear');
							                $('#major').combobox('clear');
											$('#grade').combobox('clear');
											$('#classes').combobox('clear');
											$('#major').combobox('loadData',[]);
											$('#grade').combobox('loadData',[]);
											$('#classes').combobox('loadData',[]);
											$('#collegeName').prop('value','');
											$('#majorName').prop('value','');
											$('#gradeName').prop('value','');
											$('#className').prop('value','');
							                }
							            }],
										onSelect: function(rec){
											var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(encodeURI(rec.fullName)); 
							                
							                $('#major').combobox('clear');
											$('#grade').combobox('clear');
											$('#classes').combobox('clear');
											$('#major').combobox('reload',url);
											$('#grade').combobox('loadData',[]);
											$('#classes').combobox('loadData',[]);
											$('#collegeName').prop('value',rec.singleName);
											$('#majorName').prop('value','');
											$('#gradeName').prop('value','');
											$('#className').prop('value','');
										}" />
								</td>

								<th align="right" width="30px;">专业</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><input id="major" class="easyui-combobox"
									style="width: 150px;"
									data-options="    
							            valueField: 'fullName',  
										textField: 'singleName',	
										editable:false,
										prompt:'--请选择--',
					                    icons:[{
							                iconCls:'icon-clear',
							                handler: function(e){
							                $('#major').combobox('clear');
											$('#grade').combobox('clear');
											$('#classes').combobox('clear');
											$('#grade').combobox('loadData',[]);
											$('#classes').combobox('loadData',[]);
											$('#majorName').prop('value','');
											$('#gradeName').prop('value','');
											$('#className').prop('value','');
							                }
							            }],
										onSelect: function(rec){
											var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(encodeURI(rec.fullName)); 
											$('#grade').combobox('clear');
											$('#classes').combobox('clear');
											$('#grade').combobox('reload',url);
											$('#classes').combobox('loadData',[]);
											$('#majorName').prop('value',rec.singleName);
											$('#gradeName').prop('value','');
											$('#className').prop('value','');
										}" />
								</td>

								<th align="right" width="30px;">年级</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><input id="grade" class="easyui-combobox"
									style="width: 150px;"
									data-options="    
							            valueField: 'fullName',  
										textField: 'singleName',	
										editable:false,
										prompt:'--请选择--',
					                    icons:[{
							                iconCls:'icon-clear',
							                handler: function(e){
											$('#grade').combobox('clear');
											$('#classes').combobox('clear');
											$('#classes').combobox('loadData',[]);
											$('#gradeName').prop('value','');
											$('#className').prop('value','');
							                }
							            }],
										onSelect: function(rec){
											var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(encodeURI(rec.fullName)); 
											$('#classes').combobox('clear');
											$('#classes').combobox('reload',url);
											$('#gradeName').prop('value',rec.singleName);
											$('#className').prop('value','');
										}" />
								</td>

								<th align="right" width="30px;">班级</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><input id="classes" class="easyui-combobox"
									style="width: 150px;"
									data-options="    
							            valueField: 'fullName',  
										textField: 'singleName',	
										editable:false,
										prompt:'--请选择--',
					                    icons:[{
							                iconCls:'icon-clear',
							                handler: function(e){
											$('#classes').combobox('clear');
											$('#className').prop('value','');
							                }
							            }],
										onSelect: function(rec){
											$('#className').prop('value',rec.singleName);
										}" />
								</td>
							</tr>
							<tr>
								<th align="right">姓名</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><input name="user_name" style="width: 150px;" /></td>
								<th align="right">学号</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><input name="studentNumber" style="width: 150px;" /></td>
								<th align="right">学历</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><input id="studentType" class="easyui-combobox"
									style="width: 150px;" name="studentType"
									data-options="  
										valueField: 'dictName',  
										textField: 'dictName',  
										prompt:'--请选择--',
						                    icons:[{
								                iconCls:'icon-clear',
								                handler: function(e){
												$('#studentType').combobox('clear');
								                }
								            }],  
										editable:false,
										url: '${pageContext.request.contextPath}/dicttype/dictTypeAction!doNotNeedSecurity_getDict.action?dictTypeName='+encodeURI(encodeURI('学历')) 
									" />
								</td>


								<th align="right">是否注册</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><select class="easyui-combobox"  name="regflag" id="regflag" style="width: 150px;"
									data-options="  
										prompt:'--请选择--',
					                    icons:[{
							                iconCls:'icon-clear',
							                handler: function(e){
											$('#regflag').combobox('clear');
							                }
							            }],  
										editable:false
									">
										<option value="1">是</option>
										<option value="0">否</option>
								</select></td>
								<td colspan="3"><a href="javascript:void(0);"
									class="easyui-linkbutton"
									data-options="iconCls:'icon-search',plain:true"
									onclick="searchUserInfo();">查询</a> <a
									href="javascript:void(0);" class="easyui-linkbutton"
									data-options="iconCls:'ext-icon-huifu',plain:true"
									onclick="resetT();">重置</a></td>
							</tr>

						</table>
					</form></td>
			</tr>
			<tr>
				<td>
					<table>
						<tr>
							<td id="addTd"><authority:authority
									authorizationCode="新增毕业生" role="${sessionScope.user.role}">
									<a href="javascript:void(0);" class="easyui-linkbutton"
										data-options="iconCls:'ext-icon-note_add',plain:true"
										onclick="addFun();">新增</a>
								</authority:authority></td>
							<td><authority:authority role="${sessionScope.user.role}"
									authorizationCode="毕业生导入">
									<a href="javascript:void(0);" class="easyui-linkbutton"
										data-options="iconCls:'ext-icon-import_customer',plain:true"
										onclick="importFun();"
										style="margin-left: 5px; margin-top: 1px;">导入</a>
								</authority:authority></td>
							<td><authority:authority authorizationCode="删除毕业生"
									role="${sessionScope.user.role}">
									<a href="javascript:void(0);" class="easyui-linkbutton"
										data-options="iconCls:'ext-icon-note_delete',plain:true"
										onclick="removeFun();">删除</a>
								</authority:authority></td>
							<td><authority:authority authorizationCode="按搜索条件清空毕业生"
									role="${sessionScope.user.role}">
									<a href="javascript:void(0);" class="easyui-linkbutton"
										data-options="iconCls:'ext-icon-note_delete',plain:true"
										onclick="clearFun();">按搜索条件清空</a>
								</authority:authority></td>
							<td><authority:authority authorizationCode="发送毕业生邮件"
									role="${sessionScope.user.role}">
									<a href="javascript:void(0);" class="easyui-linkbutton"
										data-options="iconCls:'ext-icon-export_customer',plain:true"
										onclick="sendEmail();">给选中毕业生发送邮件</a>
								</authority:authority></td>
								<td><authority:authority authorizationCode="发送毕业生短信"
									role="${sessionScope.user.role}">
									<a href="javascript:void(0);" class="easyui-linkbutton"
										data-options="iconCls:'ext-icon-export_customer',plain:true"
										onclick="sendMsg();">给选中毕业生发送短信</a>
								</authority:authority></td>
							<td><authority:authority authorizationCode="按搜索条件导出毕业生"
									role="${sessionScope.user.role}">
									<a href="javascript:void(0);" class="easyui-linkbutton"
										data-options="iconCls:'ext-icon-export_customer',plain:true"
										onclick="exportFun();">按搜索条件导出</a>
								</authority:authority></td>
							<td><span id="exportResult"></span></td>
						</tr>
					</table></td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="userInfoGrid"></table>
	</div>
</body>
</html>
