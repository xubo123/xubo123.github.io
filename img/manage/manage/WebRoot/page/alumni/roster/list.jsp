<%@ page language="java" pageEncoding="UTF-8"%>
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
	var userInfoGrid;
	$(function()
	{
				userInfoGrid = $('#userInfoGrid').datagrid({
					url : '${pageContext.request.contextPath}/userBaseInfo/userBaseInfoAction!dataGridForAlumni.action',
					fit : true,
					method : 'post',
					border : false,
					striped : true,
					pagination : true,
					singleSelect : true,
					sortName : 'user_name',
					sortOrder : 'asc',
					columns : [ [
					{
						field : 'user_id',
						checkbox : true
					},
					{
						width : '80',
						title : '姓名',
						field : 'user_name',
						align : 'center'
					}, {
						width : '50',
						title : '性别',
						field : 'sex',
						align : 'center'
					}, {
						width : '100',
						title : '电话号码',
						field : 'tel_id',
						align : 'center'
					}, {
						width : '100',
						title : '邮箱',
						field : 'email',
						align : 'center'
					}, {
						width : '150',
						title : '工作单位',
						field : 'workUnit',
						align : 'center'
					},{
						width : '100',
						title : '职务',
						field : 'position',
						align : 'center'
					},
					{
						width : '150',
						title : '所在城市',
						field : 'residentialArea',
						align : 'center'
					},
					{
						width : '350',
						title : '学习经历',
						field : 'allClassName',
						align : 'center',
						formatter : function(value, row)
						{
							var text='';
							if(value!=undefined){
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
						width : '80',
						formatter : function(value, row)
						{
							var str = '';
							<authority:authority authorizationCode="查看校友会学生" role="${sessionScope.user.role}">
							str += '<a href="javascript:void(0)" onclick="viewFun(\'' + row.allUser_id + '\');"><img class="iconImg ext-icon-note"/>查看</a>&nbsp;';
							</authority:authority>
							return str;
						}
					} ] ],
					toolbar : '#toolbar',
					onBeforeLoad : function(param)
					{
						parent.parent.$.messager.progress({
							text : '数据加载中....'
						});
					},
					onLoadSuccess : function(data)
					{
						$('.iconImg').attr('src', pixel_0);
						parent.parent.$.messager.progress('close');
					}
				});

	});

	function searchUserInfo()
	{
		$('#userInfoGrid').datagrid('load', serializeObject($('#searchForm')));
	}


	var viewFun = function(id)
	{
		var dialog = parent.parent.WidescreenModalDialog({
			title : '查看学生',
			iconCls : 'ext-icon-note',
			url : '${pageContext.request.contextPath}/page/alumni/roster/view.jsp?id=' + id
		});
	}
	
	  function resetT(){
			$('#school').combobox('clear');
			$('#depart').combobox('clear');
			$('#grade').combobox('clear');
			$('#classes').combobox('clear');
			$('#major').combobox('clear');
			$('#studentType').combobox('clear');
			$('#classes').combobox('loadData',[]);
			$('#grade').combobox('loadData',[]);
			$('#major').combobox('loadData',[]);
			$('#depart').combobox('loadData',[]);
			$('#searchForm')[0].reset();
			$('#schoolId').prop('value','');
			$('#departId').prop('value','');
			$('#gradeId').prop('value','');
			$('#classId').prop('value','');
			$('#alumni').combobox('clear');
			$('#sex').combobox('clear');
		}
	  
	  /**--短信发送--**/
		function messageSend(){
			//查询的条件
			var row = $("#userInfoGrid").datagrid('getChecked');
			if (row.length>0){
			var id = row[0].user_id;
			var dialog = parent.parent.WidescreenModalDialog({
				title : '短信发送',
				iconCls : 'ext-icon-export_customer',
				url : '${pageContext.request.contextPath}/page/alumni/roster/send.jsp? messageid ='+ id,
				buttons : [ {
					text : '发送',
					iconCls : 'ext-icon-save',
					handler : function()
					{
						dialog.find('iframe').get(0).contentWindow.doSend();
					}
				} ]
			});
			}else{
			parent.parent.$.messager.alert('提示', '请选择要发送短信的用户！', 'error');
			}
		}
		
		/**--邮件发送--**/
		function emailSend(){
			//查询的条件
			var userName = $("#userName").val();
			var schoolId = $("#schoolId").val();
			var departId = $("#departId").val();
			var classId = $("#classId").val();
			var majorId = $("#major").combobox('getValue');
			
			var params = "userName="+userName+"&schoolId="+schoolId+"&departId="+departId+"&classId="+classId+"&majorId="+majorId;
			var url = "<%=path %>/page/admin/email/send.jsp?"+params;
			var dialog = parent.parent.WidescreenModalDialog({
			title : '邮件发送',
			iconCls : 'ext-icon-export_customer',
			url : url,
			buttons : [ {
				text : '发送',
				iconCls : 'ext-icon-save',
				handler : function()
				{
					dialog.find('iframe').get(0).contentWindow.submitForm();
				}
			} ]
			});
		}
	  

</script>
	</head>

	<body class="easyui-layout" data-options="fit:true,border:false">
		<div id="toolbar" style="display: none;">
		<form id="searchForm">
			<table>
				<tr>   
	                            <th align="right" width="30px;">
	                            	学校
	                            </th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
								<td>
									<input name="schoolName" id="schoolName" type="hidden">
									<input name="college" id="collegeName" type="hidden">
									<input name="major" id="majorName" type="hidden">
									<input name="grade" id="gradeName" type="hidden"> 
									<input name="className" id="className" type="hidden">
									
									<input id="school" class="easyui-combobox" style="width: 150px;"
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
								
								<th align="right" width="30px;">
									院系
								</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
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
								
								<th align="right" width="30px;">
									专业
								</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
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
								
								<th align="right" width="30px;">
									年级
								</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
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
								
								<th align="right" width="30px;">
									班级
								</th>
								<td>
									<div class="datagrid-btn-separator"></div>
								</td>
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
									<div class="datagrid-btn-separator"></div>
								</td>
								<td>
									<input id="studentType" class="easyui-combobox"
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
									<th align="right">
										姓名
									</th>
									<td>
										<div class="datagrid-btn-separator"></div>
									</td>
									<td>
										<input name="deptId" id="deptId" type="hidden" />
										<input id="userName" name="userInfo.userName" style="width: 150px;" />
									</td>
								</tr>
								<tr>
									<th align="right">
										性别
									</th>
									<td>
										<div class="datagrid-btn-separator"></div>
									</td>
									<td>
										<input class="easyui-combobox" name="sex" style="width: 150px;" id="sex" data-options="
										editable:false,
										prompt:'--请选择--',
							                    icons:[{
									                iconCls:'icon-clear',
									                handler: function(e){
													$('#sex').combobox('clear');
									                }
									            }],
										valueField: 'label',
										textField: 'value',
										data: [{
											label: '男',
											value: '男'
										},{
											label: '女',
											value: '女'
										}]" />
									</td>

									<td colspan="3">
										<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="searchUserInfo();">查询</a>&nbsp;
										<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="resetT()">重置</a>
									</td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
				<tr>
					<%-- <td>
						<table>
							<tr>
								<td>
									<authority:authority authorizationCode="校友会短信发送" role="${sessionScope.user.role}">
										<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-export_customer',plain:true" onclick="messageSend();">按搜索条件发送短信</a>
									</authority:authority>
								</td>
								<td>
									<authority:authority authorizationCode="校友会邮件发送" role="${sessionScope.user.role}">
										<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-export_customer',plain:true" onclick="emailSend();">按搜索条件发送邮件</a>
									</authority:authority>
								</td>
								<td>
									<span id="exportResult"></span>
								</td>
							</tr>
						</table>
					</td> --%>
				</tr>
			</table>
			</form>
		</div>
		<div data-options="region:'center',fit:true,border:false">
			<table id="userInfoGrid"></table>
		</div>
	</body>
</html>
