<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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

                    <form id="frm_contact" name="frm_contact" action="<%=path%>/_donation/_donationAction!donateQuery.action" method="post">
                        <h4 class="headline">捐赠查询</h4>
                        <p>本功能为查询您的捐赠记录，请填写您捐赠时填写的姓名和电话进行查询。</p>
                        <div class="field">
                            <label for="name">姓名<span class="required">*</span></label>

                            <div class="inputs">
                                <input class="aweform" type="text" id="name" name="donation.x_name" style="width: 200px;margin-right: 10px;" datatype="*" sucmsg=" "/>
                            </div>
                        </div>
                        <div class="field">
                            <label for="phone">联系电话 <span class="required">*</span></label>

                            <div class="inputs">
                                <input class="aweform small" type="text" id="phone" name="donation.x_phone" datatype="*" sucmsg=" "/>
                            </div>
                        </div>

                        <div class="form-submit">
                            <button type="submit" id="btn_sub" name="submit" style="float: left">查询</button>
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
var valid=$("#frm_contact").Validform({
	btnSubmit:"#btn_sub", 
	tiptype:4, 
	label:".label",
	showAllError:false,
	postonce:true,
	datatype:{
		"phone":/^(13[0-9]|14[5|7]|15[0-9]|18[0-9]|17[0-9])\d{8}$/i
	}
});
valid.addRule([
{
    ele:"#phone",
    datatype:"phone",
    nullmsg:"请输入联系方式！",
    sucmsg :" ",
    errormsg:"请输入正确的手机号码！"
}
	               ])
</script>
</body>
</html>