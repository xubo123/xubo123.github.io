<%@page import="com.hxy.system.SpringManager"%>
<%@ page language="java" import="java.util.*,com.hxy.util.WebUtil" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="com.hxy.web.news.service.WebNewsService"%>
<%@page import="com.hxy.web.news.entity.WebNewsType"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/web/";
WebNewsType webNewsType = new WebNewsType();
String webOrigin = request.getParameter("webNewsType.origin");
String webCityProName = request.getParameter("webNewsType.cityName");
String categoryWeb = request.getParameter("categoryWeb");
String strWebCityProName = "";
String name = request.getParameter("name");
if(name!=null&&name.length()>0&&name.equals("捐赠")){
	WebNewsService webNewsService = SpringManager.getBean("webNewsService", WebNewsService.class);
	List<WebNewsType> list = webNewsService.getByName(name);
	if(list!=null&&list.size()>0){
		categoryWeb = String.valueOf(list.get(0).getId());
	}
	
}

if(WebUtil.isEmpty(categoryWeb))
{
	categoryWeb = "0";
}

if(WebUtil.isEmpty(webOrigin))
{
	webOrigin = "1";
}

int intWebOrigin = 1;
try{
	intWebOrigin = Integer.parseInt(webOrigin);
}catch(Exception e){
	intWebOrigin = 1;
}

if(WebUtil.isEmpty(webCityProName))
{
	strWebCityProName = "校友总会";
	webCityProName = "";
}else{
	if(webCityProName.indexOf(" ")!=-1){
		strWebCityProName = webCityProName.replace(" ", "") + "校友会";	
	}else{
		strWebCityProName = webCityProName + "校友会";
	}
	
}

webNewsType.setOrigin(intWebOrigin);
webNewsType.setCityName(webCityProName);

WebNewsService webSer = SpringManager.getBean("webNewsService", WebNewsService.class);

List<WebNewsType> listNewsType = webSer.getWebNewsTypeList(webNewsType);

WebNewsType webNewsTypeTmp = new WebNewsType();
webNewsTypeTmp.setId(Long.parseLong(categoryWeb));
List<WebNewsType> newsTypeParentsList = webSer.getWebParentsByTypeId(webNewsTypeTmp);

long parentIdNavTmp = 0;
String currentTypeName = "";
if(!WebUtil.isEmpty(newsTypeParentsList))
{
	if(newsTypeParentsList.size() != 0)
	{
		WebNewsType type = newsTypeParentsList.get(newsTypeParentsList.size() - 1);
	
		parentIdNavTmp = type.getId();
		
		currentTypeName = type.getName();
	}else{
		parentIdNavTmp = Long.parseLong(categoryWeb);
	}
	
}else{
	parentIdNavTmp = Long.parseLong(categoryWeb);
}


request.setAttribute("listNewsType", listNewsType);
request.setAttribute("intWebOrigin", intWebOrigin);
request.setAttribute("parentIdNavTmp", parentIdNavTmp);
request.setAttribute("basePath", basePath);
request.setAttribute("webCityProName", webCityProName);

String chooseTopPageUrl = "";

if(intWebOrigin==1){
	chooseTopPageUrl =basePath+"index.jsp";
}else if(intWebOrigin==2){
	chooseTopPageUrl ="webNewsAction!getWebSubAlumniPage.action?cityName="+webCityProName;
}

%>


<header class="header">
    <div class="container">
        <div class="logos columns">
            <h1 class="logo" style="background-image: url(<%=basePath %>images/logo.png)"><a href="<%=chooseTopPageUrl%>"><%=strWebCityProName %></a></h1>
        </div>

        <div class="eleven columns">
            <nav class="main_menu">
				
                <ul>
                
			    <li <c:if test="${parentIdNavTmp==0}">class="current_page_item"</c:if> >
		            <a href="<%=chooseTopPageUrl%>">
		               首页
		            </a>
		        </li>
		        
		        <c:if test="${intWebOrigin==1}">
		        <li <c:if test="${parentIdNavTmp==-1}">class="current_page_item"</c:if>>
		            <a href="webNewsAction!getWebOfChooseCity.action">
		               校友分会
		            </a>
		        </li>
		        </c:if>
                
                <c:if test="${listNewsType!=null}">
                   <c:set var="tmpTypeUrl" value=""/>
		           
	               <c:forEach var="type" items="${listNewsType}">
		             <li <c:if test="${parentIdNavTmp==type.id}">class="current_page_item"</c:if> >
		               
		               <c:choose> 
						    <c:when test="${type.type==1}">
						       <c:set var="tmpTypeUrl" value="${basePath}newsList.jsp?categoryWeb=${type.id}&origin=${intWebOrigin}&cityName=${webCityProName}"/>
						    </c:when>
						    <c:when test="${type.type==2}">
						        <c:set var="tmpTypeUrl" value="${type.url }"/>
						    </c:when>
						    <c:when test="${type.type==3}">
						        <c:set var="tmpTypeUrl" value="${basePath}singlePageDetail.jsp?typeId=${type.id }&origin=${intWebOrigin}&cityName=${webCityProName}"/>
						    </c:when>
						    <c:otherwise>
						    	<c:set var="tmpTypeUrl" value="#"/>
						    </c:otherwise>
						</c:choose>
						
					   <%-- <c:set var="tmpTypeUrl" value="${fn:replace(tmpTypeUrl,'http://','')}"/>
					   <c:set var="tmpTypeUrl" value="${fn:replace(tmpTypeUrl,'https://','')}"/>
					   <c:set var="tmpTypeUrl" value="http://${tmpTypeUrl}"/> --%>
		               
		               <c:choose>   
		               <c:when test="${type.webNewsType!=null and type.webNewsType.size()>0}">  
					   
					   
						 <a href="#">
			                ${type.name }
			             </a>
					     <ul>
							<c:forEach var="type2" items="${type.webNewsType}">
							    
								<c:choose> 
								    <c:when test="${type2.type==1}">
								       <c:set var="tmpTypeUrl" value="${basePath}newsList.jsp?categoryWeb=${type2.id}&origin=${intWebOrigin}&cityName=${webCityProName}"/>
								    </c:when>
								    <c:when test="${type2.type==2}">
								        <c:set var="tmpTypeUrl" value="${type2.url }"/>
								    </c:when>
								    <c:when test="${type2.type==3}">
								        <c:set var="tmpTypeUrl" value="${basePath}singlePageDetail.jsp?typeId=${type2.id }&origin=${intWebOrigin}&cityName=${webCityProName}"/>
								    </c:when>
								    <c:otherwise>
								    	<c:set var="tmpTypeUrl" value="#"/>
								    </c:otherwise>
								</c:choose>
							
							  <li><a href="${tmpTypeUrl }">${type2.name }</a></li>
							</c:forEach>
						 </ul>		
					   </c:when>
					   <c:otherwise>
					     <a href="${tmpTypeUrl }">
			               ${type.name }
			             </a>
					   </c:otherwise>
					   </c:choose>
					   
					  </li>
					</c:forEach>
                </c:if>
                
                <c:if test="${intWebOrigin==2}">
                	<!-- 分会网站最后一个栏目是返回总会 -->
			        <li>
		               <a href="<%=basePath%>index.jsp">
		                  返回总会
		               </a>
		            </li>
                </c:if>
                
                </ul>

            </nav>
        </div>
        <div class="clearfix"></div>
    </div>
</header>
