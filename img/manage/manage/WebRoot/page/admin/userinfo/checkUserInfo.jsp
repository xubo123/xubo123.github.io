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
				$.ajax({
					url : '${pageContext.request.contextPath}/userInfo/userInfoAction!doNotNeedSecurity_getUserInfoByUserId.action',
					data : $('form').serialize(),
					dataType : 'json',
					success : function(result) {
						if (result.userId != undefined) {
							$('b').append(result.fullName);
							if(result.checkFlag==1){
								$('#st').html('<span style="color:green;">正式校友</span>');
							}else{
								$('#st').html('<span style="color:red;">待核校友</span>');
							}
							$('form').form('load', {
								'userInfo.userName' : result.userName,
								'userInfo.aliasname' : result.aliasname,
								'userInfo.sex':result.sex,
								'userInfo.birthday':$.format.date(result.birthday,'yyyy-MM-dd'),
								//'userInfo.card':result.card,
								'userInfo.telId':result.telId,
								'userInfo.email':result.email,
								'userInfo.qq':result.qq,
								//'userInfo.cardType':result.cardType,
								'userInfo.studentnumber':result.studentnumber,
								'userInfo.nation':result.nation,
								'userInfo.political':result.political,
								'userInfo.nationality':result.nationality,
								'userInfo.entranceTime':$.format.date(result.entranceTime,'yyyy-MM-dd'),
								'userInfo.graduationTime':$.format.date(result.graduationTime,'yyyy-MM-dd'),
								'userInfo.programLength':result.programLength,
								'userInfo.studentType':result.studentType,
								'userInfo.resourceArea':result.resourceArea,
								'userInfo.status':result.status,
								'userInfo.weibo':result.weibo,
								'userInfo.personalWebsite':result.personalWebsite,
								'userInfo.mailingAddress':result.mailingAddress,
								'userInfo.residentialArea':result.residentialArea,
								'userInfo.workUnit':result.workUnit,
								'userInfo.workTel':result.workTel,
								'userInfo.workAddress':result.workAddress,
								'userInfo.position':result.position,
								'userInfo.industryType':result.industryType,
								'userInfo.enterprise':result.enterprise,
								'userInfo.remarks':result.remarks,
								'userInfo.majorId':result.majorId==0?'':result.majorId
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
		});
		
	function submitForm($dialog, $grid, $pjq)
	{
		if ($('form').form('validate'))
		{
			$.ajax({
				url : '${pageContext.request.contextPath}/userInfo/userInfoAction!updateIdea.action',
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
			<div style="text-align: center;"><b></b></div>
			<fieldset>
				<legend>
					审核信息
				</legend>
				<table class="ta001">
					<tr>
						<th>审核状态</th>
						<td>
							<select class="easyui-combobox" data-options="editable:false" name="userInfo.checkFlag" style="width: 150px;">
								<option value="1">通过</option>
								<option value="0">不通过</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>审核意见</th>
						<td>
							<textarea rows="5" cols="80" name="userInfo.checkIdea"></textarea>
						</td>
					</tr>
				</table>
			</fieldset>
			<br/>
			<fieldset>
				<legend>
					基本信息
				</legend>
				<table class="ta001">
					<tr>
						<th>
							姓名
						</th>
						<td>
							<input name="userInfo.userId" type="hidden" value="${param.id}">
							<input name="userInfo.userName" class="easyui-validatebox" disabled="disabled" disdata-options="required:true,validType:'customRequired'"/>
						</td>
						<th>
							性别
						</th>
						<td>
							<select class="easyui-combobox" disabled="disabled" data-options="editable:false" name="userInfo.sex" style="width: 150px;">
								<option value="男">男</option>
								<option value="女">女</option>
							</select>
						</td>
					</tr>
					<tr>
					</tr>
					<tr>
						<th>
							学号
						</th>
						<td>
							<input name="userInfo.studentnumber" disabled="disabled" class="easyui-validatebox" />
						</td>
						<th>
							专业
						</th>
						<td>
							<input id="majorId" class="easyui-combobox" disabled="disabled" style="width: 150px;" name="userInfo.majorId"
											data-options="  
											valueField: 'majorId',  
											textField: 'majorName',  
											editable:false,
											url: '${pageContext.request.contextPath}/major/majorAction!doNotNeedSecurity_getMajor.action?deptId=${param.id.substring(0,10)}' 
										" />
						</td>		
					</tr>
					<tr>
						<th>
							入学时间
						</th>
						<td>
							<input name="userInfo.entranceTime" disabled="disabled" class="easyui-validatebox" readonly="readonly" onClick="WdatePicker()"/>
						</td>
						<th>
							毕业时间
						</th>
						<td>
							<input name="userInfo.graduationTime" disabled="disabled" class="easyui-validatebox" readonly="readonly" onClick="WdatePicker()"/>
							
						</td>
					</tr>
					<tr>
						<th>
							学历
						</th>
						<td>
							<input class="easyui-combobox" disabled="disabled" style="width: 150px;" name="userInfo.studentType"
								data-options="  
								valueField: 'dictName',  
								textField: 'dictName',  
								editable:false,
								url: '${pageContext.request.contextPath}/dicttype/dictTypeAction!doNotNeedSecurity_getDict.action?dictTypeName='+encodeURI('学历') 
							" />
						</td>
						<th>
							学制
						</th>
						<td>
							<input class="easyui-combobox" disabled="disabled" style="width: 150px;" name="userInfo.programLength"
								data-options="  
								valueField: 'dictName',  
								textField: 'dictName',  
								editable:false,
								url: '${pageContext.request.contextPath}/dicttype/dictTypeAction!doNotNeedSecurity_getDict.action?dictTypeName='+encodeURI('学制') 
							" />
						</td>
					</tr>
					<tr>
						<th>
							生源
						</th>
						<td>
							<input name="userInfo.resourceArea" disabled="disabled" class="easyui-validatebox"/>
						</td>
						<th>状态</th>
						<td id="st">
							
						</td>
					</tr>
				</table>
			</fieldset>
			<br/>
			<fieldset>
				<legend>
					其它信息
				</legend>
				<table class="ta001">
					<tr>
						<th>
							曾用名
						</th>
						<td>
							<input name="userInfo.aliasname" disabled="disabled" class="easyui-validatebox"/>
						</td>
						<th>
							生日
						</th>
						<td>
							<input id="birthday" disabled="disabled" name="userInfo.birthday" class="easyui-validatebox" readonly="readonly" onClick="WdatePicker()" />
						</td>
					</tr>
					<!--  
					<tr>
						<th>
							证件类型
						</th>
						<td>
							<input class="easyui-combobox" disabled="disabled" style="width: 150px;" name="userInfo.cardType"
											data-options="  
												valueField: 'dictName',  
												textField: 'dictName',  
												editable:false,
												url: '${pageContext.request.contextPath}/dicttype/dictTypeAction!doNotNeedSecurity_getDict.action?dictTypeName='+encodeURI('证件类型') 
												" />
						</td>
						<th>
							证件号码
						</th>
						<td>
							<input name="userInfo.card" disabled="disabled" class="easyui-validatebox" />
						</td>
					</tr>
					-->
					<tr>
						<th>
							电话号码
						</th>
						<td>
							<input name="userInfo.telId" disabled="disabled" class="easyui-validatebox" data-options="validType:'telePhone'"/>
						</td>
						<th>
							国籍
						</th>
						<td>
							<input name="userInfo.nationality" disabled="disabled" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							民族
						</th>
						<td>
							<input name="userInfo.nation" disabled="disabled" class="easyui-validatebox"/>
						</td>
						<th>
							政治面貌
						</th>
						<td>
							<input name="userInfo.political" disabled="disabled" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							邮箱
						</th>
						<td>
							<input name="userInfo.email" disabled="disabled" class="easyui-validatebox" data-options="validType:'email'"/>
						</td>
						<th>
							QQ
						</th>
						<td>
							<input name="userInfo.qq" disabled="disabled" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							微博
						</th>
						<td>
							<input name="userInfo.weibo" disabled="disabled" class="easyui-validatebox"/>
							
						</td>
						<th>
							个人网站
						</th>
						<td>
							<input name="userInfo.personalWebsite" disabled="disabled" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							通讯地址
						</th>
						<td>
							<input name="userInfo.mailingAddress" disabled="disabled" class="easyui-validatebox"/>
							
						</td>
						<th>
							居住地区
						</th>
						<td>
							<input name="userInfo.residentialArea" disabled="disabled" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							工作单位
						</th>
						<td>
							<input name="userInfo.workUnit" disabled="disabled" class="easyui-validatebox"/>
							
						</td>
						<th>
							工作电话
						</th>
						<td>
							<input name="userInfo.workTel" disabled="disabled" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							工作地址
						</th>
						<td>
							<input name="userInfo.workAddress" disabled="disabled" class="easyui-validatebox"/>
							
						</td>
						<th>
							职务
						</th>
						<td>
							<input name="userInfo.position" disabled="disabled" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							行业类别
						</th>
						<td>
							<input name="userInfo.industryType" disabled="disabled" class="easyui-validatebox"/>
							
						</td>
						<th>
							企业性质
						</th>
						<td>
							<input name="userInfo.enterprise" disabled="disabled" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							状态
						</th>
						<td colspan="3">
							<input name="userInfo.status" disabled="disabled" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							备注
						</th>
						<td colspan="3">
							<textarea rows="5" cols="90" disabled="disabled" name="userInfo.remarks"></textarea>
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</body>
</html>
