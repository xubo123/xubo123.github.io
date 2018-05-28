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
				url : '${pageContext.request.contextPath}/userBaseInfo/userBaseInfoAction!getAlumniInfoByIds.action',
				data : $('form').serialize(),
				dataType : 'json',
				success : function(result) {
					if (result[0].user_id != undefined) {
					
						$('form').form('load', {
							'userbaseInfo.user_name' : result[0].user_name,
							'userbaseInfo.sex':result[0].sex,
							'userbaseInfo.resourceArea':result[0].resourceArea,
							'userbaseInfo.aliasname' : result[0].aliasname,
							'userbaseInfo.birthday':$.format.date(result[0].birthday,'yyyy-MM-dd'),
							'userbaseInfo.cardType':result[0].cardType,
							'userbaseInfo.cardID':result[0].cardID,
							'userbaseInfo.tel_id':result[0].tel_id,
							'userbaseInfo.email':result[0].email,
							'userbaseInfo.qq':result[0].qq,
							'userbaseInfo.weibo':result[0].weibo,
							'userbaseInfo.nation':result[0].nation,
							'userbaseInfo.political':result[0].political,
							'userbaseInfo.nationality':result[0].nationality,
							'userbaseInfo.residentialArea':result[0].residentialArea,
							'userbaseInfo.residentialTel':result[0].residentialTel,
							'userbaseInfo.workUnit':result[0].workUnit,
							'userbaseInfo.profession':result[0].profession,
							'userbaseInfo.avocation':result[0].avocation,
							'userbaseInfo.status':result[0].status,
							'userbaseInfo.remarks':result[0].remarks
						});
						
						$.each(result, function(i, value) {
							var entranceTime='';
							var graduationTime = '';
							if(value.entranceTime!=undefined){
								entranceTime = $.format.date(value.entranceTime,'yyyy-MM-dd')
							}
							if(value.graduationTime!=undefined){
								graduationTime = $.format.date(value.graduationTime,'yyyy-MM-dd')
							}
							var txt=
							"<table  class='ta001'>"+
							"<tr>"+
							"<th>"+
								"学校"+
							"</th>"+
							"<td>"+
								"<input type='text' disabled='disabled' value='"+value.schoolName+"'/>"+
							"</td>"+
							"<th>"+
								"院系"+
							"</th>"+
							"<td>"+
								"<input type='text' disabled='disabled' value='"+value.college+"'/>"+
							"</td>"+
							"</tr>"+
							"<tr>"+
							"<th>"+
								"专业"+
							"</th>"+
							"<td>"+
								"<input type='text' disabled='disabled' value='"+value.major+"'/>"+
							"</td>"+
							"<th>"+
								"年级"+
							"</th>"+
							"<td>"+
								"<input type='text' disabled='disabled' value='"+value.grade+"'/>"+
							"</td>"+
							"</tr>"+
							"<tr>"+
							"<th>"+
								"班级"+
							"</th>"+
							"<td>"+
								"<input type='text' disabled='disabled' value='"+value.className+"'/>"+
							"</td>"+
							"<th>"+
								"学历"+
							"</th>"+
							"<td>"+
								"<input type='text' disabled='disabled' value='"+value.studentType+"'/>"+
							"</td>"+
							"</tr>"+
							"<tr>"+
							"<th>"+
								"学制"+
							"</th>"+
							"<td>"+
								"<input type='text' disabled='disabled' value='"+value.programLength+"'/>"+
							"</td>"+
							"<th>"+
								"学号"+
							"</th>"+
							"<td>"+
								"<input type='text' disabled='disabled' value='"+value.studentNumber+"'/>"+
							"</td>"+
							"</tr>"+
							"<tr>"+
							"<th>"+
								"入学时间"+
							"</th>"+
							"<td>"+
								"<input type='text' disabled='disabled' value='"+entranceTime+"'/>"+
							"</td>"+
							"<th>"+
								"毕业时间"+
							"</th>"+
							"<td>"+
								"<input type='text' disabled='disabled' value='"+graduationTime+"'/>"+
							"</td>"+
							"</tr>"+
							"</table><br/>"
							$('#stuExp').append(txt);
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
			<input name="userbaseInfo.allUser_id" type="hidden" id="allUser_id" value="${param.id}">
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
							籍贯
						</th>
						<td>
							<input name="userbaseInfo.resourceArea" class="easyui-validatebox" disabled="disabled"/>
						</td>
						<th>
						</th>
						<td>
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
			<br>
			<fieldset>
				<legend>
					学习经历
				</legend>
				<div id="stuExp">
					
				</div>
			</fieldset>
		</form>
	</body>
</html>
