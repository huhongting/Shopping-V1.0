<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/pages/share/taglib.jsp" %>

<div class="right_c">
	<h3>地址管理</h3>
	<div class="my_order">
		<div id="myinerhtml">
           	<table border="0" cellpadding="0" cellspacing="0" class="address_list" style="font-size: 10px">
           		<c:forEach items="${address}" var="addr">
              		<tr>
           			<td colspan="3">${addr.name}</td>
                   	<td>${addr.address} &nbsp;&nbsp;${addr.postalcode}</td>
                   	<td>${addr.email}</td>
                   	<td><a href="javascript:del_t(${addr.id});">删除</a></td>
           		</tr>
           		</c:forEach>
       		</table>     
        </div>
        <pre class="prettyprint" id="demo_animate_1"></pre>
       	<a class="pay_hurry" name="demo_animate_1" onclick="add()">新增地址</a>
       	<div id="addnewaddr" style="padding-left: 100; display:none">
			<input type="hidden" id="uid" value="${user.id}">
			姓名:<input type="text" id="name">&nbsp;<font color=red>[不能为空]</font><br>
			固话:<input type="text" id="tell"><br>
			手机:<input type="text" id="mobile">&nbsp;<font color=red>[不能为空,长度为11]</font><br>
			邮箱:<input type="text" id="email">&nbsp;<font color=red>[不能为空]</font><br>
			邮编:<input type="text" id="post">&nbsp;<font color=red>[不能为空,长度为6]</font><br>
			地址:<input type="text" id="addr">&nbsp;<font color=red>[不能为空]</font><br>
			<input type="button" value="确定" onclick="submit()">
			<input type="button" value="取消" onclick="cancel()">
		</div>
   </div>
</div>
	