<%@page import="cn.edu.jnu.web.util.PagingUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>

<div class="search_result">
	<c:forEach items="${typeBooks.books.results}" var="b">
	<table border="0" cellpadding="0" cellspacing="0">
		<tbody><tr>
			<td valign="top" width="100"><a target="_blank" href="view/book/show?id=${b.id}"><img src="${b.picUrl}" n="-1" border="0" width="88"></a></td>
			<td valign="top">
				<ul>
					<li class="result_name">
						<a target="_blank" href="view/book/show?id=${b.id}">${b.bookName}</a>
					</li>
					<li style="font-size:10px;">${b.auther} （著）</li>
					<li style="font-size:10px;">${b.press.name} | ${b.pubTime} 出版</li>
					<li style="font-size:10px;"><fmt:formatDate value="${b.upTime}" /> 上架</li>
					<li style="font-size:10px;">销量：${b.saleCount}</li>
					<li class="result_book">
						<ul>
							<li class="book_dis"><b>￥${b.price}</b></li>
							<li class="book_price">￥${b.normalPrice}</li>
							<li class="book_opera">
								<a class="book_buy" href="javascript:putCar2(${b.id}, 1)"></a>
							</li>
						</ul>
						<div class="clear"></div>
					</li>
				</ul>
			</td>
		</tr>
	</tbody></table>
	</c:forEach>
</div>

<div class="pro_pag"> 
	<table border="0" cellpadding="0" cellspacing="0">
		<tbody><tr>
			<% PagingUtil.createPagingData(request, "typeBooks", "type"); %>
			<c:if test="${totalPage > 1}">
				<c:choose>
					<c:when test="${cur == 1}">
						<td><a href="javascript:void(0);">上一页</a></td>
					</c:when>
					<c:otherwise>
						<td><a href="javascript:void(0)" onclick="showtypelist(${pre}, ${limit})">上一页</a></td>
					</c:otherwise>
				</c:choose>
				<c:forEach begin="1" end="${totalPage}" varStatus="v">
					<c:choose>
						<c:when test="${v.count == cur}">
							<td><span class="pro_pag_on">${v.count}</span></td>
						</c:when>
						<c:otherwise>
							<td><a href="javascript:void(0)" onclick="showtypelist(${(v.count - 1) * limit}, ${limit})">${v.count}</a></td>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:choose>
					<c:when test="${cur == totalPage}">
						<td><a href="javascript:void(0);">下一页</a></td>
					</c:when>
					<c:otherwise>
						<td><a href="javascript:void(0)" onclick="showtypelist(${next}, ${limit})">下一页</a></td>
					</c:otherwise>
				</c:choose>
			</c:if>
			</tr></tbody>
	</table>
</div>