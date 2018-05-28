<%@page import="com.hxy.system.Global"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
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
<style>
	#loading{width:20px;height:20px;overflow:hidden;background:url(<%=basePath%>images/loading1.gif) no-repeat;z-index:10;display:none;float: right;position: relative;left: -195px;}
</style>
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
                        <a href="./_projectAction!donationIndex.action">捐赠</a>
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

                    <form id="frm_contact" name="frm_contact" action="<%=path%>/_donation/_donationAction!doNotNeedSessionAndSecurity_donationSave.action" method="post">
                        <h4 class="headline">捐赠信息</h4>
                        <p>欢迎您进行网上捐赠，<%=deptName%>非常感谢您的捐赠！</p>
                        <div class="field">
                            <label for="donateNum">捐赠金额 <span class="required">*</span></label>

                            <div class="inputs">
                                <input class="aweform" type="text" id="money" name="donation.money"/>
                            </div>
                        </div>
                        <div class="field">
                            <label for="subject">捐赠项目</label>

                            <div class="inputs">
                                <select class="aweform" id="projectId" name="donation.projectId" datatype="*" sucmsg=" ">
	                                <c:if test="${projects!=null}">
	                                	<c:forEach items="${projects}" var="project">
	                                		<c:choose>
		                                		<c:when test="${project.projectId==id}">
			                                		<option value="${project.projectId}" selected="selected">${project.projectName}</option>
		                                		</c:when>
		                                		<c:otherwise>
			                                		<option value="${project.projectId}">${project.projectName}</option>
		                                		</c:otherwise>
	                                		</c:choose>
	                                	</c:forEach>
	                                </c:if>
                                </select>
                            </div>
                        </div>
                        <div class="field">
                            <label for="message">捐赠留言 <span class="required">*</span></label>

                            <div class="inputs">
                                <textarea class="aweform" id="message" name="donation.message" rows="30" cols="5" datatype="*" sucmsg=" "></textarea>
                            </div>
                        </div>
                        <div class="field">
                            <label for="remark">备注 </label>

                            <div class="inputs">
                                <input class="aweform" type="text" id="remark" name="donation.remark"/>
                            </div>
                        </div>
                        <h4 class="headline">个人信息</h4>
                        <p>请如实填写您的信息，您的信息也将作为将来查询的依据。</p>
                        <div class="field">
                            <label for="name">姓名<span class="required">*</span></label>
                            <div class="inputs">
                                <input class="aweform" type="text" id="name" name="donation.x_name" style="width: 200px;margin-right: 10px;" datatype="*" sucmsg=" "/><div id="loading"></div>
                           		<input class="aweform" type="hidden" id="accountNum" name="donation.accountNum"/>
                           		<input class="aweform" type="hidden" id="userId" name="donation.userId"/>
                           		<input class="aweform" type="hidden" name="donation.payMethod" value="网站"/>
                            </div>
                        </div>
                        <div class="field">
                            <label for="sex">匿名显示</label>
                            <div class="inputs">
                            	<input class="aweform" type="checkbox" id="anonymous"  onclick="changeA()"/>
                            	<input class="aweform" type="hidden" id="anonymous0" name="donation.anonymous"/>
                            </div>
                        </div>
                        <div class="field">
                            <label for="sex">性别<span class="required">*</span></label>
                            <div class="inputs">
                            	<select class="aweform" id="x_sex" name="donation.x_sex">
			                         <option value="男" selected="selected">男</option>
			                         <option value="女">女</option>
                                </select>
                            </div>
                        </div>
                        <div id="div1">
                        	<div class="field">
                            <label for="school">学校</label>
                            <div class="inputs">
                                <input class="aweform" type="text" id="school" name="donation.x_school"/>
                            </div>
                        </div>
                        <div class="field">
                            <label for="departName">院系</label>
                            <div class="inputs">
                                <input class="aweform" type="text" id="departName" name="donation.x_depart"/>
                            </div>
                        </div>
                        <div class="field">
                            <label for="gradeName">年级</label>
                            <div class="inputs">
                                <input class="aweform" type="text" id="gradeName" name="donation.x_grade"/>
                            </div>
                        </div>
                        <div class="field">
                            <label for="className">班级</label>
                            <div class="inputs">
                                <input class="aweform" type="text" id="className" name="donation.x_clazz"/>
                            </div>
                        </div>
                        <div class="field">
                            <label for="majorName">专业</label>
                            <div class="inputs">
                                <input class="aweform" type="text" id="majorName" name="donation.x_major"/>
                            </div>
                        </div>
                        </div>
                        <div style="display: none;" id="div2">
                        	<div class="field">
                            <label for="departName">院系</label>
                            <div class="inputs" id="div3">
                                
                            </div>
                        </div>
                        </div>
                        <div class="field">
                            <label for="email">电子邮箱<span class="required">*</span></label>

                            <div class="inputs">
                                <input class="aweform" type="text" id="email" name="donation.x_email" datatype="e" sucmsg=" "/>
                            </div>
                        </div>

                        <div class="field">
                            <label for="phone">联系电话 <span class="required">*</span></label>

                            <div class="inputs">
                                <input class="aweform small" type="text" id="phone" name="donation.x_phone"/>
                            </div>
                        </div>
                        <div class="field">
                            <label for="mailingAddress">联系地址</label>

                            <div class="inputs">
                                <input class="aweform small" type="text" id="mailingAddress" name="donation.x_address"/>
                            </div>
                        </div>
                        <div class="field">
                            <label for="workunit">工作单位</label>

                            <div class="inputs">
                                <input class="aweform small" type="text" id="workunit" name="donation.x_workunit"/>
                            </div>
                        </div>
                        <div class="field">
                            <label for="position">职务</label>

                            <div class="inputs">
                                <input class="aweform small" type="text" id="position" name="donation.x_position"/>
                            </div>
                        </div>

                        <div class="form-submit">
                            <button type="submit" id="btn_sub" name="submit" style="float: left">确认捐赠</button>
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
var s1;
	$(function(){
		var userId=$('#userId').val();
		if(userId.length>0){
			 $('#div1').hide();
        	 $('#div2').show();
        	 $('#div3').append(s1);
		}
	})
		
	
		$('#name').keydown(function(event){
			if(event.keyCode==8){
	        	 $("#email").val('');
	        	 $("#phone").val('');
	        	 $("#mailingAddress").val('');
	        	 $("#workunit").val('');
	        	 $("#position").val('');
	        	 $("#accountNum").val('');
	        	 $("#userId").val('');
	        	 $('#div1').show();
	        	 $('#div2').hide();
	        	 $('#s1').remove();
			}
		});
	var valid=$("#frm_contact").Validform({
		btnSubmit:"#btn_sub", 
		tiptype:4, 
		label:".label",
		showAllError:false,
		postonce:true,
		datatype:{
			"money":/^(\d+\.\d{1,2}|\d+)$/,
			"phone":/^(13[0-9]|14[5|7]|15[0-9]|18[0-9]|17[0-9])\d{8}$/i
		}
	});
	valid.addRule([
{
    ele:"#money",
    datatype:"money",
    nullmsg:"请输入捐赠金额！",
    sucmsg :" ",
    errormsg:"请输入正整数或者小数,小数点后保留1位或者2位小数！"
},
{
    ele:"#phone",
    datatype:"phone",
    nullmsg:"请输入联系方式！",
    sucmsg :" ",
    errormsg:"请输入正确的手机号码！"
}
	               ])
