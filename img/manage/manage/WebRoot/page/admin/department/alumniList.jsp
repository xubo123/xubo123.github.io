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
                url: '${pageContext.request.contextPath}/department/departmentAction!getAlumniList.action',
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
					    width: '200',
					    title: '组织名称',
					    field: 'departmentName'
					},
					{
					    width: '150',
					    title: '组织类别',
					    field: 'type',
					    formatter: function (value, row) {
	                        if(value==2) {
	                        	return "地方校友会";
	                        } else if(value==3) {
	                        	return "行业校友会";
	                        } else if(value==4) {
	                        	return "企业校友会";
	                        } else if(value==5) {
	                        	return "兴趣校友会";
	                        } else if(value==6) {
	                        	return "其他";
	                        }
	                        return "";
	                    }
					},
					{
	                    title: '操作',
	                    field: 'action',
	                    width: '80',
	                    formatter: function (value, row) {
	                        var str = '';
                        	<authority:authority authorizationCode="编辑校友组织" role="${sessionScope.user.role}">
								str += '<a href="javascript:void(0)" onclick="editFun(' + row.department_id + ');"><img class="iconImg ext-icon-note_edit"/>编辑</a>&nbsp;';
							</authority:authority>          
                        	<authority:authority authorizationCode="删除校友组织" role="${sessionScope.user.role}">
								str += '<a href="javascript:void(0)" onclick="doDel(' + row.department_id + ');"><img class="iconImg ext-icon-note_delete"/>删除</a>&nbsp;';
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
            
            $('#type').combobox('clear');
        });

        function searchT(){
			  if ($('#searchForm').form('validate')) {
				  $('#eventGrid').datagrid('load',serializeObject($('#searchForm')));
			  }
		}
		
		/**--重置--**/
		function resetT(){				
			$('#searchForm')[0].reset();
			$('#type').combobox('clear');
		}
		
		var addFun = function () {
            var dialog = parent.WidescreenModalDialog({
                title: '新增校友组织',
                iconCls: 'ext-icon-note_add',
                url: '${pageContext.request.contextPath}/page/admin/department/editAlumni.jsp?id=0',
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
                title: '编辑校友组织',
                iconCls: 'ext-icon-note_edit',
                url: '${pageContext.request.contextPath}/page/admin/department/editAlumni.jsp?id=' + id,
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
        	$.messager.confirm('确认', '确定删除该校友组织吗？', function(r) {
				if (r) {
					$.ajax({
						url : '${pageContext.request.contextPath}/department/departmentAction!deleteAlumni.action',
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
                            <th align="right">
								组织名称
							</th>
							<td>
								<div class="datagrid-btn-separator"></div>
							</td>
							<td >
								<input id="name" name="name" style="width: 155px;"/>
							</td>  
							
							<th align="right">
								组织类别
							</th>
							<td>
								<div class="datagrid-btn-separator"></div>
							</td>
							<td >
								<select class="easyui-combobox" name="type" id="type" style="width: 155px;"
								data-options="  
									prompt:'--请选择--',
				                    icons:[{
						                iconCls:'icon-clear',
						                handler: function(e){
										$('#type').combobox('clear');
						                }
						            }],  
									editable:false
								">
									<option value="2">地方校友会</option>
									<option value="3">行业校友会</option>
									<option value="4">企业校友会</option>
									<option value="5">兴趣校友会</option>
									<option value="6">其他</option>
								</select>
							</td>                    
                            
							<td>
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
                        	<authority:authority authorizationCode="新增校友组织" role="${sessionScope.user.role}">
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