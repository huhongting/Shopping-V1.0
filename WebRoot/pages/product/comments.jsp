<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>

<c:forEach items="${comments.results}" var="comm">
<div class="pro_pl">
	<dl>
		<dd>${comm.user.name}</dd>
		<dd>购买时间</dd><dd><fmt:formatDate value="${comm.order.createDate}" /></dd>
	</dl>
	<table border="0" cellspacing="0" cellpadding="0" width="660" style="font-size: 10">
		<tbody>
			<tr>
				<td class="pro_pl_t">
					<div class="pro_pl_menu"><span style="float:left;">${comm.title}</span></div>
					<div class="pro_pl_time">发表于：<fmt:formatDate value="${comm.date}" type="both" /></div>
				</td>
			</tr>
			<tr>
				<td class="pro_pl_m">
					<div style="width:635px;overflow:auto;">${comm.content}</div>
					<div class="pro_pl_me">
						此评价对我：<a href="javascript:void(0)" onclick="newfollow(${comm.id}, true)">有用(<span id="uf">${comm.usefull}</span>)</a>
						<a href="javascript:void(0)" onclick="newfollow(${comm.id}, false)">没用(<span id="ul">${comm.useless}</span>)</a>
					</div>
				</td>
			</tr>
			<tr>
				<td class="pro_pl_b">&nbsp;</td>
			</tr>
		</tbody>
	</table>
	<div class="clear"></div>
</div>
</c:forEach>