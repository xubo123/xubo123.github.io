<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
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
	var hasCheck=0;
	$(function() {
		if ($('#userId').val() > 0) {
			$.ajax({
				url : '${pageContext.request.contextPath}/user/userAction!getUserByUserId.action',
				data : $('form').serialize(),
				dataType : 'json',
				success : function(result) {
					if (result.userId != undefined) {
						$('form').form('load', {
							'user.userName' : result.userName,
							'user.userAccount' : result.userAccount,
							'user.telephone':result.telephone,
							'user.email':result.email,
							'user.flag':result.flag,
							'user.roleId':result.roleId
						});
						if(result.flag==0){
							$('form').form('load', {
								'role':result.roleId
							});
							$('#roletr').show();
							$('#depttr').show();
							$('#roletr1').hide();
							$('#depttr1').hide();
							
							if(result.depts.length > 0) {
								$('#deptId').combobox('setValue',result.depts[0].department_id);
							}
						}else{
							$('form').form('load', {
								'xrole':result.roleId
							});
							$('#roletr').hide();
							$('#depttr').hide();
							$('#roletr1').show();
							$('#depttr1').show();
							
							if(result.depts.length > 0) {
								$('#deptId1').combobox('setValue',result.depts[0].department_id);
							}
						}

					}
				},
				complete:function(){
					parent.$.messager.progress('close');
				}
			});
			$("#flag").combobox("disable"); 
		}
	});
	
	var submitForm = function($dialog, $grid, $pjq) {
		if($('#roleId').val()==''){
			$pjq.messager.alert('提示', '请设置用户角色', 'info');
			return false;
		}
		if($('#flag').combobox('getValue')==1){
			if($('#deptId1').combobox('getValue')==''){
				$pjq.messager.alert('提示', '请设置管理校友会', 'info');
				return false;
			}
			$('#ids').prop('value',$('#deptId1').combobox('getValue'));
		}else{
			if($('#deptId').combobox('getValue')==''){
				$pjq.messager.alert('提示', '请设置管理院系', 'info');
				return false;
			}
			$('#ids').prop('value',$('#deptId').combobox('getValue'));
		}
		if ($('form').form('validate')) {
			var url;
			if ($('#userId').val() > 0) {
				url = '${pageContext.request.contextPath}/user/userAction!update.action';
			} else {
				url = '${pageContext.request.contextPath}/user/userAction!save.action';
			}
			$.ajax({
				url : url,
				data :$('form').serialize(),
				dataType : 'json',
				success : function(result) {
					if (result.success) {
						$dialog.dialog('destroy');
						$grid.datagrid('reload');
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
     <form method="post" id="userForm">
     	<fieldset>
				<legend>
					用户基本信息
				</legend>
				<table class="ta001">
					<input name="ids" id="ids" type="hidden"/>
					<input name="user.userId" type="hidden" id="userId" value="${param.id}">
					<c:if test="${param.id==0}">
						<tr>
							<th>
								用户帐号
							</th>
							<td>
								<input name="user.userAccount" class="easyui-validatebox"
									data-options="required:true" />
							</td>
						</tr>
						<tr>
							<th>
								用户密码
							</th>
							<td>
								<input name="user.userPassword" class="easyui-validatebox" data-options="required:true,validType:'passWord[6]'" type="password">
							</td>
						</tr>
						<tr>
							<th>
								系统
							</th>
							<td>
								<select class="easyui-combobox" data-options="editable:false,onSelect:function(record){
									if($('#flag').combobox('getValue')==1){
										$('#roletr').hide();
										$('#depttr').hide();
										$('#roletr1').show();
										$('#depttr1').show();
										$('#roleId').prop('value','');
										$('#role').combobox('clear');
										$('#deptId').combobox('clear');
									}else{
										$('#roletr').show();
										$('#depttr').show();
										$('#roletr1').hide();
										$('#depttr1').hide();
										$('#roleId').prop('value','');
										$('#xrole').combobox('clear');
										$('#deptId1').combobox('clear');
									}
								}" id="flag" name="user.flag" style="width: 150px;">
									<option value="0">WEB后台系统</option>
									<option value="1">校友会系统</option>
								</select>
								<input name="user.roleId" id="roleId" type="hidden">
							</td>
						</tr>
					</c:if>
					<c:if test="${param.id!=0}">
						<tr>
							<th>
								系统
							</th>
							<td>
								<select class="easyui-combobox" data-options="editable:false,onSelect:function(record){
									if($('#flag').combobox('getValue')==1){
										$('#roletr').hide();
										$('#depttr').hide();
										$('#roletr1').show();
										$('#depttr1').show();
										$('#roleId').prop('value','');
										$('#role').combobox('clear');
										$('#deptId').combobox('clear');
									}else{
										$('#roletr').show();
										$('#depttr').show();
										$('#roletr1').hide();
										$('#depttr1').hide();
										$('#roleId').prop('value','');
										$('#xrole').combobox('clear');
										$('#deptId1').combobox('clear');
									}
								}" id="flag" name="user.flag" style="width: 150px;">
									<option value="0">WEB后台系统</option>
									<option value="1">校友会系统</option>
								</select>
								<input name="user.roleId" id="roleId" type="hidden">
							</td>
						</tr>
					</c:if>
					<tr>
						<th>
							用户姓名
						</th>
						<td>
							<input name="user.userName" class="easyui-validatebox"
								data-options="required:true,validType:'customRequired'" />
						</td>
					</tr>
					<tr>
						<th>
							电话号码
						</th>
						<td>
							<input name="user.telephone" class="easyui-validatebox"
								data-options="validType:'telePhone'" />
						</td>
					</tr>
					<tr>
						<th>
							电子邮箱
						</th>
						<td>
							<input name="user.email" class="easyui-validatebox"
								data-options="validType:'email'" />
						</td>
					</tr>
					<tr id="roletr">
						<th>
							角色
						</th>
						<td >
							<input id="role" class="easyui-combobox" style="width: 150px;" name="role"
											data-options="  
												valueField: 'roleId',  
												textField: 'roleName',  
												editable:false,
												prompt:'--请选择--',
							                    icons:[{
									                iconCls:'icon-clear',
									                handler: function(e){
									                $('#role').combobox('clear');
									                $('#roleId').prop('value','');
									                }
									            }],
												url: '${pageContext.request.contextPath}/role/roleAction!doNotNeedSecurity_getNoAdmin.action',  
												onSelect: function(rec){
												$('#roleId').prop('value',rec.roleId);
										}" />
						</td>
					</tr>
					<tr id="depttr">
						<th>
							管理院系
						</th>
						<td >
							<input class="easyui-combobox" id="deptId" style="width: 150px;"
										data-options="
					                    url:'${pageContext.request.contextPath}/department/departmentAction!doNotNeedSecurity_getCollege2ComboBox.action',
					                    method:'post',
					                    valueField:'department_id',
					                    textField:'departmentName',
					                    editable:false,
					                    prompt:'--请选择--',
					                    icons:[{
							                iconCls:'icon-clear',
							                handler: function(e){
							                	$('#deptId').combobox('clear');
							                }
							            }]
				                    	">
						</td>
					</tr>
					<tr id="roletr1" style="display: none;">
						<th>
							角色
						</th>
						<td >
							<input id="xrole" class="easyui-combobox" style="width: 150px;" name="xrole"
											data-options="  
												valueField: 'roleId',  
												textField: 'roleName',  
												editable:false,
												prompt:'--请选择--',
							                    icons:[{
									                iconCls:'icon-clear',
									                handler: function(e){
									                $('#xrole').combobox('clear');
									                $('#roleId').prop('value','');
									                }
									            }],
												url: '${pageContext.request.contextPath}/role/roleAction!doNotNeedSecurity_getxNoAdmin.action',  
												onSelect: function(rec){
												$('#roleId').prop('value',rec.roleId);
										}" />
						</td>
					</tr>
					<tr id="depttr1" style="display: none;">
						<th>
							管理校友会
						</th>
						<td >
							<input class="easyui-combobox" id="deptId1" style="width: 150px;"
										data-options="
					                    url:'${pageContext.request.contextPath}/department/departmentAction!doNotNeedSecurity_getAlumni2ComboBox.action',
					                    method:'post',
					                    valueField:'department_id',
					                    textField:'departmentName',
					                    editable:false,
					                    prompt:'--请选择--',
					                    icons:[{
							                iconCls:'icon-clear',
							                handler: function(e){
							                	$('#deptId1').combobox('clear');
							                }
							            }]
				                    	">
						</td>
					</tr>
				</table>
			</fieldset>
     </form>
  </body>
</html>
