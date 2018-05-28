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
                url: '${pageContext.request.contextPath}/event/eventAction!getSignupPeople.action?id='+$('#eventId').val(),
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
                        title: '姓名',
                        field: 'user_name'                        
                    },
                    {
                        width: '40',
                        title: '性别',
                        field: 'user_sex',
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
                        width: '80',
                        title: '电话',
                        field: 'user_mobile'                        
                    },
                    {
                        width: '120',
                        title: '电子邮箱',
                        field: 'user_email'                        
                    },
                    {
						width : '340',
						title : '学习经历',
						field : 'allClassName',
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

<input name="event.id" type="hidden" id="eventId" value="${param.id}">

<div data-options="region:'center',fit:true,border:false">
    <table id="eventGrid"></table>
</div>
</body>
</html>