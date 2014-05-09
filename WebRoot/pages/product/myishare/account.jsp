<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>我的IShare</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="/resources/css/product.css">
  </head>
  
  <body>
    <%@ include file="/pages/share/menu2.jsp" %>
    
    <div id="main"> 
        <div id="right">
        	<div class="my_right">
	        	<div class="my_infor">
	            	<table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size: 10px">
	                	<tbody><tr>
	                        <td class="infor" valign="top">
	                        	<div><b>${user.name}</b></div>
	                            <p class="member_ver">
	                                <span>注册时间：<fmt:formatDate value="${user.regTime}" type="both"/></span>
	                                <span>注册邮箱：${user.email}</span>
	                            </p>
	                            <div class="clear"></div>
	                            <p></p>
	                            <p class="order_rem">
	                                <b>订单提醒：待评价商品<a href="javascript:showpanel('view/user/account/waitcommentlist')"><b>(${reccount})</b></a><br><br>
	                                	账户余额：<a href="javascript:showpanel('view/user/account/emoney')"><b>￥<fmt:formatNumber value="${user.account.money}" minFractionDigits="2" /></b></a>
	                            </p>
	                        </td>
	                    </tr>
	                </tbody></table>
	            </div>
        	</div>
		</div>
    	<%@ include file="/pages/product/myishare/accountmenu.jsp" %>
     	<div class="clear"></div>
    </div>
    
    <%@ include file="/pages/share/bottom.jsp" %>
    
  </body>
</html>
