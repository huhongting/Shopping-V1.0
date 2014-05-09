<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>

<c:forEach items="${books}" var="b" varStatus="vs">
	<c:choose>
		<c:when test="${vs.count == 1}">
			<li class="rank_${vs.count}">
				<div class="toplistimg" id="cxpic_${vs.count}" style="padding:0 5 0 0">
					<a target="_blank" href="view/book/show?id=${b.id}">
						<img src="${b.picUrl}" border="0" width="60" height="83">
					</a>
				</div>
				<a target="_blank" href="view/book/show?id=${b.id}">${b.bookName}</a>
			</li>
		</c:when>
		<c:otherwise>
			<li class="rank_${vs.count} overflow_h" onmouseover="cxs(${vs.count}, '')" onmouseout="cxs(${vs.count}, 'none')">
				<div class="toplistimg" style="display: none;padding:0 5 0 0;" id="cxpic_${vs.count}">
					<a target="_blank" href="view/book/show?id=${b.id}">
						<img src="${b.picUrl}" border="0" width="60" height="83">
					</a>
				</div>
				<a target="_blank" href="view/book/show?id=${b.id}">${b.bookName}</a>
			</li>
		</c:otherwise>
	</c:choose>
</c:forEach>
