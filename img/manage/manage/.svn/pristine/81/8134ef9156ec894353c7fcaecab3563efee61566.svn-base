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
					$('b').append(' ['+(result.appuser_id>0?'已注册':'未注册')+']');
					$('form').form('load',{
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
						'userbaseInfo.remarks':result.remarks,
						'userbaseInfo.class_id':result.class_id,
						'userbaseInfo.appuser_id':result.appuser_id
					});
					$('#school').combobox('setValue',result.schoolName);
					$('#college').combobox('setValue',result.college);
					$('#major').combobox('setValue',result.major);
					$('#grade').combobox('setValue',result.grade);
					$('#classes').combobox('setValue',result.className);
				}
			},
			beforeSend : function() {
				parent.$.messager.progress({
					text : '数据加载中....'
				});
			},
			complete : function() {
				parent.$.messager.progress('close');
			}
		});
	});

	function submitForm($dialog, $grid, $pjq) {
		if($("#inputs").is(":hidden")&&$("#select").is(":visible")){
			if($('#classId').val()==''){
				parent.$.messager.alert('提示', '请选择班级', 'info');
				return false;
			}
		}
		if($("#inputs").is(":visible")&&$("#select").is(":hidden")){
			if($('#schoolName').val()==''){
				parent.$.messager.alert('提示', '请输入学校名称', 'info');
				return false;
			}
			if($('#collegeName').val()==''){
				parent.$.messager.alert('提示', '请输入院系名称', 'info');
				return false;
			}
			if($('#majorName').val()==''){
				parent.$.messager.alert('提示', '请输入专业名称', 'info');
				return false;
			}
			if($('#gradeName').val()==''){
				parent.$.messager.alert('提示', '请输入年级', 'info');
				return false;
			}
			if($('#className').val()==''){
				parent.$.messager.alert('提示', '请输入班级名称', 'info');
				return false;
			}
		}
		if ($('form').form('validate')) {
			$
					.ajax({
						url : '${pageContext.request.contextPath}/userBaseInfo/userBaseInfoAction!update.action',
						data : $('form').serialize(),
						dataType : 'json',
						success : function(result) {
							if (result.success) {
								$grid.datagrid('reload');
								$dialog.dialog('destroy');
								$pjq.messager.alert('提示', result.msg, 'info');
							} else {
								$pjq.messager.alert('提示', result.msg, 'error');
							}
						},
						beforeSend : function() {
							parent.$.messager.progress({
								text : '数据提交中....'
							});
						},
						complete : function() {
							parent.$.messager.progress('close');
						}
					});
		}
	};

	function changeInput(){
		if($("#inputs").is(":hidden")&&$("#select").is(":visible")){
			$("#inputs").show();
			$("#select").hide();
			$('#isInput').prop('value','1');
		}
		else if($("#inputs").is(":visible")&&$("#select").is(":hidden")){
			$("#inputs").hide();
			$("#select").show();
			$('#isInput').prop('value','0');
		}
	}
	
</script>
</head>

