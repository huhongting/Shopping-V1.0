<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.edu.jnu.web.view.dao.TypeBookList,
				 cn.edu.jnu.web.entity.BookType,
				 cn.edu.jnu.web.entity.Book" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>IShare网上书店-网上买书的网站,网购图书商城</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  
  </head>
  
  <body><a id="top"> </a>
	<%@ include file="/pages/share/head.jsp" %>
	<!-- main start -->
	<div id="main" class="w1002 mCenter">
		<div class="go_back">
	    	<a href="javascript:void(0);" onclick="document.getElementById('top').scrollIntoView();"><div class="go_old">返回顶部</div></a>
	    </div>
    	<p>&nbsp;</p>
    	
    	<a href="index">IShare网上书店</a> > 新书专区
    	<c:forEach items="${newBooks}" var="tbs">
	    <div class="mTop10 title_bg">
			<h2><span class="other fRight"><a href=" " target="_blank">更多>></a></span>
	            <a href=" ">${tbs.type.typeName}</a>
	        </h2>
	        
	        <div class="book">
	            <ul class="book_nav">
					<c:forEach items="${tbs.books}" var="b">
	               <li>
						<div class="book_rank">
							<div class="book_s">
								<a target="_blank" href="view/book/show?id=${b.id}">
									<img src="${b.picUrl}" border="0" width="104">
								</a>
							</div>
							<p class="mTop10 price_height">
								<a target="_blank" href=" ">${b.bookName}</a>
							</p>
							<p><span class="book_price">￥${b.normalPrice}</span><span class="book_dis">￥${b.price}</span></p>
							<br>
							<p><a href="javascript:void(0);" onclick="putCar2(${b.id}, 1)">
								<img src="images/main/buy.gif" alt="加入购物车" height="24" width="104">
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
	           
	<!-- bottom start -->
	<div id="bottom">
		<div class="w1002 mCenter bottom_c">
	    	<div class="bottom_box" style="_width:160px;">
	        	<p class="cp_phone"><strong>售后服务电话</strong></p>
	            <p class="pOrange"><strong>400 098 9991</strong></p>
	            <ul class="cp_work">
	            	<li>工作时间：</li>
	                <li>7:30 - 19:30（周一至周五）</li>
	                <li>9:00 - 18:00（周六、周日）</li>
	            </ul>
	        </div>
	        <div class="bottom_box">
	        	<p class="cp_phone"><strong>新手指南</strong></p>
	            <ul class="cp_intr">
	            	<li><a rel="nofollow" target="_blank" href=" ">购物流程</a></li>
	                <li><a rel="nofollow" target="_blank" href=" ">会员介绍</a></li>
	                <li><a rel="nofollow" target="_blank" href=" ">互动联盟</a></li>
	                <li><a rel="nofollow" target="_blank" href=" ">联系我们</a></li>
	                <li><a rel="nofollow" target="_blank" href=" ">常见问题</a></li>
	            </ul>
	        </div>
	        <div class="bottom_box">
	        	<p class="cp_phone"><strong>付款方式</strong></p>
	            <ul class="cp_intr">
	            	<li><a rel="nofollow" target="_blank" href=" ">网上支付</a></li>
	                <li><a rel="nofollow" target="_blank" href=" ">货到付款</a></li>
	                <li><a rel="nofollow" target="_blank" href=" ">银行汇款</a></li>
	                <li><a rel="nofollow" target="_blank" href=" ">邮政汇款</a></li>
	                <li><a rel="nofollow" target="_blank" href=" ">优惠券支付</a></li>
	            </ul>
	        </div>
	        <div class="bottom_box">
	        	<p class="cp_phone"><strong>订单处理配送</strong></p>
	            <ul class="cp_intr">
	            	<li><a target="_blank" href=" " rel="nofollow">订单处理方式</a></li>
	                <li><a target="_blank" href=" " rel="nofollow">订单有效期</a></li>
	                <li><a target="_blank" href=" " rel="nofollow">缺货说明</a></li>
	                <li><a target="_blank" href=" " rel="nofollow">配送方式</a></li>
	            </ul>
	        </div>
	        <div class="bottom_box">
	        	<p class="cp_phone"><strong>售后服务</strong></p>
	            <ul class="cp_intr">
	            	<li><a target="_blank" rel="nofollow" href=" ">退换货政策</a></li>
	                <li><a target="_blank" rel="nofollow" href=" ">退换货方法</a></li>
	                <li><a target="_blank" rel="nofollow" href=" ">发票说明</a></li>
	            </ul>
	        </div>
	        <div class="bottom_box">
	        	<p class="cp_phone"><strong>特色服务</strong></p>
	            <ul class="cp_intr">
	            	<li><a target="_blank" href=" ">购书卡</a></li>
	                <li><a target="_blank" rel="nofollow" href=" ">团购服务</a></li>
	                <li><a target="_blank" rel="nofollow" href=" ">在线试读</a></li>
	                <li><a target="_blank" href=" ">视频教程</a></li>
	               
	            </ul>
	        </div>
	        <div class="bottom_box border_r_none">
	        	<p class="cp_phone"><strong>关于我们</strong></p>
	            <ul class="cp_intr">
	                <li><a target="_blank" rel="nofollow" href=" ">关于我们</a></li>
					<li><a target="_blank" rel="nofollow" href=" ">联系我们</a></li>
	            </ul>
	        </div>
	        <div class="clear"></div>
	    </div>
	</div>

</body></html>