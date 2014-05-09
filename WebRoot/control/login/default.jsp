<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ include file="/pages/share/taglib.jsp" %>
<%
String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
String basePath = path+request.getContextPath()+"/";
String extPath = path + "/ext.4.2/";
pageContext.setAttribute("extpath", extPath);
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>后台管理系统登入平台</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<style type="text/css">
		.key {
			background-image: url(${extpath}/examples/shared/icons/fam/user_suit.png);
		}
	</style>
	
	<link rel="stylesheet" type="text/css" href="${extpath}resources/css/ext-all.css">
	<script type="text/javascript" src="${extpath}bootstrap.js"></script>
	<script type="text/javascript" src="/resources/control/login/js/app.js" charset="gbk"></script>
  </head>
  
  <body oncontextmenu="return false">
    
    <div id="info" class="x-hide-display">
    	<c:forEach items="${notes.results}" var="note">
	    	<h4 style="text-align:center;">${note.title}</h4>
	    	<p style="text-align:center;"><fmt:formatDate value="${note.createDate}" type="both" /></p>
	    	${note.content}
    	</c:forEach>
    </div>
    <div id="about" class="x-hide-display">
    	<h4>高校二手教材交易网管理平台 V0.1</h4>
    	版权所有 (C) 2014 Huht (JNU.EDU.CN)<br><br>
    	<b>技术支持</b><br><br>
    	QQ：1151695736&nbsp;&nbsp; 
    	Email：huht@live.cn<br><br>
    	访问官方网站：<a href="/index" target="_blank" title="点此访问网站主页">www.ishare.com</a>
    </div>
    
  </body>
</html>
