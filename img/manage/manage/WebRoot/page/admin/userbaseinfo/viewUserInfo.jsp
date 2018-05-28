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
					url : '${pageContext.request.contextPath}/userBaseInfo/userBaseInfoAction!getById.action',
					data : $('form').serialize(),
					dataType : 'json',
					success : function(result) {
						if (result.user_id != undefined) {
							$('b').append(result.fullName+' ['+(result.appuser_id>0?'已注册':'未注册')+']');
							$('form').form('load', {
								'userbaseInfo.user_name' : result.user_name,
								'userbaseInfo.sex':result.sex,
								'userbaseInfo.studentType':result.studentType,
								'userbaseInfo.programLength':result.programLength,
								'userbaseInfo.resourceArea':result.resourceArea,
								'userbaseInfo.studentNumber':result.studentNumber,
								'userbaseInfo.entranceTime':$.format.date(result.entranceTime,'yyyy-MM-dd'),
								'userbaseInfo.graduationTime':$.format.date(result.graduationTime,'yyyy-MM-dd'),
								'userbaseInfo.aliasname' : result.aliasname,
								'userbaseInfo.birthday':$.format.date(result.birthday,'yyyy-MM-dd'),
								'userbaseInfo.cardType':result.cardType,
								'userbaseInfo.cardID':result.cardID,
								'userbaseInfo.tel_id':result.tel_id,
								'userbaseInfo.email':result.email,
								'userbaseInfo.qq':result.qq,
								'userbaseInfo.weibo':result.weibo,
								'userbaseInfo.nation':result.nation,
								'userbaseInfo.political':result.political,
								'userbaseInfo.nationality':result.nationality,
								'userbaseInfo.residentialArea':result.residentialArea,
								'userbaseInfo.residentialTel':result.residentialTel,
								'userbaseInfo.workUnit':result.workUnit,
								'userbaseInfo.profession':result.profession,
								'userbaseInfo.avocation':result.avocation,
								'userbaseInfo.status':result.status,
								'userbaseInfo.remarks':result.remarks
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
		
</script>
	</head>

	<body>
		<form method="post">
			<input name="userbaseInfo.user_id" type="hidden" id="user_id" value="${param.id}">
			<div style="text-align: center;"><b></b></div>
			<fieldset>
				<legend>
					基本信息
				</legend>
				<table class="ta001">
					<tr>
						<th width="10%">
							姓名                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
						</th>
						<td width="40%">
							<input name="userbaseInfo.user_name" class="easyui-validatebox" disabled="disabled" data-options="required:true,validType:'customRequired'"/>
						</td>
						<th>
							性别
						</th>
						<td>
							<select class="easyui-combobox" data-options="editable:false" name="userbaseInfo.sex" disabled="disabled" style="width: 150px;">
								<option value="男">男</option>
								<option value="女">女</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>
							学历
						</th>
						<td>
							<input class="easyui-combobox" style="width: 150px;" disabled="disabled" name="userbaseInfo.studentType"
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
							<input class="easyui-combobox" style="width: 150px;" disabled="disabled" name="userbaseInfo.programLength"
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
							学号
						</th>
						<td>
							<input name="userbaseInfo.studentNumber" class="easyui-validatebox" disabled="disabled"/>
						</td>
						<th>
							籍贯
						</th>
						<td>
							<input name="userbaseInfo.resourceArea" class="easyui-validatebox" disabled="disabled"/>
						</td>
					</tr>
					<tr>
						<th>
							入学时间
						</th>
						<td>
							<input name="userbaseInfo.entranceTime" class="easyui-validatebox" disabled="disabled" readonly="readonly" onClick="WdatePicker()"/>
						</td>
						<th>
							毕业时间
						</th>
						<td>
							<input name="userbaseInfo.graduationTime" class="easyui-validatebox" disabled="disabled" readonly="readonly" onClick="WdatePicker()"/>							
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
						<th width="10%">
							曾用名
						</th>
						<td width="40%">
							<input name="userbaseInfo.aliasname" class="easyui-validatebox" disabled="disabled"/>
						</td>
						<th>
							生日
						</th>
						<td>
							<input id="birthday" name="userbaseInfo.birthday" class="easyui-validatebox" disabled="disabled" readonly="readonly" onClick="WdatePicker()" />
						</td>
					</tr>
					<tr>
						<th>
							电话号码
						</th>
						<td>
							<input name="userbaseInfo.tel_id" class="easyui-validatebox" disabled="disabled" data-options="validType:'telePhone'"/>
						</td>
						<th>
							邮箱
						</th>
						<td>
							<input name="userbaseInfo.email" class="easyui-validatebox" disabled="disabled" data-options="validType:'email'"/>
						</td>
					</tr>
					<tr>
						<th>
							QQ
						</th>
						<td>
							<input name="userbaseInfo.qq" class="easyui-validatebox" disabled="disabled"/>
						</td>
						<th>
							微博
						</th>
						<td>
							<input name="userbaseInfo.weibo" class="easyui-validatebox" disabled="disabled"/>
						</td>
					</tr>
					<tr>
						<th>
							证件类型
						</th>
						<td>
							<input class="easyui-combobox" style="width: 150px;" name="userbaseInfo.cardType" disabled="disabled"
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
							<input name="userbaseInfo.cardID" class="easyui-validatebox" disabled="disabled"/>
						</td>
					</tr>
					<tr>
						<th>
							国籍
						</th>
						<td>
							<input name="userbaseInfo.nationality" class="easyui-validatebox" disabled="disabled"/>
						</td>
						<th>
							民族
						</th>
						<td>
							<input name="userbaseInfo.nation" class="easyui-validatebox" disabled="disabled"/>
						</td>
					</tr>
					<tr>
						<th>
							家庭地址
						</th>
						<td>
							<input name="userbaseInfo.residentialArea" class="easyui-validatebox" disabled="disabled"/>
						</td>
						<th>
							家庭电话
						</th>
						<td>
							<input name="userbaseInfo.residentialTel" class="easyui-validatebox" disabled="disabled"/>
						</td>
					</tr>
					<tr>
						<th>
							工作单位
						</th>
						<td>
							<input name="userbaseInfo.workUnit" class="easyui-validatebox" disabled="disabled"/>
						</td>
						<th>
							所属行业
						</th>
						<td>
							<input name="userbaseInfo.profession" class="easyui-validatebox" disabled="disabled"/>
						</td>
					</tr>
					<tr>
						<th>
							政治面貌
						</th>
						<td>
							<input name="userbaseInfo.political" class="easyui-validatebox" disabled="disabled"/>
						</td>
						<th>
							兴趣爱好
						</th>
						<td>
							<input name="userbaseInfo.avocation" class="easyui-validatebox" disabled="disabled"/>
						</td>
					</tr>
					<tr>
						<th>
							状态
						</th>
						<td>
							<input name="userbaseInfo.status" class="easyui-validatebox" disabled="disabled"/>
						</td>
						<th>	
						</th>
						<td>
						</td>
					</tr>
					<tr>
						<th>
							备注
						</th>
						<td colspan="3">
							<textarea rows="5" cols="90" name="userbaseInfo.remarks" disabled="disabled"></textarea>
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</body>
</html>
