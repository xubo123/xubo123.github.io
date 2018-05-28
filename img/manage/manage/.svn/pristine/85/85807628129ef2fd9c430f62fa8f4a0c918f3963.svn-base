<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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

KindEditor.ready(function(K) {
	K.create('#content',{
		 fontSizeTable:['9px', '10px', '11px', '12px', '13px', '14px', '15px', '16px', '17px', '18px', '19px', '20px', '22px', '24px', '28px', '32px'],
    	 uploadJson : '${pageContext.request.contextPath}/fileUpload/fileUploadAction!doNotNeedSecurity_fileUpload.action',
    	 readonlyMode : true,
         afterChange:function(){
	        	this.sync();
	        }
    });
});

</script>
</head>
  
  <body>
<form method="post" id="viewNewsForm">
	<fieldset>
		<legend>
			新闻基本信息
		</legend>
		<table class="ta001">
			<tr>
				<th>
					标题
				</th>
				<td colspan="3">
					<input name="news2.newsId" type="hidden" id="newsId" 
							value="${news2.newsId}">
					<input name="news2.title" class="easyui-validatebox" disabled="disabled"
						style="width: 700px;"
						data-options="required:true,validType:'customRequired'"
						maxlength="30" value="${news2.title}"/>
				</td>
			</tr>
			<tr>
				<th>
					新闻简介
				</th>
				<td colspan="3">
					<textarea id="introduction" rows="7" cols="100" disabled="disabled"
						name="news2.introduction">${news2.introduction}</textarea>
				</td>
			</tr>
			<tr>
				<th>
					网页链接
				</th>
				<td colspan="3">
					<textarea id="newsUrl" rows="3" cols="100" name="news2.newsUrl" disabled="disabled">${news2.newsUrl}</textarea>
				</td>
			</tr>
			<tr>
				<th>
					频道
				</th>
				<td colspan="3">
					<input name="news2.tagName" class="easyui-combobox" style="width: 150px;" value="${news2.tagName}" disabled="disabled"
						data-options="editable:false,required:true,
							        valueField: 'channelName',
							        textField: 'channelName',
							        url: '${pageContext.request.contextPath}/newsChannel/newsChannelAction!doNotNeedSecurity_initType.action'" />
				</td>
			</tr>
		<%-- 	<tr>
				<th>
					兴趣标签
				</th>
				<td colspan="3">
					${news2.type}
					<input name="news.type" class="easyui-combobox"
						data-options="editable:false,required:true,
							        valueField: 'value',
							        textField: 'value',
							        url: '${pageContext.request.contextPath}/news/newsAction!doNotNeedSecurity_initType.action'" />
				</td>
			</tr> --%>
			
			
			
			<tr>
					<th>
						栏目
					</th>
					<td colspan="3">
						${news2.categoryName}
					</td>
			</tr>
		<%-- 	<tr>
					<th>
						网页新闻栏目
					</th>
					<td colspan="3">
						${news2.categoryWebName}
					</td>
			</tr> --%>
	<%-- 		<c:if test="${news2.origin==1 || news2.originP==1 || news2.originWeb==1 || news2.originWebP==1}">
			<tr>
					<th>
						所属院系
					</th>
					<td colspan="3">
						${news.dept_name}
					</td>
			</tr>
			</c:if> --%>
			
<%-- 			<c:if test="${news2.cityName!=null && news2.cityName!=''}">
			<tr>
				<th>所属地区</th>
				<td colspan="3">
					${news2.cityName}
				</td>
			</tr>
			</c:if> --%>
			
			<tr>
				<th>
					时间
				</th>
				<td colspan="3">
					<input name="news2.createTime" class="easyui-datetimebox" style="width: 150px;" data-options="editable:false"  disabled="disabled" value="${createTime}" />
				</td>
			</tr>
			
			<tr>
				<th>
					新闻内容
				</th>
				<td colspan="3">
					<textarea id="content" name="news2.content"
						style="width: 700px; height: 300px;" disabled="disabled">${news2.content}</textarea>
				</td>
			</tr>
			
			<tr>
				<th>
					新闻封面图片
				</th>
				<td colspan="3">
					<div id="newsPic">
						<c:if test="${news2.pic!=null and news2.pic!=''}">
							<div style="float:left;width:180px;"><img src="${news2.picUrl}" width="150px" height="150px"/><div class="bb001"></div><input type="hidden" name="news2.pic" value="${news2.pic}"/></div>
						</c:if>
					</div>
				</td>
			</tr>
		</table>
	</fieldset>
</form>
  </body>
</html>