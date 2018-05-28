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
                url: '${pageContext.request.contextPath}/community/communityAction!getGroupBoardList.action',
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
					    title: '社区',
					    field: 'board_name'
					},
                    {
                        width: '100',
                        title: '成员数量',
                        align: 'center',
                        field: 'memberNum'
                    },
					{
	                    title: '操作',
	                    field: 'action',
	                    width: '80',
	                    formatter: function (value, row) {
	                        var str = '';
	                        if(row.memberNum > 0) {
	                        	<authority:authority authorizationCode="查看社区成员" role="${sessionScope.user.role}">
									str += '<a href="javascript:void(0)" onclick="viewMember(' + row.id + ');"><img class="iconImg ext-icon-note"/>查看</a>&nbsp;';
								</authority:authority>  
	                    	}
	                        return str;
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

		
		var viewMember = function (id) {
            var dialog = parent.WidescreenModalDialog({
                title: '查看社区成员',
                iconCls: 'ext-icon-note',
                url: '${pageContext.request.contextPath}/page/admin/community/viewMember.jsp?boardId=' + id
            });
        };
        
		
    </script>
</head>

<body class="easyui-layout" data-options="fit:true,border:false">


<div data-options="region:'center',fit:true,border:false">
    <table id="eventGrid"></table>
</div>
</body>
</html>