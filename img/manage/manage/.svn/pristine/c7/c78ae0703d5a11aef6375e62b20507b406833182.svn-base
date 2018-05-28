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
                url: '${pageContext.request.contextPath}/event/eventAction!getListPersonal.action',
                fit: true,
                border: false,
                fitColumns: true,
                striped: true,
                rownumbers: true,
                pagination: true,
                idField: 'id',
                columns: [[
                	{
	                    width: '150',
	                    title: '标题',
	                    field: 'title'
                	},
                	{
	                    width: '150',
	                    title: '地点',
	                    field: 'place'
                	},
                    {
                        width: '70',
                        title: '发起人',
                        field: 'appUserName',
                    },
                    {
                        width: '50',
                        title: '类别',
                        field: 'category'
                    },
                    {
                        width: '110',
                        title: '开始时间',
                        field: 'startTime'
                    },
                    {
                        width: '110',
                        title: '结束时间',
                        field: 'endTime'
                    },
                    {
                        width: '50',
                        title: '是否签到',
                        field: 'needSignIn',
                        align:'center',
                        formatter : function(value, row) {
                        	return row.needSignIn?"是":"否";
						}
                    },
                    {
                        width: '40',
                        title: '签到码',
                        field: 'signInCode'
                    },
                    {
                        width: '60',
                        title: '审核状态',
                        field: 'auditStatus',
                        formatter : function(value, row) {
                        	if(row.auditStatus == 0 ) {
                        		return "<span style='color: red;'>待审核</span>";
                        	} else if(row.auditStatus == 1 ) {
                        		return "<span style='color: green;'>通过</span>";
                        	} else if(row.auditStatus == 2 ) {
                        		return "<span style='color: black;'>不通过</span>";
                        	}
						}
                    },
                    {
                        width: '60',
                        title: '活动状态',
                        field: 'nowStatus'
                    },
                    {
                        width: '50',
                        title: '报名人数',
                        field: 'signupNum',
                        formatter: function (value, row) {
	                        var str = '0';
	                        if(row.signupNum > 0) {
	                        	str = '<a href="javascript:void(0)" onclick="signupFun(' + row.id + ');">' + row.signupNum + '</a>&nbsp;';
	                        }						
	                        return str;
                    	}
                    },
                    {
                    title: '操作',
                    field: 'action',
                    width: '60',
                    formatter: function (value, row) {
                        var str = '';
                        <authority:authority authorizationCode="查看个人活动" role="${sessionScope.user.role}">
							str += '<a href="javascript:void(0)" onclick="showFun(' + row.id + ');"><img class="iconImg ext-icon-note"/>查看</a>&nbsp;';
						</authority:authority>
						
						if(row.nowStatus != '已删除') {
							if(row.auditStatus == 0 ) {
								<authority:authority authorizationCode="审核个人活动" role="${sessionScope.user.role}">
									str += '<a href="javascript:void(0)" onclick="auditFun(' + row.id + ');"><img class="iconImg ext-icon-note_edit"/>审核</a>&nbsp;';
								</authority:authority>
							} else if (row.auditStatus == 1 ) {
								<authority:authority authorizationCode="查看活动花絮" role="${sessionScope.user.role}">
									str += '<a href="javascript:void(0)" onclick="boardFun(' + row.groupId + ');"><img class="iconImg ext-icon-activity"/>花絮</a>&nbsp;';
								</authority:authority>
							}
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
            
            $('#auditStatus').combobox('clear');
        });

		function searchEvent(){
			  if ($('#searchForm').form('validate')) {
				  $('#eventGrid').datagrid('load',serializeObject($('#searchForm')));
			  }
		}
		
		/**--重置--**/
		function resetT(){		
			$('#searchForm')[0].reset();
			$('#category').combobox('clear');
			$('#startFrom').datetimebox('setValue', '');
			$('#startTo').datetimebox('setValue', '');
			$('#endFrom').datetimebox('setValue', '');
			$('#endTo').datetimebox('setValue', '');
			$('#userInfoId').prop('value','');
			$('#auditStatus').combobox('clear');
		}

        var showFun = function (id) {
            var dialog = parent.WidescreenModalDialog({
                title: '查看个人活动',
                iconCls: 'ext-icon-note',
                url: '${pageContext.request.contextPath}/page/admin/event/viewPersonalEvent.jsp?id=' + id
            });
        };
        var boardFun = function (id) {
            var dialog = parent.WidescreenModalDialog({
                title: '查看活动花絮',
                iconCls: 'ext-icon-note',
                url: '${pageContext.request.contextPath}/page/admin/event/viewEventBoard.jsp?id=' + id
            });
        };
        var signupFun = function (id) {
            var dialog = parent.modalDialog({
                title: '查看报名人员',
                iconCls: 'ext-icon-note',
                url: '${pageContext.request.contextPath}/page/admin/event/viewSignupPeople.jsp?id=' + id
            });
        };
        
        var auditFun = function (id) {
            var dialog = parent.WidescreenModalDialog({
                title: '审核个人活动',
                iconCls: 'ext-icon-note_edit',
                url: '${pageContext.request.contextPath}/page/admin/event/auditPersonalEvent.jsp?id=' + id,
                buttons: [{
                    text: '保存',
                    iconCls: 'ext-icon-save',
                    handler: function () {
                        dialog.find('iframe').get(0).contentWindow.submitForm(dialog, eventGrid, parent.$);
                    }
                }]
            });
        };
        
        function searchFun(){
			$('#cc').combogrid('grid').datagrid('load',serializeObject($('#searchUserForm')));
		}
    </script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">
<div id="toolbar" style="display: none;">
    <table>
        <tr>
            <td>
                <form id="searchForm">
                	<input id="userInfoId" name="userInfoId" type="hidden"/>
                    <table>
                        <tr>
                            <th align="right">
								标题
							</th>
							<td>
								<div class="datagrid-btn-separator"></div>
							</td>
							<td>
								<input id="title" name="title" style="width: 155px;"/>
							</td>

                           
                            <th align="right">
								地点
							</th>
							<td>
								<div class="datagrid-btn-separator"></div>
							</td>
							<td>
								<input id="place" name="place" style="width: 155px;"/>
							</td>
							
							<th align="right">
								类别
							</th>
							<td>
								<div class="datagrid-btn-separator"></div>
							</td>
							<td>
								 <input id="category" name="category" class="easyui-combobox" style="width: 150px;" 
											data-options="  
											valueField: 'dictName',  
											textField: 'dictName',  
											editable:false,
											prompt:'--请选择--',
					                    	icons:[{
								                iconCls:'icon-clear',
								                handler: function(e){
												$('#category').combobox('clear');
								                }
								            }],
											url: '${pageContext.request.contextPath}/dicttype/dictTypeAction!doNotNeedSecurity_getDict.action?dictTypeName='+encodeURI('活动类别') 
										" />
							</td>
							
                          	<!-- 
                          	<th align="right">
								发起人
							</th>
							<td>
								<div class="datagrid-btn-separator"></div>
							</td>
							<td>
								<select name="userSel" class="easyui-combogrid" id="cc" style="width:155px;"
						        data-options="
						        	editable:false,
						            panelWidth:600,
						            idField:'userName',
						            textField:'userName',
						            pagination : true,
						            url:'${pageContext.request.contextPath}/userInfo/userInfoAction!doNotNeedSecurity_dataGridFor.action',
						            columns:[[
						                {field:'userName',title:'姓名',width:100,align:'center'},
						                {field:'fullName',title:'所属机构',width:500,align:'center'}
						            ]],
						            toolbar: $('#usertoolbar'),
						            onSelect:function(rowIndex, rowData){
						            	if(rowData.accountNum != null) {
						            		$('#userInfoId').prop('value',rowData.accountNum);	
						            	} else {
						            		$('#userInfoId').prop('value',-1);	
						            	}	            	
						            }
						        "></select>
							</td>
							 -->
							
                        </tr>
                        <tr>
                            <th align="right">
                                	开始时间
                            </th>
                            <td>
								<div class="datagrid-btn-separator"></div>
							</td>
                            <td colspan="4">
			                    <input name="startFrom" id="startFrom" class="easyui-datetimebox " 
									data-options="editable:false" style="width: 150px;" /> &nbsp;&nbsp; - &nbsp;&nbsp;
								<input name="startTo" id="startTo" class="easyui-datetimebox " 
									data-options="editable:false" style="width: 150px;" />
                            </td>
                            
                            <th align="right">
                                	结束时间
                            </th>
                            <td>
								<div class="datagrid-btn-separator"></div>
							</td>
                            <td colspan="4">
			                    <input name="endFrom" id="endFrom" class="easyui-datetimebox " 
									data-options="editable:false" style="width: 150px;" /> &nbsp;&nbsp; - &nbsp;&nbsp;
								<input name="endTo" id="endTo" class="easyui-datetimebox " 
									data-options="editable:false" style="width: 150px;" />
                            </td>
                            
                        </tr>
                        <tr>
                        	
							
                            <th align="right">
                                	审核状态
                            </th>
                            <td>
								<div class="datagrid-btn-separator"></div>
							</td>
                            <td>
			                    <select class="easyui-combobox" name="auditStatus" id="auditStatus" style="width: 150px;"
				                    data-options="  
										prompt:'--请选择--',
					                    icons:[{
							                iconCls:'icon-clear',
							                handler: function(e){
											$('#auditStatus').combobox('clear');
							                }
							            }],  
										editable:false
									">
									<option value="0">待审核</option>
									<option value="1">通过</option>
									<option value="2">不通过</option>
								</select>
                            </td>
                            
                            

                           	<td>
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   data-options="iconCls:'icon-search',plain:true"
                                   onclick="searchEvent();">查询</a>
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
<div id="usertoolbar" style="display: none;">
	<table>
		<tr>
			<td>
				<form id="searchUserForm">
					<table>
						<tr>
							<th>
								姓名：
							</th>
							<td>
								<input name="userInfo.userName" style="width: 150px;" />
							</td>
							<th>
								电话号码：
							</th>
							<td>
								<input name="userInfo.telId" style="width: 150px;" />
							</td>
							<td>
							<a href="javascript:void(0);" class="easyui-linkbutton"
								data-options="iconCls:'ext-icon-zoom',plain:true"
								onclick="searchFun();">查询</a>
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
</div>
</body>
</html>
