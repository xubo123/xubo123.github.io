<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title></title>
<jsp:include page="../../../inc.jsp"></jsp:include>
<script type="text/javascript">
		$(function() {
			$.ajax({
				url : '${pageContext.request.contextPath}/dept/deptAction!doNotNeedSecurity_getById.action',
				data : $('form').serialize(),
				dataType : 'json',
				success : function(result) {
					if (result.department_id != undefined) {
						$('form').form('load', {
							'department.departmentName':result.departmentName,
							'department.schoolName':result.schoolName
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
			var submitForm = function($dialog, $grid, $pjq) {
				if ($('form').form('validate')) {
					$.ajax({
						url : '${pageContext.request.contextPath}/dept/deptAction!updateBelong.action',
						data : $('form').serialize(),
						dataType : 'json',
						success : function(result) {
							if (result.success) {
								if(${param.id.length()==1}){
									refrensh();
								}else{
									$grid.tree('reload');
								}
								$dialog.dialog('destroy');
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
			}
			
			function refrensh(){
				var panel = parent.$('#mainTabs').tabs('getSelected').panel('panel');
				var frame = panel.find('iframe');
				try {
					if (frame.length > 0) {
						for (var i = 0; i < frame.length; i++) {
							frame[i].contentWindow.document.write('');
							frame[i].contentWindow.close();
							frame[i].src = frame[i].src;
						}
						if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
							try {
								CollectGarbage();
							} catch (e) {
							}
						}
					}
				} catch (e) {
				}
			}
		</script>
</head>

<body>
	<form method="post" id="addDeptForm">
		<input id="departmentId" name="department.department_id" type="hidden">
		<fieldset>
			<legend> 设置归属院系 </legend>
	        <input id="classId" name="classId" type="hidden" value="${param.id}">
			<table class="ta001">
				<tr>
					<th>学校</th>
					<td><input id="schoolName" name="department.schoolName"
						class="easyui-combobox" style="width: 150px;"
						data-options="
													editable:false,
													valueField:'schoolName',
													textField:'schoolName',
													disabled:true,
													url:'${pageContext.request.contextPath}/dept/deptAction!doNotNeedSecurity_getDepart_new.action',
													onSelect: function(rec){   
												    var url = '${pageContext.request.contextPath}/dept/deptAction!doNotNeedSecurity_getByParentId_new.action?type=department&deptName='+rec.schoolName; 
												    url=encodeURI(url); 
                                                    url=encodeURI(url); 
                                                    $('#department').combobox('reload', url);
												}
													" />
					</td>
					<th>归属院系</th>
					<td><input id="department" name="department.departmentName"
						class="easyui-combobox" style="width: 150px;"
						data-options="
													editable:false,
													valueField:'deptId',
													textField:'deptName',
													disabled:true,
													onSelect: function(rec){   
													$('#departmentId').prop('value',$('#department').combobox('getValue')) 
												}
													" />
						<a href="javascript:void(0)"
						onclick="$('#department').combobox('clear');$('#schoolName').combobox('clear');$('#department').combobox('enable');$('#schoolName').combobox('enable');">修改</a>
					</td>
				</tr>
			</table>
		</fieldset>
	</form>
</body>
</html>
