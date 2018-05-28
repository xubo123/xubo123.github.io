<%@page import="com.hxy.alipay.util.AlipaySubmit"%>
<%@page import="com.hxy.alipay.config.AlipayConfig"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<%
	String service = "create_direct_pay_by_user";

	String partner = AlipayConfig.partner;

	String _input_charset = AlipayConfig._input_charset;

	String notify_url = AlipayConfig.notify_url;

	String return_url = AlipayConfig.return_url;

	//订单编号
	String out_trade_no = request.getParameter("out_trade_no");

	//订单名称
	String subject = request.getParameter("subject");

	//支付类型,4为捐赠，1为商品购买
	String payment_type = "4";

	//付款金额
	String total_fee = request.getParameter("total_fee");

	String seller_id = AlipayConfig.seller_email;
	
	String anti_phishing_key = AlipaySubmit.query_timestamp();
	//若要使用请调用类文件submit中的query_timestamp函数

	//客户端的IP地址
	String exter_invoke_ip = AlipayConfig.exter_invoke_ip;

	Map<String, String> sParaTemp = new HashMap<String, String>();
	sParaTemp.put("service", "create_direct_pay_by_user");
	sParaTemp.put("partner", partner);
	sParaTemp.put("_input_charset", _input_charset);
	sParaTemp.put("notify_url", notify_url);
	sParaTemp.put("return_url", return_url);
	sParaTemp.put("out_trade_no", out_trade_no);
	sParaTemp.put("subject", subject);
	sParaTemp.put("payment_type", payment_type);
	sParaTemp.put("total_fee", total_fee);
	sParaTemp.put("seller_email",seller_id);
	sParaTemp.put("anti_phishing_key", anti_phishing_key);
	sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
	//建立请求
	String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
	out.println(sHtmlText);
%>
</head>

<body>
</body>
</html>
