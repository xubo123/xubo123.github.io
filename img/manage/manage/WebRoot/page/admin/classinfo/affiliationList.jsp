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
                url: '${pageContext.request.contextPath}/classInfo/classInfoAction!getAffiliationList.action',
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
					    title: '隶属当前院系',
					    field: 'affiliationName'
					},
					{
	                    title: '操作',
	                    field: 'action',
	                    width: '80',
	                    formatter: function (value, row) {
	                        var str = '';
                        	<authority:authority authorizationCode="设置隶属院系" role="${sessionScope.user.role}">
								str += '<a href="javascript:void(0)" onclick="editFun(' + row.class_id + ');"><img class="iconImg ext-icon-note_edit"/>隶属</a>&nbsp;';
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
            
            $('#affiliated').combobox('clear');
        });

        function searchT(){
			  if ($('#searchForm').form('validate')) {
				  $('#eventGrid').datagrid('load',serializeObject($('#searchForm')));
			  }
		}
		
		/**--重置--**/
		function resetT(){				
			$('#searchForm')[0].reset();
			$('#affiliated').combobox('clear');
		}
		
        var editFun = function (id) {
            var dialog = parent.WidescreenModalDialog({
                title: '设置隶属院系',
                iconCls: 'ext-icon-note_edit',
                url: '${pageContext.request.contextPath}/page/admin/classinfo/editAffiliation.jsp?id=' + id,
                buttons: [{
                    text: '保存',
                    iconCls: 'ext-icon-save',
                    handler: function () {
                        dialog.find('iframe').get(0).contentWindow.submitForm(dialog, eventGrid, parent.$);
                    }
                }]
            });
        };

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
								隶属状态
							</th>
							<td>
								<div class="datagrid-btn-separator"></div>
							</td>
							<td>
								<select name="affiliated" id="affiliated" class="easyui-combobox" style="width: 155px;" 
										data-options="  
											prompt:'--请选择--',
						                    icons:[{
								                iconCls:'icon-clear',
								                handler: function(e){
												$('#affiliated').combobox('clear');
								                }
								            }],  
											editable:false
										">
			                        <option value="-1">未隶属</option>
			                        <option value="1">已隶属</option>
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
    </table>
</div>
<div data-options="region:'center',fit:true,border:false">
    <table id="eventGrid"></table>
</div>
</body>
</html>