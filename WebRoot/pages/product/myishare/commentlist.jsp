<%@page import="cn.edu.jnu.web.util.PagingUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>
<div class="right_c">
	<h3>我的评论</h3>
	<div class="my_order">
       	<div id="ordersinfo">
       		<div id="con_a_1" class="type_inf">
       			<c:forEach items="${comments.results}" var="mc">
       			<div class="pro_pl">
					<table border="0" cellspacing="0" cellpadding="0" width="660" style="font-size: 10">
						<tbody>
							<tr>
								<td style="text-align:left;padding:0 0 0 10">
									<c:if test="${mc.delflag==true}"><font color=red>[此评论已被管理员删除]&nbsp;&nbsp;</font></c:if><b>订单号：</b>${mc.order.orderid}&nbsp;&nbsp;<b>商品名：</b>${mc.book.bookName}&nbsp;&nbsp;<b>购买于：</b><fmt:formatDate value="${mc.date}" type="both" />
								</td>
							</tr>
							<tr>
								<td class="pro_pl_m">
									<div style="width:635px;overflow:auto;text-align:left;padding:0 0 0 10"><b>评论标题：</b>${mc.title}&nbsp;&nbsp;<b>发表于：</b><fmt:formatDate value="${mc.date}" type="both" /><br>${mc.content}</div>
									<div class="pro_pl_me" style="text-align:right;padding: 0 10 0 0">有用(${mc.usefull})&nbsp;&nbsp;没用(${mc.useless})
									</div>
									<div class="clear"></div>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="clear"></div>
				</div>
				</c:forEach>
           		</div>
           	</div>
         </div>
     </div>
     <div class="pro_pag"> 
		<table border="0" cellpadding="0" cellspacing="0">
			<tbody><tr>
				<% PagingUtil.getPagingData(request, "comments"); %>
		<c:if test="${totalPage > 1}">
		<c:choose>
			<c:when test="${cur == 1}">
			<td><a href="javascript:void(0);">上一页</a></td>
		</c:when>
		<c:otherwise>
			<td><a href="javascript:void(0)" onclick="showpanel('view/user/account/mycommentlist?start=${pre}&limit=${limit}')">上一页</a></td>
			</c:otherwise>
		</c:choose>
		<c:forEach begin="1" end="${totalPage}" varStatus="v">
		<c:choose>
			<c:when test="${v.count == cur}">
			<td><span class="pro_pag_on">${v.count}</span></td>
		</c:when>
		<c:otherwise>
			<td><a href="javascript:void(0)" onclick="showpanel('view/user/account/mycommentlist?start=${(v.count - 1) * limit}&limit=${limit}')">${v.count}</a></td>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:choose>
			<c:when test="${cur == totalPage}">
			<td><a href="javascript:void(0);">下一页</a></td>
		</c:when>
		<c:otherwise>
			<td><a href="javascript:void(0)" onclick="showpanel('view/user/account/mycommentlist?start=${next}&limit=${limit}')">下一页</a></td>
						</c:otherwise>
					</c:choose>
				</c:if>
				</tr></tbody>
		</table>
</div>
		