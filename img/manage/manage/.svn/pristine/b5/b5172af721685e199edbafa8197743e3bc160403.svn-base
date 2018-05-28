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
                url: '${pageContext.request.contextPath}/community/communityAction!getContextComplaint.action?id='+$('#contextId').val(),
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
                        width: '70',
                        title: '投诉人',
                        field: 'appUserName'
                    },
                    {
                        width: '450',
                        title: '投诉内容',
                        field: 'reason'
                    },
                    {
                        width: '110',
                        title: '投诉时间',
                        field: 'created_date'                        
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

<input name="context.id" type="hidden" id="contextId" value="${param.id}">

<div data-options="region:'center',fit:true,border:false">
    <table id="eventGrid"></table>
</div>
</body>
</html>