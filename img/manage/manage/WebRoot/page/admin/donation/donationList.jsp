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
	var donationGrid;
	$(function() {
		donationGrid = $('#donationGrid')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/donation/donationAction!dataGrid.action',
							fit : true,
							border : false,
							fitColumns : true,
							striped : true,
							rownumbers : true,
							pagination : true,
							idField : 'donationId',
							columns : [ [
									{
										field : 'userId',
										checkbox : true
									},
									{
										width : '100',
										title : '姓名',
										field : 'x_name',
										align : 'center'
									},
									{
										width : '80',
										title : '是否是校友',
										field : 'flag',
										align : 'center',
										formatter : function(value, row) {
											if (value == 1 && row.userId != '') {
												return "<font style='color: green;'>校友</font>";
											} else if (value == 1
													&& row.userId == '') {
												return "<font style='color: red;'>待核校友</font>";
											} else {
												return "<font style='color: gray;'>非校友</font>";
											}
										}
									},
									{
										width : '150',
										title : '学校',
										field : 'x_school',
										align : 'center'
									},
									{
										width : '150',
										title : '院系',
										field : 'x_depart',
										align : 'center'
									},
									{
										width : '100',
										title : '年级',
										field : 'x_grade',
										align : 'center'
									},
									{
										width : '150',
										title : '班级',
										field : 'x_clazz',
										align : 'center'
									},
									{
										width : '150',
										title : '专业',
										field : 'x_major',
										align : 'center'
									},
									{
										width : '250',
										title : '订单编号',
										field : 'orderNo',
										align : 'center'
									},
									{
										width : '150',
										title : '捐赠项目',
										field : 'projectName',
										align : 'center',
										formatter : function(value, row) {
											if (row.project != undefined) {
												return row.project.projectName;
											} else {
												return "";
											}
										}
									},
									{
										width : '80',
										title : '捐赠金额',
										field : 'money',
										align : 'center'
									},
									{
										width : '150',
										title : '捐赠时间',
										field : 'donationTime',
										align : 'center'
									},
									{
										width : '80',
										title : '支付金额',
										field : 'payMoney',
										align : 'center'
									},
									{
										width : '150',
										title : '支付时间',
										field : 'payTime',
										align : 'center'
									},
									{
										width : '80',
										title : '支付状态',
										field : 'payStatus',
										align : 'center',
										formatter : function(value, row) {
											if (value == 1) {
												return "已支付";
											} else {
												return "未支付";
											}
										}
									},
									{
										width : '80',
										title : '确认状态',
										field : 'confirmStatus',
										align : 'center',
										formatter : function(value, row) {
											if (value == 1) {
												return "<font style='color: green;'>已确认</font>";
											} else {
												return "未确认";
											}
										}
									},
									{
										title : '操作',
										field : 'action',
										width : '120',
										formatter : function(value, row) {
											var str = '';
											<authority:authority authorizationCode="查看捐赠信息" role="${sessionScope.user.role}">
											str += '<a href="javascript:void(0)" onclick="showFun('
													+ row.donationId
													+ ');"><img class="iconImg ext-icon-note"/>查看</a>&nbsp;';
											</authority:authority>
											<authority:authority authorizationCode="编辑捐赠信息" role="${sessionScope.user.role}">
											str += '<a href="javascript:void(0)" onclick="editFun('
													+ row.donationId
													+ ');"><img class="iconImg ext-icon-note_edit"/>编辑</a>&nbsp;';
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
		var dialog = parent
				.WidescreenModalDialog({
					title : '新增捐赠信息',
					iconCls : 'ext-icon-note_add',
					url : '${pageContext.request.contextPath}/page/admin/donation/addDonation.jsp',
					buttons : [ {
						text : '保存',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, donationGrid, parent.$);
						}
					} ]
				});
	};

	function showFun(id) {
		var dialog = parent
				.WidescreenModalDialog({
					title : '查看捐赠信息',
					iconCls : 'ext-icon-note',
					url : '${pageContext.request.contextPath}/page/admin/donation/viewDonation.jsp?id='
							+ id
				});
	}

	function editFun(id) {
		var dialog = parent
				.WidescreenModalDialog({
					title : '编辑捐赠信息',
					iconCls : 'ext-icon-note_edit',
					url : '${pageContext.request.contextPath}/page/admin/donation/editDonation.jsp?id='
							+ id,
					buttons : [ {
						text : '保存',
						iconCls : 'ext-icon-save',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow
									.submitForm(dialog, donationGrid, parent.$);
						}
					} ]
				});
	}

	function removeFun() {
		var rows = $('#donationGrid').datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			parent.$.messager
					.confirm(
							'确认',
							'确定删除吗？',
							function(r) {
								if (r) {
									for ( var i = 0; i < rows.length; i++) {
										ids.push(rows[i].donationId);
									}
									$
											.ajax({
												url : '${pageContext.request.contextPath}/donation/donationAction!delete.action',
												data : {
													ids : ids.join(',')
												},
												dataType : 'json',
												success : function(data) {
													if (data.success) {
														$("#donationGrid")
																.datagrid(
																		'reload');
														$("#donationGrid")
																.datagrid(
																		'unselectAll');
														parent.$.messager
																.alert(
																		'提示',
																		data.msg,
																		'info');
													} else {
														parent.$.messager
																.alert(
																		'错误',
																		data.msg,
																		'error');
													}
												},
												beforeSend : function() {
													parent.$.messager
															.progress({
																text : '数据提交中....'
															});
												},
												complete : function() {
													parent.$.messager
															.progress('close');
												}
											});
								}
							});
		} else {
			parent.$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}

	/**--查询--**/
	function searchFun() {
		$('#donationGrid').datagrid('load', serializeObject($('#searchForm')));
	}

	/**--重置--**/
	function resetT() {
		$('#school').combobox('clear');
		$('#depart').combobox('clear');
		$('#grade').combobox('clear');
		$('#classes').combobox('clear');
		$('#major').combobox('clear');
		$('#studentType').combobox('clear');
		$('#classes').combobox('loadData', []);
		$('#grade').combobox('loadData', []);
		$('#major').combobox('loadData', []);
		$('#depart').combobox('loadData', []);
		$('#searchForm')[0].reset();
		$('#schoolId').prop('value', '');
		$('#departId').prop('value', '');
		$('#gradeId').prop('value', '');
		$('#classId').prop('value', '');
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
								<th align="right" width="30px;">金额范围</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
								<td colspan="4"><input name="startMoney"
									style="width: 150px;" class="easyui-validatebox"
									onkeyup="this.value=this.value.replace(/\D/g,'')"
									onafterpaste="this.value=this.value.replace(/\D/g,'')"></input>
									- <input name="endMoney" style="width: 150px;"
									class="easyui-validatebox"
									onkeyup="this.value=this.value.replace(/\D/g,'')"
									onafterpaste="this.value=this.value.replace(/\D/g,'')"></input>
								</td>
								<th align="right" width="30px;">时间范围</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
								<td colspan="5"><input name="startTime"
									style="width: 150px;" class="easyui-validatebox"
									readonly="readonly" onClick="WdatePicker()" /> - <input
									name="endTime" style="width: 150px;" class="easyui-validatebox"
									readonly="readonly" onClick="WdatePicker()" />
								</td>
							</tr>
							<tr>
								<th align="right" width="30px;">捐赠项目</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
								<td><input id="projectId" name="donation.projectId"
									class="easyui-combobox" style="width: 150px;"
									data-options="editable:false,
									        valueField: 'projectId',
									        textField: 'projectName',
									        prompt:'--请选择--',
							                    icons:[{
									                iconCls:'icon-clear',
									                handler: function(e){
													$('#projectId').combobox('clear');
									                }
									            }],  
									        url: '${pageContext.request.contextPath}/project/projectAction!doNotNeedSecurity_getAll.action'" />
								</td>
								<th align="right" width="30px;">确认状态</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
								<td><select class="easyui-combobox"
									data-options="editable:false" name="donation.confirmStatus"
									style="width: 150px;">
										<option value="-1">全部</option>
										<option value="0">未确认</option>
										<option value="1">已确认</option>
								</select>
								</td>
								<th align="right" width="30px;">支付状态</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
								<td><select class="easyui-combobox"
									data-options="editable:false" name="donation.payStatus"
									style="width: 150px;">
										<option value="-1">全部</option>
										<option value="0">未支付</option>
										<option value="1">已支付</option>
								</select>
								</td>
								<th align="right" width="30px;">姓名</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
								<td><input name="donation.userInfo.userName"
									style="width: 150px;" class="easyui-validatebox"></input>
								</td>
								<td colspan="3"><a href="javascript:void(0);"
									class="easyui-linkbutton"
									data-options="iconCls:'icon-search',plain:true"
									onclick="searchFun();">查询</a> <a href="javascript:void(0);"
									class="easyui-linkbutton"
									data-options="iconCls:'icon-redo',plain:true"
									onclick="resetT()">重置</a>
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
							<td><authority:authority authorizationCode="删除捐赠信息"
									role="${sessionScope.user.role}">
									<a href="javascript:void(0);" class="easyui-linkbutton"
										data-options="iconCls:'ext-icon-note_delete',plain:true"
										onclick="removeFun();">删除</a>
								</authority:authority>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="donationGrid"></table>
	</div>
</body>
</html>