<body>
	<form method="post">
		<input id="isInput" name="isInput" type="hidden">
		<input name="userbaseInfo.user_id" type="hidden" id="user_id" value="${param.id}">
		<input name="userbaseInfo.appuser_id" type="hidden" id="appuser_id">
		<div style="text-align: center;"><b></b></div>
		<fieldset>
				<legend>
					院系信息
				</legend>
				<table class="ta001" id="select">
					<tr>
						<th width="10%">
							学校                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
						</th>
						<td width="40%">
							<input name="userbaseInfo.class_id" id="classId" type="hidden">
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
											$('#classId').prop('value','');
							                }
							            }],
									url: '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action',  
									onSelect: function(rec){
										var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(rec.fullName);    
										$('#college').combobox('clear');
						                $('#major').combobox('clear');
										$('#grade').combobox('clear');
										$('#classes').combobox('clear');
										$('#college').combobox('reload',url); 
										$('#major').combobox('loadData',[]);
										$('#grade').combobox('loadData',[]);
										$('#classes').combobox('loadData',[]);
										$('#classId').prop('value','');
									}" />
						</td>
						<th>
							院系
						</th>
						<td>
							<input id="college" class="easyui-combobox"
								style="width: 150px;"
								data-options="    
						            valueField: 'fullName',  
									textField: 'singleName',	
									editable:false,
									onSelect: function(rec){
										var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(rec.fullName); 
						                $('#major').combobox('clear');
										$('#grade').combobox('clear');
										$('#classes').combobox('clear');
										$('#major').combobox('reload',url);
										$('#grade').combobox('loadData',[]);
										$('#classes').combobox('loadData',[]);
										$('#classId').prop('value','');
									}" />
						</td>
					</tr>
					<tr>
						<th>
							专业
						</th>
						<td>
							<input id="major" class="easyui-combobox"
								style="width: 150px;"
								data-options="    
						            valueField: 'fullName',  
									textField: 'singleName',	
									editable:false,
									onSelect: function(rec){
										var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(rec.fullName); 
										$('#grade').combobox('clear');
										$('#classes').combobox('clear');
										$('#grade').combobox('reload',url);
										$('#classes').combobox('loadData',[]);
										$('#classId').prop('value','');
									}" />
						</td> 
						<th>
							年级
						</th>
						<td>
							<input id="grade" class="easyui-combobox"
								style="width: 150px;"
								data-options="    
						            valueField: 'fullName',  
									textField: 'singleName',	
									editable:false,
									onSelect: function(rec){
										var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(rec.fullName); 
										$('#classes').combobox('clear');
										$('#classes').combobox('reload',url);
										$('#classId').prop('value','');
									}" />
						</td>
					</tr>
					<tr>
						<th>
							班级
						</th>
						<td>
							<input id="classes" class="easyui-combobox" style="width: 150px;"
								data-options="
									valueField: 'fullName',  
									textField: 'singleName',	
									editable:false,
									onSelect: function(rec){
										$('#classId').prop('value',rec.fullName)  
									}
									"/>
						</td>
						<th></th>
						<td><a href="javascript:void(0)" onclick="changeInput()">找不到院系信息?切换至手动输入</a></td>				
					</tr>
				</table>
				<table class="ta001" id="inputs" style="display: none;">
					<tr>
						<th width="10%">
							学校                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
						</th>
						<td width="40%">
							<input id="schoolName" name="userbaseInfo.schoolName" class="easyui-validatebox" style="width: 150px;"/>
						</td>
						<th>
							院系
						</th>
						<td>
							<input id="collegeName" name="userbaseInfo.college" class="easyui-validatebox" style="width: 150px;"/>
						</td>
					</tr>
					<tr>
						<th>
							专业
						</th>
						<td>
							<input id="majorName" name="userbaseInfo.major" class="easyui-validatebox" style="width: 150px;"/>
						</td>
						<th>
							年级
						</th>
						<td>
							<input id="gradeName" name="userbaseInfo.grade" class="easyui-validatebox" style="width: 150px;" onClick="WdatePicker({skin:'default',dateFmt:'yyyy'})" readonly="readonly"/>级
						</td>
					</tr>
					<tr>
						<th>
							班级
						</th>
						<td>
							<input id="className" name="userbaseInfo.className" class="easyui-validatebox" style="width: 150px;"/>
						</td>
						<th>
						</th>
						<td>
				            <a href="javascript:void(0)" onclick="changeInput()">切换至自动选择</a>
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
						<th width="10%">
							姓名                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
						</th>
						<td width="40%">
							<input name="userbaseInfo.user_name" class="easyui-validatebox" data-options="required:true,validType:'customRequired'"/>
						</td>
						<th>
							性别
						</th>
						<td>
							<select class="easyui-combobox" data-options="editable:false" name="userbaseInfo.sex" style="width: 150px;">
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
							<input class="easyui-combobox" style="width: 150px;" name="userbaseInfo.studentType"
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
							<input class="easyui-combobox" style="width: 150px;" name="userbaseInfo.programLength"
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
							<input name="userbaseInfo.studentNumber" class="easyui-validatebox" />
						</td>
						<th>
							籍贯
						</th>
						<td>
							<input name="userbaseInfo.resourceArea" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							入学时间
						</th>
						<td>
							<input name="userbaseInfo.entranceTime" class="easyui-validatebox" readonly="readonly" onClick="WdatePicker()"/>
						</td>
						<th>
							毕业时间
						</th>
						<td>
							<input name="userbaseInfo.graduationTime" class="easyui-validatebox" readonly="readonly" onClick="WdatePicker()"/>							
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
							<input name="userbaseInfo.aliasname" class="easyui-validatebox"/>
						</td>
						<th>
							生日
						</th>
						<td>
							<input id="birthday" name="userbaseInfo.birthday" class="easyui-validatebox" readonly="readonly" onClick="WdatePicker()" />
						</td>
					</tr>
					<tr>
						<th>
							电话号码
						</th>
						<td>
							<input name="userbaseInfo.tel_id" class="easyui-validatebox" data-options="validType:'telePhone'"/>
						</td>
						<th>
							邮箱
						</th>
						<td>
							<input name="userbaseInfo.email" class="easyui-validatebox" data-options="validType:'email'"/>
						</td>
					</tr>
					<tr>
						<th>
							QQ
						</th>
						<td>
							<input name="userbaseInfo.qq" class="easyui-validatebox"/>
						</td>
						<th>
							微博
						</th>
						<td>
							<input name="userbaseInfo.weibo" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							证件类型
						</th>
						<td>
							<input class="easyui-combobox" style="width: 150px;" name="userbaseInfo.cardType"
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
							<input name="userbaseInfo.cardID" class="easyui-validatebox" />
						</td>
					</tr>
					<tr>
						<th>
							国籍
						</th>
						<td>
							<input name="userbaseInfo.nationality" class="easyui-validatebox"/>
						</td>
						<th>
							民族
						</th>
						<td>
							<input name="userbaseInfo.nation" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							家庭地址
						</th>
						<td>
							<input name="userbaseInfo.residentialArea" class="easyui-validatebox"/>
						</td>
						<th>
							家庭电话
						</th>
						<td>
							<input name="userbaseInfo.residentialTel" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							工作单位
						</th>
						<td>
							<input name="userbaseInfo.workUnit" class="easyui-validatebox"/>
						</td>
						<th>
							所属行业
						</th>
						<td>
							<input name="userbaseInfo.profession" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							政治面貌
						</th>
						<td>
							<input name="userbaseInfo.political" class="easyui-validatebox"/>
						</td>
						<th>
							兴趣爱好
						</th>
						<td>
							<input name="userbaseInfo.avocation" class="easyui-validatebox"/>
						</td>
					</tr>
					<tr>
						<th>
							状态
						</th>
						<td>
							<input name="userbaseInfo.status" class="easyui-validatebox"/>
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
							<textarea rows="5" cols="90" name="userbaseInfo.remarks"></textarea>
						</td>
					</tr>
				</table>
			</fieldset>
	</form>
</body>
</html>
