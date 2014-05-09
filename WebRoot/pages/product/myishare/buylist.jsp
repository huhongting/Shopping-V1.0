<%@page import="cn.edu.jnu.web.util.PagingUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>

<div class="right_c">
	<h3>已购商品</h3>
	<div class="my_order">
       	<div id="ordersinfo">
       		<div id="con_a_1" class="type_inf">
       			<table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size: 10px"><tbody>
       				<tr>
       					<th>商品名称</th><th>商品数量</th><th>商品价格</th><th>订单号</th><th>购买时间</th>
       				</tr>
       				<c:forEach items="${items.results}" var="item">
       				<tr>
       					<td><a target="_blank" href="view/book/show?id=${item.productid}">${item.productName}</a></td>
       					<td>${item.amount}</td>
       					<td><fmt:formatNumber value="${item.productPrice}" minFractionDigits="2" /></td>
       					<td>${item.order.orderid}</td>
       					<td><fmt:formatDate value="${item.order.createDate}" type="both" /></td>
           				</tr>
           				</c:forEach>
           			</tbody></table>
           		</div>
           	</div>
         </div>
     </div>
     <div class="pro_pag"> 
		<table border="0" cellpadding="0" cellspacing="0">
			<tbody><tr>
				<% PagingUtil.getPagingData(request, "items"); %>
		<c:if test="${totalPage > 1}">
		<c:choose>
			<c:when test="${cur == 1}">
			<td><a href="javascript:void(0);">上一页</a></td>
		</c:when>
		<c:otherwise>
			<td><a href="javascript:void(0)" onclick="showpanel('view/user/account/buylist?start=${pre}&limit=${limit}')">上一页</a></td>
			</c:otherwise>
		</c:choose>
		<c:forEach begin="1" end="${totalPage}" varStatus="v">
		<c:choose>
			<c:when test="${v.count == cur}">
			<td><span class="pro_pag_on">${v.count}</span></td>
		</c:when>
		<c:otherwise>
			<td><a href="javascript:void(0)" onclick="showpanel('view/user/account/buylist?start=${(v.count - 1) * limit}&limit=${limit}')">${v.count}</a></td>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:choose>
			<c:when test="${cur == totalPage}">
			<td><a href="javascript:void(0);">下一页</a></td>
		</c:when>
		<c:otherwise>
			<td><a href="javascript:void(0)" onclick="showpanel('view/user/account/buylist?start=${next}&limit=${limit}')">下一页</a></td>
						</c:otherwise>
					</c:choose>
				</c:if>
				</tr></tbody>
		</table>
</div>
		