<%@ page language="java" pageEncoding="UTF-8" %>
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
    <jsp:include page="../../../inc.jsp"></jsp:include>
    <script type="text/javascript">
        var eventGrid;
        $(function () {
            eventGrid = $('#eventGrid').datagrid({
                url: '${pageContext.request.contextPath}/classInfo/classInfoAction!getList.action',
                nowrap: false,
                fit: true,
                border: false,
                fitColumns: true,
                striped: true,
                rownumbers: true,
                pagination: true,
                singleSelect: true,
                columns: [[
					{
					    width: '100',
					    title: '学校',
					    field: 'schoolName'
					},
					{
					    width: '100',
					    title: '院系',
					    field: 'college'
					},
					{
					    width: '100',
					    title: '专业',
					    field: 'major'
					},
					{
					    width: '100',
					    title: '年级',
					    field: 'grade'
					},
					{
					    width: '100',
					    title: '班级',
					    field: 'className'
					},
					{
					    width: '60',
					    title: '学生人数',
					    field: 'studentNum',
					    align: 'center'
					},
					{
					    width: '100',
					    title: '隶属当前院系',
					    field: 'affiliationName'
					},
					{
	                    title: '操作',
	                    field: 'action',
	                    width: '80',
	                    formatter: function (value, row) {
	                        var str = '';
                        	<authority:authority authorizationCode="编辑班级" role="${sessionScope.user.role}">
								str += '<a href="javascript:void(0)" onclick="editFun(' + row.class_id + ');"><img class="iconImg ext-icon-note_edit"/>编辑</a>&nbsp;';
							</authority:authority>          
                        	<authority:authority authorizationCode="删除班级" role="${sessionScope.user.role}">
								str += '<a href="javascript:void(0)" onclick="doDel(' + row.class_id + ');"><img class="iconImg ext-icon-note_delete"/>删除</a>&nbsp;';
							</authority:authority>
	                        return str;
	                    }
	                 }
                    ]],
                toolbar: '#toolbar',
                onBeforeLoad: function (param) {
                    parent.$.messager.progress({
                        text: '数据加载中....'
                    });
                },
                onLoadSuccess: function (data) {
                    $('.iconImg').attr('src', pixel_0);
                    parent.$.messager.progress('close');
                }
            });
        });

        function searchT(){
			  if ($('#searchForm').form('validate')) {
				  $('#eventGrid').datagrid('load',serializeObject($('#searchForm')));
			  }
		}
		
		/**--重置--**/
		function resetT(){				
			$('#searchForm')[0].reset();
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
			
			$('#affiliation').combobox('clear');
		}
		
		var addFun = function () {
            var dialog = parent.WidescreenModalDialog({
                title: '新增班级',
                iconCls: 'ext-icon-note_add',
                url: '${pageContext.request.contextPath}/page/admin/classinfo/editClass.jsp?id=0',
                buttons: [{
                    text: '保存',
                    iconCls: 'ext-icon-save',
                    handler: function () {
                        dialog.find('iframe').get(0).contentWindow.submitForm(dialog, eventGrid, parent.$);
                    }
                }]
            });
        };

        var editFun = function (id) {
            var dialog = parent.WidescreenModalDialog({
                title: '编辑班级',
                iconCls: 'ext-icon-note_edit',
                url: '${pageContext.request.contextPath}/page/admin/classinfo/editClass.jsp?id=' + id,
                buttons: [{
                    text: '保存',
                    iconCls: 'ext-icon-save',
                    handler: function () {
                        dialog.find('iframe').get(0).contentWindow.submitForm(dialog, eventGrid, parent.$);
                    }
                }]
            });
        };
        
        function doDel(id){
        	$.messager.confirm('确认', '确定删除该班级吗？', function(r) {
				if (r) {
					$.ajax({
						url : '${pageContext.request.contextPath}/classInfo/classInfoAction!delete.action',
						data : {
							id : id
						},
						dataType : 'json',
						success : function(data) {
							if(data.success){
								$("#eventGrid").datagrid('reload');
								$("#eventGrid").datagrid('unselectAll');
								$.messager.alert('提示',data.msg,'info');
							}
							else{
								$.messager.alert('错误', data.msg, 'error');
							}
						},
						beforeSend:function(){
							$.messager.progress({
								text : '数据提交中....'
							});
						},
						complete:function(){
							$.messager.progress('close');
						}
					});
				}
			});
        }
       
    </script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">

<div id="toolbar" style="display: none;">
    <table>
        <tr>
            <td>
                <form id="searchForm">
                    <table>
                        <tr>   
                            <th align="right" width="75px;">
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
										var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(rec.fullName);    
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
										var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(rec.fullName); 
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
										var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(rec.fullName); 
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
										var url = '${pageContext.request.contextPath}/classInfo/classInfoAction!doNotNeedSecurity_getDeptByParent.action?pn='+encodeURI(rec.fullName); 
										$('#classes').combobox('clear');
										$('#classes').combobox('reload',url);
										$('#gradeName').prop('value',rec.singleName);
										$('#className').prop('value','');
									}" />
							</td>      
                        </tr>
                        <tr>
                        	<th align="right" width="75px;">
								隶属当前院系
							</th>
							<td>
								<div class="datagrid-btn-separator"></div>
							</td>
							<td>
								<input class="easyui-combobox" name="affiliation" style="width: 150px;" id="affiliation"
										data-options="
					                    url:'${pageContext.request.contextPath}/department/departmentAction!doNotNeedSecurity_getCollege2ComboBox.action',
					                    method:'post',
					                    valueField:'department_id',
					                    textField:'departmentName',
					                    prompt:'--请选择--',
				                    	icons:[{
							                iconCls:'icon-clear',
							                handler: function(e){
											$('#affiliation').combobox('clear');
							                }
							            }],
					                    editable:false
				                    	">
							</td>
							
                        	<td colspan="3">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   data-options="iconCls:'icon-search',plain:true"
                                   onclick="searchT();">查询</a>
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   data-options="iconCls:'ext-icon-huifu',plain:true"
                                   onclick="resetT();">重置</a>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
        <tr>
            <td>
                <table>
                    <tr>
                        <td>
                        	<authority:authority authorizationCode="新增班级" role="${sessionScope.user.role}">
                            <a href="javascript:void(0);" class="easyui-linkbutton"
                               data-options="iconCls:'ext-icon-note_add',plain:true"
                               onclick="addFun();">新增</a>
                            </authority:authority>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
<div data-options="region:'center',fit:true,border:false">
    <table id="eventGrid"></table>
</div>
</body>
</html>