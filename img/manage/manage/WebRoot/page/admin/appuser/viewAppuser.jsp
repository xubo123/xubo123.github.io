<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String id = request.getParameter("id");
%>

<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">

		<title></title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<jsp:include page="../../../inc.jsp"></jsp:include>
		<script type="text/javascript">
		var id = "<%=id%>";
		$(function() {
				$.ajax({
					url : '${pageContext.request.contextPath}/appuser/appUserAction!getById.action',
					data : "id="+id,
					dataType : 'json',
					success : function(result) {
						$("#user_id").val(result.user_id);
						$("#user_name").val(result.user_name);
						if(result.user_sex=="0"){
							$("#user_sex").val("男");
						}else if(result.sex=="1"){
							$("#user_sex").val("女");
						}else{
							$("#user_sex").val("");
						}
						$("#user_alumni_card_id").val(result.user_alumni_card_id);
						$("#user_mobile").val(result.user_mobile);
						$("#user_email").val(result.user_email);
						$("#user_city").val(result.user_city);
						$("#user_profession").val(result.user_profession);
						$("#user_work_unit").val(result.user_work_unit);
						$("#user_avocation").val(result.user_avocation);
						//教育经历赋值
						var fullClassName = result.fullClassName;
						if(fullClassName!=null && fullClassName!=""){
							var array = fullClassName.split(",");
							var html = "";
							for(var i=0;i<array.length;i++){
								html += "<tr>";
								html += "<th>";
								html += ""+(i+1);
								html += "</th>";
								html += "<td>";
								html += array[i];
								html += "</td>";
								html += "</tr>";
							}
							$("#table").html(html);
						}
					},
					beforeSend:function(){
						parent.$.messager.progress({
							text : '数据加载中....'
						});
					},
					complete:function(){
						parent.$.messager.progress('close');
					}
				});
		});
		
		</script>
	</head>

	<body>
		<form method="post">
			<div style="text-align: center;"><b></b></div>
			<fieldset>
				<legend>
					基本信息
				</legend>
				<table class="ta001">
					<tr>
						<th>
							帐号
						</th>
						<td>
							<input id="user_id" class="easyui-validatebox" disabled="disabled" />
						</td>
						<th>
							姓名
						</th>
						<td>
							<input id="user_name" class="easyui-validatebox" disabled="disabled" />
						</td>
					</tr>
					<tr>
						<th>
							性别
						</th>
						<td>
							<input id="user_sex" class="easyui-validatebox" disabled="disabled" />
						</td>
						<th>
							校友卡号
						</th>
						<td>
							<input id="user_alumni_card_id" class="easyui-validatebox" disabled="disabled" />
						</td>
					</tr>
					<tr>
						<th>
							电话号码
						</th>
						<td>
							<input id="user_mobile" disabled="disabled" class="easyui-validatebox" />
						</td>
						<th>
							电子邮箱
						</th>
						<td>
							<input id="user_email" disabled="disabled" class="easyui-validatebox" />
							
						</td>
					</tr>
					<tr>
						<th>
							所在地
						</th>
						<td>
							<input id="user_city" class="easyui-validatebox" disabled="disabled" style="width: 150px;" />
						</td>
						<th>
							行业
						</th>
						<td>
							<input id="user_profession" class="easyui-validatebox" disabled="disabled" style="width: 150px;" />
						</td>
					</tr>
					<tr>
						<th>
							工作单位
						</th>
						<td>
							<input id="user_work_unit" disabled="disabled" class="easyui-validatebox" />
						</td>
						<th>
							兴趣爱好
						</th>
						<td>
							<input id="user_avocation" disabled="disabled" class="easyui-validatebox"/>
						</td>
					</tr>
				</table>
			</fieldset>
			<br/>
			<fieldset>
				<legend>
					学习经历
				</legend>
				<table id="table" class="ta001">
					
				</table>
			</fieldset>
		</form>
	</body>
</html>
