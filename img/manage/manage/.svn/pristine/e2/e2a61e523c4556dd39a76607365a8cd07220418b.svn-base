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
                url: '${pageContext.request.contextPath}/community/communityAction!getTopicList.action',
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
					    title: '社区',
					    field: 'board_name'
					},
                    {
                        width: '70',
                        title: '作者',
                        field: 'appUserName'
                    },
                    {
                        width: '300',
                        title: '内容',
                        field: 'context',
                        formatter : function(value, row) {
							if(row.first_post!=undefined){
								return row.first_post.context;
							}else{
								return "";
							}
						}   
                    },
                    {
                        width: '50',
                        title: '图片',
                        align: 'center',
                        field: 'pic',
                        formatter: function (value, row) {
	                        var str = '';
							str += '<a href="javascript:void(0)" onclick="showPicFun(' + row.first_post_id + ');">查看</a>&nbsp;';
	                        return str;
                    	}  
                    },
                    {
                        width: '110',
                        title: '时间',
                        field: 'created_date'                        
                    },
                    {
                        width: '40',
                        title: '评论',
                        align: 'center',
                        field: 'commentNum',
                        formatter: function (value, row) {
	                        var str = '0';
	                        if(row.commentNum > 0) {
	                        	str = '<a href="javascript:void(0)" onclick="commentFun(' + row.id + ');">' + row.commentNum + '</a>&nbsp;';
	                        }						
	                        return str;
                    	}                       
                    },
                    {
                        width: '40',
                        title: '点赞',
                        align: 'center',
                        field: 'praiseNum'                        
                    },
                    {
                        width: '40',
                        title: '投诉',
                        align: 'center',      
                        field: 'complaintNum',
                        formatter : function(value, row) {
  							if(row.first_post!=undefined){
  								return row.first_post.complaintNum;
  							}else{
  								return "";
  							}
  						}   
                    },
                    {
						width : '100',
						title : '状态',
						field : 'status',
						formatter : function(value, row)
						{
							if(row.first_post!=undefined) {
								if(row.first_post.status == '0') {
									return '正常';
								} else if(row.first_post.status == '1') {
									return '投诉处理-正常';
								} else if(row.first_post.status == '2') {
									return '投诉处理-违规';
								} else if(row.first_post.status == '3') {
									return '用户已删除';
								} else if(row.first_post.status == '4') {
									return '管理员已删除';
								}
							}
						}
					},
					{
	                    title: '操作',
	                    field: 'action',
	                    width: '80',
	                    formatter: function (value, row) {
	                        var str = '';
	                        if(row.first_post!=undefined) {
		                        if(row.first_post.status == '0' || row.first_post.status == '1' || row.first_post.status == '2') {
		                        	<authority:authority authorizationCode="处理社区主题" role="${sessionScope.user.role}">
										str += '<a href="javascript:void(0)" onclick="del(' + row.first_post_id + ');"><img class="iconImg ext-icon-note_delete"/>删除</a>&nbsp;';
									</authority:authority>
		                        } else if(row.first_post.status == '4'){
		                        	<authority:authority authorizationCode="处理社区主题" role="${sessionScope.user.role}">
										str += '<a href="javascript:void(0)" onclick="undoDelete(' + row.first_post_id + ');"><img class="iconImg ext-icon-export_customer"/>恢复</a>&nbsp;';
									</authority:authority>
		                        }
	                        }
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
            
            $('#status').combobox('clear');
            
            if(${sessionScope.user.role.systemAdmin} != 1) {
    			$(".queryboard").hide();
    		}
        });

        function searchTopic(){
			  if ($('#searchForm').form('validate')) {
				  $('#eventGrid').datagrid('load',serializeObject($('#searchForm')));
			  }
		}
		
		/**--重置--**/
		function resetT(){				
			$('#searchForm')[0].reset();
			$('#boardId').combobox('clear');
			$('#status').combobox('clear');
		}
		
		var showPicFun = function (id) {
            var dialog = parent.modalDialog({
                title: '查看图片',
                iconCls: 'ext-icon-note',
                url: '${pageContext.request.contextPath}/page/admin/community/viewContextPic.jsp?id=' + id
            });
        };
        
		var commentFun = function (id) {
            var dialog = parent.modalDialog({
                title: '查看评论',
                iconCls: 'ext-icon-note',
                url: '${pageContext.request.contextPath}/page/admin/community/viewContextComment.jsp?id=' + id
            });
        };
        
        function del (boardId){
        	$.messager.confirm('确认', '确定删除该主题吗？', function(r) {
				if (r) {
					handleEventBoard(boardId,4);
				}
			});
        }
        function undoDelete (boardId){
        	$.messager.confirm('确认', '确定恢复该主题吗？恢复后主题为正常状态。', function(r) {
				if (r) {
					handleEventBoard(boardId,0);
				}
			});
        }
        
        function handleEventBoard(contextId, status){			
			$.ajax({
				url : '${pageContext.request.contextPath}/community/communityAction!handleTopic.action',
				data : {
					id : contextId,
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
                            <th align="right" class="queryboard">
								社区
							</th>
							<td class="queryboard">
								<div class="datagrid-btn-separator"></div>
							</td>
							<td class="queryboard">
								<input class="easyui-combobox" name="boardId" id="boardId" style="width: 150px;"
											data-options="
						                    url:'${pageContext.request.contextPath}/community/communityAction!doNotNeedSecurity_getAllGroupBoard.action',
						                    method:'post',
						                    valueField:'id',
						                    textField:'board_name',
						                    prompt:'--请选择--',
					                    	icons:[{
								                iconCls:'icon-clear',
								                handler: function(e){
												$('#boardId').combobox('clear');
								                }
								            }],
						                    editable:false
					                    	">
							</td>
							                         
                            <th align="right">
								状态
							</th>
							<td>
								<div class="datagrid-btn-separator"></div>
							</td>
							<td>
								<select id="status" name="status" class="easyui-combobox" style="width: 155px;" 
									data-options="  
										prompt:'--请选择--',
					                    icons:[{
							                iconCls:'icon-clear',
							                handler: function(e){
											$('#status').combobox('clear');
							                }
							            }],  
										editable:false
									">
			                        <option value="0">正常</option>
			                        <option value="1,2">投诉已处理</option>
			                        <option value="3,4">已删除</option>
			                    </select>
							</td>
							<td>
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   data-options="iconCls:'icon-search',plain:true"
                                   onclick="searchTopic();">查询</a>
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