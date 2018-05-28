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
		$(function() {
			var donationId=$('#donationId').val();
			if (donationId > 0) {
				$.ajax({  
					url:'${pageContext.request.contextPath}/donation/donationAction!doNotNeedSecurity_getById.action',
					data :{'id':donationId},
					dataType:'json',
					success : function(result){  
						if (result.donationId != undefined) {
							$('form').form('load', {
								'donation.projectId' : result.projectId,
								'donation.money' : result.money,
								'donation.payMoney' : result.payMoney,
								'donation.message' : result.message,
								'donation.remark' : result.remark,
								'departName' : result.x_depart,
								'gradeName' : result.x_grade,
								'className' : result.x_clazz,
								'sex' : result.x_sex,
								'telId' : result.x_phone,
								'email' : result.x_email,
								'mailingAddress' : result.x_address,
								'donation.confirmStatus':result.confirmStatus,
								'donation.payStatus':result.payStatus,
								'payTime' : result.payTime,
								'payDetail':result.payDetail,
								'donation.payModel':result.payMode,
								'userName':result.x_name,
								'orderNo':result.orderNo,
								'payTime':result.payTime,
								'schoolName':result.x_school,
								'majorName':result.x_major,
								'workunit':result.x_workunit,
								'position':result.x_position,
								'donationTime':result.donationTime
							});
						}
			          },
					  beforeSend:function(){
						parent.$.messager.progress({
							text : '数据加载中....'
						});
					  },
					  complete:function(){
						parent.$.messager.progress('close');
					  }  
				});
			}
		})
		function searchFun(){
				$('#cc').combogrid('grid').datagrid('load',serializeObject($('#searchForm')));
		}
		function submitForm($dialog, $grid, $pjq)
		{
			if ($('form').form('validate'))
			{
				$.ajax({
					url : '${pageContext.request.contextPath}/donation/donationAction!update.action',
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
							订单编号
						</th>
						<td colspan="3">
							<input name="orderNo" disabled="disabled" style="width: 300px;"/>
						</td>
					</tr>
					<tr>
						<th>
							捐赠项目
						</th>
						<td>
							<input id="donationId" name="donation.donationId" type="hidden" value="${param.id}">
							<input id="projectId" name="donation.projectId" class="easyui-combobox" style="width: 150px;" disabled="disabled"
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
							<input name="donation.money" disabled="disabled" style="width: 150px;" class="easyui-validatebox" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></input>
						</td>
					</tr>
					<tr>
						<th>
							捐赠时间
						</th>
						<td>
							<input name="donationTime" disabled="disabled"></input>
						</td>
						<th>
							支付金额/元
						</th>
						<td>
							<input name="donation.payMoney" disabled="disabled" style="width: 150px;" class="easyui-validatebox" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></input>
						</td>
					</tr>
					<tr>
						<th>
							支付时间
						</th>
						<td>
							<input name="payTime" disabled="disabled"></input>
						</td>
						<th>
							支付方式
						</th>
						<td>
							<input name="donation.payModel" disabled="disabled" class="easyui-validatebox"></input>
						</td>
					</tr>
					<tr>
						<th>
							支付状态
						</th>
						<td>
							<select class="easyui-combobox" disabled="disabled" data-options="editable:false" name="donation.payStatus" style="width: 150px;">
									<option value="0">未支付</option>
									<option value="1">已支付</option>
							</select>
						</td>
						<th>
							支付详情
						</th>
						<td>
							<input name="payDetail" disabled="disabled" style="width: 150px;" class="easyui-validatebox"></input>
						</td>
					</tr>
					<tr>
						<th>
							捐赠留言
						</th>
						<td colspan="3">
							<textarea rows="2" cols="100" name="donation.message" disabled="disabled"></textarea>
						</td>
					</tr>
					<tr>
						<th>
							备注
						</th>
						<td colspan="3">
							<textarea rows="2" cols="100" name="donation.remark" disabled="disabled"></textarea>
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
							<input name="userName" disabled="disabled">
						</td>
						<th>
							学校
						</th>
						<td>
							<input id="schoolName" name="schoolName" disabled="disabled" type="text"></input>
						</td>
					</tr>
					<tr>
						<th>
							院系
						</th>
						<td>
							<input id="departName" name="departName" disabled="disabled" type="text"></input>
						</td>
						<th>
							年级
						</th>
						<td>
							<input id="gradeName" name="gradeName" disabled="disabled" type="text"></input>
						</td>
					</tr>
					<tr>
						<th>
							班级
						</th>
						<td>
							<input id="className" name="className" disabled="disabled" type="text"></input>
						</td>
						<th>
							专业
						</th>
						<td>
							<input id="majorName" name="majorName" disabled="disabled" type="text"></input>
						</td>
					</tr>
					<tr>
						<th>
							性别
						</th>
						<td>
							<input id="sex" name="sex" disabled="disabled" type="text"></input>
						</td>
						<th>
							联系电话
						</th>
						<td>
							<input id="telId" name="telId" disabled="disabled" type="text"></input>
						</td>
					</tr>
					<tr>
						<th>
							邮箱
						</th>
						<td>
							<input id="email" name="email"  disabled="disabled" type="text"></input>
						</td>
						<th>
							联系地址
						</th>
						<td>
							<input id="mailingAddress" name="mailingAddress" disabled="disabled" type="text"></input>
						</td>
					</tr>
					<tr>
						<th>
							工作单位
						</th>
						<td>
							<input name="workunit"  disabled="disabled" type="text"></input>
						</td>
						<th>
							职务
						</th>
						<td>
							<input name="position" disabled="disabled" type="text"></input>
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
					</tr>
				</table>
			</fieldset>
		</form>
	</body>
</html>
