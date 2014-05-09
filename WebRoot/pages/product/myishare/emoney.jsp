<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>
<div class="right_c">
	<h3>账户余额</h3>
	<div class="my_order">
		<p class="my_point">
       		当前账户余额：<span class="book_dis_five" id="ye1">￥<fmt:formatNumber value="${user.account.money}" minFractionDigits="2" /></span><br />
        </p>
        <div class="book_type">消费明细</div>
        <div id="dataList" class="type_inf">
        	<table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size: 10px"><tbody>
      				<tr>
      					<th>账户操作</th><th>消费额</th><th>操作时间</th><th>备注</th>
      				</tr>
      				<c:forEach items="${records.results}" var="r">
      				<tr>
      					<td>${r.cmd}</td>
      					<td><fmt:formatNumber value="${r.money}" minFractionDigits="2" /></td>
      					<td><fmt:formatDate value="${r.time}" type="both" /></td>
      					<td>${r.remark}</td>
     				</tr>
     				</c:forEach>
     			</tbody></table>
        </div>
        <div class="clear"></div>
    </div>
</div>
		