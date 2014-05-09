<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>

<div class="right_c">
	<h3>待评论商品</h3>
	<div class="my_order">
       	<div id="ordersinfo">
       		<div id="con_a_1" class="type_inf">
       			<table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size: 10px"><tbody>
       				<tr>
       					<th>订单号</th><th>商品名称</th><th>下单时间</th><th>操作</th>
       				</tr>
       				<c:forEach items="${comms}" var="cp">
       				<tr>
       					<td><a target="_blank" href="view/user/account/order?oid=${cp.orderId}">${cp.orderId}</a></td>
       					<td><a href="view/book/show?id=${cp.bookId}" target="_blank">${cp.bookName}</a></td>
       					<td><fmt:formatDate value="${cp.orderCreateDate}" type="both" /></td>
       					<td><a href="javascript:void(0)" onclick="showpinglun(${cp.orderId}, ${cp.bookId})">评论</a></td>
      				</tr>
      				</c:forEach>
      			</tbody></table>
      		</div>
      	</div>
    </div>
</div>

<div id="popupcontent"></div>
		