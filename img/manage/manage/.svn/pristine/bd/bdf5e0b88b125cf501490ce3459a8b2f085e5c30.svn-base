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
                url: '${pageContext.request.contextPath}/community/communityAction!getMemberList.action?boardId='+$('#boardId').val(),
                fit: true,
                border: false,
                fitColumns: true,
                striped: true,
                rownumbers: true,
                pagination: true,
                singleSelect: true,
                columns: [[
					{
						width : '80',
						title : '姓名',
						field : 'user_name',
						align : 'center'
					},
					{
						width : '50',
						title : '性别',
						field : 'user_sex',
						align : 'center',
						formatter : function(value, row) {
					    	if(value == 0) {
					    		return "男";
					    	} else if(value == 1) {
					    		return "女";
					    	} else {
					    		return "";
					    	}
						}  
					},
					{
						width : '60',
						title : '所在地',
						field : 'user_city',
						align : 'center'
					},
					{
						width : '60',
						title : '行业',
						field : 'user_profession',
						align : 'center'
					},
					{
						width : '100',
						title : '工作单位',
						field : 'work_unit',
						align : 'center'
					},
					{
						width : '100',
						title : '电话号码',
						field : 'user_mobile',
						align : 'center'
					},
					{
						width : '120',
						title : '电子邮箱',
						field : 'user_email',
						align : 'center'
					},
					{
						width : '300',
						title : '学习经历',
						field : 'fullClassName',
						align : 'center',
						formatter : function(value, row)
						{
							var text='';
							if(value != null) {
								var array = value.split(',');
								for(var i=0;i<array.length;i++){
									if(i==array.length-1){
										text+=array[i];
									}
									else{
										text+=array[i]+ "<br />";
									}
								}
							}
							return text;
						}
					}
                    ]],
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

		
        
    </script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">

<input name="board.id" type="hidden" id="boardId" value="${param.boardId}">

<div data-options="region:'center',fit:true,border:false">
    <table id="eventGrid"></table>
</div>
</body>
</html>