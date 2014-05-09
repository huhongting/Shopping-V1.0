<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>
<c:forEach items="${hisBooks}" var="book">
<li class="overflow_h">·<a href="view/book/show?id=${book.id}">${book.bookName}</a></li>
</c:forEach>
