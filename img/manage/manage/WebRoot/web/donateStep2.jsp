<%@page import="com.hxy.system.Global"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/web/";
String deptName=Global.schoolSign;
%>

<!DOCTYPE html>
<!--[if lt IE 7 ]>
<html class="ie ie6"> <![endif]-->
<!--[if IE 7 ]>
<html class="ie ie7"> <![endif]-->
<!--[if IE 8 ]>
<html class="ie ie8"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->
<html> <!--<![endif]-->
<head>
<!-- head -->
<jsp:include page="webHead.jsp" flush="true" />
<!-- head -->
</head>
<body>
<!-- header -->
	<jsp:include page="webNavigation.jsp" flush="true">
		<jsp:param name="webNewsType.origin" value="1" />
		<jsp:param name="name" value="捐赠" />
	</jsp:include>
<!-- header -->

<div class="container">
    <div class="sixteen columns page-title">
        <div class="eight columns alpha">
            <h3><span>校友捐赠</span></h3>
        </div>
        <div class="eight columns omega">
            <nav class="breadcrumbs">
                <ul>
                    <li>当前位置:</li>
                    <li>
                        <a href="../web">首页</a>
                    </li>
                    <li>
                        <a href="../_project/_projectAction!donationIndex.action">捐赠</a>
                    </li>
                    <li>校友捐赠</li>
                </ul>
            </nav>
        </div>
        <div class="clearfix"></div>
    </div>
</div>

<div class="container">
    <div class="sixteen columns">

        <div class="ajax-contact-form">
            <div class="form">

                <div class="form-holder">
                    <div class="notification canhide"></div>

                    <form id="frm_contact" name="frm_contact" action="../alipay/alipay.jsp" method="post" onsubmit="return check();">
                        <h4 class="headline">在线支付</h4>
                        <p>欢迎您进行网上捐赠，<%=deptName%>非常感谢您的捐赠！</p>
                        <div class="field">
                            <label>订单编号</label>
                            <div class="inputs">
                                <span>${donation.orderNo}</span>
                                <input type="hidden" name="out_trade_no" id="out_trade_no" value="${donation.orderNo}">
                            </div>
                        </div>
                        <div class="field">
                            <label>捐赠人姓名</label>
                            <div class="inputs">
                                <span>${donation.x_name}</span>
                                 <input type="hidden" name="subject" id="subject" value="${donation.project.projectName}">
                            </div>
                        </div>
                        <div class="field">
                            <label>捐赠金额</label>
                            <div class="inputs">
                                <span class="prize">${donation.money}</span> 元
                                <input type="hidden" name="total_fee" id="total_fee" value="${donation.money}">
                            </div>
                        </div>
                        <div class="field">
                            <label for="payment">支付方式</label>

                            <span class="payment alipay"><input type="radio" checked value="alipay" hidden /> <img src="../web/images/alipay.png" alt="支付宝"/></span>
                        </div>

                        <div class="form-submit">
                            <button type="submit" id="submit" name="submit" style="float: left">去支付</button>
                        </div>

                    </form>

                </div>

            </div>

        </div>

    </div>
</div>

<!-- footer -->
	<jsp:include page="webBottom.jsp" flush="true">
		<jsp:param name="webNewsType.origin" value="1" />
	</jsp:include>
<!-- footer -->
<script type="text/javascript">
function check(){
	if($('#out_trade_no').val()==''||$('#subject').val()==''||$('#total_fee').val()==''){
		alert('捐赠信息有误');
		return false;
	}
}
</script>
</body>
</html>