var cache = {};
	$( "#name" ).autocomplete({
        source: function(request, response ) {
            var term = request.term;
            if ( term in cache ) {
                response( $.map( cache[ term ], function( item ) {
                    return {
                    	 userName: item.userName,
                         fullName: item.fullName,
                         accountNum:item.accountNum,
                         userId:item.userId,
                         sex:item.sex,
                         email:item.email,
                         telId:item.telId,
                         mailingAddress:item.mailingAddress,
                         workunit:item.workunit,
                         position:item.position
                    }
                }));
                return;
            }
            $.ajax({
                url: "${pageContext.request.contextPath}/webUserInfo/webUserInfoAction!selectUserInfoByName.action",
                dataType: "json",
                data:{
                    name: request.term
                },
                success: function( data ) {
                    cache[ term ] = data;
                    response( $.map( data, function( item ) {
                        return {
                        	 userName: item.userName,
                             fullName: item.fullName,
                             accountNum:item.accountNum,
                             userId:item.userId,
                             sex:item.sex,
                             email:item.email,
                             telId:item.telId,
                             mailingAddress:item.mailingAddress,
                             workunit:item.workunit,
                             position:item.position
                        }
                    }));
                },
                beforeSend : function() {
                	$("#loading").show();
				},
				complete : function() {
					$("#loading").hide();
				}
            });
        },
        minLength: 2,
        select: function( event, ui ) {
        	 $("#name").val( ui.item.userName);
        	 $("#x_sex").val(ui.item.sex);
        	 $("#email").val(ui.item.email);
        	 $("#phone").val(ui.item.telId);
        	 $("#mailingAddress").val(ui.item.mailingAddress);
        	 $("#workunit").val(ui.item.workunit);
        	 $("#position").val(ui.item.position);
        	 $("#accountNum").val(ui.item.accountNum);
        	 $("#userId").val(ui.item.userId);
        	 if(ui.item.userId!=undefined){
        		 $('#div1').hide();
        		 $('#div2').show();
        		 s1="<span id='s1'>"+ui.item.fullName.replace(',','<br>')+"</span>";
        		 $('#div3').append("<span id='s1'>"+ui.item.fullName.replace(',','<br>')+"</span>");
        	 }
        	 return false;
        },
        focus: function( event, ui ) {
            $( "#name" ).val( ui.item.userName);
            return false;
          }
    }).data( "ui-autocomplete" )._renderItem = function( ul, item ) {
      return $( "<li>" )
        .append( "<a>" + item.userName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + item.fullName.replace(',','<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;') + "</a>" )
        .appendTo( ul );
    };
    
    function changeA(){
    	if($('#anonymous').is(':checked')){
    		$('#anonymous0').prop('value','1')
    	}else{
    		$('#anonymous0').prop('value','0')
    	}
    }
</script>
</body>
</html>