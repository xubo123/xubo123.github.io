<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<script type="text/javascript">
	$(function() {
		$
				.ajax({
					url : '${pageContext.request.contextPath}/systemSetting/systemSettingAction!initSystemSetting.action',
					dataType : 'json',
					success : function(result) {
						if (result.systemId != undefined) {
							$('form')
									.form(
											'load',
											{
												'systemSetting.systemId' : result.systemId,
												'systemSetting.smtpHost' : result.smtpHost,
												'systemSetting.smtpPort' : result.smtpPort,
												'systemSetting.email_account' : result.email_account,
												'systemSetting.email_password' : result.email_password,
												'systemSetting.download_app_url' : result.download_app_url,
												'systemSetting.partner' : result.partner,
												'systemSetting.seller_email' : result.seller_email,
												'systemSetting.key' : result.key,
												'systemSetting.private_key' : result.private_key,
												'systemSetting.notify_url' : result.notify_url,
												'systemSetting.return_url' : result.return_url,
												'systemSetting.wap_merchant_url' : result.wap_merchant_url,
												'systemSetting.wap_return_url' : result.wap_return_url,
												'systemSetting.wap_notify_url' : result.wap_notify_url,
												'systemSetting.exter_invoke_ip' : result.exter_invoke_ip,
												'systemSetting.smsUrl' : result.smsUrl,
												'systemSetting.smsAccount' : result.smsAccount,
												'systemSetting.smsPassword' : result.smsPassword,
												'systemSetting.sendType' : result.sendType,
												'systemSetting.smsCodeTemplate' : result.smsCodeTemplate,
												'systemSetting.smsVisitTemplate' : result.smsVisitTemplate,
												'systemSetting.smsBirthdayTemplate' : result.smsBirthdayTemplate,
												'systemSetting.wap_public_key':result.wap_public_key
											});
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
	});
	function save($dialog, $grid, $pjq) {
		if ($('form').form('validate')) {
			$
					.ajax({
						url : '${pageContext.request.contextPath}/systemSetting/systemSettingAction!save.action',
						data : $('form').serialize(),
						dataType : 'json',
						success : function(result) {
							if (result.success) {
								parent.$.messager.alert('提示', result.msg,
										'info');
							} else {
								parent.$.messager.alert('提示', result.msg,
										'error');
							}
						},
						beforeSend : function() {
							parent.$.messager.progress({
								text : '数据提交中....'
							});
						},
						complete : function() {
							parent.$.messager.progress('close');
						}
					});
		}
	};
</script>
</head>

<body>
	<form method="post" class="form">
		<fieldset>
			<legend> 邮件设置 </legend>
			<table class="ta001">
				<tr>
					<th>服务器</th>
					<td><input name="systemSetting.systemId" type="hidden" /> <input
						name="systemSetting.smtpHost" class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>端口</th>
					<td><input name="systemSetting.smtpPort"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>账号</th>
					<td><input name="systemSetting.email_account"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>密码</th>
					<td><input name="systemSetting.email_password"
						class="easyui-validatebox"
						type="password" style="width: 300px;" />
					</td>
				</tr>
			</table>
		</fieldset>
		<br>
		<fieldset>
			<legend> 短信设置 </legend>
			<table class="ta001">
				<tr>
					<th>短信服务器地址</th>
					<td> <input
						name="systemSetting.smsUrl" class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>账号</th>
					<td><input name="systemSetting.smsAccount"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>密码</th>
					<td><input name="systemSetting.smsPassword"
						class="easyui-validatebox"
						type="password" style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>发送方式</th>
					<td>
						<input type="radio" name="systemSetting.sendType" value="HTTP" style="width: 20px;" checked="checked">HTTP
						<input type="radio" name="systemSetting.sendType" value="SDK" style="width: 20px;">SDK
					</td>
				</tr>
				<tr>
					<th>网关签名</th>
					<td><input name="systemSetting.download_app_url"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
			</table>
		</fieldset>
		<br>
		<fieldset>
			<legend>支付宝设置 </legend>
			<table class="ta001">
				<tr>
					<th>合作者身份ID</th>
					<td><input name="systemSetting.partner"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>收款支付宝账号</th>
					<td><input name="systemSetting.seller_email"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>MD5私钥</th>
					<td><input name="systemSetting.key"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>RSA私钥</th>
					<td>
						<textarea rows="5" cols="100" name="systemSetting.private_key"></textarea>
					</td>
				</tr>
				<tr>
					<th>无线产品公钥</th>
					<td>
						<textarea rows="5" cols="100" name="systemSetting.wap_public_key"></textarea>
					</td>
				</tr>
				<tr>
					<th>PC异步通知页面</th>
					<td><input name="systemSetting.notify_url"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>PC同步通知页面</th>
					<td><input name="systemSetting.return_url"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>WAP中断操作页面</th>
					<td><input name="systemSetting.wap_merchant_url"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>WAP同步通知页面</th>
					<td><input name="systemSetting.wap_return_url"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>WAP异步通知页面</th>
					<td><input name="systemSetting.wap_notify_url"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
				<tr>
					<th>客户端IP地址</th>
					<td><input name="systemSetting.exter_invoke_ip"
						class="easyui-validatebox"
						style="width: 300px;" />
					</td>
				</tr>
			</table>
		</fieldset>
		<br>
		<fieldset>
			<legend> 短信模板设置 </legend>
			<table class="ta001">
				<tr>
					<th>验证码短信</th>
					<td>
						<textarea rows="5" cols="100" name="systemSetting.smsCodeTemplate"></textarea>
					</td>
				</tr>
				<tr>
					<th>邀请短信</th>
					<td>
						<textarea rows="5" cols="100" name="systemSetting.smsVisitTemplate"></textarea>
					</td>
				</tr>
				<tr>
					<th>生日祝福短信</th>
					<td>
						<textarea rows="5" cols="100" name="systemSetting.smsBirthdayTemplate"></textarea>
					</td>
				</tr>
			</table>
		</fieldset>
		<br>
		<table align="center">
			<tr>
				<td><a href="javascript:void(0);" class="easyui-linkbutton"
					data-options="iconCls:'ext-icon-save',plain:true" onclick="save();">保存</a>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
