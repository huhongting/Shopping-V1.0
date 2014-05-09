<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>我的订单-${order.orderid}</title>
	<link rel="stylesheet" type="text/css" href="/resources/css/my_cp.css">
  </head>

<body>
<%@ include file="/pages/share/menu2.jsp" %>
<div id="main">
	<div class="cart_box">
       <div class="flow_order_list">
            <table border="0" cellpadding="0" cellspacing="0" style="font-size: 10px">
                <tr>
                	<td class="con_title">
                		订单信息 [${order.orderid}] 
                		[<font color=red>${order.state.name}</font>]
                		<c:if test='${order.state.name=="已发货"}'><a href="">确认收货</a></c:if>
                		<c:if test='${order.state.name=="等待付款"}'>
                			<c:if test='${order.paymentWay.name=="在线支付"}'><a href="">去支付</a></c:if>
                		</c:if>
                	</td>
                </tr>
                <tr>
                	<td>
                    	<div class="pro_list">
                        	<dl><dt>收货人信息</dt>
                                <dd>
                                	<span>${order.orderDeliverInfo.user.name}</span>
                                    <span>${order.orderDeliverInfo.address}</span>
                                    <span>${order.orderDeliverInfo.mobile}&nbsp;&nbsp;</span>
                                    <span>${order.orderDeliverInfo.email}</span>
                                </dd>
                                <dt>送货信息</dt>
                                <dd>
                                	<span>${order.deliverWay.name}</span>
                                </dd>
                                <dt>支付方式</dt>
                                <dd><span>${order.paymentWay.name}</span></dd>
                                <dt>发票</dt>
                                <dd>
                                	<span>${order.fapiao}</span>
                                </dd>
                                <dt>附言</dt>
                                <dd>
                                	<span>${order.note}</span>
                                </dd>
	                            <dt>商品清单</dt>
                                <dd>
                                	<table border="0" cellpadding="0" cellspacing="0" style="font-size: 10px">
                                        <tr>
                                            <th>商品名称</th>
                                            <th>优惠价</th>
                                            <th>数量</th>
                                            <th>小计</th>
                                            <th>状态</th>
                                        </tr>
                                        <c:forEach items="${order.items}" var="item">
                                        <tr>
                                            <td><a target="_blank" href="view/book/show?id=${item.productid}">${item.productName}</a></td>
                                            <td>￥<fmt:formatNumber value="${item.productPrice}" minFractionDigits="2" /></td>
                                            <td>${item.amount}</td>
                                            <td>￥<fmt:formatNumber value="${item.productPrice * item.amount}" minFractionDigits="2" /></td>
                                            <td><font color=#cc0000>${order.state.name}</font></td>
                                        </tr>
                                        </c:forEach>
                                     </table>
	                            </dd>
                                <dt>结算信息</dt>
                                <dd>
                                	<div style="text-align:right;padding:0 75 0 0">使用电子货币支付：￥<fmt:formatNumber value="${order.epay}" minFractionDigits="2" /></div>
                                	<div class="checkout_list">
                                		<table border="0" cellpadding="0" cellspacing="0"  style='_margin-right:30px;font-size: 10px'>
                                    		<tr>
                                        		<td class="check_l_t" style="height:10px;"></td>
                                    		</tr>
                                    		<tr>
                                        		<td class="check_l_c">
                                            		<div class="check_result">
                                            			商品金额总计：￥<fmt:formatNumber value="${order.productTotalPrice}" minFractionDigits="2" /><br/>
                                            			运费总计：￥<fmt:formatNumber value="${order.deliverWay.price}" minFractionDigits="2" />
                                            		</div>
                                        		</td>
                                    		</tr>
		                                    <tr>
		                                        <td class="check_l_b"></td>
		                                    </tr>
                                		</table>
                                		<div class="clear"></div>
                                		<h2  style='_padding-right:60px'>应付总计：<span>￥<fmt:formatNumber value="${order.totalPrice}" minFractionDigits="2" /></span></h2>
                            		</div>
                                </dd>
                          </dl>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
<%@ include file="/pages/share/bottom.jsp" %>
</body></html>