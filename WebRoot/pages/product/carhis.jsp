<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>
<c:forEach items="${hisBooks}" var="book">
<li>
   	<div class="book_rank">
   		<p class="book_img" style="height:154px;">
        	<a target="_blank" href="view/book/show?id=${book.id}">
        		<img src="${book.picUrl}" style="height: 154px;" n="-1" s="1">
        	</a>
       	</p>
       	<p class="mTop10 price_height">
       		<a target="_blank" href="view/book/show?id=${book.id}">${book.bookName}</a>
       	</p>
       	<p>
       		<span class="book_price">￥${book.normalPrice}</span>
       		<span class="book_dis">￥${book.price}</span>
       	</p>
       	<a class="flow_add_cart" href="javascript:void(0)" onclick="buy(${book.id});"></a>
   	</div>
</li>
</c:forEach>
