<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
					url : '${pageContext.request.contextPath}/donation/donationAction!save.action',
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
		}
		function searchFun(){
				$('#cc').combogrid('grid').datagrid('load',serializeObject($('#searchForm')));
		}
</script>
	</head>

	<body>
		<form method="post">
			<fieldset>
				<legend>
					捐赠基本信息
				</legend>
				<table class="ta001">
					<tr>
						<th>
							捐赠项目
						</th>
						<td>
							<input id="projectId" name="donation.projectId" class="easyui-combobox" style="width: 150px;"
											data-options="editable:false,
									        required:true,
									        valueField: 'projectId',
									        textField: 'projectName',
									        url: '${pageContext.request.contextPath}/project/projectAction!doNotNeedSecurity_getAll.action'"/>
						</td>
						<th>
							捐赠金额/元
						</th>
						<td>
							<input name="donation.money" style="width: 150px;" class="easyui-validatebox" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></input>
						</td>
					</tr>
					<tr>
						<th>
							支付金额/元
						</th>
						<td>
							<input name="donation.payMoney" style="width: 150px;" class="easyui-validatebox" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></input>
						</td>
						<th>
							捐赠留言
						</th>
						<td>
							<input name="donation.message" class="easyui-validatebox"></input>
						</td>
					</tr>
					<tr>
						<th>
							备注
						</th>
						<td colspan="3">
							<textarea rows="5" cols="60" name="donation.remark"></textarea>
						</td>
					</tr>
				</table>
			</fieldset>
			<br/>
			<fieldset>
				<legend>
					捐赠人信息
				</legend>
				<table class="ta001">
					<tr>
						<th>
							姓名
						</th>
						<td>
							<select class="easyui-combogrid" id="cc" style="width:150px;"
						        data-options="
						        	required:true,
						        	editable:false,
						            panelWidth:600,
						            idField:'userName',
						            textField:'userName',
						            pagination : true,
						            url:'${pageContext.request.contextPath}/userInfo/userInfoAction!doNotNeedSecurity_dataGridFor.action',
						            columns:[[
						                {field:'userName',title:'姓名',width:100,align:'center'},
						                {field:'fullName',title:'所属机构',width:500,align:'center'}
						            ]],
						            toolbar: $('#toolbar'),
						            onSelect:function(rowIndex, rowData){
						            	$('#departName').prop('value',rowData.departName);
						            	$('#gradeName').prop('value',rowData.gradeName);
						            	$('#className').prop('value',rowData.className);
						            	$('#sex').prop('value',rowData.sex);
						            	$('#telId').prop('value',rowData.telId);
						            	$('#email').prop('value',rowData.email);
						            	$('#mailingAddress').prop('value',rowData.mailingAddress);
						            	$('#userId').prop('value',rowData.userId);
						            }
						        "></select>
						</td>
						<th>
							院系
						</th>
						<td>
							<input id="userId" name="donation.userId" type="hidden"/>
							<input id="departName" disabled="disabled" type="text"></input>
						</td>
					</tr>
					<tr>
						<th>
							年级
						</th>
						<td>
							<input id="gradeName" disabled="disabled" type="text"></input>
						</td>
						<th>
							班级
						</th>
						<td>
							<input id="className" disabled="disabled" type="text"></input>
						</td>
					</tr>
					<tr>
						<th>
							性别
						</th>
						<td>
							<input id="sex" disabled="disabled" type="text"></input>
						</td>
						<th>
							联系电话
						</th>
						<td>
							<input id="telId" disabled="disabled" type="text"></input>
						</td>
					</tr>
					<tr>
						<th>
							邮箱
						</th>
						<td>
							<input id="email" disabled="disabled" type="text"></input>
						</td>
						<th>
							联系地址
						</th>
						<td>
							<input id="mailingAddress" disabled="disabled" type="text"></input>
						</td>
					</tr>
				</table>
			</fieldset>
			<br/>
			<fieldset>
				<legend>
					状态
				</legend>
				<table class="ta001">
					<tr>
						<th>
							确认状态
						</th>
						<td>
							<select class="easyui-combobox" data-options="editable:false" name="donation.confirmStatus" style="width: 150px;">
									<option value="0">未确认</option>
									<option value="1">已确认</option>
							</select>
						</td>
						<th>
							支付状态
						</th>
						<td>
							<select class="easyui-combobox" data-options="editable:false" name="donation.payStatus" style="width: 150px;">
									<option value="0">未支付</option>
									<option value="1">已支付</option>
							</select>
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
		<div id="toolbar" style="display: none;">
			<table>
				<tr>
					<td>
						<form id="searchForm">
							<table>
								<tr>
									<th>
										姓名：
									</th>
									<td>
										<input name="userInfo.userName" style="width: 150px;" />
									</td>
									<th>
										电话号码：
									</th>
									<td>
										<input name="userInfo.telId" style="width: 150px;" />
									</td>
									<td>
										<a href="javascript:void(0);" class="easyui-linkbutton"
											data-options="iconCls:'ext-icon-zoom',plain:true"
											onclick="searchFun();">查询</a>
									</td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
