<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>
<c:forEach items="${delivers}" var="deli">
<dd>
	<span class="address_pep">
		<label>
			<input name="addrid" value="${deli.id}" checked="checked" type="radio">
			${user.name}
		</label>
	</span>
	<span class="address_name">${deli.address}</span>
	<span class="address_number">${deli.postalcode}</span>
	<span class="addrees_tel">${deli.mobile}</span>
	<span class="address_mail">${deli.email}</span>
	<span class="address_edit">
		<a class="mLeft10" href="javascript:void(0)" onclick="del_t(${deli.id})">[删除]</a>
	</span>
	<div class="clear"></div>
</dd>
</c:forEach>
