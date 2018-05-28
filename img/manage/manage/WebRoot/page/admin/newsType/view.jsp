<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hxy.util.WebUtil"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<jsp:include page="../../../inc.jsp"></jsp:include>
<script channel="text/javascript">
	var editor;
	KindEditor
			.ready(function(K) {
				editor = K
						.create(
								'#newscontent',
								{
									fontSizeTable : [ '9px', '10px', '11px',
											'12px', '13px', '14px', '15px',
											'16px', '17px', '18px', '19px',
											'20px', '22px', '24px', '28px',
											'32px' ],
									uploadJson : '${pageContext.request.contextPath}/fileUpload/fileUploadAction!doNotNeedSecurity_fileUploadK.action',
									readonlyMode : true,
									afterChange : function() {
										this.sync();
									}
								});
			});

	$(function() {
		if ($('#channelId').val() > 0) {
			$
					.ajax({
						url : '${pageContext.request.contextPath}/mobNewsType/mobNewsTypeAction!getNewsType.action',
						data : $('form').serialize(),
						dataType : 'json',
						success : function(result) {
							if (result.id != undefined) {

								$('form')
										.form(
												'load',
												{
													'channel.id' : result.id,
													'channel.channel_pid' : result.channel_pid,
													'channel.channel_name' : result.channel_name,
													'channel.channel_url' : result.channel_url,
													'channel.channel_type' : result.channel_type,
													'channel.channel_ordernum' : result.channel_ordernum,

												});
								if (result.channel_type == 1) {
									$("#trUrl").css("display", "none");
									$("#url").val("");
								} else if (result.channel_type == 2) {
									$("#trUrl").css("display",
											result.channel_url);
								}
								/*   if(result.channel_pid == 0) {
									$("#level").val('一级栏目');
								} else if(result.channel_pid > 0) {
									$("#level").val('二级栏目');
								} */
							}
						},
						beforeSend : function() {
							parent.$.messager.progress({
								text : '数据加载中....'
							});
						},
						complete : function() {

							parent.$.messager.progress('close');

						}
					});
		}

	});
</script>
</head>

<body>
	<form method="post" id="addNewsForm">
		<input name="channel.id" type="hidden" id="channelId"
			value="${param.id}">
		<fieldset>
			<legend> 栏目信息 </legend>
			<table class="ta001">
				<tr>
					<th>栏目名称</th>
					<td colspan="3"><input id="name" name="channel.channel_name"
						class="easyui-validatebox" style="width: 700px;"
						data-options="required:true,validType:'customRequired'"
						maxlength="30" disabled="disabled" /></td>
				</tr>
				<!-- 	<tr>
				<th>
					栏目级别
				</th>
				<td colspan="3" >
				<input id="level" style="width: 150px;"  type = "text"
						maxlength="30" disabled="disabled" >
				</td>
			</tr> -->
				<tr>
					<th>栏目类型</th>
					<td colspan="3"><select id="type" name="channel.channel_type"
						onchange="selectType();" disabled="disabled">
							<option value="1">新闻类别</option>
							<option value="2">链接</option>
					</select></td>
				</tr>
				<tr id="trUrl">
					<th>URL(可为空)</th>
					<td colspan="3"><input id="url" style="width: 700px;"
						name="channel.channel_url" type="text" disabled="disabled">
					</td>
				</tr>
				<!--	
			<tr id="trNewsTitle" style="display:none;">
				<th>
					新闻标题
				</th>
				<td colspan="3">
					<input id="newstitle" style="width: 700px;" name="channel.newsTitle" channel="text" value="" disabled="disabled">
				</td>
			</tr>
			<tr id="trNewsContent" style="display:none;">
				<th>
					新闻内容
				</th>
				<td colspan="3">
					<textarea id="newscontent" name="channel.newsContent"
						style="width: 700px; height: 300px;"></textarea>
				</td>
			</tr>
			
			<tr id="trOrigin" style="display:none;">
				<th>
					新闻来源
				</th>
				<td colspan="3">
					<select id="origin" name="channel.origin" disabled="disabled">
						<option value="1">总会</option>
						<option value="2">地方</option>
					</select>
				</td>
			</tr>

		 	<tr id="trArea" style="display:none;">
				<th>
					地域
				</th>
				<td colspan="3">
					<input name="channel.cityName" style="width: 500px;" disabled="disabled" />
				</td>
			</tr>
			
		 	<tr>
				<th>
					导航显示
				</th>
				<td colspan="3">
					<select id="isNavigation" name="channel.isNavigation" disabled="disabled">
						<option value="0">否</option>
						<option value="1">是</option>
					</select>
				</td>
			</tr>-->
				<tr>
					<th>排序编号</th>
					<td colspan="3"><input id="orderNum"
						name="channel.channel_ordernum" class="easyui-validatebox"
						style="width: 150px;" data-options="required:true,validType:'tel'"
						maxlength="20" value="" disabled="disabled" />&nbsp;&nbsp;&nbsp;&nbsp;(
						数字越小越靠前)</td>
				</tr>

				<!--<tr>
				<th>
					栏目地址
				</th>
				<td colspan="3">
					<input id="address" class="easyui-validatebox"
						style="width: 700px;" disabled="disabled"/>
				</td>
			</tr>-->
			</table>
		</fieldset>
	</form>
</body>
</html>