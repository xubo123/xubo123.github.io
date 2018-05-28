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
                url: '${pageContext.request.contextPath}/event/eventAction!getComplaintList.action',
                nowrap: false,
                fit: true,
                border: false,
                fitColumns: true,
                striped: true,
                rownumbers: true,
                pagination: true,
                singleSelect: true,
                idField: 'id',
                columns: [[
                	{
	                    width: '80',
	                    title: '发表人',
	                    field: 'appUserName'
                	},
                	{
	                    width: '480',
	                    title: '发表内容',
	                    field: 'context'
                	},
                	{
                        width: '50',
                        title: '发表图片',
                        align: 'center',
                        field: 'pic',
                        formatter: function (value, row) {
	                        var str = '';
							str += '<a href="javascript:void(0)" onclick="showPicFun(' + row.id + ');">查看</a>&nbsp;';
	                        return str;
                    	}  
                    },
                    {
                        width: '110',
                        title: '发表时间',
                        field: 'created_date'
                    },
                    {
                        width: '60',
                        title: '投诉',
                        align: 'center',
                        field: 'complaintNum',
                        formatter: function (value, row) {
	                        var str = '0';
	                        if(row.complaintNum > 0) {
	                        	str = '<a href="javascript:void(0)" onclick="showFun(' + row.id + ');">' + row.complaintNum + '</a>&nbsp;';
	                        }						
	                        return str;
                    	}
                    },                    
                    {
                    title: '处理',
                    field: 'action',
                    width: '100',
                    formatter: function (value, row) {
                        var str = '';
                        if(row.status == 0) {
                        	<authority:authority authorizationCode="处理活动投诉" role="${sessionScope.user.role}">
								str += '<a href="javascript:void(0)" onclick="handleYes(' + row.id + ');"><img class="iconImg ext-icon-yes"/>内容正常</a>&nbsp;';
								str += '<a href="javascript:void(0)" onclick="handleNo(' + row.id + ');"><img class="iconImg ext-icon-recyle"/>内容违规</a>&nbsp;';
							</authority:authority>
                        } else if(row.status == 1) {
                        	str += '投诉已处理 - 内容正常&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
                        	<authority:authority authorizationCode="处理活动投诉" role="${sessionScope.user.role}">
								str += '<a href="javascript:void(0)" onclick="handleUndo(' + row.id + ');"><img class="iconImg ext-icon-export_customer"/>撤销处理</a>&nbsp;';
							</authority:authority>
                        } else if(row.status == 2) {
                        	str += '投诉已处理 - 内容违规&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
                        	<authority:authority authorizationCode="处理活动投诉" role="${sessionScope.user.role}">
								str += '<a href="javascript:void(0)" onclick="handleUndo(' + row.id + ');"><img class="iconImg ext-icon-export_customer"/>撤销处理</a>&nbsp;';
							</authority:authority>
                        }
                        
                        return str;
                    }
                }]],
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

		function searchEvent(){
			  if ($('#searchForm').form('validate')) {
				  $('#eventGrid').datagrid('load',serializeObject($('#searchForm')));
			  }
		}
		
		var showPicFun = function (id) {
            var dialog = parent.modalDialog({
                title: '查看图片',
                iconCls: 'ext-icon-note',
                url: '${pageContext.request.contextPath}/page/admin/event/viewEventBoardPic.jsp?id=' + id
            });
        };
        
        var showFun = function (id) {
            var dialog = parent.modalDialog({
                title: '查看投诉',
                iconCls: 'ext-icon-note',
                url: '${pageContext.request.contextPath}/page/admin/event/viewEventBoardComplaint.jsp?id=' + id
            });
        };
        
        var showOfficalEventFun = function (id) {
            var dialog = parent.WidescreenModalDialog({
                title: '查看官方活动',
                iconCls: 'ext-icon-note',
                url: '${pageContext.request.contextPath}/page/admin/event/viewOfficialEvent.jsp?id=' + id
            });
        };
        
        var showAlumniEventFun = function (id) {
            var dialog = parent.WidescreenModalDialog({
                title: '查看校友活动',
                iconCls: 'ext-icon-note',
                url: '${pageContext.request.contextPath}/page/admin/event/viewAlumniEvent.jsp?id=' + id
            });
        };
        
        var showPersonEventFun = function (id) {
            var dialog = parent.WidescreenModalDialog({
                title: '查看个人活动',
                iconCls: 'ext-icon-note',
                url: '${pageContext.request.contextPath}/page/admin/event/viewPersonalEvent.jsp?id=' + id
            });
        };
        
        function handleYes (boardId){
        	$.messager.confirm('确认', '确定该内容正常吗？', function(r) {
				if (r) {
					handleComplaint(boardId,1);
				}
			});
        }
        function handleNo (boardId){
        	$.messager.confirm('确认', '确定该内容违规吗？', function(r) {
				if (r) {
					handleComplaint(boardId,2);
				}
			});
        }
        function handleUndo (boardId){
        	$.messager.confirm('确认', '确定撤销处理吗？撤销后内容恢复为投诉未处理状态。', function(r) {
				if (r) {
					handleComplaint(boardId,0);
				}
			});
        }
        
        function handleComplaint(boardId, status){			
			$.ajax({
				url : '${pageContext.request.contextPath}/event/eventAction!handleComplaint.action',
				data : {
					id : boardId,
					handleStatus: status
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
								处理状态
							</th>
							<td>
								<div class="datagrid-btn-separator"></div>
							</td>
							<td>
								<select name="status" class="easyui-combobox" style="width: 155px;" data-options="editable:false">
			                        <option value="0">未处理</option>
			                        <option value="1">已处理-内容正常</option>
			                        <option value="2">已处理-内容违规</option>
			                    </select>
							</td>
							<td>
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   data-options="iconCls:'icon-search',plain:true"
                                   onclick="searchEvent();">查询</a>
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
