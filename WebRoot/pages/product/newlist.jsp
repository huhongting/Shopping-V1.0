<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>IShare网上二手教材书店-新书专区</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  </head>
  
  <body><a id="top"> </a>
	<%@ include file="/pages/share/menu.jsp" %>
	<!-- main start -->
	<div id="main" class="w1002 mCenter">
		<div class="go_back">
	    	<a href="javascript:void(0);" onclick="document.getElementById('top').scrollIntoView();"><div class="go_old">返回顶部</div></a>
	    </div>
    	<p>&nbsp;</p>
    	
    	<a href="index">IShare网上书店</a> &gt; 新书专区
    	<c:forEach items="${newBooks}" var="tbs">
	    <div class="mTop10 title_bg">
			<h2><span class="other fRight"><a href="view/type/list?type=${tbs.type.typeId}" target="_blank">更多>></a></span>
	            <a href="view/type/list?type=${tbs.type.typeId}&start=0&limit=15">${tbs.type.typeName}</a>
	        </h2>
	        
	        <div class="book">
	            <ul class="book_nav">
					<c:forEach items="${tbs.books.results}" var="b">
	               <li>
						<div class="book_rank">
							<div class="book_s">
								<a target="_blank" href="view/book/show?id=${b.id}">
									<img src="${b.picUrl}" border="0" width="104">
								</a>
							</div>
							<p class="mTop10 price_height">
								<a target="_blank" href="view/book/show?id=${b.id}">${b.bookName}</a>
							</p>
							<p><span class="book_price">￥${b.normalPrice}</span><span class="book_dis">￥${b.price}</span></p>
							<br>
							<p><a href="javascript:void(0);" onclick="putCar2(${b.id}, 1)">
								<img src="/resources/images/main/buy.gif" alt="加入购物车" height="24" width="104">
							</a></p>
							<br><br>
						</div>
					</li>
					</c:forEach>
	            </ul>
	            <div class="clear"></div>
	        </div>
	        <%--<div class="book_word">
	        	 <ul>
	            	<c:forEach items="${itBooks}" var="b" begin="6" end="12">
	            	<li><a target="_blank" href=" ">${b.bookName}</li>
	            	</c:forEach>
	             </ul>
	            <div class="clear"></div>
	        </div>
	    --%></div>
	    </c:forEach>
	    
	</div>
	           
	<%@ include file="/pages/share/bottom.jsp" %>

</body></html>