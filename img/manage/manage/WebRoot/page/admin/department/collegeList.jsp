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
                url: '${pageContext.request.contextPath}/department/departmentAction!getCollegeList.action',
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
					    title: '院系名称',
					    field: 'departmentName'
					},
					{
	                    title: '操作',
	                    field: 'action',
	                    width: '80',
	                    formatter: function (value, row) {
	                        var str = '';
                        	<authority:authority authorizationCode="编辑当前院系" role="${sessionScope.user.role}">
								str += '<a href="javascript:void(0)" onclick="editFun(' + row.department_id + ');"><img class="iconImg ext-icon-note_edit"/>编辑</a>&nbsp;';
							</authority:authority>          
                        	<authority:authority authorizationCode="删除当前院系" role="${sessionScope.user.role}">
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
        });

        function searchT(){
			  if ($('#searchForm').form('validate')) {
				  $('#eventGrid').datagrid('load',serializeObject($('#searchForm')));
			  }
		}
		
		/**--重置--**/
		function resetT(){				
			$('#searchForm')[0].reset();
		}
		
		var addFun = function () {
            var dialog = parent.WidescreenModalDialog({
                title: '新增当前院系',
                iconCls: 'ext-icon-note_add',
                url: '${pageContext.request.contextPath}/page/admin/department/editCollege.jsp?id=0',
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
                title: '编辑当前院系',
                iconCls: 'ext-icon-note_edit',
                url: '${pageContext.request.contextPath}/page/admin/department/editCollege.jsp?id=' + id,
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
        	$.messager.confirm('确认', '确定删除该当前院系吗？', function(r) {
				if (r) {
					$.ajax({
						url : '${pageContext.request.contextPath}/department/departmentAction!deleteCollege.action',
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
								院系名称
							</th>
							<td>
								<div class="datagrid-btn-separator"></div>
							</td>
							<td >
								<input id="name" name="name" style="width: 155px;"/>
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
                        	<authority:authority authorizationCode="新增当前院系" role="${sessionScope.user.role}">
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