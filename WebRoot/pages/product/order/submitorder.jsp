<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>成功提交订单</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="/resources/js/shoppingcar.js"></script>
  </head>
  
  <body>
  <%@ include file="/pages/share/menu.jsp" %>
  <link rel="stylesheet" type="text/css" href="/resources/css/dingdan.css">
  <link rel="stylesheet" type="text/css" href="/resources/css/my_cp.css">

    <div>
	    <div id="tipdiv" class="box_window" style="top: 0px; left: 0px; display: none"></div>
	    <div id="tipdiv3" class="box_window" style="top: 0px; left: 0px; display: none"></div>
	    <div id="main">
	    	<div class="cp_logo fLeft"></div>
    		<div class="clear"></div>
	        <div class="cart_box">
	            <div class="flow_step">
	                <ul class="cols_3">
	                    <li class="step_1">1.我的购物车</li>
	                    <li class="step_2">2.确认订单信息</li>
	                    <li class="step_3">3.成功提交订单</li>
					</ul>
	            </div>
	            <div class="flow_step_title">
	                <h2 id="mycar">成功提交订单</h2>
	                <label id="tipTag" style="float: left"></label>
	                <div class="clear"></div>
	            </div>
				<div id="cartbook" class="flow_pro_list">
					<table id="tb" border="0" cellpadding="0" cellspacing="0"><tbody>
						<tr id="nbookstip">
							<td colspan="8">
								<div class="flow_cart">
									<p>您的订单已成功提交</p>
									<p><a href="index">继续购物</a>&nbsp;&nbsp;<a href="view/user/account">查看订单</a></p>
								</div>
							</td>
						</tr>
					</tbody></table>
				</div>
	            <div class="clear"></div>
				
	            <div class="flow_cart_l">
	                <div class="flow_cart_nav">
	                    <a class="onhover" id="a2">最新浏览</a>
	                    <div class="clear"></div>
	                </div>
	                <div id="con_a_2" style="overflow:hidden;height:240px;" class="flow_show_b">
                		<ul id="bookhis" class="book_nav"></ul>
	                	<div class="clear"></div>
	                </div>
	                <script type="text/javascript">
	                	document.getElementById('bookhis').innerHTML = '<img src="/resources/images/load.gif" style="width:14;height:14">&nbsp;数据正在加载...';
	                	sendRequest('view/product/carhis', function () {
	                		document.getElementById('bookhis').innerHTML = XMLHttpReq.responseText;
	                	});
	                </script>
	            </div>
	        </div>
	    </div>
	    <div id="addone"></div>
    </div>
    
    <script type="text/javascript">
    	document.getElementById('dh').style.display = 'none';
    	document.getElementById('gw_sh').style.display = 'none';
    </script>
    
    <%@ include file="/pages/share/bottom.jsp" %>
    
  </body>
</html>
