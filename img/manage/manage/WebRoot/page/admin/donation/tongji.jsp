<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	var tongjiGrid;
	$(function() {
		tongjiGrid = $('#tongjiGrid')
				.datagrid(
						{
							url : '${pageContext.request.contextPath}/donation/donationAction!dataGridForCount.action',
							fit : true,
							border : false,
							fitColumns : true,
							striped : true,
							rownumbers : true,
							pagination : true,
							columns : [ [ {
								width : '100',
								title : '学校',
								field : 'x_school',
								align : 'center'
							}, {
								width : '200',
								title : '院系',
								field : 'x_depart',
								align : 'center'
							}, {
								width : '120',
								title : '专业',
								field : 'x_major',
								align : 'center'
							}, {
								width : '80',
								title : '年级',
								field : 'x_grade',
								align : 'center'
							}, {
								width : '200',
								title : '班级',
								field : 'x_clazz',
								align : 'center'
							}, {
								width : '150',
								title : '捐赠总金额',
								field : 'totalDonationMoney',
								align : 'center'
							}, {
								width : '150',
								title : '支付总金额',
								field : 'totalMoney',
								align : 'center'
							}, {
								width : '100',
								title : '总人数',
								field : 'totalPeople',
								align : 'center'
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

	/**--重置--**/
	function searchFun() {
		$('#tongjiGrid').datagrid('load', serializeObject($('#searchForm')));
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
		$('#majorId').prop('value', '');
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<form id="searchForm">
						<table border="0">
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
								<th align="right" width="30px;">捐赠项目</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
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
								<th align="right" width="30px;">性别</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><select class="easyui-combobox"
									data-options="editable:false" name="donation.userInfo.sex"
									style="width: 150px;">
										<option value="">全部</option>
										<option value="男">男</option>
										<option value="女">女</option>
								</select></td>
								<th align="right" width="30px;">入学年份</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><input name="donation.userInfo.entranceTime"
									readonly="readonly" class="easyui-validatebox"
									style="width: 150px;"
									onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy'})" /></td>
								<th align="right" width="30px;">统计方式</th>
								<td>
									<div class="datagrid-btn-separator"></div></td>
								<td><select class="easyui-combobox"
									data-options="editable:false" name="countMethod"
									style="width: 150px;">
										<option value="4">按学校统计</option>
										<option value="1">按院系统计</option>
										<option value="5">按专业统计</option>
										<option value="2">按年级统计</option>
										<option value="3">按班级统计</option>
								</select></td>
								<td><a href="javascript:void(0);" class="easyui-linkbutton"
									data-options="iconCls:'icon-search',plain:true"
									onclick="searchFun();">查询</a> <a href="javascript:void(0);"
									class="easyui-linkbutton"
									data-options="iconCls:'icon-redo',plain:true"
									onclick="resetT()">重置</a></td>
							</tr>
						</table>
					</form></td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="tongjiGrid"></table>
	</div>
</body>
</html>
